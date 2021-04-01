package com.example;

import com.fpnn.rtm.*;
import com.fpnn.sdk.ErrorCode;
import com.fpnn.sdk.ErrorRecorder;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Thread.sleep;

public class RealTimeVoiceAPIFunctions {

    public static void main(String[] args) {

        // get your project params from RTM Console.
        RTMServerClient client = RTMServerClient.create(80000087, "94c04aa3-9a12-42aa-864d-1e8928a86ded",
                "rtm-intl-backgate.ilivedata.com:13315");
        //-- Optional
        client.setAutoCleanup(false);

        //-- Optional when need process connect event or willClose event or closed event, maybe set these callback
        RTMClientConnectCallback connectCallback = (peerAddress, connected, reConnect, regressiveState) -> {
            if(connected)
            {
                System.out.println("rtm client connected " + peerAddress.toString());
            }
            else if(regressiveState != null){
                String info = "RTMReconnect time at " + regressiveState.connectStartMilliseconds + " ,currentFailedCount = " + regressiveState.currentFailedCount;
                System.out.println("rtm client not connected " + peerAddress.toString() + " ,can reconnet: " + reConnect + " ,reconnect infos: " +info);
            }
        };

        RTMClientWillCloseCallback willCloseCallback = (peerAddress, causedByError) -> System.out.println("rtm client will close " + "cause by error: " + causedByError);

        RTMClientHasClosedCallback hasClosedCallback = (peerAddress, causedByError, reConnect, regressiveState) ->{
            if(regressiveState != null){
                String info = "RTMReconnect time at " + regressiveState.connectStartMilliseconds + " ,currentFailedCount = " + regressiveState.currentFailedCount;
                System.out.println("rtm client has closed " + "cause by error: " + causedByError + " ,can reconnect: " + reConnect + " ,reconnect infos: " + info);
            }
        };

        client.setRTMClientConnectedCallback(connectCallback);
        client.setRTMClientWillCloseCallback(willCloseCallback);
        client.setRTMClientHasClosedCallback(hasClosedCallback);

        long uid = 4444;
        long rid = 8888;
        Set<Long> toUids = new HashSet<>();
        toUids.add((long)5555);
        toUids.add((long)6666);
        long fromUid = 7777;

        // sync api
        try{

            // pullIntoVoiceRoom
            client.pullIntoVoiceRoom(rid, toUids);
            System.out.println("sync return pullIntoVoiceRoom success");

            // inviteUserIntoVoiceRoom;
            client.inviteUserIntoVoiceRoom(rid, toUids, fromUid);
            System.out.println("sync return inviteUserIntoVoiceRoom success");

            sleep(500);

            // getVoiceRoomList
            Set<Long> rids = client.getVoiceRoomList();
            System.out.println("sync return getVoiceRoomList success, rids: " + rids);

            // getVoiceRoomMembers
            Set<Long> uids = new HashSet<>();
            Set<Long> managers = new HashSet<>();
            client.getVoiceRoomMembers(rid, uids, managers);
            System.out.println("sync return getVoiceRoomMembers success, uids: " + uids+ ", managers: " + managers);

            // getVoiceRoomMemberCount
            int count = client.getVoiceRoomMemberCount(rid);
            System.out.println("sync return getVoiceRoomMemberCount success count: " + count);

            // setVoiceRoomMicStatus
            client.setVoiceRoomMicStatus(rid, true);
            System.out.println("sync return setVoiceRoomMicStatus success");

            // kickoutFromVoiceRoom
            client.kickoutFromVoiceRoom(rid, uid, fromUid);
            System.out.println("sync return kickoutFromVoiceRoom success");

            // closeVoiceRoom
            client.closeVoiceRoom(rid);
            System.out.println("sync return closeVoiceRoom success");

            System.out.println();
            sleep(500);

        }catch (RTMException ex){
            System.out.println(ex.toString());
        }
        catch (Exception ex){
            System.out.println("sync fun exception msg ");
            ex.printStackTrace();
        }

        //async api
        try{
            // pullIntoVoiceRoom
            client.pullIntoVoiceRoom(rid, toUids, (errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return pullIntoVoiceRoom error answer errorCode = " + errorCode + " ,errorMessage = " + errorMessage);
                }
                else{
                    System.out.println("async return pullIntoVoiceRoom success");
                }
            });
            sleep(500);

            // inviteUserIntoVoiceRoom
            client.inviteUserIntoVoiceRoom(rid, toUids, fromUid, (errorCode, message) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return inviteUserIntoVoiceRoom error answer errorCode = " + errorCode + " ,errorMessage = " + message);
                }
                else{
                    System.out.println("async return inviteUserIntoVoiceRoom success");
                }
            });
            sleep(500);

            // getVoiceRoomList
            client.getVoiceRoomList((rids, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return getVoiceRoomList error answer errorCode = " + errorCode + " ,errorMessage = " + errorMessage);
                }
                else{
                    System.out.println("async return getVoiceRoomList success, rids: " + rids);
                }
            });
            sleep(500);

            // getVoiceRoomMembers
            client.getVoiceRoomMembers(rid, (uids, managers, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return getVoiceRoomMembers error answer errorCode = " + errorCode + " ,errorMessage = " + errorMessage);
                }
                else{
                    System.out.println("async return getVoiceRoomMembers success uids: " + uids + ", managers: " + managers);
                }
            });
            sleep(500);

            // getVoiceRoomMemberCount
            client.getVoiceRoomMemberCount(rid, (count, errorCode, message) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return getVoiceRoomMemberCount error answer errorCode = " + errorCode + " ,errorMessage = " + message);
                }
                else{
                    System.out.println("async return getVoiceRoomMemberCount success, count: " + count);
                }
            });
            sleep(500);

            // setVoiceRoomMicStatus
            client.setVoiceRoomMicStatus(rid, true, (errorCode, message) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return setVoiceRoomMicStatus error answer errorCode = " + errorCode + " ,errorMessage = " + message);
                }
                else{
                    System.out.println("async return setVoiceRoomMicStatus success");
                }
            });
            sleep(500);

            // kickoutFromVoiceRoom
            client.kickoutFromVoiceRoom(rid, uid, fromUid, (errorCode, message) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return kickoutFromVoiceRoom error answer errorCode = " + errorCode + " ,errorMessage = " + message);
                }
                else{
                    System.out.println("async return kickoutFromVoiceRoom success");
                }
            });
            sleep(500);

            // closeVoiceRoom
            client.closeVoiceRoom(rid, (errorCode, message) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return closeVoiceRoom error answer errorCode = " + errorCode + " ,errorMessage = " + message);
                }
                else{
                    System.out.println("async return closeVoiceRoom success");
                }
            });

            sleep(2000);  //-- Waiting for the async callback printed.

        }catch (Exception ex){
            System.out.println("async fun exception msg ");
            ex.printStackTrace();
        }

        client.close();
        //-- Wait for close event is processed.
        try{
            sleep(1000);
        }catch (Exception ex){
            System.out.println("fun exception msg: ");
            ex.printStackTrace();
        }

        //-- Optional
        ErrorRecorder recorder = ErrorRecorder.getInstance();
        recorder.println();
        recorder.clear();

        //-- Optional: Only when client.setAutoCleanup(false); must call this function for cleaning up;
        client.SDKCleanup();

    }
}
