package com.test;

import com.fpnn.callback.CallbackData;
import com.fpnn.callback.FPCallback;
import com.fpnn.event.EventData;
import com.fpnn.event.FPEvent;
import com.rtm.RTMClient;
import com.rtm.RTMConfig;
import com.rtm.RTMProcessor;
import com.rtm.json.JsonHelper;

import java.util.*;

public class TestCase implements TestMain.ITestCase {

    class SendLocker {
        public int status = 0;
    }

    private RTMClient _client;
    private int _sleepCount;
    private byte[] _fileBytes;

    public void startTest(byte[] fileBytes) {
        this._fileBytes = fileBytes;
        this._client = new RTMClient(
                11000001,
                "ef3617e5-e886-4a4e-9eef-7263c0320628",
                "52.83.245.22:13315",
                true,
                20 * 1000,
                true
        );
        final TestCase self = this;
        this._client.getEvent().addListener("connect", new FPEvent.IListener() {
            @Override
            public void fpEvent(EventData evd) {
                System.out.println(new String("Connected!"));
                self.startThread();
            }
        });
        this._client.getEvent().addListener("close", new FPEvent.IListener() {
            @Override
            public void fpEvent(EventData evd) {
                System.out.println(new String("Closed!"));
            }
        });
        this._client.getEvent().addListener("error", new FPEvent.IListener() {
            @Override
            public void fpEvent(EventData evd) {
                evd.getException().printStackTrace();
            }
        });
        this._client.connect();
        RTMProcessor processor = this._client.getProcessor();
        processor.addPushService(RTMConfig.SERVER_PUSH.recvMessage, new RTMProcessor.IService() {
            @Override
            public void Service(Map<String, Object> data) {
                System.out.println("[recvMessage]: " + JsonHelper.getInstance().getJson().toJSON(data));
            }
        });
    }

    public void stopTest() {
        this.stopThread();
        if (this._client != null) {
            this._client.destroy();
            this._client = null;
        }
    }

    private Thread _thread;
    private SendLocker send_locker = new SendLocker();

    private void startThread() {
        synchronized (send_locker) {
            if (send_locker.status != 0) {
                return;
            }
            send_locker.status = 1;
            try {
                final TestCase self = this;
                this._thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        self.beginTest();
                    }
                });
                this._thread.start();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void stopThread() {
        synchronized (send_locker) {
            send_locker.status = 0;
        }
    }

    private void beginTest() {
        try {
            this.doTest();
//            this.doFinish();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

//    private void doFinish() {
//        this._client.getToken(777779, 20 * 1000, new FPCallback.ICallback() {
//            @Override
//            public void callback(CallbackData cbd) {
//                Object obj = cbd.getPayload();
//
//                if (obj != null) {
//                    Map payload = (Map) obj;
//                    System.out.print("[DATA] getToken:");
//                    System.out.println(payload.toString());
//                } else {
//                    System.err.print("[ERR] getToken:");
//                    System.err.println(cbd.getException().getMessage());
//                }
//            }
//        });
//        this._client.destroy();
//    }

    private void doTest() {
        long from = 777779;
        long to = 778899;
        long fuid = 778898;
        long mid = 0;
        List<Long> tos = new ArrayList();
        tos.add((long)654321);
        tos.add(fuid);
        tos.add(to);
        long gid = 999;
        long rid = 666;
        List<Long> friends = new ArrayList();
        friends.add(fuid);
        friends.add(to);
        List<Long> gids = new ArrayList();
        gids.add(gid);
        List<Long> rids = new ArrayList();
        rids.add(rid);
        List<String> evets = new ArrayList();
        evets.add(RTMConfig.SERVER_EVENT.login);
        evets.add(RTMConfig.SERVER_EVENT.logout);
        int timeout = 20 * 1000;
        int sleep = 1000;
        this.threadSleep(sleep);
        //ServerGate (9c)
        //---------------------------------setEvtListener--------------------------------------
        this._client.setEvtListener(true, true, true, true, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] setEvtListener:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] setEvtListener:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (1a)
        //---------------------------------getToken--------------------------------------
        this._client.getToken(from, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] getToken:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] getToken:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (1b)
        //---------------------------------kickout--------------------------------------
        this._client.kickout(to, "", timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] kickout:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] kickout:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (1c)
        //---------------------------------addDevice--------------------------------------
        this._client.addDevice(from, "app-info", "device-token", timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] addDevice:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] addDevice:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (1d)
        //---------------------------------removeDevice--------------------------------------
        this._client.removeDevice(from, "app-info", timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] removeDevice:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] removeDevice:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (1e)
        //---------------------------------removeToken--------------------------------------
        this._client.removeToken(to, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] removeToken:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] removeToken:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (2a)
        //---------------------------------sendMessage--------------------------------------
        this._client.sendMessage(from, to, (byte) 8, "hello !", "", 0, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] sendMessage:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] sendMessage:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (2b)
        //---------------------------------sendMessages--------------------------------------
        this._client.sendMessages(from, tos, (byte) 8, "hello !", "", 0, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] sendMessages:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] sendMessages:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (2c)
        //---------------------------------sendGroupMessage--------------------------------------
        this._client.sendGroupMessage(from, gid, (byte) 8, "hello !", "", 0, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] sendGroupMessage:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] sendGroupMessage:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (2d)
        //---------------------------------sendRoomMessage--------------------------------------
        this._client.sendRoomMessage(from, rid, (byte) 8, "hello !", "", 0, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] sendRoomMessage:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] sendRoomMessage:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (2e)
        //---------------------------------broadcastMessage--------------------------------------
        this._client.broadcastMessage(from, (byte) 8, "hello !", "", 0, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] broadcastMessage:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] broadcastMessage:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (2f)
        //---------------------------------getGroupMessage--------------------------------------
        this._client.getGroupMessage(gid, false, 10, 0, 0, 0, Arrays.asList((byte)8), timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] getGroupMessage:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] getGroupMessage:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (2g)
        //---------------------------------getRoomMessage--------------------------------------
        this._client.getRoomMessage(rid, false, 10, 0, 0, 0, Arrays.asList((byte)8), timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] getRoomMessage:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] getRoomMessage:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (2h)
        //---------------------------------getBroadcastMessage--------------------------------------
        this._client.getBroadcastMessage(false, 10, 0, 0, 0, Arrays.asList((byte)8), timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] getBroadcastMessage:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] getBroadcastMessage:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (2i)
        //---------------------------------getP2PMessage--------------------------------------
        this._client.getP2PMessage(from, to, false, 10, 0, 0, 0, Arrays.asList((byte)8), timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] getP2PMessage:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] getP2PMessage:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (2j)
        //---------------------------------deleteMessage--------------------------------------
        this._client.deleteMessage(mid, from, to, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] deleteMessage:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] deleteMessage:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (2j')
        //---------------------------------deleteGroupMessage--------------------------------------
        this._client.deleteGroupMessage(mid, from, gid, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] deleteGroupMessage:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] deleteGroupMessage:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (2j'')
        //---------------------------------deleteRoomMessage--------------------------------------
        this._client.deleteRoomMessage(mid, from, rid, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] deleteRoomMessage:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] deleteRoomMessage:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (2j''')
        //---------------------------------deleteBroadcastMessage--------------------------------------
        this._client.deleteBroadcastMessage(mid, from, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] deleteBroadcastMessage:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] deleteBroadcastMessage:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (3a)
        //---------------------------------sendChat--------------------------------------
        this._client.sendChat(from, to, "hello !", "", 0, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] sendChat:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] sendChat:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (3a'')
        //---------------------------------sendCmd--------------------------------------
        this._client.sendCmd(from, to, "friends_invite", "", 0, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] sendCmd:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] sendCmd:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (3b)
        //---------------------------------sendChats--------------------------------------
        this._client.sendChats(from, tos, "hello !", "", 0, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] sendChats:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] sendChats:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (3b'')
        //---------------------------------sendCmds--------------------------------------
        this._client.sendCmds(from, tos, "friends_invite", "", 0, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] sendCmds:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] sendCmds:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (3c)
        //---------------------------------sendGroupChat--------------------------------------
        this._client.sendGroupChat(from, gid, "hello !", "", 0, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] sendGroupChat:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] sendGroupChat:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (3c'')
        //---------------------------------sendGroupCmd--------------------------------------
        this._client.sendGroupCmd(from, gid, "group_friends_invite", "", 0, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] sendGroupCmd:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] sendGroupCmd:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (3d)
        //---------------------------------sendRoomChat--------------------------------------
        this._client.sendRoomChat(from, rid, "hello !", "", 0, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] sendRoomChat:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] sendRoomChat:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (3d'')
        //---------------------------------sendRoomCmd--------------------------------------
        this._client.sendRoomCmd(from, rid, "room_friends_invite", "", 0, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] sendRoomCmd:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] sendRoomCmd:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (3e)
        //---------------------------------broadcastChat--------------------------------------
        this._client.broadcastChat(from, "hello !", "", 0, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] broadcastChat:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] broadcastChat:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (3e'')
        //---------------------------------broadcastCmd--------------------------------------
        this._client.broadcastCmd(from, "broadcast_friends_invite", "", 0, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] broadcastCmd:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] broadcastCmd:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (3f)
        //---------------------------------getGroupChat--------------------------------------
        this._client.getGroupChat(gid, true, 10, 0, 0, 0, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] getGroupChat:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] getGroupChat:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (3g)
        //---------------------------------getRoomChat--------------------------------------
        this._client.getRoomChat(rid, true, 10, 0, 0, 0, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] getRoomChat:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] getRoomChat:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (3h)
        //---------------------------------getBroadcastChat--------------------------------------
        this._client.getBroadcastChat(true, 10, 0, 0, 0, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] getBroadcastChat:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] getBroadcastChat:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (3i)
        //---------------------------------getP2PChat--------------------------------------
        this._client.getP2PChat(from, to, true, 10, 0, 0, 0, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] getP2PChat:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] getP2PChat:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (3j)
        //---------------------------------deleteChat--------------------------------------
        this._client.deleteChat(mid, from, to, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] deleteChat:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] deleteChat:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (3j')
        //---------------------------------deleteGroupChat--------------------------------------
        this._client.deleteGroupChat(mid, from, gid, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] deleteGroupChat:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] deleteGroupChat:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (3j'')
        //---------------------------------deleteRoomChat--------------------------------------
        this._client.deleteRoomChat(mid, from, rid, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] deleteRoomChat:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] deleteRoomChat:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (3j'')
        //---------------------------------deleteBroadcastChat--------------------------------------
        this._client.deleteBroadcastChat(mid, from, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] deleteBroadcastChat:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] deleteBroadcastChat:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (3k)
        //---------------------------------translate--------------------------------------
        this._client.translate("点数优惠", RTMConfig.TRANS_LANGUAGE.zh_cn, RTMConfig.TRANS_LANGUAGE.en, "chat", "censor", timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] translate:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] translate:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (3i)
        //---------------------------------profanity--------------------------------------
        this._client.profanity("点数优惠", "stop", timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] profanity:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] profanity:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (3j)
        //---------------------------------transcribe--------------------------------------
        //this.threadSleep(sleep);
        //ServerGate (4a)
        //---------------------------------fileToken--------------------------------------
        this._client.fileToken(from, "sendfile", null, to, 0, 0, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] fileToken:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] fileToken:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (5a)
        //---------------------------------getOnlineUsers--------------------------------------
        this._client.getOnlineUsers(tos, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    List<Long> payload = (List<Long>) obj;
                    System.out.print("[DATA] getOnlineUsers:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] getOnlineUsers:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (5b)
        //---------------------------------addProjectBlack--------------------------------------
        this._client.addProjectBlack(to, 60, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] addProjectBlack:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] addProjectBlack:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (5c)
        //---------------------------------removeProjectBlack--------------------------------------
        this._client.removeProjectBlack(to, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] removeProjectBlack:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] removeProjectBlack:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (5d)
        //---------------------------------isProjectBlack--------------------------------------
        this._client.isProjectBlack(to, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] isProjectBlack:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] isProjectBlack:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (5e)
        //---------------------------------setUserInfo--------------------------------------
        this._client.setUserInfo(to, "oinfo", "pinfo", timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] setUserInfo:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] setUserInfo:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (5f)
        //---------------------------------getUserInfo--------------------------------------
        this._client.getUserInfo(to, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] getUserInfo:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] getUserInfo:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (5g)
        //---------------------------------getUserOpenInfo--------------------------------------
        this._client.getUserOpenInfo(tos, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] getUserOpenInfo:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] getUserOpenInfo:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (6a)
        //---------------------------------addFriends--------------------------------------
        this._client.addFriends(from, friends, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] addFriends:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] addFriends:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (6b)
        //---------------------------------deleteFriends--------------------------------------
        this._client.deleteFriends(to, friends, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] deleteFriends:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] deleteFriends:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (6c)
        //---------------------------------getFriends--------------------------------------
        this._client.getFriends(from, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    List payload = (List<Long>) obj;
                    System.out.print("[DATA] getFriends:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] getFriends:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (6d)
        //---------------------------------isFriend--------------------------------------
        this._client.isFriend(from, fuid, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] isFriend:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] isFriend:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (6e)
        //---------------------------------isFriends--------------------------------------
        this._client.isFriends(from, friends, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    List<Long> payload = (List<Long>) obj;
                    System.out.print("[DATA] isFriends:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] isFriends:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (7a)
        //---------------------------------addGroupMembers--------------------------------------
        this._client.addGroupMembers(gid, tos, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] addGroupMembers:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] addGroupMembers:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (7b)
        //---------------------------------deleteGroupMembers--------------------------------------
        this._client.deleteGroupMembers(gid, tos, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] deleteGroupMembers:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] deleteGroupMembers:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (7c)
        //---------------------------------deleteGroup--------------------------------------
        this._client.deleteGroup(gid, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] deleteGroup:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] deleteGroup:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (7d)
        //---------------------------------getGroupMembers--------------------------------------
        this._client.getGroupMembers(gid, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    List<Long> payload = (List<Long>) obj;
                    System.out.print("[DATA] getGroupMembers:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] getGroupMembers:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (7e)
        //---------------------------------isGroupMember--------------------------------------
        this._client.isGroupMember(gid, fuid, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] isGroupMember:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] isGroupMember:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (7f)
        //---------------------------------getUserGroups--------------------------------------
        this._client.getUserGroups(fuid, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    List<Long> payload = (List<Long>) obj;
                    System.out.print("[DATA] getUserGroups:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] getUserGroups:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (7g)
        //---------------------------------addGroupBan--------------------------------------
        this._client.addGroupBan(gid, to, 60, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] addGroupBan:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] addGroupBan:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (7h)
        //---------------------------------removeGroupBan--------------------------------------
        this._client.removeGroupBan(gid, to, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] removeGroupBan:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] removeGroupBan:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (7i)
        //---------------------------------isBanOfGroup--------------------------------------
        this._client.isBanOfGroup(gid, to, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] isBanOfGroup:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] isBanOfGroup:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (7j)
        //---------------------------------setGroupInfo--------------------------------------
        this._client.setGroupInfo(gid, "oinfo", "pinfo", timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] setGroupInfo:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] setGroupInfo:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (7k)
        //---------------------------------getGroupInfo--------------------------------------
        this._client.getGroupInfo(gid, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] getGroupInfo:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] getGroupInfo:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (8a)
        //---------------------------------addRoomBan--------------------------------------
        this._client.addRoomBan(rid, to, 60, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] addRoomBan:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] addRoomBan:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (8b)
        //---------------------------------removeRoomBan--------------------------------------
        this._client.removeRoomBan(rid, to, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] removeRoomBan:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] removeRoomBan:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (8c)
        //---------------------------------isBanOfRoom--------------------------------------
        this._client.isBanOfRoom(rid, to, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] isBanOfRoom:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] isBanOfRoom:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (8d)
        //---------------------------------addRoomMember--------------------------------------
        this._client.addRoomMember(rid, to, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] addRoomMember:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] addRoomMember:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (8e)
        //---------------------------------deleteRoomMember--------------------------------------
        this._client.deleteRoomMember(rid, to, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] deleteRoomMember:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] deleteRoomMember:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (8f)
        //---------------------------------setRoomInfo--------------------------------------
        this._client.setRoomInfo(rid, "oinfo", "pinfo", timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] setRoomInfo:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] setRoomInfo:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (8g)
        //---------------------------------getRoomInfo--------------------------------------
        this._client.getRoomInfo(rid, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] getRoomInfo:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] getRoomInfo:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (9a)
        //---------------------------------addEvtListener--------------------------------------
        this._client.addEvtListener(null, rids, tos, evets, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] addEvtListener:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] addEvtListener:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (9b)
        //---------------------------------removeEvtListener--------------------------------------
        this._client.removeEvtListener(gids, null, tos, evets, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] removeEvtListener:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] removeEvtListener:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (9c')
        //---------------------------------setEvtListener--------------------------------------
        this._client.setEvtListener(gids, rids, tos, evets, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] setEvtListener:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] setEvtListener:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (10b)
        //---------------------------------dataSet--------------------------------------
        this._client.dataSet(to, "db-test-key", "db-test-value", timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] dataSet:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] dataSet:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (10a)
        //---------------------------------dataGet--------------------------------------
        this._client.dataGet(to, "db-test-key", timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] dataGet:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] dataGet:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (10c)
        //---------------------------------dataDelete--------------------------------------
        this._client.dataDelete(to, "db-test-key", timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] dataDelete:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] dataDelete:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //fileGate (1)
        //---------------------------------sendFile--------------------------------------
        this._client.sendFile(from, to, (byte)50, this._fileBytes, null, null, 0, 30 * 1000, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] sendFile:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] sendFile:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep * 5);
        //fileGate (2)
        //---------------------------------sendFiles--------------------------------------
        this._client.sendFiles(from, tos, (byte)50, this._fileBytes, "", "", 0, 30 * 1000, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] sendFiles:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] sendFiles:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep * 5);
        //fileGate (3)
        //---------------------------------sendGroupFile--------------------------------------
        this._client.sendGroupFile(from, gid, (byte)50, this._fileBytes, "jpg", "pic",0, 30 * 1000, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] sendGroupFile:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] sendGroupFile:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep * 5);
        //fileGate (4)
        //---------------------------------sendRoomFile--------------------------------------
        this._client.sendRoomFile(from, rid, (byte)50, this._fileBytes, null, null, 0, 30 * 1000, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] sendRoomFile:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] sendRoomFile:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep * 5);
        //fileGate (5)
        //---------------------------------broadcastFile--------------------------------------
        this._client.broadcastFile(from, (byte)50, this._fileBytes, null, null, 0, 30 * 1000, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.print("[DATA] broadcastFile:");
                    System.out.println(payload.toString());
                } else {
                    System.err.print("[ERR] broadcastFile:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep * 5);
        System.out.println("test end! ".concat(String.valueOf(this._sleepCount - 1)));
    }

    private void threadSleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        this._sleepCount++;
    }
}
