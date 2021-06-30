package com.example;

import com.fpnn.rtm.*;
import com.fpnn.sdk.ErrorCode;
import com.fpnn.sdk.ErrorRecorder;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Thread.sleep;

public class RTCAPIFunctions {

    public static void main(String[] args) {

        // get your project params from RTM Console.
        RTMServerClient client = RTMServerClient.create(11000001, "f8746860-7294-4fbf-aae5-e1ebe45f4f40",
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

        long uid = 4444;
        long rid = 8888;
        Set<Long> toUids = new HashSet<>();
        toUids.add((long)5555);
        toUids.add((long)6666);
        long fromUid = 7777;

        // sync api
        try{

            // pullIntoRTCRoom
            client.pullIntoRTCRoom(rid, toUids, 1);
            System.out.println("sync return pullIntoRTCRoom success");

            // inviteUserIntoRTCRoom;
            client.inviteUserIntoRTCRoom(rid, toUids, fromUid);
            System.out.println("sync return inviteUserIntoRTCRoom success");

            sleep(500);

            // getRTCRoomList
            Set<Long> rids = client.getRTCRoomList();
            System.out.println("sync return getRTCRoomList success, rids: " + rids);

            // getRTCRoomMembers
            Set<Long> uids = new HashSet<>();
            Set<Long> managers = new HashSet<>();
            long owner = client.getRTCRoomMembers(rid, uids, managers);
            System.out.println("sync return getRTCRoomMembers success, uids: " + uids+ ", managers: " + managers+ " ,owner: " + owner);

            // getRTCRoomMemberCount
            int count = client.getRTCRoomMemberCount(rid);
            System.out.println("sync return getRTCRoomMemberCount success count: " + count);

            // setRTCRoomMicStatus
            client.setRTCRoomMicStatus(rid, true);
            System.out.println("sync return setRTCRoomMicStatus success");

            // admincommand
            client.adminCommand(rid, toUids, 0);
            System.out.println("sync return adminCommand success");

            // kickoutFromRTCRoom
            client.kickoutFromRTCRoom(rid, uid, fromUid);
            System.out.println("sync return kickoutFromRTCRoom success");

            // closeRTCRoom
            client.closeRTCRoom(rid);
            System.out.println("sync return closeRTCRoom success");

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
            // pullIntoRTCRoom
            client.pullIntoRTCRoom(rid, toUids, 1,(errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return pullIntoRTCRoom error answer errorCode = " + errorCode + " ,errorMessage = " + errorMessage);
                }
                else{
                    System.out.println("async return pullIntoRTCRoom success");
                }
            });
            sleep(500);

            // inviteUserIntoRTCRoom
            client.inviteUserIntoRTCRoom(rid, toUids, fromUid, (errorCode, message) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return inviteUserIntoRTCRoom error answer errorCode = " + errorCode + " ,errorMessage = " + message);
                }
                else{
                    System.out.println("async return inviteUserIntoRTCRoom success");
                }
            });
            sleep(500);

            // getRTCRoomList
            client.getRTCRoomList((rids, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return getRTCRoomList error answer errorCode = " + errorCode + " ,errorMessage = " + errorMessage);
                }
                else{
                    System.out.println("async return getRTCRoomList success, rids: " + rids);
                }
            });
            sleep(500);

            // getRTCRoomMembers
            client.getRTCRoomMembers(rid, (uids, managers, owner, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return getRTCRoomMembers error answer errorCode = " + errorCode + " ,errorMessage = " + errorMessage);
                }
                else{
                    System.out.println("async return getRTCRoomMembers success uids: " + uids + ", managers: " + managers + " ,owner: " + owner);
                }
            });
            sleep(500);

            // getRTCRoomMemberCount
            client.getRTCRoomMemberCount(rid, (count, errorCode, message) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return getRTCRoomMemberCount error answer errorCode = " + errorCode + " ,errorMessage = " + message);
                }
                else{
                    System.out.println("async return getRTCRoomMemberCount success, count: " + count);
                }
            });
            sleep(500);

            // setRTCRoomMicStatus
            client.setRTCRoomMicStatus(rid, true, (errorCode, message) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return setRTCRoomMicStatus error answer errorCode = " + errorCode + " ,errorMessage = " + message);
                }
                else{
                    System.out.println("async return setRTCRoomMicStatus success");
                }
            });
            sleep(500);

            // adminCommand
            client.adminCommand(rid, toUids, 0, (errorCode, message) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return adminCommand error answer errorCode = " + errorCode + " ,errorMessage = " + message);
                }
                else{
                    System.out.println("async return adminCommand success");
                }
            });
            sleep(500);

            // kickoutFromRTCRoom
            client.kickoutFromRTCRoom(rid, uid, fromUid, (errorCode, message) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return kickoutFromRTCRoom error answer errorCode = " + errorCode + " ,errorMessage = " + message);
                }
                else{
                    System.out.println("async return kickoutFromRTCRoom success");
                }
            });
            sleep(500);

            // closeRTCRoom
            client.closeRTCRoom(rid, (errorCode, message) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return closeRTCRoom error answer errorCode = " + errorCode + " ,errorMessage = " + message);
                }
                else{
                    System.out.println("async return closeRTCRoom success");
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
        //client.SDKCleanup();

    }
}
