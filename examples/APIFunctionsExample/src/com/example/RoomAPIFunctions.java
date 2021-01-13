package com.example;

import com.fpnn.rtm.RTMClientConnectCallback;
import com.fpnn.rtm.RTMClientHasClosedCallback;
import com.fpnn.rtm.RTMClientWillCloseCallback;
import com.fpnn.rtm.RTMServerClient;
import com.fpnn.rtm.RTMException;
import com.fpnn.sdk.ErrorCode;
import com.fpnn.sdk.ErrorRecorder;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.lang.Thread.sleep;

public class RoomAPIFunctions {

    public static void main(String[] args) {

        // get your project params from RTM Console.
        RTMServerClient client = RTMServerClient.create(11000001, "ef3617e5-e886-4a4e-9eef-7263c0320628",
                "161.189.171.91:13315");
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

        long uid = 1111;
        long rid = 4444;
        Set<Long> uids = new HashSet<>();
        uids.add((long)5555);
        uids.add((long)6666);
        uids.add(uid);

        // sync api
        try{
            // addRoomMember
            client.addRoomMember(rid, uid);
            System.out.println("sync return addRoomMember  success");

            // addRoomBan
            client.addRoomBan(rid, uid, 300);
            System.out.println("sync return addRoomBan success");

            // isBanOfRoom
            boolean isBan = client.isBanOfRoom(rid, uid);
            System.out.println("sync return isBanOfRoom success ban : " + isBan);

            // removeRoomBan
            client.removeRoomBan(rid, uid);
            System.out.println("sync return removeRoomBan success");

            // setRoomInfo
            String roomPublicInfo = "room public info";
            String roomPrivateInfo = "room private info";
            client.setRoomInfo(rid, roomPublicInfo, roomPrivateInfo);
            System.out.println("sync return setRoomInfo success");

            // getRoomInfo
            StringBuffer oInfo = new StringBuffer();
            StringBuffer pInfo = new StringBuffer();
            client.getRoomInfo(rid, oInfo, pInfo);
            System.out.println("sync return getRoomInfo success, oInfo = " + oInfo + " ,pInfo = " + pInfo);

            // getRoomMembers
            Set<Long> members = client.getRoomMembers(rid);
            System.out.println("sync return getRoomMembers success, values: " + members);

            // getRoomUserCount
            Map<Long, Integer> count = client.getRoomUserCount(new HashSet<Long>(){{add(rid);}});
            System.out.println("sync return getRoomUserCount success, count: " + count);

            // delRoomMember
            client.delRoomMember(rid, uid);
            System.out.println("sync return delRoomMember success");
            System.out.println();

        }catch (RTMException ex){
            System.out.println(ex.toString());
        }
        catch (Exception ex){
            System.out.println("sync fun exception msg: ");
            ex.printStackTrace();
        }

        //async api
        try{
            // addRoomMember
            client.addRoomMember(rid, uid, (errorCode, message) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return addRoomMember error answer errorCode = " + errorCode + " ,errorMessage =  " + message);
                }
                else{
                    System.out.println("async return addRoomMember  success");
                }
            });

            // addRoomBan
            client.addRoomBan(rid, uid, 300, (errorCode, message) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return addRoomMember error answer errorCode = " + errorCode + " ,errorMessage =  " + message);
                }
                else{
                    System.out.println("async return addRoomBan success");
                }
            });

            // isBanOfRoom
            client.isBanOfRoom(rid, uid, (ok, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return isBanOfRoom error answer errorCode = " + errorCode + " ,errorMessage =  " + errorMessage);
                }
                else{
                    System.out.println("async return isBanOfRoom success ok : " + ok);
                }
            });


            // removeRoomBan
            client.removeRoomBan(rid, uid, (errorCode, message) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return removeRoomBan error answer errorCode = " + errorCode + " ,errorMessage =  " + message);
                }
                else{
                    System.out.println("async return removeRoomBan success");
                }
            });

            // setRoomInfo
            String roomPublicInfo = "room public info";
            String roomPrivateInfo = "room private info";
            client.setRoomInfo(rid, roomPublicInfo, roomPrivateInfo, (errorCode, message) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return setRoomInfo error answer errorCode = " + errorCode + " ,errorMessage =  " + message);
                }
                else{
                    System.out.println("async return setRoomInfo success");
                }
            });

            // getRoomInf
            client.getRoomInfo(rid, (openInfo, priInfo, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return getRoomInfo error answer errorCode = " + errorCode + " ,errorMessage =  " + errorMessage);
                }
                else{
                    System.out.println("async return getRoomInfo success, openInfo = " + openInfo + " ,priInfo = " + priInfo);
                }
            });

            client.getRoomMembers(rid, (members, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return getRoomMembers error answer errorCode = " + errorCode + " ,errorMessage =  " + errorMessage);
                }
                else{
                    System.out.println("async return getRoomMembers success, values: " + members);
                }
            });

            client.getRoomUserCount(new HashSet<Long>(){{add(rid);}}, (count, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return getRoomUserCount error answer errorCode = " + errorCode + " ,errorMessage =  " + errorMessage);
                }
                else{
                    System.out.println("async return getRoomUserCount success, count: " + count);
                }
            });

            // delRoomMember
            client.delRoomMember(rid, uid, (errorCode, message) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return delRoomMember error answer errorCode = " + errorCode + " ,errorMessage =  " + message);
                }
                else{
                    System.out.println("async return delRoomMember success");
                }
            });

            sleep(5000);  //-- Waiting for the async callback printed.

        }catch (Exception ex){
            System.out.println("async fun exception msg: ");
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
        ErrorRecorder recorder = (ErrorRecorder)ErrorRecorder.getInstance();
        recorder.println();

        //-- Optional: Only when client.setAutoCleanup(false); must call this function for cleaning up;
        client.SDKCleanup();

    }
}
