package com.rtm;

import com.fpnn.ErrorRecorder;
import com.fpnn.FPClient;
import com.fpnn.FPConfig;
import com.fpnn.FPData;
import com.fpnn.callback.CallbackData;
import com.fpnn.callback.FPCallback;
import com.fpnn.encryptor.FPEncryptor;
import com.fpnn.event.EventData;
import com.fpnn.event.FPEvent;
import com.fpnn.nio.ThreadPool;
import com.rtm.json.JsonHelper;
import com.rtm.msgpack.PayloadPacker;
import com.rtm.msgpack.PayloadUnpacker;

import java.io.*;
import java.util.*;
import java.lang.StringBuilder;
import java.security.MessageDigest;

public class RTMClient extends BaseClient {

    private static class MidGenerator {

        static private long count = 0;
        static private StringBuilder sb = new StringBuilder(20);

        static public synchronized long gen() {
            long c = 0;

            if (++count >= 999) {
                count = 0;
            }

            c = count;
            sb.setLength(0);
            sb.append(System.currentTimeMillis());

            if (c < 100) {
                sb.append("0");
            }

            if (c < 10) {
                sb.append("0");
            }

            sb.append(c);
            return Long.valueOf(sb.toString());
        }
    }

    private int _pid;
    private String _secret;
    private RTMProcessor _processor;

    /**
     * @param {int}     pid
     * @param {String}  secret
     * @param {String}  endpoint
     * @param {boolean} reconnect
     * @param {int}     timeout
     */
    public RTMClient(int pid, String secret, String endpoint, boolean reconnect, int timeout) {
        super(endpoint, reconnect, timeout);
        this._pid = pid;
        this._secret = secret;
    }

    /**
     * @param {int}     pid
     * @param {String}  secret
     * @param {String}  host
     * @param {int}     port
     * @param {boolean} reconnect
     * @param {int}     timeout
     */
    public RTMClient(int pid, String secret, String host, int port, boolean reconnect, int timeout) {
        super(host, port, reconnect, timeout);
        this._pid = pid;
        this._secret = secret;
    }

    @Override
    protected void init(String host, int port, boolean reconnect, int timeout) {
        super.init(host, port, reconnect, timeout);
        System.out.println("Hello RTM! rtm@" + RTMConfig.VERSION + ", fpnn@" + FPConfig.VERSION);
        this._processor = new RTMProcessor(this.getEvent());
        final RTMClient self = this;
        final RTMProcessor fprocessor = this._processor;
        this.getEvent().addListener("ping_timeout", new FPEvent.IListener() {
            @Override
            public void fpEvent(EventData evd) {
                if (self.isOpen()) {
                    self.close(new Exception("ping timeout"));
                }
            }
        });
        this.getEvent().addListener("connect", new FPEvent.IListener() {
            @Override
            public void fpEvent(EventData evd) {
                fprocessor.initPingTimestamp();
            }
        });
        this.getProcessor().setProcessor(this._processor);
    }

    public RTMProcessor rtmProcessor() {
        return this._processor;
    }

    @Override
    public void destroy() {
        super.destroy();
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
        long salt = MidGenerator.gen();
        Map payload = new HashMap();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);
        payload.put("uid", uid);
        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("gettoken");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, callback, timeout);
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
        long salt = MidGenerator.gen();
        Map payload = new HashMap();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);
        payload.put("uid", uid);

        if (ce != null && !ce.isEmpty()) {
            payload.put("ce", ce);
        }

        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("kickout");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, callback, timeout);
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
        long salt = MidGenerator.gen();
        Map payload = new HashMap();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);
        payload.put("uid", uid);
        payload.put("apptype", apptype);
        payload.put("devicetoken", devicetoken);
        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("adddevice");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, callback, timeout);
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
        long salt = MidGenerator.gen();
        Map payload = new HashMap();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);
        payload.put("uid", uid);
        payload.put("devicetoken", devicetoken);
        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("removedevice");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, callback, timeout);
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
        long salt = MidGenerator.gen();

        if (mid == 0) {
            mid = MidGenerator.gen();
        }

        Map payload = new HashMap();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);
        payload.put("mtype", mtype);
        payload.put("from", from);
        payload.put("to", to);
        payload.put("mid", mid);
        payload.put("msg", msg);
        payload.put("attrs", attrs);
        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("sendmsg");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ErrorRecorder.getInstance().recordError(ex);
        }

        data.setPayload(bytes);
        final long fmid = (long) payload.get("mid");
        final FPCallback.ICallback cb = callback;
        this.sendQuest(data, new FPCallback.ICallback() {
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
        long salt = MidGenerator.gen();

        if (mid == 0) {
            mid = MidGenerator.gen();
        }

        Map payload = new HashMap();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);
        payload.put("mtype", mtype);
        payload.put("from", from);
        payload.put("tos", tos);
        payload.put("mid", mid);
        payload.put("msg", msg);
        payload.put("attrs", attrs);
        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("sendmsgs");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        final long fmid = (long) payload.get("mid");
        final FPCallback.ICallback cb = callback;
        this.sendQuest(data, new FPCallback.ICallback() {
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
        long salt = MidGenerator.gen();

        if (mid == 0) {
            mid = MidGenerator.gen();
        }

        Map payload = new HashMap();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);
        payload.put("mtype", mtype);
        payload.put("from", from);
        payload.put("gid", gid);
        payload.put("mid", mid);
        payload.put("msg", msg);
        payload.put("attrs", attrs);
        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("sendgroupmsg");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        final long fmid = (long) payload.get("mid");
        final FPCallback.ICallback cb = callback;
        this.sendQuest(data, new FPCallback.ICallback() {
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
        long salt = MidGenerator.gen();

        if (mid == 0) {
            mid = MidGenerator.gen();
        }

        Map payload = new HashMap();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);
        payload.put("mtype", mtype);
        payload.put("from", from);
        payload.put("rid", rid);
        payload.put("mid", mid);
        payload.put("msg", msg);
        payload.put("attrs", attrs);
        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("sendroommsg");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        final long fmid = (long) payload.get("mid");
        final FPCallback.ICallback cb = callback;
        this.sendQuest(data, new FPCallback.ICallback() {
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
        long salt = MidGenerator.gen();

        if (mid == 0) {
            mid = MidGenerator.gen();
        }

        Map payload = new HashMap();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);
        payload.put("mtype", mtype);
        payload.put("from", from);
        payload.put("mid", mid);
        payload.put("msg", msg);
        payload.put("attrs", attrs);
        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("broadcastmsg");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        final long fmid = (long) payload.get("mid");
        final FPCallback.ICallback cb = callback;
        this.sendQuest(data, new FPCallback.ICallback() {
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
     * @param {byte[]}                  mtypes
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
     * @param {boolean}                 deleted
     * @param {String}                  msg
     * @param {String}                  attrs
     * @param {long}                    mtime
     * </GroupMsg>
     */
    public void getGroupMessage(long gid, boolean desc, int num, long begin, long end, long lastid, byte[] mtypes, int timeout, FPCallback.ICallback callback) {
        long salt = MidGenerator.gen();
        Map payload = new HashMap();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);
        payload.put("gid", gid);
        payload.put("desc", desc);
        payload.put("num", num);

        if (begin > 0) {
            payload.put("begin", begin);
        }

        if (end > 0) {
            payload.put("end", end);
        }

        if (lastid > 0) {
            payload.put("lastid", lastid);
        }

        if (mtypes != null && mtypes.length > 0) {
            payload.put("mtypes", mtypes);
        }

        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("getgroupmsg");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                if (callback == null) {
                    return;
                }

                Map payload = (Map) cbd.getPayload();

                if (payload != null && payload.containsKey("msgs")) {
                    List list = (ArrayList) payload.get("msgs");

                    for (int i = 0; i < list.size(); i++) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        List items = (ArrayList) list.get(i);
                        map.put("id", items.get(0));
                        map.put("from", items.get(1));
                        map.put("mtype", items.get(2));
                        map.put("mid", items.get(3));
                        map.put("deleted", items.get(4));
                        map.put("msg", items.get(5));
                        map.put("attrs", items.get(6));
                        map.put("mtime", items.get(7));
                        byte mtype = (byte) map.get("mtype");

                        if (mtype == 30) {
                            map.remove("mtype");
                        }

                        list.set(i, map);
                    }

                    callback.callback(new CallbackData(payload));
                    return;
                }

                callback.callback(cbd);
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
     * @param {byte[]}                  mtypes
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
     * @param {boolean}                 deleted
     * @param {String}                  msg
     * @param {String}                  attrs
     * @param {long}                    mtime
     * </RoomMsg>
     */
    public void getRoomMessage(long rid, boolean desc, int num, long begin, long end, long lastid, byte[] mtypes, int timeout, FPCallback.ICallback callback) {
        long salt = MidGenerator.gen();
        Map payload = new HashMap();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);
        payload.put("rid", rid);
        payload.put("desc", desc);
        payload.put("num", num);

        if (begin > 0) {
            payload.put("begin", begin);
        }

        if (end > 0) {
            payload.put("end", end);
        }

        if (lastid > 0) {
            payload.put("lastid", lastid);
        }

        if (mtypes != null && mtypes.length > 0) {
            payload.put("mtypes", mtypes);
        }

        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("getroommsg");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                if (callback == null) {
                    return;
                }

                Map payload = (Map) cbd.getPayload();

                if (payload != null && payload.containsKey("msgs")) {
                    List list = (ArrayList) payload.get("msgs");

                    for (int i = 0; i < list.size(); i++) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        List items = (ArrayList) list.get(i);
                        map.put("id", items.get(0));
                        map.put("from", items.get(1));
                        map.put("mtype", items.get(2));
                        map.put("mid", items.get(3));
                        map.put("deleted", items.get(4));
                        map.put("msg", items.get(5));
                        map.put("attrs", items.get(6));
                        map.put("mtime", items.get(7));
                        long mtype = (long) map.get("mtype");

                        if (mtype == 30) {
                            map.remove("mtype");
                        }

                        list.set(i, map);
                    }

                    callback.callback(new CallbackData(payload));
                    return;
                }

                callback.callback(cbd);
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
     * @param {byte[]}                  mtypes
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
     * @param {boolean}                 deleted
     * @param {String}                  msg
     * @param {String}                  attrs
     * @param {long}                    mtime
     * </BroadcastMsg>
     */
    public void getBroadcastMessage(boolean desc, int num, long begin, long end, long lastid, byte[] mtypes, int timeout, FPCallback.ICallback callback) {
        long salt = MidGenerator.gen();
        Map payload = new HashMap();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);
        payload.put("desc", desc);
        payload.put("num", num);

        if (begin > 0) {
            payload.put("begin", begin);
        }

        if (end > 0) {
            payload.put("end", end);
        }

        if (lastid > 0) {
            payload.put("lastid", lastid);
        }

        if (mtypes != null && mtypes.length > 0) {
            payload.put("mtypes", mtypes);
        }

        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("getbroadcastmsg");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                if (callback == null) {
                    return;
                }

                Map payload = (Map) cbd.getPayload();

                if (payload != null && payload.containsKey("msgs")) {
                    List list = (ArrayList) payload.get("msgs");

                    for (int i = 0; i < list.size(); i++) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        List items = (ArrayList) list.get(i);
                        map.put("id", items.get(0));
                        map.put("from", items.get(1));
                        map.put("mtype", items.get(2));
                        map.put("mid", items.get(3));
                        map.put("deleted", items.get(4));
                        map.put("msg", items.get(5));
                        map.put("attrs", items.get(6));
                        map.put("mtime", items.get(7));
                        long mtype = (long) map.get("mtype");

                        if (mtype == 30) {
                            map.remove("mtype");
                        }

                        list.set(i, map);
                    }

                    callback.callback(new CallbackData(payload));
                    return;
                }

                callback.callback(cbd);
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
     * @param {byte[]}                  mtypes
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
     * @param {boolean}                 deleted
     * @param {String}                  msg
     * @param {String}                  attrs
     * @param {long}                    mtime
     * </P2PMsg>
     */
    public void getP2PMessage(long uid, long ouid, boolean desc, int num, long begin, long end, long lastid, byte[] mtypes, int timeout, FPCallback.ICallback callback) {
        long salt = MidGenerator.gen();
        Map payload = new HashMap();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);
        payload.put("uid", uid);
        payload.put("ouid", ouid);
        payload.put("desc", desc);
        payload.put("num", num);

        if (begin > 0) {
            payload.put("begin", begin);
        }

        if (end > 0) {
            payload.put("end", end);
        }

        if (lastid > 0) {
            payload.put("lastid", lastid);
        }

        if (mtypes != null && mtypes.length > 0) {
            payload.put("mtypes", mtypes);
        }

        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("getp2pmsg");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                if (callback == null) {
                    return;
                }

                Map payload = (Map) cbd.getPayload();

                if (payload != null && payload.containsKey("msgs")) {
                    List list = (ArrayList) payload.get("msgs");

                    for (int i = 0; i < list.size(); i++) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        List items = (ArrayList) list.get(i);
                        map.put("id", items.get(0));
                        map.put("direction", items.get(1));
                        map.put("mtype", items.get(2));
                        map.put("mid", items.get(3));
                        map.put("deleted", items.get(4));
                        map.put("msg", items.get(5));
                        map.put("attrs", items.get(6));
                        map.put("mtime", items.get(7));
                        long mtype = (long) map.get("mtype");

                        if (mtype == 30) {
                            map.remove("mtype");
                        }

                        list.set(i, map);
                    }

                    callback.callback(new CallbackData(payload));
                    return;
                }

                callback.callback(cbd);
            }
        }, timeout);
    }

    /**
     *
     * ServerGate (2j)
     *
     * @param {long}                    mid
     * @param {long}                    from
     * @param {long}                    xid
     * @param {byte}                    type
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
    public void deleteMessage(long mid, long from, long xid, byte type, int timeout, FPCallback.ICallback callback) {
        long salt = MidGenerator.gen();
        Map payload = new HashMap();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);
        payload.put("mid", mid);
        payload.put("from", from);
        payload.put("xid", xid);
        payload.put("type", type);
        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("delmsg");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, callback, timeout);
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
        this.sendMessage(from, to, (byte)30, msg, attrs, mid, timeout, callback);
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
        this.sendMessages(from, tos, (byte)30, msg, attrs, mid, timeout, callback);
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
        this.sendGroupMessage(from, gid, (byte)30, msg, attrs, mid, timeout, callback);
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
        this.sendRoomMessage(from, rid, (byte)30, msg, attrs, mid, timeout, callback);
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
        this.broadcastMessage(from, (byte)30, msg, attrs, mid, timeout, callback);
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
     * @param {long}                    mid
     * @param {boolean}                 deleted
     * @param {String}                  msg
     * @param {String}                  attrs
     * @param {long}                    mtime
     * </GroupMsg>
     */
    public void getGroupChat(long gid, boolean desc, int num, long begin, long end, long lastid, int timeout, FPCallback.ICallback callback) {
        this.getGroupMessage(gid, desc, num, begin, end, lastid, new byte[] {(byte)30}, timeout, callback);
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
     * @param {long}                    mid
     * @param {boolean}                 deleted
     * @param {String}                  msg
     * @param {String}                  attrs
     * @param {long}                    mtime
     * </RoomMsg>
     */
    public void getRoomChat(long rid, boolean desc, int num, long begin, long end, long lastid, int timeout, FPCallback.ICallback callback) {
        this.getRoomMessage(rid, desc, num, begin, end, lastid, new byte[] {(byte)30}, timeout, callback);
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
     * @param {long}                    mid
     * @param {boolean}                 deleted
     * @param {String}                  msg
     * @param {String}                  attrs
     * @param {long}                    mtime
     * </BroadcastMsg>
     */
    public void getBroadcastChat(boolean desc, int num, long begin, long end, long lastid, int timeout, FPCallback.ICallback callback) {
        this.getBroadcastMessage(desc, num, begin, end, lastid, new byte[] {(byte)30}, timeout, callback);
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
     * @param {long}                    mid
     * @param {boolean}                 deleted
     * @param {String}                  msg
     * @param {String}                  attrs
     * @param {long}                    mtime
     * </P2PMsg>
     */
    public void getP2PChat(long uid, long ouid, boolean desc, int num, long begin, long end, long lastid, int timeout, FPCallback.ICallback callback) {
        this.getP2PMessage(uid, ouid, desc, num, begin, end, lastid, new byte[] {(byte)30}, timeout, callback);
    }

    /**
     *
     * ServerGate (3j)
     *
     * @param {long}                    mid
     * @param {long}                    from
     * @param {long}                    xid
     * @param {byte}                    type
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
    public void deleteChat(long mid, long from, long xid, byte type, int timeout, FPCallback.ICallback callback) {
        this.deleteMessage(mid, from, xid, type, timeout, callback);
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
        long salt = MidGenerator.gen();
        Map payload = new HashMap();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);
        payload.put("text", text);
        payload.put("dst", dst);

        if (src != null && !src.isEmpty()) {
            payload.put("src", src);
        }

        if (type != null && !type.isEmpty()) {
            payload.put("type", type);
        }

        if (profanity != null && !profanity.isEmpty()) {
            payload.put("profanity", profanity);
        }

        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("translate");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, callback, timeout);
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
        long salt = MidGenerator.gen();
        Map payload = new HashMap();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);
        payload.put("text", text);

        if (action != null && !action.isEmpty()) {
            payload.put("action", action);
        }

        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("profanity");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, callback, timeout);
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
        Map payload = new HashMap();
        payload.put("from", from);
        payload.put("cmd", cmd);

        if (tos != null && tos.size() > 0) {
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
        long salt = MidGenerator.gen();
        Map payload = new HashMap();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);
        payload.put("uids", uids);
        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("getonlineusers");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                if (callback == null) {
                    return;
                }

                Map payload = (Map) cbd.getPayload();

                if (payload != null && payload.containsKey("uids")) {
                    callback.callback(new CallbackData(payload.get("uids")));
                    return;
                }

                callback.callback(cbd);
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
        long salt = MidGenerator.gen();
        Map payload = new HashMap();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);
        payload.put("uid", uid);
        payload.put("btime", btime);
        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("addprojectblack");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, callback, timeout);
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
        long salt = MidGenerator.gen();
        Map payload = new HashMap();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);
        payload.put("uid", uid);
        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("removeprojectblack");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, callback, timeout);
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
        long salt = MidGenerator.gen();
        Map payload = new HashMap();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);
        payload.put("uid", uid);
        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("isprojectblack");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, callback, timeout);
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
        long salt = MidGenerator.gen();
        Map payload = new HashMap();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);
        payload.put("uid", uid);

        if (oinfo != null && !oinfo.isEmpty()) {
            payload.put("oinfo", oinfo);
        }

        if (pinfo != null && !pinfo.isEmpty()) {
            payload.put("pinfo", pinfo);
        }

        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("setuserinfo");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, callback, timeout);
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
        long salt = MidGenerator.gen();
        Map payload = new HashMap();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);
        payload.put("uid", uid);
        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("getuserinfo");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, callback, timeout);
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
        long salt = MidGenerator.gen();
        Map payload = new HashMap();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);
        payload.put("uids", uids);
        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("getuseropeninfo");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                if (callback == null) {
                    return;
                }

                Map payload = (Map) cbd.getPayload();

                if (payload != null && payload.containsKey("info")) {
                    callback.callback(new CallbackData(payload.get("info")));
                    return;
                }

                callback.callback(cbd);
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
        long salt = MidGenerator.gen();
        Map payload = new HashMap();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);
        payload.put("uid", uid);
        payload.put("friends", friends);
        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("addfriends");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, callback, timeout);
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
        long salt = MidGenerator.gen();
        Map payload = new HashMap();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);
        payload.put("uid", uid);
        payload.put("friends", friends);
        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("delfriends");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, callback, timeout);
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
        long salt = MidGenerator.gen();
        Map payload = new HashMap();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);
        payload.put("uid", uid);
        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("getfriends");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                if (callback == null) {
                    return;
                }

                Map payload = (Map) cbd.getPayload();

                if (payload != null && payload.containsKey("uids")) {
                    callback.callback(new CallbackData(payload.get("uids")));
                    return;
                }

                callback.callback(cbd);
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
        long salt = MidGenerator.gen();
        Map payload = new HashMap();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);
        payload.put("uid", uid);
        payload.put("fuid", fuid);
        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("isfriend");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, callback, timeout);
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
        long salt = MidGenerator.gen();
        Map payload = new HashMap();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);
        payload.put("uid", uid);
        payload.put("fuids", fuids);
        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("isfriends");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                if (callback == null) {
                    return;
                }

                Map payload = (Map) cbd.getPayload();

                if (payload != null && payload.containsKey("fuids")) {
                    callback.callback(new CallbackData(payload.get("fuids")));
                    return;
                }

                callback.callback(cbd);
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
        long salt = MidGenerator.gen();
        Map payload = new HashMap();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);
        payload.put("gid", gid);
        payload.put("uids", uids);
        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("addgroupmembers");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, callback, timeout);
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
        long salt = MidGenerator.gen();
        Map payload = new HashMap();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);
        payload.put("gid", gid);
        payload.put("uids", uids);
        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("delgroupmembers");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, callback, timeout);
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
        long salt = MidGenerator.gen();
        Map payload = new HashMap();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);
        payload.put("gid", gid);
        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("delgroup");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, callback, timeout);
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
        long salt = MidGenerator.gen();
        Map payload = new HashMap();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);
        payload.put("gid", gid);
        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("getgroupmembers");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                if (callback == null) {
                    return;
                }

                Map payload = (Map) cbd.getPayload();

                if (payload != null && payload.containsKey("uids")) {
                    callback.callback(new CallbackData(payload.get("uids")));
                    return;
                }

                callback.callback(cbd);
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
        long salt = MidGenerator.gen();
        Map payload = new HashMap();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);
        payload.put("gid", gid);
        payload.put("uid", uid);
        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("isgroupmember");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, callback, timeout);
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
        long salt = MidGenerator.gen();
        Map payload = new HashMap();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);
        payload.put("uid", uid);
        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("getusergroups");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                if (callback == null) {
                    return;
                }

                Map payload = (Map) cbd.getPayload();

                if (payload != null && payload.containsKey("gids")) {
                    callback.callback(new CallbackData(payload.get("gids")));
                    return;
                }

                callback.callback(cbd);
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
        long salt = MidGenerator.gen();
        Map payload = new HashMap();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);
        payload.put("gid", gid);
        payload.put("uid", uid);
        payload.put("btime", btime);
        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("addgroupban");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, callback, timeout);
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
        long salt = MidGenerator.gen();
        Map payload = new HashMap();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);
        payload.put("gid", gid);
        payload.put("uid", uid);
        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("removegroupban");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, callback, timeout);
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
        long salt = MidGenerator.gen();
        Map payload = new HashMap();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);
        payload.put("gid", gid);
        payload.put("uid", uid);
        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("isbanofgroup");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, callback, timeout);
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
        long salt = MidGenerator.gen();
        Map payload = new HashMap();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);
        payload.put("gid", gid);

        if (oinfo != null && !oinfo.isEmpty()) {
            payload.put("oinfo", oinfo);
        }

        if (pinfo != null && !pinfo.isEmpty()) {
            payload.put("pinfo", pinfo);
        }

        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("setgroupinfo");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, callback, timeout);
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
        long salt = MidGenerator.gen();
        Map payload = new HashMap();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);
        payload.put("gid", gid);
        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("getgroupinfo");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, callback, timeout);
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
        long salt = MidGenerator.gen();
        Map payload = new HashMap();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);
        payload.put("rid", rid);
        payload.put("uid", uid);
        payload.put("btime", btime);
        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("addroomban");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, callback, timeout);
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
        long salt = MidGenerator.gen();
        Map payload = new HashMap();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);
        payload.put("rid", rid);
        payload.put("uid", uid);
        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("removeroomban");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, callback, timeout);
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
        long salt = MidGenerator.gen();
        Map payload = new HashMap();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);
        payload.put("rid", rid);
        payload.put("uid", uid);
        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("isbanofroom");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, callback, timeout);
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
        long salt = MidGenerator.gen();
        Map payload = new HashMap();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);
        payload.put("rid", rid);
        payload.put("uid", uid);
        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("addroommember");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, callback, timeout);
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
        long salt = MidGenerator.gen();
        Map payload = new HashMap();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);
        payload.put("rid", rid);
        payload.put("uid", uid);
        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("delroommember");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, callback, timeout);
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
        long salt = MidGenerator.gen();
        Map payload = new HashMap();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);
        payload.put("rid", rid);

        if (oinfo != null && !oinfo.isEmpty()) {
            payload.put("oinfo", oinfo);
        }

        if (pinfo != null && !pinfo.isEmpty()) {
            payload.put("pinfo", pinfo);
        }

        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("setroominfo");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, callback, timeout);
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
        long salt = MidGenerator.gen();
        Map payload = new HashMap();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);
        payload.put("rid", rid);
        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("getroominfo");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, callback, timeout);
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
        long salt = MidGenerator.gen();
        Map payload = new HashMap();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);

        if (gids != null && gids.size() > 0) {
            payload.put("gids", gids);
        }

        if (rids != null && rids.size() > 0) {
            payload.put("rids", rids);
        }

        if (uids != null && uids.size() > 0) {
            payload.put("uids", uids);
        }

        if (events != null && events.size() > 0) {
            payload.put("events", events);
        }

        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("addlisten");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, callback, timeout);
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
        long salt = MidGenerator.gen();
        Map payload = new HashMap();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);

        if (gids != null && gids.size() > 0) {
            payload.put("gids", gids);
        }

        if (rids != null && rids.size() > 0) {
            payload.put("rids", rids);
        }

        if (uids != null && uids.size() > 0) {
            payload.put("uids", uids);
        }

        if (events != null && events.size() > 0) {
            payload.put("events", events);
        }

        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("removelisten");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, callback, timeout);
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
        long salt = MidGenerator.gen();
        Map payload = new HashMap();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);
        payload.put("p2p", p2p);
        payload.put("group", group);
        payload.put("room", room);
        payload.put("ev", ev);
        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("setlisten");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, callback, timeout);
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
        long salt = MidGenerator.gen();
        Map payload = new HashMap();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);

        if (gids != null && gids.size() > 0) {
            payload.put("gids", gids);
        }

        if (rids != null && rids.size() > 0) {
            payload.put("rids", rids);
        }

        if (uids != null && uids.size() > 0) {
            payload.put("uids", uids);
        }

        if (events != null && events.size() > 0) {
            payload.put("events", events);
        }

        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("setlisten");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, callback, timeout);
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
        long salt = MidGenerator.gen();
        Map payload = new HashMap();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);
        payload.put("uid", uid);
        payload.put("key", key);
        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("dataget");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, callback, timeout);
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
        long salt = MidGenerator.gen();
        Map payload = new HashMap();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);
        payload.put("uid", uid);
        payload.put("key", key);
        payload.put("val", val);
        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("dataset");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, callback, timeout);
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
        long salt = MidGenerator.gen();
        Map payload = new HashMap();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);
        payload.put("uid", uid);
        payload.put("key", key);
        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("datadel");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, callback, timeout);
    }


    /**
     *
     * @param {String}      filePath
     */
    public byte[] loadFile(String filePath) {
        return new LoadFile().read(filePath);
    }

    /**
     *
     * fileGate (1)
     *
     * @param {long}                    from
     * @param {long}                    to
     * @param {byte}                    mtype
     * @param {byte[]}                  fileBytes
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
    public void sendFile(long from, long to, byte mtype, byte[] fileBytes, long mid, int timeout, FPCallback.ICallback callback) {
        if (fileBytes == null || fileBytes.length <= 0) {
            this.getEvent().fireEvent(new EventData(this, "error", new Exception("empty file bytes!")));
            return;
        }

        Map ops = new HashMap();
        ops.put("cmd", "sendfile");
        ops.put("from", from);
        ops.put("to", to);
        ops.put("mtype", mtype);
        ops.put("file", fileBytes);
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
    public void sendFiles(long from, List<Long> tos, byte mtype, byte[] fileBytes, long mid, int timeout, FPCallback.ICallback callback) {
        if (fileBytes == null || fileBytes.length <= 0) {
            this.getEvent().fireEvent(new EventData(this, "error", new Exception("empty file bytes!")));
            return;
        }

        Map ops = new HashMap();
        ops.put("cmd", "sendfiles");
        ops.put("from", from);
        ops.put("tos", tos);
        ops.put("mtype", mtype);
        ops.put("file", fileBytes);
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
    public void sendGroupFile(long from, long gid, byte mtype, byte[] fileBytes, long mid, int timeout, FPCallback.ICallback callback) {
        if (fileBytes == null || fileBytes.length <= 0) {
            this.getEvent().fireEvent(new EventData(this, "error", new Exception("empty file bytes!")));
            return;
        }

        Map ops = new HashMap();
        ops.put("cmd", "sendgroupfile");
        ops.put("from", from);
        ops.put("gid", gid);
        ops.put("mtype", mtype);
        ops.put("file", fileBytes);
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
    public void sendRoomFile(long from, long rid, byte mtype, byte[] fileBytes, long mid, int timeout, FPCallback.ICallback callback) {
        if (fileBytes == null || fileBytes.length <= 0) {
            this.getEvent().fireEvent(new EventData(this, "error", new Exception("empty file bytes!")));
            return;
        }

        Map ops = new HashMap();
        ops.put("cmd", "sendroomfile");
        ops.put("from", from);
        ops.put("rid", rid);
        ops.put("mtype", mtype);
        ops.put("file", fileBytes);
        this.fileSendProcess(ops, mid, timeout, callback);
    }

    /**
     *
     * filegate (5)
     *
     * @param {long}                    from
     * @param {byte}                    mtype
     * @param {byte[]}                  fileBytes
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
    public void broadcastFile(long from, byte mtype, byte[] fileBytes, long mid, int timeout, FPCallback.ICallback callback) {
        if (fileBytes == null || fileBytes.length <= 0) {
            this.getEvent().fireEvent(new EventData(this, "error", new Exception("empty file bytes!")));
            return;
        }

        Map ops = new HashMap();
        ops.put("cmd", "broadcastfile");
        ops.put("from", from);
        ops.put("mtype", mtype);
        ops.put("file", fileBytes);
        this.fileSendProcess(ops, mid, timeout, callback);
    }

    private void fileSendProcess(Map ops, long mid, int timeout, FPCallback.ICallback callback) {
        Map payload = new HashMap();

        if (mid == 0) {
            mid = MidGenerator.gen();
        }

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
        final FPCallback.ICallback cb = callback;
        this.filetoken(payload, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Exception exception = cbd.getException();

                if (exception != null) {
                    self.getEvent().fireEvent(new EventData(self, "error", exception));
                    return;
                }

                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    String token = (String) payload.get("token");
                    String endpoint = (String) payload.get("endpoint");

                    if (token.isEmpty() || endpoint.isEmpty()) {
                        self.getEvent().fireEvent(new EventData(self, "error", new Exception(obj.toString())));
                        return;
                    }

                    FileClient fileClient = new FileClient(endpoint, ftimeout);
                    payload = new HashMap();
                    payload.put("pid", self._pid);
                    payload.put("mtype", fops.get("mtype"));
                    payload.put("mid", fmid);
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

                    fileClient.send((String) fops.get("cmd"), (byte[]) fops.get("file"), token, payload, ftimeout, cb);
                }
            }
        }, timeout);
    }

    private void filetoken(Map payload, FPCallback.ICallback callback, int timeout) {
        long salt = MidGenerator.gen();
        payload.put("pid", this._pid);
        payload.put("sign", this.genSign(salt));
        payload.put("salt", salt);
        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("filetoken");
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, callback, timeout);
    }

    private String genSign(long slat) {
        StringBuffer sb = new StringBuffer(Integer.toString(this._pid));
        sb.append(":");
        sb.append(this._secret);
        sb.append(":");
        sb.append(Long.toString(slat));
        return this.md5(sb.toString());
    }
}

class FileClient extends BaseClient {

    public FileClient(String endpoint, int timeout) {
        super(endpoint, false, timeout);
    }

    public FileClient(String host, int port, int timeout) {
        super(host, port, false, timeout);
    }

    @Override
    protected void init(String host, int port, boolean reconnect, int timeout) {
        super.init(host, port, reconnect, timeout);
        FileClient self = this;
        this.getEvent().addListener("connect", new FPEvent.IListener() {
            @Override
            public void fpEvent(EventData evd) {
                self.onConnect();
            }
        });
        this.getEvent().addListener("close", new FPEvent.IListener() {
            @Override
            public void fpEvent(EventData evd) {
                self.onClose();
            }
        });
        this.getEvent().addListener("error", new FPEvent.IListener() {
            @Override
            public void fpEvent(EventData evd) {
                self.onException(evd.getException());
            }
        });
    }

    /**
     * @param {String}               method
     * @param {byte[]}               fileBytes
     * @param {String}               token
     * @param {Map}                  payload
     * @param {int}                  timeout
     * @param {FPCallback.ICallback} callback
     */
    public void send(String method, byte[] fileBytes, String token, Map payload, int timeout, FPCallback.ICallback callback) {
        String fileMd5 = this.md5(fileBytes).toLowerCase();
        String sign = this.md5(fileMd5.concat(":").concat(token)).toLowerCase();

        if (sign.isEmpty()) {
            this.getEvent().fireEvent(new EventData(this, "error", new Exception("wrong sign!")));
            return;
        }

        if (!this.hasConnect()) {
            this.connect();
        }

        JsonHelper.IJson json = JsonHelper.getInstance().getJson();
        Map attrs = new HashMap();
        attrs.put("sign", sign);
        payload.put("token", token);
        payload.put("file", fileBytes);
        payload.put("attrs", json.toJSON(attrs));
        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod(method);
        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {
            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        data.setPayload(bytes);
        final long fmid = (long) payload.get("mid");
        final FPCallback.ICallback cb = callback;
        final FileClient self = this;
        this.sendQuest(data, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                cbd.setMid(fmid);
                self.destroy();

                if (cb != null) {
                    cb.callback(cbd);
                }
            }
        }, timeout);
    }

    private void onConnect() {
        System.out.println("File client connected!");
    }

    private void onClose() {
        System.out.println("File client closed!");
        this.destroy();
    }

    private void onException(Exception ex) {
        ex.printStackTrace();
    }
}

class BaseClient extends FPClient {

    public BaseClient(String endpoint, boolean reconnect, int timeout) {
        super(endpoint, reconnect, timeout);
        ThreadPool.getInstance().startTimerThread();
    }

    public BaseClient(String host, int port, boolean reconnect, int timeout) {
        super(host, port, reconnect, timeout);
        ThreadPool.getInstance().startTimerThread();
    }

    @Override
    protected void init(String host, int port, boolean reconnect, int timeout) {
        super.init(host, port, reconnect, timeout);
    }

    @Override
    public void sendQuest(FPData data, FPCallback.ICallback callback, int timeout) {
        super.sendQuest(data, this.questCallback(callback), timeout);
    }

    @Override
    public CallbackData sendQuest(FPData data, int timeout) {
        CallbackData cbd = null;

        try {
            cbd = super.sendQuest(data, timeout);
        } catch (Exception ex) {
            this.getEvent().fireEvent(new EventData(this, "error", ex));
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
                        PayloadPacker packer = new PayloadPacker();
                        Map map = new HashMap();
                        map.put("publicKey", encryptor.cryptoInfo().selfPublicKey);
                        map.put("streamMode", encryptor.cryptoInfo().streamMode);
                        map.put("bits", encryptor.cryptoInfo().keyLength);

                        try {
                            packer.pack(map);
                            bytes = packer.toByteArray();
                        } catch (Exception ex) {
                            ex.printStackTrace();
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
     * @param {String}  curve
     * @param {String}  derPath
     * @param {boolean} streamMode
     * @param {boolean} reinforce
     */
    public void connect(String curve, String derPath, boolean streamMode, boolean reinforce) {
        byte[] bytes = new LoadFile().read(derPath);
        this.connect(curve, bytes, streamMode, reinforce);
    }

    /**
     * @param {byte[]} bytes
     */
    public String md5(byte[] bytes) {
        byte[] md5Binary = new byte[0];

        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(bytes);
            md5Binary = md5.digest();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        return this.bytesToHexString(md5Binary, false);
    }

    /**
     * @param {String} str
     */
    public String md5(String str) {
        byte[] md5Binary = new byte[0];

        try {
            byte[] bytes = str.getBytes("UTF-8");
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(bytes);
            md5Binary = md5.digest();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        return this.bytesToHexString(md5Binary, false);
    }

    /**
     * @param {byte[]}  bytes
     * @param {boolean} isLowerCase
     */
    public String bytesToHexString(byte[] bytes, boolean isLowerCase) {
        String from = isLowerCase ? "%02x" : "%02X";
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        Formatter formatter = new Formatter(sb);

        for (byte b : bytes) {
            formatter.format(from, b);
        }

        return sb.toString();
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
                JsonHelper.IJson json = JsonHelper.getInstance().getJson();
                payload = json.toMap(data.jsonPayload());
            }

            if (data.getFlag() == 1) {
                PayloadUnpacker unpacker = new PayloadUnpacker(data.msgpackPayload());

                try {
                    payload = unpacker.unpack();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            if (this.getPackage().isAnswer(data)) {
                isAnswerException = data.getSS() != 0;
            }
        }

        cbd.checkException(isAnswerException, payload);
    }
}

class LoadFile {

    /**
     * @param {String} derPath
     */
    public byte[] read(String derPath) {
        File f = new File(derPath);

        if (!f.exists()) {
            System.out.println(new String("file not exists! path: ").concat(f.getAbsolutePath()));
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
            ex.printStackTrace();
        } finally {
            try {
                in.close();
                bos.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return null;
    }
}
