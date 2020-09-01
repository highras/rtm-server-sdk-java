package com.fpnn.rtm.api;

import com.fpnn.rtm.RTMServerClientBase.RTMMessage;

public interface ServerPushMonitorAPI {
//    void pushGroupMessage(long fromUid, long gId, byte mtype, long mid, String msg, String attr, long mtime);
//    void pushRoomMessage(long fromUid, long rId, byte mtype, long mid, String msg, String attr, long mtime);
//    void pushEvent(int pid, String event, long uid, int time, String endpoint, String data);
//    void pushP2PChat(long fromUid, long toUid, long mid, String msg, String attr, long mtime);
//    void pushGroupChat(long fromUid, long gId, long mid, String msg, String attr, long mtime);
//    void pushRoomChat(long fromUid, long rId, long mid, String msg, String attr, long mtime);
//    void pushP2PAudio(long fromUid, long toUid, long mid, byte[] msg, String attr, long mtime);
//    void pushGroupAudio(long fromUid, long gId, long mid, byte[] msg, String attr, long mtime);
//    void pushRoomAudio(long fromUid, long rId, long mid, byte[] msg, String attr, long mtime);
//    void pushP2PCmd(long fromUid, long toUid, long mid, String msg, String attr, long mtime);
//    void pushGroupCmd(long fromUid, long gId, long mid, String msg, String attr, long mtime);
//    void pushRoomCmd(long fromUid, long rId, long mid, String msg, String attr, long mtime);
//    void pushP2PFile(long fromUid, long toUid, byte mtype, long mid, String msg, String attr, long mtime);
//    void pushGroupFile(long fromUid, long gId, byte mtype, long mid, String msg, String attr, long mtime);
//    void pushRoomFile(long fromUid, long rId, byte mtype, long mid, String msg, String attr, long mtime);
//    void pushGroupMessage(long fromUid, long gId, byte mtype, long mid, String msg, String attr, long mtime);

    default void pushEvent(int projectId, String event, long uid, int time, String endpoint, String data){}
    // message
    default void pushRoomMessage(RTMMessage message){}
    default void pushP2PMessage(RTMMessage message){}
    default void pushGroupMessage(RTMMessage message) {}

    // chat
    default void pushRoomChat(RTMMessage message){}
    default void pushP2PChat(RTMMessage message){}
    default void pushGroupChat(RTMMessage message){}

    // audio
    default void pushRoomAudio(RTMMessage message){}
    default void pushP2PAudio(RTMMessage message){}
    default void pushGroupAudio(RTMMessage message){}

    // cmd
    default void pushRoomCmd(RTMMessage message){}
    default void pushP2PCmd(RTMMessage message){}
    default void pushGroupCmd(RTMMessage message){}

    //file
    default void pushP2PFile(RTMMessage message){}
    default void pushGroupFile(RTMMessage message){}
    default void pushRoomFile(RTMMessage message){}
}
