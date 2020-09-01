package com.example;

import com.fpnn.rtm.RTMClientConnectCallback;
import com.fpnn.rtm.RTMClientHasClosedCallback;
import com.fpnn.rtm.RTMClientWillCloseCallback;
import com.fpnn.rtm.RTMServerClient;
import com.fpnn.rtm.api.ServerPushMonitorAPI;
import com.fpnn.sdk.ErrorCode;
import com.fpnn.sdk.ErrorRecorder;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Thread.sleep;

public class ListenAPIFunctions {

    private static final Set<Long> gids = new HashSet<Long>(){{add((long)3333);}};
    private static final Set<Long> rids = new HashSet<Long>(){{add((long)4444);}};
    private static final Set<Long> uids = new HashSet<Long>(){{add((long)2222); add((long)5555); add((long)6666);}};

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

        ServerPushMonitorAPI pushMonitorAPI = new ServerPushMonitorAPI() {
        };
        client.setServerPushMonitor(pushMonitorAPI);

        try{

            addListen(client);
            System.out.println("add listen then wait 20s for client send messages");
            sleep(20000);

            removeListen(client);
            sleep(1000);

            setListen(client);
            System.out.println("set listen then wait 20s for client send messages");
            sleep(20000);

            setListenStatus(client);
            System.out.println("set listen status then wait 20s for client send messages");
            sleep(20000);

            sleep(5000);  //-- Waiting for the async callback printed

        }
        catch (Exception ex ){
            System.out.println("fun exception msg: ");
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

    private static void addListen(RTMServerClient client){
        try{
            // sync
            client.addListen(gids, rids, uids, Stream.of("login").collect(Collectors.toSet()));
            System.out.println("sync return addListen success.");

            // async
            client.addListen(gids, rids, uids, Stream.of("logout").collect(Collectors.toSet()), (errorCode, message) -> {
                    if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                        System.out.println("async return addListen error answer errorCode = " + errorCode + " ,errorMessage =  " + message);
                    }
                    else{
                        System.out.println("async return addListen success");
                    }
                });
        }
        catch (Exception ex ){
            System.out.println("fun exception msg: ");
            ex.printStackTrace();
        }
    }

    private static void removeListen(RTMServerClient client){
        try{
            // sync
            client.removeListen(gids, rids, uids, Stream.of("login").collect(Collectors.toSet()));
            System.out.println("sync return removeListen success.");

            // async
            client.removeListen(gids, rids, uids, Stream.of("logout").collect(Collectors.toSet()), (errorCode, message) -> {
                    if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                        System.out.println("async return removeListen error answer errorCode = " + errorCode + " ,errorMessage =  " + message);
                    }
                    else{
                        System.out.println("async return removeListen success");
                    }
                });
        }
        catch (Exception ex ){
            System.out.println("fun exception msg: ");
            ex.printStackTrace();
        }
    }

    private static void setListen(RTMServerClient client){
        try{
            // sync
            client.setListen(gids, rids, uids, Stream.of("login").collect(Collectors.toSet()));
            System.out.println("sync return setListen success.");

            // async
            client.setListen(gids, rids, uids, Stream.of("logout").collect(Collectors.toSet()), (errorCode, message) -> {
                    if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                        System.out.println("async return setListen error answer errorCode = " + errorCode + " ,errorMessage =  " + message);
                    }
                    else{
                        System.out.println("async return setListen success");
                    }
                });

        }
        catch (Exception ex ){
            System.out.println("fun exception msg: ");
            ex.printStackTrace();
        }
    }

    private static void setListenStatus(RTMServerClient client){
        try{
            // sync
            client.setListen(true, true, true, false);
            System.out.println("sync return setListen status success.");

            // async
            client.setListen(true, true, true, false, (errorCode, message) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return setListen status error answer errorCode = " + errorCode + " ,errorMessage =  " + message);
                }
                else{
                    System.out.println("async return setListen status success");
                }
            });

        }
        catch (Exception ex ){
            System.out.println("fun exception msg: ");
            ex.printStackTrace();
        }
    }
}
