package com.example;

import com.fpnn.rtm.RTMClientConnectCallback;
import com.fpnn.rtm.RTMClientHasClosedCallback;
import com.fpnn.rtm.RTMClientWillCloseCallback;
import com.fpnn.rtm.RTMServerClient;
import com.fpnn.rtm.RTMException;
import com.fpnn.sdk.ErrorCode;
import com.fpnn.sdk.ErrorRecorder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.lang.Thread.sleep;

public class MiscAPIFunctions {
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

        long uid = 2222;
        long fromUid = 3333;
        long groupId = 4444;

        Set<Integer> mtypes = new HashSet<>();
        mtypes.add(102);
        mtypes.add(103);


        // sync api
        try{
            // getToken
            String token = client.getToken(uid);
            System.out.println("sync return getToken success, uid = "+ uid + " ,token = " + token);

            // removeToken
            client.removeToken(uid);
            System.out.println("sync return removeToken success.");

            // addDevice
            String appType = "fcm";  //apptype fcm or apns
            String deviceToken = "demo aaabbb";  //device id
            client.addDevice(uid, appType, deviceToken);
            System.out.println("sync return addDevice success.");

            // removeDevice
            client.removeDevice(uid, deviceToken);
            System.out.println("sync return removeDevice success. uid = " + uid + " ,device id = " + deviceToken);
            System.out.println();

            client.addDevicePushOption(uid, 0, fromUid, mtypes );
            client.addDevicePushOption(uid, 1, groupId, mtypes);
            System.out.println("sync return addDevicePushOption success.");
            Map<Long, Set<Integer>> p2pOption = new HashMap<>();
            Map<Long, Set<Integer>> groupOption = new HashMap<>();
            client.getDevicePushOption(uid, p2pOption, groupOption);
            System.out.println("sync return getDevicePushOption success. p2p: " + p2pOption + " , group: " + groupOption);
            client.getDevicePushOption(uid, ((p2pOption1, groupOption1, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return getDevicePushOption error answer errorCode = " + errorCode + " ,errorMessage =  " + errorMessage);
                }
                else{
                    System.out.println("async return getDevicePushOption success. p2p = " + p2pOption1 + " ,group = " + groupOption1);
                }
            }));
            sleep(1000);
            client.removeDevicePushOption(uid, 0, fromUid, mtypes );
            client.removeDevicePushOption(uid, 1, groupId, mtypes);
            System.out.println("sync return removeDevicePushOption success.");
            Map<Long, Set<Integer>> p2pOption1 = new HashMap<>();
            Map<Long, Set<Integer>> groupOption1 = new HashMap<>();
            client.getDevicePushOption(uid, p2pOption1, groupOption1);
            System.out.println("sync return getDevicePushOption success. p2p: " + p2pOption1 + " , group: " + groupOption1);

        }catch (RTMException ex){
            System.out.println(ex.toString());
        }
        catch (Exception ex){
            System.out.println("sync fun exception msg ");
            ex.printStackTrace();
        }

        /*
        //async api
        try{
            // getToken
            client.getToken(uid, (token, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return getToken error answer errorCode = " + errorCode + " ,errorMessage =  " + errorMessage);
                }
                else{
                    System.out.println("async return getToken success. uid = " + uid + " ,token = " + token);
                }
            });

            // removeToken
            client.removeToken(uid, (errorCode, message) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return removeToken error answer errorCode = " + errorCode + " ,errorMessage =  " + message);
                }
                else{
                    System.out.println("async return removeToken success. uid = " + uid);
                }
            });

            // addDevice
            String appType = "fcm";  //apptype fcm or apns
            String deviceToken = "demo aaabbb";  //device id
            client.addDevice(uid, appType, deviceToken, (errorCode, message) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return addDevice error answer errorCode = " + errorCode + " ,errorMessage =  " + message);
                }
                else{
                    System.out.println("async return addDevice success. uid = " + uid);
                }
            });

            // removeDevice
            client.removeDevice(uid, deviceToken, (errorCode, message) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return removeDevice error answer errorCode = " + errorCode + " ,errorMessage =  " + message);
                }
                else{
                    System.out.println("async return removeDevice success. uid = " + uid + " ,device id = " + deviceToken);
                }
            });

            sleep(5000);  //-- Waiting for the async callback printed.

        }catch (Exception ex){
            System.out.println("async fun exception msg ");
            ex.printStackTrace();
        }
        */

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

        //-- Optional: Only when client.setAutoCleanup(false); must call this function for cleaning up;
        //client.SDKCleanup();

    }
}
