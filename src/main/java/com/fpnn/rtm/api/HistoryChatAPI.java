package com.fpnn.rtm.api;

import com.fpnn.rtm.RTMException;
import com.fpnn.rtm.RTMServerClientBase.RTMHistoryMessage;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface HistoryChatAPI extends MessageCoreAPI{
    //-------------------getGroupHistoryChat--------------------------------

    default RTMHistoryMessage getGroupChat(long uid, long groupId, boolean desc, int count, long begin, long end, long lastCursorId, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return internalCoreGetGroupHistoryMsg(uid, groupId, desc, count, begin, end, lastCursorId, null, timeoutInseconds);
    }

    default RTMHistoryMessage getGroupChat(long uid, long groupId, boolean desc, int count, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return getGroupChat(uid, groupId, desc, count, 0, 0, 0, timeoutInseconds);
    }

    default RTMHistoryMessage getGroupChat(long uid, long groupId, boolean desc, int count, long begin, long end, long lastCursorId)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return getGroupChat(uid, groupId, desc, count, begin, end, lastCursorId,0);
    }

    default RTMHistoryMessage getGroupChat(long uid, long groupId, boolean desc, int count)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return getGroupChat(uid, groupId, desc, count, 0);
    }

    default void getGroupChat(long uid, long groupId, boolean desc, int count, long begin, long end, long lastCursorId, GetHistoryMessagesLambdaCallback callback, int timeoutInseconds){
        internalCoreGetGroupHistoryMsg(uid, groupId, desc, count, begin, end, lastCursorId, null, callback, timeoutInseconds);
    }

    default void getGroupChat(long uid, long groupId, boolean desc, int count, GetHistoryMessagesLambdaCallback callback, int timeoutInseconds){
        getGroupChat(uid, groupId, desc, count, 0, 0, 0, callback, timeoutInseconds);
    }

    default void getGroupChat(long uid, long groupId, boolean desc, int count, long begin, long end, long lastCursorId, GetHistoryMessagesLambdaCallback callback){
        getGroupChat(uid, groupId, desc, count, begin, end, lastCursorId, callback, 0);
    }

    default void getGroupChat(long uid, long groupId, boolean desc, int count, GetHistoryMessagesLambdaCallback callback){
        getGroupChat(uid, groupId, desc, count, callback, 0);
    }

    //-------------------getRoomHistoryChat--------------------------------

    default RTMHistoryMessage getRoomChat(long uid, long roomId, boolean desc, int count, long begin, long end, long lastCursorId, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return internalCoreGetRoomHistoryMsg(uid, roomId, desc, count, begin, end, lastCursorId, null, timeoutInseconds);
    }

    default RTMHistoryMessage getRoomChat(long uid, long roomId, boolean desc, int count, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return getRoomChat(uid, roomId, desc, count, 0, 0, 0, timeoutInseconds);
    }

    default RTMHistoryMessage getRoomChat(long uid, long roomId, boolean desc, int count, long begin, long end, long lastCursorId)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return getRoomChat(uid, roomId, desc, count, begin, end, lastCursorId, 0);
    }

    default RTMHistoryMessage getRoomChat(long uid, long roomId, boolean desc, int count)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return getRoomChat(uid, roomId, desc, count, 0);
    }

    default void getRoomChat(long uid, long roomId, boolean desc, int count, long begin, long end, long lastCursorId, GetHistoryMessagesLambdaCallback callback, int timeoutInseconds){
        internalCoreGetRoomHistoryMsg(uid, roomId, desc, count, begin, end, lastCursorId, null, callback, timeoutInseconds);
    }

    default void getRoomChat(long uid, long roomId, boolean desc, int count, GetHistoryMessagesLambdaCallback callback, int timeoutInseconds){
        getRoomChat(uid, roomId, desc, count, 0, 0, 0, callback, timeoutInseconds);
    }

    default void getRoomChat(long uid, long roomId, boolean desc, int count, long begin, long end, long lastCursorId, GetHistoryMessagesLambdaCallback callback){
        getRoomChat(uid, roomId, desc, count, begin, end, lastCursorId, callback, 0);
    }

    default void getRoomChat(long uid, long roomId, boolean desc, int count, GetHistoryMessagesLambdaCallback callback){
        getRoomChat(uid, roomId, desc, count, callback, 0);
    }

    //-------------------getBroadCastHistoryChat--------------------------------

    default RTMHistoryMessage getBroadCastChat(long uid, boolean desc, int count, long begin, long end, long lastCursorId, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return internalCoreGetBroadCastHistoryMsg(uid, desc, count, begin, end, lastCursorId, null, timeoutInseconds);
    }

    default RTMHistoryMessage getBroadCastChat(long uid, boolean desc, int count, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return getBroadCastChat(uid, desc, count, 0, 0, 0, timeoutInseconds);
    }

    default RTMHistoryMessage getBroadCastChat(long uid, boolean desc, int count, long begin, long end, long lastCursorId)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return getBroadCastChat(uid, desc, count, begin, end, lastCursorId, 0);
    }

    default RTMHistoryMessage getBroadCastChat(long uid, boolean desc, int count)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return getBroadCastChat(uid, desc, count, 0);
    }

    default void getBroadCastChat(long uid, boolean desc, int count, long begin, long end, long lastCursorId, GetHistoryMessagesLambdaCallback callback, int timeoutInseconds){
        internalCoreGetBroadCastHistoryMsg(uid, desc, count, begin, end, lastCursorId, null, callback, timeoutInseconds);
    }

    default void getBroadCastChat(long uid, boolean desc, int count, GetHistoryMessagesLambdaCallback callback, int timeoutInseconds){
        getBroadCastChat(uid, desc, count, 0, 0, 0, callback, timeoutInseconds);
    }

    default void getBroadCastChat(long uid, boolean desc, int count, long begin, long end, long lastCursorId, GetHistoryMessagesLambdaCallback callback){
        getBroadCastChat(uid, desc, count, begin, end, lastCursorId, callback, 0);
    }

    default void getBroadCastChat(long uid, boolean desc, int count, GetHistoryMessagesLambdaCallback callback){
        getBroadCastChat(uid, desc, count, callback, 0);
    }

    //-------------------getP2PHistoryChat--------------------------------

    default RTMHistoryMessage getP2PChat(long uid, long peerUid, boolean desc, int count, long begin, long end, long lastCursorId, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return internalCoreGetP2PHistoryMsg(uid, peerUid, desc, count, begin, end, lastCursorId, null, timeoutInseconds);
    }

    default RTMHistoryMessage getP2PChat(long uid, long peerUid, boolean desc, int count, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return getP2PChat(uid, peerUid, desc, count, 0, 0, 0, timeoutInseconds);
    }

    default RTMHistoryMessage getP2PChat(long uid, long peerUid, boolean desc, int count, long begin, long end, long lastCursorId)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return getP2PChat(uid, peerUid, desc, count, begin, end, lastCursorId, 0);
    }

    default RTMHistoryMessage getP2PChat(long uid, long peerUid, boolean desc, int count)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return getP2PChat(uid, peerUid, desc, count, 0);
    }

    default void getP2PChat(long uid, long peerUid, boolean desc, int count, long begin, long end, long lastCursorId, GetHistoryMessagesLambdaCallback callback, int timeoutInseconds){
        internalCoreGetP2PHistoryMsg(uid, peerUid, desc, count, begin, end, lastCursorId, null, callback, timeoutInseconds);
    }

    default void getP2PChat(long uid, long peerUid, boolean desc, int count, GetHistoryMessagesLambdaCallback callback, int timeoutInseconds){
        getP2PChat(uid, peerUid, desc, count, 0, 0, 0,  callback, timeoutInseconds);
    }

    default void getP2PChat(long uid, long peerUid, boolean desc, int count, long begin, long end, long lastCursorId, GetHistoryMessagesLambdaCallback callback){
        getP2PChat(uid, peerUid, desc, count, begin, end, lastCursorId, callback, 0);
    }

    default void getP2PChat(long uid, long peerUid, boolean desc, int count, GetHistoryMessagesLambdaCallback callback){
        getP2PChat(uid, peerUid, desc, count, callback, 0);
    }
}
