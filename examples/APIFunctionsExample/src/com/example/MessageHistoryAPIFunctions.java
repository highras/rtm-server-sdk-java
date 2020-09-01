package com.example;

import com.fpnn.rtm.RTMClientConnectCallback;
import com.fpnn.rtm.RTMClientHasClosedCallback;
import com.fpnn.rtm.RTMClientWillCloseCallback;
import com.fpnn.rtm.RTMServerClient;
import com.fpnn.rtm.RTMException;
import com.fpnn.rtm.RTMServerClientBase;
import com.fpnn.sdk.ErrorCode;
import com.fpnn.sdk.ErrorRecorder;

import java.util.HashSet;
import java.util.Set;

import static java.lang.Thread.sleep;

public class MessageHistoryAPIFunctions {
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

        long adminUid = 111;
        long from = 1111;
        long to = 2222;
        long gid = 3333;
        long rid = 4444;
        Set<Long> uids = new HashSet<>();
        uids.add((long)5555);
        uids.add((long)6666);

        // sync api
        try{
            // get history p2p message
            RTMServerClientBase.RTMHistoryMessage message = client.getP2PMsg(from, to, true, 10, 0, 0, 0, null);
            System.out.println("sync return getP2PMsg " + message.toString());

            // get history group message
            message = client.getGroupMsg(from, gid, true,10,0,0,0, null);
            System.out.println("sync return getGroupMsg " + message.toString());

            // get history room message
            message = client.getRoomMsg(from, rid, true,10,0,0,0, null);
            System.out.println("sync return getRoomMsg " + message.toString());

            // get history broadcast message
            message = client.getBroadCastMsg(adminUid,true,10,0,0,0, null);
            System.out.println("sync return getBroadCastMsg " + message.toString());
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
            // get p2p history message
            client.getP2PMsg(from, to, true, 10,0,0,0,null, (result, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return getP2PMsg error answer errorCode " + errorCode + " error Message =  " + errorMessage);
                }
                else{
                    System.out.println("async return getP2PMsg success answer " + result.toString());
                }
            });

            // get group history message
            client.getGroupMsg(from, gid, true,10,0,0,0, null, (result, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return getGroupMsg error answer errorCode " + errorCode + " error Message =  " + errorMessage);
                }
                else{
                    System.out.println("async return getGroupMsg success answer " + result.toString());
                }
            });

            // get room history message
            client.getRoomMsg(from, rid, true,10,0,0,0, null, (result, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return getRoomMsg error answer errorCode " + errorCode + " error Message =  " + errorMessage);
                }
                else{
                    System.out.println("async return getRoomMsg success answer " + result.toString());
                }
            });

            // get broadcast history message
            client.getBroadCastMsg(adminUid, true,10,0,0,0, null, (result, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return getBroadCastMsg error answer errorCode " + errorCode + " error Message =  " + errorMessage);
                }
                else{
                    System.out.println("async return getBroadCastMsg success answer " + result.toString());
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
