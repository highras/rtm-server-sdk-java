package com.test;

import com.fpnn.callback.CallbackData;
import com.fpnn.callback.FPCallback;
import com.fpnn.event.EventData;
import com.fpnn.event.FPEvent;
import com.rtm.RTMClient;
import com.rtm.RTMConfig;

import java.util.*;

public class TestCase {

    private RTMClient _client;
    private int _sleepCount;

    public TestCase() {

        this._client = new RTMClient(
                11000001,
                "ef3617e5-e886-4a4e-9eef-7263c0320628",
                "52.83.245.22",
                13315,
                true,
                20 * 1000,
                true
        );

        TestCase self = this;
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
                        self.onError(event.getException());
                        break;
                }
            }
        };

        this._client.getEvent().addListener("connect", listener);
        this._client.getEvent().addListener("close", listener);
        this._client.getEvent().addListener("error", listener);

        this._client.enableEncryptorByFile(
                "secp256k1",
                "key/test-secp256k1-public.der-false",
                false,
                false
        );

        this._client.getProcessor().getEvent().addListener(RTMConfig.SERVER_PUSH.recvPing, new FPEvent.IListener() {

            @Override
            public void fpEvent(EventData event) {

                System.out.println("\n[PUSH] ".concat(event.getType()).concat(":"));
                System.out.println(event.getPayload().toString());
            }
        });
    }

    private void onConnect() {

        System.out.println(new String("Connected!"));

        long from = 778877;
        long to = 778899;
        long fuid = 778898;

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

        //---------------------------------setEvtListener--------------------------------------
        this.threadSleep(sleep);
        this._client.setEvtListener(true, timeout, new FPCallback.ICallback() {

            @Override
            public void callback(CallbackData cbd) {

                Object obj = cbd.getPayload();

                if (obj != null) {

                    Map payload = (Map) obj;
                    System.out.println("\n[DATA] setEvtListener:");
                    System.out.println(payload);
                } else {

                    System.err.println("\n[ERR] setEvtListener:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });

        //---------------------------------setEvtListener--------------------------------------
        this.threadSleep(sleep);
        this._client.setEvtListener(false, timeout, new FPCallback.ICallback() {

            @Override
            public void callback(CallbackData cbd) {

                Object obj = cbd.getPayload();

                if (obj != null) {

                    Map payload = (Map) obj;
                    System.out.println("\n[DATA] setEvtListener:");
                    System.out.println(payload);
                } else {

                    System.err.println("\n[ERR] setEvtListener:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });

        //---------------------------------setEvtListener--------------------------------------
        this.threadSleep(sleep);
        this._client.setEvtListener(gids, rids, true, evets, timeout, new FPCallback.ICallback() {

            @Override
            public void callback(CallbackData cbd) {

                Object obj = cbd.getPayload();

                if (obj != null) {

                    Map payload = (Map) obj;
                    System.out.println("\n[DATA] setEvtListener:");
                    System.out.println(payload);
                } else {

                    System.err.println("\n[ERR] setEvtListener:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });

        //---------------------------------addEvtListener--------------------------------------
        this.threadSleep(sleep);
        this._client.addEvtListener(null, rids, true, evets, timeout, new FPCallback.ICallback() {

            @Override
            public void callback(CallbackData cbd) {

                Object obj = cbd.getPayload();

                if (obj != null) {

                    Map payload = (Map) obj;
                    System.out.println("\n[DATA] addEvtListener:");
                    System.out.println(payload);
                } else {

                    System.err.println("\n[ERR] addEvtListener:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });

        //---------------------------------removeEvtListener--------------------------------------
        this._client.removeEvtListener(gids, null, false, evets, timeout, new FPCallback.ICallback() {

            @Override
            public void callback(CallbackData cbd) {

                Object obj = cbd.getPayload();

                if (obj != null) {

                    Map payload = (Map) obj;
                    System.out.println("\n[DATA] removeEvtListener:");
                    System.out.println(payload);
                } else {

                    System.err.println("\n[ERR] removeEvtListener:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });

        //---------------------------------sendMessage--------------------------------------
        this.threadSleep(sleep);
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

        //---------------------------------sendMessages--------------------------------------
        this.threadSleep(sleep);
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

        //---------------------------------sendGroupMessage--------------------------------------
        this.threadSleep(sleep);
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

        //---------------------------------sendRoomMessage--------------------------------------
        this.threadSleep(sleep);
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

        //---------------------------------broadcastMessage--------------------------------------
        this.threadSleep(sleep);
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

        //---------------------------------addFriends--------------------------------------
        this.threadSleep(sleep);
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

        //---------------------------------deleteFriends--------------------------------------
        this.threadSleep(sleep);
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

        //---------------------------------getFriends--------------------------------------
        this.threadSleep(sleep);
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

        //---------------------------------isFriend--------------------------------------
        this.threadSleep(sleep);
        this._client.isFriend(from, fuid, timeout, new FPCallback.ICallback() {

            @Override
            public void callback(CallbackData cbd) {

                Object obj = cbd.getPayload();

                if (obj != null) {

                    boolean payload = (Boolean) obj;
                    System.out.println("\n[DATA] isFriend:");
                    System.out.println(payload);
                } else {

                    System.err.println("\n[ERR] isFriend:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });

        //---------------------------------isFriends--------------------------------------
        this.threadSleep(sleep);
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

        //---------------------------------addGroupMembers--------------------------------------
        this.threadSleep(sleep);
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

        //---------------------------------deleteGroupMembers--------------------------------------
        this.threadSleep(sleep);
        this._client.deleteGroupMembers(rid, tos, timeout, new FPCallback.ICallback() {

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

        //---------------------------------deleteGroup--------------------------------------
        this.threadSleep(sleep);
        this._client.deleteGroup(rid, timeout, new FPCallback.ICallback() {

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

        //---------------------------------getGroupMembers--------------------------------------
        this.threadSleep(sleep);
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

        //---------------------------------isGroupMember--------------------------------------
        this.threadSleep(sleep);
        this._client.isGroupMember(gid, fuid, timeout, new FPCallback.ICallback() {

            @Override
            public void callback(CallbackData cbd) {

                Object obj = cbd.getPayload();

                if (obj != null) {

                    boolean payload = (Boolean) obj;
                    System.out.println("\n[DATA] isGroupMember:");
                    System.out.println(payload);
                } else {

                    System.err.println("\n[ERR] isGroupMember:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });

        //---------------------------------getUserGroups--------------------------------------
        this.threadSleep(sleep);
        this._client.getUserGroups(fuid, timeout, new FPCallback.ICallback() {

            @Override
            public void callback(CallbackData cbd) {

                Object obj = cbd.getPayload();

                if (obj != null) {

                    List<Long> payload = (List<Long>) obj;
                    System.out.println("\n[DATA] getUserGroups:");
                    System.out.println(payload);
                } else {

                    System.err.println("\n[ERR] getUserGroups:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });

        //---------------------------------getToken--------------------------------------
        this.threadSleep(sleep);
        this._client.getToken(from, timeout, new FPCallback.ICallback() {

            @Override
            public void callback(CallbackData cbd) {

                Object obj = cbd.getPayload();

                if (obj != null) {

                    String payload = (String) obj;
                    System.out.println("\n[DATA] getToken:");
                    System.out.println(payload);
                } else {

                    System.err.println("\n[ERR] getToken:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });

        //---------------------------------getOnlineUsers--------------------------------------
        this.threadSleep(sleep);
        this._client.getOnlineUsers(tos, timeout, new FPCallback.ICallback() {

            @Override
            public void callback(CallbackData cbd) {

                Object obj = cbd.getPayload();

                if (obj != null) {

                    List<Long> payload = (List<Long>) obj;
                    System.out.println("\n[DATA] getOnlineUsers:");
                    System.out.println(payload);
                } else {

                    System.err.println("\n[ERR] getOnlineUsers:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });

        //---------------------------------addGroupBan--------------------------------------
        this.threadSleep(sleep);
        this._client.addGroupBan(gid, to, 60, timeout, new FPCallback.ICallback() {

            @Override
            public void callback(CallbackData cbd) {

                Object obj = cbd.getPayload();

                if (obj != null) {

                    Map payload = (Map) obj;
                    System.out.println("\n[DATA] addGroupBan:");
                    System.out.println(payload);
                } else {

                    System.err.println("\n[ERR] addGroupBan:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });

        //---------------------------------removeGroupBan--------------------------------------
        this.threadSleep(sleep);
        this._client.removeGroupBan(gid, to, timeout, new FPCallback.ICallback() {

            @Override
            public void callback(CallbackData cbd) {

                Object obj = cbd.getPayload();

                if (obj != null) {

                    Map payload = (Map) obj;
                    System.out.println("\n[DATA] removeGroupBan:");
                    System.out.println(payload);
                } else {

                    System.err.println("\n[ERR] removeGroupBan:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });

        //---------------------------------addRoomBan--------------------------------------
        this.threadSleep(sleep);
        this._client.addRoomBan(rid, to, 60, timeout, new FPCallback.ICallback() {

            @Override
            public void callback(CallbackData cbd) {

                Object obj = cbd.getPayload();

                if (obj != null) {

                    Map payload = (Map) obj;
                    System.out.println("\n[DATA] addRoomBan:");
                    System.out.println(payload);
                } else {

                    System.err.println("\n[ERR] addRoomBan:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });

        //---------------------------------removeRoomBan--------------------------------------
        this.threadSleep(sleep);
        this._client.removeRoomBan(rid, to, timeout, new FPCallback.ICallback() {

            @Override
            public void callback(CallbackData cbd) {

                Object obj = cbd.getPayload();

                if (obj != null) {

                    Map payload = (Map) obj;
                    System.out.println("\n[DATA] removeRoomBan:");
                    System.out.println(payload);
                } else {

                    System.err.println("\n[ERR] removeRoomBan:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });

        //---------------------------------addProjectBlack--------------------------------------
        this.threadSleep(sleep);
        this._client.addProjectBlack(to, 60, timeout, new FPCallback.ICallback() {

            @Override
            public void callback(CallbackData cbd) {

                Object obj = cbd.getPayload();

                if (obj != null) {

                    Map payload = (Map) obj;
                    System.out.println("\n[DATA] addProjectBlack:");
                    System.out.println(payload);
                } else {

                    System.err.println("\n[ERR] addProjectBlack:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });

        //---------------------------------removeProjectBlack--------------------------------------
        this.threadSleep(sleep);
        this._client.removeProjectBlack(to, timeout, new FPCallback.ICallback() {

            @Override
            public void callback(CallbackData cbd) {

                Object obj = cbd.getPayload();

                if (obj != null) {

                    Map payload = (Map) obj;
                    System.out.println("\n[DATA] removeProjectBlack:");
                    System.out.println(payload);
                } else {

                    System.err.println("\n[ERR] removeProjectBlack:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });

        //---------------------------------isBanOfGroup--------------------------------------
        this.threadSleep(sleep);
        this._client.isBanOfGroup(gid, to, timeout, new FPCallback.ICallback() {

            @Override
            public void callback(CallbackData cbd) {

                Object obj = cbd.getPayload();

                if (obj != null) {

                    boolean payload = (Boolean) obj;
                    System.out.println("\n[DATA] isBanOfGroup:");
                    System.out.println(payload);
                } else {

                    System.err.println("\n[ERR] isBanOfGroup:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });

        //---------------------------------isBanOfRoom--------------------------------------
        this.threadSleep(sleep);
        this._client.isBanOfRoom(rid, to, timeout, new FPCallback.ICallback() {

            @Override
            public void callback(CallbackData cbd) {

                Object obj = cbd.getPayload();

                if (obj != null) {

                    boolean payload = (Boolean) obj;
                    System.out.println("\n[DATA] isBanOfRoom:");
                    System.out.println(payload);
                } else {

                    System.err.println("\n[ERR] isBanOfRoom:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });

        //---------------------------------isProjectBlack--------------------------------------
        this.threadSleep(sleep);
        this._client.isProjectBlack(to, timeout, new FPCallback.ICallback() {

            @Override
            public void callback(CallbackData cbd) {

                Object obj = cbd.getPayload();

                if (obj != null) {

                    boolean payload = (Boolean) obj;
                    System.out.println("\n[DATA] isProjectBlack:");
                    System.out.println(payload);
                } else {

                    System.err.println("\n[ERR] isProjectBlack:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });

        //---------------------------------setGeo--------------------------------------
        this.threadSleep(sleep);
        this._client.setGeo(from, lat, lng, timeout, new FPCallback.ICallback() {

            @Override
            public void callback(CallbackData cbd) {

                Object obj = cbd.getPayload();

                if (obj != null) {

                    Map payload = (Map) obj;
                    System.out.println("\n[DATA] setGeo:");
                    System.out.println(payload);
                } else {

                    System.err.println("\n[ERR] setGeo:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });

        //---------------------------------getGeo--------------------------------------
        this.threadSleep(sleep);
        this._client.getGeo(from, timeout, new FPCallback.ICallback() {

            @Override
            public void callback(CallbackData cbd) {

                Object obj = cbd.getPayload();

                if (obj != null) {

                    Map payload = (Map) obj;
                    System.out.println("\n[DATA] getGeo:");
                    System.out.println(payload);
                } else {

                    System.err.println("\n[ERR] getGeo:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });

        //---------------------------------getGeos--------------------------------------
        this.threadSleep(sleep);
        this._client.getGeos(tos, timeout, new FPCallback.ICallback() {

            @Override
            public void callback(CallbackData cbd) {

                Object obj = cbd.getPayload();

                if (obj != null) {

                    List<ArrayList> payload = (List<ArrayList>) obj;
                    System.out.println("\n[DATA] getGeos:");
                    System.out.println(payload);
                } else {

                    System.err.println("\n[ERR] getGeos:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });

        //---------------------------------addDevice--------------------------------------
        this.threadSleep(sleep);
        this._client.addDevice(from, "app-info", "device-token", timeout, new FPCallback.ICallback() {

            @Override
            public void callback(CallbackData cbd) {

                Object obj = cbd.getPayload();

                if (obj != null) {

                    Map payload = (Map) obj;
                    System.out.println("\n[DATA] addDevice:");
                    System.out.println(payload);
                } else {

                    System.err.println("\n[ERR] addDevice:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });

        //---------------------------------removeDevice--------------------------------------
        this.threadSleep(sleep);
        this._client.removeDevice(from, "app-info", timeout, new FPCallback.ICallback() {

            @Override
            public void callback(CallbackData cbd) {

                Object obj = cbd.getPayload();

                if (obj != null) {

                    Map payload = (Map) obj;
                    System.out.println("\n[DATA] removeDevice:");
                    System.out.println(payload);
                } else {

                    System.err.println("\n[ERR] removeDevice:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });

        //---------------------------------sendFile--------------------------------------
        this.threadSleep(sleep);
        this._client.sendFile(from, to, (byte) 8, "key/java.jpeg", 0, 30 * 1000, new FPCallback.ICallback() {

            @Override
            public void callback(CallbackData cbd) {

                Object obj = cbd.getPayload();

                if (obj != null) {

                    Map payload = (Map) obj;
                    System.out.println("\n[DATA] sendFile:");
                    System.out.println(payload);
                } else {

                    System.err.println("\n[ERR] sendFile:");
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
}
