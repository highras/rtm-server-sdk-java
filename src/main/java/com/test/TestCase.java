package com.test;

import com.fpnn.ErrorRecorder;
import com.fpnn.callback.CallbackData;
import com.fpnn.callback.FPCallback;
import com.fpnn.event.EventData;
import com.fpnn.event.FPEvent;
import com.rtm.RTMClient;
import com.rtm.RTMConfig;
import com.rtm.RTMProcessor;
import com.rtm.json.JsonHelper;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class TestCase {

    private RTMClient _client;
    private int _sleepCount;

    public TestCase() {
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
                self.onConnect();
            }
        });
        this._client.getEvent().addListener("close", new FPEvent.IListener() {
            @Override
            public void fpEvent(EventData evd) {
                self.onClose();
            }
        });
        this._client.getEvent().addListener("error", new FPEvent.IListener() {
            @Override
            public void fpEvent(EventData evd) {
                self.onError(evd.getException());
            }
        });
        this._client.connect(
            "secp256k1",
            "key/test-secp256k1-public.der-false",
            false,
            false
        );
        RTMProcessor processor = this._client.getProcessor();
        processor.addPushService(RTMConfig.SERVER_PUSH.recvMessage, new RTMProcessor.IService() {
            @Override
            public void Service(Map<String, Object> data) {
                System.out.println("[recvMessage]: " + JsonHelper.getInstance().getJson().toJSON(data));
            }
        });
    }

    private void onConnect() {
        System.out.println(new String("Connected!"));
        long from = 778877;
        long to = 778899;
        long fuid = 778898;
        long mid = 0;
        byte[] fileBytes = new LoadFile().read("key/java.jpeg");
        List<Long> tos = new ArrayList();
        tos.add(from);
        tos.add(fuid);
        tos.add(to);
        long gid = 999999;
        long rid = 666666;
        List<Long> friends = new ArrayList();
        friends.add(fuid);
        friends.add(to);
        double lat = 39239.1123;
        double lng = 69394.4850;
        List<Long> gids = new ArrayList();
        gids.add(gid);
        List<Long> rids = new ArrayList();
        rids.add(rid);
        List<String> evets = new ArrayList();
        evets.add(RTMConfig.SERVER_EVENT.login);
        evets.add(RTMConfig.SERVER_EVENT.logout);
        int timeout = 20 * 1000;
        int sleep = 1000;
        System.out.println("\ntest start!");
        this.threadSleep(sleep);
        //ServerGate (9c)
        //---------------------------------setEvtListener--------------------------------------
        this._client.setEvtListener(true, true, true, true, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.println("\n[DATA] setEvtListener:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] setEvtListener:");
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
                    System.out.println("\n[DATA] getToken:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] getToken:");
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
                    System.out.println("\n[DATA] kickout:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] kickout:");
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
                    System.out.println("\n[DATA] addDevice:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] addDevice:");
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
                    System.out.println("\n[DATA] removeDevice:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] removeDevice:");
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
                    System.out.println("\n[DATA] sendMessage:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] sendMessage:");
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
                    System.out.println("\n[DATA] sendMessages:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] sendMessages:");
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
                    System.out.println("\n[DATA] sendGroupMessage:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] sendGroupMessage:");
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
                    System.out.println("\n[DATA] sendRoomMessage:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] sendRoomMessage:");
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
                    System.out.println("\n[DATA] broadcastMessage:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] broadcastMessage:");
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
                    System.out.println("\n[DATA] getGroupMessage:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] getGroupMessage:");
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
                    System.out.println("\n[DATA] getRoomMessage:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] getRoomMessage:");
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
                    System.out.println("\n[DATA] getBroadcastMessage:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] getBroadcastMessage:");
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
                    System.out.println("\n[DATA] getP2PMessage:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] getP2PMessage:");
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
                    System.out.println("\n[DATA] deleteMessage:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] deleteMessage:");
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
                    System.out.println("\n[DATA] sendChat:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] sendChat:");
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
                    System.out.println("\n[DATA] sendChats:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] sendChats:");
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
                    System.out.println("\n[DATA] sendGroupChat:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] sendGroupChat:");
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
                    System.out.println("\n[DATA] sendRoomChat:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] sendRoomChat:");
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
                    System.out.println("\n[DATA] broadcastChat:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] broadcastChat:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (3f)
        //---------------------------------getGroupChat--------------------------------------
        this._client.getGroupChat(gid, false, 10, 0, 0, 0, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.println("\n[DATA] getGroupChat:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] getGroupChat:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (3g)
        //---------------------------------getRoomChat--------------------------------------
        this._client.getRoomChat(rid, false, 10, 0, 0, 0, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.println("\n[DATA] getRoomChat:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] getRoomChat:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (3h)
        //---------------------------------getBroadcastChat--------------------------------------
        this._client.getBroadcastChat(false, 10, 0, 0, 0, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.println("\n[DATA] getBroadcastChat:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] getBroadcastChat:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (3i)
        //---------------------------------getP2PChat--------------------------------------
        this._client.getP2PChat(from, to, false, 10, 0, 0, 0, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.println("\n[DATA] getP2PChat:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] getP2PChat:");
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
                    System.out.println("\n[DATA] deleteChat:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] deleteChat:");
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
                    System.out.println("\n[DATA] translate:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] translate:");
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
                    System.out.println("\n[DATA] profanity:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] profanity:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //ServerGate (4a)
        //---------------------------------fileToken--------------------------------------
        this._client.fileToken(from, "sendfile", null, to, 0, 0, timeout, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.println("\n[DATA] fileToken:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] fileToken:");
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
                    System.out.println("\n[DATA] getOnlineUsers:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] getOnlineUsers:");
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
                    System.out.println("\n[DATA] addProjectBlack:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] addProjectBlack:");
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
                    System.out.println("\n[DATA] removeProjectBlack:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] removeProjectBlack:");
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
                    System.out.println("\n[DATA] isProjectBlack:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] isProjectBlack:");
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
                    System.out.println("\n[DATA] setUserInfo:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] setUserInfo:");
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
                    System.out.println("\n[DATA] getUserInfo:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] getUserInfo:");
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
                    System.out.println("\n[DATA] getUserOpenInfo:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] getUserOpenInfo:");
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
                    System.out.println("\n[DATA] addFriends:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] addFriends:");
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
                    System.out.println("\n[DATA] deleteFriends:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] deleteFriends:");
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
                    System.out.println("\n[DATA] getFriends:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] getFriends:");
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
                    System.out.println("\n[DATA] isFriend:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] isFriend:");
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
                    System.out.println("\n[DATA] isFriends:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] isFriends:");
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
                    System.out.println("\n[DATA] addGroupMembers:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] addGroupMembers:");
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
                    System.out.println("\n[DATA] deleteGroupMembers:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] deleteGroupMembers:");
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
                    System.out.println("\n[DATA] deleteGroup:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] deleteGroup:");
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
                    System.out.println("\n[DATA] getGroupMembers:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] getGroupMembers:");
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
                    System.out.println("\n[DATA] isGroupMember:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] isGroupMember:");
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
                    System.out.println("\n[DATA] getUserGroups:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] getUserGroups:");
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
                    System.out.println("\n[DATA] addGroupBan:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] addGroupBan:");
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
                    System.out.println("\n[DATA] removeGroupBan:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] removeGroupBan:");
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
                    System.out.println("\n[DATA] isBanOfGroup:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] isBanOfGroup:");
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
                    System.out.println("\n[DATA] setGroupInfo:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] setGroupInfo:");
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
                    System.out.println("\n[DATA] getGroupInfo:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] getGroupInfo:");
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
                    System.out.println("\n[DATA] addRoomBan:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] addRoomBan:");
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
                    System.out.println("\n[DATA] removeRoomBan:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] removeRoomBan:");
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
                    System.out.println("\n[DATA] isBanOfRoom:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] isBanOfRoom:");
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
                    System.out.println("\n[DATA] addRoomMember:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] addRoomMember:");
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
                    System.out.println("\n[DATA] deleteRoomMember:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] deleteRoomMember:");
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
                    System.out.println("\n[DATA] setRoomInfo:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] setRoomInfo:");
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
                    System.out.println("\n[DATA] getRoomInfo:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] getRoomInfo:");
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
                    System.out.println("\n[DATA] addEvtListener:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] addEvtListener:");
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
                    System.out.println("\n[DATA] removeEvtListener:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] removeEvtListener:");
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
                    System.out.println("\n[DATA] setEvtListener:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] setEvtListener:");
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
                    System.out.println("\n[DATA] dataSet:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] dataSet:");
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
                    System.out.println("\n[DATA] dataGet:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] dataGet:");
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
                    System.out.println("\n[DATA] dataDelete:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] dataDelete:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //fileGate (1)
        //---------------------------------sendFile--------------------------------------
        this._client.sendFile(from, to, (byte)50, fileBytes, null, null, 0, 30 * 1000, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.println("\n[DATA] sendFile:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] sendFile:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //fileGate (2)
        //---------------------------------sendFiles--------------------------------------
        this._client.sendFiles(from, tos, (byte)50, fileBytes, "", "", 0, 30 * 1000, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.println("\n[DATA] sendFiles:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] sendFiles:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //fileGate (3)
        //---------------------------------sendGroupFile--------------------------------------
        this._client.sendGroupFile(from, gid, (byte)50, fileBytes, "jpg", "pic",0, 30 * 1000, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.println("\n[DATA] sendGroupFile:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] sendGroupFile:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //fileGate (4)
        //---------------------------------sendRoomFile--------------------------------------
        this._client.sendRoomFile(from, rid, (byte)50, fileBytes, null, null, 0, 30 * 1000, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.println("\n[DATA] sendRoomFile:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] sendRoomFile:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        //fileGate (5)
        //---------------------------------broadcastFile--------------------------------------
        this._client.broadcastFile(from, (byte)50, fileBytes, null, null, 0, 30 * 1000, new FPCallback.ICallback() {
            @Override
            public void callback(CallbackData cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.println("\n[DATA] broadcastFile:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] broadcastFile:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
        this.threadSleep(sleep);
        System.out.println("\ntest end! ".concat(String.valueOf(this._sleepCount - 1)));
    }

    private void onClose() {
        System.out.println(new String("Closed!"));
    }

    private void onError(Exception ex) {
        ex.printStackTrace();
    }

    private void threadSleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        this._sleepCount++;
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
