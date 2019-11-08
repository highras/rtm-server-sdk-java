package com.rtm;

import java.nio.charset.Charset;
import java.util.*;

import com.fpnn.ErrorRecorder;
import com.fpnn.FPData;
import com.fpnn.FPManager;
import com.fpnn.FPProcessor;
import com.rtm.json.JsonHelper;
import com.rtm.msgpack.PayloadUnpacker;

public class RTMProcessor implements FPProcessor.IProcessor {

    public interface IService {
        void Service(Map<String, Object> data);
    }

    class PingLocker {
        public int status = 0;
    }

    private static String JSON_PAYLOAD = "{}";
    private static byte[] MSGPACK_PAYLOAD = { (byte)0x80 };

    private Object self_locker = new Object();
    private Map<String, Long> _duplicateMap = new HashMap<String, Long>();
    private Map<String, IService> _actionMap = new HashMap<String, IService>();

    public void destroy() {
        this.clearPingTimestamp();

        synchronized (self_locker) {
            this._actionMap.clear();
            this._duplicateMap.clear();
        }
    }

    @Override
    public void service(FPData data, FPProcessor.IAnswer answer) {
        if (data == null) {
            return;
        }

        Map<String, Object> payload = null;

        if (data.getFlag() == 0 && data.jsonPayload() != null) {
            answer.sendAnswer(JSON_PAYLOAD, false);

            try {
                JsonHelper.IJson json = JsonHelper.getInstance().getJson();
                payload = json.toMap(data.jsonPayload());
            } catch (Exception ex) {
                ErrorRecorder.getInstance().recordError(ex);
            }
        }

        if (data.getFlag() == 1 && data.msgpackPayload() != null) {
            answer.sendAnswer(MSGPACK_PAYLOAD, false);

            try {
                PayloadUnpacker unpacker = new PayloadUnpacker(data.msgpackPayload());
                payload = unpacker.unpack();
            } catch (Exception ex) {
                ErrorRecorder.getInstance().recordError(ex);
            }
        }

        if (payload != null) {
            if (payload.containsKey("mid")) {
                payload.put("mid", wantLong(payload, "mid"));
            }

            if (payload.containsKey("from")) {
                payload.put("from", wantLong(payload, "from"));
            }

            if (payload.containsKey("to")) {
                payload.put("to", wantLong(payload, "to"));
            }

            if (payload.containsKey("gid")) {
                payload.put("gid", wantLong(payload, "gid"));
            }

            if (payload.containsKey("rid")) {
                payload.put("rid", wantLong(payload, "rid"));
            }

            if (payload.containsKey("mtype")) {
                payload.put("mtype", wantByte(payload, "mtype"));
            }

            if (payload.containsKey("mtime")) {
                payload.put("mtime", wantLong(payload, "mtime"));
            }

            try {
                RTMProcessor.class.getMethod(data.getMethod(), Map.class).invoke(this, payload);
            } catch (Exception ex) {
                ErrorRecorder.getInstance().recordError(ex);
            }
        }
    }

    @Override
    public boolean hasPushService(String name) {
        if (name == null || name.isEmpty()) {
            return false;
        }
        return true;
    }

    public void addPushService(String name, IService is) {
        if (name == null || name.isEmpty()) {
            return;
        }

        synchronized (self_locker) {
            if (!this._actionMap.containsKey(name)) {
                this._actionMap.put(name, is);
            } else {
                System.out.println("push service exist");
            }
        }
    }

    public void removePushService(String name) {
        if (name == null || name.isEmpty()) {
            return;
        }

        synchronized (self_locker) {
            if (this._actionMap.containsKey(name)) {
                this._actionMap.remove(name);
            }
        }
    }

    private void pushService(String name, Map<String, Object> data) {
        synchronized (self_locker) {
            if (this._actionMap.containsKey(name)) {
                IService is = this._actionMap.get(name);
                try {
                    if (is != null) {
                        is.Service(data);
                    }
                } catch (Exception ex) {
                    ErrorRecorder.getInstance().recordError(ex);
                }
            }
        }
    }

    /**
     *
     * serverPush (1a)
     *
     * @param {Map}         data
     */
    public void ping(Map<String, Object> data) {
        this.pushService(RTMConfig.SERVER_PUSH.recvPing, data);

        synchronized (ping_locker) {
            this._lastPingTimestamp = FPManager.getInstance().getMilliTimestamp();
        }
    }

    /**
     *
     * serverPush (2a)
     *
     * @param {long}        data.from
     * @param {long}        data.to
     * @param {byte}        data.mtype
     * @param {long}        data.mid
     * @param {String}      data.msg
     * @param {String}      data.attrs
     * @param {long}        data.mtime
     */
    public void pushmsg(Map<String, Object> data) {
        byte mtype = 0;
        String name = RTMConfig.SERVER_PUSH.recvMessage;

        if (data == null) {
            this.pushService(name, data);
            return;
        }

        if (data.containsKey("mid") && data.containsKey("from")) {
            if (!this.checkMid(1, (long)data.get("mid"), (long)data.get("from"), 0)) {
                return;
            }
        }

        if (data.containsKey("mtype")) {
            mtype = (byte) data.get("mtype");
        }

        if (mtype != RTMConfig.CHAT_TYPE.audio) {
            if (data.containsKey("msg") && data.get("msg") instanceof byte[]) {
                String msg = new String((byte[]) data.get("msg"), Charset.forName("UTF-8"));
                data.put("msg", msg);
            }
        }

        if (mtype == RTMConfig.CHAT_TYPE.text) {
            data.remove("mtype");
            name = RTMConfig.SERVER_PUSH.recvChat;
        }

        if (mtype == RTMConfig.CHAT_TYPE.audio) {
            data.remove("mtype");
            name = RTMConfig.SERVER_PUSH.recvAudio;
        }

        if (mtype == RTMConfig.CHAT_TYPE.cmd) {
            data.remove("mtype");
            name = RTMConfig.SERVER_PUSH.recvCmd;
        }

        if (mtype >= 40 && mtype <= 50) {
            name = RTMConfig.SERVER_PUSH.recvFile;
        }

        this.pushService(name, data);
    }

    /**
     *
     * ServerGate (2b)
     *
     * @param {long}        data.from
     * @param {long}        data.gid
     * @param {byte}        data.mtype
     * @param {long}        data.mid
     * @param {String}      data.msg
     * @param {String}      data.attrs
     * @param {long}        data.mtime
     */
    public void pushgroupmsg(Map<String, Object> data) {
        byte mtype = 0;
        String name = RTMConfig.SERVER_PUSH.recvGroupMessage;

        if (data == null) {
            this.pushService(name, data);
            return;
        }

        if (data.containsKey("mid") && data.containsKey("from") && data.containsKey("gid")) {
            if (!this.checkMid(2, (long)data.get("mid"), (long)data.get("from"), (long)data.get("gid"))) {
                return;
            }
        }

        if (data.containsKey("mtype")) {
            mtype = (byte) data.get("mtype");
        }

        if (mtype != RTMConfig.CHAT_TYPE.audio) {
            if (data.containsKey("msg") && data.get("msg") instanceof byte[]) {
                String msg = new String((byte[]) data.get("msg"), Charset.forName("UTF-8"));
                data.put("msg", msg);
            }
        }

        if (mtype == RTMConfig.CHAT_TYPE.text) {
            data.remove("mtype");
            name = RTMConfig.SERVER_PUSH.recvGroupChat;
        }

        if (mtype == RTMConfig.CHAT_TYPE.audio) {
            data.remove("mtype");
            name = RTMConfig.SERVER_PUSH.recvGroupAudio;
        }

        if (mtype == RTMConfig.CHAT_TYPE.cmd) {
            data.remove("mtype");
            name = RTMConfig.SERVER_PUSH.recvGroupCmd;
        }

        if (mtype >= 40 && mtype <= 50) {
            name = RTMConfig.SERVER_PUSH.recvGroupFile;
        }

        this.pushService(name, data);
    }

    /**
     *
     * ServerGate (2c)
     *
     * @param {long}        data.from
     * @param {long}        data.rid
     * @param {byte}        data.mtype
     * @param {long}        data.mid
     * @param {String}      data.msg
     * @param {String}      data.attrs
     * @param {long}        data.mtime
     */
    public void pushroommsg(Map<String, Object> data) {
        byte mtype = 0;
        String name = RTMConfig.SERVER_PUSH.recvRoomMessage;

        if (data == null) {
            this.pushService(name, data);
            return;
        }

        if (data.containsKey("mid") && data.containsKey("from") && data.containsKey("rid")) {
            if (!this.checkMid(3, (long)data.get("mid"), (long)data.get("from"), (long)data.get("rid"))) {
                return;
            }
        }

        if (data.containsKey("mtype")) {
            mtype = (byte) data.get("mtype");
        }

        if (mtype != RTMConfig.CHAT_TYPE.audio) {
            if (data.containsKey("msg") && data.get("msg") instanceof byte[]) {
                String msg = new String((byte[]) data.get("msg"), Charset.forName("UTF-8"));
                data.put("msg", msg);
            }
        }

        if (mtype == RTMConfig.CHAT_TYPE.text) {
            data.remove("mtype");
            name = RTMConfig.SERVER_PUSH.recvRoomChat;
        }

        if (mtype == RTMConfig.CHAT_TYPE.audio) {
            data.remove("mtype");
            name = RTMConfig.SERVER_PUSH.recvRoomAudio;
        }

        if (mtype == RTMConfig.CHAT_TYPE.cmd) {
            data.remove("mtype");
            name = RTMConfig.SERVER_PUSH.recvRoomCmd;
        }

        if (mtype >= 40 && mtype <= 50) {
            name = RTMConfig.SERVER_PUSH.recvRoomFile;
        }

        this.pushService(name, data);
    }

    /**
     *
     * serverPush(a)
     *
     * @param {long}            data.from
     * @param {long}            data.to
     * @param {byte}            data.mtype
     * @param {long}            data.mid
     * @param {Url}             data.msg
     * @param {String}          data.attrs
     * @param {long}            data.mtime
     */
    public void pushfile(Map<String, Object> data) {}

    /**
     *
     * serverPush(b)
     *
     * @param {long}            data.from
     * @param {long}            data.gid
     * @param {byte}            data.mtype
     * @param {long}            data.mid
     * @param {Url}             data.msg
     * @param {String}          data.attrs
     * @param {long}            data.mtime
     */
    public void pushgroupfile(Map<String, Object> data) {}

    /**
     *
     * serverPush(c)
     *
     * @param {long}            data.from
     * @param {long}            data.rid
     * @param {byte}            data.mtype
     * @param {long}            data.mid
     * @param {Url}             data.msg
     * @param {String}          data.attrs
     * @param {long}            data.mtime
     */
    public void pushroomfile(Map<String, Object> data) {}

    /**
     *
     * ServerGate (2d)
     *
     * @param {String}      data.event
     * @param {long}        data.uid
     * @param {int}         data.time
     * @param {String}      data.endpoint
     * @param {String}      data.data
     */
    public void pushevent(Map<String, Object> data) {
        this.pushService(RTMConfig.SERVER_PUSH.recvEvent, data);
    }

    /**
     *
     * serverPush (3a)
     *
     * @param {long}            data.from
     * @param {long}            data.to
     * @param {long}            data.mid
     * @param {JsonString}      data.msg
     * @param {String}          data.attrs
     * @param {long}            data.mtime
     *
     * <JsonString>
     * @param {String}          source
     * @param {String}          target
     * @param {String}          sourceText
     * @param {String}          targetText
     * </JsonString>
     */
    public void pushchat(Map<String, Object> data) {}

    /**
     *
     * serverPush(3a')
     *
     * @param {long}            data.from
     * @param {long}            data.to
     * @param {long}            data.mid
     * @param {byte[]}          data.msg
     * @param {String}          data.attrs
     * @param {long}            data.mtime
     */
    public void pushaudio(Map<String, Object> data) {}

    /**
     *
     * serverPush(3a'')
     *
     * @param {long}            data.from
     * @param {long}            data.to
     * @param {long}            data.mid
     * @param {String}          data.msg
     * @param {String}          data.attrs
     * @param {long}            data.mtime
     */
    public void pushcmd(Map<String, Object> data) {}

    /**
     *
     * ServerGate (3b)
     *
     * @param {long}            data.from
     * @param {long}            data.gid
     * @param {long}            data.mid
     * @param {String}          data.msg
     * @param {String}          data.attrs
     * @param {long}            data.mtime
     *
     * <JsonString>
     * @param {String}          source
     * @param {String}          target
     * @param {String}          sourceText
     * @param {String}          targetText
     * </JsonString>
     */
    public void pushgroupchat(Map<String, Object> data) {}

    /**
     *
     * serverPush(3b')
     *
     * @param {long}            data.from
     * @param {long}            data.gid
     * @param {long}            data.mid
     * @param {byte[]}          data.msg
     * @param {String}          data.attrs
     * @param {long}            data.mtime
     */
    public void pushgroupaudio(Map<String, Object> data) {}

    /**
     *
     * serverPush(3b'')
     *
     * @param {long}            data.from
     * @param {long}            data.gid
     * @param {long}            data.mid
     * @param {String}          data.msg
     * @param {String}          data.attrs
     * @param {long}            data.mtime
     */
    public void pushgroupcmd(Map<String, Object> data) {}

    /**
     *
     * ServerGate (3c)
     *
     * @param {long}            data.from
     * @param {long}            data.rid
     * @param {long}            data.mid
     * @param {String}          data.msg
     * @param {String}          data.attrs
     * @param {long}            data.mtime
     *
     * <JsonString>
     * @param {String}          source
     * @param {String}          target
     * @param {String}          sourceText
     * @param {String}          targetText
     * </JsonString>
     */
    public void pushroomchat(Map<String, Object> data) {}

    /**
     *
     * serverPush(3c')
     *
     * @param {long}            data.from
     * @param {long}            data.rid
     * @param {long}            data.mid
     * @param {byte[]}          data.msg
     * @param {String}          data.attrs
     * @param {long}            data.mtime
     */
    public void pushroomaudio(Map<String, Object> data) {}

    /**
     *
     * serverPush(3c'')
     *
     * @param {long}            data.from
     * @param {long}            data.rid
     * @param {long}            data.mid
     * @param {String}          data.msg
     * @param {String}          data.attrs
     * @param {long}            data.mtime
     */
    public void pushroomcmd(Map<String, Object> data) {}

    private long _lastPingTimestamp;
    private PingLocker ping_locker = new PingLocker();

    public long getPingTimestamp() {
        synchronized (ping_locker) {
            return this._lastPingTimestamp;
        }
    }

    public void clearPingTimestamp() {
        synchronized (ping_locker) {
            this._lastPingTimestamp = 0;
        }
    }

    public void initPingTimestamp() {
        synchronized (ping_locker) {
            if (this._lastPingTimestamp == 0) {
                this._lastPingTimestamp = FPManager.getInstance().getMilliTimestamp();
            }
        }
    }

    @Override
    public void onSecond(long timestamp) {
        this.checkExpire(timestamp);
    }

    private boolean checkMid(int type, long mid, long uid, long rgid) {
        StringBuilder sb = new StringBuilder(50);
        sb.append(type);
        sb.append("_");
        sb.append(mid);
        sb.append("_");
        sb.append(uid);

        if (rgid > 0) {
            sb.append("_");
            sb.append(rgid);
        }
        String key = sb.toString();

        synchronized (self_locker) {
            long timestamp = FPManager.getInstance().getMilliTimestamp();

            if (this._duplicateMap.containsKey(key)) {
                long expire = this._duplicateMap.get(key);

                if (expire > timestamp) {
                    return false;
                }
                this._duplicateMap.remove(key);
            }
            this._duplicateMap.put(key, RTMConfig.MID_TTL + timestamp);
            return true;
        }
    }

    private void checkExpire(long timestamp) {
        synchronized (self_locker) {
            List<String> keys = new ArrayList<String>();
            Iterator itor = this._duplicateMap.entrySet().iterator();

            while (itor.hasNext()) {
                Map.Entry entry = (Map.Entry) itor.next();
                if ((long) entry.getValue() > timestamp) {
                    continue;
                }
                keys.add((String) entry.getKey());
            }

            itor = keys.iterator();
            while (itor.hasNext()) {
                this._duplicateMap.remove(itor.next());
            }
        }
    }

    public Integer wantInteger(Map data, String key) {
        return Integer.valueOf(String.valueOf(data.get(key)));
    }

    public Long wantLong(Map data, String key) {
        return Long.valueOf(String.valueOf(data.get(key)));
    }

    public Byte wantByte(Map data, String key) {
        return Byte.valueOf(String.valueOf(data.get(key)));
    }
}
