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

public interface RealTimeVoiceAPI extends APIBase {

    default void inviteUserIntoVoiceRoom(long rid, Set<Long> toUids, long fromUid)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        inviteUserIntoVoiceRoom(rid, toUids, fromUid, 0);
    }

    default void inviteUserIntoVoiceRoom(long rid, Set<Long> toUids, long fromUid, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        RTMServerClientBase clientBase = getCoreClient();
        Quest quest = clientBase.genBasicQuest("inviteUserIntoVoiceRoom");
        quest.param("rid", rid);
        quest.param("toUids", toUids);
        quest.param("fromUid", fromUid);
        clientBase.sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    default void inviteUserIntoVoiceRoom(long rid, Set<Long> toUids, long fromUid, DoneLambdaCallback callback){
        inviteUserIntoVoiceRoom(rid, toUids, fromUid, callback, 0);
    }

    default void inviteUserIntoVoiceRoom(long rid, Set<Long> toUids, long fromUid, DoneLambdaCallback callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("inviteUserIntoVoiceRoom");
        }catch (Exception ex){
            ErrorRecorder.record("Generate inviteUserIntoVoiceRoom message sign exception.", ex);
            callback.done(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate inviteUserIntoVoiceRoom message sign exception.");
            return;
        }
        quest.param("rid", rid);
        quest.param("toUids", toUids);
        quest.param("fromUid", fromUid);
        AnswerCallback answerCallback = new FPNNDoneLambdaCallbackWrapper(callback);
        client.sendQuest(quest, answerCallback, timeoutInseconds);
    }

    default void closeVoiceRoom(long rid)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        closeVoiceRoom(rid, 0);
    }

    default void closeVoiceRoom(long rid,  int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        RTMServerClientBase clientBase = getCoreClient();
        Quest quest = clientBase.genBasicQuest("closeVoiceRoom");
        quest.param("rid", rid);
        clientBase.sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    default void closeVoiceRoom(long rid, DoneLambdaCallback callback){
        closeVoiceRoom(rid, callback, 0);
    }

    default void closeVoiceRoom(long rid, DoneLambdaCallback callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("closeVoiceRoom");
        }catch (Exception ex){
            ErrorRecorder.record("Generate closeVoiceRoom message sign exception.", ex);
            callback.done(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate closeVoiceRoom message sign exception.");
            return;
        }
        quest.param("rid", rid);
        AnswerCallback answerCallback = new FPNNDoneLambdaCallbackWrapper(callback);
        client.sendQuest(quest, answerCallback, timeoutInseconds);
    }

    default void kickoutFromVoiceRoom(long rid, long uid, long fromUid)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        kickoutFromVoiceRoom(rid, uid, fromUid , 0);
    }

    default void kickoutFromVoiceRoom(long rid, long uid, long fromUid, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        RTMServerClientBase clientBase = getCoreClient();
        Quest quest = clientBase.genBasicQuest("kickoutFromVoiceRoom");
        quest.param("rid", rid);
        quest.param("uid", uid);
        quest.param("fromUid", fromUid);
        clientBase.sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    default void kickoutFromVoiceRoom(long rid, long uid, long fromUid, DoneLambdaCallback callback){
        kickoutFromVoiceRoom(rid, uid, fromUid, callback, 0);
    }

    default void kickoutFromVoiceRoom(long rid, long uid, long fromUid, DoneLambdaCallback callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("kickoutFromVoiceRoom");
        }catch (Exception ex){
            ErrorRecorder.record("Generate kickoutFromVoiceRoom message sign exception.", ex);
            callback.done(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate kickoutFromVoiceRoom message sign exception.");
            return;
        }
        quest.param("rid", rid);
        quest.param("uid", uid);
        quest.param("fromUid", fromUid);
        AnswerCallback answerCallback = new FPNNDoneLambdaCallbackWrapper(callback);
        client.sendQuest(quest, answerCallback, timeoutInseconds);
    }

    default void setVoiceRoomMicStatus(long rid, boolean status)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        setVoiceRoomMicStatus(rid, status, 0);
    }

    default void setVoiceRoomMicStatus(long rid, boolean status, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        RTMServerClientBase clientBase = getCoreClient();
        Quest quest = clientBase.genBasicQuest("setVoiceRoomMicStatus");
        quest.param("rid", rid);
        quest.param("status", status);
        clientBase.sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    default void setVoiceRoomMicStatus(long rid, boolean status, DoneLambdaCallback callback){
        setVoiceRoomMicStatus(rid, status, callback, 0);
    }

    default void setVoiceRoomMicStatus(long rid, boolean status, DoneLambdaCallback callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("setVoiceRoomMicStatus");
        }catch (Exception ex){
            ErrorRecorder.record("Generate setVoiceRoomMicStatus message sign exception.", ex);
            callback.done(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate setVoiceRoomMicStatus message sign exception.");
            return;
        }
        quest.param("rid", rid);
        quest.param("status", status);
        AnswerCallback answerCallback = new FPNNDoneLambdaCallbackWrapper(callback);
        client.sendQuest(quest, answerCallback, timeoutInseconds);
    }

    default void pullIntoVoiceRoom(long rid, Set<Long> toUids)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        pullIntoVoiceRoom(rid, toUids, 0);
    }

    default void pullIntoVoiceRoom(long rid, Set<Long> toUids, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        RTMServerClientBase clientBase = getCoreClient();
        Quest quest = clientBase.genBasicQuest("pullIntoVoiceRoom");
        quest.param("rid", rid);
        quest.param("toUids", toUids);
        clientBase.sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    default void pullIntoVoiceRoom(long rid, Set<Long> toUids, DoneLambdaCallback callback){
        pullIntoVoiceRoom(rid, toUids, callback, 0);
    }

    default void pullIntoVoiceRoom(long rid, Set<Long> toUids, DoneLambdaCallback callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("pullIntoVoiceRoom");
        }catch (Exception ex){
            ErrorRecorder.record("Generate pullIntoVoiceRoom message sign exception.", ex);
            callback.done(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate pullIntoVoiceRoom message sign exception.");
            return;
        }
        quest.param("rid", rid);
        quest.param("toUids", toUids);
        AnswerCallback answerCallback = new FPNNDoneLambdaCallbackWrapper(callback);
        client.sendQuest(quest, answerCallback, timeoutInseconds);
    }

    interface GetVoiceRoomListLambdaCallBack{
        void done(Set<Long> rids, int errorCode, String errorMessage);
    }

    default Set<Long> getVoiceRoomList()
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return getVoiceRoomList(0);
    }

    default Set<Long> getVoiceRoomList(int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        RTMServerClientBase clientBase = getCoreClient();
        Quest quest = clientBase.genBasicQuest("getVoiceRoomList");
        Answer answer = clientBase.sendQuestAndCheckAnswer(quest, timeoutInseconds);
        Object object = answer.get("rids", null);
        Set<Long> result = new HashSet<>();
        if(object != null){
            List<Object> data = (List<Object>)object;
            for(Object o : data){
                result.add(Long.valueOf(String.valueOf(o)));
            }
        }
        return result;
    }

    default void getVoiceRoomList(GetVoiceRoomListLambdaCallBack callBack) {
        getVoiceRoomList(callBack,0);
    }

    default void getVoiceRoomList(GetVoiceRoomListLambdaCallBack callBack, int timeoutInseconds) {
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("getVoiceRoomList");
        }catch (Exception ex){
            ErrorRecorder.record("Generate getVoiceRoomList message sign exception.", ex);
            callBack.done(null, ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate getVoiceRoomList message sign exception.");
            return;
        }
        AnswerCallback answerCallback = new AnswerCallback() {
            @Override
            public void onAnswer(Answer answer) {
                Object object = answer.get("rids", null);
                Set<Long> result = new HashSet<>();
                if(object != null){
                    List<Object> data = (List<Object>)object;
                    for(Object o : data){
                        result.add(Long.valueOf(String.valueOf(o)));
                    }
                }
                callBack.done(result, ErrorCode.FPNN_EC_OK.value(), "");
            }

            @Override
            public void onException(Answer answer, int i) {
                String info = null;
                if(answer != null){
                    info = (String) answer.get("ex", "");
                }
                callBack.done(null, i, info);

            }
        };
        client.sendQuest(quest, answerCallback, timeoutInseconds);
    }

    interface GetVoiceRoomMembersLambdaCallBack{
        void done(Set<Long> uids, Set<Long> managers, int errorCode, String errorMessage);
    }

    default void getVoiceRoomMembers(long rid, Set<Long> uids, Set<Long> managers)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        getVoiceRoomMembers(rid, uids, managers, 0);
    }

    default void getVoiceRoomMembers(long rid, Set<Long> uids, Set<Long> managers, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        RTMServerClientBase clientBase = getCoreClient();
        Quest quest = clientBase.genBasicQuest("getVoiceRoomMembers");
        quest.param("rid", rid);
        Answer answer = clientBase.sendQuestAndCheckAnswer(quest, timeoutInseconds);
        Object object = answer.get("uids", null);
        if(object != null){
            List<Object> data = (List<Object>)object;
            for(Object o : data){
                uids.add(Long.valueOf(String.valueOf(o)));
            }
        }

        Object object1 = answer.get("managers", null);
        if(object1 != null){
            List<Object> data = (List<Object>)object1;
            for(Object o : data){
                managers.add(Long.valueOf(String.valueOf(o)));
            }
        }

    }

    default void getVoiceRoomMembers(long rid, GetVoiceRoomMembersLambdaCallBack callBack) {
        getVoiceRoomMembers(rid, callBack,0);
    }

    default void getVoiceRoomMembers(long rid, GetVoiceRoomMembersLambdaCallBack callBack, int timeoutInseconds) {
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("getVoiceRoomMembers");
        }catch (Exception ex){
            ErrorRecorder.record("Generate getVoiceRoomMembers message sign exception.", ex);
            callBack.done(null, null, ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate getVoiceRoomMembers message sign exception.");
            return;
        }
        quest.param("rid", rid);
        AnswerCallback answerCallback = new AnswerCallback() {
            @Override
            public void onAnswer(Answer answer) {
                Set<Long> uids = new HashSet<>();
                Set<Long> managers = new HashSet<>();
                Object object = answer.get("uids", null);
                if(object != null){
                    List<Object> data = (List<Object>)object;
                    for(Object o : data){
                        uids.add(Long.valueOf(String.valueOf(o)));
                    }
                }

                Object object1 = answer.get("managers", null);
                if(object1 != null){
                    List<Object> data = (List<Object>)object1;
                    for(Object o : data){
                        managers.add(Long.valueOf(String.valueOf(o)));
                    }
                }
                callBack.done(uids, managers, ErrorCode.FPNN_EC_OK.value(), "");
            }

            @Override
            public void onException(Answer answer, int i) {
                String info = null;
                if(answer != null){
                    info = (String) answer.get("ex", "");
                }
                callBack.done(null, null, i, info);

            }
        };
        client.sendQuest(quest, answerCallback, timeoutInseconds);
    }

    interface GetVoiceRoomMemberCountLambdaCallBack{
        void done(int count, int errorCode, String errorMessage);
    }

    default int getVoiceRoomMemberCount(long rid)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return getVoiceRoomMemberCount(rid,0);
    }

    default int getVoiceRoomMemberCount(long rid, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        RTMServerClientBase clientBase = getCoreClient();
        Quest quest = clientBase.genBasicQuest("getVoiceRoomMemberCount");
        quest.param("rid", rid);
        Answer answer = clientBase.sendQuestAndCheckAnswer(quest, timeoutInseconds);
        return answer.getInt("count", 0);
    }

    default void getVoiceRoomMemberCount(long rid, GetVoiceRoomMemberCountLambdaCallBack callBack) {
        getVoiceRoomMemberCount(rid, callBack,0);
    }

    default void getVoiceRoomMemberCount(long rid, GetVoiceRoomMemberCountLambdaCallBack callBack, int timeoutInseconds) {
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("getVoiceRoomMemberCount");
        }catch (Exception ex){
            ErrorRecorder.record("Generate getVoiceRoomMemberCount message sign exception.", ex);
            callBack.done(0, ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate getVoiceRoomMemberCount message sign exception.");
            return;
        }
        quest.param("rid", rid);
        AnswerCallback answerCallback = new AnswerCallback() {
            @Override
            public void onAnswer(Answer answer) {
                int count = answer.getInt("count", 0);
                callBack.done(count, ErrorCode.FPNN_EC_OK.value(), "");
            }

            @Override
            public void onException(Answer answer, int i) {
                String info = null;
                if(answer != null){
                    info = (String) answer.get("ex", "");
                }
                callBack.done(0, i, info);

            }
        };
        client.sendQuest(quest, answerCallback, timeoutInseconds);
    }
}
