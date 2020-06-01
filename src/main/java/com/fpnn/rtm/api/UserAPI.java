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
import java.util.*;

public interface UserAPI extends APIBase {

    default void kickOut(long uid, String ce)
            throws RTMException, GeneralSecurityException, IOException,InterruptedException{
        kickOut(uid, ce,0);
    }

    default void kickOut(long uid)
            throws RTMException, GeneralSecurityException, IOException,InterruptedException{
        kickOut(uid, null ,0);
    }

    default void kickOut(long uid, String ce, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        RTMServerClientBase client = getCoreClient();
        Quest quest = client.genBasicQuest("kickout");
        quest.param("uid", uid);
        if(ce != null && ce.length() > 0)
            quest.param("ce", ce);
        client.sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    default void kickOut(long uid, String ce, DoneLambdaCallback callback){
        kickOut(uid, ce, callback, 0);
    }

    default void kickOut(long uid, DoneLambdaCallback callback){
        kickOut(uid, null, callback, 0);
    }

    default void kickOut(long uid, String ce, DoneLambdaCallback callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("kickout");
        }
        catch (Exception ex){
            ErrorRecorder.record("Generate kickout message sign exception.", ex);
            callback.done(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate kickout message sign exception.");
            return;
        }
        quest.param("uid", uid);
        if(ce != null && ce.length() > 0)
            quest.param("ce", ce);
        AnswerCallback answerCallback = new FPNNDoneLambdaCallbackWrapper(callback);
        client.sendQuest(quest, answerCallback, timeoutInseconds);
    }

    interface GetOnlineUsersCallback{
        void done(Set<Long> uids, int errorCode, String errorMessage);
    }

    default Set<Long> getOnlineUsers(Set<Long> uids)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException{
        return getOnlineUsers(uids, 0);
    }

    default Set<Long> getOnlineUsers(Set<Long> uids, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException{
        RTMServerClientBase client = getCoreClient();
        Quest quest = client.genBasicQuest("getonlineusers");
        quest.param("uids", uids);
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

    default void getOnlineUsers(Set<Long> uids, GetOnlineUsersCallback callback){
        getOnlineUsers(uids, callback, 0);
    }

    default void getOnlineUsers(Set<Long> uids, GetOnlineUsersCallback callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("getonlineusers");
        }catch (Exception ex){
            ErrorRecorder.record("Generate getonlineusers message sign exception.", ex);
            callback.done(null, ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate getonlineusers message sign exception.");
            return;
        }
        quest.param("uids", uids);
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

    default void addProjectBlack(long uid, int btime)
        throws RTMException, GeneralSecurityException, InterruptedException, IOException {
        addProjectBlack(uid, btime, 0);
    }

    default void addProjectBlack(long uid, int btime, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException {
        RTMServerClientBase client = getCoreClient();
        Quest quest = client.genBasicQuest("addprojectblack");
        quest.param("uid", uid);
        quest.param("btime", btime);
        client.sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    default void addProjectBlack(long uid, int btime, DoneLambdaCallback callback) {
        addProjectBlack(uid, btime, callback, 0);
    }

    default void addProjectBlack(long uid, int btime, DoneLambdaCallback callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("addprojectblack");
        }catch (Exception ex){
            ErrorRecorder.record("Generate addprojectblack message sign exception.", ex);
            callback.done(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate addprojectblack message sign exception.");
            return;
        }
        quest.param("uid", uid);
        quest.param("btime", btime);
        AnswerCallback answerCallback = new FPNNDoneLambdaCallbackWrapper(callback);
        client.sendQuest(quest, answerCallback, timeoutInseconds);
    }

    default void removeProjectBlack(long uid)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException {
        removeProjectBlack(uid, 0);
    }

    default void removeProjectBlack(long uid, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException {
        RTMServerClientBase client = getCoreClient();
        Quest quest = client.genBasicQuest("removeprojectblack");
        quest.param("uid", uid);
        client.sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    default void removeProjectBlack(long uid, DoneLambdaCallback callback) {
        removeProjectBlack(uid, callback, 0);
    }

    default void removeProjectBlack(long uid, DoneLambdaCallback callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("removeprojectblack");
        }catch (Exception ex){
            ErrorRecorder.record("Generate removeprojectblack message sign exception.", ex);
            callback.done(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate removeprojectblack message sign exception.");
            return;
        }
        quest.param("uid", uid);
        AnswerCallback answerCallback = new FPNNDoneLambdaCallbackWrapper(callback);
        client.sendQuest(quest, answerCallback, timeoutInseconds);
    }

    default boolean isProjectBlack(long uid)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException {
        return isProjectBlack(uid, 0);
    }

    default boolean isProjectBlack(long uid, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException {
        RTMServerClientBase client = getCoreClient();
        Quest quest = client.genBasicQuest("isprojectblack");
        quest.param("uid", uid);
        Answer answer = client.sendQuestAndCheckAnswer(quest, timeoutInseconds);
        return (boolean)answer.get("ok", false);
    }

    interface IsProjectBlackCallback{
        void done(boolean ok, int errorCode, String errorMessage);
    }

    default void isProjectBlack(long uid, IsProjectBlackCallback callback) {
        isProjectBlack(uid, callback, 0);
    }

    default void isProjectBlack(long uid, IsProjectBlackCallback callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("isprojectblack");
        }catch (Exception ex){
            ErrorRecorder.record("Generate isprojectblack message sign exception.", ex);
            callback.done(false, ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate isprojectblack message sign exception.");
            return;
        }
        quest.param("uid", uid);
        AnswerCallback answerCallback = new AnswerCallback() {
            @Override
            public void onAnswer(Answer answer) {
                callback.done((boolean)answer.get("ok", false), ErrorCode.FPNN_EC_OK.value(), "");
            }

            @Override
            public void onException(Answer answer, int i) {
                String info = null;
                if(answer != null){
                    info = (String)answer.get("ex", "");
                }
                callback.done(false, i, info);

            }
        };
        client.sendQuest(quest, answerCallback, timeoutInseconds);
    }

    default void setUserInfo(long uid, String openInfo, String priInfo)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException {
        setUserInfo(uid, openInfo, priInfo, 0);
    }

    default void setUserInfo(long uid, String openInfo, String priInfo, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException {
        RTMServerClientBase client = getCoreClient();
        Quest quest = client.genBasicQuest("setuserinfo");
        quest.param("uid", uid);
        if(openInfo != null && openInfo.length() > 0)
            quest.param("oinfo", openInfo);
        if(priInfo != null && priInfo.length() > 0)
            quest.param("pinfo", priInfo);
        client.sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    default void setUserInfo(long uid, String openInfo, String priInfo, DoneLambdaCallback callback) {
        setUserInfo(uid, openInfo, priInfo, callback, 0);
    }

    default void setUserInfo(long uid, String openInfo, String priInfo, DoneLambdaCallback callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("setuserinfo");
        }catch (Exception ex){
            ErrorRecorder.record("Generate setuserinfo message sign exception.", ex);
            callback.done(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate setuserinfo message sign exception.");
            return;
        }
        quest.param("uid", uid);
        if(openInfo != null && openInfo.length() > 0)
            quest.param("oinfo", openInfo);
        if(priInfo != null && priInfo.length() > 0)
            quest.param("pinfo", priInfo);
        AnswerCallback answerCallback = new FPNNDoneLambdaCallbackWrapper(callback);
        client.sendQuest(quest, answerCallback, timeoutInseconds);
    }

    interface GetUserInfoCallback{
        void done(String openInfo, String priInfo, int errorCode, String errorMessage);
    }

    default void getUserInfo(long uid, StringBuffer openInfo, StringBuffer priInfo)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException {
        getUserInfo(uid, openInfo, priInfo, 0);
    }

    default void getUserInfo(long uid, StringBuffer openInfo, StringBuffer priInfo, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException {
        RTMServerClientBase client = getCoreClient();
        Quest quest = client.genBasicQuest("getuserinfo");
        quest.param("uid", uid);
        Answer answer = client.sendQuestAndCheckAnswer(quest, timeoutInseconds);
        openInfo.setLength(0);
        priInfo.setLength(0);
        openInfo.append((String)answer.get("oinfo", ""));
        priInfo.append((String)answer.get("pinfo", ""));
    }

    default void getUserInfo(long uid, GetUserInfoCallback callback) {
        getUserInfo(uid, callback, 0);
    }

    default void getUserInfo(long uid, GetUserInfoCallback callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("getuserinfo");
        }catch (Exception ex){
            ErrorRecorder.record("Generate getuserinfo message sign exception.", ex);
            callback.done("", "", ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate getuserinfo message sign exception.");
            return;
        }
        quest.param("uid", uid);
        AnswerCallback answerCallback = new AnswerCallback() {
            @Override
            public void onAnswer(Answer answer) {
                callback.done((String)answer.get("oinfo", ""), (String)answer.get("pinfo", ""), ErrorCode.FPNN_EC_OK.value(), "");
            }

            @Override
            public void onException(Answer answer, int i) {
                String info = null;
                if(answer != null){
                    info = (String)answer.get("ex", "");
                }
                callback.done("", "", i, info);

            }
        };
        client.sendQuest(quest, answerCallback, timeoutInseconds);

    }

    interface GetUserOpenInfoCallback{
        void done(Map<String, String> info, int errorCode, String errorMessage);
    }

    default Map<String, String> getUserOpenInfo(Set<Long> uids)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException {
        return getUserOpenInfo(uids, 0);
    }

    default Map<String, String> getUserOpenInfo(Set<Long> uids, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException {
        RTMServerClientBase client = getCoreClient();
        Quest quest = client.genBasicQuest("getuseropeninfo");
        quest.param("uids", uids);
        Answer answer = client.sendQuestAndCheckAnswer(quest, timeoutInseconds);
        Map<String, String> result = new HashMap<>();
        Object object = answer.get("info", null);
        if(object != null){
            Map<Object, Object> data = (Map<Object, Object>)object;
            for(Map.Entry<Object, Object> entry : data.entrySet()){
                String uid = String.valueOf(entry.getKey());
                String info = String.valueOf(entry.getValue());
                result.put(uid, info);
            }
        }
        return result;
    }

    default void getUserOpenInfo(Set<Long> uids, GetUserOpenInfoCallback callback) {
        getUserOpenInfo(uids, callback, 0);
    }

    default void getUserOpenInfo(Set<Long> uids, GetUserOpenInfoCallback callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("getuseropeninfo");
        }catch (Exception ex){
            ErrorRecorder.record("Generate getuseropeninfo message sign exception.", ex);
            callback.done(null, ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate getuseropeninfo message sign exception.");
            return;
        }
        quest.param("uids", uids);
        AnswerCallback answerCallback = new AnswerCallback() {
            @Override
            public void onAnswer(Answer answer) {
                Map<String, String> result = new HashMap<>();
                Object object = answer.get("info", null);
                if(object != null){
                    Map<Object, Object> data = (Map<Object, Object>)object;
                    for(Map.Entry<Object, Object> entry : data.entrySet()){
                        String uid = String.valueOf(entry.getKey());
                        String info = String.valueOf(entry.getValue());
                        result.put(uid, info);
                    }
                }
                callback.done(result, ErrorCode.FPNN_EC_OK.value(), "");
            }

            @Override
            public void onException(Answer answer, int i) {
                String info = null;
                if(answer != null){
                    info = (String)answer.get("ex", "");
                }
                callback.done(null, i, info);

            }
        };
        client.sendQuest(quest, answerCallback, timeoutInseconds);
    }

}
