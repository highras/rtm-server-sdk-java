package com.fpnn.rtm;

import com.fpnn.rtm.api.*;
import com.fpnn.sdk.*;
import com.fpnn.sdk.proto.Quest;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;

public class RTMServerClient extends RTMServerClientBase implements ChatAPI, DataAPI, FileAPI, FriendAPI,
        GroupAPI, ListeningAPI, MessageAPI, RoomAPI, TokenAPI, UserAPI, DeviceAPI, UtilitiesAPI, HistoryChatAPI,
        HistoryMessageAPI, BlacklistAPI, RTCAPI {

    public static String SDKVersion = "2.5.5";
    public static String InterfaceVersion = "2.7.0";

    public static class RegressiveState {
        public int currentFailedCount = 0;
        public long connectSuccessMilliseconds = 0;
    }

    private class RTMConnectCallBack implements ConnectionConnectedCallback {
        private RTMServerClient client;
        RTMClientConnectCallback userCallback;

        RTMConnectCallBack(RTMServerClient client, RTMClientConnectCallback callback) {
            this.client = client;
            userCallback = callback;
        }

        public void connectResult(InetSocketAddress peerAddress, boolean connected) {
            if(connected){
                client.regressiveState.currentFailedCount = 0;
                client.regressiveState.connectSuccessMilliseconds = System.currentTimeMillis();
                client.sendListenCache();
            }

            if (userCallback != null) {
                RegressiveState state = client.regressiveState;
                userCallback.connectResult(peerAddress, connected, client.canReconnect(), state);
            }
            if (!connected) {
                regressiveState.currentFailedCount++;
                ClientEngine.getThreadPool().execute(() -> {
                    try {
                        client.regressiveReconnection();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        ErrorRecorder.record("RTM regressiveReconnection reconnect exception.", ex);
                    }
                });
            }
        }
    }

    private class RTMWillCloseCallback implements ConnectionWillCloseCallback {
        private RTMServerClient client;
        private RTMClientWillCloseCallback userCallback;
        RTMWillCloseCallback(RTMServerClient client, RTMClientWillCloseCallback callback) {
            this.client = client;
            userCallback = callback;
        }

        public void connectionWillClose(InetSocketAddress peerAddress, boolean causedByError) {
            if (userCallback != null) {
                userCallback.connectionWillClose(peerAddress, causedByError);
            }
        }
    }

    private class RTMHasClosedCallback implements ConnectionHasClosedCallback{
        private RTMServerClient client;
        private RTMClientHasClosedCallback userCallback;
        RTMHasClosedCallback(RTMServerClient client, RTMClientHasClosedCallback callback){
            this.client = client;
            userCallback =callback;
        }

        public void connectionHasClosed(InetSocketAddress peerAddress, boolean causedByError){
            if(userCallback != null){
                RegressiveState state = client.regressiveState;
                userCallback.connectionHasClosed(peerAddress, causedByError, client.canReconnect(), state);
            }
            ClientEngine.getThreadPool().execute(() -> {
                try {
                    client.regressiveReconnection();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    ErrorRecorder.record("RTM regressiveReconnection reconnect exception.", ex);
                }
            });
        }

    }

    private RTMRegressiveConnectStrategy defaultRegressiveConnectStrategy;
    private RTMRegressiveConnectStrategy regressiveConnectStrategy;
    private RegressiveState regressiveState;
    private volatile boolean isClose;

    private class RTMListenCache {
        private HashSet<Long> listenGids;
        private HashSet<Long> listenRids;
        private HashSet<Long> listenUids;
        private HashSet<String> listenEvents;
        private boolean p2p;
        private boolean group;
        private boolean room;
        private boolean event;

        private RTMListenCache() {
            listenGids = new HashSet<>();
            listenRids = new HashSet<>();
            listenUids = new HashSet<>();
            listenEvents = new HashSet<>();
            this.p2p = false;
            this.group = false;
            this.room = false;
            this.event = false;
        }

        private void addUids(Set<Long> uids) {
            for (Long uid : uids) {
                listenUids.add(uid);
            }

        }

        private void addGids(Set<Long> gids) {
            for (Long gid : gids) {
                listenGids.add(gid);
            }
        }

        private void addRids(Set<Long> rids) {
            for (Long rid : rids) {
                listenRids.add(rid);
            }
        }

        private void addEvents(Set<String> events) {
            for (String e : events) {
                listenEvents.add(e);
            }
        }

        private void removeUids(Set<Long> uids) {
            for (Long u : uids) {
                listenUids.remove(u);
            }
        }

        private void removeRids(Set<Long> rids) {
            for (Long r : rids) {
                listenRids.remove(r);
            }
        }

        private void removeGids(Set<Long> gids) {
            for (Long g : gids) {
                listenGids.remove(g);
            }
        }

        private void removeEvents(Set<String> events) {
            for (String e : events) {
                listenEvents.remove(e);
            }
        }

        private synchronized void setUids(Set<Long> uids) {
            listenUids.clear();
            for (Long u : uids) {
                listenUids.add(u);
            }
        }

        private void setGids(Set<Long> gids) {
            listenGids.clear();
            for (Long gid : gids) {
                listenGids.add(gid);
            }
        }

        private void setRids(Set<Long> rids) {
            listenRids.clear();
            for (Long rid : rids) {
                listenRids.add(rid);
            }
        }

        private void setEvents(Set<String> events) {
            listenEvents.clear();
            for (String e : events) {
                listenEvents.add(e);
            }
        }

        private void setUids(boolean p2p) {
            this.p2p = p2p;
            if (p2p) {
                listenUids.clear();
            }
        }

        private void setGids(boolean group) {
            this.group = group;
            if (group) {
                listenGids.clear();
            }
        }

        private void setRids(boolean room) {
            this.room = room;
            if (room) {
                listenRids.clear();
            }
        }

        private void setEvents(boolean event) {
            this.event = event;
            if (event) {
                listenEvents.clear();
            }
        }

        private boolean empty() {
            if(listenEvents.isEmpty() && listenUids.isEmpty() && listenGids.isEmpty() && listenRids.isEmpty())
                return true;
            return false;
        }

        private boolean isAllFalse() {
            if(!p2p && !group && !room && !event)
                return true;
            return false;
        }

        private void clear(){
            listenRids.clear();
            listenGids.clear();
            listenUids.clear();
            listenEvents.clear();
            p2p = false;
            group = false;
            room = false;
            event = false;
        }
    }

    private RTMListenCache listenCache;
    private byte[] cacheLocker;

    public RTMServerClient(int pid, String secretKey, String host, int port) {
        super(pid, secretKey, host, port);
        isClose = false;
        regressiveState = new RegressiveState();
        listenCache = new RTMListenCache();
        cacheLocker = new byte[0];
        regressiveConnectStrategy = new RTMRegressiveConnectStrategy();
        setRTMClientConnectedCallback(null);
        setRTMClientWillCloseCallback(null);
        setRTMClientHasClosedCallback(null);
    }

    public static RTMServerClient create(int pid, String secretKey, String endpoint) throws IllegalArgumentException {
        String[] endpointInfo = endpoint.split(":");
        if (endpointInfo.length != 2)
            throw new IllegalArgumentException("Endpoint " + endpoint + " is invalid format.");

        int port = Integer.parseInt(endpointInfo[1]);
        if (port <= 0 || port > 65535)
            throw new IllegalArgumentException("Port in endpoint is invalid.");

        return new RTMServerClient(pid, secretKey, endpointInfo[0], port);
    }

    public void setDefaultRegressiveConnectStrategy(RTMRegressiveConnectStrategy strategy) {
        if(strategy == null)
            return;
        if (defaultRegressiveConnectStrategy == null){
            defaultRegressiveConnectStrategy = strategy;
            regressiveConnectStrategy = strategy;
        }
    }

    @Override
    public boolean connect(boolean synchronous) throws InterruptedException {
        isClose = false;
        return super.connect(synchronous);
    }

    @Override
    public boolean reconnect(boolean synchronous) throws InterruptedException {
        if(super.connected())
            return false;
        if (!canReconnect())
            return false;
        return connect(synchronous);
    }

    @Override
    public void close() {
        isClose = true;
        super.close();
    }

    //   internal private function
    private boolean canReconnect() {
        if (!isAutoConnect()) {
            return false;
        }

        if (isClose) {
            return false;
        }
        return true;
    }

    private void regressiveReconnection() throws InterruptedException {
        long current = System.currentTimeMillis();
        if (current - regressiveState.connectSuccessMilliseconds > regressiveConnectStrategy.connectFailedMaxIntervalMilliseconds) {
            if (regressiveState.currentFailedCount <= regressiveConnectStrategy.startConnectFailedCount) {
                reconnect(false);
                return;
            }
        }
        else{
            regressiveState.currentFailedCount++;
        }

        if (regressiveState.currentFailedCount <= regressiveConnectStrategy.startConnectFailedCount) {
            reconnect(false);
            return;
        }

        int idleSeconds = regressiveConnectStrategy.maxIntervalSeconds - regressiveConnectStrategy.firstIntervalSeconds;
        int perIdleMilliseconds = idleSeconds * 1000 / regressiveConnectStrategy.linearRegressiveCount;
        int currIdleMilliseconds = (regressiveState.currentFailedCount - regressiveConnectStrategy.startConnectFailedCount)
                * perIdleMilliseconds + regressiveConnectStrategy.firstIntervalSeconds * 1000;
        if (currIdleMilliseconds > regressiveConnectStrategy.maxIntervalSeconds * 1000) {
            currIdleMilliseconds = regressiveConnectStrategy.maxIntervalSeconds * 1000;
        }

        try {
            Thread.sleep(currIdleMilliseconds);
        } catch (Exception ex) {
            ex.printStackTrace();
            ErrorRecorder.record("RTM regressiveReconnection sleep exception.", ex);
        }
        reconnect(false);
    }

    private void sendListenCache() {
        synchronized (cacheLocker){
            if(listenCache.isAllFalse() && listenCache.empty())
                return;

            Quest quest;
            if(!listenCache.empty())
            {
                DoneLambdaCallback callback = (errorCode, message) -> System.out.println("after connected send listenCache callback code = " + errorCode + " ,errorMsg = " + message);
                try {
                    quest = genBasicQuest("addlisten");
                } catch (Exception ex) {
                    ErrorRecorder.record("Generate addlisten message sign exception.", ex);
                    callback.done(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate addlisten message sign exception.");
                    return;
                }
                if (listenCache.listenGids.size() > 0)
                    quest.param("gids", listenCache.listenGids);
                if (listenCache.listenRids.size() > 0)
                    quest.param("rids", listenCache.listenRids);
                if (listenCache.listenUids.size() > 0)
                    quest.param("uids", listenCache.listenUids);
                if (listenCache.listenEvents.size() > 0)
                    quest.param("events", listenCache.listenEvents);
                AnswerCallback answerCallback = new FPNNDoneLambdaCallbackWrapper(callback);
                sendQuest(quest, answerCallback, 0);
            }

            if(!listenCache.isAllFalse())
            {
                DoneLambdaCallback callback1 = (errorCode, message) -> System.out.println("send setListen callback code " + errorCode);
                try {
                    quest = genBasicQuest("setlisten");
                } catch (Exception ex) {
                    ErrorRecorder.record("Generate setlisten message sign exception.", ex);
                    callback1.done(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate setlisten message sign exception.");
                    return;
                }
                quest.param("p2p", listenCache.p2p);
                quest.param("group", listenCache.group);
                quest.param("room", listenCache.room);
                quest.param("ev", listenCache.event);
                AnswerCallback answerCallback = new FPNNDoneLambdaCallbackWrapper(callback1);
                sendQuest(quest, answerCallback, 0);
            }
            listenCache.clear();
        }

    }

    // set rtm user connect or close callback
    public void setRTMClientConnectedCallback(RTMClientConnectCallback cb) {
        RTMConnectCallBack callBack = new RTMConnectCallBack(this, cb);
        setConnectedCallback(callBack);
    }

    public void setRTMClientWillCloseCallback(RTMClientWillCloseCallback cb) {
        RTMWillCloseCallback callback = new RTMWillCloseCallback(this, cb);
        setWillCloseCallback(callback);
    }

    public void setRTMClientHasClosedCallback(RTMClientHasClosedCallback cb) {
        RTMHasClosedCallback callback = new RTMHasClosedCallback(this, cb);
        setHasClosedCallback(callback);
    }

    //-----------------------------------------------------//
    //--      Internal Undocumented Public APIs          --//
    //-----------------------------------------------------//
    public void addRTMListenCache(Set<Long> gids, Set<Long> rids, Set<Long> uids, Set<String> events) {
        synchronized (cacheLocker){
            if (gids != null && !gids.isEmpty()) {
                listenCache.addGids(gids);
            }
            if (rids != null && !rids.isEmpty()) {
                listenCache.addRids(rids);
            }
            if (uids != null && !uids.isEmpty()) {
                listenCache.addUids(uids);
            }
            if (events != null && !events.isEmpty()) {
                listenCache.addEvents(events);
            }
        }
    }

    public void removeRTMListenCache(Set<Long> gids, Set<Long> rids, Set<Long> uids, Set<String> events) {
        synchronized (cacheLocker){
            if (gids != null && !gids.isEmpty()) {
                listenCache.removeGids(gids);
            }
            if (rids != null && !rids.isEmpty()) {
                listenCache.removeRids(rids);
            }
            if (uids != null && !uids.isEmpty()) {
                listenCache.removeUids(uids);
            }
            if (events != null && !events.isEmpty()) {
                listenCache.removeEvents(events);
            }
        }
    }

    public void setRTMListenCache(Set<Long> gids, Set<Long> rids, Set<Long> uids, Set<String> events) {
        synchronized (cacheLocker){
            if (gids != null && !gids.isEmpty()) {
                listenCache.setGids(gids);
            }
            if (rids != null && !rids.isEmpty()) {
                listenCache.setRids(rids);
            }
            if (uids != null && !uids.isEmpty()) {
                listenCache.setUids(uids);
            }
            if (events != null && !events.isEmpty()) {
                listenCache.setEvents(events);
            }
        }
    }

    public void setRTMListenCache(boolean allP2P, boolean allGroups, boolean allRooms, boolean allEvents) {
        synchronized (cacheLocker){
            listenCache.setGids(allP2P);
            listenCache.setRids(allGroups);
            listenCache.setUids(allRooms);
            listenCache.setEvents(allEvents);
        }
    }

    public RTMServerClientBase getCoreClient() {
        return this;
    }

    public long genMid() {
        return MidGenerator.gen();
    }

    //-----------------------------------------------------//
    //--                  Configure APIs                 --//
    //-----------------------------------------------------//
    /*
        !!! Most Configure APIs please refer class com.fpnn.sdk.TCPClient. !!!
     */

    public static void configureForMultipleSynchronousConcurrentAPIs(int taskThreadCount) {
        ClientEngine.setMaxThreadInTaskPool(taskThreadCount);
    }

    public static void configureForMultipleSynchronousConcurrentAPIs() {
        configureForMultipleSynchronousConcurrentAPIs(32);
    }

    public static boolean isAutoCleanup() {
        return ClientEngine.isAutoStop();
    }

    public static void setAutoCleanup(boolean auto) {
        ClientEngine.setAutoStop(auto);
    }

    public static void SDKCleanup() {
        ClientEngine.stop();
        RTMResourceCenter.close();
    }
}
