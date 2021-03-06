package com.fpnn.rtm;

import com.fpnn.rtm.api.ServerPushMonitorAPI;
import com.fpnn.sdk.ErrorRecorder;
import com.fpnn.sdk.proto.Answer;
import com.fpnn.sdk.proto.Quest;
import java.io.IOException;
import java.net.InetSocketAddress;

public class RTMServerQuestProcessor {
    private ServerPushMonitorAPI monitor;
    private RTMDuplicatedMessageFilter dupFilter;

    public RTMServerQuestProcessor(){
        dupFilter = new RTMDuplicatedMessageFilter();
    }

    public void setMonitor(ServerPushMonitorAPI pushMonitor){
        monitor = pushMonitor;
    }

    public Answer ping(Quest quest, InetSocketAddress peer){
        return new Answer(quest);
    }

    public Answer pushmsg(Quest quest, InetSocketAddress peer){
        if(monitor == null){
            ErrorRecorder.record("[ERROR] RTMServerPushMonitor is unconfiged.");
            return new Answer(quest);
        }

        long from = quest.getLong("from", 0);
        long to = quest.getLong("to", 0);
        byte mtype = (byte)quest.getInt("mtype", 0);
        long mid = quest.getLong("mid", 0);
        Object msg = quest.get("msg", "");
        String attrs = (String)quest.get("attrs", "");
        long mtime = quest.getLong("mtime", 0);

        if(dupFilter.checkP2PMessage(from, to, mid)){
            RTMServerClientBase.RTMMessage message = new RTMServerClientBase.RTMMessage();
            message.fromUid = from;
            message.toId = to;
            message.messageType = mtype;
            message.messageId = mid;
            message.modifiedTime = mtime;
            message.attrs = attrs;
            message.stringMessage = (String)msg;
            if(mtype == RTMMessageType.Chat.value()){
                monitor.pushP2PChat(message);
            }
            else if(mtype == RTMMessageType.Cmd.value()){
                monitor.pushP2PCmd(message);
            }
            else if (mtype >= RTMMessageType.ImageFile.value() && mtype <= RTMMessageType.NormalFile.value())
            {
                message.fileMsgInfo = RTMServerClientBase.processFileInfo(message.stringMessage, message.attrs, message.messageType);
                message.attrs = RTMServerClientBase.fetchFileCustomAttrs(message.attrs);
                monitor.pushP2PFile(message);
            }
            else{
                monitor.pushP2PMessage(message);
            }
        }

        return new Answer(quest);
    }

    public Answer pushgroupmsg(Quest quest, InetSocketAddress peer){
        if(monitor == null){
            ErrorRecorder.record("[ERROR] RTMServerPushMonitor is unconfiged.");
            return new Answer(quest);
        }

        long from = quest.getLong("from", 0);
        long gid = quest.getLong("gid", 0);
        byte mtype = (byte)quest.getInt("mtype", 0);
        long mid = quest.getLong("mid", 0);
        Object msg = quest.get("msg", "");
        String attrs = (String)quest.get("attrs", "");
        long mtime = quest.getLong("mtime", 0);

        if(dupFilter.checkGroupMessage(from, gid, mid)){
            RTMServerClientBase.RTMMessage message = new RTMServerClientBase.RTMMessage();
            message.fromUid = from;
            message.toId = gid;
            message.messageType = mtype;
            message.messageId = mid;
            message.modifiedTime = mtime;
            message.attrs = attrs;
            message.stringMessage = (String)msg;
            if(mtype == RTMMessageType.Chat.value()){
                monitor.pushGroupChat(message);
            }
            else if(mtype == RTMMessageType.Cmd.value()){
                monitor.pushGroupCmd(message);
            }
            else if (mtype >= RTMMessageType.ImageFile.value() && mtype <= RTMMessageType.NormalFile.value())
            {
                message.fileMsgInfo = RTMServerClientBase.processFileInfo(message.stringMessage, message.attrs, message.messageType);
                message.attrs = RTMServerClientBase.fetchFileCustomAttrs(message.attrs);
                monitor.pushGroupFile(message);
            }
            else{
                monitor.pushGroupMessage(message);
            }
        }

        return new Answer(quest);
    }

    public Answer pushroommsg(Quest quest, InetSocketAddress peer){
        if(monitor == null){
            ErrorRecorder.record("[ERROR] RTMServerPushMonitor is unconfiged.");
            return new Answer(quest);
        }

        long from = quest.getLong("from", 0);
        long rid = quest.getLong("rid", 0);
        byte mtype = (byte)quest.getInt("mtype", 0);
        long mid = quest.getLong("mid", 0);
        Object msg = quest.get("msg", "");
        String attrs = (String)quest.get("attrs", "");
        long mtime = quest.getLong("mtime", 0);

        if(dupFilter.checkRoomMessage(from, rid, mid)){
            RTMServerClientBase.RTMMessage message = new RTMServerClientBase.RTMMessage();
            message.fromUid = from;
            message.toId = rid;
            message.messageType = mtype;
            message.messageId = mid;
            message.modifiedTime = mtime;
            message.attrs = attrs;
            message.stringMessage = (String)msg;
            if(mtype == RTMMessageType.Chat.value()){
                monitor.pushRoomChat(message);
            }
            else if(mtype == RTMMessageType.Cmd.value()){
                monitor.pushRoomCmd(message);
            }
            else if(mtype >= RTMMessageType.ImageFile.value() && mtype <= RTMMessageType.NormalFile.value()){
                message.fileMsgInfo = RTMServerClientBase.processFileInfo(message.stringMessage, message.attrs, message.messageType);
                message.attrs = RTMServerClientBase.fetchFileCustomAttrs(message.attrs);
                monitor.pushRoomFile(message);
            }
            else{
                monitor.pushRoomMessage(message);
            }
        }

        return new Answer(quest);
    }

    public Answer pushevent(Quest quest, InetSocketAddress peer){
        if(monitor == null){
            ErrorRecorder.record("[ERROR] RTMServerPushMonitor is unconfiged.");
            return new Answer(quest);
        }

        int pid = quest.getInt("pid", 0);
        String event = (String)quest.get("event");
        long uid = quest.getLong("uid", 0);
        int time = quest.getInt("time", 0);
        String endpoint = (String)quest.get("endpoint");
        String data = (String)quest.get("data", "");
        monitor.pushEvent(pid, event, uid, time, endpoint, data);

        return new Answer(quest);
    }
}
