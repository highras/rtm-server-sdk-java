package com.example;

import com.fpnn.rtm.RTMClientConnectCallback;
import com.fpnn.rtm.RTMClientHasClosedCallback;
import com.fpnn.rtm.RTMClientWillCloseCallback;
import com.fpnn.rtm.RTMServerClient;
import com.fpnn.rtm.RTMException;
import com.fpnn.sdk.ErrorCode;
import com.fpnn.sdk.ErrorRecorder;

import static java.lang.Thread.sleep;

public class DataAPIFunctions {

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

        String key1 = "demo key1";
        String key2 = "demo key2";
        String value = "123456";
        long uid = 1234;

        // sync api
        try{
            // dataSet
            client.dataSet(uid, key1, value);
            System.out.println("sync return dataSet success key = " + key1);
            client.dataSet(uid, key2, value);
            System.out.println("sync return dataSet success key = " + key2);

            // dataGet
            String val = client.dataGet(uid, key1);
            System.out.println("sync return dataGet key = " + key1 + " ,value = " + val);
            val = client.dataGet(uid, key2);
            System.out.println("sync return dataGet key = " + key2 + " ,value = " + val);

            // dataDel
            client.dataDel(uid, key1);
            System.out.println("sync return dataDel key = " + key1);
            client.dataGet(uid, key2);
            System.out.println("sync return dataDel key = " + key2);

            // dataGet
            val = client.dataGet(uid, key1);
            System.out.println("sync return dataGet key = " + key1 + " ,value = " + val);
            val = client.dataGet(uid, key2);
            System.out.println("sync return dataGet key = " + key2 + " ,value = " + val);
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
            // dataSet
            client.dataSet(uid, key1, value, (errorCode, message) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return dataSet error answer errorCode = " + errorCode + " ,errorMessage = " + message);
                }
                else{
                    System.out.println("async return dataSet success. key = " + key1);
                }
            });

            client.dataSet(uid, key2, value, (errorCode, message) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return dataSet error answer errorCode = " + errorCode + " ,errorMessage = " + message);
                }
                else{
                    System.out.println("async return dataSet success. key = " + key2);
                }
            });

            // dataGet
            dataGet(client, uid, key1);
            dataGet(client, uid, key2);

            // dataDel
            client.dataDel(uid, key1, (errorCode, message) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return dataDel error answer errorCode = " + errorCode + " c " + message);
                }
                else{
                    System.out.println("async return dataDel success. key = " + key1);
                }
            });
            client.dataDel(uid, key2, (errorCode, message) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return dataDel error answer errorCode = " + errorCode + " ,errorMessage = " + message);
                }
                else{
                    System.out.println("async return dataDel success. key = " + key2);
                }
            });

            // dataGet
            dataGet(client, uid, key1);
            dataGet(client, uid, key2);
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

    private static void dataGet(RTMServerClient client, long uid, String key){

        client.dataGet(uid, key, (value, errorCode, errorMessage) -> {
            if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                System.out.println("async return dataGet error answer errorCode = " + errorCode + " ,errorMessage = " + errorMessage);
            }
            else{
                System.out.println("async return dataGet success. key = " + key + " ,value = " + value);
            }
        });

    }
}
