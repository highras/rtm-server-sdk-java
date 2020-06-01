package com.fpnn.rtm.api;

import com.fpnn.rtm.MType;
import com.fpnn.rtm.RTMException;
import com.fpnn.rtm.RTMServerClientBase.RTMRetrievedMessage;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Set;

public interface ChatAPI extends MessageCoreAPI {
    //-----------------[ sendmsg ]-----------------//
    default void sendChat(long fromUid, long toUid, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        internalCoreSendMessage(fromUid, toUid, MType.Chat.value(), message, attrs, callback, timeoutInseconds);
    }

    default void sendChat(long fromUid, long toUid, String message, String attrs, SendMessageLambdaCallback callback) {
        internalCoreSendMessage(fromUid, toUid, MType.Chat.value(), message, attrs, callback, 0);
    }

    default void sendAudio(long fromUid, long toUid, byte[] message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        internalCoreSendMessage(fromUid, toUid, MType.AudioChat.value(), message, attrs, callback, timeoutInseconds);
    }

    default void sendAudio(long fromUid, long toUid, byte[] message, String attrs, SendMessageLambdaCallback callback) {
        internalCoreSendMessage(fromUid, toUid, MType.AudioChat.value(), message, attrs, callback, 0);
    }

    default void sendCmd(long fromUid, long toUid, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        internalCoreSendMessage(fromUid, toUid, MType.Cmd.value(), message, attrs, callback, timeoutInseconds);
    }

    default void sendCmd(long fromUid, long toUid, String message, String attrs, SendMessageLambdaCallback callback) {
        internalCoreSendMessage(fromUid, toUid, MType.Cmd.value(), message, attrs, callback, 0);
    }

    //----
    default long sendChat(long fromUid, long toUid, String message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        return internalCoreSendMessage(fromUid, toUid, MType.Chat.value(), message, attrs, timeoutInseconds);
    }

    default long sendChat(long fromUid, long toUid,  String message, String attrs)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalCoreSendMessage(fromUid, toUid, MType.Chat.value(), message, attrs, 0);
    }

    default long sendAudio(long fromUid, long toUid, byte[] message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        return internalCoreSendMessage(fromUid, toUid, MType.AudioChat.value(), message, attrs, timeoutInseconds);
    }

    default long sendAudio(long fromUid, long toUid, byte[] message, String attrs)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalCoreSendMessage(fromUid, toUid, MType.AudioChat.value(), message, attrs, 0);
    }

    default long sendCmd(long fromUid, long toUid, String message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        return internalCoreSendMessage(fromUid, toUid, MType.Cmd.value(), message, attrs, timeoutInseconds);
    }

    default long sendCmd(long fromUid, long toUid, String message, String attrs)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalCoreSendMessage(fromUid, toUid, MType.Cmd.value(), message, attrs, 0);
    }

    //-----------------[ sendmsgs ]-----------------//

    default void sendChats(long fromUid, Set<Long> toUids, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        internalCoreSendMessages(fromUid, toUids, MType.Chat.value(), message, attrs, callback, timeoutInseconds);
    }

    default void sendChats(long fromUid, Set<Long> toUids, String message, String attrs, SendMessageLambdaCallback callback) {
        internalCoreSendMessages(fromUid, toUids, MType.Chat.value(), message, attrs, callback, 0);
    }

    default void sendAudios(long fromUid, Set<Long> toUids, byte[] message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        internalCoreSendMessages(fromUid, toUids, MType.AudioChat.value(), message, attrs, callback, timeoutInseconds);
    }

    default void sendAudios(long fromUid, Set<Long> toUids, byte[] message, String attrs, SendMessageLambdaCallback callback) {
        internalCoreSendMessages(fromUid, toUids, MType.AudioChat.value(), message, attrs, callback, 0);
    }

    default void sendCmds(long fromUid, Set<Long> toUids, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        internalCoreSendMessages(fromUid, toUids, MType.Cmd.value(), message, attrs, callback, timeoutInseconds);
    }

    default void sendCmds(long fromUid, Set<Long> toUids, String message, String attrs, SendMessageLambdaCallback callback) {
        internalCoreSendMessages(fromUid, toUids, MType.Cmd.value(), message, attrs, callback, 0);
    }

    //----
    default long sendChats(long fromUid, Set<Long> toUids, String message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        return internalCoreSendMessages(fromUid, toUids, MType.Chat.value(), message, attrs, timeoutInseconds);
    }

    default long sendChats(long fromUid, Set<Long> toUids, String message, String attrs)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalCoreSendMessages(fromUid, toUids, MType.Chat.value(), message, attrs, 0);
    }

    default long sendAudios(long fromUid, Set<Long> toUids, byte[] message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        return internalCoreSendMessages(fromUid, toUids, MType.AudioChat.value(), message, attrs, timeoutInseconds);
    }

    default long sendAudios(long fromUid, Set<Long> toUids, byte[] message, String attrs)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalCoreSendMessages(fromUid, toUids, MType.AudioChat.value(), message, attrs, 0);
    }

    default long sendCmds(long fromUid, Set<Long> toUids, String message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        return internalCoreSendMessages(fromUid, toUids, MType.Cmd.value(), message, attrs, timeoutInseconds);
    }

    default long sendCmds(long fromUid, Set<Long> toUids, String message, String attrs)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalCoreSendMessages(fromUid, toUids, MType.Cmd.value(), message, attrs, 0);
    }

    //-----------------[ sendgroupmsg ]-----------------//

    default void sendGroupChat(long fromUid, long groupId, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        internalCoreSendGroupMessage(fromUid, groupId, MType.Chat.value(), message, attrs, callback, timeoutInseconds);
    }

    default void sendGroupChat(long fromUid, long groupId, String message, String attrs, SendMessageLambdaCallback callback) {
        internalCoreSendGroupMessage(fromUid, groupId, MType.Chat.value(), message, attrs, callback, 0);
    }

    default void sendGroupAudio(long fromUid, long groupId, byte[] message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        internalCoreSendGroupMessage(fromUid, groupId, MType.AudioChat.value(), message, attrs, callback, timeoutInseconds);
    }

    default void sendGroupAudio(long fromUid, long groupId, byte[] message, String attrs, SendMessageLambdaCallback callback) {
        internalCoreSendGroupMessage(fromUid, groupId, MType.AudioChat.value(), message, attrs, callback, 0);
    }

    default void sendGroupCmd(long fromUid, long groupId, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        internalCoreSendGroupMessage(fromUid, groupId, MType.Cmd.value(), message, attrs, callback, timeoutInseconds);
    }

    default void sendGroupCmd(long fromUid, long groupId, String message, String attrs, SendMessageLambdaCallback callback) {
        internalCoreSendGroupMessage(fromUid, groupId, MType.Cmd.value(), message, attrs, callback, 0);
    }

    //----
    default long sendGroupChat(long fromUid, long groupId, String message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        return internalCoreSendGroupMessage(fromUid, groupId, MType.Chat.value(), message, attrs, timeoutInseconds);
    }

    default long sendGroupChat(long fromUid, long groupId, String message, String attrs)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalCoreSendGroupMessage(fromUid, groupId, MType.Chat.value(), message, attrs, 0);
    }

    default long sendGroupAudio(long fromUid, long groupId, byte[] message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        return internalCoreSendGroupMessage(fromUid, groupId, MType.AudioChat.value(), message, attrs, timeoutInseconds);
    }

    default long sendGroupAudio(long fromUid, long groupId, byte[] message, String attrs)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalCoreSendGroupMessage(fromUid, groupId, MType.AudioChat.value(), message, attrs, 0);
    }

    default long sendGroupCmd(long fromUid, long groupId, String message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        return internalCoreSendGroupMessage(fromUid, groupId, MType.Cmd.value(), message, attrs, timeoutInseconds);
    }

    default long sendGroupCmd(long fromUid, long groupId, String message, String attrs)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalCoreSendGroupMessage(fromUid, groupId, MType.Cmd.value(), message, attrs, 0);
    }

    //-----------------[ sendroommsg ]-----------------//

    default void sendRoomChat(long fromUid, long roomId, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        internalCoreSendRoomMessage(fromUid, roomId, MType.Chat.value(), message, attrs, callback, timeoutInseconds);
    }

    default void sendRoomChat(long fromUid, long roomId, String message, String attrs, SendMessageLambdaCallback callback) {
        internalCoreSendRoomMessage(fromUid, roomId, MType.Chat.value(), message, attrs, callback, 0);
    }

    default void sendRoomAudio(long fromUid, long roomId,  byte[] message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        internalCoreSendRoomMessage(fromUid, roomId, MType.AudioChat.value(), message, attrs, callback, timeoutInseconds);
    }

    default void sendRoomAudio(long fromUid, long roomId, byte[] message, String attrs, SendMessageLambdaCallback callback) {
        internalCoreSendRoomMessage(fromUid, roomId, MType.AudioChat.value(), message, attrs, callback, 0);
    }

    default void sendRoomCmd(long fromUid, long roomId, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        internalCoreSendRoomMessage(fromUid, roomId, MType.Cmd.value(), message, attrs, callback, timeoutInseconds);
    }

    default void sendRoomCmd(long fromUid, long roomId, String message, String attrs, SendMessageLambdaCallback callback) {
        internalCoreSendRoomMessage(fromUid, roomId, MType.Cmd.value(), message, attrs, callback, 0);
    }

    //----
    default long sendRoomChat(long fromUid, long roomId, String message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalCoreSendRoomMessage(fromUid, roomId, MType.Chat.value(), message, attrs, timeoutInseconds);
    }

    default long sendRoomChat(long fromUid, long roomId, String message, String attrs)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalCoreSendRoomMessage(fromUid, roomId, MType.Chat.value(), message, attrs, 0);
    }

    default long sendRoomAudio(long fromUid, long roomId, byte[] message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalCoreSendRoomMessage(fromUid, roomId, MType.AudioChat.value(), message, attrs, timeoutInseconds);
    }

    default long sendRoomAudio(long fromUid, long roomId, byte[] message, String attrs)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalCoreSendRoomMessage(fromUid, roomId, MType.AudioChat.value(), message, attrs, 0);
    }

    default long sendRoomCmd(long fromUid, long roomId, String message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalCoreSendRoomMessage(fromUid, roomId, MType.Cmd.value(), message, attrs, timeoutInseconds);
    }

    default long sendRoomCmd(long fromUid, long roomId,  String message, String attrs)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalCoreSendRoomMessage(fromUid, roomId, MType.Cmd.value(), message, attrs, 0);
    }

    //-----------------[ broadcastmsg ]-----------------//

    default void sendBroadcastChat(long fromUid, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        internalCoreSendBroadcastMessage(fromUid, MType.Chat.value(), message, attrs, callback, timeoutInseconds);
    }

    default void sendBroadcastChat(long fromUid, String message, String attrs, SendMessageLambdaCallback callback) {
        internalCoreSendBroadcastMessage(fromUid, MType.Chat.value(), message, attrs, callback, 0);
    }

    default void sendBroadcastAudio(long fromUid, byte[] message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        internalCoreSendBroadcastMessage(fromUid, MType.AudioChat.value(), message, attrs, callback, timeoutInseconds);
    }

    default void sendBroadcastAudio(long fromUid, byte[] message, String attrs, SendMessageLambdaCallback callback) {
        internalCoreSendBroadcastMessage(fromUid, MType.AudioChat.value(), message, attrs, callback, 0);
    }

    default void sendBroadcastCmd(long fromUid, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        internalCoreSendBroadcastMessage(fromUid, MType.Cmd.value(), message, attrs, callback, timeoutInseconds);
    }

    default void sendBroadcastCmd(long fromUid, String message, String attrs, SendMessageLambdaCallback callback) {
        internalCoreSendBroadcastMessage(fromUid, MType.Cmd.value(), message, attrs, callback, 0);
    }

    //----
    default long sendBroadcastChat(long fromUid, String message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalCoreSendBroadcastMessage(fromUid, MType.Chat.value(), message, attrs, timeoutInseconds);
    }

    default long sendBroadcastChat(long fromUid, String message, String attrs)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalCoreSendBroadcastMessage(fromUid, MType.Chat.value(), message, attrs, 0);
    }

    default long sendBroadcastAudio(long fromUid, byte[] message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalCoreSendBroadcastMessage(fromUid, MType.AudioChat.value(), message, attrs, timeoutInseconds);
    }

    default long sendBroadcastAudio(long fromUid, byte[] message, String attrs)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalCoreSendBroadcastMessage(fromUid, MType.AudioChat.value(), message, attrs, 0);
    }

    default long sendBroadcastCmd(long fromUid, String message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalCoreSendBroadcastMessage(fromUid, MType.Cmd.value(), message, attrs, timeoutInseconds);
    }

    default long sendBroadcastCmd(long fromUid, String message, String attrs)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalCoreSendBroadcastMessage(fromUid, MType.Cmd.value(), message, attrs, 0);
    }

    //----------------------deleteChat-------------------------------
    default void deleteChat(long mid, long from, long xid, MessageType type)
            throws RTMException, GeneralSecurityException, IOException,InterruptedException{
        deleteChat(mid, from, xid, type, 0);
    }

    default void deleteChat(long mid, long from, long xid, MessageType type, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException,InterruptedException{
        internalDelMsg(mid, from, xid, type, timeoutInseconds);
    }

    default void deleteChat(long mid, long from, long xid, MessageType type, DoneLambdaCallback callback){
        deleteChat(mid, from, xid, type, callback,0);
    }

    default void deleteChat(long mid, long from, long xid, MessageType type, DoneLambdaCallback callback, int timeoutInseconds) {
        internalDelMsg(mid, from, xid, type, callback, timeoutInseconds);
    }

    //----------------------getChat-------------------------------
    default RTMRetrievedMessage getChat(long mid, long from, long xid, MessageType type)
            throws RTMException, GeneralSecurityException, IOException,InterruptedException{
        return getChat(mid, from, xid, type, 0);
    }

    default RTMRetrievedMessage getChat(long mid, long from, long xid, MessageType type, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException,InterruptedException{
        return internalGetMsg(mid, from, xid, type, timeoutInseconds);
    }

    default void getChat(long mid, long from, long xid, MessageType type, GetRetrievedMessageLambdaCallback callback){
        getChat(mid, from, xid, type, callback,0);
    }

    default void getChat(long mid, long from, long xid, MessageType type, GetRetrievedMessageLambdaCallback callback, int timeoutInseconds) {
        internalGetMsg(mid, from, xid, type, callback, timeoutInseconds);
    }
}
