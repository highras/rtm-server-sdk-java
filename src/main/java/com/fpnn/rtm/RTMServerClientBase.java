package com.fpnn.rtm;

import com.fpnn.rtm.api.ServerPushMonitorAPI;
import com.fpnn.sdk.*;
import com.fpnn.sdk.proto.Answer;
import com.fpnn.sdk.proto.Quest;
import com.google.gson.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Random;

public class RTMServerClientBase extends TCPClient {
    private int pid;
    private String basicToken;
    private RTMServerQuestProcessor processor;
    private static int questTimeout = 30;

    public RTMServerClientBase(int pid, String secretKey, String host, int port) {
        super(host, port);
        this.pid = pid;
        basicToken = pid + ":" + secretKey + ":";
        processor = new RTMServerQuestProcessor();
        setQuestTimeout(questTimeout);
        setQuestProcessor(processor, "com.fpnn.rtm.RTMServerQuestProcessor");
    }

    public void setServerPushMonitor(ServerPushMonitorAPI monitor){
        processor.setMonitor(monitor);
    }

    //-----------------------------------------------------//
    //--                  Private APIs                   --//
    //-----------------------------------------------------//
    protected static String bytesToHexString(byte[] bytes, boolean isLowerCase) {
        String from = isLowerCase ? "%02x" : "%02X";
        StringBuilder sb = new StringBuilder(bytes.length * 2);

        Formatter formatter = new Formatter(sb);
        for (byte b : bytes) {
            formatter.format(from, b);
        }

        return sb.toString();
    }

    //------------------------[ For Message APIs ]----------------------------//
    public static class MidGenerator {

        static private long count = 0;
        static private int randId = 0;
        static private final long randBits = 8;
        static private final long sequenceBits = 6;
        static private final long sequenceMask = -1 ^ (-1 << sequenceBits);
        static private long lastTime = 0;
        static public synchronized long gen() {
            if(randId == 0 ){
                Random random = new Random(System.currentTimeMillis());
                randId = random.nextInt(255) + 1;
            }
            long time = System.currentTimeMillis();
            count = (count + 1) & sequenceMask;
            if(count == 0){
                time = getNextMillis(lastTime);
            }
            lastTime = time;
            long id = (time<< (randBits + sequenceBits)) | randId<< sequenceBits| count;
            return id;
        }

        static private long getNextMillis(long lastTimestamp) {
            long timestamp = System.currentTimeMillis();
            while (timestamp <= lastTimestamp) {
                timestamp = System.currentTimeMillis();
            }
            return timestamp;
        }
    }

    protected String genMessageSign(String cmd, long slat, long ts) throws GeneralSecurityException, IOException {
        String token = basicToken + slat + ":" + cmd + ":" + ts;
        byte[] tokenString = token.getBytes("UTF-8");

        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(tokenString);
        byte[] md5Binary = md5.digest();
        return bytesToHexString(md5Binary, false);
    }

    //-----------------------------------------------------//
    //--              RTM Server Gate APIs               --//
    //-----------------------------------------------------//

    public Quest genBasicQuest(String method) throws GeneralSecurityException, IOException {
        long slat = MidGenerator.gen();
        long ts = System.currentTimeMillis() / 1000;
        String sign = genMessageSign(method, slat, ts);

        Quest quest = new Quest(method);
        quest.param("pid", pid);
        quest.param("sign", sign);
        quest.param("salt", slat);
        quest.param("ts", ts);

        return quest;
    }

    public Answer sendQuestAndCheckAnswer(Quest quest, int timeoutInseconds) throws RTMException, InterruptedException {
        Answer answer = sendQuest(quest, timeoutInseconds);
        if (answer.isErrorAnswer()) {
            int errorCode = answer.getErrorCode();
            String errorMessage = answer.getErrorMessage();
            throw new RTMException(errorCode, errorMessage);
        }

        return answer;
    }

    public int getPid(){
        return pid;
    }

    //------------------------[ For Files APIs ]----------------------------//
    public class FileInfo{
        public  byte[] fileContent;
        public String fileName;
        public String filenameExtensiion;

    }

    public static FileMsgInfo processFileInfo(String msg, String attrs, int mtype) {
        try {
            Gson gson = new Gson();
            FileMsgInfo fileMsgInfo = gson.fromJson(msg, FileMsgInfo.class);
            RTMAudioFileInfo rtmAudioFileInfo = new RTMAudioFileInfo();
            if(mtype == RTMMessageType.AudioFile.value()) {
                Gson gson1 = new Gson();
                JsonElement element = gson1.fromJson(attrs, JsonElement.class);
                JsonObject jsonObject = element.getAsJsonObject();
                JsonObject rtmNode = jsonObject.get("rtm").getAsJsonObject();
                String type = rtmNode.get("type").getAsString();
                if(type == "audiomsg") {
                    rtmAudioFileInfo.lang = rtmNode.get("lang").getAsString();
                    rtmAudioFileInfo.codec = rtmNode.get("codec").getAsString();
                    rtmAudioFileInfo.duration = rtmNode.get("duration").getAsInt();
                    rtmAudioFileInfo.srate = rtmNode.get("srate").getAsInt();
                    rtmAudioFileInfo.isRTMaudio = true;
                }
            }
            fileMsgInfo.rtmAudioFileInfo = rtmAudioFileInfo;
            return fileMsgInfo;
        }catch(JsonSyntaxException ex) {
            ErrorRecorder.record("parse file json error", ex);
            ex.printStackTrace();
        }
        return new FileMsgInfo();
    }

    public static String fetchFileCustomAttrs(String attr) {
        try {
            Gson gson = new Gson();
            JsonElement element = gson.fromJson(attr, JsonElement.class);
            JsonObject jsonObject = element.getAsJsonObject();
            String customAttr = jsonObject.get("custom").getAsString();
            return customAttr;
        }catch(JsonSyntaxException ex){
            ErrorRecorder.record("parse file custom attr json error", ex);
            ex.printStackTrace();
        }
        return "";
    }

    public FileInfo readFileForSendAPI(String filePath) throws IOException {
        FileInfo info = new FileInfo();
        info.fileContent = Files.readAllBytes(Paths.get(filePath));

        File file = new File(filePath);
        info.fileName = file.getName();
        int pos = info.fileName.lastIndexOf(".");
        if (pos > 0)
            info.filenameExtensiion = info.fileName.substring(pos + 1);
        else
            info.filenameExtensiion = null;

        return info;
    }

    public String buildFileAttrs(String token, byte[] fileContent, String filename, String ext, String attr, RTMAudioFileInfo rtmAudioFileInfo) throws GeneralSecurityException, IOException {

        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(fileContent);
        byte[] md5Binary = md5.digest();
        String md5Hex = bytesToHexString(md5Binary, true) + ":" + token;

        md5 = MessageDigest.getInstance("MD5");
        md5.update(md5Hex.getBytes("UTF-8"));
        md5Binary = md5.digest();
        md5Hex = bytesToHexString(md5Binary, true);

        JsonObject jsonObject = new JsonObject();
        JsonObject jsonObjectParent = new JsonObject();

        jsonObject.addProperty("sign", md5Hex);

        if (ext != null && ext.length() > 0)
            jsonObject.addProperty("ext", ext);

        if (filename != null && filename.length() > 0)
            jsonObject.addProperty("filename", filename);

        if(rtmAudioFileInfo != null && rtmAudioFileInfo.isRTMaudio) {
            jsonObject.addProperty("type", "audiomsg");
            jsonObject.addProperty("codec", rtmAudioFileInfo.codec);
            jsonObject.addProperty("srate", rtmAudioFileInfo.srate);
            jsonObject.addProperty("lang", rtmAudioFileInfo.lang);
            jsonObject.addProperty("duration", rtmAudioFileInfo.duration);
        }
        jsonObjectParent.add("rtm", jsonObject);
        if(attr.length() > 0){
            try {
                Gson gson = new Gson();
                JsonElement element = gson.fromJson(attr, JsonElement.class);
                JsonObject jsonAttr = element.getAsJsonObject();
                jsonObjectParent.add("custom", jsonAttr);
            }catch (JsonSyntaxException ex) {
                jsonObjectParent.addProperty("custom", attr);
            }
        } else {
            jsonObjectParent.addProperty("custom", "");
        }

        return jsonObjectParent.toString();
    }

    public static class RTMMessageCount {
        public int sender;
        public int count;
    }

    public static class RTMAudioFileInfo {
        public boolean isRTMaudio;
        public String codec;
        public String lang;
        public int srate;
        public int duration;
    }

    public static class FileMsgInfo {
        public String url;
        public long size;
        public String surl;
        public transient RTMAudioFileInfo rtmAudioFileInfo = null;

    }

    // for rtmmessage
    public static class RTMMessage{
        public byte messageType;
        public long toId;     // for serverpush
        public long fromUid;
        public long modifiedTime;
        public long messageId;
        public String stringMessage;
        public byte[] binaryMessage;
        public String attrs;
        public FileMsgInfo fileMsgInfo = null;

        @Override
        public String toString(){
            return " ,[One RTMMessage: mtype = " + messageType + " ,fromuid = " + fromUid + ", to = " + toId + " ,mtime = " + modifiedTime
                    + " ,mid = " + messageId + " ,message = " + stringMessage + " ,binaryMessage = " + binaryMessage + " ,attrs = " + attrs + "]";
        }
    }

    public static class RTMHistoryMessageUnit{
        public long cursorId;
        public RTMMessage message = null;

        @Override
        public String toString(){
            if(message != null){
                return cursorId + message.toString();
            }
            return "";
        }
    }

    public static class RTMHistoryMessage {

        public int count;            // 实际返回的条目数量
        public long lastCursorId;    // 继续轮询时，下次调用使用的 lastId 参数的值
        public long beginMsec;       // 继续轮询时，下次调用使用的 begin 参数的值
        public long endMsec;         // 继续轮询时，下次调用使用的 end 参数的值
        public List<RTMHistoryMessageUnit> messageList = new ArrayList<>();

        @Override
        public String toString(){
            String ss = "[HistoryMessage] count = " + count + " ,lastId = " + lastCursorId + " ,beginMsec = " + beginMsec + " ,endMsec = " + endMsec;
            if(!messageList.isEmpty()){
                ss += " , one message info: " + messageList.get(0).toString();
            }
            else{
                ss += " ,[HistoryMessage empty]";
            }
            return ss;

        }
    }

    // for translate result
    public static class RTMTranslateMessage {
        public String source;
        public String target;
        public String sourceText;
        public String targetText;

        @Override
        public String toString(){
            return "[RTMTranslateMessage] source = " + source + " ,target = " + target + " ,sourceText = " + sourceText + " ,targetText " + targetText;
        }
    }
}
