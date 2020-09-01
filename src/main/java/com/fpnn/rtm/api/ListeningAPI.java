package com.fpnn.rtm.api;

import com.fpnn.rtm.RTMException;
import com.fpnn.rtm.RTMServerClient;
import com.fpnn.rtm.RTMServerClientBase;
import com.fpnn.sdk.AnswerCallback;
import com.fpnn.sdk.ErrorCode;
import com.fpnn.sdk.ErrorRecorder;
import com.fpnn.sdk.proto.Quest;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Set;

public interface ListeningAPI extends APIBase {

    default void addListen(Set<Long> groupIds, Set<Long> roomIds, Set<Long> uids, Set<String> events)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        addListen(groupIds, roomIds, uids, events, 0);
    }

    default void addListen(Set<Long> groupIds, Set<Long> roomIds, Set<Long> uids, Set<String> events, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        RTMServerClientBase clientBase = getCoreClient();
        Quest quest = clientBase.genBasicQuest("addlisten");
        if(groupIds != null && groupIds.size() > 0)
            quest.param("gids", groupIds);
        if(roomIds != null && roomIds.size() > 0)
            quest.param("rids", roomIds);
        if(uids != null && uids.size() > 0)
            quest.param("uids", uids);
        if(events != null && events.size() > 0)
            quest.param("events", events);
        clientBase.sendQuestAndCheckAnswer(quest, timeoutInseconds);

        RTMServerClient client = (RTMServerClient)clientBase;
        client.addRTMListenCache(groupIds, roomIds, uids, events);
    }

    default void addListen(Set<Long> groupIds, Set<Long> roomIds, Set<Long> uids, Set<String> events, DoneLambdaCallback callback){
        addListen(groupIds, roomIds, uids, events, callback,0);
    }

    default void addListen(Set<Long> groupIds, Set<Long> roomIds, Set<Long> uids, Set<String> events, DoneLambdaCallback callback, int timeoutInseconds){
        RTMServerClientBase clientBase = getCoreClient();
        Quest quest;
        try{
            quest = clientBase.genBasicQuest("addlisten");
        }catch (Exception ex){
            ErrorRecorder.record("Generate addlisten message sign exception.", ex);
            callback.done(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate addlisten message sign exception.");
            return;
        }

        if(groupIds != null && groupIds.size() > 0)
            quest.param("gids", groupIds);
        if(roomIds != null && roomIds.size() > 0)
            quest.param("rids", roomIds);
        if(uids != null && uids.size() > 0)
            quest.param("uids", uids);
        if(events != null && events.size() > 0)
            quest.param("events", events);
        AnswerCallback answerCallback = new FPNNDoneLambdaCallbackWrapper(callback);
        clientBase.sendQuest(quest, answerCallback, timeoutInseconds);

        RTMServerClient client = (RTMServerClient)clientBase;
        client.addRTMListenCache(groupIds, roomIds, uids, events);
    }

    default void removeListen(Set<Long> groupIds, Set<Long> roomIds, Set<Long> uids, Set<String> events)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        removeListen(groupIds, roomIds, uids, events, 0);
    }

    default void removeListen(Set<Long> groupIds, Set<Long> roomIds, Set<Long> uids, Set<String> events, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        RTMServerClientBase clientBase = getCoreClient();

        Quest quest = clientBase.genBasicQuest("removelisten");
        if(groupIds != null && groupIds.size() > 0)
            quest.param("gids", groupIds);
        if(roomIds != null && roomIds.size() > 0)
            quest.param("rids", roomIds);
        if(uids != null && uids.size() > 0)
            quest.param("uids", uids);
        if(events != null && events.size() > 0)
            quest.param("events", events);
        clientBase.sendQuestAndCheckAnswer(quest, timeoutInseconds);

        RTMServerClient client = (RTMServerClient)clientBase;
        client.removeRTMListenCache(groupIds, roomIds, uids, events);
    }

    default void removeListen(Set<Long> groupIds, Set<Long> roomIds, Set<Long> uids, Set<String> events, DoneLambdaCallback callback){
        removeListen(groupIds, roomIds, uids, events, callback,0);
    }

    default void removeListen(Set<Long> groupIds, Set<Long> roomIds, Set<Long> uids, Set<String> events, DoneLambdaCallback callback, int timeoutInseconds){
        RTMServerClientBase clientBase = getCoreClient();
        Quest quest;
        try{
            quest = clientBase.genBasicQuest("removelisten");
        }catch (Exception ex){
            ErrorRecorder.record("Generate removelisten message sign exception.", ex);
            callback.done(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate removelisten message sign exception.");
            return;
        }

        if(groupIds != null && groupIds.size() > 0)
            quest.param("gids", groupIds);
        if(roomIds != null && roomIds.size() > 0)
            quest.param("rids", roomIds);
        if(uids != null && uids.size() > 0)
            quest.param("uids", uids);
        if(events != null && events.size() > 0)
            quest.param("events", events);
        AnswerCallback answerCallback = new FPNNDoneLambdaCallbackWrapper(callback);
        clientBase.sendQuest(quest, answerCallback, timeoutInseconds);

        RTMServerClient client = (RTMServerClient)clientBase;
        client.removeRTMListenCache(groupIds, roomIds, uids, events);
    }

    default void setListen(Set<Long> groupIds, Set<Long> roomIds, Set<Long> uids, Set<String> events)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        setListen(groupIds, roomIds, uids, events, 0);
    }

    default void setListen(Set<Long> groupIds, Set<Long> roomIds, Set<Long> uids, Set<String> events, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        RTMServerClientBase clientBase = getCoreClient();
        Quest quest = clientBase.genBasicQuest("setlisten");
        if(groupIds != null)
            quest.param("gids", groupIds);
        if(roomIds != null)
            quest.param("rids", roomIds);
        if(uids != null)
            quest.param("uids", uids);
        if(events != null)
            quest.param("events", events);
        clientBase.sendQuestAndCheckAnswer(quest, timeoutInseconds);

        RTMServerClient client = (RTMServerClient)clientBase;
        client.setRTMListenCache(groupIds, roomIds, uids, events);
    }

    default void setListen(Set<Long> groupIds, Set<Long> roomIds, Set<Long> uids, Set<String> events, DoneLambdaCallback callback){
        setListen(groupIds, roomIds, uids, events, callback,0);
    }

    default void setListen(Set<Long> groupIds, Set<Long> roomIds, Set<Long> uids, Set<String> events, DoneLambdaCallback callback, int timeoutInseconds){
        RTMServerClientBase clientBase = getCoreClient();
        Quest quest;
        try{
            quest = clientBase.genBasicQuest("setlisten");
        }catch (Exception ex){
            ErrorRecorder.record("Generate setlisten message sign exception.", ex);
            callback.done(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate setlisten message sign exception.");
            return;
        }

        if(groupIds != null)
            quest.param("gids", groupIds);
        if(roomIds != null)
            quest.param("rids", roomIds);
        if(uids != null)
            quest.param("uids", uids);
        if(events != null)
            quest.param("events", events);
        AnswerCallback answerCallback = new FPNNDoneLambdaCallbackWrapper(callback);
        clientBase.sendQuest(quest, answerCallback, timeoutInseconds);

        RTMServerClient client = (RTMServerClient)clientBase;
        client.setRTMListenCache(groupIds, roomIds, uids, events);
    }

    default void setListen(boolean allP2P, boolean allGroups, boolean allRooms, boolean allEvents)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        setListen(allP2P, allGroups, allRooms, allEvents, 0);
    }

    default void setListen(boolean allP2P, boolean allGroups, boolean allRooms, boolean allEvents, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        RTMServerClientBase clientBase = getCoreClient();
        Quest quest = clientBase.genBasicQuest("setlisten");
        quest.param("p2p", allP2P);
        quest.param("group", allGroups);
        quest.param("room", allRooms);
        quest.param("ev", allEvents);
        clientBase.sendQuestAndCheckAnswer(quest, timeoutInseconds);

        RTMServerClient client = (RTMServerClient)clientBase;
        client.setRTMListenCache(allP2P, allGroups, allRooms, allEvents);
    }

    default void setListen(boolean allP2P, boolean allGroups, boolean allRooms, boolean allEvents, DoneLambdaCallback callback){
        setListen(allP2P, allGroups, allRooms, allEvents, callback,0);
    }

    default void setListen(boolean allP2P, boolean allGroups, boolean allRooms, boolean allEvents, DoneLambdaCallback callback, int timeoutInseconds){
        RTMServerClientBase clientBase = getCoreClient();
        Quest quest;
        try{
            quest = clientBase.genBasicQuest("setlisten");
        }catch (Exception ex){
            ErrorRecorder.record("Generate setlisten message sign exception.", ex);
            callback.done(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate setlisten message sign exception.");
            return;
        }

        quest.param("p2p", allP2P);
        quest.param("group", allGroups);
        quest.param("room", allRooms);
        quest.param("ev", allEvents);
        AnswerCallback answerCallback = new FPNNDoneLambdaCallbackWrapper(callback);
        clientBase.sendQuest(quest, answerCallback, timeoutInseconds);

        RTMServerClient client = (RTMServerClient)clientBase;
        client.setRTMListenCache(allP2P, allGroups, allRooms, allEvents);
    }
}
