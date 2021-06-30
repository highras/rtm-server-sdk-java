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

public interface RTCAPI extends APIBase {

    default void inviteUserIntoRTCRoom(long rid, Set<Long> toUids, long fromUid)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        inviteUserIntoRTCRoom(rid, toUids, fromUid, 0);
    }

    default void inviteUserIntoRTCRoom(long rid, Set<Long> toUids, long fromUid, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        RTMServerClientBase clientBase = getCoreClient();
        Quest quest = clientBase.genBasicQuest("inviteUserIntoRTCRoom");
        quest.param("rid", rid);
        quest.param("toUids", toUids);
        quest.param("fromUid", fromUid);
        clientBase.sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    default void inviteUserIntoRTCRoom(long rid, Set<Long> toUids, long fromUid, DoneLambdaCallback callback){
        inviteUserIntoRTCRoom(rid, toUids, fromUid, callback, 0);
    }

    default void inviteUserIntoRTCRoom(long rid, Set<Long> toUids, long fromUid, DoneLambdaCallback callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("inviteUserIntoRTCRoom");
        }catch (Exception ex){
            ErrorRecorder.record("Generate inviteUserIntoRTCRoom message sign exception.", ex);
            callback.done(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate inviteUserIntoRTCRoom message sign exception.");
            return;
        }
        quest.param("rid", rid);
        quest.param("toUids", toUids);
        quest.param("fromUid", fromUid);
        AnswerCallback answerCallback = new FPNNDoneLambdaCallbackWrapper(callback);
        client.sendQuest(quest, answerCallback, timeoutInseconds);
    }

    default void closeRTCRoom(long rid)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        closeRTCRoom(rid, 0);
    }

    default void closeRTCRoom(long rid,  int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        RTMServerClientBase clientBase = getCoreClient();
        Quest quest = clientBase.genBasicQuest("closeRTCRoom");
        quest.param("rid", rid);
        clientBase.sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    default void closeRTCRoom(long rid, DoneLambdaCallback callback){
        closeRTCRoom(rid, callback, 0);
    }

    default void closeRTCRoom(long rid, DoneLambdaCallback callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("closeRTCRoom");
        }catch (Exception ex){
            ErrorRecorder.record("Generate closeRTCRoom message sign exception.", ex);
            callback.done(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate closeRTCRoom message sign exception.");
            return;
        }
        quest.param("rid", rid);
        AnswerCallback answerCallback = new FPNNDoneLambdaCallbackWrapper(callback);
        client.sendQuest(quest, answerCallback, timeoutInseconds);
    }

    default void kickoutFromRTCRoom(long rid, long uid, long fromUid)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        kickoutFromRTCRoom(rid, uid, fromUid , 0);
    }

    default void kickoutFromRTCRoom(long rid, long uid, long fromUid, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        RTMServerClientBase clientBase = getCoreClient();
        Quest quest = clientBase.genBasicQuest("kickoutFromRTCRoom");
        quest.param("rid", rid);
        quest.param("uid", uid);
        quest.param("fromUid", fromUid);
        clientBase.sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    default void kickoutFromRTCRoom(long rid, long uid, long fromUid, DoneLambdaCallback callback){
        kickoutFromRTCRoom(rid, uid, fromUid, callback, 0);
    }

    default void kickoutFromRTCRoom(long rid, long uid, long fromUid, DoneLambdaCallback callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("kickoutFromRTCRoom");
        }catch (Exception ex){
            ErrorRecorder.record("Generate kickoutFromRTCRoom message sign exception.", ex);
            callback.done(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate kickoutFromRTCRoom message sign exception.");
            return;
        }
        quest.param("rid", rid);
        quest.param("uid", uid);
        quest.param("fromUid", fromUid);
        AnswerCallback answerCallback = new FPNNDoneLambdaCallbackWrapper(callback);
        client.sendQuest(quest, answerCallback, timeoutInseconds);
    }

    default void setRTCRoomMicStatus(long rid, boolean status)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        setRTCRoomMicStatus(rid, status, 0);
    }

    default void setRTCRoomMicStatus(long rid, boolean status, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        RTMServerClientBase clientBase = getCoreClient();
        Quest quest = clientBase.genBasicQuest("setRTCRoomMicStatus");
        quest.param("rid", rid);
        quest.param("status", status);
        clientBase.sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    default void setRTCRoomMicStatus(long rid, boolean status, DoneLambdaCallback callback){
        setRTCRoomMicStatus(rid, status, callback, 0);
    }

    default void setRTCRoomMicStatus(long rid, boolean status, DoneLambdaCallback callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("setRTCRoomMicStatus");
        }catch (Exception ex){
            ErrorRecorder.record("Generate setRTCRoomMicStatus message sign exception.", ex);
            callback.done(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate setRTCRoomMicStatus message sign exception.");
            return;
        }
        quest.param("rid", rid);
        quest.param("status", status);
        AnswerCallback answerCallback = new FPNNDoneLambdaCallbackWrapper(callback);
        client.sendQuest(quest, answerCallback, timeoutInseconds);
    }

    default void pullIntoRTCRoom(long rid, Set<Long> toUids, int type)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        pullIntoRTCRoom(rid, toUids, type,0);
    }

    default void pullIntoRTCRoom(long rid, Set<Long> toUids, int type, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        RTMServerClientBase clientBase = getCoreClient();
        Quest quest = clientBase.genBasicQuest("pullIntoRTCRoom");
        quest.param("rid", rid);
        quest.param("toUids", toUids);
        quest.param("type", type);
        clientBase.sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    default void pullIntoRTCRoom(long rid, Set<Long> toUids, int type, DoneLambdaCallback callback){
        pullIntoRTCRoom(rid, toUids, type, callback, 0);
    }

    default void pullIntoRTCRoom(long rid, Set<Long> toUids, int type, DoneLambdaCallback callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("pullIntoRTCRoom");
        }catch (Exception ex){
            ErrorRecorder.record("Generate pullIntoRTCRoom message sign exception.", ex);
            callback.done(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate pullIntoRTCRoom message sign exception.");
            return;
        }
        quest.param("rid", rid);
        quest.param("toUids", toUids);
        quest.param("type", type);
        AnswerCallback answerCallback = new FPNNDoneLambdaCallbackWrapper(callback);
        client.sendQuest(quest, answerCallback, timeoutInseconds);
    }

    interface GetRTCRoomListLambdaCallBack{
        void done(Set<Long> rids, int errorCode, String errorMessage);
    }

    default Set<Long> getRTCRoomList()
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return getRTCRoomList(0);
    }

    default Set<Long> getRTCRoomList(int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        RTMServerClientBase clientBase = getCoreClient();
        Quest quest = clientBase.genBasicQuest("getRTCRoomList");
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

    default void getRTCRoomList(GetRTCRoomListLambdaCallBack callBack) {
        getRTCRoomList(callBack,0);
    }

    default void getRTCRoomList(GetRTCRoomListLambdaCallBack callBack, int timeoutInseconds) {
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("getRTCRoomList");
        }catch (Exception ex){
            ErrorRecorder.record("Generate getRTCRoomList message sign exception.", ex);
            callBack.done(null, ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate getRTCRoomList message sign exception.");
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

    interface GetRTCRoomMembersLambdaCallBack{
        void done(Set<Long> uids, Set<Long> managers, long ownerId, int errorCode, String errorMessage);
    }

    default long getRTCRoomMembers(long rid, Set<Long> uids, Set<Long> managers)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return getRTCRoomMembers(rid, uids, managers, 0);
    }

    default long getRTCRoomMembers(long rid, Set<Long> uids, Set<Long> managers, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        RTMServerClientBase clientBase = getCoreClient();
        Quest quest = clientBase.genBasicQuest("getRTCRoomMembers");
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

        return answer.getLong("owner", 0);
    }

    default void getRTCRoomMembers(long rid, GetRTCRoomMembersLambdaCallBack callBack) {
        getRTCRoomMembers(rid, callBack,0);
    }

    default void getRTCRoomMembers(long rid, GetRTCRoomMembersLambdaCallBack callBack, int timeoutInseconds) {
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("getRTCRoomMembers");
        }catch (Exception ex){
            ErrorRecorder.record("Generate getRTCRoomMembers message sign exception.", ex);
            callBack.done(null, null, 0,ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate getRTCRoomMembers message sign exception.");
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
                long ownerId = answer.getLong("owner", 0);
                callBack.done(uids, managers, ownerId, ErrorCode.FPNN_EC_OK.value(), "");
            }

            @Override
            public void onException(Answer answer, int i) {
                String info = null;
                if(answer != null){
                    info = (String) answer.get("ex", "");
                }
                callBack.done(null, null, 0,i, info);

            }
        };
        client.sendQuest(quest, answerCallback, timeoutInseconds);
    }

    interface GetRTCRoomMemberCountLambdaCallBack{
        void done(int count, int errorCode, String errorMessage);
    }

    default int getRTCRoomMemberCount(long rid)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return getRTCRoomMemberCount(rid,0);
    }

    default int getRTCRoomMemberCount(long rid, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        RTMServerClientBase clientBase = getCoreClient();
        Quest quest = clientBase.genBasicQuest("getRTCRoomMemberCount");
        quest.param("rid", rid);
        Answer answer = clientBase.sendQuestAndCheckAnswer(quest, timeoutInseconds);
        return answer.getInt("count", 0);
    }

    default void getRTCRoomMemberCount(long rid, GetRTCRoomMemberCountLambdaCallBack callBack) {
        getRTCRoomMemberCount(rid, callBack,0);
    }

    default void getRTCRoomMemberCount(long rid, GetRTCRoomMemberCountLambdaCallBack callBack, int timeoutInseconds) {
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("getRTCRoomMemberCount");
        }catch (Exception ex){
            ErrorRecorder.record("Generate getRTCRoomMemberCount message sign exception.", ex);
            callBack.done(0, ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate getRTCRoomMemberCount message sign exception.");
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

    default void adminCommand(long rid, Set<Long> uids, int command)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        pullIntoRTCRoom(rid, uids, command,0);
    }

    default void adminCommand(long rid, Set<Long> uids, int command, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        RTMServerClientBase clientBase = getCoreClient();
        Quest quest = clientBase.genBasicQuest("adminCommand");
        quest.param("rid", rid);
        quest.param("uids", uids);
        quest.param("command", command);
        clientBase.sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    default void adminCommand(long rid, Set<Long> uids, int command, DoneLambdaCallback callback){
        pullIntoRTCRoom(rid, uids, command, callback, 0);
    }

    default void adminCommand(long rid, Set<Long> uids, int command, DoneLambdaCallback callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("adminCommand");
        }catch (Exception ex){
            ErrorRecorder.record("Generate adminCommand message sign exception.", ex);
            callback.done(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate adminCommand message sign exception.");
            return;
        }
        quest.param("rid", rid);
        quest.param("uids", uids);
        quest.param("command", command);
        AnswerCallback answerCallback = new FPNNDoneLambdaCallbackWrapper(callback);
        client.sendQuest(quest, answerCallback, timeoutInseconds);
    }
}


