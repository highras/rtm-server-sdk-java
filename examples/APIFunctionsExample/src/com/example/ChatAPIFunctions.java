package com.example;

import com.fpnn.rtm.RTMClientConnectCallback;
import com.fpnn.rtm.RTMClientHasClosedCallback;
import com.fpnn.rtm.RTMClientWillCloseCallback;
import com.fpnn.rtm.RTMServerClient;
import com.fpnn.rtm.RTMException;
import com.fpnn.rtm.RTMServerClientBase;
import com.fpnn.rtm.api.APIBase;
import com.fpnn.sdk.ErrorCode;
import com.fpnn.sdk.ErrorRecorder;

import java.util.HashSet;
import java.util.Set;

import static java.lang.Thread.sleep;

public class ChatAPIFunctions {

    private static final long adminUid = 111;
    private static final long from = 1111;
    private static final long to = 2222;
    private static final long gid = 3333;
    private static final long rid = 4444;
    private static final Set<Long> uids = new HashSet<Long>(){{add((long)5555); add((long)6666);}};

    public static void main(String[] args) {

        // get your project params from RTM Console.
        RTMServerClient client = RTMServerClient.create(80000172, "6ae478c8-8997-4607-878f-485f81478a4e",
                "rtm-nx-back.ilivedata.com:13315");
        //-- Optional
        //client.setAutoCleanup(false);

        try{
            sendP2PChat(client);
            sendP2PsChat(client);
            sendGroupChat(client);
            sendRoomChat(client);
            sendBroadCastChat(client);
            getChat(client);
            delChat(client);

            sleep(5000); //-- Waiting for the async callback printed.

        }catch (Exception ex){
            System.out.println("fun exception msg ");
            ex.printStackTrace();
        }

        client.close();
        //-- Wait for close event is processed.
        try{
            sleep(200000);
        }catch (Exception ex){
            System.out.println("fun exception msg: ");
            ex.printStackTrace();
        }
//
//        //-- Optional
        ErrorRecorder recorder = (ErrorRecorder)ErrorRecorder.getInstance();
        recorder.println();
//
//        //-- Optional: Only when client.setAutoCleanup(false); must call this function for cleaning up;
//        client.SDKCleanup();
    }

    private static void sendP2PChat(RTMServerClient client){
        try{
            // sync send p2p chat
            long time = client.sendChat(from, to, "chat hello word", "");
            System.out.println("sync return chat mtime " + time);
            time= client.sendCmd(from, to, "cmd hello word", "");
            System.out.println("sync return cmd mtime " + time);

            // async send p2p chat
            client.sendChat(from, to, "chat hello word", "", (time1, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return sendChat error answer errorCode " + errorCode + " error message = " + errorMessage);
                }
                else{
                    System.out.println("async return sendChat success answer time = " + time1);
                }
            });

            client.sendCmd(from, to, "cmd hello word", "", (time13, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return sendCmd error answer errorCode " + errorCode + " error message = " + errorMessage);
                }
                else{
                    System.out.println("async return sendCmd success answer time = " + time13);
                }
            });
        }catch (RTMException ex){
            System.out.println(ex.toString());
        }
        catch (Exception ex){
            System.out.println("fun exception msg ");
            ex.printStackTrace();
        }

    }

    private static void sendP2PsChat(RTMServerClient client){
        try{
            // sync send p2ps chat
            long time = client.sendChats(from, uids, "chats hello word", "");
            System.out.println("sync return chats mtime " + time);
            time = client.sendCmds(from, uids, "cmds hello word", "");
            System.out.println("sync return cmds mtime " + time);

            // async send p2ps chat
            client.sendChats(from, uids, "chats hello word", "", (time1, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return sendChats error answer errorCode " + errorCode + " error message = " + errorMessage);
                }
                else{
                    System.out.println("async return sendChats success answer time = " + time1);
                }
            });


            client.sendCmds(from, uids, "cmds hello word", "", (time13, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return sendCmds error answer errorCode " + errorCode + " error message = " + errorMessage);
                }
                else{
                    System.out.println("async return sendCmds success answer time = " + time13);
                }
            });
        }catch (RTMException ex){
            System.out.println(ex.toString());
        }
        catch (Exception ex){
            System.out.println("fun exception msg ");
            ex.printStackTrace();
        }
    }

    private static void sendGroupChat(RTMServerClient client){
        try{
            // sync send group chat
            long time = client.sendGroupChat(from, gid, "group chat hello word", "");
            System.out.println("sync return group chat mtime " + time);
            time = client.sendGroupCmd(from, gid, "group cmd hello word", "");
            System.out.println("sync return group cmd mtime " + time);

            // async send group chat
            client.sendGroupChat(from, gid,"group chat hello word", "", (time1, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return sendGroupChat error answer errorCode " + errorCode + " error message = " + errorMessage);
                }
                else{
                    System.out.println("async return sendGroupChat success answer time = " + time1);
                }
            });

            client.sendGroupCmd(from, gid,"group cmd hello word", "", (time13, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return sendGroupCmd error answer errorCode " + errorCode + " error message = " + errorMessage);
                }
                else{
                    System.out.println("async return sendGroupCmd success answer time = " + time13);
                }
            });
        }catch (RTMException ex){
            System.out.println(ex.toString());
        }
        catch (Exception ex){
            System.out.println("fun exception msg ");
            ex.printStackTrace();
        }

    }

    private static void sendRoomChat(RTMServerClient client){
        try{
            // sync send room chat
            long time = client.sendRoomChat(from, rid, "room chat hello word", "");
            System.out.println("sync return room chat mtime " + time);
            time = client.sendRoomCmd(from, rid, "room cmd hello word", "");
            System.out.println("sync return room cmd mtime " + time);

            // async send room chat
            client.sendRoomChat(from, rid,"room chat hello word", "", (time1, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return sendRoomChat error answer errorCode " + errorCode + " error message = " + errorMessage);
                }
                else{
                    System.out.println("async return sendRoomChat success answer time = " + time1);
                }
            });

            client.sendRoomCmd(from, rid,"room cmd hello word", "", (time13, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return sendRoomCmd error answer errorCode " + errorCode + " error message = " + errorMessage);
                }
                else{
                    System.out.println("async return sendRoomCmd success answer time = " + time13);
                }
            });
        }catch (RTMException ex){
            System.out.println(ex.toString());
        }
        catch (Exception ex){
            System.out.println("fun exception msg ");
            ex.printStackTrace();
        }
    }

    private static void sendBroadCastChat(RTMServerClient client){
        try{
            // sync send broadcast chat
            long time = client.sendBroadcastChat(adminUid, "broadcast chat hello word", "");
            System.out.println("sync return broadcast chat mtime " + time);
            time = client.sendBroadcastCmd(adminUid, "room cmd hello word", "");
            System.out.println("sync return broadcast cmd mtime " + time);

            //async send broadcast chat
            client.sendBroadcastChat(adminUid, "broadcast chat hello word", "", (time1, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return sendBroadcastChat error answer errorCode " + errorCode + " error message = " + errorMessage);
                }
                else{
                    System.out.println("async return sendBroadcastChat success answer time = " + time1);
                }
            });

            client.sendBroadcastCmd(adminUid, "broadcast cmd hello word", "", (time13, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return sendBroadcastCmd error answer errorCode " + errorCode + " error message = " + errorMessage);
                }
                else{
                    System.out.println("async return sendBroadcastCmd success answer time = " + time13);
                }
            });
        }catch (RTMException ex){
            System.out.println(ex.toString());
        }
        catch (Exception ex){
            System.out.println("fun exception msg ");
            ex.printStackTrace();
        }
    }

    private static void getChat(RTMServerClient client){
        try{
            // sync get chat
            long mid =123456789;
            RTMServerClientBase.RTMHistoryMessageUnit message = client.getChat(mid, from, gid, APIBase.MessageType.MESSAGE_TYPE_GROUP);
            System.out.println("sync return getChat success " + message.toString());

            // async get chat
            mid = 987654321;
            client.getChat(mid, from, rid, APIBase.MessageType.MESSAGE_TYPE_ROOM, (message1, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return getChat error answer errorCode " + errorCode + " error message = " + errorMessage);
                }
                else {
                    System.out.println("async return getChat success " + message1.toString());
                }
            });
        }catch (RTMException ex){
            System.out.println(ex.toString());
        }
        catch (Exception ex){
            System.out.println("fun exception msg ");
            ex.printStackTrace();
        }
    }

    private static void delChat(RTMServerClient client){
        try{
            // sync del chat
            long mid =123456789;
            client.deleteChat(mid, from, gid, APIBase.MessageType.MESSAGE_TYPE_GROUP);
            System.out.println("sync return deleteChat ok!");

            // async del chat
            mid = 987654321;
            client.deleteChat(mid, from, rid, APIBase.MessageType.MESSAGE_TYPE_ROOM, (errorCode, message) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return deleteChat error answer errorCode " + errorCode + " error message = " + message);
                }
                else{
                    System.out.println("async return deleteChat ok!");
                }
            });
        }catch (RTMException ex){
            System.out.println(ex.toString());
        }
        catch (Exception ex){
            System.out.println("fun exception msg ");
            ex.printStackTrace();
        }
    }
}
