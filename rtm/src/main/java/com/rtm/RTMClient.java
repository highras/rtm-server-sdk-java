package com.rtm;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.lang.StringBuilder;
import java.util.*;

import com.fpnn.*;
import com.fpnn.callback.CallbackData;
import com.fpnn.callback.FPCallback;
import com.fpnn.encryptor.FPEncryptor;
import com.fpnn.event.EventData;
import com.fpnn.event.FPEvent;
import com.rtm.json.JsonHelper;
import com.rtm.msgpack.PayloadPacker;
import com.rtm.msgpack.PayloadUnpacker;

public class RTMClient {

    class DelayConnLocker {
        public int status = 0;
        public int count = 0;
        public long timestamp = 0;
    }

    private int _pid;
    private int _timeout;
    private boolean _debug;
    private String _secret;
    private String _endpoint;
    private boolean _reconnect;

    private RTMSender _sender;
    private RTMProcessor _processor;
    private BaseClient _baseClient;

    /**
     * @param {int}     pid
     * @param {String}  secret
     * @param {String}  endpoint
     * @param {boolean} reconnect
     * @param {int}     timeout
     * @param {boolean} debug
     */
    public RTMClient(int pid, String secret, String endpoint, boolean reconnect, int timeout, boolean debug) {
        if (pid <= 0) {
            System.out.println("[RUM] The 'pid' Is Zero Or Negative!");
            return;
        }
        if (secret == null || secret.isEmpty()) {
            System.out.println("[RUM] The 'secret' Is Null Or Empty!");
            return;
        }
        if (endpoint == null || endpoint.isEmpty()) {
            System.out.println("[RUM] The 'endpoint' Is Null Or Empty!");
            return;
        }

        System.out.println("[RTM] rtm_sdk@" + RTMConfig.VERSION + ", fpnn_sdk@" + FPConfig.VERSION);

        this._pid = pid;
        this._secret = secret;
        this._endpoint = endpoint;
        this._reconnect = reconnect;
        this._timeout = timeout;
        this._debug = debug;
        this.initProcessor();
    }

    private FPEvent.IListener _secondListener = null;

    private void initProcessor() {
        this._sender = new RTMSender();
        this._processor = new RTMProcessor();
        final RTMClient self = this;
        this._secondListener = new FPEvent.IListener() {
            @Override
            public void fpEvent(EventData evd) {
                self.onSecond(evd.getTimestamp());
            }
        };
        FPManager.getInstance().addSecond(this._secondListener);
        ErrorRecorder.getInstance().setRecorder(new RTMErrorRecorder(this._debug));
    }

    private Object self_locker = new Object();

    private void onSecond(long timestamp) {
        long lastPingTimestamp = 0;

        synchronized (self_locker) {
            if (this._processor != null) {
                lastPingTimestamp = this._processor.getPingTimestamp();
            }
        }

        if (lastPingTimestamp > 0 && timestamp - lastPingTimestamp > RTMConfig.RECV_PING_TIMEOUT) {
            synchronized (self_locker) {
                if (this._baseClient != null && this._baseClient.isOpen()) {
                    this._baseClient.close(new Exception("ping timeout"));
                }
            }
        }

        this.delayConnect(timestamp);
    }

    public RTMProcessor getProcessor() {
        synchronized (self_locker) {
            return this._processor;
        }
    }

    private FPEvent _event = new FPEvent();

    public FPEvent getEvent() {
        return this._event;
    }

    private CallbackData sendQuest(FPData data, int timeout) {
        synchronized (self_locker) {
            if (this._baseClient != null) {
                return this._baseClient.sendQuest(data, timeout);
            }
        }
        return new CallbackData(new Exception("connnect first"));
    }

//    private void sendQuest(FPData data, FPCallback.ICallback callback, int timeout) {
//        synchronized (self_locker) {
//            if (this._baseClient != null) {
//                this._baseClient.sendQuest(data, callback, timeout);
//            }
//        }
//    }

    private void sendQuest(String method, Map<String, Object> payload, FPCallback.ICallback callback, int timeout) {
        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod(method);

        synchronized (self_locker) {
            if (this._sender != null && this._baseClient != null) {
                final BaseClient client = this._baseClient;
                this._sender.addQuest(client, data, payload, client.questCallback(callback), timeout);
            }
        }
    }

    public void destroy() {
        synchronized (delayconn_locker) {
            delayconn_locker.status = 0;
            delayconn_locker.count = 0;
            delayconn_locker.timestamp = 0;
        }

        synchronized (self_locker) {
            if (this._secondListener != null) {
                FPManager.getInstance().removeSecond(this._secondListener);
                this._secondListener = null;
            }
            this.getEvent().fireEvent(new EventData(this,"close", this._reconnect));
            this.getEvent().removeListener();

            if (this._sender != null) {
                this._sender.destroy();
                this._sender = null;
            }

            if (this._processor != null) {
                this._processor.destroy();
                this._processor = null;
            }

            if (this._baseClient != null) {
                this._baseClient.close();
                this._baseClient = null;
            }
        }
    }

    private EncryptInfo _encryptInfo;

    private void connect(EncryptInfo info) {
        if (info == null) {
            this.connect();
        } else {
            this.connect(info.curve, info.derKey, info.streamMode, info.reinforce);
        }
    }

    public void connect() {
        this.createBaseClient();
        synchronized (self_locker) {
            if (this._baseClient != null) {
                this._encryptInfo = null;
                this._baseClient.connect();
            }
        }
    }

    public void connect(String curve, byte[] derKey, boolean streamMode, boolean reinforce) {
        this.createBaseClient();
        synchronized (self_locker) {
            if (this._baseClient != null) {
                this._encryptInfo = new EncryptInfo(curve, derKey, streamMode, reinforce);
                this._baseClient.connect(curve, derKey, streamMode, reinforce);
            }
        }
    }

    public void connect(String curve, String derPath, boolean streamMode, boolean reinforce) {
        byte[] derKey = new LoadFile().read(derPath);
        if (derKey != null && derKey.length > 0) {
            this.connect(curve, derKey, streamMode, reinforce);
        }
    }

    private void createBaseClient() {
        synchronized (self_locker) {
            if (this._baseClient == null) {
                final RTMClient self = this;
                this._baseClient = new BaseClient(this._endpoint, this._timeout);
                this._baseClient.clientCallback = new FPClient.ICallback() {
                    @Override
                    public void clientConnect(EventData evd) {
                        self.onConnect(evd);
                    }
                    @Override
                    public void clientClose(EventData evd) {
                        self.onClose(evd);
                    }
                    @Override
                    public void clientError(EventData evd) {
                        self.onError(evd.getException());
                    }
                };
                this._baseClient.getProcessor().setProcessor(this._processor);
            }
        }
    }

    private void onConnect(EventData evd) {
        this.getEvent().fireEvent(new EventData(this, "connect"));
        synchronized (delayconn_locker) {
            delayconn_locker.count = 0;
        }
    }

    private void onClose(EventData evd) {
        synchronized (self_locker) {
            if (this._baseClient != null) {
                this._baseClient = null;
            }
        }
        this.getEvent().fireEvent(new EventData(this, "close", this._reconnect));
        this.reconnect();
    }

    private void onError(Exception ex) {
        if (ex != null) {
            ErrorRecorder.getInstance().recordError(ex);
        }
    }

    private DelayConnLocker delayconn_locker = new DelayConnLocker();

    private void reconnect() {
        if (!this._reconnect) {
            return;
        }
        EncryptInfo info = null;
        synchronized (self_locker) {
            if (this._processor != null) {
                this._processor.clearPingTimestamp();
            }
            info = this._encryptInfo;
        }

        int count = 0;
        synchronized (delayconn_locker) {
            delayconn_locker.count++;
            count = delayconn_locker.count;
        }
        if (count <= RTMConfig.RECONN_COUNT_ONCE) {
            this.connect(info);
            return;
        }
        synchronized (delayconn_locker) {
            delayconn_locker.status = 1;
            delayconn_locker.timestamp = FPManager.getInstance().getMilliTimestamp();
        }
    }

    private void delayConnect(long timestamp) {
        synchronized (delayconn_locker) {
            if (delayconn_locker.status == 0) {
                return;
            }
            if (timestamp - delayconn_locker.timestamp < RTMConfig.CONNCT_INTERVAL) {
                return;
            }
            delayconn_locker.status = 0;
            delayconn_locker.count = 0;
        }

        EncryptInfo info = null;
        synchronized (self_locker) {
            info = this._encryptInfo;
        }
        this.connect(info);
    }

    /**
     *
     * ServerGate (1a)
     *
     * @param {long}                    uid
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Exception}               exception
     * @param {Map(token:String)}       payload
     * </CallbackData>
     */
    public void getToken(long uid, int timeout, FPCallback.ICallback callback) {
        String cmd = "gettoken";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
                put("uid", uid);
            }
        };
        payload.put("pid", this._pid);
        this.sendQuest(cmd, payload, callback, timeout);
    }

    /**
     *
     * ServerGate (1b)
     *
     * @param {long}                    uid
     * @param {String}                  ce
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Exception}               exception
     * @param {Map}                     payload
     * </CallbackData>
     */
    public void kickout(long uid, String ce, int timeout, FPCallback.ICallback callback) {
        String cmd = "kickout";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
                put("uid", uid);
            }
        };
        payload.put("pid", this._pid);

        if (ce != null) {
            payload.put("ce", ce);
        }
        this.sendQuest(cmd, payload, callback, timeout);
    }

    /**
     *
     * ServerGate (1c)
     *
     * @param {long}                    uid
     * @param {String}                  apptype
     * @param {String}                  devicetoken
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Exception}               exception
     * @param {Map}                     payload
     * </CallbackData>
     */
    public void addDevice(long uid, String apptype, String devicetoken, int timeout, FPCallback.ICallback callback) {
        String cmd = "adddevice";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
                put("uid", uid);
                put("apptype", apptype);
                put("devicetoken", devicetoken);
            }
        };
        payload.put("pid", this._pid);
        this.sendQuest(cmd, payload, callback, timeout);
    }

    /**
     *
     * ServerGate (1d)
     *
     * @param {long}                    uid
     * @param {String}                  devicetoken
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Exception}               exception
     * @param {Map}                     payload
     * </CallbackData>
     */
    public void removeDevice(long uid, String devicetoken, int timeout, FPCallback.ICallback callback) {
        String cmd = "removedevice";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
                put("uid", uid);
                put("devicetoken", devicetoken);
            }
        };
        payload.put("pid", this._pid);
        this.sendQuest(cmd, payload, callback, timeout);
    }

    /**
     *
     * ServerGate (1e)
     *
     * @param {long}                    uid
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Exception}               exception
     * @param {Map}                     payload
     * </CallbackData>
     */
    public void removeToken(long uid, int timeout, FPCallback.ICallback callback) {
        String cmd = "removetoken";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
                put("uid", uid);
            }
        };
        payload.put("pid", this._pid);
        this.sendQuest(cmd, payload, callback, timeout);
    }

    /**
     *
     * ServerGate (2a)
     *
     * @param {long}                    from
     * @param {long}                    to
     * @param {byte}                    mtype
     * @param {String}                  msg
     * @param {String}                  attrs
     * @param {long}                    mid
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {long}                    mid
     * @param {Exception}               exception
     * @param {Map(mtime:long)}         payload
     * </CallbackData>
     */
    public void sendMessage(long from, long to, byte mtype, String msg, String attrs, long mid, int timeout, FPCallback.ICallback callback) {
        this.sendMessage(from, to, mtype, (Object) msg, attrs, mid, timeout, callback);
    }

    private void sendMessage(long from, long to, byte mtype, Object msg, String attrs, long mid, int timeout, FPCallback.ICallback callback) {
        if (mid == 0) {
            mid = MidGenerator.gen();
        }

        String cmd = "sendmsg";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
                put("mtype", mtype);
                put("from", from);
                put("to", to);
                put("msg", msg);
                put("attrs", attrs);
            }
        };
        payload.put("pid", this._pid);
        payload.put("mid", mid);

        final long fmid = (long) payload.get("mid");
        final FPCallback.ICallback cb = callback;
        this.sendQuest(cmd, payload, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                cbd.setMid(fmid);

                if (cb != null) {
                    cb.callback(cbd);
                }
            }
        }, timeout);
    }

    /**
     *
     * ServerGate (2b)
     *
     * @param {long}                    from
     * @param {List(Long)}              tos
     * @param {byte}                    mtype
     * @param {String}                  msg
     * @param {String}                  attrs
     * @param {long}                    mid
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {long}                    mid
     * @param {Exception}               exception
     * @param {Map(mtime:long)}         payload
     * </CallbackData>
     */
    public void sendMessages(long from, List<Long> tos, byte mtype, String msg, String attrs, long mid, int timeout, FPCallback.ICallback callback) {
        this.sendMessages(from, tos, mtype, (Object) msg, attrs, mid, timeout, callback);
    }

    private void sendMessages(long from, List<Long> tos, byte mtype, Object msg, String attrs, long mid, int timeout, FPCallback.ICallback callback) {
        if (mid == 0) {
            mid = MidGenerator.gen();
        }

        String cmd = "sendmsgs";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
                put("mtype", mtype);
                put("from", from);
                put("tos", tos);
                put("msg", msg);
                put("attrs", attrs);
            }
        };
        payload.put("pid", this._pid);
        payload.put("mid", mid);

        final long fmid = (long) payload.get("mid");
        final FPCallback.ICallback cb = callback;
        this.sendQuest(cmd, payload, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                cbd.setMid(fmid);

                if (cb != null) {
                    cb.callback(cbd);
                }
            }
        }, timeout);
    }

    /**
     *
     * ServerGate (2c)
     *
     * @param {long}                    from
     * @param {long                     gid
     * @param {byte}                    mtype
     * @param {String}                  msg
     * @param {String}                  attrs
     * @param {long}                    mid
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {long}                    mid
     * @param {Exception}               exception
     * @param {Map(mtime:long)}         payload
     * </CallbackData>
     */
    public void sendGroupMessage(long from, long gid, byte mtype, String msg, String attrs, long mid, int timeout, FPCallback.ICallback callback) {
        this.sendGroupMessage(from, gid, mtype, (Object) msg, attrs, mid, timeout, callback);
    }

    private void sendGroupMessage(long from, long gid, byte mtype, Object msg, String attrs, long mid, int timeout, FPCallback.ICallback callback) {
        if (mid == 0) {
            mid = MidGenerator.gen();
        }

        String cmd = "sendgroupmsg";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
                put("mtype", mtype);
                put("from", from);
                put("gid", gid);
                put("msg", msg);
                put("attrs", attrs);
            }
        };
        payload.put("pid", this._pid);
        payload.put("mid", mid);

        final long fmid = (long) payload.get("mid");
        final FPCallback.ICallback cb = callback;
        this.sendQuest(cmd, payload, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                cbd.setMid(fmid);

                if (cb != null) {
                    cb.callback(cbd);
                }
            }
        }, timeout);
    }

    /**
     *
     * ServerGate (2d)
     *
     * @param {long}                    from
     * @param {long                     rid
     * @param {byte}                    mtype
     * @param {String}                  msg
     * @param {String}                  attrs
     * @param {long}                    mid
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {long}                    mid
     * @param {Exception}               exception
     * @param {Map(mtime:long)}         payload
     * </CallbackData>
     */
    public void sendRoomMessage(long from, long rid, byte mtype, String msg, String attrs, long mid, int timeout, FPCallback.ICallback callback) {
        this.sendRoomMessage(from, rid, mtype, (Object) msg, attrs, mid, timeout, callback);
    }

    private void sendRoomMessage(long from, long rid, byte mtype, Object msg, String attrs, long mid, int timeout, FPCallback.ICallback callback) {
        if (mid == 0) {
            mid = MidGenerator.gen();
        }

        String cmd = "sendroommsg";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
                put("mtype", mtype);
                put("from", from);
                put("rid", rid);
                put("msg", msg);
                put("attrs", attrs);
            }
        };
        payload.put("pid", this._pid);
        payload.put("mid", mid);

        final long fmid = (long) payload.get("mid");
        final FPCallback.ICallback cb = callback;
        this.sendQuest(cmd, payload, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                cbd.setMid(fmid);

                if (cb != null) {
                    cb.callback(cbd);
                }
            }
        }, timeout);
    }

    /**
     *
     * ServerGate (2e)
     *
     * @param {long}                    from
     * @param {byte}                    mtype
     * @param {String}                  msg
     * @param {String}                  attrs
     * @param {long}                    mid
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {long}                    mid
     * @param {Exception}               exception
     * @param {Map(mtime:long)}         payload
     * </CallbackData>
     */
    public void broadcastMessage(long from, byte mtype, String msg, String attrs, long mid, int timeout, FPCallback.ICallback callback) {
        this.broadcastMessage(from, mtype, (Object) msg, attrs, mid, timeout, callback);
    }

    private void broadcastMessage(long from, byte mtype, Object msg, String attrs, long mid, int timeout, FPCallback.ICallback callback) {
        if (mid == 0) {
            mid = MidGenerator.gen();
        }

        String cmd = "broadcastmsg";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
                put("mtype", mtype);
                put("from", from);
                put("msg", msg);
                put("attrs", attrs);
            }
        };
        payload.put("pid", this._pid);
        payload.put("mid", mid);

        final long fmid = (long) payload.get("mid");
        final FPCallback.ICallback cb = callback;
        this.sendQuest(cmd, payload, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                cbd.setMid(fmid);

                if (cb != null) {
                    cb.callback(cbd);
                }
            }
        }, timeout);
    }

    /**
     *
     * ServerGate (2f)
     *
     * @param {long}                    gid
     * @param {boolean}                 desc
     * @param {int}                     num
     * @param {long}                    begin
     * @param {long}                    end
     * @param {long}                    lastid
     * @param {List(Byte)}              mtypes
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Exception}               exception
     * @param {Map(num:int,lastid:long,begin:long,end:long,msgs:List(GroupMsg))} payload
     * </CallbackData>
     *
     * <GroupMsg>
     * @param {long}                    id
     * @param {long}                    from
     * @param {byte}                    mtype
     * @param {long}                    mid
     * @param {String}                  msg
     * @param {String}                  attrs
     * @param {long}                    mtime
     * </GroupMsg>
     */
    public void getGroupMessage(long gid, boolean desc, int num, long begin, long end, long lastid, List<Byte> mtypes, int timeout, FPCallback.ICallback callback) {
        String cmd = "getgroupmsg";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
                put("gid", gid);
                put("desc", desc);
                put("num", num);
            }
        };
        payload.put("pid", this._pid);

        if (begin > 0) {
            payload.put("begin", begin);
        }
        if (end > 0) {
            payload.put("end", end);
        }
        if (lastid > 0) {
            payload.put("lastid", lastid);
        }
        if (mtypes != null) {
            payload.put("mtypes", mtypes);
        }

        final FPCallback.ICallback fcb = callback;
        this.sendQuest(cmd, payload, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                if (fcb == null) {
                    return;
                }
                Map payload = (Map) cbd.getPayload();
                if (payload != null && payload.containsKey("msgs")) {
                    List list = (ArrayList) payload.get("msgs");
                    for (int i = 0; i < list.size(); i++) {
                        Map<String, Object> groupMsg = new HashMap<String, Object>();
                        List items = (ArrayList) list.get(i);
                        groupMsg.put("id", items.get(0));
                        groupMsg.put("from", items.get(1));
                        groupMsg.put("mtype", items.get(2));
                        groupMsg.put("mid", items.get(3));
                        groupMsg.put("msg", items.get(4));
                        groupMsg.put("attrs", items.get(5));
                        groupMsg.put("mtime", items.get(6));

                        byte mtype = (byte) groupMsg.get("mtype");
                        if (mtype == RTMConfig.CHAT_TYPE.audio) {
                            if (groupMsg.containsKey("msg") && groupMsg.get("msg") instanceof String) {
                                byte[] msg  = PayloadUnpacker.getBytes((String) groupMsg.get("msg"));
                                groupMsg.put("msg", msg);
                            }
                        }
                        list.set(i, groupMsg);
                    }
                    fcb.callback(new CallbackData(payload));
                    return;
                }
                fcb.callback(cbd);
            }
        }, timeout);
    }

    /**
     *
     * ServerGate (2g)
     *
     * @param {long}                    rid
     * @param {boolean}                 desc
     * @param {int}                     num
     * @param {long}                    begin
     * @param {long}                    end
     * @param {long}                    lastid
     * @param {List(Byte)}              mtypes
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Exception}               exception
     * @param {Map(num:int,lastid:long,begin:long,end:long,msgs:List(RoomMsg))} payload
     * </CallbackData>
     *
     * <RoomMsg>
     * @param {long}                    id
     * @param {long}                    from
     * @param {byte}                    mtype
     * @param {long}                    mid
     * @param {String}                  msg
     * @param {String}                  attrs
     * @param {long}                    mtime
     * </RoomMsg>
     */
    public void getRoomMessage(long rid, boolean desc, int num, long begin, long end, long lastid, List<Byte> mtypes, int timeout, FPCallback.ICallback callback) {
        String cmd = "getroommsg";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
                put("rid", rid);
                put("desc", desc);
                put("num", num);
            }
        };
        payload.put("pid", this._pid);

        if (begin > 0) {
            payload.put("begin", begin);
        }
        if (end > 0) {
            payload.put("end", end);
        }
        if (lastid > 0) {
            payload.put("lastid", lastid);
        }
        if (mtypes != null) {
            payload.put("mtypes", mtypes);
        }

        final FPCallback.ICallback fcb = callback;
        this.sendQuest(cmd, payload, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                if (fcb == null) {
                    return;
                }
                Map payload = (Map) cbd.getPayload();
                if (payload != null && payload.containsKey("msgs")) {
                    List list = (ArrayList) payload.get("msgs");
                    for (int i = 0; i < list.size(); i++) {
                        Map<String, Object> roomMsg = new HashMap<String, Object>();
                        List items = (ArrayList) list.get(i);
                        roomMsg.put("id", items.get(0));
                        roomMsg.put("from", items.get(1));
                        roomMsg.put("mtype", items.get(2));
                        roomMsg.put("mid", items.get(3));
                        roomMsg.put("msg", items.get(4));
                        roomMsg.put("attrs", items.get(5));
                        roomMsg.put("mtime", items.get(6));

                        byte mtype = (byte) roomMsg.get("mtype");
                        if (mtype == RTMConfig.CHAT_TYPE.audio) {
                            if (roomMsg.containsKey("msg") && roomMsg.get("msg") instanceof String) {
                                byte[] msg  = PayloadUnpacker.getBytes((String) roomMsg.get("msg"));
                                roomMsg.put("msg", msg);
                            }
                        }
                        list.set(i, roomMsg);
                    }
                    fcb.callback(new CallbackData(payload));
                    return;
                }
                fcb.callback(cbd);
            }
        }, timeout);
    }

    /**
     *
     * ServerGate (2h)
     *
     * @param {boolean}                 desc
     * @param {int}                     num
     * @param {long}                    begin
     * @param {long}                    end
     * @param {long}                    lastid
     * @param {List(Byte)}              mtypes
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Exception}               exception
     * @param {Map(num:int,lastid:long,begin:long,end:long,msgs:List(BroadcastMsg))} payload
     * </CallbackData>
     *
     * <BroadcastMsg>
     * @param {long}                    id
     * @param {long}                    from
     * @param {byte}                    mtype
     * @param {long}                    mid
     * @param {String}                  msg
     * @param {String}                  attrs
     * @param {long}                    mtime
     * </BroadcastMsg>
     */
    public void getBroadcastMessage(boolean desc, int num, long begin, long end, long lastid, List<Byte> mtypes, int timeout, FPCallback.ICallback callback) {
        String cmd = "getbroadcastmsg";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
                put("desc", desc);
                put("num", num);
            }
        };
        payload.put("pid", this._pid);

        if (begin > 0) {
            payload.put("begin", begin);
        }
        if (end > 0) {
            payload.put("end", end);
        }
        if (lastid > 0) {
            payload.put("lastid", lastid);
        }
        if (mtypes != null) {
            payload.put("mtypes", mtypes);
        }

        final FPCallback.ICallback fcb = callback;
        this.sendQuest(cmd, payload, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                if (fcb == null) {
                    return;
                }
                Map payload = (Map) cbd.getPayload();
                if (payload != null && payload.containsKey("msgs")) {
                    List list = (ArrayList) payload.get("msgs");
                    for (int i = 0; i < list.size(); i++) {
                        Map<String, Object> broadcastMsg = new HashMap<String, Object>();
                        List items = (ArrayList) list.get(i);
                        broadcastMsg.put("id", items.get(0));
                        broadcastMsg.put("from", items.get(1));
                        broadcastMsg.put("mtype", items.get(2));
                        broadcastMsg.put("mid", items.get(3));
                        broadcastMsg.put("msg", items.get(4));
                        broadcastMsg.put("attrs", items.get(5));
                        broadcastMsg.put("mtime", items.get(6));

                        byte mtype = (byte) broadcastMsg.get("mtype");
                        if (mtype == RTMConfig.CHAT_TYPE.audio) {
                            if (broadcastMsg.containsKey("msg") && broadcastMsg.get("msg") instanceof String) {
                                byte[] msg  = PayloadUnpacker.getBytes((String) broadcastMsg.get("msg"));
                                broadcastMsg.put("msg", msg);
                            }
                        }
                        list.set(i, broadcastMsg);
                    }
                    fcb.callback(new CallbackData(payload));
                    return;
                }
                fcb.callback(cbd);
            }
        }, timeout);
    }

    /**
     *
     * ServerGate (2i)
     *
     * @param {long}                    uid
     * @param {long}                    ouid
     * @param {boolean}                 desc
     * @param {int}                     num
     * @param {long}                    begin
     * @param {long}                    end
     * @param {long}                    lastid
     * @param {List(Byte)}              mtypes
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Exception}               exception
     * @param {Map(num:int,lastid:long,begin:long,end:long,msgs:List(P2PMsg))} payload
     * </CallbackData>
     *
     * <P2PMsg>
     * @param {long}                    id
     * @param {byte}                    direction
     * @param {byte}                    mtype
     * @param {long}                    mid
     * @param {String}                  msg
     * @param {String}                  attrs
     * @param {long}                    mtime
     * </P2PMsg>
     */
    public void getP2PMessage(long uid, long ouid, boolean desc, int num, long begin, long end, long lastid, List<Byte> mtypes, int timeout, FPCallback.ICallback callback) {
        String cmd = "getp2pmsg";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
                put("uid", uid);
                put("ouid", ouid);
                put("desc", desc);
                put("num", num);
            }
        };
        payload.put("pid", this._pid);

        if (begin > 0) {
            payload.put("begin", begin);
        }
        if (end > 0) {
            payload.put("end", end);
        }
        if (lastid > 0) {
            payload.put("lastid", lastid);
        }
        if (mtypes != null) {
            payload.put("mtypes", mtypes);
        }

        final FPCallback.ICallback fcb = callback;
        this.sendQuest(cmd, payload, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                if (fcb == null) {
                    return;
                }
                Map payload = (Map) cbd.getPayload();
                if (payload != null && payload.containsKey("msgs")) {
                    List list = (ArrayList) payload.get("msgs");
                    for (int i = 0; i < list.size(); i++) {
                        Map<String, Object> p2pMsg = new HashMap<String, Object>();
                        List items = (ArrayList) list.get(i);
                        p2pMsg.put("id", items.get(0));
                        p2pMsg.put("direction", items.get(1));
                        p2pMsg.put("mtype", items.get(2));
                        p2pMsg.put("mid", items.get(3));
                        p2pMsg.put("msg", items.get(4));
                        p2pMsg.put("attrs", items.get(5));
                        p2pMsg.put("mtime", items.get(6));

                        byte mtype = (byte) p2pMsg.get("mtype");
                        if (mtype != RTMConfig.CHAT_TYPE.audio) {
                            if (p2pMsg.containsKey("msg") && p2pMsg.get("msg") instanceof String) {
                                byte[] msg  = PayloadUnpacker.getBytes((String) p2pMsg.get("msg"));
                                p2pMsg.put("msg", msg);
                            }
                        }
                        list.set(i, p2pMsg);
                    }
                    fcb.callback(new CallbackData(payload));
                    return;
                }
                fcb.callback(cbd);
            }
        }, timeout);
    }

    /**
     *
     * ServerGate (2j)
     *
     * @param {long}                    mid
     * @param {long}                    from
     * @param {long}                    to
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Exception}               exception
     * @param {Map}                     payload
     * </CallbackData>
     */
    public void deleteMessage(long mid, long from, long to, int timeout, FPCallback.ICallback callback) {
        this.deleteMessage(mid, from, to, (byte) 1, timeout, callback);
    }

    private void deleteMessage(long mid, long from, long xid, byte type, int timeout, FPCallback.ICallback callback) {
        String cmd = "delmsg";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
                put("mid", mid);
                put("from", from);
                put("type", type);
            }
        };
        payload.put("pid", this._pid);

        if (xid >= 0) {
            payload.put("xid", xid);
        }
        this.sendQuest(cmd, payload, callback, timeout);
    }

    /**
     *
     * ServerGate (2j')
     *
     * @param {long}                    mid
     * @param {long}                    from
     * @param {long}                    gid
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Exception}               exception
     * @param {Map}                     payload
     * </CallbackData>
     */
    public void deleteGroupMessage(long mid, long from, long gid, int timeout, FPCallback.ICallback callback) {
        this.deleteMessage(mid, from, gid, (byte) 2, timeout, callback);
    }

    /**
     *
     * ServerGate (2j'')
     *
     * @param {long}                    mid
     * @param {long}                    from
     * @param {long}                    rid
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Exception}               exception
     * @param {Map}                     payload
     * </CallbackData>
     */
    public void deleteRoomMessage(long mid, long from, long rid, int timeout, FPCallback.ICallback callback) {
        this.deleteMessage(mid, from, rid, (byte) 3, timeout, callback);
    }

    /**
     *
     * ServerGate (2j''')
     *
     * @param {long}                    mid
     * @param {long}                    from
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Exception}               exception
     * @param {Map}                     payload
     * </CallbackData>
     */
    public void deleteBroadcastMessage(long mid, long from, int timeout, FPCallback.ICallback callback) {
        this.deleteMessage(mid, from, 0, (byte) 4, timeout, callback);
    }

    /**
     *
     * ServerGate (3a)
     *
     * @param {long}                    from
     * @param {long}                    to
     * @param {String}                  msg
     * @param {String}                  attrs
     * @param {long}                    mid
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {long}                    mid
     * @param {Exception}               exception
     * @param {Map(mtime:long)}         payload
     * </CallbackData>
     */
    public void sendChat(long from, long to, String msg, String attrs, long mid, int timeout, FPCallback.ICallback callback) {
        this.sendMessage(from, to, RTMConfig.CHAT_TYPE.text, msg, attrs, mid, timeout, callback);
    }

    /**
     *
     * ServerGate (3a')
     *
     * @param {long}                    from
     * @param {long}                    to
     * @param {byte[]}                  audio
     * @param {String}                  attrs
     * @param {long}                    mid
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {long}                    mid
     * @param {Exception}               exception
     * @param {Map(mtime:long)}         payload
     * </CallbackData>
     */
    public void sendAudio(long from, long to, byte[] audio, String attrs, long mid, int timeout, FPCallback.ICallback callback) {
        this.sendMessage(from, to, RTMConfig.CHAT_TYPE.audio, audio, attrs, mid, timeout, callback);
    }

    /**
     *
     * ServerGate (3a'')
     *
     * @param {long}                    from
     * @param {long}                    to
     * @param {String}                  msg
     * @param {String}                  attrs
     * @param {long}                    mid
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {long}                    mid
     * @param {Exception}               exception
     * @param {Map(mtime:long)}         payload
     * </CallbackData>
     */
    public void sendCmd(long from, long to, String msg, String attrs, long mid, int timeout, FPCallback.ICallback callback) {
        this.sendMessage(from, to, RTMConfig.CHAT_TYPE.cmd, msg, attrs, mid, timeout, callback);
    }

    /**
     *
     * ServerGate (3b)
     *
     * @param {long}                    from
     * @param {List(Long)}              tos
     * @param {String}                  msg
     * @param {String}                  attrs
     * @param {long}                    mid
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {long}                    mid
     * @param {Exception}               exception
     * @param {Map(mtime:long)}         payload
     * </CallbackData>
     */
    public void sendChats(long from, List<Long> tos, String msg, String attrs, long mid, int timeout, FPCallback.ICallback callback) {
        this.sendMessages(from, tos, RTMConfig.CHAT_TYPE.text, msg, attrs, mid, timeout, callback);
    }

    /**
     *
     * ServerGate (3b')
     *
     * @param {long}                    from
     * @param {List(Long)}              tos
     * @param {byte[]}                  audio
     * @param {String}                  attrs
     * @param {long}                    mid
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {long}                    mid
     * @param {Exception}               exception
     * @param {Map(mtime:long)}         payload
     * </CallbackData>
     */
    public void sendAudios(long from, List<Long> tos, byte[] audio, String attrs, long mid, int timeout, FPCallback.ICallback callback) {
        this.sendMessages(from, tos, RTMConfig.CHAT_TYPE.audio, audio, attrs, mid, timeout, callback);
    }

    /**
     *
     * ServerGate (3b'')
     *
     * @param {long}                    from
     * @param {List(Long)}              tos
     * @param {String}                  msg
     * @param {String}                  attrs
     * @param {long}                    mid
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {long}                    mid
     * @param {Exception}               exception
     * @param {Map(mtime:long)}         payload
     * </CallbackData>
     */
    public void sendCmds(long from, List<Long> tos, String msg, String attrs, long mid, int timeout, FPCallback.ICallback callback) {
        this.sendMessages(from, tos, RTMConfig.CHAT_TYPE.cmd, msg, attrs, mid, timeout, callback);
    }

    /**
     *
     * ServerGate (3c)
     *
     * @param {long}                    from
     * @param {long                     gid
     * @param {String}                  msg
     * @param {String}                  attrs
     * @param {long}                    mid
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {long}                    mid
     * @param {Exception}               exception
     * @param {Map(mtime:long)}         payload
     * </CallbackData>
     */
    public void sendGroupChat(long from, long gid, String msg, String attrs, long mid, int timeout, FPCallback.ICallback callback) {
        this.sendGroupMessage(from, gid, RTMConfig.CHAT_TYPE.text, msg, attrs, mid, timeout, callback);
    }

    /**
     *
     * ServerGate (3c')
     *
     * @param {long}                    from
     * @param {long                     gid
     * @param {byte[]}                  audio
     * @param {String}                  attrs
     * @param {long}                    mid
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {long}                    mid
     * @param {Exception}               exception
     * @param {Map(mtime:long)}         payload
     * </CallbackData>
     */
    public void sendGroupAudio(long from, long gid, byte[] audio, String attrs, long mid, int timeout, FPCallback.ICallback callback) {
        this.sendGroupMessage(from, gid, RTMConfig.CHAT_TYPE.audio, audio, attrs, mid, timeout, callback);
    }

    /**
     *
     * ServerGate (3c'')
     *
     * @param {long}                    from
     * @param {long                     gid
     * @param {String}                  msg
     * @param {String}                  attrs
     * @param {long}                    mid
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {long}                    mid
     * @param {Exception}               exception
     * @param {Map(mtime:long)}         payload
     * </CallbackData>
     */
    public void sendGroupCmd(long from, long gid, String msg, String attrs, long mid, int timeout, FPCallback.ICallback callback) {
        this.sendGroupMessage(from, gid, RTMConfig.CHAT_TYPE.cmd, msg, attrs, mid, timeout, callback);
    }

    /**
     *
     * ServerGate (3d)
     *
     * @param {long}                    from
     * @param {long                     rid
     * @param {String}                  msg
     * @param {String}                  attrs
     * @param {long}                    mid
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {long}                    mid
     * @param {Exception}               exception
     * @param {Map(mtime:long)}         payload
     * </CallbackData>
     */
    public void sendRoomChat(long from, long rid, String msg, String attrs, long mid, int timeout, FPCallback.ICallback callback) {
        this.sendRoomMessage(from, rid, RTMConfig.CHAT_TYPE.text, msg, attrs, mid, timeout, callback);
    }

    /**
     *
     * ServerGate (3d')
     *
     * @param {long}                    from
     * @param {long                     rid
     * @param {byte[]}                  audio
     * @param {String}                  attrs
     * @param {long}                    mid
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {long}                    mid
     * @param {Exception}               exception
     * @param {Map(mtime:long)}         payload
     * </CallbackData>
     */
    public void sendRoomAudio(long from, long rid, byte[] audio, String attrs, long mid, int timeout, FPCallback.ICallback callback) {
        this.sendRoomMessage(from, rid, RTMConfig.CHAT_TYPE.audio, audio, attrs, mid, timeout, callback);
    }

    /**
     *
     * ServerGate (3d'')
     *
     * @param {long}                    from
     * @param {long                     rid
     * @param {String}                  msg
     * @param {String}                  attrs
     * @param {long}                    mid
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {long}                    mid
     * @param {Exception}               exception
     * @param {Map(mtime:long)}         payload
     * </CallbackData>
     */
    public void sendRoomCmd(long from, long rid, String msg, String attrs, long mid, int timeout, FPCallback.ICallback callback) {
        this.sendRoomMessage(from, rid, RTMConfig.CHAT_TYPE.cmd, msg, attrs, mid, timeout, callback);
    }

    /**
     *
     * ServerGate (3e)
     *
     * @param {long}                    from
     * @param {String}                  msg
     * @param {String}                  attrs
     * @param {long}                    mid
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {long}                    mid
     * @param {Exception}               exception
     * @param {Map(mtime:long)}         payload
     * </CallbackData>
     */
    public void broadcastChat(long from, String msg, String attrs, long mid, int timeout, FPCallback.ICallback callback) {
        this.broadcastMessage(from, RTMConfig.CHAT_TYPE.text, msg, attrs, mid, timeout, callback);
    }

    /**
     *
     * ServerGate (3e')
     *
     * @param {long}                    from
     * @param {byte[]}                  audio
     * @param {String}                  attrs
     * @param {long}                    mid
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {long}                    mid
     * @param {Exception}               exception
     * @param {Map(mtime:long)}         payload
     * </CallbackData>
     */
    public void broadcastAudio(long from, byte[] audio, String attrs, long mid, int timeout, FPCallback.ICallback callback) {
        this.broadcastMessage(from, RTMConfig.CHAT_TYPE.audio, audio, attrs, mid, timeout, callback);
    }

    /**
     *
     * ServerGate (3e'')
     *
     * @param {long}                    from
     * @param {String}                  msg
     * @param {String}                  attrs
     * @param {long}                    mid
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {long}                    mid
     * @param {Exception}               exception
     * @param {Map(mtime:long)}         payload
     * </CallbackData>
     */
    public void broadcastCmd(long from, String msg, String attrs, long mid, int timeout, FPCallback.ICallback callback) {
        this.broadcastMessage(from, RTMConfig.CHAT_TYPE.cmd, msg, attrs, mid, timeout, callback);
    }

    /**
     *
     * ServerGate (3f)
     *
     * @param {long}                    gid
     * @param {boolean}                 desc
     * @param {int}                     num
     * @param {long}                    begin
     * @param {long}                    end
     * @param {long}                    lastid
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Exception}               exception
     * @param {Map(num:int,lastid:long,begin:long,end:long,msgs:List(GroupMsg))} payload
     * </CallbackData>
     *
     * <GroupMsg>
     * @param {long}                    id
     * @param {long}                    from
     * @param {byte}                    mtype
     * @param {long}                    mid
     * @param {String/byte[]}           msg
     * @param {String}                  attrs
     * @param {long}                    mtime
     * </GroupMsg>
     */
    public void getGroupChat(long gid, boolean desc, int num, long begin, long end, long lastid, int timeout, FPCallback.ICallback callback) {
        List<Byte> mtypes = Arrays.asList(RTMConfig.CHAT_TYPE.text, RTMConfig.CHAT_TYPE.audio, RTMConfig.CHAT_TYPE.cmd);
        this.getGroupMessage(gid, desc, num, begin, end, lastid, mtypes, timeout, callback);
    }

    /**
     *
     * ServerGate (3g)
     *
     * @param {long}                    rid
     * @param {boolean}                 desc
     * @param {int}                     num
     * @param {long}                    begin
     * @param {long}                    end
     * @param {long}                    lastid
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Exception}               exception
     * @param {Map(num:int,lastid:long,begin:long,end:long,msgs:List(RoomMsg))} payload
     * </CallbackData>
     *
     * <RoomMsg>
     * @param {long}                    id
     * @param {long}                    from
     * @param {byte}                    mtype
     * @param {long}                    mid
     * @param {String/byte[]}           msg
     * @param {String}                  attrs
     * @param {long}                    mtime
     * </RoomMsg>
     */
    public void getRoomChat(long rid, boolean desc, int num, long begin, long end, long lastid, int timeout, FPCallback.ICallback callback) {
        List<Byte> mtypes = Arrays.asList(RTMConfig.CHAT_TYPE.text, RTMConfig.CHAT_TYPE.audio, RTMConfig.CHAT_TYPE.cmd);
        this.getRoomMessage(rid, desc, num, begin, end, lastid, mtypes, timeout, callback);
    }

    /**
     *
     * ServerGate (3h)
     *
     * @param {boolean}                 desc
     * @param {int}                     num
     * @param {long}                    begin
     * @param {long}                    end
     * @param {long}                    lastid
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Exception}               exception
     * @param {Map(num:int,lastid:long,begin:long,end:long,msgs:List(BroadcastMsg))} payload
     * </CallbackData>
     *
     * <BroadcastMsg>
     * @param {long}                    id
     * @param {long}                    from
     * @param {byte}                    mtype
     * @param {long}                    mid
     * @param {String/byte[]}           msg
     * @param {String}                  attrs
     * @param {long}                    mtime
     * </BroadcastMsg>
     */
    public void getBroadcastChat(boolean desc, int num, long begin, long end, long lastid, int timeout, FPCallback.ICallback callback) {
        List<Byte> mtypes = Arrays.asList(RTMConfig.CHAT_TYPE.text, RTMConfig.CHAT_TYPE.audio, RTMConfig.CHAT_TYPE.cmd);
        this.getBroadcastMessage(desc, num, begin, end, lastid, mtypes, timeout, callback);
    }

    /**
     *
     * ServerGate (3i)
     *
     * @param {long}                    uid
     * @param {long}                    ouid
     * @param {boolean}                 desc
     * @param {int}                     num
     * @param {long}                    begin
     * @param {long}                    end
     * @param {long}                    lastid
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Exception}               exception
     * @param {Map(num:int,lastid:long,begin:long,end:long,msgs:List(P2PMsg))} payload
     * </CallbackData>
     *
     * <P2PMsg>
     * @param {long}                    id
     * @param {byte}                    direction
     * @param {byte}                    mtype
     * @param {long}                    mid
     * @param {String/byte[]}           msg
     * @param {String}                  attrs
     * @param {long}                    mtime
     * </P2PMsg>
     */
    public void getP2PChat(long uid, long ouid, boolean desc, int num, long begin, long end, long lastid, int timeout, FPCallback.ICallback callback) {
        List<Byte> mtypes = Arrays.asList(RTMConfig.CHAT_TYPE.text, RTMConfig.CHAT_TYPE.audio, RTMConfig.CHAT_TYPE.cmd);
        this.getP2PMessage(uid, ouid, desc, num, begin, end, lastid, mtypes, timeout, callback);
    }

    /**
     *
     * ServerGate (3j)
     *
     * @param {long}                    mid
     * @param {long}                    from
     * @param {long}                    to
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Exception}               exception
     * @param {Map}                     payload
     * </CallbackData>
     */
    public void deleteChat(long mid, long from, long to, int timeout, FPCallback.ICallback callback) {
        this.deleteMessage(mid, from, to, (byte) 1, timeout, callback);
    }

    /**
     *
     * ServerGate (3j')
     *
     * @param {long}                    mid
     * @param {long}                    from
     * @param {long}                    gid
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Exception}               exception
     * @param {Map}                     payload
     * </CallbackData>
     */
    public void deleteGroupChat(long mid, long from, long gid, int timeout, FPCallback.ICallback callback) {
        this.deleteMessage(mid, from, gid, (byte) 2, timeout, callback);
    }

    /**
     *
     * ServerGate (3j'')
     *
     * @param {long}                    mid
     * @param {long}                    from
     * @param {long}                    rid
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Exception}               exception
     * @param {Map}                     payload
     * </CallbackData>
     */
    public void deleteRoomChat(long mid, long from, long rid, int timeout, FPCallback.ICallback callback) {
        this.deleteMessage(mid, from, rid, (byte) 3, timeout, callback);
    }

    /**
     *
     * ServerGate (3j''')
     *
     * @param {long}                    mid
     * @param {long}                    from
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Exception}               exception
     * @param {Map}                     payload
     * </CallbackData>
     */
    public void deleteBroadcastChat(long mid, long from, int timeout, FPCallback.ICallback callback) {
        this.deleteMessage(mid, from, 0, (byte) 4, timeout, callback);
    }

    /**
     *
     * ServerGate (3k)
     *
     * @param {String}                      text
     * @param {String}                      src
     * @param {String}                      dst
     * @param {String}                      type
     * @param {String}                      profanity
     * @param {int}                         timeout
     * @param {FPCallback.ICallback}        callback
     *
     * @callback
     * @param {CallbackData}                cbdata
     *
     * <CallbackData>
     * @param {Exception}                   exception
     * @param {Map(source:String,target:String,sourceText:String,targetText:String)} payload
     * </CallbackData>
     */
    public void translate(String text, String src, String dst, String type, String profanity, int timeout, FPCallback.ICallback callback) {
        String cmd = "translate";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
                put("text", text);
                put("dst", dst);
            }
        };
        payload.put("pid", this._pid);

        if (src != null) {
            payload.put("src", src);
        }
        if (type != null) {
            payload.put("type", type);
        }
        if (profanity != null) {
            payload.put("profanity", profanity);
        }
        this.sendQuest(cmd, payload, callback, timeout);
    }

    /**
     *
     * ServerGate (3i)
     *
     * @param {String}                      text
     * @param {String}                      action
     * @param {int}                         timeout
     * @param {FPCallback.ICallback}        callback
     *
     * @callback
     * @param {CallbackData}                cbdata
     *
     * <CallbackData>
     * @param {Map(text:String)}            payload
     * @param {Exception}                   exception
     * </CallbackData>
     */
    public void profanity(String text, String action, int timeout, FPCallback.ICallback callback) {
        String cmd = "profanity";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
                put("text", text);
            }
        };
        payload.put("pid", this._pid);

        if (action != null) {
            payload.put("action", action);
        }
        this.sendQuest(cmd, payload, callback, timeout);
    }

    /**
     *
     * ServerGate (3j)
     *
     * @param {byte[]}                      audio
     * @param {String}                      lang
     * @param {String}                      action
     * @param {int}                         timeout
     * @param {FPCallback.ICallback}        callback
     *
     * @callback
     * @param {CallbackData}                cbdata
     *
     * <CallbackData>
     * @param {Map(text:String,lang:String)}    payload
     * @param {Exception}                   exception
     * </CallbackData>
     */
    public void transcribe(byte[] audio, String lang, String action, int timeout, FPCallback.ICallback callback) {
        String cmd = "transcribe";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
                put("audio", audio);
                put("lang", lang);
            }
        };
        payload.put("pid", this._pid);

        if (action != null) {
            payload.put("action", action);
        }
        this.sendQuest(cmd, payload, callback, timeout);
    }

    /**
     *
     * ServerGate (4a)
     *
     * @param {long}                    from
     * @param {String}                  cmd
     * @param {List(Long)}              tos
     * @param {long}                    to
     * @param {long}                    rid
     * @param {long}                    gid
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Exception}                           exception
     * @param {Map(token:String,endpoint:String)}   payload
     * </CallbackData>
     */
    public void fileToken(long from, String cmd, List<Long> tos, long to, long rid, long gid, int timeout, FPCallback.ICallback callback) {
        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("from", from);
                put("cmd", cmd);
            }
        };

        if (tos != null) {
            payload.put("tos", tos);
        }
        if (to > 0) {
            payload.put("to", to);
        }
        if (rid > 0) {
            payload.put("rid", rid);
        }
        if (gid > 0) {
            payload.put("gid", gid);
        }
        this.filetoken(payload, callback, timeout);
    }

    /**
     *
     * ServerGate (5a)
     *
     * @param {List(Long)}              uids
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {List(Long)}              payload
     * @param {Exception}               exception
     * </CallbackData>
     */
    public void getOnlineUsers(List<Long> uids, int timeout, FPCallback.ICallback callback) {
        String cmd = "getonlineusers";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
                put("uids", uids);
            }
        };
        payload.put("pid", this._pid);

        final FPCallback.ICallback fcb = callback;
        this.sendQuest(cmd, payload, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                if (fcb == null) {
                    return;
                }
                Map payload = (Map) cbd.getPayload();
                if (payload != null && payload.containsKey("uids")) {
                    fcb.callback(new CallbackData(payload.get("uids")));
                    return;
                }
                fcb.callback(cbd);
            }
        }, timeout);
    }

    /**
     *
     * ServerGate (5b)
     *
     * @param {long}                    uid
     * @param {int}                     btime
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Map}                     payload
     * @param {Exception}               exception
     * </CallbackData>
     */
    public void addProjectBlack(long uid, int btime, int timeout, FPCallback.ICallback callback) {
        String cmd = "addprojectblack";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
                put("uid", uid);
                put("btime", btime);
            }
        };
        payload.put("pid", this._pid);
        this.sendQuest(cmd, payload, callback, timeout);
    }

    /**
     *
     * ServerGate (5c)
     *
     * @param {long}                    uid
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Map}                     payload
     * @param {Exception}               exception
     * </CallbackData>
     */
    public void removeProjectBlack(long uid, int timeout, FPCallback.ICallback callback) {
        String cmd = "removeprojectblack";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
                put("uid", uid);
            }
        };
        payload.put("pid", this._pid);
        this.sendQuest(cmd, payload, callback, timeout);
    }

    /**
     *
     * ServerGate (5d)
     *
     * @param {long}                    uid
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Map(ok:boolean)}         payload
     * @param {Exception}               exception
     * </CallbackData>
     */
    public void isProjectBlack(long uid, int timeout, FPCallback.ICallback callback) {
        String cmd = "isprojectblack";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
                put("uid", uid);
            }
        };
        payload.put("pid", this._pid);
        this.sendQuest(cmd, payload, callback, timeout);
    }

    /**
     *
     * ServerGate (5e)
     *
     * @param {long}                        uid
     * @param {String}                      oinfo
     * @param {String}                      pinfo
     * @param {int}                         timeout
     * @param {FPCallback.ICallback}        callback
     *
     * @callback
     * @param {CallbackData}                cbdata
     *
     * <CallbackData>
     * @param {Map}                         payload
     * @param {Exception}                   exception
     * </CallbackData>
     */
    public void setUserInfo(long uid, String oinfo, String pinfo, int timeout, FPCallback.ICallback callback) {
        String cmd = "setuserinfo";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
                put("uid", uid);
            }
        };
        payload.put("pid", this._pid);

        if (oinfo != null) {
            payload.put("oinfo", oinfo);
        }
        if (pinfo != null) {
            payload.put("pinfo", pinfo);
        }
        this.sendQuest(cmd, payload, callback, timeout);
    }

    /**
     *
     * ServerGate (5f)
     *
     * @param {long}                        uid
     * @param {int}                         timeout
     * @param {FPCallback.ICallback}        callback
     *
     * @callback
     * @param {CallbackData}                cbdata
     *
     * <CallbackData>
     * @param {Exception}                   exception
     * @param {Map(oinfo:String,pinfo:String)} payload
     * </CallbackData>
     */
    public void getUserInfo(long uid, int timeout, FPCallback.ICallback callback) {
        String cmd = "getuserinfo";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
                put("uid", uid);
            }
        };
        payload.put("pid", this._pid);
        this.sendQuest(cmd, payload, callback, timeout);
    }

    /**
     *
     * ServerGate (5g)
     *
     * @param {List(Long)}                  uids
     * @param {int}                         timeout
     * @param {FPCallback.ICallback}        callback
     *
     * @callback
     * @param {CallbackData}                cbdata
     *
     * <CallbackData>
     * @param {Exception}                   exception
     * @param {Map(String,String)}          payload
     * </CallbackData>
     */
    public void getUserOpenInfo(List<Long> uids, int timeout, FPCallback.ICallback callback) {
        String cmd = "getuseropeninfo";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
                put("uids", uids);
            }
        };
        payload.put("pid", this._pid);

        final FPCallback.ICallback fcb = callback;
        this.sendQuest(cmd, payload, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                if (fcb == null) {
                    return;
                }
                Map payload = (Map) cbd.getPayload();
                if (payload != null && payload.containsKey("info")) {
                    fcb.callback(new CallbackData(payload.get("info")));
                    return;
                }
                fcb.callback(cbd);
            }
        }, timeout);
    }

    /**
     *
     * ServerGate (6a)
     *
     * @param {long}                    uid
     * @param {List(Long)}              friends
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Map}                     payload
     * @param {Exception}               exception
     * </CallbackData>
     */
    public void addFriends(long uid, List<Long> friends, int timeout, FPCallback.ICallback callback) {
        String cmd = "addfriends";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
                put("uid", uid);
                put("friends", friends);
            }
        };
        payload.put("pid", this._pid);
        this.sendQuest(cmd, payload, callback, timeout);
    }

    /**
     *
     * ServerGate (6b)
     *
     * @param {long}                    uid
     * @param {List(Long)}              friends
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Map}                     payload
     * @param {Exception}               exception
     * </CallbackData>
     */
    public void deleteFriends(long uid, List<Long> friends, int timeout, FPCallback.ICallback callback) {
        String cmd = "delfriends";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
                put("uid", uid);
                put("friends", friends);
            }
        };
        payload.put("pid", this._pid);
        this.sendQuest(cmd, payload, callback, timeout);
    }

    /**
     *
     * ServerGate (6c)
     *
     * @param {long}                    uid
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {List(Long)}              payload
     * @param {Exception}               exception
     * </CallbackData>
     */
    public void getFriends(long uid, int timeout, FPCallback.ICallback callback) {
        String cmd = "getfriends";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
                put("uid", uid);
            }
        };
        payload.put("pid", this._pid);

        final FPCallback.ICallback fcb = callback;
        this.sendQuest(cmd, payload, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                if (fcb == null) {
                    return;
                }
                Map payload = (Map) cbd.getPayload();
                if (payload != null && payload.containsKey("uids")) {
                    fcb.callback(new CallbackData(payload.get("uids")));
                    return;
                }
                fcb.callback(cbd);
            }
        }, timeout);
    }

    /**
     *
     * ServerGate (6d)
     *
     * @param {long}                    uid
     * @param {long}                    fuid
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Map(ok:boolean)}         payload
     * @param {Exception}               exception
     * </CallbackData>
     */
    public void isFriend(long uid, long fuid, int timeout, FPCallback.ICallback callback) {
        String cmd = "isfriend";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
                put("uid", uid);
                put("fuid", fuid);
            }
        };
        payload.put("pid", this._pid);
        this.sendQuest(cmd, payload, callback, timeout);
    }

    /**
     *
     * ServerGate (6e)
     *
     * @param {long}                    uid
     * @param {List(Long)}              fuids
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {List(Long)}              payload
     * @param {Exception}               exception
     * </CallbackData>
     */
    public void isFriends(long uid, List<Long> fuids, int timeout, FPCallback.ICallback callback) {
        String cmd = "isfriends";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
                put("uid", uid);
                put("fuids", fuids);
            }
        };
        payload.put("pid", this._pid);

        final FPCallback.ICallback fcb = callback;
        this.sendQuest(cmd, payload, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                if (fcb == null) {
                    return;
                }
                Map payload = (Map) cbd.getPayload();
                if (payload != null && payload.containsKey("fuids")) {
                    fcb.callback(new CallbackData(payload.get("fuids")));
                    return;
                }
                fcb.callback(cbd);
            }
        }, timeout);
    }

    /**
     *
     * ServerGate (7a)
     *
     * @param {long}                    gid
     * @param {List(Long)}              uids
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Map}                     payload
     * @param {Exception}               exception
     * </CallbackData>
     */
    public void addGroupMembers(long gid, List<Long> uids, int timeout, FPCallback.ICallback callback) {
        String cmd = "addgroupmembers";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
                put("gid", gid);
                put("uids", uids);
            }
        };
        payload.put("pid", this._pid);
        this.sendQuest(cmd, payload, callback, timeout);
    }

    /**
     *
     * ServerGate (7b)
     *
     * @param {long}                    gid
     * @param {List(Long)}              uids
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Map}                     payload
     * @param {Exception}               exception
     * </CallbackData>
     */
    public void deleteGroupMembers(long gid, List<Long> uids, int timeout, FPCallback.ICallback callback) {
        String cmd = "delgroupmembers";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
                put("gid", gid);
                put("uids", uids);
            }
        };
        payload.put("pid", this._pid);
        this.sendQuest(cmd, payload, callback, timeout);
    }

    /**
     *
     * ServerGate (7c)
     *
     * @param {long}                    gid
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Map}                     payload
     * @param {Exception}               exception
     * </CallbackData>
     */
    public void deleteGroup(long gid, int timeout, FPCallback.ICallback callback) {
        String cmd = "delgroup";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
                put("gid", gid);
            }
        };
        payload.put("pid", this._pid);
        this.sendQuest(cmd, payload, callback, timeout);
    }

    /**
     *
     * ServerGate (7d)
     *
     * @param {long}                    gid
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {List(Long)}              payload
     * @param {Exception}               exception
     * </CallbackData>
     */
    public void getGroupMembers(long gid, int timeout, FPCallback.ICallback callback) {
        String cmd = "getgroupmembers";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
                put("gid", gid);
            }
        };
        payload.put("pid", this._pid);

        final FPCallback.ICallback fcb = callback;
        this.sendQuest(cmd, payload, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                if (fcb == null) {
                    return;
                }
                Map payload = (Map) cbd.getPayload();
                if (payload != null && payload.containsKey("uids")) {
                    fcb.callback(new CallbackData(payload.get("uids")));
                    return;
                }
                fcb.callback(cbd);
            }
        }, timeout);
    }

    /**
     *
     * ServerGate (7e)
     *
     * @param {long}                    gid
     * @param {long}                    uid
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Map(ok:boolean)}         payload
     * @param {Exception}               exception
     * </CallbackData>
     */
    public void isGroupMember(long gid, long uid, int timeout, FPCallback.ICallback callback) {
        String cmd = "isgroupmember";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
                put("gid", gid);
                put("uid", uid);
            }
        };
        payload.put("pid", this._pid);
        this.sendQuest(cmd, payload, callback, timeout);
    }

    /**
     *
     * ServerGate (7f)
     *
     * @param {long}                    uid
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {List(Long)}              payload
     * @param {Exception}               exception
     * </CallbackData>
     */
    public void getUserGroups(long uid, int timeout, FPCallback.ICallback callback) {
        String cmd = "getusergroups";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
                put("uid", uid);
            }
        };
        payload.put("pid", this._pid);

        final FPCallback.ICallback fcb = callback;
        this.sendQuest(cmd, payload, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                if (fcb == null) {
                    return;
                }
                Map payload = (Map) cbd.getPayload();
                if (payload != null && payload.containsKey("gids")) {
                    fcb.callback(new CallbackData(payload.get("gids")));
                    return;
                }
                fcb.callback(cbd);
            }
        }, timeout);
    }

    /**
     *
     * ServerGate (7g)
     *
     * @param {long}                    gid
     * @param {long}                    uid
     * @param {int}                     btime
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Map}                     payload
     * @param {Exception}               exception
     * </CallbackData>
     */
    public void addGroupBan(long gid, long uid, int btime, int timeout, FPCallback.ICallback callback) {
        String cmd = "addgroupban";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
                put("gid", gid);
                put("uid", uid);
                put("btime", btime);
            }
        };
        payload.put("pid", this._pid);
        this.sendQuest(cmd, payload, callback, timeout);
    }

    /**
     *
     * ServerGate (7h)
     *
     * @param {long}                    gid
     * @param {long}                    uid
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Map}                     payload
     * @param {Exception}               exception
     * </CallbackData>
     */
    public void removeGroupBan(long gid, long uid, int timeout, FPCallback.ICallback callback) {
        String cmd = "removegroupban";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
                put("gid", gid);
                put("uid", uid);
            }
        };
        payload.put("pid", this._pid);
        this.sendQuest(cmd, payload, callback, timeout);
    }

    /**
     *
     * ServerGate (7i)
     *
     * @param {long}                    gid
     * @param {long}                    uid
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Map(ok:boolean)}         payload
     * @param {Exception}               exception
     * </CallbackData>
     */
    public void isBanOfGroup(long gid, long uid, int timeout, FPCallback.ICallback callback) {
        String cmd = "isbanofgroup";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
                put("gid", gid);
                put("uid", uid);
            }
        };
        payload.put("pid", this._pid);
        this.sendQuest(cmd, payload, callback, timeout);
    }

    /**
     *
     * ServerGate (7j)
     *
     * @param {long}                    gid
     * @param {String}                  oinfo
     * @param {String}                  pinfo
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Map}                     payload
     * @param {Exception}               exception
     * </CallbackData>
     */
    public void setGroupInfo(long gid, String oinfo, String pinfo, int timeout, FPCallback.ICallback callback) {
        String cmd = "setgroupinfo";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
                put("gid", gid);
            }
        };
        payload.put("pid", this._pid);

        if (oinfo != null) {
            payload.put("oinfo", oinfo);
        }
        if (pinfo != null) {
            payload.put("pinfo", pinfo);
        }
        this.sendQuest(cmd, payload, callback, timeout);
    }

    /**
     *
     * ServerGate (7k)
     *
     * @param {long}                        gid
     * @param {int}                         timeout
     * @param {FPCallback.ICallback}        callback
     *
     * @callback
     * @param {CallbackData}                cbdata
     *
     * <CallbackData>
     * @param {Exception}                   exception
     * @param {Map(oinfo:String,pinfo:String)} payload
     * </CallbackData>
     */
    public void getGroupInfo(long gid, int timeout, FPCallback.ICallback callback) {
        String cmd = "getgroupinfo";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
                put("gid", gid);
            }
        };
        payload.put("pid", this._pid);
        this.sendQuest(cmd, payload, callback, timeout);
    }

    /**
     *
     * ServerGate (8a)
     *
     * @param {long}                    rid
     * @param {long}                    uid
     * @param {int}                     btime
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Map}                     payload
     * @param {Exception}               exception
     * </CallbackData>
     */
    public void addRoomBan(long rid, long uid, int btime, int timeout, FPCallback.ICallback callback) {
        String cmd = "addroomban";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
                put("rid", rid);
                put("uid", uid);
                put("btime", btime);
            }
        };
        payload.put("pid", this._pid);
        this.sendQuest(cmd, payload, callback, timeout);
    }

    /**
     *
     * ServerGate (8b)
     *
     * @param {long}                    rid
     * @param {long}                    uid
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Map}                     payload
     * @param {Exception}               exception
     * </CallbackData>
     */
    public void removeRoomBan(long rid, long uid, int timeout, FPCallback.ICallback callback) {
        String cmd = "removeroomban";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
                put("rid", rid);
                put("uid", uid);
            }
        };
        payload.put("pid", this._pid);
        this.sendQuest(cmd, payload, callback, timeout);
    }

    /**
     *
     * ServerGate (8c)
     *
     * @param {long}                    rid
     * @param {long}                    uid
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Map(ok:boolean)}         payload
     * @param {Exception}               exception
     * </CallbackData>
     */
    public void isBanOfRoom(long rid, long uid, int timeout, FPCallback.ICallback callback) {
        String cmd = "isbanofroom";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
                put("rid", rid);
                put("uid", uid);
            }
        };
        payload.put("pid", this._pid);
        this.sendQuest(cmd, payload, callback, timeout);
    }

    /**
     *
     * ServerGate (8d)
     *
     * @param {long}                    rid
     * @param {long}                    uid
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Exception}               exception
     * @param {Map}                     payload
     * </CallbackData>
     */
    public void addRoomMember(long rid, long uid, int timeout, FPCallback.ICallback callback) {
        String cmd = "addroommember";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
                put("rid", rid);
                put("uid", uid);
            }
        };
        payload.put("pid", this._pid);
        this.sendQuest(cmd, payload, callback, timeout);
    }

    /**
     *
     * ServerGate (8e)
     *
     * @param {long}                    rid
     * @param {long}                    uid
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Exception}               exception
     * @param {Map}                     payload
     * </CallbackData>
     */
    public void deleteRoomMember(long rid, long uid, int timeout, FPCallback.ICallback callback) {
        String cmd = "delroommember";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
                put("rid", rid);
                put("uid", uid);
            }
        };
        payload.put("pid", this._pid);
        this.sendQuest(cmd, payload, callback, timeout);
    }

    /**
     *
     * ServerGate (8f)
     *
     * @param {long}                    rid
     * @param {String}                  oinfo
     * @param {String}                  pinfo
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Exception}               exception
     * @param {Map}                     payload
     * </CallbackData>
     */
    public void setRoomInfo(long rid, String oinfo, String pinfo, int timeout, FPCallback.ICallback callback) {
        String cmd = "setroominfo";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
                put("rid", rid);
            }
        };
        payload.put("pid", this._pid);

        if (oinfo != null) {
            payload.put("oinfo", oinfo);
        }
        if (pinfo != null) {
            payload.put("pinfo", pinfo);
        }
        this.sendQuest(cmd, payload, callback, timeout);
    }

    /**
     *
     * ServerGate (8g)
     *
     * @param {long}                        rid
     * @param {int}                         timeout
     * @param {FPCallback.ICallback}        callback
     *
     * @callback
     * @param {CallbackData}                cbdata
     *
     * <CallbackData>
     * @param {Exception}                   exception
     * @param {Map(oinfo:String,pinfo:String)} payload
     * </CallbackData>
     */
    public void getRoomInfo(long rid, int timeout, FPCallback.ICallback callback) {
        String cmd = "getroominfo";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
                put("rid", rid);
            }
        };
        payload.put("pid", this._pid);
        this.sendQuest(cmd, payload, callback, timeout);
    }

    /**
     *
     * ServerGate (9a)
     *
     * @param {List(Long)}              gids
     * @param {List(Long)}              rids
     * @param {List<Long>}              uids
     * @param {List(String)}            events
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Exception}               exception
     * @param {Map}                     payload
     * </CallbackData>
     */
    public void addEvtListener(List<Long> gids, List<Long> rids, List<Long> uids, List<String> events, int timeout, FPCallback.ICallback callback) {
        String cmd = "addlisten";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
            }
        };
        payload.put("pid", this._pid);

        if (gids != null) {
            payload.put("gids", gids);
        }
        if (rids != null) {
            payload.put("rids", rids);
        }
        if (uids != null) {
            payload.put("uids", uids);
        }
        if (events != null) {
            payload.put("events", events);
        }
        this.sendQuest(cmd, payload, callback, timeout);
    }

    /**
     *
     * ServerGate (9b)
     *
     * @param {List(Long)}              gids
     * @param {List(Long)}              rids
     * @param {List<Long>}              uids
     * @param {List(String)}            events
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Exception}               exception
     * @param {Map}                     payload
     * </CallbackData>
     */
    public void removeEvtListener(List<Long> gids, List<Long> rids, List<Long> uids, List<String> events, int timeout, FPCallback.ICallback callback) {
        String cmd = "removelisten";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
            }
        };
        payload.put("pid", this._pid);

        if (gids != null) {
            payload.put("gids", gids);
        }
        if (rids != null) {
            payload.put("rids", rids);
        }
        if (uids != null) {
            payload.put("uids", uids);
        }
        if (events != null) {
            payload.put("events", events);
        }
        this.sendQuest(cmd, payload, callback, timeout);
    }

    /**
     *
     * ServerGate (9c)
     *
     * @param {boolean}                 p2p
     * @param {boolean}                 group
     * @param {boolean}                 room
     * @param {boolean}                 ev
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Exception}               exception
     * @param {Map}                     payload
     * </CallbackData>
     */
    public void setEvtListener(boolean p2p, boolean group, boolean room, boolean ev, int timeout, FPCallback.ICallback callback) {
        String cmd = "setlisten";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
                put("p2p", p2p);
                put("group", group);
                put("room", room);
                put("ev", ev);
            }
        };
        payload.put("pid", this._pid);
        this.sendQuest(cmd, payload, callback, timeout);
    }

    /**
     *
     * ServerGate (9c')
     *
     * @param {List(Long)}              gids
     * @param {List(Long)}              rids
     * @param {List(Long)}              uids
     * @param {List(String)}            events
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Exception}               exception
     * @param {Map}                     payload
     * </CallbackData>
     */
    public void setEvtListener(List<Long> gids, List<Long> rids, List<Long> uids, List<String> events, int timeout, FPCallback.ICallback callback) {
        String cmd = "setlisten";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
            }
        };
        payload.put("pid", this._pid);

        if (gids != null) {
            payload.put("gids", gids);
        }
        if (rids != null) {
            payload.put("rids", rids);
        }
        if (uids != null) {
            payload.put("uids", uids);
        }
        if (events != null) {
            payload.put("events", events);
        }
        this.sendQuest(cmd, payload, callback, timeout);
    }

    /**
     *
     * ServerGate (10a)
     *
     * @param {long}                    uid
     * @param {String}                  key
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Exception}               exception
     * @param {Map(val:String)}         payload
     * </CallbackData>
     */
    public void dataGet(long uid, String key, int timeout, FPCallback.ICallback callback) {
        String cmd = "dataget";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
                put("uid", uid);
                put("key", key);
            }
        };
        payload.put("pid", this._pid);
        this.sendQuest(cmd, payload, callback, timeout);
    }

    /**
     *
     * ServerGate (10b)
     *
     * @param {long}                    uid
     * @param {String}                  key
     * @param {String}                  val
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Exception}               exception
     * @param {Map}                     payload
     * </CallbackData>
     */
    public void dataSet(long uid, String key, String val, int timeout, FPCallback.ICallback callback) {
        String cmd = "dataset";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
                put("uid", uid);
                put("key", key);
                put("val", val);
            }
        };
        payload.put("pid", this._pid);
        this.sendQuest(cmd, payload, callback, timeout);
    }

    /**
     *
     * ServerGate (10c)
     *
     * @param {long}                    uid
     * @param {String}                  key
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Exception}               exception
     * @param {Map}                     payload
     * </CallbackData>
     */
    public void dataDelete(long uid, String key, int timeout, FPCallback.ICallback callback) {
        String cmd = "datadel";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        Map<String, Object> payload = new HashMap<String, Object>() {
            {
                put("ts", ts);
                put("salt", salt);
                put("sign", sign);
                put("uid", uid);
                put("key", key);
            }
        };
        payload.put("pid", this._pid);
        this.sendQuest(cmd, payload, callback, timeout);
    }

    /**
     *
     * fileGate (1)
     *
     * @param {long}                    from
     * @param {long}                    to
     * @param {byte}                    mtype
     * @param {byte[]}                  fileBytes
     * @param {String}                  fileExt
     * @param {String}                  fileName
     * @param {long}                    mid
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Map(mtime:long)}         payload
     * @param {Exception}               exception
     * @param {long}                    mid
     */
    public void sendFile(long from, long to, byte mtype, byte[] fileBytes, String fileExt, String fileName, long mid, int timeout, FPCallback.ICallback callback) {
        if (fileBytes == null || fileBytes.length <= 0) {
            if (callback != null) {
                callback.callback(new CallbackData(new Exception("empty file bytes!")));
            }
            return;
        }

        Map<String, Object> ops = new HashMap<String, Object>() {
            {
                put("cmd", "sendfile");
                put("from", from);
                put("to", to);
                put("mtype", mtype);
                put("file", fileBytes);
                put("ext", fileExt);
                put("filename", fileName);
            }
        };
        this.fileSendProcess(ops, mid, timeout, callback);
    }

    /**
     *
     * filegate (2)
     *
     * @param {long}                    from
     * @param {List(Long)}              tos
     * @param {byte}                    mtype
     * @param {byte[]}                  fileBytes
     * @param {String}                  fileExt
     * @param {String}                  fileName
     * @param {long}                    mid
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Map(mtime:long)}         payload
     * @param {Exception}               exception
     * @param {long}                    mid
     */
    public void sendFiles(long from, List<Long> tos, byte mtype, byte[] fileBytes, String fileExt, String fileName, long mid, int timeout, FPCallback.ICallback callback) {
        if (fileBytes == null || fileBytes.length <= 0) {
            if (callback != null) {
                callback.callback(new CallbackData(new Exception("empty file bytes!")));
            }
            return;
        }

        Map<String, Object> ops = new HashMap<String, Object>() {
            {
                put("cmd", "sendfiles");
                put("from", from);
                put("tos", tos);
                put("mtype", mtype);
                put("file", fileBytes);
                put("ext", fileExt);
                put("filename", fileName);
            }
        };
        this.fileSendProcess(ops, mid, timeout, callback);
    }

    /**
     *
     * filegate (3)
     *
     * @param {long}                    from
     * @param {long}                    gid
     * @param {byte}                    mtype
     * @param {byte[]}                  fileBytes
     * @param {String}                  fileExt
     * @param {String}                  fileName
     * @param {long}                    mid
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Map(mtime:long)}         payload
     * @param {Exception}               exception
     * @param {long}                    mid
     */
    public void sendGroupFile(long from, long gid, byte mtype, byte[] fileBytes, String fileExt, String fileName, long mid, int timeout, FPCallback.ICallback callback) {
        if (fileBytes == null || fileBytes.length <= 0) {
            if (callback != null) {
                callback.callback(new CallbackData(new Exception("empty file bytes!")));
            }
            return;
        }

        Map<String, Object> ops = new HashMap<String, Object>() {
            {
                put("cmd", "sendgroupfile");
                put("from", from);
                put("gid", gid);
                put("mtype", mtype);
                put("file", fileBytes);
                put("ext", fileExt);
                put("filename", fileName);
            }
        };
        this.fileSendProcess(ops, mid, timeout, callback);
    }

    /**
     *
     * filegate (4)
     *
     * @param {long}                    from
     * @param {long}                    rid
     * @param {byte}                    mtype
     * @param {byte[]}                  fileBytes
     * @param {String}                  fileExt
     * @param {String}                  fileName
     * @param {long}                    mid
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Map(mtime:long)}         payload
     * @param {Exception}               exception
     * @param {long}                    mid
     */
    public void sendRoomFile(long from, long rid, byte mtype, byte[] fileBytes, String fileExt, String fileName, long mid, int timeout, FPCallback.ICallback callback) {
        if (fileBytes == null || fileBytes.length <= 0) {
            if (callback != null) {
                callback.callback(new CallbackData(new Exception("empty file bytes!")));
            }
            return;
        }

        Map<String, Object> ops = new HashMap<String, Object>() {
            {
                put("cmd", "sendroomfile");
                put("from", from);
                put("rid", rid);
                put("mtype", mtype);
                put("file", fileBytes);
                put("ext", fileExt);
                put("filename", fileName);
            }
        };
        this.fileSendProcess(ops, mid, timeout, callback);
    }

    /**
     *
     * filegate (5)
     *
     * @param {long}                    from
     * @param {byte}                    mtype
     * @param {byte[]}                  fileBytes
     * @param {String}                  fileExt
     * @param {String}                  fileName
     * @param {long}                    mid
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {Map(mtime:long)}         payload
     * @param {Exception}               exception
     * @param {long}                    mid
     */
    public void broadcastFile(long from, byte mtype, byte[] fileBytes, String fileExt, String fileName, long mid, int timeout, FPCallback.ICallback callback) {
        if (fileBytes == null || fileBytes.length <= 0) {
            if (callback != null) {
                callback.callback(new CallbackData(new Exception("empty file bytes!")));
            }
            return;
        }

        Map<String, Object> ops = new HashMap<String, Object>() {
            {
                put("cmd", "broadcastfile");
                put("from", from);
                put("mtype", mtype);
                put("file", fileBytes);
                put("ext", fileExt);
                put("filename", fileName);
            }
        };
        this.fileSendProcess(ops, mid, timeout, callback);
    }

    private void fileSendProcess(Map<String, Object> ops, long mid, int timeout, FPCallback.ICallback callback) {
        if (mid == 0) {
            mid = MidGenerator.gen();
        }

        Map<String, Object> payload = new HashMap<String, Object>();
        payload.put("cmd", ops.get("cmd"));

        if (ops.containsKey("tos")) {
            payload.put("tos", ops.get("tos"));
        }
        if (ops.containsKey("to")) {
            payload.put("to", ops.get("to"));
        }
        if (ops.containsKey("rid")) {
            payload.put("rid", ops.get("rid"));
        }
        if (ops.containsKey("gid")) {
            payload.put("gid", ops.get("gid"));
        }
        if (ops.containsKey("from")) {
            payload.put("from", ops.get("from"));
        }

        final RTMClient self = this;
        final long fmid = mid;
        final int ftimeout = timeout;
        final Map fops = ops;
        final FPCallback.ICallback fcb = callback;
        this.filetoken(payload, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Exception exception = cbd.getException();
                if (exception != null) {
                    if (fcb != null) {
                        fcb.callback(new CallbackData(exception));
                    }
                    return;
                }
                Object obj = cbd.getPayload();
                if (obj == null) {
                    if (fcb != null) {
                        fcb.callback(new CallbackData(new Exception("file token error")));
                    }
                    return;
                }

                String token = null;
                String endpoint = null;
                Map<String, Object> payload = (Map<String, Object>) obj;

                if (payload != null && payload.containsKey("token")) {
                    token = (String) payload.get("token");
                }
                if (payload != null && payload.containsKey("endpoint")) {
                    endpoint = (String) payload.get("endpoint");
                }
                if (token == null || token.isEmpty()) {
                    if (fcb != null) {
                        fcb.callback(new CallbackData(new Exception("file token is null or empty")));
                    }
                    return;
                }
                if (endpoint == null || endpoint.isEmpty()) {
                    if (fcb != null) {
                        fcb.callback(new CallbackData(new Exception("file endpoint is null or empty")));
                    }
                    return;
                }

                FileClient fileClient = new FileClient(endpoint, ftimeout);
                payload = new HashMap<String, Object>() {
                    {
                        put("pid", self._pid);
                        put("mid", fmid);
                    }
                };
                payload.put("mtype", fops.get("mtype"));
                payload.put("from", fops.get("from"));

                if (fops.containsKey("tos")) {
                    payload.put("tos", fops.get("tos"));
                }
                if (fops.containsKey("to")) {
                    payload.put("to", fops.get("to"));
                }
                if (fops.containsKey("rid")) {
                    payload.put("rid", fops.get("rid"));
                }
                if (fops.containsKey("gid")) {
                    payload.put("gid", fops.get("gid"));
                }

                synchronized (self_locker) {
                    fileClient.send(self._sender, ops, token, payload, ftimeout, fcb);
                }
            }
        }, timeout);
    }

    private void filetoken(Map payload, FPCallback.ICallback callback, int timeout) {
        String cmd = "filetoken";
        long salt = MidGenerator.gen();
        int ts = FPManager.getInstance().getTimestamp();
        String sign = this.genSign(salt, cmd, ts);

        payload.put("ts", ts);
        payload.put("sign", sign);
        payload.put("salt", salt);
        payload.put("pid", this._pid);

        this.sendQuest(cmd, payload, callback, timeout);
    }

    private String genSign(long slat, String cmd, int ts) {
        StringBuffer sb = new StringBuffer(Integer.toString(this._pid));
        sb.append(":");
        sb.append(this._secret);
        sb.append(":");
        sb.append(slat);
        sb.append(":");
        sb.append(cmd);
        sb.append(":");
        sb.append(ts);
        return FPManager.getInstance().md5(sb.toString());
    }

    private static class MidGenerator {
        private static long count = 0;
        private static StringBuilder sb = new StringBuilder(20);

        public synchronized static long gen() {
            if (++count > 999) {
                count = 1;
            }
            sb.setLength(0);
            sb.append(FPManager.getInstance().getMilliTimestamp());

            if (count < 100) {
                sb.append("0");
            }
            if (count < 10) {
                sb.append("0");
            }
            sb.append(count);
            return Long.valueOf(sb.toString());
        }
    }

    public static class RTMRegistration {
        public static void register() {
            FPManager.getInstance().init();
        }
    }

    class RTMErrorRecorder implements ErrorRecorder.IErrorRecorder {
        private boolean _debug;

        public RTMErrorRecorder(boolean debug) {
            this._debug = debug;
        }

        @Override
        public void recordError(Exception ex) {
            if (this._debug) {
                System.out.println(ex);
            }
        }
    }

    class EncryptInfo {
        public String curve;
        public byte[] derKey;
        public boolean streamMode;
        public boolean reinforce;

        public EncryptInfo(String curve, byte[] derKey, boolean streamMode, boolean reinforce) {
            curve = curve;
            derKey = derKey;
            streamMode = streamMode;
            reinforce = reinforce;
        }
    }

    class BaseClient extends FPClient {
        public BaseClient(String endpoint, int timeout) {
            super(endpoint, timeout);
        }
        public BaseClient(String host, int port, int timeout) {
            super(host, port, timeout);
        }

//        @Override
//        public void sendQuest(FPData data, FPCallback.ICallback callback, int timeout) {
//            super.sendQuest(data, this.questCallback(callback), timeout);
//        }

        @Override
        public CallbackData sendQuest(FPData data, int timeout) {
            CallbackData cbd = null;

            try {
                cbd = super.sendQuest(data, timeout);
            } catch (Exception ex) {
                ErrorRecorder.getInstance().recordError(ex);
            }

            if (cbd != null) {
                this.checkFPCallback(cbd);
            }
            return cbd;
        }

        /**
         * @param {String}  curve
         * @param {byte[]}  derKey
         * @param {boolean} streamMode
         * @param {boolean} reinforce
         */
        public void connect(String curve, byte[] derKey, boolean streamMode, boolean reinforce) {
            if (derKey != null && derKey.length > 0) {
                if (this.encryptor(curve, derKey, streamMode, reinforce, false)) {
                    this.connect(new FPClient.IKeyData() {
                        @Override
                        public FPData getKeyData(FPEncryptor encryptor) {
                            byte[] bytes = new byte[0];
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("publicKey", encryptor.cryptoInfo().selfPublicKey);
                            map.put("streamMode", encryptor.cryptoInfo().streamMode);
                            map.put("bits", encryptor.cryptoInfo().keyLength);

                            try {
                                PayloadPacker packer = new PayloadPacker();
                                packer.pack(map);
                                bytes = packer.toByteArray();
                            } catch (Exception ex) {
                                ErrorRecorder.getInstance().recordError(ex);
                            }

                            FPData data = new FPData();
                            if (bytes.length > 0) {
                                data.setPayload(bytes);
                            }
                            return data;
                        }
                    });
                    return;
                }
            }
            this.connect();
        }

        /**
         * @param {FPCallback.ICallback} callback
         */
        public FPCallback.ICallback questCallback(FPCallback.ICallback callback) {
            BaseClient self = this;
            return new FPCallback.ICallback() {
                @Override
                public void callback(CallbackData cbd) {
                    if (callback == null) {
                        return;
                    }
                    self.checkFPCallback(cbd);
                    callback.callback(cbd);
                }
            };
        }

        private void checkFPCallback(CallbackData cbd) {
            Map payload = null;
            FPData data = cbd.getData();
            Boolean isAnswerException = false;

            if (data != null) {
                if (data.getFlag() == 0) {
                    try {
                        JsonHelper.IJson json = JsonHelper.getInstance().getJson();
                        payload = json.toMap(data.jsonPayload());
                    } catch (Exception ex) {
                        ErrorRecorder.getInstance().recordError(ex);
                    }
                }

                if (data.getFlag() == 1) {
                    try {
                        PayloadUnpacker unpacker = new PayloadUnpacker(data.msgpackPayload());
                        payload = unpacker.unpack();
                    } catch (Exception ex) {
                        ErrorRecorder.getInstance().recordError(ex);
                    }
                }

                if (this.getPackage().isAnswer(data)) {
                    isAnswerException = data.getSS() != 0;
                }
            }
            cbd.checkException(isAnswerException, payload);
        }
    }

    class FileClient extends BaseClient {
        public FileClient(String endpoint, int timeout) {
            super(endpoint, timeout);
        }
        public FileClient(String host, int port, int timeout) {
            super(host, port, timeout);
        }

        /**
         * @param {RTMSender}            sender
         * @param {String}               method
         * @param {byte[]}               fileBytes
         * @param {String}               token
         * @param {Map}                  payload
         * @param {int}                  timeout
         * @param {FPCallback.ICallback} callback
         */
        public void send(RTMSender sender, Map<String, Object> ops, String token, Map payload, int timeout, FPCallback.ICallback callback) {
            String method = null;
            if (ops.containsKey("cmd")){
                method = (String) ops.get("cmd");
            }
            if (method == null || method.isEmpty()) {
                if (callback != null) {
                    callback.callback(new CallbackData(new Exception("wrong cmd!")));
                }
                return;
            }

            byte[] fileBytes = null;
            if (ops.containsKey("file")){
                fileBytes = (byte[]) ops.get("file");
            }
            String fileMd5 = FPManager.getInstance().md5(fileBytes).toLowerCase();
            String sign = FPManager.getInstance().md5(fileMd5.concat(":").concat(token)).toLowerCase();
            if (sign == null || sign.isEmpty()) {
                if (callback != null) {
                    callback.callback(new CallbackData(new Exception("wrong sign!")));
                }
                return;
            }

            if (!this.hasConnect()) {
                this.connect();
            }

            String fileExt = null;
            if (ops.containsKey("ext")) {
                fileExt = (String) ops.get("ext");
            }
            String fileName = null;
            if (ops.containsKey("filename")) {
                fileName = (String) ops.get("filename");
            }
            Map<String, Object> attrs = new HashMap<String, Object>() {
                {
                    put("sign", sign);
                }
            };
            if (fileExt != null && !fileExt.isEmpty()) {
                attrs.put("ext", fileExt);
            }
            if (fileName != null && !fileName.isEmpty()) {
                attrs.put("filename", fileName);
            }

            payload.put("token", token);
            payload.put("file", fileBytes);
            JsonHelper.IJson json = JsonHelper.getInstance().getJson();
            payload.put("attrs", json.toJSON(attrs));

            FPData data = new FPData();
            data.setFlag(0x1);
            data.setMtype(0x1);
            data.setMethod(method);

            if (sender != null) {
                final long fmid = (long) payload.get("mid");
                final FPCallback.ICallback fcb = callback;
                final FileClient self = this;
                sender.addQuest(this, data, payload, this.questCallback(new FPCallback.ICallback() {
                    @Override
                    public void callback(CallbackData cbd) {
                        cbd.setMid(fmid);
                        self.close();

                        if (fcb != null) {
                            fcb.callback(cbd);
                        }
                    }
                }), timeout);
            }
        }
    }

    class LoadFile {
        /**
         * @param {String} derPath
         */
        public byte[] read(String derPath) {
            File f = new File(derPath);

            if (!f.exists()) {
                System.err.println(new String("file not exists! path: ").concat(f.getAbsolutePath()));
                return null;
            }

            ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());
            BufferedInputStream in = null;

            try {
                in = new BufferedInputStream(new FileInputStream(f));
                int buf_size = 1024;
                byte[] buffer = new byte[buf_size];
                int len = 0;

                while (-1 != (len = in.read(buffer, 0, buf_size))) {
                    bos.write(buffer, 0, len);
                }
                return bos.toByteArray();
            } catch (Exception ex) {
                ErrorRecorder.getInstance().recordError(ex);
            } finally {
                try {
                    in.close();
                    bos.close();
                } catch (Exception ex) {
                    ErrorRecorder.getInstance().recordError(ex);
                }
            }
            return null;
        }
    }
}


