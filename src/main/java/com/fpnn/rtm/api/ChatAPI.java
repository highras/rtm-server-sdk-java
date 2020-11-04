package com.fpnn.rtm.api;

import com.fpnn.rtm.RTMException;
import com.fpnn.rtm.RTMMessageType;
import com.fpnn.rtm.RTMServerClientBase.RTMHistoryMessageUnit;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Set;

public interface ChatAPI extends MessageCoreAPI {
    //-----------------[ sendmsg ]-----------------//
    default void sendChat(long fromUid, long toUid, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        internalCoreSendMessage(fromUid, toUid, RTMMessageType.Chat.value(), message, attrs, callback, timeoutInseconds);
    }

    default void sendChat(long fromUid, long toUid, String message, String attrs, SendMessageLambdaCallback callback) {
        internalCoreSendMessage(fromUid, toUid, RTMMessageType.Chat.value(), message, attrs, callback, 0);
    }

    default void sendCmd(long fromUid, long toUid, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        internalCoreSendMessage(fromUid, toUid, RTMMessageType.Cmd.value(), message, attrs, callback, timeoutInseconds);
    }

    default void sendCmd(long fromUid, long toUid, String message, String attrs, SendMessageLambdaCallback callback) {
        internalCoreSendMessage(fromUid, toUid, RTMMessageType.Cmd.value(), message, attrs, callback, 0);
    }

    //----
    default long sendChat(long fromUid, long toUid, String message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        return internalCoreSendMessage(fromUid, toUid, RTMMessageType.Chat.value(), message, attrs, timeoutInseconds);
    }

    default long sendChat(long fromUid, long toUid,  String message, String attrs)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalCoreSendMessage(fromUid, toUid, RTMMessageType.Chat.value(), message, attrs, 0);
    }

    default long sendCmd(long fromUid, long toUid, String message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        return internalCoreSendMessage(fromUid, toUid, RTMMessageType.Cmd.value(), message, attrs, timeoutInseconds);
    }

    default long sendCmd(long fromUid, long toUid, String message, String attrs)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalCoreSendMessage(fromUid, toUid, RTMMessageType.Cmd.value(), message, attrs, 0);
    }

    //-----------------[ sendmsgs ]-----------------//

    default void sendChats(long fromUid, Set<Long> toUids, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        internalCoreSendMessages(fromUid, toUids, RTMMessageType.Chat.value(), message, attrs, callback, timeoutInseconds);
    }

    default void sendChats(long fromUid, Set<Long> toUids, String message, String attrs, SendMessageLambdaCallback callback) {
        internalCoreSendMessages(fromUid, toUids, RTMMessageType.Chat.value(), message, attrs, callback, 0);
    }

    default void sendCmds(long fromUid, Set<Long> toUids, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        internalCoreSendMessages(fromUid, toUids, RTMMessageType.Cmd.value(), message, attrs, callback, timeoutInseconds);
    }

    default void sendCmds(long fromUid, Set<Long> toUids, String message, String attrs, SendMessageLambdaCallback callback) {
        internalCoreSendMessages(fromUid, toUids, RTMMessageType.Cmd.value(), message, attrs, callback, 0);
    }

    //----
    default long sendChats(long fromUid, Set<Long> toUids, String message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        return internalCoreSendMessages(fromUid, toUids, RTMMessageType.Chat.value(), message, attrs, timeoutInseconds);
    }

    default long sendChats(long fromUid, Set<Long> toUids, String message, String attrs)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalCoreSendMessages(fromUid, toUids, RTMMessageType.Chat.value(), message, attrs, 0);
    }

    default long sendCmds(long fromUid, Set<Long> toUids, String message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        return internalCoreSendMessages(fromUid, toUids, RTMMessageType.Cmd.value(), message, attrs, timeoutInseconds);
    }

    default long sendCmds(long fromUid, Set<Long> toUids, String message, String attrs)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalCoreSendMessages(fromUid, toUids, RTMMessageType.Cmd.value(), message, attrs, 0);
    }

    //-----------------[ sendgroupmsg ]-----------------//

    default void sendGroupChat(long fromUid, long groupId, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        internalCoreSendGroupMessage(fromUid, groupId, RTMMessageType.Chat.value(), message, attrs, callback, timeoutInseconds);
    }

    default void sendGroupChat(long fromUid, long groupId, String message, String attrs, SendMessageLambdaCallback callback) {
        internalCoreSendGroupMessage(fromUid, groupId, RTMMessageType.Chat.value(), message, attrs, callback, 0);
    }

    default void sendGroupCmd(long fromUid, long groupId, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        internalCoreSendGroupMessage(fromUid, groupId, RTMMessageType.Cmd.value(), message, attrs, callback, timeoutInseconds);
    }

    default void sendGroupCmd(long fromUid, long groupId, String message, String attrs, SendMessageLambdaCallback callback) {
        internalCoreSendGroupMessage(fromUid, groupId, RTMMessageType.Cmd.value(), message, attrs, callback, 0);
    }

    //----
    default long sendGroupChat(long fromUid, long groupId, String message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        return internalCoreSendGroupMessage(fromUid, groupId, RTMMessageType.Chat.value(), message, attrs, timeoutInseconds);
    }

    default long sendGroupChat(long fromUid, long groupId, String message, String attrs)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalCoreSendGroupMessage(fromUid, groupId, RTMMessageType.Chat.value(), message, attrs, 0);
    }

    default long sendGroupCmd(long fromUid, long groupId, String message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {

        return internalCoreSendGroupMessage(fromUid, groupId, RTMMessageType.Cmd.value(), message, attrs, timeoutInseconds);
    }

    default long sendGroupCmd(long fromUid, long groupId, String message, String attrs)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalCoreSendGroupMessage(fromUid, groupId, RTMMessageType.Cmd.value(), message, attrs, 0);
    }

    //-----------------[ sendroommsg ]-----------------//

    default void sendRoomChat(long fromUid, long roomId, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        internalCoreSendRoomMessage(fromUid, roomId, RTMMessageType.Chat.value(), message, attrs, callback, timeoutInseconds);
    }

    default void sendRoomChat(long fromUid, long roomId, String message, String attrs, SendMessageLambdaCallback callback) {
        internalCoreSendRoomMessage(fromUid, roomId, RTMMessageType.Chat.value(), message, attrs, callback, 0);
    }

    default void sendRoomCmd(long fromUid, long roomId, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        internalCoreSendRoomMessage(fromUid, roomId, RTMMessageType.Cmd.value(), message, attrs, callback, timeoutInseconds);
    }

    default void sendRoomCmd(long fromUid, long roomId, String message, String attrs, SendMessageLambdaCallback callback) {
        internalCoreSendRoomMessage(fromUid, roomId, RTMMessageType.Cmd.value(), message, attrs, callback, 0);
    }

    //----
    default long sendRoomChat(long fromUid, long roomId, String message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalCoreSendRoomMessage(fromUid, roomId, RTMMessageType.Chat.value(), message, attrs, timeoutInseconds);
    }

    default long sendRoomChat(long fromUid, long roomId, String message, String attrs)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalCoreSendRoomMessage(fromUid, roomId, RTMMessageType.Chat.value(), message, attrs, 0);
    }

    default long sendRoomCmd(long fromUid, long roomId, String message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalCoreSendRoomMessage(fromUid, roomId, RTMMessageType.Cmd.value(), message, attrs, timeoutInseconds);
    }

    default long sendRoomCmd(long fromUid, long roomId,  String message, String attrs)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalCoreSendRoomMessage(fromUid, roomId, RTMMessageType.Cmd.value(), message, attrs, 0);
    }

    //-----------------[ broadcastmsg ]-----------------//

    default void sendBroadcastChat(long fromUid, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        internalCoreSendBroadcastMessage(fromUid, RTMMessageType.Chat.value(), message, attrs, callback, timeoutInseconds);
    }

    default void sendBroadcastChat(long fromUid, String message, String attrs, SendMessageLambdaCallback callback) {
        internalCoreSendBroadcastMessage(fromUid, RTMMessageType.Chat.value(), message, attrs, callback, 0);
    }

    default void sendBroadcastCmd(long fromUid, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds) {
        internalCoreSendBroadcastMessage(fromUid, RTMMessageType.Cmd.value(), message, attrs, callback, timeoutInseconds);
    }

    default void sendBroadcastCmd(long fromUid, String message, String attrs, SendMessageLambdaCallback callback) {
        internalCoreSendBroadcastMessage(fromUid, RTMMessageType.Cmd.value(), message, attrs, callback, 0);
    }

    //----
    default long sendBroadcastChat(long fromUid, String message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalCoreSendBroadcastMessage(fromUid, RTMMessageType.Chat.value(), message, attrs, timeoutInseconds);
    }

    default long sendBroadcastChat(long fromUid, String message, String attrs)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalCoreSendBroadcastMessage(fromUid, RTMMessageType.Chat.value(), message, attrs, 0);
    }

    default long sendBroadcastCmd(long fromUid, String message, String attrs, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalCoreSendBroadcastMessage(fromUid, RTMMessageType.Cmd.value(), message, attrs, timeoutInseconds);
    }

    default long sendBroadcastCmd(long fromUid, String message, String attrs)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return internalCoreSendBroadcastMessage(fromUid, RTMMessageType.Cmd.value(), message, attrs, 0);
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

    default void deleteP2PChat(long messageId, long fromUid, long toUid)
            throws RTMException, GeneralSecurityException, IOException,InterruptedException {
        deleteChat(messageId, fromUid, toUid, MessageType.MESSAGE_TYPE_P2P);
    }

    default void deleteP2PChat(long messageId, long fromUid, long toUid, int timeInseconds)
            throws RTMException, GeneralSecurityException, IOException,InterruptedException {
        deleteChat(messageId, fromUid, toUid, MessageType.MESSAGE_TYPE_P2P, timeInseconds);
    }

    default void deleteGroupChat(long messageId, long fromUid, long groupId)
            throws RTMException, GeneralSecurityException, IOException,InterruptedException {
        deleteChat(messageId, fromUid, groupId, MessageType.MESSAGE_TYPE_GROUP);
    }

    default void deleteGroupChat(long messageId, long fromUid, long groupId, int timeInseconds)
            throws RTMException, GeneralSecurityException, IOException,InterruptedException {
        deleteChat(messageId, fromUid, groupId, MessageType.MESSAGE_TYPE_GROUP, timeInseconds);
    }

    default void deleteRoomChat(long messageId, long fromUid, long roomId)
            throws RTMException, GeneralSecurityException, IOException,InterruptedException {
        deleteChat(messageId, fromUid, roomId, MessageType.MESSAGE_TYPE_ROOM);
    }

    default void deleteRoomChat(long messageId, long fromUid, long roomId, int timeInseconds)
            throws RTMException, GeneralSecurityException, IOException,InterruptedException {
        deleteChat(messageId, fromUid, roomId, MessageType.MESSAGE_TYPE_ROOM, timeInseconds);
    }

    default void deleteBroadcastChat(long messageId, long fromUid)
            throws RTMException, GeneralSecurityException, IOException,InterruptedException {
        deleteChat(messageId, fromUid, 0, MessageType.MESSAGE_TYPE_BROADCAST);
    }

    default void deleteBroadcastChat(long messageId, long fromUid, int timeInseconds)
            throws RTMException, GeneralSecurityException, IOException,InterruptedException {
        deleteChat(messageId, fromUid, 0, MessageType.MESSAGE_TYPE_BROADCAST, timeInseconds);
    }

    default void deleteChat(long mid, long from, long xid, MessageType type, DoneLambdaCallback callback){
        deleteChat(mid, from, xid, type, callback,0);
    }

    default void deleteChat(long mid, long from, long xid, MessageType type, DoneLambdaCallback callback, int timeoutInseconds) {
        internalDelMsg(mid, from, xid, type, callback, timeoutInseconds);
    }

    default void deleteP2PChat(long messageId, long fromUid, long toUid, DoneLambdaCallback callback){
        deleteChat(messageId, fromUid, toUid, MessageType.MESSAGE_TYPE_P2P, callback);
    }

    default void deleteP2PChat(long messageId, long fromUid, long toUid, DoneLambdaCallback callback, int timeInseconds){
        deleteChat(messageId, fromUid, toUid, MessageType.MESSAGE_TYPE_P2P, callback, timeInseconds);
    }

    default void deleteGroupChat(long messageId, long fromUid, long groupId, DoneLambdaCallback callback){
        deleteChat(messageId, fromUid, groupId, MessageType.MESSAGE_TYPE_GROUP, callback);
    }

    default void deleteGroupChat(long messageId, long fromUid, long groupId, DoneLambdaCallback callback, int timeInseconds){
        deleteChat(messageId, fromUid, groupId, MessageType.MESSAGE_TYPE_GROUP, callback, timeInseconds);
    }

    default void deleteRoomChat(long messageId, long fromUid, long roomId, DoneLambdaCallback callback){
        deleteChat(messageId, fromUid, roomId, MessageType.MESSAGE_TYPE_ROOM, callback);
    }

    default void deleteRoomChat(long messageId, long fromUid, long roomId, DoneLambdaCallback callback, int timeInseconds){
        deleteChat(messageId, fromUid, roomId, MessageType.MESSAGE_TYPE_ROOM, callback, timeInseconds);
    }

    default void deleteBroadcastChat(long messageId, long fromUid, DoneLambdaCallback callback){
        deleteChat(messageId, fromUid, 0, MessageType.MESSAGE_TYPE_BROADCAST, callback);
    }

    default void deleteBroadcastChat(long messageId, long fromUid, DoneLambdaCallback callback, int timeInseconds){
        deleteChat(messageId, fromUid, 0, MessageType.MESSAGE_TYPE_BROADCAST, callback, timeInseconds);
    }

    //----------------------getChat-------------------------------
    default RTMHistoryMessageUnit getChat(long messageId, long from, long xid, MessageType type)
            throws RTMException, GeneralSecurityException, IOException,InterruptedException{
        return getChat(messageId, from, xid, type, 0);
    }

    default RTMHistoryMessageUnit getChat(long messageId, long from, long xid, MessageType type, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException,InterruptedException{
        return internalGetMsg(messageId, from, xid, type, timeoutInseconds);
    }

    default RTMHistoryMessageUnit getP2pChat(long messageId, long fromUid, long toUid)
            throws RTMException, GeneralSecurityException, IOException,InterruptedException {
        return getChat(messageId, fromUid, toUid, MessageType.MESSAGE_TYPE_P2P);
    }

    default RTMHistoryMessageUnit getP2pChat(long messageId, long fromUid, long toUid, int timeInseconds)
            throws RTMException, GeneralSecurityException, IOException,InterruptedException {
        return getChat(messageId, fromUid, toUid, MessageType.MESSAGE_TYPE_P2P, timeInseconds);
    }

    default RTMHistoryMessageUnit getGroupChat(long messageId, long fromUid, long groupId)
            throws RTMException, GeneralSecurityException, IOException,InterruptedException {
        return getChat(messageId, fromUid, groupId, MessageType.MESSAGE_TYPE_GROUP);
    }

    default RTMHistoryMessageUnit getGroupChat(long messageId, long fromUid, long groupId, int timeInseconds)
            throws RTMException, GeneralSecurityException, IOException,InterruptedException {
        return getChat(messageId, fromUid, groupId, MessageType.MESSAGE_TYPE_GROUP, timeInseconds);
    }

    default RTMHistoryMessageUnit getRoomChat(long messageId, long fromUid, long roomId)
            throws RTMException, GeneralSecurityException, IOException,InterruptedException {
        return getChat(messageId, fromUid, roomId, MessageType.MESSAGE_TYPE_ROOM);
    }

    default RTMHistoryMessageUnit getRoomChat(long messageId, long fromUid, long roomId, int timeInseconds)
            throws RTMException, GeneralSecurityException, IOException,InterruptedException {
        return getChat(messageId, fromUid, roomId, MessageType.MESSAGE_TYPE_ROOM, timeInseconds);
    }

    default RTMHistoryMessageUnit getBroadcastChat(long messageId, long fromUid)
            throws RTMException, GeneralSecurityException, IOException,InterruptedException {
        return getChat(messageId, fromUid, 0, MessageType.MESSAGE_TYPE_BROADCAST);
    }

    default RTMHistoryMessageUnit getBroadcastChat(long messageId, long fromUid, int timeInseconds)
            throws RTMException, GeneralSecurityException, IOException,InterruptedException {
        return getChat(messageId, fromUid, 0, MessageType.MESSAGE_TYPE_BROADCAST, timeInseconds);
    }

    default void getChat(long messageId, long from, long xid, MessageType type, GetRetrievedMessageLambdaCallback callback){
        getChat(messageId, from, xid, type, callback,0);
    }

    default void getChat(long messageId, long from, long xid, MessageType type, GetRetrievedMessageLambdaCallback callback, int timeoutInseconds) {
        internalGetMsg(messageId, from, xid, type, callback, timeoutInseconds);
    }

    default void getP2PChat(long messageId, long fromUid, long toUid, GetRetrievedMessageLambdaCallback callback){
        getChat(messageId, fromUid, toUid, MessageType.MESSAGE_TYPE_P2P, callback);
    }

    default void getP2PChat(long messageId, long fromUid, long toUid, GetRetrievedMessageLambdaCallback callback, int timeInseconds){
        getChat(messageId, fromUid, toUid, MessageType.MESSAGE_TYPE_P2P, callback, timeInseconds);
    }

    default void getGroupChat(long messageId, long fromUid, long groupId, GetRetrievedMessageLambdaCallback callback){
        getChat(messageId, fromUid, groupId, MessageType.MESSAGE_TYPE_GROUP, callback);
    }

    default void getGroupChat(long messageId, long fromUid, long groupId, GetRetrievedMessageLambdaCallback callback, int timeInseconds){
        getChat(messageId, fromUid, groupId, MessageType.MESSAGE_TYPE_GROUP, callback, timeInseconds);
    }

    default void getRoomChat(long messageId, long fromUid, long roomId, GetRetrievedMessageLambdaCallback callback){
        getChat(messageId, fromUid, roomId, MessageType.MESSAGE_TYPE_ROOM, callback);
    }

    default void getRoomChat(long messageId, long fromUid, long roomId, GetRetrievedMessageLambdaCallback callback, int timeInseconds){
        getChat(messageId, fromUid, roomId, MessageType.MESSAGE_TYPE_ROOM, callback, timeInseconds);
    }

    default void getBroadcastChat(long messageId, long fromUid, GetRetrievedMessageLambdaCallback callback){
        getChat(messageId, fromUid, 0, MessageType.MESSAGE_TYPE_BROADCAST, callback);
    }

    default void getBroadcastChat(long messageId, long fromUid, GetRetrievedMessageLambdaCallback callback, int timeInseconds){
        getChat(messageId, fromUid, 0, MessageType.MESSAGE_TYPE_BROADCAST, callback, timeInseconds);
    }
}
