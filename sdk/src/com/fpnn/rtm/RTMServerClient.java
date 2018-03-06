package com.fpnn.rtm;

import com.fpnn.sdk.*;
import com.fpnn.sdk.proto.Answer;
import com.fpnn.sdk.proto.Quest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.Formatter;
import java.util.Set;

public class RTMServerClient extends TCPClient {

    private int pid;
    private String basicToken;

    public RTMServerClient(int pid, String secretKey, String host, int port) {
        super(host, port);
        this.pid = pid;
        basicToken = Integer.toString(pid) + ":" + secretKey + ":";
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

    public static void setAtuoCleanup(boolean auto) {
        ClientEngine.setAutoStop(auto);
    }

    public static void SDKCleanup() {
        ClientEngine.stop();
        RTMResourceCenter.close();
    }

    //-----------------------------------------------------//
    //--                  Private APIs                   --//
    //-----------------------------------------------------//
    private static String bytesToHexString(byte[] bytes, boolean isLowerCase) {
        String from = isLowerCase ? "%02x" : "%02X";
        StringBuilder sb = new StringBuilder(bytes.length * 2);

        Formatter formatter = new Formatter(sb);
        for (byte b : bytes) {
            formatter.format(from, b);
        }

        return sb.toString();
    }

    //------------------------[ For Message APIs ]----------------------------//
    private static class MidGenerator {

        static private long count = 0;

        static public synchronized long gen() {
            if (count == 0)
                count = System.currentTimeMillis() % 1000;

            return ++count;
        }
    }

    private long genMessageSlat() {
        long curr = System.currentTimeMillis();
        long a = curr >> 32;
        long b = (curr & 0xffff) << 32;
        long c = a | b;
        return (curr ^ c);
    }

    private String genMessageSign(long slat) throws GeneralSecurityException, IOException {
        String token = basicToken + Long.toString(slat);
        byte[] tokenString = token.getBytes("UTF-8");

        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(tokenString);
        byte[] md5Binary = md5.digest();
        return bytesToHexString(md5Binary, false);
    }

    //------------------------[ For Files APIs ]----------------------------//
    private String buildFileAttrs(String token, byte[] fileContent, String filename, String ext) throws GeneralSecurityException, IOException {

        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(fileContent);
        byte[] md5Binary = md5.digest();
        String md5Hex = bytesToHexString(md5Binary, true) + ":" + token;

        md5 = MessageDigest.getInstance("MD5");
        md5.update(md5Hex.getBytes("UTF-8"));
        md5Binary = md5.digest();
        md5Hex = bytesToHexString(md5Binary, true);

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"sign\":\"").append(md5Hex).append("\"");

        if (ext != null && ext.length() > 0)
            sb.append(", \"ext\":\"").append(ext).append("\"");

        if (filename != null && filename.length() > 0)
            sb.append(", \"filename\":\"").append(filename).append("\"");

        sb.append("}");

        return sb.toString();
    }

    private class FileInfo {
        byte[] fileContent;
        String filename;
        String filenameExtension;
    }
    private FileInfo readFileForSendAPI(String filePath) throws IOException {
        FileInfo info = new FileInfo();
        info.fileContent = Files.readAllBytes(Paths.get(filePath));

        File file = new File(filePath);
        info.filename = file.getName();
        int pos = info.filename.lastIndexOf(".");
        if (pos > 0)
            info.filenameExtension = info.filename.substring(pos + 1);
        else
            info.filenameExtension = null;

        return info;
    }

    //-----------------------------------------------------//
    //--              RTM Server Gate APIs               --//
    //-----------------------------------------------------//
    public interface DoneCallback {
        void done();
        void onException(int errorCode, String message);
    }

    private class FPNNDoneCallbackWrapper extends AnswerCallback {

        DoneCallback callback;

        FPNNDoneCallbackWrapper(DoneCallback callback) {
            this.callback = callback;
        }

        @Override
        public void onAnswer(Answer answer) {
            if (callback != null)
                callback.done();
        }

        @Override
        public void onException(Answer answer, int errorCode) {
            if (callback != null) {
                String info = null;
                if (answer != null)
                    info = (String) answer.get("ex");

                callback.onException(errorCode, info);
            }
        }
    }

    private Quest genBasicQuest(String method) throws GeneralSecurityException, IOException {
        long slat = genMessageSlat();
        String sign = genMessageSign(slat);

        Quest quest = new Quest(method);
        quest.param("pid", pid);
        quest.param("sign", sign);
        quest.param("salt", slat);

        return quest;
    }

    private Answer sendQuestAndCheckAnswer(Quest quest, int timeoutInseconds) throws RTMException, InterruptedException {
        Answer answer = sendQuest(quest, timeoutInseconds);
        if (answer.isErrorAnswer()) {
            int errorCode = answer.getErrorCode();
            String errorMessage = answer.getErrorMessage();
            throw new RTMException(errorCode, errorMessage);
        }

        return answer;
    }

    //-----------------[ sendmsg ]-----------------//
    public void sendMessage(long fromUid, long toUid, byte mType, String message, String attrs, DoneCallback callback, int timeoutInseconds) {

        Quest quest;
        try {
            quest = genBasicQuest("sendmsg");
        } catch (Exception e) {
            ErrorRecorder.record("Generate P2P message sign exception.", e);
            callback.onException(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate P2P message sign exception.");
            return;
        }

        quest.param("from", fromUid);
        quest.param("to", toUid);
        quest.param("mid", MidGenerator.gen());
        quest.param("mtype", mType);
        quest.param("msg", message);
        quest.param("attrs", attrs);

        AnswerCallback internalCallback = new FPNNDoneCallbackWrapper(callback);

        sendQuest(quest, internalCallback, timeoutInseconds);
    }

    public void sendMessage(long fromUid, long toUid, byte mType, String message, String attrs, DoneCallback callback) {
        sendMessage(fromUid, toUid, mType, message, attrs, callback, 0);
    }

    public void sendMessage(long fromUid, long toUid, byte mType, String message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        Quest quest = genBasicQuest("sendmsg");
        quest.param("from", fromUid);
        quest.param("to", toUid);
        quest.param("mid", MidGenerator.gen());
        quest.param("mtype", mType);
        quest.param("msg", message);
        quest.param("attrs", attrs);

        sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    public void sendMessage(long fromUid, long toUid, byte mType, String message, String attrs)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        sendMessage(fromUid, toUid, mType, message, attrs, 0);
    }

    //-----------------[ sendmsgs ]-----------------//

    public void sendMessages(long fromUid, Set<Long> toUids, byte mType, String message, String attrs, DoneCallback callback, int timeoutInseconds) {

        Quest quest;
        try {
            quest = genBasicQuest("sendmsgs");
        } catch (Exception e) {
            ErrorRecorder.record("Generate multi-receiver message sign exception.", e);
            callback.onException(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate multi-receiver message sign exception.");
            return;
        }

        quest.param("from", fromUid);
        quest.param("tos", toUids);
        quest.param("mid", MidGenerator.gen());
        quest.param("mtype", mType);
        quest.param("msg", message);
        quest.param("attrs", attrs);

        AnswerCallback internalCallback = new FPNNDoneCallbackWrapper(callback);

        sendQuest(quest, internalCallback, timeoutInseconds);
    }

    public void sendMessages(long fromUid, Set<Long> toUids, byte mType, String message, String attrs, DoneCallback callback) {
        sendMessages(fromUid, toUids, mType, message, attrs, callback, 0);
    }

    public void sendMessages(long fromUid, Set<Long> toUids, byte mType, String message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        Quest quest = genBasicQuest("sendmsgs");
        quest.param("from", fromUid);
        quest.param("tos", toUids);
        quest.param("mid", MidGenerator.gen());
        quest.param("mtype", mType);
        quest.param("msg", message);
        quest.param("attrs", attrs);

        sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    public void sendMessages(long fromUid, Set<Long> toUids, byte mType, String message, String attrs)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        sendMessages(fromUid, toUids, mType, message, attrs, 0);
    }

    //-----------------[ sendgroupmsg ]-----------------//

    public void sendGroupMessage(long fromUid, long groupId, byte mType, String message, String attrs, DoneCallback callback, int timeoutInseconds) {

        Quest quest;
        try {
            quest = genBasicQuest("sendgroupmsg");
        } catch (Exception e) {
            ErrorRecorder.record("Generate group message sign exception.", e);
            callback.onException(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate group message sign exception.");
            return;
        }

        quest.param("from", fromUid);
        quest.param("gid", groupId);
        quest.param("mid", MidGenerator.gen());
        quest.param("mtype", mType);
        quest.param("msg", message);
        quest.param("attrs", attrs);

        AnswerCallback internalCallback = new FPNNDoneCallbackWrapper(callback);

        sendQuest(quest, internalCallback, timeoutInseconds);
    }

    public void sendGroupMessage(long fromUid, long groupId, byte mType, String message, String attrs, DoneCallback callback) {
        sendGroupMessage(fromUid, groupId, mType, message, attrs, callback, 0);
    }

    public void sendGroupMessage(long fromUid, long groupId, byte mType, String message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        Quest quest = genBasicQuest("sendgroupmsg");
        quest.param("from", fromUid);
        quest.param("gid", groupId);
        quest.param("mid", MidGenerator.gen());
        quest.param("mtype", mType);
        quest.param("msg", message);
        quest.param("attrs", attrs);

        sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    public void sendGroupMessage(long fromUid, long groupId, byte mType, String message, String attrs)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        sendGroupMessage(fromUid, groupId, mType, message, attrs, 0);
    }

    //-----------------[ sendroommsg ]-----------------//

    public void sendRoomMessage(long fromUid, long roomId, byte mType, String message, String attrs, DoneCallback callback, int timeoutInseconds) {

        Quest quest;
        try {
            quest = genBasicQuest("sendroommsg");
        } catch (Exception e) {
            ErrorRecorder.record("Generate room message sign exception.", e);
            callback.onException(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate room message sign exception.");
            return;
        }

        quest.param("from", fromUid);
        quest.param("rid", roomId);
        quest.param("mid", MidGenerator.gen());
        quest.param("mtype", mType);
        quest.param("msg", message);
        quest.param("attrs", attrs);

        AnswerCallback internalCallback = new FPNNDoneCallbackWrapper(callback);

        sendQuest(quest, internalCallback, timeoutInseconds);
    }

    public void sendRoomMessage(long fromUid, long roomId, byte mType, String message, String attrs, DoneCallback callback) {
        sendRoomMessage(fromUid, roomId, mType, message, attrs, callback, 0);
    }

    public void sendRoomMessage(long fromUid, long roomId, byte mType, String message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        Quest quest = genBasicQuest("sendroommsg");
        quest.param("from", fromUid);
        quest.param("rid", roomId);
        quest.param("mid", MidGenerator.gen());
        quest.param("mtype", mType);
        quest.param("msg", message);
        quest.param("attrs", attrs);

        sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    public void sendRoomMessage(long fromUid, long roomId, byte mType, String message, String attrs)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        sendRoomMessage(fromUid, roomId, mType, message, attrs, 0);
    }

    //-----------------[ broadcastmsg ]-----------------//

    public void sendBroadcastMessage(long fromUid, byte mType, String message, String attrs, DoneCallback callback, int timeoutInseconds) {

        Quest quest;
        try {
            quest = genBasicQuest("broadcastmsg");
        } catch (Exception e) {
            ErrorRecorder.record("Generate broadcast message sign exception.", e);
            callback.onException(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate broadcast message sign exception.");
            return;
        }

        quest.param("from", fromUid);
        quest.param("mid", MidGenerator.gen());
        quest.param("mtype", mType);
        quest.param("msg", message);
        quest.param("attrs", attrs);

        AnswerCallback internalCallback = new FPNNDoneCallbackWrapper(callback);

        sendQuest(quest, internalCallback, timeoutInseconds);
    }

    public void sendBroadcastMessage(long fromUid, byte mType, String message, String attrs, DoneCallback callback) {
        sendBroadcastMessage(fromUid, mType, message, attrs, callback, 0);
    }

    public void sendBroadcastMessage(long fromUid, byte mType, String message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        Quest quest = genBasicQuest("broadcastmsg");
        quest.param("from", fromUid);
        quest.param("mid", MidGenerator.gen());
        quest.param("mtype", mType);
        quest.param("msg", message);
        quest.param("attrs", attrs);

        sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    public void sendBroadcastMessage(long fromUid, byte mType, String message, String attrs)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        sendBroadcastMessage(fromUid, mType, message, attrs, 0);
    }

    //-----------------[ addfriends ]-----------------//

    public void addFriends(long uid, Set<Long> friendUids, DoneCallback callback, int timeoutInseconds) {

        Quest quest;
        try {
            quest = genBasicQuest("addfriends");
        } catch (Exception e) {
            ErrorRecorder.record("Generate adding friends sign exception.", e);
            callback.onException(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate adding friends sign exception.");
            return;
        }

        quest.param("uid", uid);
        quest.param("friends", friendUids);

        AnswerCallback internalCallback = new FPNNDoneCallbackWrapper(callback);

        sendQuest(quest, internalCallback, timeoutInseconds);
    }

    public void addFriends(long uid, Set<Long> friendUids, DoneCallback callback) {
        addFriends(uid, friendUids, callback, 0);
    }

    public void addFriends(long uid, Set<Long> friendUids, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        Quest quest = genBasicQuest("addfriends");
        quest.param("uid", uid);
        quest.param("friends", friendUids);

        sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    public void addFriends(long uid, Set<Long> friendUids)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        addFriends(uid, friendUids, 0);
    }

    //-----------------[ delfriends ]-----------------//

    public void deleteFriends(long uid, Set<Long> friendUids, DoneCallback callback, int timeoutInseconds) {

        Quest quest;
        try {
            quest = genBasicQuest("delfriends");
        } catch (Exception e) {
            ErrorRecorder.record("Generate deleting friends sign exception.", e);
            callback.onException(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate deleting friends sign exception.");
            return;
        }

        quest.param("uid", uid);
        quest.param("friends", friendUids);

        AnswerCallback internalCallback = new FPNNDoneCallbackWrapper(callback);

        sendQuest(quest, internalCallback, timeoutInseconds);
    }

    public void deleteFriends(long uid, Set<Long> friendUids, DoneCallback callback) {
        deleteFriends(uid, friendUids, callback, 0);
    }

    public void deleteFriends(long uid, Set<Long> friendUids, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        Quest quest = genBasicQuest("delfriends");
        quest.param("uid", uid);
        quest.param("friends", friendUids);

        sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    public void deleteFriends(long uid, Set<Long> friendUids)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        deleteFriends(uid, friendUids, 0);
    }

    //-----------------[ getfriends ]-----------------//

    public interface GetFriendsCallback {
        void done(Set<Long> friendUids);
        void onException(int errorCode, String message);
    }

    public void getFriends(long uid, GetFriendsCallback callback, int timeoutInseconds) {

        Quest quest;
        try {
            quest = genBasicQuest("getfriends");
        } catch (Exception e) {
            ErrorRecorder.record("Generate getting friends sign exception.", e);
            callback.onException(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate getting friends sign exception.");
            return;
        }

        quest.param("uid", uid);

        AnswerCallback internalCallback = new AnswerCallback() {
            @Override
            @SuppressWarnings("unchecked")
            public void onAnswer(Answer answer) {
                callback.done((Set<Long>) answer.get("uids"));
            }

            @Override
            public void onException(Answer answer, int errorCode) {
                String info = null;
                if (answer != null)
                    info = (String) answer.get("ex");
                callback.onException(errorCode, info);
            }
        };

        sendQuest(quest, internalCallback, timeoutInseconds);
    }

    public void getFriends(long uid, GetFriendsCallback callback) {
        getFriends(uid, callback, 0);
    }

    @SuppressWarnings("unchecked")
    public Set<Long> getFriends(long uid, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        Quest quest = genBasicQuest("getfriends");
        quest.param("uid", uid);

        Answer answer = sendQuestAndCheckAnswer(quest, timeoutInseconds);
        return (Set<Long>) answer.get("uids");
    }

    public Set<Long> getFriends(long uid)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return getFriends(uid, 0);
    }

    //-----------------[ isfriend ]-----------------//

    public interface IsFriendCallback {
        void done(boolean ok);
        void onException(int errorCode, String message);
    }

    public void isFriend(long uid, long friendUid, IsFriendCallback callback, int timeoutInseconds) {

        Quest quest;
        try {
            quest = genBasicQuest("isfriend");
        } catch (Exception e) {
            ErrorRecorder.record("Generate sign for isfriend interface exception.", e);
            callback.onException(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate sign for isfriend interface exception.");
            return;
        }

        quest.param("uid", uid);
        quest.param("fuid", friendUid);

        AnswerCallback internalCallback = new AnswerCallback() {
            @Override
            @SuppressWarnings("unchecked")
            public void onAnswer(Answer answer) {
                callback.done((boolean) answer.get("ok"));
            }

            @Override
            public void onException(Answer answer, int errorCode) {
                String info = null;
                if (answer != null)
                    info = (String) answer.get("ex");
                callback.onException(errorCode, info);
            }
        };

        sendQuest(quest, internalCallback, timeoutInseconds);
    }

    public void isFriend(long uid, long friendUid, IsFriendCallback callback) {
        isFriend(uid, friendUid, callback, 0);
    }

    @SuppressWarnings("unchecked")
    public boolean isFriend(long uid, long friendUid, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        Quest quest = genBasicQuest("isfriend");
        quest.param("uid", uid);
        quest.param("fuid", friendUid);

        Answer answer = sendQuestAndCheckAnswer(quest, timeoutInseconds);
        return (boolean) answer.get("ok");
    }

    public boolean isFriend(long uid, long friendUid)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return isFriend(uid, friendUid, 0);
    }

    //-----------------[ isfriends ]-----------------//

    public interface IsFriendsCallback {
        void done(Set<Long> friendUids);
        void onException(int errorCode, String message);
    }

    public void isFriends(long uid, Set<Long> friendUids, IsFriendsCallback callback, int timeoutInseconds) {

        Quest quest;
        try {
            quest = genBasicQuest("isfriends");
        } catch (Exception e) {
            ErrorRecorder.record("Generate sign for isfriends interface exception.", e);
            callback.onException(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate sign for isfriends interface exception.");
            return;
        }

        quest.param("uid", uid);
        quest.param("fuids", friendUids);

        AnswerCallback internalCallback = new AnswerCallback() {
            @Override
            @SuppressWarnings("unchecked")
            public void onAnswer(Answer answer) {
                callback.done((Set<Long>) answer.get("fuids"));
            }

            @Override
            public void onException(Answer answer, int errorCode) {
                String info = null;
                if (answer != null)
                    info = (String) answer.get("ex");
                callback.onException(errorCode, info);
            }
        };

        sendQuest(quest, internalCallback, timeoutInseconds);
    }

    public void isFriends(long uid, Set<Long> friendUids, IsFriendsCallback callback) {
        isFriends(uid, friendUids, callback, 0);
    }

    @SuppressWarnings("unchecked")
    public Set<Long> isFriends(long uid, Set<Long> friendUids, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        Quest quest = genBasicQuest("isfriends");
        quest.param("uid", uid);
        quest.param("fuids", friendUids);

        Answer answer = sendQuestAndCheckAnswer(quest, timeoutInseconds);
        return (Set<Long>) answer.get("fuids");
    }

    public Set<Long> isFriends(long uid, Set<Long> friendUids)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return isFriends(uid, friendUids, 0);
    }

    //-----------------[ addgroupmembers ]-----------------//

    public void addGroupMembers(long groupId, Set<Long> uids, DoneCallback callback, int timeoutInseconds) {

        Quest quest;
        try {
            quest = genBasicQuest("addgroupmembers");
        } catch (Exception e) {
            ErrorRecorder.record("Generate adding group members sign exception.", e);
            callback.onException(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate adding group members sign exception.");
            return;
        }

        quest.param("gid", groupId);
        quest.param("uids", uids);

        AnswerCallback internalCallback = new FPNNDoneCallbackWrapper(callback);

        sendQuest(quest, internalCallback, timeoutInseconds);
    }

    public void addGroupMembers(long groupId, Set<Long> uids, DoneCallback callback) {
        addGroupMembers(groupId, uids, callback, 0);
    }

    public void addGroupMembers(long groupId, Set<Long> uids, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        Quest quest = genBasicQuest("addgroupmembers");
        quest.param("gid", groupId);
        quest.param("uids", uids);

        sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    public void addGroupMembers(long groupId, Set<Long> uids)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        addGroupMembers(groupId, uids, 0);
    }

    //-----------------[ delgroupmembers ]-----------------//

    public void deleteGroupMembers(long groupId, Set<Long> uids, DoneCallback callback, int timeoutInseconds) {

        Quest quest;
        try {
            quest = genBasicQuest("delgroupmembers");
        } catch (Exception e) {
            ErrorRecorder.record("Generate deleting group members sign exception.", e);
            callback.onException(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate deleting group members sign exception.");
            return;
        }

        quest.param("gid", groupId);
        quest.param("uids", uids);

        AnswerCallback internalCallback = new FPNNDoneCallbackWrapper(callback);

        sendQuest(quest, internalCallback, timeoutInseconds);
    }

    public void deleteGroupMembers(long groupId, Set<Long> uids, DoneCallback callback) {
        deleteGroupMembers(groupId, uids, callback, 0);
    }

    public void deleteGroupMembers(long groupId, Set<Long> uids, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        Quest quest = genBasicQuest("delgroupmembers");
        quest.param("gid", groupId);
        quest.param("uids", uids);

        sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    public void deleteGroupMembers(long groupId, Set<Long> uids)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        deleteGroupMembers(groupId, uids, 0);
    }

    //-----------------[ delgroup ]-----------------//

    public void deleteGroup(long groupId, DoneCallback callback, int timeoutInseconds) {

        Quest quest;
        try {
            quest = genBasicQuest("delgroup");
        } catch (Exception e) {
            ErrorRecorder.record("Generate deleting group sign exception.", e);
            callback.onException(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate deleting group sign exception.");
            return;
        }

        quest.param("gid", groupId);

        AnswerCallback internalCallback = new FPNNDoneCallbackWrapper(callback);

        sendQuest(quest, internalCallback, timeoutInseconds);
    }

    public void deleteGroup(long groupId, DoneCallback callback) {
        deleteGroup(groupId, callback, 0);
    }

    public void deleteGroup(long groupId, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        Quest quest = genBasicQuest("delgroup");
        quest.param("gid", groupId);

        sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    public void deleteGroup(long groupId)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        deleteGroup(groupId, 0);
    }

    //-----------------[ getgroupmembers ]-----------------//

    public interface GetGroupMembersCallback {
        void done(Set<Long> uids);
        void onException(int errorCode, String message);
    }

    public void getGroupMembers(long groupId, GetGroupMembersCallback callback, int timeoutInseconds) {

        Quest quest;
        try {
            quest = genBasicQuest("getgroupmembers");
        } catch (Exception e) {
            ErrorRecorder.record("Generate sign for getgroupmembers interface exception.", e);
            callback.onException(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate sign for getgroupmembers interface exception.");
            return;
        }

        quest.param("gid", groupId);

        AnswerCallback internalCallback = new AnswerCallback() {
            @Override
            @SuppressWarnings("unchecked")
            public void onAnswer(Answer answer) {
                callback.done((Set<Long>) answer.get("uids"));
            }

            @Override
            public void onException(Answer answer, int errorCode) {
                String info = null;
                if (answer != null)
                    info = (String) answer.get("ex");
                callback.onException(errorCode, info);
            }
        };

        sendQuest(quest, internalCallback, timeoutInseconds);
    }

    public void getGroupMembers(long groupId, GetGroupMembersCallback callback) {
        getGroupMembers(groupId, callback, 0);
    }

    @SuppressWarnings("unchecked")
    public Set<Long> getGroupMembers(long groupId, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        Quest quest = genBasicQuest("getgroupmembers");
        quest.param("gid", groupId);

        Answer answer = sendQuestAndCheckAnswer(quest, timeoutInseconds);
        return (Set<Long>) answer.get("uids");
    }

    public Set<Long> getGroupMembers(long groupId)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return getGroupMembers(groupId, 0);
    }

    //-----------------[ isgroupmember ]-----------------//

    public interface IsGroupMemberCallback {
        void done(boolean ok);
        void onException(int errorCode, String message);
    }

    public void isGroupMember(long groupId, long uid, IsGroupMemberCallback callback, int timeoutInseconds) {

        Quest quest;
        try {
            quest = genBasicQuest("isgroupmember");
        } catch (Exception e) {
            ErrorRecorder.record("Generate sign for isgroupmember interface exception.", e);
            callback.onException(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate sign for isgroupmember interface exception.");
            return;
        }

        quest.param("uid", uid);
        quest.param("gid", groupId);

        AnswerCallback internalCallback = new AnswerCallback() {
            @Override
            @SuppressWarnings("unchecked")
            public void onAnswer(Answer answer) {
                callback.done((boolean) answer.get("ok"));
            }

            @Override
            public void onException(Answer answer, int errorCode) {
                String info = null;
                if (answer != null)
                    info = (String) answer.get("ex");
                callback.onException(errorCode, info);
            }
        };

        sendQuest(quest, internalCallback, timeoutInseconds);
    }

    public void isGroupMember(long groupId, long uid, IsGroupMemberCallback callback) {
        isGroupMember(groupId, uid, callback, 0);
    }

    @SuppressWarnings("unchecked")
    public boolean isGroupMember(long groupId, long uid, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        Quest quest = genBasicQuest("isgroupmember");
        quest.param("uid", uid);
        quest.param("gid", groupId);

        Answer answer = sendQuestAndCheckAnswer(quest, timeoutInseconds);
        return (boolean) answer.get("ok");
    }

    public boolean isGroupMember(long groupId, long uid)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return isGroupMember(groupId, uid, 0);
    }

    //-----------------[ getusergroups ]-----------------//

    public interface GetUserGroupsCallback {
        void done(Set<Long> groupIds);
        void onException(int errorCode, String message);
    }

    public void getUserGroups(long uid, GetUserGroupsCallback callback, int timeoutInseconds) {

        Quest quest;
        try {
            quest = genBasicQuest("getusergroups");
        } catch (Exception e) {
            ErrorRecorder.record("Generate sign for getusergroups interface exception.", e);
            callback.onException(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate sign for getusergroups interface exception.");
            return;
        }

        quest.param("uid", uid);

        AnswerCallback internalCallback = new AnswerCallback() {
            @Override
            @SuppressWarnings("unchecked")
            public void onAnswer(Answer answer) {
                callback.done((Set<Long>) answer.get("gids"));
            }

            @Override
            public void onException(Answer answer, int errorCode) {
                String info = null;
                if (answer != null)
                    info = (String) answer.get("ex");
                callback.onException(errorCode, info);
            }
        };

        sendQuest(quest, internalCallback, timeoutInseconds);
    }

    public void getUserGroups(long uid, GetUserGroupsCallback callback) {
        getUserGroups(uid, callback, 0);
    }

    @SuppressWarnings("unchecked")
    public Set<Long> getUserGroups(long uid, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        Quest quest = genBasicQuest("getusergroups");
        quest.param("uid", uid);

        Answer answer = sendQuestAndCheckAnswer(quest, timeoutInseconds);
        return (Set<Long>) answer.get("gids");
    }

    public Set<Long> getUserGroups(long uid)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return getUserGroups(uid, 0);
    }

    //-----------------[ gettoken ]-----------------//

    public interface GetTokenCallback {
        void done(String token);
        void onException(int errorCode, String message);
    }

    public void getToken(long uid, GetTokenCallback callback, int timeoutInseconds) {

        Quest quest;
        try {
            quest = genBasicQuest("gettoken");
        } catch (Exception e) {
            ErrorRecorder.record("Generate sign for gettoken interface exception.", e);
            callback.onException(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate sign for gettoken interface exception.");
            return;
        }

        quest.param("uid", uid);

        AnswerCallback internalCallback = new AnswerCallback() {
            @Override
            @SuppressWarnings("unchecked")
            public void onAnswer(Answer answer) {
                callback.done((String) answer.get("token"));
            }

            @Override
            public void onException(Answer answer, int errorCode) {
                String info = null;
                if (answer != null)
                    info = (String) answer.get("ex");
                callback.onException(errorCode, info);
            }
        };

        sendQuest(quest, internalCallback, timeoutInseconds);
    }

    public void getToken(long uid, GetTokenCallback callback) {
        getToken(uid, callback, 0);
    }

    @SuppressWarnings("unchecked")
    public String getToken(long uid, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        Quest quest = genBasicQuest("gettoken");
        quest.param("uid", uid);

        Answer answer = sendQuestAndCheckAnswer(quest, timeoutInseconds);
        return (String) answer.get("token");
    }

    public String getToken(long uid)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return getToken(uid, 0);
    }

    //-----------------[ getonlineusers ]-----------------//

    public interface GetOnlineUsersCallback {
        void done(Set<Long> onlineUids);
        void onException(int errorCode, String message);
    }

    public void getOnlineUsers(Set<Long> uids, GetOnlineUsersCallback callback, int timeoutInseconds) {

        Quest quest;
        try {
            quest = genBasicQuest("getonlineusers");
        } catch (Exception e) {
            ErrorRecorder.record("Generate sign for getonlineusers interface exception.", e);
            callback.onException(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate sign for getonlineusers interface exception.");
            return;
        }

        quest.param("uids", uids);

        AnswerCallback internalCallback = new AnswerCallback() {
            @Override
            @SuppressWarnings("unchecked")
            public void onAnswer(Answer answer) {
                callback.done((Set<Long>) answer.get("uids"));
            }

            @Override
            public void onException(Answer answer, int errorCode) {
                String info = null;
                if (answer != null)
                    info = (String) answer.get("ex");
                callback.onException(errorCode, info);
            }
        };

        sendQuest(quest, internalCallback, timeoutInseconds);
    }

    public void getOnlineUsers(Set<Long> uids, GetOnlineUsersCallback callback) {
        getOnlineUsers(uids, callback, 0);
    }

    @SuppressWarnings("unchecked")
    public Set<Long> getOnlineUsers(Set<Long> uids, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        Quest quest = genBasicQuest("getonlineusers");
        quest.param("uids", uids);

        Answer answer = sendQuestAndCheckAnswer(quest, timeoutInseconds);
        return (Set<Long>) answer.get("uids");
    }

    public Set<Long> getOnlineUsers(Set<Long> uids)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return getOnlineUsers(uids, 0);
    }

    //-----------------[ addgroupban ]-----------------//

    public void addGroupBan(long groupId, long uid, int banSeconds, DoneCallback callback, int timeoutInseconds) {

        Quest quest;
        try {
            quest = genBasicQuest("addgroupban");
        } catch (Exception e) {
            ErrorRecorder.record("Generate sign for addgroupban interface exception.", e);
            callback.onException(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate sign for addgroupban interface exception.");
            return;
        }

        quest.param("gid", groupId);
        quest.param("uid", uid);
        quest.param("btime", banSeconds);

        AnswerCallback internalCallback = new FPNNDoneCallbackWrapper(callback);

        sendQuest(quest, internalCallback, timeoutInseconds);
    }

    public void addGroupBan(long groupId, long uid, int banSeconds, DoneCallback callback) {
        addGroupBan(groupId, uid, banSeconds, callback, 0);
    }

    public void addGroupBan(long groupId, long uid, int banSeconds, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        Quest quest = genBasicQuest("addgroupban");
        quest.param("gid", groupId);
        quest.param("uid", uid);
        quest.param("btime", banSeconds);

        sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    public void addGroupBan(long groupId, long uid, int banSeconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        addGroupBan(groupId, uid, banSeconds, 0);
    }

    //-----------------[ removegroupban ]-----------------//

    public void removeGroupBan(long groupId, long uid, DoneCallback callback, int timeoutInseconds) {

        Quest quest;
        try {
            quest = genBasicQuest("removegroupban");
        } catch (Exception e) {
            ErrorRecorder.record("Generate sign for removegroupban interface exception.", e);
            callback.onException(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate sign for removegroupban interface exception.");
            return;
        }

        quest.param("gid", groupId);
        quest.param("uid", uid);

        AnswerCallback internalCallback = new FPNNDoneCallbackWrapper(callback);

        sendQuest(quest, internalCallback, timeoutInseconds);
    }

    public void removeGroupBan(long groupId, long uid, DoneCallback callback) {
        removeGroupBan(groupId, uid, callback, 0);
    }

    public void removeGroupBan(long groupId, long uid, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        Quest quest = genBasicQuest("removegroupban");
        quest.param("gid", groupId);
        quest.param("uid", uid);

        sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    public void removeGroupBan(long groupId, long uid)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        removeGroupBan(groupId, uid, 0);
    }

    //-----------------[ addroomban ]-----------------//

    public void addRoomBan(long roomId, long uid, int banSeconds, DoneCallback callback, int timeoutInseconds) {

        Quest quest;
        try {
            quest = genBasicQuest("addroomban");
        } catch (Exception e) {
            ErrorRecorder.record("Generate sign for addroomban interface exception.", e);
            callback.onException(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate sign for addroomban interface exception.");
            return;
        }

        quest.param("rid", roomId);
        quest.param("uid", uid);
        quest.param("btime", banSeconds);

        AnswerCallback internalCallback = new FPNNDoneCallbackWrapper(callback);

        sendQuest(quest, internalCallback, timeoutInseconds);
    }

    public void addRoomBan(long roomId, long uid, int banSeconds, DoneCallback callback) {
        addRoomBan(roomId, uid, banSeconds, callback, 0);
    }

    public void addRoomBan(long roomId, long uid, int banSeconds, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        Quest quest = genBasicQuest("addroomban");
        quest.param("rid", roomId);
        quest.param("uid", uid);
        quest.param("btime", banSeconds);

        sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    public void addRoomBan(long roomId, long uid, int banSeconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        addRoomBan(roomId, uid, banSeconds, 0);
    }

    //-----------------[ removeroomban ]-----------------//

    public void removeRoomBan(long roomId, long uid, DoneCallback callback, int timeoutInseconds) {

        Quest quest;
        try {
            quest = genBasicQuest("removeroomban");
        } catch (Exception e) {
            ErrorRecorder.record("Generate sign for removeroomban interface exception.", e);
            callback.onException(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate sign for removeroomban interface exception.");
            return;
        }

        quest.param("rid", roomId);
        quest.param("uid", uid);

        AnswerCallback internalCallback = new FPNNDoneCallbackWrapper(callback);

        sendQuest(quest, internalCallback, timeoutInseconds);
    }

    public void removeRoomBan(long roomId, long uid, DoneCallback callback) {
        removeRoomBan(roomId, uid, callback, 0);
    }

    public void removeRoomBan(long roomId, long uid, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        Quest quest = genBasicQuest("removeroomban");
        quest.param("rid", roomId);
        quest.param("uid", uid);

        sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    public void removeRoomBan(long roomId, long uid)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        removeRoomBan(roomId, uid, 0);
    }

    //-----------------[ addprojectblack ]-----------------//

    public void addProjectBlack(long uid, int banSeconds, DoneCallback callback, int timeoutInseconds) {

        Quest quest;
        try {
            quest = genBasicQuest("addprojectblack");
        } catch (Exception e) {
            ErrorRecorder.record("Generate sign for addprojectblack interface exception.", e);
            callback.onException(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate sign for addprojectblack interface exception.");
            return;
        }

        quest.param("uid", uid);
        quest.param("btime", banSeconds);

        AnswerCallback internalCallback = new FPNNDoneCallbackWrapper(callback);

        sendQuest(quest, internalCallback, timeoutInseconds);
    }

    public void addProjectBlack(long uid, int banSeconds, DoneCallback callback) {
        addProjectBlack(uid, banSeconds, callback, 0);
    }

    public void addProjectBlack(long uid, int banSeconds, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        Quest quest = genBasicQuest("addprojectblack");
        quest.param("uid", uid);
        quest.param("btime", banSeconds);

        sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    public void addProjectBlack(long uid, int banSeconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        addProjectBlack(uid, banSeconds, 0);
    }

    //-----------------[ removeprojectblack ]-----------------//

    public void removeProjectBlack(long uid, DoneCallback callback, int timeoutInseconds) {

        Quest quest;
        try {
            quest = genBasicQuest("removeprojectblack");
        } catch (Exception e) {
            ErrorRecorder.record("Generate sign for removeprojectblack interface exception.", e);
            callback.onException(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate sign for removeprojectblack interface exception.");
            return;
        }

        quest.param("uid", uid);

        AnswerCallback internalCallback = new FPNNDoneCallbackWrapper(callback);

        sendQuest(quest, internalCallback, timeoutInseconds);
    }

    public void removeProjectBlack(long uid, DoneCallback callback) {
        removeProjectBlack(uid, callback, 0);
    }

    public void removeProjectBlack(long uid, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        Quest quest = genBasicQuest("removeprojectblack");
        quest.param("uid", uid);

        sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    public void removeProjectBlack(long uid)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        removeProjectBlack(uid, 0);
    }

    //-----------------[ isbanofgroup ]-----------------//

    public interface IsBanOfGroupCallback {
        void done(boolean ok);
        void onException(int errorCode, String message);
    }

    public void isBanOfGroup(long groupId, long uid, IsBanOfGroupCallback callback, int timeoutInseconds) {

        Quest quest;
        try {
            quest = genBasicQuest("isbanofgroup");
        } catch (Exception e) {
            ErrorRecorder.record("Generate sign for isbanofgroup interface exception.", e);
            callback.onException(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate sign for isbanofgroup interface exception.");
            return;
        }

        quest.param("uid", uid);
        quest.param("gid", groupId);

        AnswerCallback internalCallback = new AnswerCallback() {
            @Override
            @SuppressWarnings("unchecked")
            public void onAnswer(Answer answer) {
                callback.done((boolean) answer.get("ok"));
            }

            @Override
            public void onException(Answer answer, int errorCode) {
                String info = null;
                if (answer != null)
                    info = (String) answer.get("ex");
                callback.onException(errorCode, info);
            }
        };

        sendQuest(quest, internalCallback, timeoutInseconds);
    }

    public void isBanOfGroup(long groupId, long uid, IsBanOfGroupCallback callback) {
        isBanOfGroup(groupId, uid, callback, 0);
    }

    @SuppressWarnings("unchecked")
    public boolean isBanOfGroup(long groupId, long uid, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        Quest quest = genBasicQuest("isbanofgroup");
        quest.param("uid", uid);
        quest.param("gid", groupId);

        Answer answer = sendQuestAndCheckAnswer(quest, timeoutInseconds);
        return (boolean) answer.get("ok");
    }

    public boolean isBanOfGroup(long groupId, long uid)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return isBanOfGroup(groupId, uid, 0);
    }

    //-----------------[ isbanofroom ]-----------------//

    public interface IsBanOfRoomCallback {
        void done(boolean ok);
        void onException(int errorCode, String message);
    }

    public void isBanOfRoom(long roomId, long uid, IsBanOfRoomCallback callback, int timeoutInseconds) {

        Quest quest;
        try {
            quest = genBasicQuest("isbanofroom");
        } catch (Exception e) {
            ErrorRecorder.record("Generate sign for isbanofroom interface exception.", e);
            callback.onException(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate sign for isbanofroom interface exception.");
            return;
        }

        quest.param("uid", uid);
        quest.param("rid", roomId);

        AnswerCallback internalCallback = new AnswerCallback() {
            @Override
            @SuppressWarnings("unchecked")
            public void onAnswer(Answer answer) {
                callback.done((boolean) answer.get("ok"));
            }

            @Override
            public void onException(Answer answer, int errorCode) {
                String info = null;
                if (answer != null)
                    info = (String) answer.get("ex");
                callback.onException(errorCode, info);
            }
        };

        sendQuest(quest, internalCallback, timeoutInseconds);
    }

    public void isBanOfRoom(long roomId, long uid, IsBanOfRoomCallback callback) {
        isBanOfRoom(roomId, uid, callback, 0);
    }

    @SuppressWarnings("unchecked")
    public boolean isBanOfRoom(long roomId, long uid, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        Quest quest = genBasicQuest("isbanofroom");
        quest.param("uid", uid);
        quest.param("rid", roomId);

        Answer answer = sendQuestAndCheckAnswer(quest, timeoutInseconds);
        return (boolean) answer.get("ok");
    }

    public boolean isBanOfRoom(long roomId, long uid)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return isBanOfRoom(roomId, uid, 0);
    }

    //-----------------[ isprojectblack ]-----------------//

    public interface IsProjectBlackCallback {
        void done(boolean ok);
        void onException(int errorCode, String message);
    }

    public void isProjectBlack(long uid, IsProjectBlackCallback callback, int timeoutInseconds) {

        Quest quest;
        try {
            quest = genBasicQuest("isprojectblack");
        } catch (Exception e) {
            ErrorRecorder.record("Generate sign for isprojectblack interface exception.", e);
            callback.onException(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate sign for isprojectblack interface exception.");
            return;
        }

        quest.param("uid", uid);

        AnswerCallback internalCallback = new AnswerCallback() {
            @Override
            @SuppressWarnings("unchecked")
            public void onAnswer(Answer answer) {
                callback.done((boolean) answer.get("ok"));
            }

            @Override
            public void onException(Answer answer, int errorCode) {
                String info = null;
                if (answer != null)
                    info = (String) answer.get("ex");
                callback.onException(errorCode, info);
            }
        };

        sendQuest(quest, internalCallback, timeoutInseconds);
    }

    public void isProjectBlack(long uid, IsProjectBlackCallback callback) {
        isProjectBlack(uid, callback, 0);
    }

    @SuppressWarnings("unchecked")
    public boolean isProjectBlack(long uid, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        Quest quest = genBasicQuest("isprojectblack");
        quest.param("uid", uid);

        Answer answer = sendQuestAndCheckAnswer(quest, timeoutInseconds);
        return (boolean) answer.get("ok");
    }

    public boolean isProjectBlack(long uid)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return isProjectBlack(uid, 0);
    }

    //-----------------[ setpushname ]-----------------//

    public void setPushName(long uid, String pushName, DoneCallback callback, int timeoutInseconds) {

        Quest quest;
        try {
            quest = genBasicQuest("setpushname");
        } catch (Exception e) {
            ErrorRecorder.record("Generate sign for setpushname interface exception.", e);
            callback.onException(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate sign for setpushname interface exception.");
            return;
        }

        quest.param("uid", uid);
        quest.param("pushname", pushName);

        AnswerCallback internalCallback = new FPNNDoneCallbackWrapper(callback);

        sendQuest(quest, internalCallback, timeoutInseconds);
    }

    public void setPushName(long uid, String pushName, DoneCallback callback) {
        setPushName(uid, pushName, callback, 0);
    }

    public void setPushName(long uid, String pushName, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        Quest quest = genBasicQuest("setpushname");
        quest.param("uid", uid);
        quest.param("pushname", pushName);

        sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    public void setPushName(long uid, String pushName)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        setPushName(uid, pushName, 0);
    }

    //-----------------[ getpushname ]-----------------//

    public interface GetPushNameCallback {
        void done(String pushName);
        void onException(int errorCode, String message);
    }

    public void getPushName(long uid, GetPushNameCallback callback, int timeoutInseconds) {

        Quest quest;
        try {
            quest = genBasicQuest("getpushname");
        } catch (Exception e) {
            ErrorRecorder.record("Generate sign for getpushname interface exception.", e);
            callback.onException(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate sign for getpushname interface exception.");
            return;
        }

        quest.param("uid", uid);

        AnswerCallback internalCallback = new AnswerCallback() {
            @Override
            @SuppressWarnings("unchecked")
            public void onAnswer(Answer answer) {
                callback.done((String) answer.get("pushname"));
            }

            @Override
            public void onException(Answer answer, int errorCode) {
                String info = null;
                if (answer != null)
                    info = (String) answer.get("ex");
                callback.onException(errorCode, info);
            }
        };

        sendQuest(quest, internalCallback, timeoutInseconds);
    }

    public void getPushName(long uid, GetPushNameCallback callback) {
        getPushName(uid, callback, 0);
    }

    @SuppressWarnings("unchecked")
    public String getPushName(long uid, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        Quest quest = genBasicQuest("getpushname");
        quest.param("uid", uid);

        Answer answer = sendQuestAndCheckAnswer(quest, timeoutInseconds);
        return (String) answer.get("pushname");
    }

    public String getPushName(long uid)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return getPushName(uid, 0);
    }

    //-----------------[ setgeo ]-----------------//

    public void setGeo(long uid, double latitude, double longitude, DoneCallback callback, int timeoutInseconds) {

        Quest quest;
        try {
            quest = genBasicQuest("setgeo");
        } catch (Exception e) {
            ErrorRecorder.record("Generate sign for setgeo interface exception.", e);
            callback.onException(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate sign for setgeo interface exception.");
            return;
        }

        quest.param("uid", uid);
        quest.param("lat", latitude);
        quest.param("lng", longitude);

        AnswerCallback internalCallback = new FPNNDoneCallbackWrapper(callback);

        sendQuest(quest, internalCallback, timeoutInseconds);
    }

    public void setGeo(long uid, double latitude, double longitude, DoneCallback callback) {
        setGeo(uid, latitude, longitude, callback, 0);
    }

    public void setGeo(long uid, double latitude, double longitude, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        Quest quest = genBasicQuest("setgeo");
        quest.param("uid", uid);
        quest.param("lat", latitude);
        quest.param("lng", longitude);

        sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    public void setGeo(long uid, double latitude, double longitude)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        setGeo(uid, latitude, longitude, 0);
    }

    //-----------------[ getgeo ]-----------------//

    public class GeoInfo {
        public long uid;
        double latitude;
        double longitude;
    }

    public interface GetGeoCallback {
        void done(double latitude, double longitude);
        void onException(int errorCode, String message);
    }

    public void getGeo(long uid, GetGeoCallback callback, int timeoutInseconds) {

        Quest quest;
        try {
            quest = genBasicQuest("getgeo");
        } catch (Exception e) {
            ErrorRecorder.record("Generate sign for getgeo interface exception.", e);
            callback.onException(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate sign for getgeo interface exception.");
            return;
        }

        quest.param("uid", uid);

        AnswerCallback internalCallback = new AnswerCallback() {
            @Override
            @SuppressWarnings("unchecked")
            public void onAnswer(Answer answer) {
                callback.done((double) answer.get("lat"), (double) answer.get("lng"));
            }

            @Override
            public void onException(Answer answer, int errorCode) {
                String info = null;
                if (answer != null)
                    info = (String) answer.get("ex");
                callback.onException(errorCode, info);
            }
        };

        sendQuest(quest, internalCallback, timeoutInseconds);
    }

    public void getGeo(long uid, GetGeoCallback callback) {
        getGeo(uid, callback, 0);
    }

    @SuppressWarnings("unchecked")
    public GeoInfo getGeo(long uid, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        Quest quest = genBasicQuest("getgeo");
        quest.param("uid", uid);

        Answer answer = sendQuestAndCheckAnswer(quest, timeoutInseconds);

        GeoInfo gi = new GeoInfo();
        gi.uid = uid;
        gi.latitude = (double) answer.get("lat");
        gi.longitude = (double) answer.get("lng");
        return gi;
    }

    public GeoInfo getGeo(long uid)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return getGeo(uid, 0);
    }

    //-----------------[ getgeos ]-----------------//

    //-- TODO: Wait the Interface confirmed.

    //-----------------[ filetoken ]-----------------//

    //-- TODO: Maybe hidden behind send files functions.

    //-----------------[sendfile]-----------------//

    public void sendFile(long fromUid, long toUid, String mType, byte[] fileContent, String filename, String filenameExtension, DoneCallback callback, int timeoutInseconds) {

        Quest fileTokenQuest;
        try {
            fileTokenQuest = genBasicQuest("filetoken");
        } catch (Exception e) {
            ErrorRecorder.record("Prepare sending P2P file failed. Prepare file token exception.", e);
            callback.onException(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Prepare sending P2P file failed. Prepare file token exception.");
            return;
        }


        fileTokenQuest.param("from", fromUid);
        fileTokenQuest.param("cmd", "sendfile");
        fileTokenQuest.param("to", toUid);

        long adjustedTimeout = System.currentTimeMillis();

        AnswerCallback fileTokenCallback = new AnswerCallback() {

            @Override
            public void onException(Answer answer, int errorCode) {

                String info = null;
                if (answer != null)
                    info = (String) answer.get("ex");

                String message = "Prepare sending P2P file failed. Cannot get file token. Message: " + info;

                callback.onException(errorCode, message);
            }

            @Override
            public void onAnswer(Answer answer) {

                String token = (String) answer.get("token");
                String endpoint = (String) answer.get("endpoint");

                String attrs;
                try {
                    attrs = buildFileAttrs(token, fileContent, filename, filenameExtension);
                } catch (Exception e) {
                    ErrorRecorder.record("Build attrs for sending P2P file exception.", e);
                    callback.onException(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Build attrs for sending P2P file exception.");
                    return;
                }

                int originalTimeout = timeoutInseconds;
                if (originalTimeout == 0)
                    originalTimeout = ClientEngine.getQuestTimeout();

                int timeout = (int)(System.currentTimeMillis() - adjustedTimeout);
                timeout = originalTimeout - (timeout / 1000);

                if (timeout <= 0) {
                    callback.onException(ErrorCode.FPNN_EC_CORE_TIMEOUT.value(), "Timeout. Prepare sending P2P file is ready, but no data sent.");
                    return;
                }

                TCPClient fileGate = RTMResourceCenter.instance().getFileClient(endpoint, timeout);

                Quest quest = new Quest("sendfile");
                quest.param("pid", pid);
                quest.param("token", token);
                quest.param("mtype", mType);
                quest.param("from", fromUid);

                quest.param("to", toUid);
                quest.param("mid", MidGenerator.gen());
                quest.param("file", fileContent);
                quest.param("attrs", attrs);

                AnswerCallback internalCallback = new FPNNDoneCallbackWrapper(callback);

                fileGate.sendQuest(quest, internalCallback, timeout);
            }
        };

        sendQuest(fileTokenQuest, fileTokenCallback, timeoutInseconds);
    }

    public void sendFile(long fromUid, long toUid, String mType, byte[] fileContent, String filename, String filenameExtension, DoneCallback callback) {
        sendFile(fromUid, toUid, mType, fileContent, filename, filenameExtension, callback, 0);
    }

    public void sendFile(long fromUid, long toUid, String mType, String filePath, DoneCallback callback, int timeoutInseconds) throws IOException {
        FileInfo info = readFileForSendAPI(filePath);
        sendFile(fromUid, toUid, mType, info.fileContent, info.filename, info.filenameExtension, callback, timeoutInseconds);
    }

    public void sendFile(long fromUid, long toUid, String mType, String filePath, DoneCallback callback) throws IOException {
        sendFile(fromUid, toUid, mType, filePath, callback, 0);
    }

    public void sendFile(long fromUid, long toUid, String mType, byte[] fileContent, String filename, String filenameExtension, int timeoutInseconds)
            throws RTMException, IOException, GeneralSecurityException, InterruptedException {

        String token;
        String endpoint;
        long adjustedTimeout = System.currentTimeMillis();

        //-- get file token
        {
            Quest quest = genBasicQuest("filetoken");
            quest.param("from", fromUid);
            quest.param("cmd", "sendfile");
            quest.param("to", toUid);

            Answer answer = sendQuestAndCheckAnswer(quest, timeoutInseconds);
            token = (String) answer.get("token");
            endpoint = (String) answer.get("endpoint");
        }

        String attrs = buildFileAttrs(token, fileContent, filename, filenameExtension);

        //-- Recalculate the timeout.
        {
            if (timeoutInseconds == 0)
                timeoutInseconds = ClientEngine.getQuestTimeout();

            adjustedTimeout = System.currentTimeMillis() - adjustedTimeout;
            timeoutInseconds -= adjustedTimeout / 1000;

            if (timeoutInseconds <= 0) {
                throw new RTMException(ErrorCode.FPNN_EC_CORE_TIMEOUT.value(), "Timeout. Prepare sending P2P file is ready, but no data sent.");
            }
        }

        //-- send data
        {
            TCPClient fileGate = RTMResourceCenter.instance().getFileClient(endpoint, timeoutInseconds);

            Quest quest = new Quest("sendfile");
            quest.param("pid", pid);
            quest.param("token", token);
            quest.param("mtype", mType);
            quest.param("from", fromUid);

            quest.param("to", toUid);
            quest.param("mid", MidGenerator.gen());
            quest.param("file", fileContent);
            quest.param("attrs", attrs);

            Answer answer = fileGate.sendQuest(quest, timeoutInseconds);
            if (answer.isErrorAnswer()) {
                int errorCode = answer.getErrorCode();
                String message = answer.getErrorMessage();
                throw new RTMException(errorCode, message);
            }
        }
    }

    public void sendFile(long fromUid, long toUid, String mType, byte[] fileContent, String filename, String filenameExtension)
            throws RTMException, IOException, GeneralSecurityException, InterruptedException {
        sendFile(fromUid, toUid, mType, fileContent, filename, filenameExtension, 0);
    }

    public void sendFile(long fromUid, long toUid, String mType, String filePath, int timeoutInseconds)
            throws RTMException, IOException, GeneralSecurityException, InterruptedException {
        FileInfo info = readFileForSendAPI(filePath);
        sendFile(fromUid, toUid, mType, info.fileContent, info.filename, info.filenameExtension, timeoutInseconds);
    }

    public void sendFile(long fromUid, long toUid, String mType, String filePath)
            throws RTMException, IOException, GeneralSecurityException, InterruptedException {
        sendFile(fromUid, toUid, mType, filePath, 0);
    }

    //-----------------[sendfiles]-----------------//

    public void sendFiles(long fromUid, Set<Long> toUids, String mType, byte[] fileContent, String filename, String filenameExtension, DoneCallback callback, int timeoutInseconds) {

        Quest fileTokenQuest;
        try {
            fileTokenQuest = genBasicQuest("filetoken");
        } catch (Exception e) {
            ErrorRecorder.record("Prepare sending multi-receiver file failed. Prepare file token exception.", e);
            callback.onException(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Prepare sending multi-receiver file failed. Prepare file token exception.");
            return;
        }


        fileTokenQuest.param("from", fromUid);
        fileTokenQuest.param("cmd", "sendfiles");
        fileTokenQuest.param("tos", toUids);

        long adjustedTimeout = System.currentTimeMillis();

        AnswerCallback fileTokenCallback = new AnswerCallback() {

            @Override
            public void onException(Answer answer, int errorCode) {

                String info = null;
                if (answer != null)
                    info = (String) answer.get("ex");

                String message = "Prepare sending file to multi-peers failed. Cannot get file token. Message: " + info;

                callback.onException(errorCode, message);
            }

            @Override
            public void onAnswer(Answer answer) {

                String token = (String) answer.get("token");
                String endpoint = (String) answer.get("endpoint");

                String attrs;
                try {
                    attrs = buildFileAttrs(token, fileContent, filename, filenameExtension);
                } catch (Exception e) {
                    ErrorRecorder.record("Build attrs for sending file to multi-peers exception.", e);
                    callback.onException(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Build attrs for sending file to multi-peers exception.");
                    return;
                }

                int originalTimeout = timeoutInseconds;
                if (originalTimeout == 0)
                    originalTimeout = ClientEngine.getQuestTimeout();

                int timeout = (int)(System.currentTimeMillis() - adjustedTimeout);
                timeout = originalTimeout - (timeout / 1000);

                if (timeout <= 0) {
                    callback.onException(ErrorCode.FPNN_EC_CORE_TIMEOUT.value(), "Timeout. Prepare sending file to multi-peers is ready, but no data sent.");
                    return;
                }

                TCPClient fileGate = RTMResourceCenter.instance().getFileClient(endpoint, timeout);

                Quest quest = new Quest("sendfiles");
                quest.param("pid", pid);
                quest.param("token", token);
                quest.param("mtype", mType);
                quest.param("from", fromUid);

                quest.param("tos", toUids);
                quest.param("mid", MidGenerator.gen());
                quest.param("file", fileContent);
                quest.param("attrs", attrs);

                AnswerCallback internalCallback = new FPNNDoneCallbackWrapper(callback);

                fileGate.sendQuest(quest, internalCallback, timeout);
            }
        };

        sendQuest(fileTokenQuest, fileTokenCallback, timeoutInseconds);
    }

    public void sendFiles(long fromUid, Set<Long> toUids, String mType, byte[] fileContent, String filename, String filenameExtension, DoneCallback callback) {
        sendFiles(fromUid, toUids, mType, fileContent, filename, filenameExtension, callback, 0);
    }

    public void sendFiles(long fromUid, Set<Long> toUids, String mType, String filePath, DoneCallback callback, int timeoutInseconds) throws IOException {
        FileInfo info = readFileForSendAPI(filePath);
        sendFiles(fromUid, toUids, mType, info.fileContent, info.filename, info.filenameExtension, callback, timeoutInseconds);
    }

    public void sendFiles(long fromUid, Set<Long> toUids, String mType, String filePath, DoneCallback callback) throws IOException {
        sendFiles(fromUid, toUids, mType, filePath, callback, 0);
    }

    public void sendFiles(long fromUid, Set<Long> toUids, String mType, byte[] fileContent, String filename, String filenameExtension, int timeoutInseconds)
            throws RTMException, IOException, GeneralSecurityException, InterruptedException {

        String token;
        String endpoint;
        long adjustedTimeout = System.currentTimeMillis();

        //-- get file token
        {
            Quest quest = genBasicQuest("filetoken");
            quest.param("from", fromUid);
            quest.param("cmd", "sendfiles");
            quest.param("tos", toUids);

            Answer answer = sendQuestAndCheckAnswer(quest, timeoutInseconds);
            token = (String) answer.get("token");
            endpoint = (String) answer.get("endpoint");
        }

        String attrs = buildFileAttrs(token, fileContent, filename, filenameExtension);

        //-- Recalculate the timeout.
        {
            if (timeoutInseconds == 0)
                timeoutInseconds = ClientEngine.getQuestTimeout();

            adjustedTimeout = System.currentTimeMillis() - adjustedTimeout;
            timeoutInseconds -= adjustedTimeout / 1000;

            if (timeoutInseconds <= 0) {
                throw new RTMException(ErrorCode.FPNN_EC_CORE_TIMEOUT.value(), "Timeout. Prepare sending file to multi-peers is ready, but no data sent.");
            }
        }

        //-- send data
        {
            TCPClient fileGate = RTMResourceCenter.instance().getFileClient(endpoint, timeoutInseconds);

            Quest quest = new Quest("sendfiles");
            quest.param("pid", pid);
            quest.param("token", token);
            quest.param("mtype", mType);
            quest.param("from", fromUid);

            quest.param("tos", toUids);
            quest.param("mid", MidGenerator.gen());
            quest.param("file", fileContent);
            quest.param("attrs", attrs);

            Answer answer = fileGate.sendQuest(quest, timeoutInseconds);
            if (answer.isErrorAnswer()) {
                int errorCode = answer.getErrorCode();
                String message = answer.getErrorMessage();
                throw new RTMException(errorCode, message);
            }
        }
    }

    public void sendFiles(long fromUid, Set<Long> toUids, String mType, byte[] fileContent, String filename, String filenameExtension)
            throws RTMException, IOException, GeneralSecurityException, InterruptedException {
        sendFiles(fromUid, toUids, mType, fileContent, filename, filenameExtension, 0);
    }

    public void sendFiles(long fromUid, Set<Long> toUids, String mType, String filePath, int timeoutInseconds)
            throws RTMException, IOException, GeneralSecurityException, InterruptedException {
        FileInfo info = readFileForSendAPI(filePath);
        sendFiles(fromUid, toUids, mType, info.fileContent, info.filename, info.filenameExtension, timeoutInseconds);
    }

    public void sendFiles(long fromUid, Set<Long> toUids, String mType, String filePath)
            throws RTMException, IOException, GeneralSecurityException, InterruptedException {
        sendFiles(fromUid, toUids, mType, filePath, 0);
    }

    //-----------------[sendgroupfile]-----------------//

    public void sendGroupFile(long fromUid, long groupId, String mType, byte[] fileContent, String filename, String filenameExtension, DoneCallback callback, int timeoutInseconds) {

        Quest fileTokenQuest;
        try {
            fileTokenQuest = genBasicQuest("filetoken");
        } catch (Exception e) {
            ErrorRecorder.record("Prepare sending group file failed. Prepare file token exception.", e);
            callback.onException(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Prepare sending group file failed. Prepare file token exception.");
            return;
        }


        fileTokenQuest.param("from", fromUid);
        fileTokenQuest.param("cmd", "sendgroupfile");
        fileTokenQuest.param("gid", groupId);

        long adjustedTimeout = System.currentTimeMillis();

        AnswerCallback fileTokenCallback = new AnswerCallback() {

            @Override
            public void onException(Answer answer, int errorCode) {

                String info = null;
                if (answer != null)
                    info = (String) answer.get("ex");

                String message = "Prepare sending group file failed. Cannot get file token. Message: " + info;

                callback.onException(errorCode, message);
            }

            @Override
            public void onAnswer(Answer answer) {

                String token = (String) answer.get("token");
                String endpoint = (String) answer.get("endpoint");

                String attrs;
                try {
                    attrs = buildFileAttrs(token, fileContent, filename, filenameExtension);
                } catch (Exception e) {
                    ErrorRecorder.record("Build attrs for sending group file exception.", e);
                    callback.onException(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Build attrs for sending group file exception.");
                    return;
                }

                int originalTimeout = timeoutInseconds;
                if (originalTimeout == 0)
                    originalTimeout = ClientEngine.getQuestTimeout();

                int timeout = (int)(System.currentTimeMillis() - adjustedTimeout);
                timeout = originalTimeout - (timeout / 1000);

                if (timeout <= 0) {
                    callback.onException(ErrorCode.FPNN_EC_CORE_TIMEOUT.value(), "Timeout. Prepare sending group file is ready, but no data sent.");
                    return;
                }

                TCPClient fileGate = RTMResourceCenter.instance().getFileClient(endpoint, timeout);

                Quest quest = new Quest("sendgroupfile");
                quest.param("pid", pid);
                quest.param("token", token);
                quest.param("mtype", mType);
                quest.param("from", fromUid);

                quest.param("gid", groupId);
                quest.param("mid", MidGenerator.gen());
                quest.param("file", fileContent);
                quest.param("attrs", attrs);

                AnswerCallback internalCallback = new FPNNDoneCallbackWrapper(callback);

                fileGate.sendQuest(quest, internalCallback, timeout);
            }
        };

        sendQuest(fileTokenQuest, fileTokenCallback, timeoutInseconds);
    }

    public void sendGroupFile(long fromUid, long groupId, String mType, byte[] fileContent, String filename, String filenameExtension, DoneCallback callback) {
        sendGroupFile(fromUid, groupId, mType, fileContent, filename, filenameExtension, callback, 0);
    }

    public void sendGroupFile(long fromUid, long groupId, String mType, String filePath, DoneCallback callback, int timeoutInseconds) throws IOException {
        FileInfo info = readFileForSendAPI(filePath);
        sendGroupFile(fromUid, groupId, mType, info.fileContent, info.filename, info.filenameExtension, callback, timeoutInseconds);
    }

    public void sendGroupFile(long fromUid, long groupId, String mType, String filePath, DoneCallback callback) throws IOException {
        sendGroupFile(fromUid, groupId, mType, filePath, callback, 0);
    }

    public void sendGroupFile(long fromUid, long groupId, String mType, byte[] fileContent, String filename, String filenameExtension, int timeoutInseconds)
            throws RTMException, IOException, GeneralSecurityException, InterruptedException {

        String token;
        String endpoint;
        long adjustedTimeout = System.currentTimeMillis();

        //-- get file token
        {
            Quest quest = genBasicQuest("filetoken");
            quest.param("from", fromUid);
            quest.param("cmd", "sendgroupfile");
            quest.param("gid", groupId);

            Answer answer = sendQuestAndCheckAnswer(quest, timeoutInseconds);
            token = (String) answer.get("token");
            endpoint = (String) answer.get("endpoint");
        }

        String attrs = buildFileAttrs(token, fileContent, filename, filenameExtension);

        //-- Recalculate the timeout.
        {
            if (timeoutInseconds == 0)
                timeoutInseconds = ClientEngine.getQuestTimeout();

            adjustedTimeout = System.currentTimeMillis() - adjustedTimeout;
            timeoutInseconds -= adjustedTimeout / 1000;

            if (timeoutInseconds <= 0) {
                throw new RTMException(ErrorCode.FPNN_EC_CORE_TIMEOUT.value(), "Timeout. Prepare sending group file is ready, but no data sent.");
            }
        }

        //-- send data
        {
            TCPClient fileGate = RTMResourceCenter.instance().getFileClient(endpoint, timeoutInseconds);

            Quest quest = new Quest("sendgroupfile");
            quest.param("pid", pid);
            quest.param("token", token);
            quest.param("mtype", mType);
            quest.param("from", fromUid);

            quest.param("gid", groupId);
            quest.param("mid", MidGenerator.gen());
            quest.param("file", fileContent);
            quest.param("attrs", attrs);

            Answer answer = fileGate.sendQuest(quest, timeoutInseconds);
            if (answer.isErrorAnswer()) {
                int errorCode = answer.getErrorCode();
                String message = answer.getErrorMessage();
                throw new RTMException(errorCode, message);
            }
        }
    }

    public void sendGroupFile(long fromUid, long groupId, String mType, byte[] fileContent, String filename, String filenameExtension)
            throws RTMException, IOException, GeneralSecurityException, InterruptedException {
        sendGroupFile(fromUid, groupId, mType, fileContent, filename, filenameExtension, 0);
    }

    public void sendGroupFile(long fromUid, long groupId, String mType, String filePath, int timeoutInseconds)
            throws RTMException, IOException, GeneralSecurityException, InterruptedException {
        FileInfo info = readFileForSendAPI(filePath);
        sendGroupFile(fromUid, groupId, mType, info.fileContent, info.filename, info.filenameExtension, timeoutInseconds);
    }

    public void sendGroupFile(long fromUid, long groupId, String mType, String filePath)
            throws RTMException, IOException, GeneralSecurityException, InterruptedException {
        sendGroupFile(fromUid, groupId, mType, filePath, 0);
    }

    //-----------------[sendroomfile]-----------------//

    public void sendRoomFile(long fromUid, long roomId, String mType, byte[] fileContent, String filename, String filenameExtension, DoneCallback callback, int timeoutInseconds) {

        Quest fileTokenQuest;
        try {
            fileTokenQuest = genBasicQuest("filetoken");
        } catch (Exception e) {
            ErrorRecorder.record("Prepare sending room file failed. Prepare file token exception.", e);
            callback.onException(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Prepare sending room file failed. Prepare file token exception.");
            return;
        }


        fileTokenQuest.param("from", fromUid);
        fileTokenQuest.param("cmd", "sendroomfile");
        fileTokenQuest.param("rid", roomId);

        long adjustedTimeout = System.currentTimeMillis();

        AnswerCallback fileTokenCallback = new AnswerCallback() {

            @Override
            public void onException(Answer answer, int errorCode) {

                String info = null;
                if (answer != null)
                    info = (String) answer.get("ex");

                String message = "Prepare sending room file failed. Cannot get file token. Message: " + info;

                callback.onException(errorCode, message);
            }

            @Override
            public void onAnswer(Answer answer) {

                String token = (String) answer.get("token");
                String endpoint = (String) answer.get("endpoint");

                String attrs;
                try {
                    attrs = buildFileAttrs(token, fileContent, filename, filenameExtension);
                } catch (Exception e) {
                    ErrorRecorder.record("Build attrs for sending room file exception.", e);
                    callback.onException(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Build attrs for sending room file exception.");
                    return;
                }

                int originalTimeout = timeoutInseconds;
                if (originalTimeout == 0)
                    originalTimeout = ClientEngine.getQuestTimeout();

                int timeout = (int)(System.currentTimeMillis() - adjustedTimeout);
                timeout = originalTimeout - (timeout / 1000);

                if (timeout <= 0) {
                    callback.onException(ErrorCode.FPNN_EC_CORE_TIMEOUT.value(), "Timeout. Prepare sending room file is ready, but no data sent.");
                    return;
                }

                TCPClient fileGate = RTMResourceCenter.instance().getFileClient(endpoint, timeout);

                Quest quest = new Quest("sendroomfile");
                quest.param("pid", pid);
                quest.param("token", token);
                quest.param("mtype", mType);
                quest.param("from", fromUid);

                quest.param("rid", roomId);
                quest.param("mid", MidGenerator.gen());
                quest.param("file", fileContent);
                quest.param("attrs", attrs);

                AnswerCallback internalCallback = new FPNNDoneCallbackWrapper(callback);

                fileGate.sendQuest(quest, internalCallback, timeout);
            }
        };

        sendQuest(fileTokenQuest, fileTokenCallback, timeoutInseconds);
    }

    public void sendRoomFile(long fromUid, long roomId, String mType, byte[] fileContent, String filename, String filenameExtension, DoneCallback callback) {
        sendRoomFile(fromUid, roomId, mType, fileContent, filename, filenameExtension, callback, 0);
    }


    public void sendRoomFile(long fromUid, long roomId, String mType, String filePath, DoneCallback callback, int timeoutInseconds) throws IOException {
        FileInfo info = readFileForSendAPI(filePath);
        sendRoomFile(fromUid, roomId, mType, info.fileContent, info.filename, info.filenameExtension, callback, timeoutInseconds);
    }

    public void sendRoomFile(long fromUid, long roomId, String mType, String filePath, DoneCallback callback) throws IOException {
        sendRoomFile(fromUid, roomId, mType, filePath, callback, 0);
    }

    public void sendRoomFile(long fromUid, long roomId, String mType, byte[] fileContent, String filename, String filenameExtension, int timeoutInseconds)
            throws RTMException, IOException, GeneralSecurityException, InterruptedException {

        String token;
        String endpoint;
        long adjustedTimeout = System.currentTimeMillis();

        //-- get file token
        {
            Quest quest = genBasicQuest("filetoken");
            quest.param("from", fromUid);
            quest.param("cmd", "sendroomfile");
            quest.param("rid", roomId);

            Answer answer = sendQuestAndCheckAnswer(quest, timeoutInseconds);
            token = (String) answer.get("token");
            endpoint = (String) answer.get("endpoint");
        }

        String attrs = buildFileAttrs(token, fileContent, filename, filenameExtension);

        //-- Recalculate the timeout.
        {
            if (timeoutInseconds == 0)
                timeoutInseconds = ClientEngine.getQuestTimeout();

            adjustedTimeout = System.currentTimeMillis() - adjustedTimeout;
            timeoutInseconds -= adjustedTimeout / 1000;

            if (timeoutInseconds <= 0) {
                throw new RTMException(ErrorCode.FPNN_EC_CORE_TIMEOUT.value(), "Timeout. Prepare sending room file is ready, but no data sent.");
            }
        }

        //-- send data
        {
            TCPClient fileGate = RTMResourceCenter.instance().getFileClient(endpoint, timeoutInseconds);

            Quest quest = new Quest("sendroomfile");
            quest.param("pid", pid);
            quest.param("token", token);
            quest.param("mtype", mType);
            quest.param("from", fromUid);

            quest.param("rid", roomId);
            quest.param("mid", MidGenerator.gen());
            quest.param("file", fileContent);
            quest.param("attrs", attrs);

            Answer answer = fileGate.sendQuest(quest, timeoutInseconds);
            if (answer.isErrorAnswer()) {
                int errorCode = answer.getErrorCode();
                String message = answer.getErrorMessage();
                throw new RTMException(errorCode, message);
            }
        }
    }

    public void sendRoomFile(long fromUid, long roomId, String mType, byte[] fileContent, String filename, String filenameExtension)
            throws RTMException, IOException, GeneralSecurityException, InterruptedException {
        sendRoomFile(fromUid, roomId, mType, fileContent, filename, filenameExtension, 0);
    }

    public void sendRoomFile(long fromUid, long roomId, String mType, String filePath, int timeoutInseconds)
            throws RTMException, IOException, GeneralSecurityException, InterruptedException {
        FileInfo info = readFileForSendAPI(filePath);
        sendRoomFile(fromUid, roomId, mType, info.fileContent, info.filename, info.filenameExtension, timeoutInseconds);
    }

    public void sendRoomFile(long fromUid, long roomId, String mType, String filePath)
            throws RTMException, IOException, GeneralSecurityException, InterruptedException {
        sendRoomFile(fromUid, roomId, mType, filePath, 0);
    }

    //-----------------[broadcastfile]-----------------//

    public void broadcastFile(long adminUid, String mType, byte[] fileContent, String filename, String filenameExtension, DoneCallback callback, int timeoutInseconds) {

        Quest fileTokenQuest;
        try {
            fileTokenQuest = genBasicQuest("filetoken");
        } catch (Exception e) {
            ErrorRecorder.record("Prepare sending broadcast file failed. Prepare file token exception.", e);
            callback.onException(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Prepare sending broadcast file failed. Prepare file token exception.");
            return;
        }


        fileTokenQuest.param("from", adminUid);
        fileTokenQuest.param("cmd", "broadcastfile");

        long adjustedTimeout = System.currentTimeMillis();

        AnswerCallback fileTokenCallback = new AnswerCallback() {

            @Override
            public void onException(Answer answer, int errorCode) {

                String info = null;
                if (answer != null)
                    info = (String) answer.get("ex");

                String message = "Prepare sending broadcast file failed. Cannot get file token. Message: " + info;

                callback.onException(errorCode, message);
            }

            @Override
            public void onAnswer(Answer answer) {

                String token = (String) answer.get("token");
                String endpoint = (String) answer.get("endpoint");

                String attrs;
                try {
                    attrs = buildFileAttrs(token, fileContent, filename, filenameExtension);
                } catch (Exception e) {
                    ErrorRecorder.record("Build attrs for sending broadcast file exception.", e);
                    callback.onException(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Build attrs for sending broadcast file exception.");
                    return;
                }

                int originalTimeout = timeoutInseconds;
                if (originalTimeout == 0)
                    originalTimeout = ClientEngine.getQuestTimeout();

                int timeout = (int)(System.currentTimeMillis() - adjustedTimeout);
                timeout = originalTimeout - (timeout / 1000);

                if (timeout <= 0) {
                    callback.onException(ErrorCode.FPNN_EC_CORE_TIMEOUT.value(), "Timeout. Prepare sending broadcast file is ready, but no data sent.");
                    return;
                }

                TCPClient fileGate = RTMResourceCenter.instance().getFileClient(endpoint, timeout);

                Quest quest = new Quest("broadcastfile");
                quest.param("pid", pid);
                quest.param("token", token);
                quest.param("mtype", mType);
                quest.param("from", adminUid);

                quest.param("mid", MidGenerator.gen());
                quest.param("file", fileContent);
                quest.param("attrs", attrs);

                AnswerCallback internalCallback = new FPNNDoneCallbackWrapper(callback);

                fileGate.sendQuest(quest, internalCallback, timeout);
            }
        };

        sendQuest(fileTokenQuest, fileTokenCallback, timeoutInseconds);
    }

    public void broadcastFile(long adminUid, String mType, byte[] fileContent, String filename, String filenameExtension, DoneCallback callback) {
        broadcastFile(adminUid, mType, fileContent, filename, filenameExtension, callback, 0);
    }


    public void broadcastFile(long adminUid, String mType, String filePath, DoneCallback callback, int timeoutInseconds) throws IOException {
        FileInfo info = readFileForSendAPI(filePath);
        broadcastFile(adminUid, mType, info.fileContent, info.filename, info.filenameExtension, callback, timeoutInseconds);
    }

    public void broadcastFile(long adminUid, String mType, String filePath, DoneCallback callback) throws IOException {
        broadcastFile(adminUid, mType, filePath, callback, 0);
    }

    public void broadcastFile(long adminUid, String mType, byte[] fileContent, String filename, String filenameExtension, int timeoutInseconds)
            throws RTMException, IOException, GeneralSecurityException, InterruptedException {

        String token;
        String endpoint;
        long adjustedTimeout = System.currentTimeMillis();

        //-- get file token
        {
            Quest quest = genBasicQuest("filetoken");
            quest.param("from", adminUid);
            quest.param("cmd", "broadcastfile");

            Answer answer = sendQuestAndCheckAnswer(quest, timeoutInseconds);
            token = (String) answer.get("token");
            endpoint = (String) answer.get("endpoint");
        }

        String attrs = buildFileAttrs(token, fileContent, filename, filenameExtension);

        //-- Recalculate the timeout.
        {
            if (timeoutInseconds == 0)
                timeoutInseconds = ClientEngine.getQuestTimeout();

            adjustedTimeout = System.currentTimeMillis() - adjustedTimeout;
            timeoutInseconds -= adjustedTimeout / 1000;

            if (timeoutInseconds <= 0) {
                throw new RTMException(ErrorCode.FPNN_EC_CORE_TIMEOUT.value(), "Timeout. Prepare sending broadcast file is ready, but no data sent.");
            }
        }

        //-- send data
        {
            TCPClient fileGate = RTMResourceCenter.instance().getFileClient(endpoint, timeoutInseconds);

            Quest quest = new Quest("broadcastfile");
            quest.param("pid", pid);
            quest.param("token", token);
            quest.param("mtype", mType);
            quest.param("from", adminUid);

            quest.param("mid", MidGenerator.gen());
            quest.param("file", fileContent);
            quest.param("attrs", attrs);

            Answer answer = fileGate.sendQuest(quest, timeoutInseconds);
            if (answer.isErrorAnswer()) {
                int errorCode = answer.getErrorCode();
                String message = answer.getErrorMessage();
                throw new RTMException(errorCode, message);
            }
        }
    }

    public void broadcastFile(long adminUid, String mType, byte[] fileContent, String filename, String filenameExtension)
            throws RTMException, IOException, GeneralSecurityException, InterruptedException {
        broadcastFile(adminUid, mType, fileContent, filename, filenameExtension, 0);
    }

    public void broadcastFile(long adminUid, String mType, String filePath, int timeoutInseconds)
            throws RTMException, IOException, GeneralSecurityException, InterruptedException {
        FileInfo info = readFileForSendAPI(filePath);
        broadcastFile(adminUid, mType, info.fileContent, info.filename, info.filenameExtension, timeoutInseconds);
    }

    public void broadcastFile(long adminUid, String mType, String filePath)
            throws RTMException, IOException, GeneralSecurityException, InterruptedException {
        broadcastFile(adminUid, mType, filePath, 0);
    }
}
