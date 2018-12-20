package com.rtm;

import com.fpnn.FPClient;
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
import java.security.MessageDigest;
import java.util.*;

public class RTMClient extends BaseClient {

    private static class MidGenerator {

        static private long count = 0;

        static public synchronized long gen() {

            if (++count >= 999) {

                count = 0;
            }

            return Long.valueOf(String.valueOf(System.currentTimeMillis()).concat(String.valueOf(count)));
        }
    }

    private int _pid;
    private String _secret;
    private boolean _reconnect;
    private int _timeout;
    private RTMProcessor _processor;

    /**
     * @param {int}     pid
     * @param {String}  secret
     * @param {String}  endpoint
     * @param {boolean} reconnect
     * @param {int}     timeout
     * @param {boolean} startTimerThread
     */
    public RTMClient(int pid, String secret, String endpoint, boolean reconnect, int timeout, boolean startTimerThread) {

        super(endpoint, reconnect, timeout, startTimerThread);

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
     * @param {boolean} startTimerThread
     */
    public RTMClient(int pid, String secret, String host, int port, boolean reconnect, int timeout, boolean startTimerThread) {

        super(host, port, reconnect, timeout, startTimerThread);

        this._pid = pid;
        this._secret = secret;
    }

    @Override
    protected void init(String host, int port, boolean reconnect, int timeout) {

        super.init(host, port, reconnect, timeout);

        this._reconnect = reconnect;
        this._timeout = timeout;

        this._processor = new RTMProcessor(this.getEvent());
        this.getProcessor().setProcessor(this._processor);
    }

    @Override
    public void destroy() {

        super.destroy();

        this._reconnect = false;
    }

    /**
     *
     * ServerGate (1)
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
     * @param {Map(mtime:long)}         payload
     * @param {Exception}               exception
     * @param {long}                    mid
     * </CallbackData>
     */
    public void sendMessage(long from, long to, byte mtype, String msg, String attrs, long mid, int timeout, FPCallback.ICallback callback) {

        long salt = this.genMessageSlat();

        if (mid == 0) {

            mid = MidGenerator.gen();
        }

        Map payload = new HashMap();

        payload.put("pid", this._pid);
        payload.put("sign", this.genMessageSign(salt));
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
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        data.setPayload(bytes);

        final long fmid = (long) payload.get("mid");
        final FPCallback.ICallback cb = callback;

        this.sendQuest(data, this.questCallback(new FPCallback.ICallback() {

            @Override
            public void callback(CallbackData cbd) {

                cbd.setMid(fmid);

                if (cb != null) {

                    cb.callback(cbd);
                }
            }
        }), timeout);
    }

    /**
     *
     * ServerGate (2)
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
     * @param {Map(mtime:long)}         payload
     * @param {Exception}               exception
     * @param {long}                    mid
     * </CallbackData>
     */
    public void sendMessages(long from, List<Long> tos, byte mtype, String msg, String attrs, long mid, int timeout, FPCallback.ICallback callback) {

        long salt = this.genMessageSlat();

        if (mid == 0) {

            mid = MidGenerator.gen();
        }

        Map payload = new HashMap();

        payload.put("pid", this._pid);
        payload.put("sign", this.genMessageSign(salt));
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
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        data.setPayload(bytes);

        final long fmid = (long) payload.get("mid");
        final FPCallback.ICallback cb = callback;

        this.sendQuest(data, this.questCallback(new FPCallback.ICallback() {

            @Override
            public void callback(CallbackData cbd) {

                cbd.setMid(fmid);

                if (cb != null) {

                    cb.callback(cbd);
                }
            }
        }), timeout);
    }

    /**
     *
     * ServerGate (3)
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
     * @param {Map(mtime:long)}         payload
     * @param {Exception}               exception
     * @param {long}                    mid
     * </CallbackData>
     */
    public void sendGroupMessage(long from, long gid, byte mtype, String msg, String attrs, long mid, int timeout, FPCallback.ICallback callback) {

        long salt = this.genMessageSlat();

        if (mid == 0) {

            mid = MidGenerator.gen();
        }

        Map payload = new HashMap();

        payload.put("pid", this._pid);
        payload.put("sign", this.genMessageSign(salt));
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
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        data.setPayload(bytes);

        final long fmid = (long) payload.get("mid");
        final FPCallback.ICallback cb = callback;

        this.sendQuest(data, this.questCallback(new FPCallback.ICallback() {

            @Override
            public void callback(CallbackData cbd) {

                cbd.setMid(fmid);

                if (cb != null) {

                    cb.callback(cbd);
                }
            }
        }), timeout);
    }

    /**
     *
     * ServerGate (4)
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
     * @param {Map(mtime:long)}         payload
     * @param {Exception}               exception
     * @param {long}                    mid
     * </CallbackData>
     */
    public void sendRoomMessage(long from, long rid, byte mtype, String msg, String attrs, long mid, int timeout, FPCallback.ICallback callback) {

        long salt = this.genMessageSlat();

        if (mid == 0) {

            mid = MidGenerator.gen();
        }

        Map payload = new HashMap();

        payload.put("pid", this._pid);
        payload.put("sign", this.genMessageSign(salt));
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
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        data.setPayload(bytes);

        final long fmid = (long) payload.get("mid");
        final FPCallback.ICallback cb = callback;

        this.sendQuest(data, this.questCallback(new FPCallback.ICallback() {

            @Override
            public void callback(CallbackData cbd) {

                cbd.setMid(fmid);

                if (cb != null) {

                    cb.callback(cbd);
                }
            }
        }), timeout);
    }

    /**
     *
     * ServerGate (5)
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
     * @param {Map(mtime:long)}         payload
     * @param {Exception}               exception
     * @param {long}                    mid
     * </CallbackData>
     */
    public void broadcastMessage(long from, byte mtype, String msg, String attrs, long mid, int timeout, FPCallback.ICallback callback) {

        long salt = this.genMessageSlat();

        if (mid == 0) {

            mid = MidGenerator.gen();
        }

        Map payload = new HashMap();

        payload.put("pid", this._pid);
        payload.put("sign", this.genMessageSign(salt));
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
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        data.setPayload(bytes);

        final long fmid = (long) payload.get("mid");
        final FPCallback.ICallback cb = callback;

        this.sendQuest(data, this.questCallback(new FPCallback.ICallback() {

            @Override
            public void callback(CallbackData cbd) {

                cbd.setMid(fmid);

                if (cb != null) {

                    cb.callback(cbd);
                }
            }
        }), timeout);
    }

    /**
     *  
     * ServerGate (6)
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

        long salt = this.genMessageSlat();

        Map payload = new HashMap();

        payload.put("pid", this._pid);
        payload.put("sign", this.genMessageSign(salt));
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
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, this.questCallback(callback), timeout);
    }

    /**
     *  
     * ServerGate (7)
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

        long salt = this.genMessageSlat();

        Map payload = new HashMap();

        payload.put("pid", this._pid);
        payload.put("sign", this.genMessageSign(salt));
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
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, this.questCallback(callback), timeout);
    }

    /**
     *  
     * ServerGate (8)
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

        long salt = this.genMessageSlat();

        Map payload = new HashMap();

        payload.put("pid", this._pid);
        payload.put("sign", this.genMessageSign(salt));
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
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, this.questCallback(new FPCallback.ICallback() {

            @Override
            public void callback(CallbackData cbd) {
                if (callback == null) {

                    return;
                }

                Map payload = (Map) cbd.getPayload();

                if (payload != null) {

                    List uids = (List<Long>) payload.get("uids");
                    callback.callback(new CallbackData(uids));
                    return;
                }

                callback.callback(cbd);
            }
        }), timeout);
    }

    /**
     *  
     * ServerGate (9)
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
     * @param {boolean}                 payload
     * @param {Exception}               exception
     * </CallbackData>
     */
    public void isFriend(long uid, long fuid, int timeout, FPCallback.ICallback callback) {

        long salt = this.genMessageSlat();

        Map payload = new HashMap();

        payload.put("pid", this._pid);
        payload.put("sign", this.genMessageSign(salt));
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
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, this.questCallback(new FPCallback.ICallback() {

            @Override
            public void callback(CallbackData cbd) {
                if (callback == null) {

                    return;
                }

                Map payload = (Map) cbd.getPayload();

                if (payload != null) {

                    boolean ok = (Boolean) payload.get("ok");
                    callback.callback(new CallbackData(ok));
                    return;
                }

                callback.callback(cbd);
            }
        }), timeout);
    }

    /**
     *  
     * ServerGate (10)
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

        long salt = this.genMessageSlat();

        Map payload = new HashMap();

        payload.put("pid", this._pid);
        payload.put("sign", this.genMessageSign(salt));
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
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, this.questCallback(new FPCallback.ICallback() {

            @Override
            public void callback(CallbackData cbd) {
                if (callback == null) {

                    return;
                }

                Map payload = (Map) cbd.getPayload();

                if (payload != null) {

                    List<Long> fuids = (List<Long>) payload.get("fuids");
                    callback.callback(new CallbackData(fuids));
                    return;
                }

                callback.callback(cbd);
            }
        }), timeout);
    }

    /**
     *  
     * ServerGate (11)
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

        long salt = this.genMessageSlat();

        Map payload = new HashMap();

        payload.put("pid", this._pid);
        payload.put("sign", this.genMessageSign(salt));
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
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, this.questCallback(callback), timeout);
    }

    /**
     *  
     * ServerGate (12)
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

        long salt = this.genMessageSlat();

        Map payload = new HashMap();

        payload.put("pid", this._pid);
        payload.put("sign", this.genMessageSign(salt));
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
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, this.questCallback(callback), timeout);
    }

    /**
     *  
     * ServerGate (13)
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

        long salt = this.genMessageSlat();

        Map payload = new HashMap();

        payload.put("pid", this._pid);
        payload.put("sign", this.genMessageSign(salt));
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
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, this.questCallback(callback), timeout);
    }

    /**
     *  
     * ServerGate (14)
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

        long salt = this.genMessageSlat();

        Map payload = new HashMap();

        payload.put("pid", this._pid);
        payload.put("sign", this.genMessageSign(salt));
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
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, this.questCallback(new FPCallback.ICallback() {

            @Override
            public void callback(CallbackData cbd) {
                if (callback == null) {

                    return;
                }

                Map payload = (Map) cbd.getPayload();

                if (payload != null) {

                    List<Long> uids = (List<Long>) payload.get("uids");
                    callback.callback(new CallbackData(uids));
                    return;
                }

                callback.callback(cbd);
            }
        }), timeout);
    }

    /**
     *  
     * ServerGate (15)
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
     * @param {boolean}                 payload
     * @param {Exception}               exception
     * </CallbackData>
     */
    public void isGroupMember(long gid, long uid, int timeout, FPCallback.ICallback callback) {

        long salt = this.genMessageSlat();

        Map payload = new HashMap();

        payload.put("pid", this._pid);
        payload.put("sign", this.genMessageSign(salt));
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
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, this.questCallback(new FPCallback.ICallback() {

            @Override
            public void callback(CallbackData cbd) {
                if (callback == null) {

                    return;
                }

                Map payload = (Map) cbd.getPayload();

                if (payload != null) {

                    boolean ok = (Boolean) payload.get("ok");
                    callback.callback(new CallbackData(ok));
                    return;
                }

                callback.callback(cbd);
            }
        }), timeout);
    }

    /**
     *  
     * ServerGate (16)
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

        long salt = this.genMessageSlat();

        Map payload = new HashMap();

        payload.put("pid", this._pid);
        payload.put("sign", this.genMessageSign(salt));
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
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, this.questCallback(new FPCallback.ICallback() {

            @Override
            public void callback(CallbackData cbd) {
                if (callback == null) {

                    return;
                }

                Map payload = (Map) cbd.getPayload();

                if (payload != null) {

                    List<Long> gids = (List<Long>) payload.get("gids");
                    callback.callback(new CallbackData(gids));
                    return;
                }

                callback.callback(cbd);
            }
        }), timeout);
    }

    /**
     *  
     * ServerGate (17)
     *
     * @param {long}                    uid
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {String}                  payload
     * @param {Exception}               exception
     * </CallbackData>
     */
    public void getToken(long uid, int timeout, FPCallback.ICallback callback) {

        long salt = this.genMessageSlat();

        Map payload = new HashMap();

        payload.put("pid", this._pid);
        payload.put("sign", this.genMessageSign(salt));
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
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, this.questCallback(new FPCallback.ICallback() {

            @Override
            public void callback(CallbackData cbd) {
                if (callback == null) {

                    return;
                }

                Map payload = (Map) cbd.getPayload();

                if (payload != null) {

                    String token = (String) payload.get("token");
                    callback.callback(new CallbackData(token));
                    return;
                }

                callback.callback(cbd);
            }
        }), timeout);
    }

    /**
     *  
     * ServerGate (18)
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

        long salt = this.genMessageSlat();

        Map payload = new HashMap();

        payload.put("pid", this._pid);
        payload.put("sign", this.genMessageSign(salt));
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
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, this.questCallback(new FPCallback.ICallback() {

            @Override
            public void callback(CallbackData cbd) {
                if (callback == null) {

                    return;
                }

                Map payload = (Map) cbd.getPayload();

                if (payload != null) {

                    List<Long> uids = (List<Long>) payload.get("uids");
                    callback.callback(new CallbackData(uids));
                    return;
                }

                callback.callback(cbd);
            }
        }), timeout);
    }

    /**
     *  
     * ServerGate (19)
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

        long salt = this.genMessageSlat();

        Map payload = new HashMap();

        payload.put("pid", this._pid);
        payload.put("sign", this.genMessageSign(salt));
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
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, this.questCallback(callback), timeout);
    }

    /**
     *  
     * ServerGate (20)
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

        long salt = this.genMessageSlat();

        Map payload = new HashMap();

        payload.put("pid", this._pid);
        payload.put("sign", this.genMessageSign(salt));
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
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, this.questCallback(callback), timeout);
    }

    /**
     *  
     * ServerGate (21)
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

        long salt = this.genMessageSlat();

        Map payload = new HashMap();

        payload.put("pid", this._pid);
        payload.put("sign", this.genMessageSign(salt));
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
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, this.questCallback(callback), timeout);
    }

    /**
     *  
     * ServerGate (22)
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

        long salt = this.genMessageSlat();

        Map payload = new HashMap();

        payload.put("pid", this._pid);
        payload.put("sign", this.genMessageSign(salt));
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
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, this.questCallback(callback), timeout);
    }

    /**
     *  
     * ServerGate (23)
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

        long salt = this.genMessageSlat();

        Map payload = new HashMap();

        payload.put("pid", this._pid);
        payload.put("sign", this.genMessageSign(salt));
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
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, this.questCallback(callback), timeout);
    }

    /**
     *  
     * ServerGate (24)
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

        long salt = this.genMessageSlat();

        Map payload = new HashMap();

        payload.put("pid", this._pid);
        payload.put("sign", this.genMessageSign(salt));
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
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, this.questCallback(callback), timeout);
    }

    /**
     *  
     * ServerGate (25)
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
     * @param {boolean}                 payload
     * @param {Exception}               exception
     * </CallbackData>
     */
    public void isBanOfGroup(long gid, long uid, int timeout, FPCallback.ICallback callback) {

        long salt = this.genMessageSlat();

        Map payload = new HashMap();

        payload.put("pid", this._pid);
        payload.put("sign", this.genMessageSign(salt));
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
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, this.questCallback(new FPCallback.ICallback() {

            @Override
            public void callback(CallbackData cbd) {
                if (callback == null) {

                    return;
                }

                Map payload = (Map) cbd.getPayload();

                if (payload != null) {

                    boolean ok = (Boolean) payload.get("ok");
                    callback.callback(new CallbackData(ok));
                    return;
                }

                callback.callback(cbd);
            }
        }), timeout);
    }

    /**
     *  
     * ServerGate (26)
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
     * @param {boolean}                 payload
     * @param {Exception}               exception
     * </CallbackData>
     */
    public void isBanOfRoom(long rid, long uid, int timeout, FPCallback.ICallback callback) {

        long salt = this.genMessageSlat();

        Map payload = new HashMap();

        payload.put("pid", this._pid);
        payload.put("sign", this.genMessageSign(salt));
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
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, this.questCallback(new FPCallback.ICallback() {

            @Override
            public void callback(CallbackData cbd) {
                if (callback == null) {

                    return;
                }

                Map payload = (Map) cbd.getPayload();

                if (payload != null) {

                    boolean ok = (Boolean) payload.get("ok");
                    callback.callback(new CallbackData(ok));
                    return;
                }

                callback.callback(cbd);
            }
        }), timeout);
    }

    /**
     *  
     * ServerGate (27)
     *
     * @param {long}                    uid
     * @param {int}                     timeout
     * @param {FPCallback.ICallback}    callback
     *
     * @callback
     * @param {CallbackData}            cbdata
     *
     * <CallbackData>
     * @param {boolean}                 payload
     * @param {Exception}               exception
     * </CallbackData>
     */
    public void isProjectBlack(long uid, int timeout, FPCallback.ICallback callback) {

        long salt = this.genMessageSlat();

        Map payload = new HashMap();

        payload.put("pid", this._pid);
        payload.put("sign", this.genMessageSign(salt));
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
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, this.questCallback(new FPCallback.ICallback() {

            @Override
            public void callback(CallbackData cbd) {
                if (callback == null) {

                    return;
                }

                Map payload = (Map) cbd.getPayload();

                if (payload != null) {

                    boolean ok = (Boolean) payload.get("ok");
                    callback.callback(new CallbackData(ok));
                    return;
                }

                callback.callback(cbd);
            }
        }), timeout);
    }

    /**
     *  
     * ServerGate (28)
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
     * @param {Map(token:string,endpoint:string)}   payload
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
     * ServerGate (29)
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
     * @param {boolean}                 deleted
     * @param {String}                  msg
     * @param {String}                  attrs
     * @param {long}                    mtime
     * </GroupMsg>
     */
    public void getGroupMessage(long gid, boolean desc, int num, long begin, long end, long lastid, int timeout, FPCallback.ICallback callback) {

        long salt = this.genMessageSlat();

        Map payload = new HashMap();

        payload.put("pid", this._pid);
        payload.put("sign", this.genMessageSign(salt));
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

        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("getgroupmsg");

        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {

            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, this.questCallback(new FPCallback.ICallback() {

            @Override
            public void callback(CallbackData cbd) {
                if (callback == null) {

                    return;
                }

                Map payload = (Map) cbd.getPayload();

                if (payload != null) {

                    List list = (ArrayList) payload.get("msgs");

                    for (int i = 0; i < list.size(); i++) {

                        Map map = new HashMap();
                        List items = (ArrayList) list.get(i);

                        map.put("id", items.get(0));
                        map.put("from", items.get(1));
                        map.put("mtype", items.get(2));
                        map.put("mid", items.get(3));
                        map.put("deleted", items.get(4));
                        map.put("msg", items.get(5));
                        map.put("attrs", items.get(6));
                        map.put("mtime", items.get(7));

                        list.set(i, map);
                    }

                    callback.callback(new CallbackData(payload));
                    return;
                }

                callback.callback(cbd);
            }
        }), timeout);
    }

    /**
     *  
     * ServerGate (30)
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
     * @param {boolean}                 deleted
     * @param {String}                  msg
     * @param {String}                  attrs
     * @param {long}                    mtime
     * </RoomMsg>
     */
    public void getRoomMessage(long rid, boolean desc, int num, long begin, long end, long lastid, int timeout, FPCallback.ICallback callback) {

        long salt = this.genMessageSlat();

        Map payload = new HashMap();

        payload.put("pid", this._pid);
        payload.put("sign", this.genMessageSign(salt));
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

        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("getroommsg");

        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {

            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, this.questCallback(new FPCallback.ICallback() {

            @Override
            public void callback(CallbackData cbd) {
                if (callback == null) {

                    return;
                }

                Map payload = (Map) cbd.getPayload();

                if (payload != null) {

                    List list = (ArrayList) payload.get("msgs");

                    for (int i = 0; i < list.size(); i++) {

                        Map map = new HashMap();
                        List items = (ArrayList) list.get(i);

                        map.put("id", items.get(0));
                        map.put("from", items.get(1));
                        map.put("mtype", items.get(2));
                        map.put("mid", items.get(3));
                        map.put("deleted", items.get(4));
                        map.put("msg", items.get(5));
                        map.put("attrs", items.get(6));
                        map.put("mtime", items.get(7));

                        list.set(i, map);
                    }

                    callback.callback(new CallbackData(payload));
                    return;
                }

                callback.callback(cbd);
            }
        }), timeout);
    }

    /**
     *  
     * ServerGate (31)
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
     * @param {boolean}                 deleted
     * @param {String}                  msg
     * @param {String}                  attrs
     * @param {long}                    mtime
     * </BroadcastMsg>
     */
    public void getBroadcastMessage(boolean desc, int num, long begin, long end, long lastid, int timeout, FPCallback.ICallback callback) {

        long salt = this.genMessageSlat();

        Map payload = new HashMap();

        payload.put("pid", this._pid);
        payload.put("sign", this.genMessageSign(salt));
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

        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("getbroadcastmsg");

        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {

            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, this.questCallback(new FPCallback.ICallback() {

            @Override
            public void callback(CallbackData cbd) {
                if (callback == null) {

                    return;
                }

                Map payload = (Map) cbd.getPayload();

                if (payload != null) {

                    List list = (ArrayList) payload.get("msgs");

                    for (int i = 0; i < list.size(); i++) {

                        Map map = new HashMap();
                        List items = (ArrayList) list.get(i);

                        map.put("id", items.get(0));
                        map.put("from", items.get(1));
                        map.put("mtype", items.get(2));
                        map.put("mid", items.get(3));
                        map.put("deleted", items.get(4));
                        map.put("msg", items.get(5));
                        map.put("attrs", items.get(6));
                        map.put("mtime", items.get(7));

                        list.set(i, map);
                    }

                    callback.callback(new CallbackData(payload));
                    return;
                }

                callback.callback(cbd);
            }
        }), timeout);
    }

    /**
     *  
     * ServerGate (32)
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
     * @param {boolean}                 deleted
     * @param {String}                  msg
     * @param {String}                  attrs
     * @param {long}                    mtime
     * </P2PMsg>
     */
    public void getP2PMessage(long uid, long ouid, boolean desc, int num, long begin, long end, long lastid, int timeout, FPCallback.ICallback callback) {

        long salt = this.genMessageSlat();

        Map payload = new HashMap();

        payload.put("pid", this._pid);
        payload.put("sign", this.genMessageSign(salt));
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

        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("getp2pmsg");

        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {

            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, this.questCallback(new FPCallback.ICallback() {

            @Override
            public void callback(CallbackData cbd) {
                if (callback == null) {

                    return;
                }

                Map payload = (Map) cbd.getPayload();

                if (payload != null) {

                    List list = (ArrayList) payload.get("msgs");

                    for (int i = 0; i < list.size(); i++) {

                        Map map = new HashMap();
                        List items = (ArrayList) list.get(i);

                        map.put("id", items.get(0));
                        map.put("direction", items.get(1));
                        map.put("mtype", items.get(2));
                        map.put("mid", items.get(3));
                        map.put("deleted", items.get(4));
                        map.put("msg", items.get(5));
                        map.put("attrs", items.get(6));
                        map.put("mtime", items.get(7));

                        list.set(i, map);
                    }

                    callback.callback(new CallbackData(payload));
                    return;
                }

                callback.callback(cbd);
            }
        }), timeout);
    }

    /**
     *  
     * ServerGate (33)
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

        long salt = this.genMessageSlat();

        Map payload = new HashMap();

        payload.put("pid", this._pid);
        payload.put("sign", this.genMessageSign(salt));
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
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, this.questCallback(callback), timeout);
    }

    /**
     *  
     * ServerGate (34)
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

        long salt = this.genMessageSlat();

        Map payload = new HashMap();

        payload.put("pid", this._pid);
        payload.put("sign", this.genMessageSign(salt));
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
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, this.questCallback(callback), timeout);
    }

    /**
     *  
     * ServerGate (35)
     *
     * @param {List(Long)}              gids
     * @param {List(Long)}              rids
     * @param {boolean}                 p2p
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
    public void addEvtListener(List<Long> gids, List<Long> rids, boolean p2p, List<String> events, int timeout, FPCallback.ICallback callback) {

        long salt = this.genMessageSlat();

        Map payload = new HashMap();

        payload.put("pid", this._pid);
        payload.put("sign", this.genMessageSign(salt));
        payload.put("salt", salt);

        if (gids != null && gids.size() > 0) {

            payload.put("gids", gids);
        }

        if (rids != null && rids.size() > 0) {

            payload.put("rids", rids);
        }

        if (p2p) {

            payload.put("p2p", true);
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
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, this.questCallback(callback), timeout);
    }

    /**
     *  
     * ServerGate (36)
     *
     * @param {List(Long)}              gids
     * @param {List(Long)}              rids
     * @param {boolean}                 p2p
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
    public void removeEvtListener(List<Long> gids, List<Long> rids, boolean p2p, List<String> events, int timeout, FPCallback.ICallback callback) {

        long salt = this.genMessageSlat();

        Map payload = new HashMap();

        payload.put("pid", this._pid);
        payload.put("sign", this.genMessageSign(salt));
        payload.put("salt", salt);

        if (gids != null && gids.size() > 0) {

            payload.put("gids", gids);
        }

        if (rids != null && rids.size() > 0) {

            payload.put("rids", rids);
        }

        if (p2p) {

            payload.put("p2p", true);
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
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, this.questCallback(callback), timeout);
    }

    /**
     *  
     * ServerGate (37)
     *
     * @param {boolean}                 all
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
    public void setEvtListener(boolean all, int timeout, FPCallback.ICallback callback) {

        long salt = this.genMessageSlat();

        Map payload = new HashMap();

        payload.put("pid", this._pid);
        payload.put("sign", this.genMessageSign(salt));
        payload.put("salt", salt);
        payload.put("all", all);

        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("removelisten");

        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {

            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, this.questCallback(callback), timeout);
    }

    /**
     *  
     * ServerGate (37)
     *
     * @param {List(Long)}              gids
     * @param {List(Long)}              rids
     * @param {boolean}                 p2p
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
    public void setEvtListener(List<Long> gids, List<Long> rids, boolean p2p, List<String> events, int timeout, FPCallback.ICallback callback) {

        long salt = this.genMessageSlat();

        Map payload = new HashMap();

        payload.put("pid", this._pid);
        payload.put("sign", this.genMessageSign(salt));
        payload.put("salt", salt);
        payload.put("gids", gids);
        payload.put("rids", rids);
        payload.put("p2p", p2p);
        payload.put("events", events);

        FPData data = new FPData();
        data.setFlag(0x1);
        data.setMtype(0x1);
        data.setMethod("removelisten");

        byte[] bytes = new byte[0];
        PayloadPacker packer = new PayloadPacker();

        try {

            packer.pack(payload);
            bytes = packer.toByteArray();
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, this.questCallback(callback), timeout);
    }

    /**
     *  
     * ServerGate (38)
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

        long salt = this.genMessageSlat();

        Map payload = new HashMap();

        payload.put("pid", this._pid);
        payload.put("sign", this.genMessageSign(salt));
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
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, this.questCallback(callback), timeout);
    }

    /**
     *  
     * ServerGate (39)
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

        long salt = this.genMessageSlat();

        Map payload = new HashMap();

        payload.put("pid", this._pid);
        payload.put("sign", this.genMessageSign(salt));
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
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, this.questCallback(callback), timeout);
    }

    /**
     *  
     * ServerGate (40)
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

        long salt = this.genMessageSlat();

        Map payload = new HashMap();

        payload.put("pid", this._pid);
        payload.put("sign", this.genMessageSign(salt));
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
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, this.questCallback(callback), timeout);
    }

    /**
     *  
     * ServerGate (41)
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

        long salt = this.genMessageSlat();

        Map payload = new HashMap();

        payload.put("pid", this._pid);
        payload.put("sign", this.genMessageSign(salt));
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
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, this.questCallback(callback), timeout);
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

                    FileClient fileClient = new FileClient(endpoint, ftimeout, false);

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

        long salt = this.genMessageSlat();

        payload.put("pid", this._pid);
        payload.put("sign", this.genMessageSign(salt));
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
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        data.setPayload(bytes);
        this.sendQuest(data, this.questCallback(callback), timeout);
    }

    private long genMessageSlat() {

        long curr = System.currentTimeMillis();
        long a = curr >> 32;
        long b = (curr & 0xffff) << 32;
        long c = a | b;

        return (curr ^ c);
    }

    private String genMessageSign(long slat) {

        String token = Integer.toString(this._pid)
                .concat(":")
                .concat(this._secret)
                .concat(":")
                .concat(Long.toString(slat));


        return this.md5(token);
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

class FileClient extends BaseClient {

    public FileClient(String endpoint, int timeout, boolean startTimerThread) {

        super(endpoint, false, timeout, startTimerThread);
    }

    public FileClient(String host, int port, int timeout, boolean startTimerThread) {

        super(host, port, false, timeout, startTimerThread);
    }

    @Override
    protected void init(String host, int port, boolean reconnect, int timeout) {

        super.init(host, port, reconnect, timeout);

        FileClient self = this;
        FPEvent.IListener listener = new FPEvent.IListener() {

            @Override
            public void fpEvent(EventData event) {

                switch (event.getType()) {
                    case "connect":
                        self.onConnect();
                        break;
                    case "close":
                        self.onClose();
                        break;
                    case "error":
                        self.onException(event.getException());
                        break;
                }
            }
        };

        this.getEvent().addListener("connect", listener);
        this.getEvent().addListener("close", listener);
        this.getEvent().addListener("error", listener);
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
        } catch (IOException ex) {

            ex.printStackTrace();
        }

        data.setPayload(bytes);

        final long fmid = (long) payload.get("mid");
        final FPCallback.ICallback cb = callback;
        final FileClient self = this;

        this.sendQuest(data, this.questCallback(new FPCallback.ICallback() {

            @Override
            public void callback(CallbackData cbd) {

                cbd.setMid(fmid);
                self.destroy();

                if (cb != null) {

                    cb.callback(cbd);
                }
            }
        }), timeout);
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

    public BaseClient(String endpoint, boolean reconnect, int timeout, boolean startTimerThread) {

        super(endpoint, reconnect, timeout);

        if (startTimerThread) {

            ThreadPool.getInstance().startTimerThread();
        }
    }

    public BaseClient(String host, int port, boolean reconnect, int timeout, boolean startTimerThread) {

        super(host, port, reconnect, timeout);

        if (startTimerThread) {

            ThreadPool.getInstance().startTimerThread();
        }
    }

    @Override
    protected void init(String host, int port, boolean reconnect, int timeout) {

        super.init(host, port, reconnect, timeout);
    }

    public void enableConnect() {

        this.connect();
    }

    /**
     * @param {String}  curve
     * @param {byte[]}  derKey
     * @param {boolean} streamMode
     * @param {boolean} reinforce
     */
    public void enableEncryptorByData(String curve, byte[] derKey, boolean streamMode, boolean reinforce) {

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
                        } catch (IOException ex) {

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

        this.enableConnect();
    }

    /**
     * @param {String}  curve
     * @param {String}  derPath
     * @param {boolean} streamMode
     * @param {boolean} reinforce
     */
    public void enableEncryptorByFile(String curve, String derPath, boolean streamMode, boolean reinforce) {

        byte[] bytes = new LoadFile().read(derPath);
        this.enableEncryptorByData(curve, bytes, streamMode, reinforce);
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
                } catch (IOException ex) {

                    ex.printStackTrace();
                }
            }

            if (this.getPackage().isAnswer(data)) {

                isAnswerException = data.getSS() != 0;
            }
        }

        cbd.checkException(isAnswerException, payload);
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
