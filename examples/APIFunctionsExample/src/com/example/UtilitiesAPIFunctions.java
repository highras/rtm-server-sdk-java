package com.example;

import com.fpnn.rtm.RTMClientConnectCallback;
import com.fpnn.rtm.RTMClientHasClosedCallback;
import com.fpnn.rtm.RTMClientWillCloseCallback;
import com.fpnn.rtm.RTMServerClient;
import com.fpnn.rtm.RTMException;
import com.fpnn.rtm.RTMServerClientBase.RTMTranslateMessage;
import com.fpnn.rtm.api.UtilitiesAPI;
import com.fpnn.sdk.ErrorCode;
import com.fpnn.sdk.ErrorRecorder;

import java.util.HashSet;
import java.util.Set;

import static java.lang.Thread.sleep;

public class UtilitiesAPIFunctions {

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

        // 以下所有接口为增值服务接口，需要在项目管理系统的控制台启用相应系统，若没有配置会返回error，此处只是展示接口调用示例
        // sync api
        try{

            // translate
            String sourceText = "hello word!";
            RTMTranslateMessage result = client.translate(sourceText, "zh-CN");
            System.out.println("sync return translate success, result = "+ result.toString());

            result = client.translate(sourceText, "", "zh-CN", UtilitiesAPI.TranslateType.TRANSLATE_TYPE_CHAT, UtilitiesAPI.ProfanityType.PROFANITY_TYPE_OFF, 0);
            System.out.println("sync return translate success, result = "+ result.toString());

            // profanity
            String profanityDemo = "demo profanity text";
            StringBuffer text = new StringBuffer();
            Set<String> classification = new HashSet<>();
            client.profanity(profanityDemo, text, classification);
            System.out.println("sync return profanity success. source = " + profanityDemo + " ,result = " + text + " ,classification types = " + classification);
            text.setLength(0);
            classification.clear();
            client.profanity(profanityDemo, true, 0, text, classification);
            System.out.println("sync return profanity success. source = " + profanityDemo + " ,result = " + text + " ,classification types = " + classification);

            // transcribe
            String audio = "test aaaaa"; //此处只是展示使用接口，可能会返回失败，一般都是客户端转发过来的语音
            StringBuffer textAudio = new StringBuffer();
            StringBuffer texLang = new StringBuffer();
            client.transcribe(audio.getBytes("UTF-8"), textAudio, texLang);
            System.out.println("sync return transcribe success. audio = " + audio + " ,text audio = " + textAudio + " ,textlang = " + texLang);

            client.transcribe(audio.getBytes("UTF-8"), 0, true, textAudio, texLang);
            System.out.println("sync return transcribe success. audio = " + audio + " ,text audio = " + textAudio + " ,textlang = " + texLang);
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
            // translate
            String sourceText = "hello word!";
            client.translate(sourceText, "zh-CN", (result, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return translate error answer errorCode = " + errorCode + " ,errorMessage = " + errorMessage);
                }
                else{
                    System.out.println("async return translate success, result = "+ result.toString());
                }
            });

            client.translate(sourceText, "", "zh-CN", UtilitiesAPI.TranslateType.TRANSLATE_TYPE_CHAT, UtilitiesAPI.ProfanityType.PROFANITY_TYPE_OFF, 0, (result, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return translate error answer errorCode = " + errorCode + " ,errorMessage = " + errorMessage);
                }
                else{
                    System.out.println("async return translate success, result = "+ result.toString());
                }
            });

            // profanity
            String profanityDemo = "demo profanity text";
            client.profanity(profanityDemo, (text, classification, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return profanity error answer errorCode = " + errorCode + " ,errorMessage = " + errorMessage);
                }
                else{
                    System.out.println("async return profanity success. source = " + profanityDemo + " ,result = " + text + " ,classification types = " + classification);
                }
            });
            client.profanity(profanityDemo, true, 0, (text, classification, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return profanity error answer errorCode = " + errorCode + " ,errorMessage = " + errorMessage);
                }
                else{
                    System.out.println("async return profanity success. source = " + profanityDemo + " ,result = " + text + " ,classification types = " + classification);
                }
            });

            // transcribe
            String audio = "test aaaaa"; //此处只是展示使用接口，可能会返回失败，一般都是客户端转发过来的语音
            client.transcribe(audio.getBytes("UTF-8"), (text, lang, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return transcribe error answer errorCode = " + errorCode + " ,errorMessage = " + errorMessage);
                }
                else{
                    System.out.println("async return transcribe success. audio = " + audio + " ,text audio = " + text + " ,textlang = " + lang);
                }
            });

            client.transcribe(audio.getBytes("UTF-8"), 0, true, (text, lang, errorCode, errorMessage) -> {
                if(errorCode != ErrorCode.FPNN_EC_OK.value()){
                    System.out.println("async return transcribe error answer errorCode = " + errorCode + " ,errorMessage = " + errorMessage);
                }
                else{
                    System.out.println("async return transcribe success. audio = " + audio + " ,text audio = " + text + " ,textlang = " + lang);
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
