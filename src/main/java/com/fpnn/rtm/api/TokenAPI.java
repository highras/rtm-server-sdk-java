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

public interface TokenAPI extends APIBase {

    interface GetTokenCallback {
        void done(String token);
        void onException(int errorCode, String message);
    }

    interface GetTokenLambdaCallback {
        void done(String token, int errorCode, String errorMessage);
    }

    default void getToken(long uid, GetTokenCallback callback, int timeoutInseconds) {

        RTMServerClientBase client = getCoreClient();

        Quest quest;
        try {
            quest = client.genBasicQuest("gettoken");
        } catch (Exception e) {
            ErrorRecorder.record("Generate sign for gettoken interface exception.", e);
            callback.onException(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate sign for gettoken interface exception.");
            return;
        }

        quest.param("uid", uid);

        AnswerCallback internalCallback = new AnswerCallback() {
            @Override
            public void onAnswer(Answer answer)
            {
                if(answer != null)
                    callback.done((String) answer.get("token", ""));
            }

            @Override
            public void onException(Answer answer, int errorCode) {
                String info = null;
                if (answer != null)
                    info = (String) answer.get("ex", "");
                callback.onException(errorCode, info);
            }
        };

        client.sendQuest(quest, internalCallback, timeoutInseconds);
    }

    default void getToken(long uid, GetTokenCallback callback) {
        getToken(uid, callback, 0);
    }

    default boolean getToken(long uid, GetTokenLambdaCallback callback, int timeoutInseconds) {

        RTMServerClientBase client = getCoreClient();

        Quest quest;
        try {
            quest = client.genBasicQuest("gettoken");
        } catch (Exception e) {
            ErrorRecorder.record("Generate sign for gettoken interface exception.", e);
            callback.done("", ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate sign for gettoken interface exception.");
            return false;
        }

        quest.param("uid", uid);

        AnswerCallback internalCallback = new AnswerCallback() {
            @Override
            public void onAnswer(Answer answer) {
                if(answer != null)
                    callback.done((String) answer.get("token", ""), ErrorCode.FPNN_EC_OK.value(), "");
            }

            @Override
            public void onException(Answer answer, int errorCode) {
                String info = null;
                if (answer != null)
                    info = (String) answer.get("ex", "");
                callback.done("", errorCode, info);
            }
        };

        client.sendQuest(quest, internalCallback, timeoutInseconds);
        return true;
    }

    default boolean getToken(long uid, GetTokenLambdaCallback callback) {
        return getToken(uid, callback, 0);
    }

    default String getToken(long uid, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        RTMServerClientBase client = getCoreClient();

        Quest quest = client.genBasicQuest("gettoken");
        quest.param("uid", uid);

        Answer answer = client.sendQuestAndCheckAnswer(quest, timeoutInseconds);
        return (String) answer.get("token", "");
    }

    default String getToken(long uid)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return getToken(uid, 0);
    }

    default void removeToken(long uid, int timeoutInseconds)
        throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        RTMServerClientBase client = getCoreClient();
        Quest quest = client.genBasicQuest("removetoken");
        quest.param("uid", uid);
        client.sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    default void removeToken(long uid)
        throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        removeToken(uid, 0);
    }

    default void removeToken(long uid,  DoneLambdaCallback callback, int timeoutInseconds) {
        RTMServerClientBase client = getCoreClient();

        Quest quest;
        try {
            quest = client.genBasicQuest("removetoken");
        } catch (Exception e) {
            ErrorRecorder.record("Generate removetoken message sign exception.", e);
            callback.done(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate removetoken message sign exception.");
            return;
        }

        quest.param("uid", uid);
        AnswerCallback internalCallback = new FPNNDoneLambdaCallbackWrapper(callback);

        client.sendQuest(quest, internalCallback, timeoutInseconds);
    }

    default void removeToken(long uid,  DoneLambdaCallback callback) {

        removeToken(uid, callback, 0);
    }


}
