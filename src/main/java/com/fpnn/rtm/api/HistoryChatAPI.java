package com.fpnn.rtm.api;

import com.fpnn.rtm.RTMException;
import com.fpnn.rtm.RTMServerClientBase.RTMHistoryMessage;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface HistoryChatAPI extends MessageCoreAPI{
    //-------------------getGroupHistoryChat--------------------------------

    default RTMHistoryMessage getGroupChat(long uid, long gid, boolean desc, int count, long begin, long end, long lastId, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return internalCoreGetGroupHistoryMsg(uid, gid, desc, count, begin, end, lastId, null, timeoutInseconds);
    }

    default RTMHistoryMessage getGroupChat(long uid, long gid, boolean desc, int count, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return getGroupChat(uid, gid, desc, count, 0, 0, 0, timeoutInseconds);
    }

    default RTMHistoryMessage getGroupChat(long uid, long gid, boolean desc, int count, long begin, long end, long lastId)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return getGroupChat(uid, gid, desc, count, begin, end, lastId,0);
    }

    default RTMHistoryMessage getGroupChat(long uid, long gid, boolean desc, int count)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return getGroupChat(uid, gid, desc, count, 0);
    }

    default void getGroupChat(long uid, long gid, boolean desc, int count, long begin, long end, long lastId, GetHistoryMessagesLambdaCallback callback, int timeoutInseconds){
        internalCoreGetGroupHistoryMsg(uid, gid, desc, count, begin, end, lastId, null, callback, timeoutInseconds);
    }

    default void getGroupChat(long uid, long gid, boolean desc, int count, GetHistoryMessagesLambdaCallback callback, int timeoutInseconds){
        getGroupChat(uid, gid, desc, count, 0, 0, 0, callback, timeoutInseconds);
    }

    default void getGroupChat(long uid, long gid, boolean desc, int count, long begin, long end, long lastId, GetHistoryMessagesLambdaCallback callback){
        getGroupChat(uid, gid, desc, count, begin, end, lastId, callback, 0);
    }

    default void getGroupChat(long uid, long gid, boolean desc, int count, GetHistoryMessagesLambdaCallback callback){
        getGroupChat(uid, gid, desc, count, callback, 0);
    }

    //-------------------getRoomHistoryChat--------------------------------

    default RTMHistoryMessage getRoomChat(long uid, long rid, boolean desc, int count, long begin, long end, long lastId, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return internalCoreGetRoomHistoryMsg(uid, rid, desc, count, begin, end, lastId, null, timeoutInseconds);
    }

    default RTMHistoryMessage getRoomChat(long uid, long rid, boolean desc, int count, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return getRoomChat(uid, rid, desc, count, 0, 0, 0, timeoutInseconds);
    }

    default RTMHistoryMessage getRoomChat(long uid, long rid, boolean desc, int count, long begin, long end, long lastId)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return getRoomChat(uid, rid, desc, count, begin, end, lastId, 0);
    }

    default RTMHistoryMessage getRoomChat(long uid, long rid, boolean desc, int count)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return getRoomChat(uid, rid, desc, count, 0);
    }

    default void getRoomChat(long uid, long rid, boolean desc, int count, long begin, long end, long lastId, GetHistoryMessagesLambdaCallback callback, int timeoutInseconds){
        internalCoreGetRoomHistoryMsg(uid, rid, desc, count, begin, end, lastId, null, callback, timeoutInseconds);
    }

    default void getRoomChat(long uid, long rid, boolean desc, int count, GetHistoryMessagesLambdaCallback callback, int timeoutInseconds){
        getRoomChat(uid, rid, desc, count, 0, 0, 0, callback, timeoutInseconds);
    }

    default void getRoomChat(long uid, long rid, boolean desc, int count, long begin, long end, long lastId, GetHistoryMessagesLambdaCallback callback){
        getRoomChat(uid, rid, desc, count, begin, end, lastId, callback, 0);
    }

    default void getRoomChat(long uid, long rid, boolean desc, int count, GetHistoryMessagesLambdaCallback callback){
        getRoomChat(uid, rid, desc, count, callback, 0);
    }

    //-------------------getBroadCastHistoryChat--------------------------------

    default RTMHistoryMessage getBroadCastChat(long uid, boolean desc, int count, long begin, long end, long lastId, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return internalCoreGetBroadCastHistoryMsg(uid, desc, count, begin, end, lastId, null, timeoutInseconds);
    }

    default RTMHistoryMessage getBroadCastChat(long uid, boolean desc, int count, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return getBroadCastChat(uid, desc, count, 0, 0, 0, timeoutInseconds);
    }

    default RTMHistoryMessage getBroadCastChat(long uid, boolean desc, int count, long begin, long end, long lastId)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return getBroadCastChat(uid, desc, count, begin, end, lastId, 0);
    }

    default RTMHistoryMessage getBroadCastChat(long uid, boolean desc, int count)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return getBroadCastChat(uid, desc, count, 0);
    }

    default void getBroadCastChat(long uid, boolean desc, int count, long begin, long end, long lastId, GetHistoryMessagesLambdaCallback callback, int timeoutInseconds){
        internalCoreGetBroadCastHistoryMsg(uid, desc, count, begin, end, lastId, null, callback, timeoutInseconds);
    }

    default void getBroadCastChat(long uid, boolean desc, int count, GetHistoryMessagesLambdaCallback callback, int timeoutInseconds){
        getBroadCastChat(uid, desc, count, 0, 0, 0, callback, timeoutInseconds);
    }

    default void getBroadCastChat(long uid, boolean desc, int count, long begin, long end, long lastId, GetHistoryMessagesLambdaCallback callback){
        getBroadCastChat(uid, desc, count, begin, end, lastId, callback, 0);
    }

    default void getBroadCastChat(long uid, boolean desc, int count, GetHistoryMessagesLambdaCallback callback){
        getBroadCastChat(uid, desc, count, callback, 0);
    }

    //-------------------getP2PHistoryChat--------------------------------

    default RTMHistoryMessage getP2PChat(long uid, long peerUid, boolean desc, int count, long begin, long end, long lastId, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return internalCoreGetP2PHistoryMsg(uid, peerUid, desc, count, begin, end, lastId, null, timeoutInseconds);
    }

    default RTMHistoryMessage getP2PChat(long uid, long peerUid, boolean desc, int count, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return getP2PChat(uid, peerUid, desc, count, 0, 0, 0, timeoutInseconds);
    }

    default RTMHistoryMessage getP2PChat(long uid, long peerUid, boolean desc, int count, long begin, long end, long lastId)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return getP2PChat(uid, peerUid, desc, count, begin, end, lastId, 0);
    }

    default RTMHistoryMessage getP2PChat(long uid, long peerUid, boolean desc, int count)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return getP2PChat(uid, peerUid, desc, count, 0);
    }

    default void getP2PChat(long uid, long peerUid, boolean desc, int count, long begin, long end, long lastId, GetHistoryMessagesLambdaCallback callback, int timeoutInseconds){
        internalCoreGetP2PHistoryMsg(uid, peerUid, desc, count, begin, end, lastId, null, callback, timeoutInseconds);
    }

    default void getP2PChat(long uid, long peerUid, boolean desc, int count, GetHistoryMessagesLambdaCallback callback, int timeoutInseconds){
        getP2PChat(uid, peerUid, desc, count, 0, 0, 0,  callback, timeoutInseconds);
    }

    default void getP2PChat(long uid, long peerUid, boolean desc, int count, long begin, long end, long lastId, GetHistoryMessagesLambdaCallback callback){
        getP2PChat(uid, peerUid, desc, count, begin, end, lastId, callback, 0);
    }

    default void getP2PChat(long uid, long peerUid, boolean desc, int count, GetHistoryMessagesLambdaCallback callback){
        getP2PChat(uid, peerUid, desc, count, callback, 0);
    }
}
