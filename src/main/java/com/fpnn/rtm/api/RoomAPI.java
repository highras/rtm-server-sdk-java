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

public interface RoomAPI extends APIBase {

    default void addRoomBan(long roomId, long uid, int banTime)
        throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        addRoomBan(roomId, uid, banTime, 0);
    }

    default void addRoomBan(long roomId, long uid, int banTime, int timeoutInseconds)
        throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        RTMServerClientBase clientBase = getCoreClient();
        Quest quest = clientBase.genBasicQuest("addroomban");
        if(roomId > 0){
            quest.param("rid", roomId);
        }
        quest.param("uid", uid);
        quest.param("btime", banTime);
        clientBase.sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    default void addRoomBan(long roomId, long uid, int banTime, DoneLambdaCallback callback){
        addRoomBan(roomId, uid, banTime, callback, 0);
    }

    default void addRoomBan(long roomId, long uid, int banTime, DoneLambdaCallback callback, int timeoutInseconds){
        RTMServerClientBase clientBase = getCoreClient();
        Quest quest;
        try{
            quest = clientBase.genBasicQuest("addroomban");
        }catch (Exception ex){
            ErrorRecorder.record("Generate addroomban message sign exception.", ex);
            callback.done(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate addroomban message sign exception.");
            return;
        }
        if(roomId > 0){
            quest.param("rid", roomId);
        }
        quest.param("uid", uid);
        quest.param("btime", banTime);
        AnswerCallback answerCallback = new FPNNDoneLambdaCallbackWrapper(callback);
        clientBase.sendQuest(quest, answerCallback, timeoutInseconds);
    }

    default void removeRoomBan(long roomId, long uid)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        removeRoomBan(roomId, uid, 0);
    }

    default void removeRoomBan(long roomId, long uid, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        RTMServerClientBase clientBase = getCoreClient();
        Quest quest = clientBase.genBasicQuest("removeroomban");
        if(roomId > 0){
            quest.param("rid", roomId);
        }
        quest.param("uid", uid);
        clientBase.sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    default void removeRoomBan(long roomId, long uid, DoneLambdaCallback callback){
        removeRoomBan(roomId, uid, callback, 0);
    }

    default void removeRoomBan(long roomId, long uid, DoneLambdaCallback callback, int timeoutInseconds){
        RTMServerClientBase clientBase = getCoreClient();
        Quest quest;
        try{
            quest = clientBase.genBasicQuest("removeroomban");
        }catch (Exception ex){
            ErrorRecorder.record("Generate removeroomban message sign exception.", ex);
            callback.done(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate removeroomban message sign exception.");
            return;
        }
        if(roomId > 0){
            quest.param("rid", roomId);
        }
        quest.param("uid", uid);
        AnswerCallback answerCallback = new FPNNDoneLambdaCallbackWrapper(callback);
        clientBase.sendQuest(quest, answerCallback, timeoutInseconds);
    }

    interface IsBanOfRoomCallBack{
        void done(boolean ok, int errorCode, String errorMessage);
    }

    default boolean isBanOfRoom(long roomId, long uid)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return isBanOfRoom(roomId, uid, 0);
    }

    default boolean isBanOfRoom(long roomId, long uid, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        RTMServerClientBase clientBase = getCoreClient();
        Quest quest = clientBase.genBasicQuest("isbanofroom");
        quest.param("rid", roomId);
        quest.param("uid", uid);
        Answer answer = clientBase.sendQuestAndCheckAnswer(quest, timeoutInseconds);
        return (Boolean)answer.get("ok", false);
    }

    default void isBanOfRoom(long roomId, long uid, IsBanOfRoomCallBack callback){
        isBanOfRoom(roomId, uid, callback, 0);
    }

    default void isBanOfRoom(long roomId, long uid, IsBanOfRoomCallBack callback, int timeoutInseconds){
        RTMServerClientBase clientBase = getCoreClient();
        Quest quest;
        try{
            quest = clientBase.genBasicQuest("isbanofroom");
        }catch (Exception ex){
            ErrorRecorder.record("Generate isbanofroom message sign exception.", ex);
            callback.done(false, ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate isbanofroom message sign exception.");
            return;
        }
        quest.param("rid", roomId);
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

    default void addRoomMember(long roomId, long uid)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        addRoomMember(roomId, uid, 0);
    }

    default void addRoomMember(long roomId, long uid, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        RTMServerClientBase clientBase = getCoreClient();
        Quest quest = clientBase.genBasicQuest("addroommember");
        quest.param("rid", roomId);
        quest.param("uid", uid);
        clientBase.sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    default void addRoomMember(long roomId, long uid, DoneLambdaCallback callback){
        addRoomMember(roomId, uid, callback, 0);
    }

    default void addRoomMember(long roomId, long uid, DoneLambdaCallback callback, int timeoutInseconds){
        RTMServerClientBase clientBase = getCoreClient();
        Quest quest;
        try{
            quest = clientBase.genBasicQuest("addroommember");
        }catch (Exception ex){
            ErrorRecorder.record("Generate addroommember message sign exception.", ex);
            callback.done(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate addroommember message sign exception.");
            return;
        }
        quest.param("rid", roomId);
        quest.param("uid", uid);
        AnswerCallback answerCallback = new FPNNDoneLambdaCallbackWrapper(callback);
        clientBase.sendQuest(quest, answerCallback, timeoutInseconds);
    }

    default void delRoomMember(long roomId, long uid)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        delRoomMember(roomId, uid, 0);
    }

    default void delRoomMember(long roomId, long uid, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        RTMServerClientBase clientBase = getCoreClient();
        Quest quest = clientBase.genBasicQuest("delroommember");
        quest.param("rid", roomId);
        quest.param("uid", uid);
        clientBase.sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    default void delRoomMember(long roomId, long uid, DoneLambdaCallback callback){
        delRoomMember(roomId, uid, callback, 0);
    }

    default void delRoomMember(long roomId, long uid, DoneLambdaCallback callback, int timeoutInseconds){
        RTMServerClientBase clientBase = getCoreClient();
        Quest quest;
        try{
            quest = clientBase.genBasicQuest("delroommember");
        }catch (Exception ex){
            ErrorRecorder.record("Generate delroommember message sign exception.", ex);
            callback.done(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate delroommember message sign exception.");
            return;
        }
        quest.param("rid", roomId);
        quest.param("uid", uid);
        AnswerCallback answerCallback = new FPNNDoneLambdaCallbackWrapper(callback);
        clientBase.sendQuest(quest, answerCallback, timeoutInseconds);
    }

    default void setRoomInfo(long roomId, String openInfo, String priInfo)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        setRoomInfo(roomId, openInfo, priInfo, 0);
    }

    default void setRoomInfo(long roomId, String openInfo, String priInfo, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        RTMServerClientBase client = getCoreClient();
        Quest quest = client.genBasicQuest("setroominfo");
        quest.param("rid", roomId);
        if(openInfo != null && openInfo.length() > 0)
            quest.param("oinfo", openInfo);
        if(priInfo != null && priInfo.length() > 0)
            quest.param("pinfo", priInfo);
        client.sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    default void setRoomInfo(long roomId, String openInfo, String priInfo, DoneLambdaCallback callback) {
        setRoomInfo(roomId, openInfo, priInfo, callback,0);
    }

    default void setRoomInfo(long roomId, String openInfo, String priInfo, DoneLambdaCallback callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("setroominfo");
        }catch (Exception ex){
            ErrorRecorder.record("Generate setroominfo message sign exception.", ex);
            callback.done(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate setroominfo message sign exception.");
            return;
        }
        quest.param("rid", roomId);
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

    default void getRoomInfo(long roomId, StringBuffer openInfo, StringBuffer priInfo)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        getRoomInfo(roomId, openInfo, priInfo, 0);
    }

    default void getRoomInfo(long roomId, StringBuffer openInfo, StringBuffer priInfo, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        RTMServerClientBase client = getCoreClient();
        Quest quest = client.genBasicQuest("getroominfo");
        quest.param("rid", roomId);
        Answer answer = client.sendQuestAndCheckAnswer(quest, timeoutInseconds);
        openInfo.setLength(0);
        priInfo.setLength(0);
        openInfo.append((String)answer.get("oinfo"));
        priInfo.append((String)answer.get("pinfo"));
    }

    default void getRoomInfo(long roomId, GetRoomInfoCallback callback) {
        getRoomInfo(roomId, callback,0);
    }

    default void getRoomInfo(long roomId, GetRoomInfoCallback callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("getroominfo");
        }catch (Exception ex){
            ErrorRecorder.record("Generate getroominfo message sign exception.", ex);
            callback.done("", "", ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate getroominfo message sign exception.");
            return;
        }
        quest.param("rid", roomId);
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

    interface GetRoomMembersCallback{
        void done(Set<Long> uids, int errorCode, String errorMessage);
    }

    default Set<Long> getRoomMembers(long roomId)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return getRoomMembers(roomId, 0);
    }

    default Set<Long> getRoomMembers(long roomId, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        RTMServerClientBase client = getCoreClient();
        Quest quest = client.genBasicQuest("getroommembers");
        quest.param("rid", roomId);
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

    default void getRoomMembers(long roomId, GetRoomMembersCallback callback) {
        getRoomMembers(roomId, callback,0);
    }

    default void getRoomMembers(long roomId, GetRoomMembersCallback callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("getroommembers");
        }catch (Exception ex){
            ErrorRecorder.record("Generate getroommembers message sign exception.", ex);
            callback.done(null, ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate getroommembers message sign exception.");
            return;
        }
        quest.param("rid", roomId);
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
                    info = (String)answer.get("ex","");
                }
                callback.done(null, i, info);

            }
        };
        client.sendQuest(quest, answerCallback, timeoutInseconds);
    }

    interface GetRoomUserCountCallback{
        void done(Map<Long, Integer> count, int errorCode, String errorMessage);
    }

    default Map<Long, Integer> getRoomUserCount(Set<Long> roomIds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return getRoomUserCount(roomIds, 0);
    }

    default Map<Long, Integer> getRoomUserCount(Set<Long> roomIds, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        RTMServerClientBase client = getCoreClient();
        Quest quest = client.genBasicQuest("getroomcount");
        quest.param("rids", roomIds);
        Answer answer = client.sendQuestAndCheckAnswer(quest, timeoutInseconds);
        Object o = answer.get("cn", null);
        Map<Long, Integer> count = new HashMap<>();
        if(o != null){
            Map<Object, Object> data = (Map<Object, Object>)o;
            for(Map.Entry<Object, Object> entry : data.entrySet()){
                count.put(Long.valueOf(String.valueOf(entry.getKey())), Integer.valueOf(String.valueOf(entry.getValue())));
            }
        }
        return count;
    }

    default void getRoomUserCount(Set<Long> roomIds, GetRoomUserCountCallback callback) {
        getRoomUserCount(roomIds, callback,0);
    }

    default void getRoomUserCount(Set<Long> roomIds, GetRoomUserCountCallback callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("getroomcount");
        }catch (Exception ex){
            ErrorRecorder.record("Generate getroomcount message sign exception.", ex);
            callback.done(null, ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate getroomcount message sign exception.");
            return;
        }
        quest.param("rids", roomIds);
        AnswerCallback answerCallback = new AnswerCallback() {
            @Override
            public void onAnswer(Answer answer) {
                Object o = answer.get("cn", null);
                Map<Long, Integer> count = new HashMap<>();
                if(o != null){
                    Map<Object, Object> data = (Map<Object, Object>)o;
                    for(Map.Entry<Object, Object> entry : data.entrySet()){
                        count.put(Long.valueOf(String.valueOf(entry.getKey())), Integer.valueOf(String.valueOf(entry.getValue())));
                    }
                }
                callback.done(count, ErrorCode.FPNN_EC_OK.value(), "");
            }

            @Override
            public void onException(Answer answer, int i) {
                String info = null;
                if(answer != null){
                    info = (String)answer.get("ex","");
                }
                callback.done(null, i, info);

            }
        };
        client.sendQuest(quest, answerCallback, timeoutInseconds);
    }

}
