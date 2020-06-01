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
        String msg = (String)quest.get("msg", "");
        String attrs = (String)quest.get("attrs", "");
        long mtime = quest.getLong("mtime", 0);

        if(dupFilter.checkP2PMessage(from, to, mid)){
            if(mtype == MType.Chat.value()){
                monitor.pushP2PChat(from, to, mid, msg, attrs, mtime);
            }
            else if(mtype == MType.AudioChat.value()){
                try{
                    monitor.pushP2PAudio(from, to, mid, msg.getBytes("UTF-8"), attrs, mtime);
                }catch (IOException ex){
                    ex.printStackTrace();
                    ErrorRecorder.record("[ERROR] RTMServer pushmsg exception.", ex);
                }
            }
            else if(mtype == MType.Cmd.value()){
                monitor.pushP2PCmd(from, to, mid, msg, attrs, mtime);
            }
            else{
                monitor.pushP2PMessage(from, to, mtype, mid, msg, attrs, mtime);
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
        String msg = (String)quest.get("msg", "");
        String attrs = (String)quest.get("attrs", "");
        long mtime = quest.getLong("mtime", 0);

        if(dupFilter.checkGroupMessage(from, gid, mid)){
            if(mtype == MType.Chat.value()){
                monitor.pushGroupChat(from, gid, mid, msg, attrs, mtime);
            }
            else if(mtype == MType.AudioChat.value()){
                try{
                    monitor.pushGroupAudio(from, gid, mid, msg.getBytes("UTF-8"), attrs, mtime);
                }catch (IOException ex){
                    ex.printStackTrace();
                    ErrorRecorder.record("[ERROR] RTMServer pushgroupmsg exception.", ex);
                }
            }
            else if(mtype == MType.Cmd.value()){
                monitor.pushGroupCmd(from, gid, mid, msg, attrs, mtime);
            }
            else{
                monitor.pushGroupMessage(from, gid, mtype, mid, msg, attrs, mtime);
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
        String msg = (String)quest.get("msg", "");
        String attrs = (String)quest.get("attrs", "");
        long mtime = quest.getLong("mtime", 0);

        if(dupFilter.checkRoomMessage(from, rid, mid)){
            if(mtype == MType.Chat.value()){
                monitor.pushRoomChat(from, rid, mid, msg, attrs, mtime);
            }
            else if(mtype == MType.AudioChat.value()){
                try{
                    monitor.pushRoomAudio(from, rid, mid, msg.getBytes("UTF-8"), attrs, mtime);
                }catch (IOException ex){
                    ex.printStackTrace();
                    ErrorRecorder.record("[ERROR] RTMServer pushroommsg exception.", ex);
                }
            }
            else if(mtype == MType.Cmd.value()){
                monitor.pushRoomCmd(from, rid, mid, msg, attrs, mtime);
            }
            else{
                monitor.pushRoomMessage(from, rid, mtype, mid, msg, attrs, mtime);
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

    public Answer pushfile(Quest quest, InetSocketAddress peer){
        if(monitor == null){
            ErrorRecorder.record("[ERROR] RTMServerPushMonitor is unconfiged.");
            return new Answer(quest);
        }
        long from = quest.getLong("from", 0);
        long to = quest.getLong("to", 0);
        byte mtype = (byte)quest.getInt("mtype", 0);
        long mid = quest.getLong("mid", 0);
        String msg = (String)quest.get("msg", "");
        String attrs = (String)quest.get("attrs", "");
        long mtime = quest.getLong("mtime", 0);
        monitor.pushP2PFile(from, to, mtype, mid, msg, attrs, mtime);

        return new Answer(quest);
    }

    public Answer pushgroupfile(Quest quest, InetSocketAddress peer){
        if(monitor == null){
            ErrorRecorder.record("[ERROR] RTMServerPushMonitor is unconfiged.");
            return new Answer(quest);
        }

        long from = quest.getLong("from", 0);
        long gid = quest.getLong("gid", 0);
        byte mtype = (byte)quest.getInt("mtype", 0);
        long mid = quest.getLong("mid", 0);
        String msg = (String)quest.get("msg");
        String attrs = (String)quest.get("attrs");
        long mtime = quest.getLong("mtime", 0);
        monitor.pushGroupFile(from, gid, mtype, mid, msg, attrs, mtime);

        return new Answer(quest);
    }

    public Answer pushroomfile(Quest quest, InetSocketAddress peer){
        if(monitor == null){
            ErrorRecorder.record("[ERROR] RTMServerPushMonitor is unconfiged.");
            return new Answer(quest);
        }

        long from = quest.getLong("from", 0);
        long rid = quest.getLong("rid", 0);
        byte mtype = (byte)quest.getInt("mtype", 0);
        long mid = quest.getLong("mid", 0);
        String msg = (String)quest.get("msg", "");
        String attrs = (String)quest.get("attrs", "");
        long mtime = quest.getLong("mtime", 0);
        monitor.pushRoomFile(from, rid, mtype, mid, msg, attrs, mtime);

        return new Answer(quest);
    }
}
