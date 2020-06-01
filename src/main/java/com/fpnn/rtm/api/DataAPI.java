package com.fpnn.rtm.api;

import com.fpnn.rtm.RTMException;
import com.fpnn.rtm.RTMServerClientBase;
import com.fpnn.sdk.AnswerCallback;
import com.fpnn.sdk.ErrorCode;
import com.fpnn.sdk.ErrorRecorder;
import com.fpnn.sdk.proto.Answer;
import com.fpnn.sdk.proto.Quest;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface DataAPI extends APIBase {

    interface DataGetCallback{
        void done(String value, int errorCode, String errorMessage);
    }

    default void dataSet(long uid, String key, String value)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        dataSet(uid, key, value, 0);
    }

    default void dataSet(long uid, String key, String value, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        RTMServerClientBase client = getCoreClient();
        Quest quest = client.genBasicQuest("dataset");
        quest.param("uid", uid);
        quest.param("key", key);
        quest.param("val", value);
        client.sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    default void dataSet(long uid, String key, String value, DoneLambdaCallback callback) {
        dataSet(uid, key, value, callback,0);
    }

    default void dataSet(long uid, String key, String value, DoneLambdaCallback callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("dataset");
        }catch (Exception ex){
            ErrorRecorder.record("Generate dataset message sign exception.", ex);
            callback.done(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate dataset message sign exception.");
            return;
        }
        quest.param("uid", uid);
        quest.param("key", key);
        quest.param("val", value);
        AnswerCallback answerCallback = new FPNNDoneLambdaCallbackWrapper(callback);
        client.sendQuest(quest, answerCallback, timeoutInseconds);
    }

    default void dataDel(long uid, String key)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        dataDel(uid, key, 0);
    }

    default void dataDel(long uid, String key, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        RTMServerClientBase client = getCoreClient();
        Quest quest = client.genBasicQuest("datadel");
        quest.param("uid", uid);
        quest.param("key", key);
        client.sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    default void dataDel(long uid, String key, DoneLambdaCallback callback) {
        dataDel(uid, key, callback,0);
    }

    default void dataDel(long uid, String key, DoneLambdaCallback callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("datadel");
        }catch (Exception ex){
            ErrorRecorder.record("Generate datadel message sign exception.", ex);
            callback.done(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate datadel message sign exception.");
            return;
        }
        quest.param("uid", uid);
        quest.param("key", key);
        AnswerCallback answerCallback = new FPNNDoneLambdaCallbackWrapper(callback);
        client.sendQuest(quest, answerCallback, timeoutInseconds);
    }

    default String dataGet(long uid, String key)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return dataGet(uid, key, 0);
    }

    default String dataGet(long uid, String key, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        RTMServerClientBase client = getCoreClient();
        Quest quest = client.genBasicQuest("dataget");
        quest.param("uid", uid);
        quest.param("key", key);
        Answer answer = client.sendQuestAndCheckAnswer(quest, timeoutInseconds);
        return (String)answer.get("val", "");
    }

    default void dataGet(long uid, String key, DataGetCallback callback) {
        dataGet(uid, key, callback,0);
    }

    default void dataGet(long uid, String key, DataGetCallback callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("dataget");
        }catch (Exception ex){
            ErrorRecorder.record("Generate dataget message sign exception.", ex);
            callback.done("", ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate dataget message sign exception.");
            return;
        }
        quest.param("uid", uid);
        quest.param("key", key);
        AnswerCallback answerCallback = new AnswerCallback() {
            @Override
            public void onAnswer(Answer answer) {
                callback.done((String)answer.get("val",""), ErrorCode.FPNN_EC_OK.value(), "");
            }

            @Override
            public void onException(Answer answer, int i) {
                String info = null;
                if (answer != null){
                    info = (String)answer.get("ex","");
                }
                callback.done("", i, info);
            }
        };
        client.sendQuest(quest, answerCallback, timeoutInseconds);
    }
}
