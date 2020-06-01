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

        ServerPushMonitorAPI pushMonitorAPI = new ServerPushMonitorAPI() {
            @Override
            public void pushP2PMessage(long fromUid, long toUid, byte mtype, long mid, String msg, String attr, long mtime) {
                System.out.println("receive push p2p message from "+ fromUid + " ,to " + toUid + " ,mtype " + mtype + " ,msg " + msg);
            }

            @Override
            public void pushGroupMessage(long fromUid, long gId, byte mtype, long mid, String msg, String attr, long mtime) {
                System.out.println("receive push group message from "+ fromUid + " ,gid " + gId + " ,mtype " + mtype + " ,msg " + msg);
            }

            @Override
            public void pushRoomMessage(long fromUid, long rId, byte mtype, long mid, String msg, String attr, long mtime) {
                System.out.println("receive push room message from "+ fromUid + " ,rid " + rId + " ,mtype " + mtype + " ,msg " + msg);
            }

            @Override
            public void pushEvent(int pid, String event, long uid, int time, String endpoint, String data) {
                System.out.println("receive push event pid "+ pid + " ,event " + event + " ,uid " + uid + " ,endpoint " + endpoint + " ,time" + time);
            }

            @Override
            public void pushP2PChat(long fromUid, long toUid, long mid, String msg, String attr, long mtime) {
                System.out.println("receive push p2p chat from "+ fromUid + " ,to " + toUid + " ,msg " + msg);
            }

            @Override
            public void pushGroupChat(long fromUid, long gId, long mid, String msg, String attr, long mtime) {
                System.out.println("receive push group chat from "+ fromUid + " ,gid " + gId + " ,msg " + msg);
            }

            @Override
            public void pushRoomChat(long fromUid, long rId, long mid, String msg, String attr, long mtime) {
                System.out.println("receive push room chat from "+ fromUid + " ,rid " + rId + " ,msg " + msg);
            }

            @Override
            public void pushP2PAudio(long fromUid, long toUid, long mid, byte[] msg, String attr, long mtime) {
                System.out.println("receive push p2p audio from "+ fromUid + " ,to " + toUid + " ,msg " + msg);
            }

            @Override
            public void pushGroupAudio(long fromUid, long gId, long mid, byte[] msg, String attr, long mtime) {
                System.out.println("receive push group audio from "+ fromUid + " ,gid " + gId + " ,msg " + msg);
            }

            @Override
            public void pushRoomAudio(long fromUid, long rId, long mid, byte[] msg, String attr, long mtime) {
                System.out.println("receive push room audio from "+ fromUid + " ,rid " + rId + " ,msg " + msg);
            }

            @Override
            public void pushP2PCmd(long fromUid, long toUid, long mid, String msg, String attr, long mtime) {
                System.out.println("receive push p2p cmd from "+ fromUid + " ,to " + toUid + " ,msg " + msg);
            }

            @Override
            public void pushGroupCmd(long fromUid, long gId, long mid, String msg, String attr, long mtime) {
                System.out.println("receive push group cmd from "+ fromUid + " ,gid " + gId + " ,msg " + msg);
            }

            @Override
            public void pushRoomCmd(long fromUid, long rId, long mid, String msg, String attr, long mtime) {
                System.out.println("receive push room cmd from "+ fromUid + " ,rid " + rId + " ,msg " + msg);
            }

            @Override
            public void pushP2PFile(long fromUid, long toUid, byte mtype, long mid, String msg, String attr, long mtime) {
                System.out.println("receive push p2p file from "+ fromUid + " ,to " + toUid + " ,mtype " + mtype + " ,msg " + msg);
            }

            @Override
            public void pushGroupFile(long fromUid, long gId, byte mtype, long mid, String msg, String attr, long mtime) {
                System.out.println("receive push group file from "+ fromUid + " ,gid " + gId + " ,mtype " + mtype + " ,msg " + msg);
            }

            @Override
            public void pushRoomFile(long fromUid, long rId, byte mtype, long mid, String msg, String attr, long mtime) {
                System.out.println("receive push room file from "+ fromUid + " ,rid " + rId + " ,mtype " + mtype + " ,msg " + msg);
            }
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
