package com.fpnn.rtm.api;
import com.fpnn.rtm.RTMException;
import com.fpnn.rtm.RTMServerClientBase.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public interface HistoryMessageAPI extends MessageCoreAPI{

    //-------------------getGroupHistorymsg--------------------------------

    default RTMHistoryMessage getGroupMsg(long uid, long gid, boolean desc, int count, long begin, long end, long lastId, List<Byte> mtypes, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return internalCoreGetGroupHistoryMsg(uid, gid, desc, count, begin, end, lastId, mtypes, timeoutInseconds);
    }

    default RTMHistoryMessage getGroupMsg(long uid, long gid, boolean desc, int count, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return getGroupMsg(uid, gid, desc, count, 0, 0, 0, null, timeoutInseconds);
    }

    default RTMHistoryMessage getGroupMsg(long uid, long gid, boolean desc, int count, long begin, long end, long lastId, List<Byte> mtypes)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return getGroupMsg(uid, gid, desc, count, begin, end, lastId, mtypes, 0);
    }

    default RTMHistoryMessage getGroupMsg(long uid, long gid, boolean desc, int count)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return getGroupMsg(uid, gid, desc, count, 0);
    }

    default void getGroupMsg(long uid, long gid, boolean desc, int count, long begin, long end, long lastId, List<Byte> mtypes, GetHistoryMessagesLambdaCallback callback, int timeoutInseconds){
        internalCoreGetGroupHistoryMsg(uid, gid, desc, count, begin, end, lastId, mtypes, callback, timeoutInseconds);
    }

    default void getGroupMsg(long uid, long gid, boolean desc, int count, GetHistoryMessagesLambdaCallback callback, int timeoutInseconds){
        getGroupMsg(uid, gid, desc, count, 0, 0, 0, null, callback, timeoutInseconds);
    }

    default void getGroupMsg(long uid, long gid, boolean desc, int count, long begin, long end, long lastId, List<Byte> mtype, GetHistoryMessagesLambdaCallback callback){
        getGroupMsg(uid, gid, desc, count, begin, end, lastId, mtype, callback, 0);
    }

    default void getGroupMsg(long uid, long gid, boolean desc, int count, GetHistoryMessagesLambdaCallback callback){
        getGroupMsg(uid, gid, desc, count, callback, 0);
    }

    //-------------------getRoomHistorymsg--------------------------------

    default RTMHistoryMessage getRoomMsg(long uid, long rid, boolean desc, int count, long begin, long end, long lastId, List<Byte> mtypes, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return internalCoreGetRoomHistoryMsg(uid, rid, desc, count, begin, end, lastId, mtypes, timeoutInseconds);
    }

    default RTMHistoryMessage getRoomMsg(long uid, long rid, boolean desc, int count, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return getRoomMsg(uid, rid, desc, count, 0, 0, 0, null, timeoutInseconds);
    }

    default RTMHistoryMessage getRoomMsg(long uid, long rid, boolean desc, int count, long begin, long end, long lastId, List<Byte> mtypes)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return getRoomMsg(uid, rid, desc, count, begin, end, lastId, mtypes, 0);
    }

    default RTMHistoryMessage getRoomMsg(long uid, long rid, boolean desc, int count)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return getRoomMsg(uid, rid, desc, count, 0);
    }

    default void getRoomMsg(long uid, long rid, boolean desc, int count, long begin, long end, long lastId, List<Byte> mtypes, GetHistoryMessagesLambdaCallback callback, int timeoutInseconds){
        internalCoreGetRoomHistoryMsg(uid, rid, desc, count, begin, end, lastId, mtypes, callback, timeoutInseconds);
    }

    default void getRoomMsg(long uid, long rid, boolean desc, int count, GetHistoryMessagesLambdaCallback callback, int timeoutInseconds){
        getRoomMsg(uid, rid, desc, count, 0, 0, 0, null, callback, timeoutInseconds);
    }

    default void getRoomMsg(long uid, long rid, boolean desc, int count, long begin, long end, long lastId, List<Byte> mtype, GetHistoryMessagesLambdaCallback callback){
        getRoomMsg(uid, rid, desc, count, begin, end, lastId, mtype, callback, 0);
    }

    default void getRoomMsg(long uid, long rid, boolean desc, int count, GetHistoryMessagesLambdaCallback callback){
        getRoomMsg(uid, rid, desc, count, callback, 0);
    }

    //-------------------getBroadCastHistorymsg--------------------------------

    default RTMHistoryMessage getBroadCastMsg(long uid, boolean desc, int count, long begin, long end, long lastId, List<Byte> mtypes, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return internalCoreGetBroadCastHistoryMsg(uid, desc, count, begin, end, lastId, mtypes, timeoutInseconds);
    }

    default RTMHistoryMessage getBroadCastMsg(long uid, boolean desc, int count, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return getBroadCastMsg(uid, desc, count, 0, 0, 0, null, timeoutInseconds);
    }

    default RTMHistoryMessage getBroadCastMsg(long uid, boolean desc, int count, long begin, long end, long lastId, List<Byte> mtypes)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return getBroadCastMsg(uid, desc, count, begin, end, lastId, mtypes, 0);
    }

    default RTMHistoryMessage getBroadCastMsg(long uid, boolean desc, int count)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return getBroadCastMsg(uid, desc, count, 0);
    }

    default void getBroadCastMsg(long uid, boolean desc, int count, long begin, long end, long lastId, List<Byte> mtypes, GetHistoryMessagesLambdaCallback callback, int timeoutInseconds){
        internalCoreGetBroadCastHistoryMsg(uid, desc, count, begin, end, lastId, mtypes, callback, timeoutInseconds);
    }

    default void getBroadCastMsg(long uid, boolean desc, int count, GetHistoryMessagesLambdaCallback callback, int timeoutInseconds){
        getBroadCastMsg(uid, desc, count, 0, 0, 0, null, callback, timeoutInseconds);
    }

    default void getBroadCastMsg(long uid, boolean desc, int count, long begin, long end, long lastId, List<Byte> mtype, GetHistoryMessagesLambdaCallback callback){
        getBroadCastMsg(uid, desc, count, begin, end, lastId, mtype, callback, 0);
    }

    default void getBroadCastMsg(long uid, boolean desc, int count, GetHistoryMessagesLambdaCallback callback){
        getBroadCastMsg(uid, desc, count, callback, 0);
    }

    //-------------------getP2PHistorymsg--------------------------------

    default RTMHistoryMessage getP2PMsg(long uid, long peerUid, boolean desc, int count, long begin, long end, long lastId, List<Byte> mtypes, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return internalCoreGetP2PHistoryMsg(uid, peerUid, desc, count, begin, end, lastId, mtypes, timeoutInseconds);
    }

    default RTMHistoryMessage getP2PMsg(long uid, long peerUid, boolean desc, int count, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return getP2PMsg(uid, peerUid, desc, count, 0, 0, 0, null, timeoutInseconds);
    }

    default RTMHistoryMessage getP2PMsg(long uid, long peerUid, boolean desc, int count, long begin, long end, long lastId, List<Byte> mtypes)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return getP2PMsg(uid, peerUid, desc, count, begin, end, lastId, mtypes, 0);
    }

    default RTMHistoryMessage getP2PMsg(long uid, long peerUid, boolean desc, int count)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return getP2PMsg(uid, peerUid, desc, count, 0);
    }

    default void getP2PMsg(long uid, long peerUid, boolean desc, int count, long begin, long end, long lastId, List<Byte> mtypes, GetHistoryMessagesLambdaCallback callback, int timeoutInseconds){
        internalCoreGetP2PHistoryMsg(uid, peerUid, desc, count, begin, end, lastId, mtypes, callback, timeoutInseconds);
    }

    default void getP2PMsg(long uid, long peerUid, boolean desc, int count, GetHistoryMessagesLambdaCallback callback, int timeoutInseconds){
        getP2PMsg(uid, peerUid, desc, count, 0, 0, 0, null, callback, timeoutInseconds);
    }

    default void getP2PMsg(long uid, long peerUid, boolean desc, int count, long begin, long end, long lastId, List<Byte> mtype, GetHistoryMessagesLambdaCallback callback){
        getP2PMsg(uid, peerUid, desc, count, begin, end, lastId, mtype, callback, 0);
    }

    default void getP2PMsg(long uid, long peerUid, boolean desc, int count, GetHistoryMessagesLambdaCallback callback){
        getP2PMsg(uid, peerUid, desc, count, callback, 0);
    }

}
