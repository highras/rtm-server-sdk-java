package com.fpnn.rtm.api;

import com.fpnn.rtm.RTMErrorCode;
import com.fpnn.rtm.RTMException;
import com.fpnn.rtm.RTMServerClientBase.RTMRetrievedMessage;
import com.fpnn.sdk.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Set;

public interface MessageAPI extends MessageCoreAPI {
    //-----------------[ sendmsg ]-----------------//
    default void internalSendMessage(long fromUid, long toUid, byte mType, Object message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        if (mType <= 50)
        {
            ErrorRecorder.record("MType MUST large than 50, current mType is " + mType);
            callback.done(-1, RTMErrorCode.RTM_EC_INVALID_MTYPE.value(), "MType MUST large than 50, current mType is " + mType);
            return;
        }

        internalCoreSendMessage(fromUid, toUid, mType, message, attrs, callback, timeoutInseconds);
    }

    default void sendMessage(long fromUid, long toUid, byte mType, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        internalSendMessage(fromUid, toUid, mType, message, attrs, callback, timeoutInseconds);
    }

    default void sendMessage(long fromUid, long toUid, byte mType, String message, String attrs, SendMessageLambdaCallback callback) {
        internalSendMessage(fromUid, toUid, mType, message, attrs, callback, 0);
    }

    default void sendMessage(long fromUid, long toUid, byte mType, byte[] message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        internalSendMessage(fromUid, toUid, mType, message, attrs, callback, timeoutInseconds);
    }

    default void sendMessage(long fromUid, long toUid, byte mType, byte[] message, String attrs, SendMessageLambdaCallback callback) {
        internalSendMessage(fromUid, toUid, mType, message, attrs, callback, 0);
    }

    default long internalSendMessage(long fromUid, long toUid, byte mType, Object message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        if (mType <= 50)
            throw new RTMException(RTMErrorCode.RTM_EC_INVALID_MTYPE.value(), "MType MUST large than 50, current mType is " + mType);

        return internalCoreSendMessage(fromUid, toUid, mType, message, attrs, timeoutInseconds);
    }

    default long sendMessage(long fromUid, long toUid, byte mType, String message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        return internalSendMessage(fromUid, toUid, mType, message, attrs, timeoutInseconds);
    }

    default long sendMessage(long fromUid, long toUid, byte mType, String message, String attrs)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalSendMessage(fromUid, toUid, mType, message, attrs, 0);
    }

    default long sendMessage(long fromUid, long toUid, byte mType, byte[] message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        return internalSendMessage(fromUid, toUid, mType, message, attrs, timeoutInseconds);
    }

    default long sendMessage(long fromUid, long toUid, byte mType, byte[] message, String attrs)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalSendMessage(fromUid, toUid, mType, message, attrs, 0);
    }

    //-----------------[ sendmsgs ]-----------------//

    default void internalSendMessages(long fromUid, Set<Long> toUids, byte mType, Object message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        if (mType <= 50)
        {
            ErrorRecorder.record("MType MUST large than 50, current mType is " + mType);
            callback.done(-1, RTMErrorCode.RTM_EC_INVALID_MTYPE.value(), "MType MUST large than 50, current mType is " + mType);
            return;
        }

        internalCoreSendMessages(fromUid, toUids, mType, message, attrs, callback, timeoutInseconds);
    }

    default void sendMessages(long fromUid, Set<Long> toUids, byte mType, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        internalSendMessages(fromUid, toUids, mType, message, attrs, callback, timeoutInseconds);
    }

    default void sendMessages(long fromUid, Set<Long> toUids, byte mType, String message, String attrs, SendMessageLambdaCallback callback) {
        internalSendMessages(fromUid, toUids, mType, message, attrs, callback, 0);
    }

    default void sendMessages(long fromUid, Set<Long> toUids, byte mType, byte[] message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        internalSendMessages(fromUid, toUids, mType, message, attrs, callback, timeoutInseconds);
    }

    default void sendMessages(long fromUid, Set<Long> toUids, byte mType, byte[] message, String attrs, SendMessageLambdaCallback callback) {
        internalSendMessages(fromUid, toUids, mType, message, attrs, callback, 0);
    }

    default long internalSendMessages(long fromUid, Set<Long> toUids, byte mType, Object message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        if (mType <= 50)
            throw new RTMException(RTMErrorCode.RTM_EC_INVALID_MTYPE.value(), "MType MUST large than 50, current mType is " + mType);

        return internalCoreSendMessages(fromUid, toUids, mType, message, attrs, timeoutInseconds);
    }

    default long sendMessages(long fromUid, Set<Long> toUids, byte mType, String message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        return internalSendMessages(fromUid, toUids, mType, message, attrs, timeoutInseconds);
    }

    default long sendMessages(long fromUid, Set<Long> toUids, byte mType, String message, String attrs)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalSendMessages(fromUid, toUids, mType, message, attrs, 0);
    }

    default long sendMessages(long fromUid, Set<Long> toUids, byte mType, byte[] message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        return internalSendMessages(fromUid, toUids, mType, message, attrs, timeoutInseconds);
    }

    default long sendMessages(long fromUid, Set<Long> toUids, byte mType, byte[] message, String attrs)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalSendMessages(fromUid, toUids, mType, message, attrs, 0);
    }

    //-----------------[ sendgroupmsg ]-----------------//

    default void internalSendGroupMessage(long fromUid, long groupId, byte mType, Object message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        if (mType <= 50)
        {
            ErrorRecorder.record("MType MUST large than 50, current mType is " + mType);
            callback.done(-1, RTMErrorCode.RTM_EC_INVALID_MTYPE.value(), "MType MUST large than 50, current mType is " + mType);
            return;
        }

        internalCoreSendGroupMessage(fromUid, groupId, mType, message, attrs, callback, timeoutInseconds);
    }

    default void sendGroupMessage(long fromUid, long groupId, byte mType, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        internalSendGroupMessage(fromUid, groupId, mType, message, attrs, callback, timeoutInseconds);
    }

    default void sendGroupMessage(long fromUid, long groupId, byte mType, String message, String attrs, SendMessageLambdaCallback callback) {
        internalSendGroupMessage(fromUid, groupId, mType, message, attrs, callback, 0);
    }

    default void sendGroupMessage(long fromUid, long groupId, byte mType, byte[] message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        internalSendGroupMessage(fromUid, groupId, mType, message, attrs, callback, timeoutInseconds);
    }

    default void sendGroupMessage(long fromUid, long groupId, byte mType, byte[] message, String attrs, SendMessageLambdaCallback callback) {
        internalSendGroupMessage(fromUid, groupId, mType, message, attrs, callback, 0);
    }

    default long internalSendGroupMessage(long fromUid, long groupId, byte mType, Object message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        if (mType <= 50)
            throw new RTMException(RTMErrorCode.RTM_EC_INVALID_MTYPE.value(), "MType MUST large than 50, current mType is " + mType);

        return internalCoreSendGroupMessage(fromUid, groupId, mType, message, attrs, timeoutInseconds);
    }

    default long sendGroupMessage(long fromUid, long groupId, byte mType, String message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        return internalSendGroupMessage(fromUid, groupId, mType, message, attrs, timeoutInseconds);
    }

    default long sendGroupMessage(long fromUid, long groupId, byte mType, String message, String attrs)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalSendGroupMessage(fromUid, groupId, mType, message, attrs, 0);
    }

    default long sendGroupMessage(long fromUid, long groupId, byte mType, byte[] message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        return internalSendGroupMessage(fromUid, groupId, mType, message, attrs, timeoutInseconds);
    }

    default long sendGroupMessage(long fromUid, long groupId, byte mType, byte[] message, String attrs)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalSendGroupMessage(fromUid, groupId, mType, message, attrs, 0);
    }

    //-----------------[ sendroommsg ]-----------------//

    default void internalSendRoomMessage(long fromUid, long roomId, byte mType, Object message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        if (mType <= 50)
        {
            ErrorRecorder.record("MType MUST large than 50, current mType is " + mType);
            callback.done(-1, RTMErrorCode.RTM_EC_INVALID_MTYPE.value(), "MType MUST large than 50, current mType is " + mType);
            return;
        }

        internalCoreSendRoomMessage(fromUid, roomId, mType, message, attrs, callback, timeoutInseconds);
    }

    default void sendRoomMessage(long fromUid, long roomId, byte mType, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        internalSendRoomMessage(fromUid, roomId, mType, message, attrs, callback, timeoutInseconds);
    }

    default void sendRoomMessage(long fromUid, long roomId, byte mType, String message, String attrs, SendMessageLambdaCallback callback) {
        internalSendRoomMessage(fromUid, roomId, mType, message, attrs, callback, 0);
    }

    default void sendRoomMessage(long fromUid, long roomId, byte mType, byte[] message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        internalSendRoomMessage(fromUid, roomId, mType, message, attrs, callback, timeoutInseconds);
    }

    default void sendRoomMessage(long fromUid, long roomId, byte mType, byte[] message, String attrs, SendMessageLambdaCallback callback) {
        internalSendRoomMessage(fromUid, roomId, mType, message, attrs, callback, 0);
    }

    default long internalSendRoomMessage(long fromUid, long roomId, byte mType, Object message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        if (mType <= 50)
            throw new RTMException(RTMErrorCode.RTM_EC_INVALID_MTYPE.value(), "MType MUST large than 50, current mType is " + mType);

        return internalCoreSendRoomMessage(fromUid, roomId, mType, message, attrs, timeoutInseconds);
    }

    default long sendRoomMessage(long fromUid, long roomId, byte mType, String message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        return internalSendRoomMessage(fromUid, roomId, mType, message, attrs, timeoutInseconds);
    }

    default long sendRoomMessage(long fromUid, long roomId, byte mType, String message, String attrs)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalSendRoomMessage(fromUid, roomId, mType, message, attrs, 0);
    }

    default long sendRoomMessage(long fromUid, long roomId, byte mType, byte[] message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        return internalSendRoomMessage(fromUid, roomId, mType, message, attrs, timeoutInseconds);
    }

    default long sendRoomMessage(long fromUid, long roomId, byte mType, byte[] message, String attrs)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalSendRoomMessage(fromUid, roomId, mType, message, attrs, 0);
    }

    //-----------------[ broadcastmsg ]-----------------//

    default void internalSendBroadcastMessage(long fromUid, byte mType, Object message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        if (mType <= 50)
        {
            ErrorRecorder.record("MType MUST large than 50, current mType is " + mType);
            callback.done(-1, RTMErrorCode.RTM_EC_INVALID_MTYPE.value(), "MType MUST large than 50, current mType is " + mType);
            return;
        }

        internalCoreSendBroadcastMessage(fromUid, mType, message, attrs, callback, timeoutInseconds);
    }

    default void sendBroadcastMessage(long fromUid, byte mType, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        internalSendBroadcastMessage(fromUid, mType, message, attrs, callback, timeoutInseconds);
    }

    default void sendBroadcastMessage(long fromUid, byte mType, String message, String attrs, SendMessageLambdaCallback callback) {
        internalSendBroadcastMessage(fromUid, mType, message, attrs, callback, 0);
    }

    default void sendBroadcastMessage(long fromUid, byte mType, byte[] message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        internalSendBroadcastMessage(fromUid, mType, message, attrs, callback, timeoutInseconds);
    }

    default void sendBroadcastMessage(long fromUid, byte mType, byte[] message, String attrs, SendMessageLambdaCallback callback) {
        internalSendBroadcastMessage(fromUid, mType, message, attrs, callback, 0);
    }

    default long internalSendBroadcastMessage(long fromUid, byte mType, Object message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        if (mType <= 50)
            throw new RTMException(RTMErrorCode.RTM_EC_INVALID_MTYPE.value(), "MType MUST large than 50, current mType is " + mType);

        return internalCoreSendBroadcastMessage(fromUid, mType, message, attrs, timeoutInseconds);
    }

    default long sendBroadcastMessage(long fromUid, byte mType, String message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        return internalSendBroadcastMessage(fromUid, mType, message, attrs, timeoutInseconds);
    }

    default long sendBroadcastMessage(long fromUid, byte mType, String message, String attrs)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalSendBroadcastMessage(fromUid, mType, message, attrs, 0);
    }

    default long sendBroadcastMessage(long fromUid, byte mType, byte[] message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        return internalSendBroadcastMessage(fromUid, mType, message, attrs, timeoutInseconds);
    }

    default long sendBroadcastMessage(long fromUid, byte mType, byte[] message, String attrs)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalSendBroadcastMessage(fromUid, mType, message, attrs, 0);
    }

    //----------------------deleteMsg-------------------------------
    default void deleteMsg(long mid, long from, long xid, MessageType type)
            throws RTMException, GeneralSecurityException, IOException,InterruptedException{
        deleteMsg(mid, from, xid, type, 0);
    }

    default void deleteMsg(long mid, long from, long xid, MessageType type, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException,InterruptedException{
        internalDelMsg(mid, from, xid, type, timeoutInseconds);
    }

    default void deleteMsg(long mid, long from, long xid, MessageType type, DoneLambdaCallback callback){
        deleteMsg(mid, from, xid, type, callback,0);
    }

    default void deleteMsg(long mid, long from, long xid, MessageType type, DoneLambdaCallback callback, int timeoutInseconds) {
        internalDelMsg(mid, from, xid, type, callback, timeoutInseconds);
    }

    //----------------------getMsg-------------------------------
    default RTMRetrievedMessage getMsg(long mid, long from, long xid, MessageType type)
            throws RTMException, GeneralSecurityException, IOException,InterruptedException{
        return getMsg(mid, from, xid, type, 0);
    }

    default RTMRetrievedMessage getMsg(long mid, long from, long xid, MessageType type, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException,InterruptedException{
        return internalGetMsg(mid, from, xid, type, timeoutInseconds);
    }

    default void getMsg(long mid, long from, long xid, MessageType type, GetRetrievedMessageLambdaCallback callback){
        getMsg(mid, from, xid, type, callback,0);
    }

    default void getMsg(long mid, long from, long xid, MessageType type, GetRetrievedMessageLambdaCallback callback, int timeoutInseconds) {
        internalGetMsg(mid, from, xid, type, callback, timeoutInseconds);
    }

}
