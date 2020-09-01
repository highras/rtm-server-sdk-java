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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface BlacklistAPI extends APIBase {

    default void addBlacks(long userId, Set<Long> blacklistIds)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException {
        addBlacks(userId, blacklistIds, 0);
    }

    default void addBlacks(long userId, Set<Long> blacklistIds, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException {
        RTMServerClientBase client = getCoreClient();
        Quest quest = client.genBasicQuest("addblacks");
        quest.param("uid", userId);
        quest.param("blacks", blacklistIds);
        client.sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    default void addBlacks(long userId, Set<Long> blacklistIds, APIBase.DoneLambdaCallback callback) {
        addBlacks(userId, blacklistIds, callback, 0);
    }

    default void addBlacks(long userId, Set<Long> blacklistIds, APIBase.DoneLambdaCallback callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("addblacks");
        }catch (Exception ex){
            ErrorRecorder.record("Generate addblacks message sign exception.", ex);
            callback.done(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate addblacks message sign exception.");
            return;
        }
        quest.param("uid", userId);
        quest.param("blacks", blacklistIds);
        AnswerCallback answerCallback = new APIBase.FPNNDoneLambdaCallbackWrapper(callback);
        client.sendQuest(quest, answerCallback, timeoutInseconds);
    }

    default void delBlacks(long userId, Set<Long> blacklistIds)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException {
        delBlacks(userId, blacklistIds, 0);
    }

    default void delBlacks(long userId, Set<Long> blacklistIds, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException {
        RTMServerClientBase client = getCoreClient();
        Quest quest = client.genBasicQuest("delblacks");
        quest.param("uid", userId);
        quest.param("blacks", blacklistIds);
        client.sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    default void delBlacks(long userId, Set<Long> blacklistIds, APIBase.DoneLambdaCallback callback) {
        delBlacks(userId, blacklistIds, callback, 0);
    }

    default void delBlacks(long userId, Set<Long> blacklistIds, APIBase.DoneLambdaCallback callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("delblacks");
        }catch (Exception ex){
            ErrorRecorder.record("Generate delblacks message sign exception.", ex);
            callback.done(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate delblacks message sign exception.");
            return;
        }
        quest.param("uid", userId);
        quest.param("blacks", blacklistIds);
        AnswerCallback answerCallback = new APIBase.FPNNDoneLambdaCallbackWrapper(callback);
        client.sendQuest(quest, answerCallback, timeoutInseconds);
    }

    interface GetBlacksLambdaCallBack{
        void done(Set<Long> userIds, int errorCode, String errorMessage);
    }

    default Set<Long> getBlacks(long userId)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException {
        return getBlacks(userId,0);
    }

    default Set<Long> getBlacks(long userId, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException {
        RTMServerClientBase client = getCoreClient();
        Quest quest = client.genBasicQuest("getblacks");
        quest.param("uid", userId);

        Answer answer = client.sendQuestAndCheckAnswer(quest, timeoutInseconds);
        Object object = answer.get("uids", null);
        Set<Long> result = new HashSet<>();
        if(object != null){
            List<Object> data = (List<Object>)object;
            for(Object o : data){
                result.add(Long.valueOf(String.valueOf(o)));
            }
        }
        return result;
    }

    default void getBlacks(long userId, GetBlacksLambdaCallBack callback) {
        getBlacks(userId, callback, 0);
    }

    default void getBlacks(long userId, GetBlacksLambdaCallBack callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("getblacks");
        }catch (Exception ex){
            ErrorRecorder.record("Generate getblacks message sign exception.", ex);
            callback.done(null, ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate getblacks message sign exception.");
            return;
        }
        quest.param("uid", userId);
        AnswerCallback answerCallback = new AnswerCallback() {
            @Override
            public void onAnswer(Answer answer) {
                Object object = answer.get("uids", null);
                Set<Long> result = new HashSet<>();
                if(object != null){
                    List<Object> data = (List<Object>)object;
                    for(Object o : data){
                        result.add(Long.valueOf(String.valueOf(o)));
                    }
                }
                callback.done(result, ErrorCode.FPNN_EC_OK.value(), "");
            }

            @Override
            public void onException(Answer answer, int i) {
                String info = null;
                if(answer != null){
                    info = (String) answer.get("ex", "");
                }
                callback.done(null, i, info);

            }
        };
        client.sendQuest(quest, answerCallback, timeoutInseconds);
    }

    interface IsBlackLambdaCallBack{
        void done(boolean ok, int errorCode, String errorMessage);
    }

    default boolean isBlack(long userId, long blackUserId)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException {
        return isBlack(userId, blackUserId,0);
    }

    default boolean isBlack(long userId, long blackUserId, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException {
        RTMServerClientBase client = getCoreClient();
        Quest quest = client.genBasicQuest("isblack");
        quest.param("uid", userId);
        quest.param("buid", blackUserId);
        Answer answer = client.sendQuestAndCheckAnswer(quest, timeoutInseconds);
        return (boolean)answer.get("ok",false);
    }

    default void isBlack(long userId, long blackUserId, IsBlackLambdaCallBack callback) {
        isBlack(userId, blackUserId, callback, 0);
    }

    default void isBlack(long userId, long blackUserId, IsBlackLambdaCallBack callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("isblack");
        }catch (Exception ex){
            ErrorRecorder.record("Generate isblack message sign exception.", ex);
            callback.done(false, ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate isblack message sign exception.");
            return;
        }
        quest.param("uid", userId);
        quest.param("buid", blackUserId);
        AnswerCallback answerCallback = new AnswerCallback() {
            @Override
            public void onAnswer(Answer answer) {
                callback.done((boolean)answer.get("ok", false), ErrorCode.FPNN_EC_OK.value(), "");
            }

            @Override
            public void onException(Answer answer, int i) {
                String info = null;
                if(answer != null){
                    info = (String) answer.get("ex", "");
                }
                callback.done(false, i, info);

            }
        };
        client.sendQuest(quest, answerCallback, timeoutInseconds);
    }

    interface IsBlacksLambdaCallBack{
        void done(Set<Long> blackUserIds, int errorCode, String errorMessage);
    }

    default Set<Long> isBlacks(long userId, Set<Long> blackUserIds)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException {
        return isBlacks(userId, blackUserIds,0);
    }

    default Set<Long> isBlacks(long userId, Set<Long> blackUserIds, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException {
        RTMServerClientBase client = getCoreClient();
        Quest quest = client.genBasicQuest("isblacks");
        quest.param("uid", userId);
        quest.param("buids", blackUserIds);
        Answer answer = client.sendQuestAndCheckAnswer(quest, timeoutInseconds);
        Object object = answer.get("buids", null);
        Set<Long> result = new HashSet<>();
        if (object != null) {
            List<Object> data = (List<Object>) object;
            for (Object o : data) {
                result.add(Long.valueOf(String.valueOf(o)));
            }
        }
        return result;
    }


    default void isBlacks(long userId, Set<Long> blackUserIds, IsBlacksLambdaCallBack callback) {
        isBlacks(userId, blackUserIds, callback, 0);
    }

    default void isBlacks(long userId, Set<Long> blackUserIds, IsBlacksLambdaCallBack callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("isblacks");
        }catch (Exception ex){
            ErrorRecorder.record("Generate isblacks message sign exception.", ex);
            callback.done(null, ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value( ), "Generate isblacks message sign exception.");
            return;
        }
        quest.param("uid", userId);
        quest.param("buids", blackUserIds);
        AnswerCallback answerCallback = new AnswerCallback() {
            @Override
            public void onAnswer(Answer answer) {
                Object object = answer.get("buids", null);
                Set<Long> result = new HashSet<>();
                if (object != null) {
                    List<Object> data = (List<Object>) object;
                    for (Object o : data) {
                        result.add(Long.valueOf(String.valueOf(o)));
                    }
                }
                callback.done(result, ErrorCode.FPNN_EC_OK.value(), "");
            }

            @Override
            public void onException(Answer answer, int i) {
                String info = null;
                if(answer != null){
                    info = (String) answer.get("ex", "");
                }
                callback.done(null, i, info);

            }
        };
        client.sendQuest(quest, answerCallback, timeoutInseconds);
    }

}
