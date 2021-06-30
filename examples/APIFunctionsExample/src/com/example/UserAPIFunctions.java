package com.example;

import com.fpnn.rtm.RTMClientConnectCallback;
import com.fpnn.rtm.RTMClientHasClosedCallback;
import com.fpnn.rtm.RTMClientWillCloseCallback;
import com.fpnn.rtm.RTMServerClient;
import com.fpnn.rtm.RTMException;
import com.fpnn.sdk.ErrorCode;
import com.fpnn.sdk.ErrorRecorder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.lang.Thread.sleep;

public class UserAPIFunctions {

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

        long uid = 1111;
        Set<Long> uids = new HashSet<>();
        uids.add((long)5555);
        uids.add((long)6666);
        uids.add(uid);

        // sync api
        try{
            // kick out
            client.kickOut(uid);
            System.out.println("sync return kickOut success");

            // get online users
            Set<Long> ouids = client.getOnlineUsers(uids);
            System.out.println("sync return getOnlineUsers =" + ouids);

            // add project black
            client.addProjectBlack(uid, 300);
            System.out.println("sync return addProjectBlack success");

            // is project black
            boolean black = client.isProjectBlack(uid);
            System.out.println("sync return isProjectBlack success black: " + black);

            // remove project black
            client.removeProjectBlack(uid);
            System.out.println("sync return removeProjectBlack success");

            // set user info
            String userPublicInfo = "user public info";
            String userPrivateInfo = "user private info";

            client.setUserInfo(uid, userPublicInfo, userPrivateInfo);
            System.out.println("sync return setUserInfo success");

            // get user info
            StringBuffer oInfo = new StringBuffer();
            StringBuffer pInfo = new StringBuffer();
            client.getUserInfo(uid, oInfo, pInfo);
            System.out.println("sync return getUserInfo success, oInfo = " + oInfo + " ,pInfo = " + pInfo);

            // get user openinfo
            Map<String, String> data = client.getUserOpenInfo(uids);
            System.out.println("sync return getUserOpenInfo = " + Arrays.asList(data));
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
            // kick out
            client.kickOut(uid, (errorCode, message) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return kickOut error answer errorCode = " + errorCode + " ,errorMessage = " + message);
                }
                else{
                    System.out.println("async return kickOut success");
                }
            });

            // get online users
            client.getOnlineUsers(uids, (uids1, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return getOnlineUsers error answer errorCode = " + errorCode + " ,errorMessage =  " + errorMessage);
                }
                else{
                    System.out.println("async return getOnlineUsers = " + uids1);
                }
            });

            // add project black
            client.addProjectBlack(uid, 300, (errorCode, message) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return addProjectBlack error answer errorCode = " + errorCode + " ,errorMessage =  " + message);
                }
                else{
                    System.out.println("async return addProjectBlack success");
                }
            });

            // is project black
            client.isProjectBlack(uid, (ok, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return isProjectBlack error answer errorCode = " + errorCode + " ,errorMessage =  " + errorMessage);
                }
                else{
                    System.out.println("async return isProjectBlack success black: " + ok);
                }
            });

            // remove project black
            client.removeProjectBlack(uid, (errorCode, message) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return removeProjectBlack error answer errorCode = " + errorCode + " ,errorMessage =  " + message);
                }
                else{
                    System.out.println("async return removeProjectBlack success");
                }
            });

            // set user info
            String userPublicInfo = "user public info";
            String userPrivateInfo = "user private info";

            client.setUserInfo(uid, userPublicInfo, userPrivateInfo, (errorCode, message) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return setUserInfo error answer errorCode = " + errorCode + " ,errorMessage =  " + message);
                }
                else{
                    System.out.println("async return setUserInfo success");
                }
            });

            // get user info
            client.getUserInfo(uid, (openInfo, priInfo, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return getUserInfo error answer errorCode = " + errorCode + " ,errorMessage =  " + errorMessage);
                }
                else{
                    System.out.println("async return getUserInfo success, openInfo = " + openInfo + " ,priInfo = " + priInfo);
                }
            });

            // get user openinfo
            client.getUserOpenInfo(uids, (info, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return getUserOpenInfo error answer errorCode = " + errorCode + " ,errorMessage =  " + errorMessage);
                }
                else{
                    System.out.println("async return getUserOpenInfo = " + Arrays.asList(info));
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
        //client.SDKCleanup();

    }
}
