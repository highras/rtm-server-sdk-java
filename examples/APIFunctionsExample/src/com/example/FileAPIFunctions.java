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

public class FileAPIFunctions {
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

        //send message
        long adminUid = 111;
        long from = 1111;
        long to = 2222;
        long gid = 3333;
        long rid = 4444;
        Set<Long> uids = new HashSet<>();
        uids.add((long)5555);
        uids.add((long)6666);

        String cp = "/com/example/testFile.txt";
        String filePath = FileAPIFunctions.class.getResource(cp).getFile();

        // sync api
        try{
            // sendFile
            long time = client.sendFile(from, to, filePath, "", null);
            System.out.println("sync return sendFile " + time);
            sleep(1000);

            // sendGroupFile
            time= client.sendGroupFile(from, gid, filePath, "", null);
            System.out.println("sync return sendGroupFile mtime " + time);
            sleep(1000);

            // sendRoomFile
            time = client.sendRoomFile(from, rid, filePath, "", null);
            System.out.println("sync return sendRoomFile mtime " + time);
            sleep(1000);

            // sendFiles
            time= client.sendFiles(from, uids, filePath, "", null);
            System.out.println("sync return sendFiles mtime " + time);
            sleep(1000);

            //sendBroadcastFile
            time= client.sendBroadcastFile(adminUid, filePath, "", null);
            System.out.println("sync return sendBroadcastFile mtime " + time);
            sleep(1000);
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
            // sendFile
            client.sendFile(from, to, filePath,"", null,(time, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return sendFile error answer errorCode " + errorCode + " error Message =  " + errorMessage);
                }
                else{
                    System.out.println("async return sendFile " + time);
                }
            });
            sleep(1000);

            // sendGroupFile
            client.sendGroupFile(from, gid, filePath, "", null, (time, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return sendGroupFile error answer errorCode " + errorCode + " error Message =  " + errorMessage);
                }
                else{
                    System.out.println("async return sendGroupFile " + time);
                }
            });
            sleep(1000);

            // sendRoomFile
            client.sendRoomFile(from, rid, filePath, "", null, (time, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return sendRoomFile error answer errorCode " + errorCode + " error Message =  " + errorMessage);
                }
                else{
                    System.out.println("async return sendRoomFile " + time);
                }
            });
            sleep(1000);

            // sendFiles
            client.sendFiles(from, uids, filePath, "", null, (time, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return sendFiles error answer errorCode " + errorCode + " error Message =  " + errorMessage);
                }
                else{
                    System.out.println("async return sendFiles " + time);
                }
            });
            sleep(1000);

            //sendBroadcastFile
            client.sendBroadcastFile(adminUid, filePath, "", null, (time, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return sendBroadcastFile error answer errorCode " + errorCode + " error Message =  " + errorMessage);
                }
                else{
                    System.out.println("async return sendBroadcastFile " + time);
                }
            });

            sleep(5000); //-- Waiting for the async callback printed.

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
