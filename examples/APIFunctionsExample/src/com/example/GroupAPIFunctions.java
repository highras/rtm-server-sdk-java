package com.example;

import com.fpnn.rtm.RTMClientConnectCallback;
import com.fpnn.rtm.RTMClientHasClosedCallback;
import com.fpnn.rtm.RTMClientWillCloseCallback;
import com.fpnn.rtm.RTMServerClient;
import com.fpnn.rtm.RTMException;
import com.fpnn.sdk.ErrorCode;
import com.fpnn.sdk.ErrorRecorder;

import java.util.HashSet;
import java.util.Set;

import static java.lang.Thread.sleep;

public class GroupAPIFunctions {

    public static void main(String[] args) {

        // get your project params from RTM Console.
        RTMServerClient client = RTMServerClient.create(11000001, "ef3617e5-e886-4a4e-9eef-7263c0320628",
                "161.189.171.91:13315");
        //-- Optional
        client.setAutoCleanup(false);

        //-- Optional when need process connect event or willClose event or closed event, maybe set these callback
        RTMClientConnectCallback connectCallback = (peerAddress, connected, reConnect, reConnectInfo) -> {
            if(connected)
            {
                System.out.println("rtm client connected " + peerAddress.toString());
            }
            else{
                System.out.println("rtm client not connected " + peerAddress.toString() + " ,can reconnet: " + reConnect + " ,reconnect infos: " +reConnectInfo);
            }
        };

        RTMClientWillCloseCallback willCloseCallback = (peerAddress, causedByError) -> System.out.println("rtm client will close " + "cause by error: " + causedByError);

        RTMClientHasClosedCallback hasClosedCallback = (peerAddress, causedByError, reConnect, reConnectInfo) ->
                System.out.println("rtm client has closed " + "cause by error: " + causedByError + " ,can reconnect: " + reConnect + " ,reconnect infos: " + reConnectInfo);

        client.setRTMClientConnectedCallback(connectCallback);
        client.setRTMClientWillCloseCallback(willCloseCallback);
        client.setRTMClientHasClosedCallback(hasClosedCallback);

        long uid = 1111;
        long gid = 3333;
        Set<Long> uids = new HashSet<>();
        uids.add((long)5555);
        uids.add((long)6666);
        uids.add(uid);

        // sync api
        try{
            // addGroupMembers
            client.addGroupMembers(gid, uids);
            System.out.println("sync return addGroupMembers success");

            // getGroupMembers
            Set<Long> ouids = client.getGroupMembers(gid);
            System.out.println("sync return getGroupMembers = " + ouids);

            // isGroupMember
            boolean ok = client.isGroupMember(uid, gid);
            System.out.println("sync return isGroupMember success, ok " + ok);

            // getUserGroups
            Set<Long> gids = client.getUserGroups(uid);
            System.out.println("sync return getUserGroups success gids = " + gids);

            // addGroupBan
            client.addGroupBan(gid, uid, 300);
            System.out.println("sync return addGroupBan success");

            // isBanOfGroup
            boolean isBan = client.isBanOfGroup(gid, uid);
            System.out.println("sync return isBanOfGroup success ban : " + isBan);

            // removeGroupBan
            client.removeGroupBan(gid, uid);
            System.out.println("sync return removeGroupBan success");

            // setGroupInfo
            String groupPublicInfo = "group public info";
            String groupPrivateInfo = "group private info";
            client.setGroupInfo(gid, groupPublicInfo, groupPrivateInfo);
            System.out.println("sync return setGroupInfo success");

            // getGroupInfo
            StringBuffer oInfo = new StringBuffer();
            StringBuffer pInfo = new StringBuffer();
            client.getGroupInfo(gid, oInfo, pInfo);
            System.out.println("sync return getGroupInfo success, oInfo = " + oInfo + " ,pInfo = " + pInfo);

            // delGroupMembers
            client.delGroupMembers(gid, uids);
            System.out.println("sync return delGroupMembers success");

            // delGroup
            client.delGroup(gid);
            System.out.println("sync return delGroup success");
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
            // addGroupMembers
            client.addGroupMembers(gid, uids, (errorCode, message) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return addGroupMembers error answer errorCode = " + errorCode + " ,errorMessage =  " + message);
                }
                else{
                    System.out.println("async return addGroupMembers success.");
                }
            });

            // getGroupMembers
            client.getGroupMembers(gid, (uids1, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return getGroupMembers error answer errorCode = " + errorCode + " ,errorMessage =  " + errorMessage);
                }
                else{
                    System.out.println("async return getGroupMembers = " + uids1);
                }
            });

            // isGroupMember
            client.isGroupMember(uid, gid, (ok, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return isGroupMember error answer errorCode = " + errorCode + " ,errorMessage =  " + errorMessage);
                }
                else{
                    System.out.println("async return isGroupMember ok = " + ok);
                }
            });

            // getUserGroups
            client.getUserGroups(uid, (gids, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return getUserGroups error answer errorCode = " + errorCode + " ,errorMessage =  " + errorMessage);
                }
                else{
                    System.out.println("async return getUserGroups = " + gids);
                }
            });

            // addGroupBan
            client.addGroupBan(gid, uid, 300, (errorCode, message) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return addGroupBan error answer errorCode = " + errorCode + " ,errorMessage =  " + message);
                }
                else{
                    System.out.println("async return addGroupBan success.");
                }
            });

            // isBanOfGroup
            client.isBanOfGroup(gid, uid, (ok, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return isBanOfGroup error answer errorCode = " + errorCode + " ,errorMessage =  " + errorMessage);
                }
                else{
                    System.out.println("async return isBanOfGroup ok = " + ok);
                }
            });

            // removeGroupBan
            client.removeGroupBan(gid, uid, (errorCode, message) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return removeGroupBan error answer errorCode = " + errorCode + " ,errorMessage =  " + message);
                }
                else{
                    System.out.println("async return removeGroupBan success.");
                }
            });

            // setGroupInfo
            String groupPublicInfo = "group public info";
            String groupPrivateInfo = "group private info";
            client.setGroupInfo(gid, groupPublicInfo, groupPrivateInfo, (errorCode, message) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return setGroupInfo error answer errorCode = " + errorCode + " ,errorMessage =  " + message);
                }
                else{
                    System.out.println("async return setGroupInfo success.");
                }
            });

            // getGroupInfo
            client.getGroupInfo(gid, (openInfo, priInfo, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return setGroupInfo error answer errorCode = " + errorCode + " ,errorMessage =  " + errorMessage);
                }
                else{
                    System.out.println("async return getGroupInfo success, openInfo = " + openInfo + " ,priInfo = " + priInfo);
                }
            });

            // delGroupMembers
            client.delGroupMembers(gid, uids, (errorCode, message) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return delGroupMembers error answer errorCode = " + errorCode + " ,errorMessage =  " + message);
                }
                else{
                    System.out.println("async return delGroupMembers success.");
                }
            });

            // delGroup
            client.delGroup(gid, (errorCode, message) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return delGroup error answer errorCode = " + errorCode + " ,errorMessage =  " + message);
                }
                else{
                    System.out.println("async return delGroup success.");
                }
            });

            sleep(5000);  //-- Waiting for the async callback printed.

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
        ErrorRecorder recorder = (ErrorRecorder)ErrorRecorder.getInstance();
        recorder.println();

        //-- Optional: Only when client.setAutoCleanup(false); must call this function for cleaning up;
        client.SDKCleanup();

    }
}
