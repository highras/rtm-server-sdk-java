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

    default void addDevicePushOption(long uid, int pushType, long xid, Set<Integer> mtype)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        addDevicePushOption(uid, pushType, xid, mtype, 0);
    }

    default void addDevicePushOption(long uid, int pushType, long xid, Set<Integer> mtype, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        RTMServerClientBase client = getCoreClient();
        Quest quest = client.genBasicQuest("addoption");
        quest.param("uid", uid);
        quest.param("type", pushType);
        quest.param("xid", xid);
        if(mtype != null && mtype.size() > 0){
            quest.param("mtypes", mtype);
        }
        client.sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    default void addDevicePushOption(long uid, int pushType, long xid, Set<Integer> mtype, DoneLambdaCallback callback){
        addDevicePushOption(uid, pushType, xid, mtype, callback, 0);
    }

    default void addDevicePushOption(long uid, int pushType, long xid, Set<Integer> mtype, DoneLambdaCallback callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("addoption");
        }
        catch (Exception ex){
            ErrorRecorder.record("Generate addoption message sign exception.", ex);
            callback.done(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate addoption message sign exception.");
            return;
        }
        quest.param("uid", uid);
        quest.param("type", pushType);
        quest.param("xid", xid);
        if(mtype != null && mtype.size() > 0){
            quest.param("mtypes", mtype);
        }
        AnswerCallback answerCallback = new FPNNDoneLambdaCallbackWrapper(callback);
        client.sendQuest(quest, answerCallback, timeoutInseconds);
    }

    default void removeDevicePushOption(long uid, int pushType, long xid, Set<Integer> mtype)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        removeDevicePushOption(uid, pushType, xid, mtype, 0);
    }

    default void removeDevicePushOption(long uid, int pushType, long xid, Set<Integer> mtype, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        RTMServerClientBase client = getCoreClient();
        Quest quest = client.genBasicQuest("removeoption");
        quest.param("uid", uid);
        quest.param("type", pushType);
        quest.param("xid", xid);
        if(mtype != null && mtype.size() > 0){
            quest.param("mtypes", mtype);
        }
        client.sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    default void removeDevicePushOption(long uid, int pushType, long xid, Set<Integer> mtype, DoneLambdaCallback callback){
        removeDevicePushOption(uid, pushType, xid, mtype, callback, 0);
    }

    default void removeDevicePushOption(long uid, int pushType, long xid, Set<Integer> mtype, DoneLambdaCallback callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("removeoption");
        }
        catch (Exception ex){
            ErrorRecorder.record("Generate removeoption message sign exception.", ex);
            callback.done(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate removeoption message sign exception.");
            return;
        }
        quest.param("uid", uid);
        quest.param("type", pushType);
        quest.param("xid", xid);
        if(mtype != null && mtype.size() > 0){
            quest.param("mtypes", mtype);
        }
        AnswerCallback answerCallback = new FPNNDoneLambdaCallbackWrapper(callback);
        client.sendQuest(quest, answerCallback, timeoutInseconds);
    }

    interface GetPushOptionLambdaCallBack{
        void done(Map<Long, Set<Integer>> p2pOption, Map<Long, Set<Integer>> groupOption, int errorCode, String errorMessage);
    }

    default void getDevicePushOption(long uid, Map<Long, Set<Integer>> p2pOption, Map<Long, Set<Integer>> groupOption)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        getDevicePushOption(uid, p2pOption, groupOption,0);
    }

    default void getDevicePushOption(long uid, Map<Long, Set<Integer>> p2pOption, Map<Long, Set<Integer>> groupOption, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        RTMServerClientBase client = getCoreClient();
        Quest quest = client.genBasicQuest("getoption");
        quest.param("uid", uid);
        Answer answer = client.sendQuestAndCheckAnswer(quest, timeoutInseconds);
        Object object = answer.get("p2p", null);
        getOptionResult(object, p2pOption);
        Object object1 = answer.get("group", null);
        getOptionResult(object1, groupOption);
    }

    default void getDevicePushOption(long uid, GetPushOptionLambdaCallBack callback){
        getDevicePushOption(uid, callback, 0);
    }

    default void getOptionResult(Object object, Map<Long, Set<Integer>> result) {
        if(object != null){
            Map<Object, Object> data = (Map<Object, Object>)object;
            for(Map.Entry<Object, Object> entry : data.entrySet()) {
                Object key = entry.getKey();
                Long real_key = Long.valueOf(String.valueOf(key));
                List<Object> value = (List<Object>)entry.getValue();
                Set<Integer> real_value = new HashSet<>();
                for(Object o : value){
                    real_value.add(Integer.valueOf(String.valueOf(o)));
                }
                result.put(real_key, real_value);
            }
        }
    }

    default void getDevicePushOption(long uid, GetPushOptionLambdaCallBack callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("getoption");
        }
        catch (Exception ex){
            ErrorRecorder.record("Generate getoption message sign exception.", ex);
            callback.done(null, null, ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate getoption message sign exception.");
            return;
        }
        quest.param("uid", uid);
        AnswerCallback answerCallback = new AnswerCallback() {
            @Override
            public void onAnswer(Answer answer) {
                Object object = answer.get("p2p", null);
                HashMap<Long, Set<Integer>> p2pResult = new HashMap<>();
                getOptionResult(object, p2pResult);
                Object object1 = answer.get("group", null);
                HashMap<Long, Set<Integer>> groupResult = new HashMap<>();
                getOptionResult(object1, groupResult);
                callback.done(p2pResult, groupResult, ErrorCode.FPNN_EC_OK.value(), "");
            }

            @Override
            public void onException(Answer answer, int i) {
                String info = null;
                if(answer != null){
                    info = (String) answer.get("ex", "");
                }
                callback.done(null, null, i, info);

            }
        };
        client.sendQuest(quest, answerCallback, timeoutInseconds);
    }
}
