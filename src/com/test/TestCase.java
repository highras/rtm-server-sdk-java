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

        this._client.getEvent().addListener("connect", new FPEvent.IListener() {

            @Override
            public void fpEvent(EventData event) {

                self.onConnect();
            }
        });

        this._client.getEvent().addListener("close", new FPEvent.IListener() {

            @Override
            public void fpEvent(EventData event) {

                self.onClose();
            }
        });

        this._client.getEvent().addListener("error", new FPEvent.IListener() {

            @Override
            public void fpEvent(EventData event) {

                self.onError(event.getException());
            }
        });

        this._client.connect(
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
        long mid = 0;
        byte[] fileBytes = this._client.loadFile("key/java.jpeg");

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

        //ServerGate (1)
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

        //ServerGate (2)
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

        //ServerGate (3)
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

        //ServerGate (4)
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

        //ServerGate (5)
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

        //ServerGate (6)
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

        //ServerGate (7)
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

        //ServerGate (8)
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

        //ServerGate (9)
        //---------------------------------isFriend--------------------------------------
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

        this.threadSleep(sleep);

        //ServerGate (10)
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

        //ServerGate (11)
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

        //ServerGate (12)
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

        //ServerGate (13)
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

        //ServerGate (14)
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

        //ServerGate (15)
        //---------------------------------isGroupMember--------------------------------------
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

        this.threadSleep(sleep);

        //ServerGate (16)
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

        //ServerGate (17)
        //---------------------------------getToken--------------------------------------
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

        this.threadSleep(sleep);

        //ServerGate (18)
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

        //ServerGate (19)
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

        //ServerGate (20)
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

        //ServerGate (21)
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

        //ServerGate (22)
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

        //ServerGate (23)
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

        //ServerGate (24)
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

        //ServerGate (25)
        //---------------------------------isBanOfGroup--------------------------------------
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

        this.threadSleep(sleep);

        //ServerGate (26)
        //---------------------------------isBanOfRoom--------------------------------------
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

        this.threadSleep(sleep);

        //ServerGate (27)
        //---------------------------------isProjectBlack--------------------------------------
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
        
        this.threadSleep(sleep);

        //ServerGate (28)
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

        //ServerGate (29)
        //---------------------------------getGroupMessage--------------------------------------
        this._client.getGroupMessage(gid, false, 10, 0, 0, 0, timeout, new FPCallback.ICallback() {

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

        //ServerGate (30)
        //---------------------------------getRoomMessage--------------------------------------
        this._client.getRoomMessage(rid, false, 10, 0, 0, 0, timeout, new FPCallback.ICallback() {

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

        //ServerGate (31)
        //---------------------------------getBroadcastMessage--------------------------------------
        this._client.getBroadcastMessage(false, 10, 0, 0, 0, timeout, new FPCallback.ICallback() {

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

        //ServerGate (32)
        //---------------------------------getP2PMessage--------------------------------------
        this._client.getP2PMessage(from, to, false, 10, 0, 0, 0, timeout, new FPCallback.ICallback() {

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

        //ServerGate (33)
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

        //ServerGate (34)
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

        //ServerGate (35)
        //---------------------------------addEvtListener--------------------------------------
        this._client.addEvtListener(null, rids, true, evets, timeout, new FPCallback.ICallback() {

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

        //ServerGate (36)
        //---------------------------------removeEvtListener--------------------------------------
        this._client.removeEvtListener(gids, null, false, evets, timeout, new FPCallback.ICallback() {

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

        //ServerGate (37)
        //---------------------------------setEvtListener--------------------------------------
        this._client.setEvtListener(true, timeout, new FPCallback.ICallback() {

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

        // this._client.setEvtListener(gids, rids, true, evets, timeout, new FPCallback.ICallback() {

        //     @Override
        //     public void callback(CallbackData cbd) {

        //         Object obj = cbd.getPayload();

        //         if (obj != null) {

        //             Map payload = (Map) obj;
        //             System.out.println("\n[DATA] setEvtListener:");
        //             System.out.println(payload);
        //         } else {

        //             System.err.println("\n[ERR] setEvtListener:");
        //             System.err.println(cbd.getException().getMessage());
        //         }
        //     }
        // });

        this.threadSleep(sleep);

        //ServerGate (38)
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

        //ServerGate (39)
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

        //ServerGate (40)
        //---------------------------------deleteMessage--------------------------------------
        this._client.deleteMessage(mid, from, to, (byte) 1, timeout, new FPCallback.ICallback() {

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

        //ServerGate (41)
        //---------------------------------kickout--------------------------------------
        this._client.kickout(to, "", timeout, new FPCallback.ICallback() {

            @Override
            public void callback(CallbackData cbd) {

                Object obj = cbd.getPayload();

                if (obj != null) {

                    Map payload = (Map) obj;
                    System.out.println("\n[DATA] kickout:");
                    System.out.println(payload);
                } else {

                    System.err.println("\n[ERR] kickout:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });

        this.threadSleep(sleep);

        //fileGate (1)
        //---------------------------------sendFile--------------------------------------
        this._client.sendFile(from, to, (byte)50, fileBytes, 0, 30 * 1000, new FPCallback.ICallback() {

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

        //fileGate (2)
        //---------------------------------sendFiles--------------------------------------
        this._client.sendFiles(from, tos, (byte)50, fileBytes, 0, 30 * 1000, new FPCallback.ICallback() {

            @Override
            public void callback(CallbackData cbd) {

                Object obj = cbd.getPayload();

                if (obj != null) {

                    Map payload = (Map) obj;
                    System.out.println("\n[DATA] sendFiles:");
                    System.out.println(payload);
                } else {

                    System.err.println("\n[ERR] sendFiles:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });

        this.threadSleep(sleep);

        //fileGate (3)
        //---------------------------------sendGroupFile--------------------------------------
        this._client.sendGroupFile(from, gid, (byte)50, fileBytes, 0, 30 * 1000, new FPCallback.ICallback() {

            @Override
            public void callback(CallbackData cbd) {

                Object obj = cbd.getPayload();

                if (obj != null) {

                    Map payload = (Map) obj;
                    System.out.println("\n[DATA] sendGroupFile:");
                    System.out.println(payload);
                } else {

                    System.err.println("\n[ERR] sendGroupFile:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });

        this.threadSleep(sleep);

        //fileGate (4)
        //---------------------------------sendRoomFile--------------------------------------
        this._client.sendRoomFile(from, rid, (byte)50, fileBytes, 0, 30 * 1000, new FPCallback.ICallback() {

            @Override
            public void callback(CallbackData cbd) {

                Object obj = cbd.getPayload();

                if (obj != null) {

                    Map payload = (Map) obj;
                    System.out.println("\n[DATA] sendRoomFile:");
                    System.out.println(payload);
                } else {

                    System.err.println("\n[ERR] sendRoomFile:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });

        this.threadSleep(sleep);

        //fileGate (5)
        //---------------------------------broadcastFile--------------------------------------
        this._client.broadcastFile(from, (byte)50, fileBytes, 0, 30 * 1000, new FPCallback.ICallback() {

            @Override
            public void callback(CallbackData cbd) {

                Object obj = cbd.getPayload();

                if (obj != null) {

                    Map payload = (Map) obj;
                    System.out.println("\n[DATA] broadcastFile:");
                    System.out.println(payload);
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
}
