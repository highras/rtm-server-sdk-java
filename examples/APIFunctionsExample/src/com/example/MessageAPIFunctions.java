package com.example;

import com.fpnn.rtm.RTMClientConnectCallback;
import com.fpnn.rtm.RTMClientHasClosedCallback;
import com.fpnn.rtm.RTMClientWillCloseCallback;
import com.fpnn.rtm.RTMServerClient;
import com.fpnn.rtm.RTMException;
import com.fpnn.rtm.RTMServerClientBase;
import com.fpnn.rtm.api.APIBase.MessageType;
import com.fpnn.rtm.api.APIBase;
import com.fpnn.sdk.ErrorCode;
import com.fpnn.sdk.ErrorRecorder;

import java.util.HashSet;
import java.util.Set;
import static java.lang.Thread.sleep;

public class MessageAPIFunctions {

    public static void main(String[] args) {

        // get your project params from RTM Console.
        RTMServerClient client = RTMServerClient.create(11000001, "ef3617e5-e886-4a4e-9eef-7263c0320628",
                "161.189.171.91:13315");
        //-- Optional
        //client.setAutoCleanup(false);

        //-- Optional when need process connect event or willClose event or closed event, maybe set these callback
        RTMClientConnectCallback connectCallback = (peerAddress, connected, reConnect, regressiveState) -> {
            if(connected)
            {
                System.out.println("rtm client connected " + peerAddress.toString());
            }
            else if(regressiveState != null){
                String info = "RTM last connected time at " + regressiveState.connectSuccessMilliseconds + " ,currentFailedCount = " + regressiveState.currentFailedCount;
                System.out.println("rtm client not connected " + peerAddress.toString() + " ,can reconnet: " + reConnect + " ,reconnect infos: " +info);
            }
        };

        RTMClientWillCloseCallback willCloseCallback = (peerAddress, causedByError) -> System.out.println("rtm client will close " + "cause by error: " + causedByError);

        RTMClientHasClosedCallback hasClosedCallback = (peerAddress, causedByError, reConnect, regressiveState) ->{
            if(regressiveState != null){
                String info = "RTM last connected time at " + regressiveState.connectSuccessMilliseconds + " ,currentFailedCount = " + regressiveState.currentFailedCount;
                System.out.println("rtm client has closed " + "cause by error: " + causedByError + " ,can reconnect: " + reConnect + " ,reconnect infos: " + info);
            }
        };

        client.setRTMClientConnectedCallback(connectCallback);
        client.setRTMClientWillCloseCallback(willCloseCallback);
        client.setRTMClientHasClosedCallback(hasClosedCallback);


        long adminUid = 111;
        long from = 1111;
        long to = 2222;
        long gid = 3333;
        long rid = 4444;
        Set<Long> uids = new HashSet<>();
        uids.add((long)5555);
        uids.add((long)6666);
        byte mtype = 127;

        // sync api
        try{
            // send p2p message
            long time = client.sendMessage(from, to, mtype, "p2p hello word", "");
            System.out.println("sync return p2p mtime " + time);
            time = client.sendMessage(from, to, mtype, "p2p hello word".getBytes("UTF-8"), "");
            System.out.println("sync return p2p mtime " + time);

            // send group message
            time= client.sendGroupMessage(from, gid, mtype, "group hello word", "");
            System.out.println("sync return group mtime " + time);
            time = client.sendGroupMessage(from, gid, mtype, "group hello word".getBytes("UTF-8"), "");
            System.out.println("sync return group mtime " + time);

            // send room message
            time = client.sendRoomMessage(from, rid, mtype, "room hello word", "");
            System.out.println("sync return room  mtime " + time);
            time = client.sendRoomMessage(from, rid, mtype, "room hello word".getBytes("UTF-8"), "");
            System.out.println("sync return room mtime " + time);

            // send p2ps message
            time= client.sendMessages(from, uids, mtype, "p2ps hello word", "");
            System.out.println("sync return p2ps mtime " + time);
            time = client.sendMessages(from, uids, mtype, "p2ps hello word".getBytes("UTF-8"), "");
            System.out.println("sync return p2ps mtime " + time);

            //send broadcast message
            time= client.sendBroadcastMessage(adminUid, mtype, "broadcast hello word", "");
            System.out.println("sync return broadcast mtime " + time);
            time = client.sendBroadcastMessage(adminUid, mtype, "broadcast hello word".getBytes("UTF-8"), "");
            System.out.println("sync return broadcast mtime " + time);

            // get message
            long mid =123456789;
            RTMServerClientBase.RTMHistoryMessageUnit result = client.getMsg(mid, from, to, MessageType.MESSAGE_TYPE_P2P);
            System.out.println("sync return getMsg message: " + result.toString());

            // del message
            client.deleteMsg(mid, from, to, MessageType.MESSAGE_TYPE_P2P);
            System.out.println("sync return deleteMsg success.");
            System.out.println();

        }catch (RTMException ex){
            System.out.println(ex.toString());
        }
        catch (Exception ex){
            System.out.println("sync fun exception msg ");
            ex.printStackTrace();
        }

        //async api
        try{
            // send p2p message
            client.sendMessage(from, to, mtype, "p2p hello word", "", (time, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return sendMessage for p2p error answer errorCode " + errorCode + " error Message =  " + errorMessage);
                }
                else{
                    System.out.println("async return sendMessage for p2p success answer time = " + time);
                }
            });

            client.sendMessage(from, to, mtype, "p2p hello word".getBytes("UTF-8"), "", (time, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return sendMessage for p2p error answer errorCode " + errorCode + " error Message =  " + errorMessage);
                }
                else{
                    System.out.println("async return sendMessage for p2p success answer time = " + time);
                }
            });

            // send group message
            client.sendGroupMessage(from, gid, mtype, "group hello word", "", (time, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return sendMessage for group error answer errorCode " + errorCode + " error Message =  " + errorMessage);
                }
                else{
                    System.out.println("async return sendMessage for group success answer time = " + time);
                }
            });

            client.sendGroupMessage(from, gid, mtype, "group hello word".getBytes("UTF-8"), "", (time, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return sendMessage for group error answer errorCode " + errorCode + " error Message =  " + errorMessage);
                }
                else{
                    System.out.println("async return sendMessage for group success answer time = " + time);
                }
            });

            // send room message
            client.sendRoomMessage(from, rid, mtype, "room hello word", "", (time, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return sendMessage for room error answer errorCode " + errorCode + " error Message =  " + errorMessage);
                }
                else{
                    System.out.println("async return sendMessage for room success answer time = " + time);
                }
            });

            client.sendRoomMessage(from, rid, mtype, "room hello word".getBytes("UTF-8"), "", (time, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return sendMessage for room error answer errorCode " + errorCode + " error Message =  " + errorMessage);
                }
                else{
                    System.out.println("async return sendMessage for room success answer time = " + time);
                }
            });

            // send p2ps message
            client.sendMessages(from, uids, mtype, "p2ps hello word", "", (time, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return sendMessage for p2ps error answer errorCode " + errorCode + " error Message =  " + errorMessage);
                }
                else{
                    System.out.println("async return sendMessage for p2ps success answer time = " + time);
                }
            });

            client.sendMessages(from, uids, mtype, "p2ps hello word".getBytes("UTF-8"), "", (time, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return sendMessage for p2ps error answer errorCode " + errorCode + " error Message =  " + errorMessage);
                }
                else{
                    System.out.println("async return sendMessage for p2ps success answer time = " + time);
                }
            });

            //send broadcast message
            client.sendBroadcastMessage(adminUid, mtype, "broadcast hello word", "", (time, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return sendMessage for broadcast error answer errorCode " + errorCode + " error Message =  " + errorMessage);
                }
                else{
                    System.out.println("async return sendMessage for broadcast success answer time = " + time);
                }
            });

            client.sendBroadcastMessage(adminUid, mtype, "broadcast hello word".getBytes("UTF-8"), "", (time, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return sendMessage for broadcast error answer errorCode " + errorCode + " error Message =  " + errorMessage);
                }
                else{
                    System.out.println("async return sendMessage for broadcast success answer time = " + time);
                }
            });

            // get message
            long mid =123456789;
            client.getMsg(mid, from, to, APIBase.MessageType.MESSAGE_TYPE_P2P, (message, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return getMsg error answer errorCode " + errorCode + " error Message =  " + errorMessage);
                }
                else{
                    System.out.println("async return getMsg message mtype = " + message.message.messageType + " ,msg = " + message.message.stringMessage + " ,binary message " + message.message.binaryMessage
                            + " ,mtime = " + message.message.modifiedTime + " ,attrs = " + message.message.attrs);
                }
            });

            // del message
            client.deleteMsg(mid, from, to, APIBase.MessageType.MESSAGE_TYPE_P2P, (errorCode, message) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return deleteMsg error answer errorCode " + errorCode + " error Message =  " + message);
                }
                else{
                    System.out.println("async return deleteMsg ok!");
                }
            });
            sleep(5000); //-- Waiting for the async callback printed.

        }catch (Exception ex){
            System.out.println("async fun exception msg ");
            ex.printStackTrace();
        }


        //-- Wait for close event is processed.
        client.close();
        try{
            sleep(100000);
        }catch (Exception ex){
            System.out.println("fun exception msg: ");
            ex.printStackTrace();
        }

        //-- Optional
        ErrorRecorder recorder = (ErrorRecorder)ErrorRecorder.getInstance();
        recorder.println();

        //-- Optional: Only when client.setAutoCleanup(false); must call this function for cleaning up;
        //client.SDKCleanup();

    }
}
