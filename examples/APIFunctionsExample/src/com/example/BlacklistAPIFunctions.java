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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Thread.sleep;

public class BlacklistAPIFunctions {

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
        Set<Long> buids = new HashSet<>();
        buids.add((long)5555);
        buids.add((long)6666);
        long buid = 7777;
        long ouid = 9999;
        buids.add(buid);

        // sync api
        try{
            // addBlacks;
            client.addBlacks(uid, buids);
            System.out.println("sync return addBlacks success");

            // send msg test
            try{
                long time = client.sendChat(uid, buid, "p2p hello word", "");
                System.out.println("sync return p2p mtime " + time);
                time = client.sendChat(buid, uid, "p2p hello word", "");
                System.out.println("sync return p2p mtime " + time);

            }catch (RTMException ex){
                System.out.println(ex.toString());
            }

            // getBlacks
            Set<Long> ids = client.getBlacks(uid);
            System.out.println("sync return getBlacks  = " + ids);

            // isBlack
            boolean ok = client.isBlack(uid, buid);
            System.out.println("sync return isBlack success, ok " + ok + " " + uid + " ->" + buid);

            ok = client.isBlack(uid, ouid);
            System.out.println("sync return isBlack success, ok " + ok+ " " + uid + " ->" + ouid);

            // isBlacks
            ids = client.isBlacks(uid, Stream.of(buid, ouid).collect(Collectors.toSet()));
            System.out.println("sync return isBlacks success ids = " + ids);

            // delBlacks
            client.delBlacks(uid, buids);
            System.out.println("sync return delBlacks success");

            // getBlacks
            ids = client.getBlacks(uid);
            System.out.println("sync return getBlacks  = " + ids);
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
            // addBlacks
            client.addBlacks(uid, buids, (errorCode, message) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return addBlacks error answer errorCode = " + errorCode + " ,errorMessage = " + message);
                }
                else{
                    System.out.println("async return addBlacks success");
                }
            });

            sleep(1000);

            // send msg test
            try{
                long time = client.sendChat(uid, buid, "p2p hello word", "");
                System.out.println("sync return p2p mtime " + time);
                time = client.sendChat(buid, uid, "p2p hello word", "");
                System.out.println("sync return p2p mtime " + time);

            }catch (RTMException ex){
                System.out.println(ex.toString());
            }

            // getBlacks
            client.getBlacks(uid, (fuids1, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return getBlacks error answer errorCode = " + errorCode + " ,errorMessage = " + errorMessage);
                }
                else{
                    System.out.println("async return getBlacks = " + fuids1);
                }
            });

            // isBlack
            client.isBlack(uid, buid, (ok, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return isBlack error answer errorCode = " + errorCode + " ,errorMessage = " + errorMessage);
                }
                else{
                    System.out.println("async return isBlack ok " + ok);
                }
            });

            // isBlacks
            client.isBlacks(uid, Stream.of(buid, ouid).collect(Collectors.toSet()), (fuids12, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return isBlacks error answer errorCode = " + errorCode + " ,errorMessage = " + errorMessage);
                }
                else{
                    System.out.println("async return isBlacks success ids = " + fuids12);
                }
            });

            // delBlacks
            client.delBlacks(uid, buids, (errorCode, message) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return delBlacks error answer errorCode = " + errorCode + " ,errorMessage = " + message);
                }
                else{
                    System.out.println("async return delBlacks success");
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
        //client.SDKCleanup();

    }
}
