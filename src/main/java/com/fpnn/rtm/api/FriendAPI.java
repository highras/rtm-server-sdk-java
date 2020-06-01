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

public interface FriendAPI extends APIBase {

    default void addFriends(long uid, Set<Long> friends)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException {
        addFriends(uid, friends, 0);
    }

    default void addFriends(long uid, Set<Long> friends, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException {
        RTMServerClientBase client = getCoreClient();
        Quest quest = client.genBasicQuest("addfriends");
        quest.param("uid", uid);
        quest.param("friends", friends);
        client.sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    default void addFriends(long uid, Set<Long> friends, DoneLambdaCallback callback) {
        addFriends(uid, friends, callback, 0);
    }

    default void addFriends(long uid, Set<Long> friends, DoneLambdaCallback callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("addfriends");
        }catch (Exception ex){
            ErrorRecorder.record("Generate addfriends message sign exception.", ex);
            callback.done(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate addfriends message sign exception.");
            return;
        }
        quest.param("uid", uid);
        quest.param("friends", friends);
        AnswerCallback answerCallback = new FPNNDoneLambdaCallbackWrapper(callback);
        client.sendQuest(quest, answerCallback, timeoutInseconds);
    }

    default void delFriends(long uid, Set<Long> friends)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException {
        delFriends(uid, friends, 0);
    }

    default void delFriends(long uid, Set<Long> friends, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException {
        RTMServerClientBase client = getCoreClient();
        Quest quest = client.genBasicQuest("delfriends");
        quest.param("uid", uid);
        quest.param("friends", friends);
        client.sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    default void delFriends(long uid, Set<Long> friends, DoneLambdaCallback callback) {
        delFriends(uid, friends, callback, 0);
    }

    default void delFriends(long uid, Set<Long> friends, DoneLambdaCallback callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("delfriends");
        }catch (Exception ex){
            ErrorRecorder.record("Generate delfriends message sign exception.", ex);
            callback.done(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate delfriends message sign exception.");
            return;
        }
        quest.param("uid", uid);
        quest.param("friends", friends);
        AnswerCallback answerCallback = new FPNNDoneLambdaCallbackWrapper(callback);
        client.sendQuest(quest, answerCallback, timeoutInseconds);
    }

    interface GetFriendsLambdaCallBack{
        void done(Set<Long> fuids, int errorCode, String errorMessage);
    }

    default Set<Long> getFriends(long uid)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException {
        return getFriends(uid,0);
    }

    default Set<Long> getFriends(long uid, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException {
        RTMServerClientBase client = getCoreClient();
        Quest quest = client.genBasicQuest("getfriends");
        quest.param("uid", uid);

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

    default void getFriends(long uid, GetFriendsLambdaCallBack callback) {
        getFriends(uid, callback, 0);
    }

    default void getFriends(long uid, GetFriendsLambdaCallBack callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("getfriends");
        }catch (Exception ex){
            ErrorRecorder.record("Generate getfriends message sign exception.", ex);
            callback.done(null, ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate getfriends message sign exception.");
            return;
        }
        quest.param("uid", uid);
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

    interface IsFriendLambdaCallBack{
        void done(boolean ok, int errorCode, String errorMessage);
    }

    default boolean isFriend(long uid, long fuid)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException {
        return isFriend(uid, fuid,0);
    }

    default boolean isFriend(long uid, long fuid, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException {
        RTMServerClientBase client = getCoreClient();
        Quest quest = client.genBasicQuest("isfriend");
        quest.param("uid", uid);
        quest.param("fuid", fuid);
        Answer answer = client.sendQuestAndCheckAnswer(quest, timeoutInseconds);
        return (boolean)answer.get("ok",false);
    }

    default void isFriend(long uid, long fuid, IsFriendLambdaCallBack callback) {
        isFriend(uid, fuid, callback, 0);
    }

    default void isFriend(long uid, long fuid, IsFriendLambdaCallBack callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("isfriend");
        }catch (Exception ex){
            ErrorRecorder.record("Generate isfriend message sign exception.", ex);
            callback.done(false, ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate isfriend message sign exception.");
            return;
        }
        quest.param("uid", uid);
        quest.param("fuid", fuid);
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

    interface IsFriendsLambdaCallBack{
        void done(Set<Long> fuids, int errorCode, String errorMessage);
    }

    default Set<Long> isFriends(long uid, Set<Long> fuids)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException {
        return isFriends(uid, fuids,0);
    }

    default Set<Long> isFriends(long uid, Set<Long> fuids, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException {
        RTMServerClientBase client = getCoreClient();
        Quest quest = client.genBasicQuest("isfriends");
        quest.param("uid", uid);
        quest.param("fuids", fuids);
        Answer answer = client.sendQuestAndCheckAnswer(quest, timeoutInseconds);
        Object object = answer.get("fuids", null);
        Set<Long> result = new HashSet<>();
        if (object != null) {
            List<Object> data = (List<Object>) object;
            for (Object o : data) {
                result.add(Long.valueOf(String.valueOf(o)));
            }
        }
        return result;
    }


    default void isFriends(long uid, Set<Long> fuids, IsFriendsLambdaCallBack callback) {
        isFriends(uid, fuids, callback, 0);
    }

    default void isFriends(long uid, Set<Long> fuids, IsFriendsLambdaCallBack callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("isfriends");
        }catch (Exception ex){
            ErrorRecorder.record("Generate isfriends message sign exception.", ex);
            callback.done(null, ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate isfriends message sign exception.");
            return;
        }
        quest.param("uid", uid);
        quest.param("fuids", fuids);
        AnswerCallback answerCallback = new AnswerCallback() {
            @Override
            public void onAnswer(Answer answer) {
                Object object = answer.get("fuids", null);
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
