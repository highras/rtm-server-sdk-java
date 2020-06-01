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

public interface RoomAPI extends APIBase {

    default void addRoomBan(long rid, long uid, int btime)
        throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        addRoomBan(rid, uid, btime, 0);
    }

    default void addRoomBan(long rid, long uid, int btime, int timeoutInseconds)
        throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        RTMServerClientBase clientBase = getCoreClient();
        Quest quest = clientBase.genBasicQuest("addroomban");
        quest.param("rid", rid);
        quest.param("uid", uid);
        quest.param("btime", btime);
        clientBase.sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    default void addRoomBan(long rid, long uid, int btime, DoneLambdaCallback callback){
        addRoomBan(rid, uid, btime, callback, 0);
    }

    default void addRoomBan(long rid, long uid, int btime, DoneLambdaCallback callback, int timeoutInseconds){
        RTMServerClientBase clientBase = getCoreClient();
        Quest quest;
        try{
            quest = clientBase.genBasicQuest("addroomban");
        }catch (Exception ex){
            ErrorRecorder.record("Generate addroomban message sign exception.", ex);
            callback.done(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate addroomban message sign exception.");
            return;
        }
        quest.param("rid", rid);
        quest.param("uid", uid);
        quest.param("btime", btime);
        AnswerCallback answerCallback = new FPNNDoneLambdaCallbackWrapper(callback);
        clientBase.sendQuest(quest, answerCallback, timeoutInseconds);
    }

    default void removeRoomBan(long rid, long uid)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        removeRoomBan(rid, uid, 0);
    }

    default void removeRoomBan(long rid, long uid, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        RTMServerClientBase clientBase = getCoreClient();
        Quest quest = clientBase.genBasicQuest("removeroomban");
        quest.param("rid", rid);
        quest.param("uid", uid);
        clientBase.sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    default void removeRoomBan(long rid, long uid, DoneLambdaCallback callback){
        removeRoomBan(rid, uid, callback, 0);
    }

    default void removeRoomBan(long rid, long uid, DoneLambdaCallback callback, int timeoutInseconds){
        RTMServerClientBase clientBase = getCoreClient();
        Quest quest;
        try{
            quest = clientBase.genBasicQuest("removeroomban");
        }catch (Exception ex){
            ErrorRecorder.record("Generate removeroomban message sign exception.", ex);
            callback.done(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate removeroomban message sign exception.");
            return;
        }
        quest.param("rid", rid);
        quest.param("uid", uid);
        AnswerCallback answerCallback = new FPNNDoneLambdaCallbackWrapper(callback);
        clientBase.sendQuest(quest, answerCallback, timeoutInseconds);
    }

    interface IsBanOfRoomCallBack{
        void done(boolean ok, int errorCode, String errorMessage);
    }

    default boolean isBanOfRoom(long rid, long uid)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return isBanOfRoom(rid, uid, 0);
    }

    default boolean isBanOfRoom(long rid, long uid, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        RTMServerClientBase clientBase = getCoreClient();
        Quest quest = clientBase.genBasicQuest("isbanofroom");
        quest.param("rid", rid);
        quest.param("uid", uid);
        Answer answer = clientBase.sendQuestAndCheckAnswer(quest, timeoutInseconds);
        return (Boolean)answer.get("ok", false);
    }

    default void isBanOfRoom(long rid, long uid, IsBanOfRoomCallBack callback){
        isBanOfRoom(rid, uid, callback, 0);
    }

    default void isBanOfRoom(long rid, long uid, IsBanOfRoomCallBack callback, int timeoutInseconds){
        RTMServerClientBase clientBase = getCoreClient();
        Quest quest;
        try{
            quest = clientBase.genBasicQuest("isbanofroom");
        }catch (Exception ex){
            ErrorRecorder.record("Generate isbanofroom message sign exception.", ex);
            callback.done(false, ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate isbanofroom message sign exception.");
            return;
        }
        quest.param("rid", rid);
        quest.param("uid", uid);
        AnswerCallback answerCallback = new AnswerCallback() {
            @Override
            public void onAnswer(Answer answer) {
                callback.done((Boolean)answer.get("ok", false), ErrorCode.FPNN_EC_OK.value(), "");
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
        clientBase.sendQuest(quest, answerCallback, timeoutInseconds);
    }

    default void addRoomMember(long rid, long uid)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        addRoomMember(rid, uid, 0);
    }

    default void addRoomMember(long rid, long uid, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        RTMServerClientBase clientBase = getCoreClient();
        Quest quest = clientBase.genBasicQuest("addroommember");
        quest.param("rid", rid);
        quest.param("uid", uid);
        clientBase.sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    default void addRoomMember(long rid, long uid, DoneLambdaCallback callback){
        addRoomMember(rid, uid, callback, 0);
    }

    default void addRoomMember(long rid, long uid, DoneLambdaCallback callback, int timeoutInseconds){
        RTMServerClientBase clientBase = getCoreClient();
        Quest quest;
        try{
            quest = clientBase.genBasicQuest("addroommember");
        }catch (Exception ex){
            ErrorRecorder.record("Generate addroommember message sign exception.", ex);
            callback.done(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate addroommember message sign exception.");
            return;
        }
        quest.param("rid", rid);
        quest.param("uid", uid);
        AnswerCallback answerCallback = new FPNNDoneLambdaCallbackWrapper(callback);
        clientBase.sendQuest(quest, answerCallback, timeoutInseconds);
    }

    default void delRoomMember(long rid, long uid)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        delRoomMember(rid, uid, 0);
    }

    default void delRoomMember(long rid, long uid, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        RTMServerClientBase clientBase = getCoreClient();
        Quest quest = clientBase.genBasicQuest("delroommember");
        quest.param("rid", rid);
        quest.param("uid", uid);
        clientBase.sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    default void delRoomMember(long rid, long uid, DoneLambdaCallback callback){
        delRoomMember(rid, uid, callback, 0);
    }

    default void delRoomMember(long rid, long uid, DoneLambdaCallback callback, int timeoutInseconds){
        RTMServerClientBase clientBase = getCoreClient();
        Quest quest;
        try{
            quest = clientBase.genBasicQuest("delroommember");
        }catch (Exception ex){
            ErrorRecorder.record("Generate delroommember message sign exception.", ex);
            callback.done(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate delroommember message sign exception.");
            return;
        }
        quest.param("rid", rid);
        quest.param("uid", uid);
        AnswerCallback answerCallback = new FPNNDoneLambdaCallbackWrapper(callback);
        clientBase.sendQuest(quest, answerCallback, timeoutInseconds);
    }

    default void setRoomInfo(long rid, String openInfo, String priInfo)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        setRoomInfo(rid, openInfo, priInfo, 0);
    }

    default void setRoomInfo(long rid, String openInfo, String priInfo, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        RTMServerClientBase client = getCoreClient();
        Quest quest = client.genBasicQuest("setroominfo");
        quest.param("rid", rid);
        if(openInfo != null && openInfo.length() > 0)
            quest.param("oinfo", openInfo);
        if(priInfo != null && priInfo.length() > 0)
            quest.param("pinfo", priInfo);
        client.sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    default void setRoomInfo(long rid, String openInfo, String priInfo, DoneLambdaCallback callback) {
        setRoomInfo(rid, openInfo, priInfo, callback,0);
    }

    default void setRoomInfo(long rid, String openInfo, String priInfo, DoneLambdaCallback callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("setroominfo");
        }catch (Exception ex){
            ErrorRecorder.record("Generate setroominfo message sign exception.", ex);
            callback.done(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate setroominfo message sign exception.");
            return;
        }
        quest.param("rid", rid);
        if(openInfo != null && openInfo.length() > 0)
            quest.param("oinfo", openInfo);
        if(priInfo != null && priInfo.length() > 0)
            quest.param("pinfo", priInfo);
        AnswerCallback answerCallback = new FPNNDoneLambdaCallbackWrapper(callback);
        client.sendQuest(quest, answerCallback, timeoutInseconds);
    }

    interface GetRoomInfoCallback{
        void done(String openInfo, String priInfo, int errorCode, String errorMessage);
    }

    default void getRoomInfo(long rid, StringBuffer openInfo, StringBuffer priInfo)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        getRoomInfo(rid, openInfo, priInfo, 0);
    }

    default void getRoomInfo(long rid, StringBuffer openInfo, StringBuffer priInfo, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        RTMServerClientBase client = getCoreClient();
        Quest quest = client.genBasicQuest("getroominfo");
        quest.param("rid", rid);
        Answer answer = client.sendQuestAndCheckAnswer(quest, timeoutInseconds);
        openInfo.setLength(0);
        priInfo.setLength(0);
        openInfo.append((String)answer.get("oinfo"));
        priInfo.append((String)answer.get("pinfo"));
    }

    default void getRoomInfo(long rid, GetRoomInfoCallback callback) {
        getRoomInfo(rid, callback,0);
    }

    default void getRoomInfo(long rid, GetRoomInfoCallback callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("getroominfo");
        }catch (Exception ex){
            ErrorRecorder.record("Generate getroominfo message sign exception.", ex);
            callback.done("", "", ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate getroominfo message sign exception.");
            return;
        }
        quest.param("rid", rid);
        AnswerCallback answerCallback = new AnswerCallback() {
            @Override
            public void onAnswer(Answer answer) {
                callback.done((String)answer.get("oinfo", ""), (String)answer.get("pinfo", ""), ErrorCode.FPNN_EC_OK.value(), "");
            }

            @Override
            public void onException(Answer answer, int i) {
                String info = null;
                if(answer != null){
                    info = (String)answer.get("ex","");
                }
                callback.done("", "", i, info);

            }
        };
        client.sendQuest(quest, answerCallback, timeoutInseconds);
    }
}
