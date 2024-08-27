package com.fpnn.rtm.api;

import com.fpnn.rtm.RTMErrorCode;
import com.fpnn.rtm.RTMException;
import com.fpnn.rtm.RTMServerClientBase;
import com.fpnn.rtm.RTMServerClientBase.RTMMessageCount;
import com.fpnn.rtm.RTMServerClientBase.RTMHistoryMessageUnit;
import com.fpnn.sdk.*;
import com.fpnn.sdk.proto.Answer;
import com.fpnn.sdk.proto.Quest;

import java.io.IOException;
import java.net.InterfaceAddress;
import java.security.GeneralSecurityException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface MessageAPI extends MessageCoreAPI {
    //-----------------[ sendmsg ]-----------------//
    default void internalSendMessage(long fromUid, long toUid, byte messageType, Object message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        if (messageType <= 50)
        {
            ErrorRecorder.record("RTMMessageType MUST large than 50, current messageType is " + messageType);
            callback.done(-1, RTMErrorCode.RTM_EC_INVALID_MTYPE.value(), "RTMMessageType MUST large than 50, current messageType is " + messageType);
            return;
        }

        internalCoreSendMessage(fromUid, toUid, messageType, message, attrs, callback, timeoutInseconds);
    }

    default void sendMessage(long fromUid, long toUid, byte messageType, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        internalSendMessage(fromUid, toUid, messageType, message, attrs, callback, timeoutInseconds);
    }

    default void sendMessage(long fromUid, long toUid, byte messageType, String message, String attrs, SendMessageLambdaCallback callback) {
        internalSendMessage(fromUid, toUid, messageType, message, attrs, callback, 0);
    }

    default void sendMessage(long fromUid, long toUid, byte messageType, byte[] message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        internalSendMessage(fromUid, toUid, messageType, message, attrs, callback, timeoutInseconds);
    }

    default void sendMessage(long fromUid, long toUid, byte messageType, byte[] message, String attrs, SendMessageLambdaCallback callback) {
        internalSendMessage(fromUid, toUid, messageType, message, attrs, callback, 0);
    }

    default long internalSendMessage(long fromUid, long toUid, byte messageType, Object message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        if (messageType <= 50)
            throw new RTMException(RTMErrorCode.RTM_EC_INVALID_MTYPE.value(), "RTMMessageType MUST large than 50, current messageType is " + messageType);

        return internalCoreSendMessage(fromUid, toUid, messageType, message, attrs, timeoutInseconds);
    }

    default long sendMessage(long fromUid, long toUid, byte messageType, String message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        return internalSendMessage(fromUid, toUid, messageType, message, attrs, timeoutInseconds);
    }

    default long sendMessage(long fromUid, long toUid, byte messageType, String message, String attrs)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalSendMessage(fromUid, toUid, messageType, message, attrs, 0);
    }

    default long sendMessage(long fromUid, long toUid, byte messageType, byte[] message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        return internalSendMessage(fromUid, toUid, messageType, message, attrs, timeoutInseconds);
    }

    default long sendMessage(long fromUid, long toUid, byte messageType, byte[] message, String attrs)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalSendMessage(fromUid, toUid, messageType, message, attrs, 0);
    }

    //-----------------[ sendmsgs ]-----------------//

    default void internalSendMessages(long fromUid, Set<Long> toUids, byte messageType, Object message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        if (messageType <= 50)
        {
            ErrorRecorder.record("RTMMessageType MUST large than 50, current messageType is " + messageType);
            callback.done(-1, RTMErrorCode.RTM_EC_INVALID_MTYPE.value(), "RTMMessageType MUST large than 50, current messageType is " + messageType);
            return;
        }

        internalCoreSendMessages(fromUid, toUids, messageType, message, attrs, callback, timeoutInseconds);
    }

    default void sendMessages(long fromUid, Set<Long> toUids, byte messageType, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        internalSendMessages(fromUid, toUids, messageType, message, attrs, callback, timeoutInseconds);
    }

    default void sendMessages(long fromUid, Set<Long> toUids, byte messageType, String message, String attrs, SendMessageLambdaCallback callback) {
        internalSendMessages(fromUid, toUids, messageType, message, attrs, callback, 0);
    }

    default void sendMessages(long fromUid, Set<Long> toUids, byte messageType, byte[] message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        internalSendMessages(fromUid, toUids, messageType, message, attrs, callback, timeoutInseconds);
    }

    default void sendMessages(long fromUid, Set<Long> toUids, byte messageType, byte[] message, String attrs, SendMessageLambdaCallback callback) {
        internalSendMessages(fromUid, toUids, messageType, message, attrs, callback, 0);
    }

    default long internalSendMessages(long fromUid, Set<Long> toUids, byte messageType, Object message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        if (messageType <= 50)
            throw new RTMException(RTMErrorCode.RTM_EC_INVALID_MTYPE.value(), "RTMMessageType MUST large than 50, current messageType is " + messageType);

        return internalCoreSendMessages(fromUid, toUids, messageType, message, attrs, timeoutInseconds);
    }

    default long sendMessages(long fromUid, Set<Long> toUids, byte messageType, String message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        return internalSendMessages(fromUid, toUids, messageType, message, attrs, timeoutInseconds);
    }

    default long sendMessages(long fromUid, Set<Long> toUids, byte messageType, String message, String attrs)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalSendMessages(fromUid, toUids, messageType, message, attrs, 0);
    }

    default long sendMessages(long fromUid, Set<Long> toUids, byte messageType, byte[] message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        return internalSendMessages(fromUid, toUids, messageType, message, attrs, timeoutInseconds);
    }

    default long sendMessages(long fromUid, Set<Long> toUids, byte messageType, byte[] message, String attrs)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalSendMessages(fromUid, toUids, messageType, message, attrs, 0);
    }

    //-----------------[ sendgroupmsg ]-----------------//

    default void internalSendGroupMessage(long fromUid, long groupId, byte messageType, Object message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        if (messageType <= 50)
        {
            ErrorRecorder.record("RTMMessageType MUST large than 50, current messageType is " + messageType);
            callback.done(-1, RTMErrorCode.RTM_EC_INVALID_MTYPE.value(), "RTMMessageType MUST large than 50, current messageType is " + messageType);
            return;
        }

        internalCoreSendGroupMessage(fromUid, groupId, messageType, message, attrs, callback, timeoutInseconds);
    }

    default void sendGroupMessage(long fromUid, long groupId, byte messageType, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        internalSendGroupMessage(fromUid, groupId, messageType, message, attrs, callback, timeoutInseconds);
    }

    default void sendGroupMessage(long fromUid, long groupId, byte messageType, String message, String attrs, SendMessageLambdaCallback callback) {
        internalSendGroupMessage(fromUid, groupId, messageType, message, attrs, callback, 0);
    }

    default void sendGroupMessage(long fromUid, long groupId, byte messageType, byte[] message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        internalSendGroupMessage(fromUid, groupId, messageType, message, attrs, callback, timeoutInseconds);
    }

    default void sendGroupMessage(long fromUid, long groupId, byte messageType, byte[] message, String attrs, SendMessageLambdaCallback callback) {
        internalSendGroupMessage(fromUid, groupId, messageType, message, attrs, callback, 0);
    }

    default long internalSendGroupMessage(long fromUid, long groupId, byte messageType, Object message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        if (messageType <= 50)
            throw new RTMException(RTMErrorCode.RTM_EC_INVALID_MTYPE.value(), "RTMMessageType MUST large than 50, current messageType is " + messageType);

        return internalCoreSendGroupMessage(fromUid, groupId, messageType, message, attrs, timeoutInseconds);
    }

    default long sendGroupMessage(long fromUid, long groupId, byte messageType, String message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        return internalSendGroupMessage(fromUid, groupId, messageType, message, attrs, timeoutInseconds);
    }

    default long sendGroupMessage(long fromUid, long groupId, byte messageType, String message, String attrs)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalSendGroupMessage(fromUid, groupId, messageType, message, attrs, 0);
    }

    default long sendGroupMessage(long fromUid, long groupId, byte messageType, byte[] message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        return internalSendGroupMessage(fromUid, groupId, messageType, message, attrs, timeoutInseconds);
    }

    default long sendGroupMessage(long fromUid, long groupId, byte messageType, byte[] message, String attrs)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalSendGroupMessage(fromUid, groupId, messageType, message, attrs, 0);
    }

    //-----------------[ sendroommsg ]-----------------//

    default void internalSendRoomMessage(long fromUid, long roomId, byte messageType, Object message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        if (messageType <= 50)
        {
            ErrorRecorder.record("RTMMessageType MUST large than 50, current mType is " + messageType);
            callback.done(-1, RTMErrorCode.RTM_EC_INVALID_MTYPE.value(), "RTMMessageType MUST large than 50, current mType is " + messageType);
            return;
        }

        internalCoreSendRoomMessage(fromUid, roomId, messageType, message, attrs, callback, timeoutInseconds);
    }

    default void sendRoomMessage(long fromUid, long roomId, byte messageType, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        internalSendRoomMessage(fromUid, roomId, messageType, message, attrs, callback, timeoutInseconds);
    }

    default void sendRoomMessage(long fromUid, long roomId, byte messageType, String message, String attrs, SendMessageLambdaCallback callback) {
        internalSendRoomMessage(fromUid, roomId, messageType, message, attrs, callback, 0);
    }

    default void sendRoomMessage(long fromUid, long roomId, byte messageType, byte[] message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        internalSendRoomMessage(fromUid, roomId, messageType, message, attrs, callback, timeoutInseconds);
    }

    default void sendRoomMessage(long fromUid, long roomId, byte messageType, byte[] message, String attrs, SendMessageLambdaCallback callback) {
        internalSendRoomMessage(fromUid, roomId, messageType, message, attrs, callback, 0);
    }

    default long internalSendRoomMessage(long fromUid, long roomId, byte messageType, Object message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        if (messageType <= 50)
            throw new RTMException(RTMErrorCode.RTM_EC_INVALID_MTYPE.value(), "RTMMessageType MUST large than 50, current messageType is " + messageType);

        return internalCoreSendRoomMessage(fromUid, roomId, messageType, message, attrs, timeoutInseconds);
    }

    default long sendRoomMessage(long fromUid, long roomId, byte messageType, String message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        return internalSendRoomMessage(fromUid, roomId, messageType, message, attrs, timeoutInseconds);
    }

    default long sendRoomMessage(long fromUid, long roomId, byte messageType, String message, String attrs)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalSendRoomMessage(fromUid, roomId, messageType, message, attrs, 0);
    }

    default long sendRoomMessage(long fromUid, long roomId, byte messageType, byte[] message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        return internalSendRoomMessage(fromUid, roomId, messageType, message, attrs, timeoutInseconds);
    }

    default long sendRoomMessage(long fromUid, long roomId, byte messageType, byte[] message, String attrs)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalSendRoomMessage(fromUid, roomId, messageType, message, attrs, 0);
    }

    //-----------------[ broadcastmsg ]-----------------//

    default void internalSendBroadcastMessage(long fromUid, byte messageType, Object message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        if (messageType <= 50)
        {
            ErrorRecorder.record("RTMMessageType MUST large than 50, current messageType is " + messageType);
            callback.done(-1, RTMErrorCode.RTM_EC_INVALID_MTYPE.value(), "RTMMessageType MUST large than 50, current messageType is " + messageType);
            return;
        }

        internalCoreSendBroadcastMessage(fromUid, messageType, message, attrs, callback, timeoutInseconds);
    }

    default void sendBroadcastMessage(long fromUid, byte messageType, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        internalSendBroadcastMessage(fromUid, messageType, message, attrs, callback, timeoutInseconds);
    }

    default void sendBroadcastMessage(long fromUid, byte messageType, String message, String attrs, SendMessageLambdaCallback callback) {
        internalSendBroadcastMessage(fromUid, messageType, message, attrs, callback, 0);
    }

    default void sendBroadcastMessage(long fromUid, byte messageType, byte[] message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        internalSendBroadcastMessage(fromUid, messageType, message, attrs, callback, timeoutInseconds);
    }

    default void sendBroadcastMessage(long fromUid, byte messageType, byte[] message, String attrs, SendMessageLambdaCallback callback) {
        internalSendBroadcastMessage(fromUid, messageType, message, attrs, callback, 0);
    }

    default long internalSendBroadcastMessage(long fromUid, byte messageType, Object message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        if (messageType <= 50)
            throw new RTMException(RTMErrorCode.RTM_EC_INVALID_MTYPE.value(), "RTMMessageType MUST large than 50, current messageType is " + messageType);

        return internalCoreSendBroadcastMessage(fromUid, messageType, message, attrs, timeoutInseconds);
    }

    default long sendBroadcastMessage(long fromUid, byte messageType, String message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        return internalSendBroadcastMessage(fromUid, messageType, message, attrs, timeoutInseconds);
    }

    default long sendBroadcastMessage(long fromUid, byte messageType, String message, String attrs)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalSendBroadcastMessage(fromUid, messageType, message, attrs, 0);
    }

    default long sendBroadcastMessage(long fromUid, byte messageType, byte[] message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        return internalSendBroadcastMessage(fromUid, messageType, message, attrs, timeoutInseconds);
    }

    default long sendBroadcastMessage(long fromUid, byte messageType, byte[] message, String attrs)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalSendBroadcastMessage(fromUid, messageType, message, attrs, 0);
    }

    //----------------------deleteMsg-------------------------------
    default void deleteMsg(long messageId, long from, long xid, MessageType type)
            throws RTMException, GeneralSecurityException, IOException,InterruptedException{
        deleteMsg(messageId, from, xid, type, 0);
    }

    default void deleteMsg(long messageId, long from, long xid, MessageType type, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException,InterruptedException{
        internalDelMsg(messageId, from, xid, type, timeoutInseconds);
    }

    default void deleteP2PMsg(long messageId, long fromUid, long toUid)
            throws RTMException, GeneralSecurityException, IOException,InterruptedException {
        deleteMsg(messageId, fromUid, toUid, MessageType.MESSAGE_TYPE_P2P);
    }

    default void deleteP2PMsg(long messageId, long fromUid, long toUid, int timeInseconds)
            throws RTMException, GeneralSecurityException, IOException,InterruptedException {
        deleteMsg(messageId, fromUid, toUid, MessageType.MESSAGE_TYPE_P2P, timeInseconds);
    }

    default void deleteGroupMsg(long messageId, long fromUid, long groupId)
            throws RTMException, GeneralSecurityException, IOException,InterruptedException {
        deleteMsg(messageId, fromUid, groupId, MessageType.MESSAGE_TYPE_GROUP);
    }

    default void deleteGroupMsg(long messageId, long fromUid, long groupId, int timeInseconds)
            throws RTMException, GeneralSecurityException, IOException,InterruptedException {
        deleteMsg(messageId, fromUid, groupId, MessageType.MESSAGE_TYPE_GROUP, timeInseconds);
    }

    default void deleteRoomMsg(long messageId, long fromUid, long roomId)
            throws RTMException, GeneralSecurityException, IOException,InterruptedException {
        deleteMsg(messageId, fromUid, roomId, MessageType.MESSAGE_TYPE_ROOM);
    }

    default void deleteRoomMsg(long messageId, long fromUid, long roomId, int timeInseconds)
            throws RTMException, GeneralSecurityException, IOException,InterruptedException {
        deleteMsg(messageId, fromUid, roomId, MessageType.MESSAGE_TYPE_ROOM, timeInseconds);
    }

    default void deleteBroadcastMsg(long messageId, long fromUid)
            throws RTMException, GeneralSecurityException, IOException,InterruptedException {
        deleteMsg(messageId, fromUid, 0, MessageType.MESSAGE_TYPE_BROADCAST);
    }

    default void deleteBroadcastMsg(long messageId, long fromUid, int timeInseconds)
            throws RTMException, GeneralSecurityException, IOException,InterruptedException {
        deleteMsg(messageId, fromUid, 0, MessageType.MESSAGE_TYPE_BROADCAST, timeInseconds);
    }

    default void deleteMsg(long messageId, long from, long xid, MessageType type, DoneLambdaCallback callback){
        deleteMsg(messageId, from, xid, type, callback,0);
    }

    default void deleteMsg(long messageId, long from, long xid, MessageType type, DoneLambdaCallback callback, int timeoutInseconds) {
        internalDelMsg(messageId, from, xid, type, callback, timeoutInseconds);
    }

    default void deleteP2PMsg(long messageId, long fromUid, long toUid, DoneLambdaCallback callback){
        deleteMsg(messageId, fromUid, toUid, MessageType.MESSAGE_TYPE_P2P, callback);
    }

    default void deleteP2PMsg(long messageId, long fromUid, long toUid, DoneLambdaCallback callback, int timeInseconds){
        deleteMsg(messageId, fromUid, toUid, MessageType.MESSAGE_TYPE_P2P, callback, timeInseconds);
    }

    default void deleteGroupMsg(long messageId, long fromUid, long groupId, DoneLambdaCallback callback){
        deleteMsg(messageId, fromUid, groupId, MessageType.MESSAGE_TYPE_GROUP, callback);
    }

    default void deleteGroupMsg(long messageId, long fromUid, long groupId, DoneLambdaCallback callback, int timeInseconds){
        deleteMsg(messageId, fromUid, groupId, MessageType.MESSAGE_TYPE_GROUP, callback, timeInseconds);
    }

    default void deleteRoomMsg(long messageId, long fromUid, long roomId, DoneLambdaCallback callback){
        deleteMsg(messageId, fromUid, roomId, MessageType.MESSAGE_TYPE_ROOM, callback);
    }

    default void deleteRoomMsg(long messageId, long fromUid, long roomId, DoneLambdaCallback callback, int timeInseconds){
        deleteMsg(messageId, fromUid, roomId, MessageType.MESSAGE_TYPE_ROOM, callback, timeInseconds);
    }

    default void deleteBroadcastMsg(long messageId, long fromUid, DoneLambdaCallback callback){
        deleteMsg(messageId, fromUid, 0, MessageType.MESSAGE_TYPE_BROADCAST, callback);
    }

    default void deleteBroadcastMsg(long messageId, long fromUid, DoneLambdaCallback callback, int timeInseconds){
        deleteMsg(messageId, fromUid, 0, MessageType.MESSAGE_TYPE_BROADCAST, callback, timeInseconds);
    }

    //----------------------deleteConversationMsg-------------------------------
    default void deleteConversationMsg(long from, long xid, MessageType type)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException {
        deleteConversationMsg(from ,xid, type, 0);
    }

    default void deleteConversationMsg(long from, long xid, MessageType type, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException {
        RTMServerClientBase client = getCoreClient();
        Quest quest = client.genBasicQuest("delconversationmsgs");
        quest.param("from", from);
        quest.param("xid", xid);
        quest.param("type", type.value());
        client.sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    default void deleteConversationMsg(long from, long xid, MessageType type, DoneLambdaCallback callback) {
        deleteConversationMsg(from, xid, type, callback, 0);
    }

    default void deleteConversationMsg(long from, long xid, MessageType type, DoneLambdaCallback callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("delconversationmsgs");
        }catch (Exception ex){
            ErrorRecorder.record("Generate delconversationmsgs message sign exception.", ex);
            callback.done(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate delconversationmsgs message sign exception.");
            return;
        }
        quest.param("from", from);
        quest.param("xid", xid);
        quest.param("type", type.value());

        AnswerCallback answerCallback = new FPNNDoneLambdaCallbackWrapper(callback);
        client.sendQuest(quest, answerCallback, timeoutInseconds);
    }

    //----------------------getMsg-------------------------------
    default RTMHistoryMessageUnit getMsg(long messageId, long from, long xid, MessageType type)
            throws RTMException, GeneralSecurityException, IOException,InterruptedException{
        return getMsg(messageId, from, xid, type, 0);
    }

    default RTMHistoryMessageUnit getMsg(long messageId, long from, long xid, MessageType type, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException,InterruptedException{
        return internalGetMsg(messageId, from, xid, type, timeoutInseconds);
    }

    default RTMHistoryMessageUnit getP2pMsg(long messageId, long fromUid, long toUid)
            throws RTMException, GeneralSecurityException, IOException,InterruptedException {
        return getMsg(messageId, fromUid, toUid, MessageType.MESSAGE_TYPE_P2P);
    }

    default RTMHistoryMessageUnit getP2pMsg(long messageId, long fromUid, long toUid, int timeInseconds)
            throws RTMException, GeneralSecurityException, IOException,InterruptedException {
        return getMsg(messageId, fromUid, toUid, MessageType.MESSAGE_TYPE_P2P, timeInseconds);
    }

    default RTMHistoryMessageUnit getGroupMsg(long messageId, long fromUid, long groupId)
            throws RTMException, GeneralSecurityException, IOException,InterruptedException {
        return getMsg(messageId, fromUid, groupId, MessageType.MESSAGE_TYPE_GROUP);
    }

    default RTMHistoryMessageUnit getGroupMsg(long messageId, long fromUid, long groupId, int timeInseconds)
            throws RTMException, GeneralSecurityException, IOException,InterruptedException {
        return getMsg(messageId, fromUid, groupId, MessageType.MESSAGE_TYPE_GROUP, timeInseconds);
    }

    default RTMHistoryMessageUnit getRoomMsg(long messageId, long fromUid, long roomId)
            throws RTMException, GeneralSecurityException, IOException,InterruptedException {
        return getMsg(messageId, fromUid, roomId, MessageType.MESSAGE_TYPE_ROOM);
    }

    default RTMMessageCount getMsgCount(MessageType type, long xid, Set<Long> mtypes, long begin, long end)
            throws RTMException, GeneralSecurityException, IOException,InterruptedException {
        return getMsgCount(type, xid, mtypes, begin, end, 0);
    }

    default RTMMessageCount getMsgCount(MessageType type, long xid, Set<Long> mtypes, long begin, long end, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException,InterruptedException {
        RTMServerClientBase client = getCoreClient();
        Quest quest = client.genBasicQuest("getmsgnum");
        quest.param("type", type.value());
        quest.param("xid", xid);
        if(mtypes != null && mtypes.size() > 0){
            quest.param("mtypes", mtypes);
        }
        if(begin > 0){
            quest.param("begin", begin);
        }
        if(end > 0) {
            quest.param("end", end);
        }
        Answer answer = client.sendQuestAndCheckAnswer(quest, timeoutInseconds);
        RTMMessageCount rtmMessageCount = new RTMMessageCount();
        rtmMessageCount.sender = answer.getInt("sender", 0);
        rtmMessageCount.count = answer.getInt("num", 0);
        return rtmMessageCount;
    }

    interface GetMssageCountLambdaCallback{
        void done(RTMMessageCount result, int errorCode, String errorMessage);
    }

    default void getMsgCount(MessageType type, long xid, Set<Long> mtypes, long begin, long end, GetMssageCountLambdaCallback callback) {
        getMsgCount(type, xid, mtypes, begin, end, callback, 0);
    }

    default void getMsgCount(MessageType type, long xid, Set<Long> mtypes, long begin, long end,  GetMssageCountLambdaCallback callback, int timeoutInseconds) {
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("getmsgnum");
        }catch (Exception ex){
            ErrorRecorder.record("Generate getmsgnum message sign exception.", ex);
            callback.done(null, ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate getmsgnum message sign exception.");
            return;
        }
        quest.param("type", type.value());
        quest.param("xid", xid);
        if(mtypes != null && mtypes.size() > 0){
            quest.param("mtypes", mtypes);
        }
        if(begin > 0){
            quest.param("begin", begin);
        }
        if(end > 0) {
            quest.param("end", end);
        }
        AnswerCallback answerCallback = new AnswerCallback() {
            @Override
            public void onAnswer(Answer answer) {
                RTMMessageCount rtmMessageCount = new RTMMessageCount();
                rtmMessageCount.sender = answer.getInt("sender", 0);
                rtmMessageCount.count = answer.getInt("num", 0);
                callback.done(rtmMessageCount, ErrorCode.FPNN_EC_OK.value(), "");
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

    default RTMHistoryMessageUnit getRoomMsg(long messageId, long fromUid, long roomId, int timeInseconds)
            throws RTMException, GeneralSecurityException, IOException,InterruptedException {
        return getMsg(messageId, fromUid, roomId, MessageType.MESSAGE_TYPE_ROOM, timeInseconds);
    }

    default RTMHistoryMessageUnit getBroadcastMsg(long messageId, long fromUid)
            throws RTMException, GeneralSecurityException, IOException,InterruptedException {
        return getMsg(messageId, fromUid, 0, MessageType.MESSAGE_TYPE_BROADCAST);
    }

    default RTMHistoryMessageUnit getBroadcastMsg(long messageId, long fromUid, int timeInseconds)
            throws RTMException, GeneralSecurityException, IOException,InterruptedException {
        return getMsg(messageId, fromUid, 0, MessageType.MESSAGE_TYPE_BROADCAST, timeInseconds);
    }

    default void getMsg(long messageId, long from, long xid, MessageType type, GetRetrievedMessageLambdaCallback callback){
        getMsg(messageId, from, xid, type, callback,0);
    }

    default void getMsg(long messageId, long from, long xid, MessageType type, GetRetrievedMessageLambdaCallback callback, int timeoutInseconds) {
        internalGetMsg(messageId, from, xid, type, callback, timeoutInseconds);
    }

    default void getP2PMsg(long messageId, long fromUid, long toUid, GetRetrievedMessageLambdaCallback callback){
        getMsg(messageId, fromUid, toUid, MessageType.MESSAGE_TYPE_P2P, callback);
    }

    default void getP2PMsg(long messageId, long fromUid, long toUid, GetRetrievedMessageLambdaCallback callback, int timeInseconds){
        getMsg(messageId, fromUid, toUid, MessageType.MESSAGE_TYPE_P2P, callback, timeInseconds);
    }

    default void getGroupMsg(long messageId, long fromUid, long groupId, GetRetrievedMessageLambdaCallback callback){
        getMsg(messageId, fromUid, groupId, MessageType.MESSAGE_TYPE_GROUP, callback);
    }

    default void getGroupMsg(long messageId, long fromUid, long groupId, GetRetrievedMessageLambdaCallback callback, int timeInseconds){
        getMsg(messageId, fromUid, groupId, MessageType.MESSAGE_TYPE_GROUP, callback, timeInseconds);
    }

    default void getRoomMsg(long messageId, long fromUid, long roomId, GetRetrievedMessageLambdaCallback callback){
        getMsg(messageId, fromUid, roomId, MessageType.MESSAGE_TYPE_ROOM, callback);
    }

    default void getRoomMsg(long messageId, long fromUid, long roomId, GetRetrievedMessageLambdaCallback callback, int timeInseconds){
        getMsg(messageId, fromUid, roomId, MessageType.MESSAGE_TYPE_ROOM, callback, timeInseconds);
    }

    default void getBroadcastMsg(long messageId, long fromUid, GetRetrievedMessageLambdaCallback callback){
        getMsg(messageId, fromUid, 0, MessageType.MESSAGE_TYPE_BROADCAST, callback);
    }

    default void getBroadcastMsg(long messageId, long fromUid, GetRetrievedMessageLambdaCallback callback, int timeInseconds){
        getMsg(messageId, fromUid, 0, MessageType.MESSAGE_TYPE_BROADCAST, callback, timeInseconds);
    }

    //----------------------editMsg-------------------------------
    default void editMsg(long messageId, long from, long xid, MessageType type, String msg, String attrs, long timeLimit)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException {
        editMsg(messageId, from ,xid, type, msg, attrs,timeLimit, 0);
    }

    default void editMsg(long messageId, long from, long xid, MessageType type, String msg, String attrs, long timeLimit, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException {
        RTMServerClientBase client = getCoreClient();
        Quest quest = client.genBasicQuest("editmsg");
        quest.param("mid", messageId);
        quest.param("from", from);
        quest.param("xid", xid);
        quest.param("type", type.value());
        quest.param("msg", msg);
        quest.param("attrs", attrs);
        quest.param("timeLimit", timeLimit);
        client.sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    default void editMsg(long messageId, long from, long xid, MessageType type, String msg, String attrs, long timeLimit, DoneLambdaCallback callback) {
        editMsg(messageId, from, xid, type, msg, attrs, timeLimit, callback, 0);
    }

    default void editMsg(long messageId, long from, long xid, MessageType type, String msg, String attrs, long timeLimit, DoneLambdaCallback callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("editmsg");
        }catch (Exception ex){
            ErrorRecorder.record("Generate editmsg message sign exception.", ex);
            callback.done(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate editmsg message sign exception.");
            return;
        }
        quest.param("mid", messageId);
        quest.param("from", from);
        quest.param("xid", xid);
        quest.param("type", type.value());
        quest.param("msg", msg);
        quest.param("attrs", attrs);
        quest.param("timeLimit", timeLimit);

        AnswerCallback answerCallback = new FPNNDoneLambdaCallbackWrapper(callback);
        client.sendQuest(quest, answerCallback, timeoutInseconds);
    }

    //----------------------clearProjectMsg-------------------------------
    default void clearProjectMsg(ClearType type)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException {
        clearProjectMsg(type, 0);
    }

    default void clearProjectMsg(ClearType type, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException {
        RTMServerClientBase client = getCoreClient();
        Quest quest = client.genBasicQuest("clearprojectmsg");
        quest.param("type", type.value());
        client.sendQuestAndCheckAnswer(quest, timeoutInseconds);
    }

    default void clearProjectMsg(ClearType type, DoneLambdaCallback callback) {
        clearProjectMsg(type, callback, 0);
    }

    default void clearProjectMsg(ClearType type, DoneLambdaCallback callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("clearprojectmsg");
        }catch (Exception ex){
            ErrorRecorder.record("Generate clearprojectmsg message sign exception.", ex);
            callback.done(ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate clearprojectmsg message sign exception.");
            return;
        }
        quest.param("type", type.value());
        AnswerCallback answerCallback = new FPNNDoneLambdaCallbackWrapper(callback);
        client.sendQuest(quest, answerCallback, timeoutInseconds);
    }
}
