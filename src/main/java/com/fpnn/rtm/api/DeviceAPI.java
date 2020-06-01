package com.fpnn.rtm.api;

import com.fpnn.rtm.RTMException;
import com.fpnn.rtm.RTMServerClientBase;
import com.fpnn.sdk.AnswerCallback;
import com.fpnn.sdk.ErrorCode;
import com.fpnn.sdk.ErrorRecorder;
import com.fpnn.sdk.proto.Quest;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface DeviceAPI extends APIBase{

    default void addDevice(long uid, String appType, String deviceToken)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        addDevice(uid, appType, deviceToken, 0);
    }

    default void addDevice(long uid, String appType, String deviceToken, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        RTMServerClientBase client = getCoreClient();
        Quest quest = client.genBasicQuest("adddevice");
        quest.param("uid", uid);
        quest.param("apptype", appType);
        quest.param("devicetoken", deviceToken);
        client.sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    default void addDevice(long uid, String appType, String deviceToken, DoneLambdaCallback callback){
        addDevice(uid, appType, deviceToken, callback, 0);
    }

    default void addDevice(long uid, String appType, String deviceToken, DoneLambdaCallback callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("adddevice");
        }
        catch (Exception ex) {
            ErrorRecorder.record("Generate adddevice message sign exception.", ex);
            callback.done(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate adddevice message sign exception.");
            return;
        }
        quest.param("uid", uid);
        quest.param("apptype", appType);
        quest.param("devicetoken", deviceToken);
        AnswerCallback answerCallback = new FPNNDoneLambdaCallbackWrapper(callback);
        client.sendQuest(quest, answerCallback, timeoutInseconds);
    }

    default void removeDevice(long uid, String deviceToken)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        removeDevice(uid, deviceToken, 0);
    }

    default void removeDevice(long uid, String deviceToken, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        RTMServerClientBase client = getCoreClient();
        Quest quest = client.genBasicQuest("removedevice");
        quest.param("uid", uid);
        quest.param("devicetoken", deviceToken);
        client.sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    default void removeDevice(long uid, String deviceToken, DoneLambdaCallback callback){
        removeDevice(uid, deviceToken, callback, 0);
    }

    default void removeDevice(long uid, String deviceToken, DoneLambdaCallback callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("removedevice");
        }
        catch (Exception ex){
            ErrorRecorder.record("Generate removedevice message sign exception.", ex);
            callback.done(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate removedevice message sign exception.");
            return;
        }
        quest.param("uid", uid);
        quest.param("devicetoken", deviceToken);
        AnswerCallback answerCallback = new FPNNDoneLambdaCallbackWrapper(callback);
        client.sendQuest(quest, answerCallback, timeoutInseconds);
    }
}
