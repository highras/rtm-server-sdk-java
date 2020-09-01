package com.fpnn.rtm;

import com.fpnn.rtm.api.ServerPushMonitorAPI;
import com.fpnn.sdk.*;
import com.fpnn.sdk.proto.Answer;
import com.fpnn.sdk.proto.Quest;

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
        Random random = new Random(System.currentTimeMillis());
        MidGenerator.randId = random.nextInt(255) + 1;
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
            long time = System.currentTimeMillis();
            count = (count + 1) & sequenceMask;
            if(count == 0){
                time = getNextMillis(lastTime);
            }
            lastTime = time;
            long id = (time<< (randBits + sequenceBits)) | randId<< sequenceBits| count;
            return id &0x1FFFFFFFFFFFFFL;
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

    public String buildFileAttrs(String token, byte[] fileContent, String filename, String ext) throws GeneralSecurityException, IOException {

        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(fileContent);
        byte[] md5Binary = md5.digest();
        String md5Hex = bytesToHexString(md5Binary, true) + ":" + token;

        md5 = MessageDigest.getInstance("MD5");
        md5.update(md5Hex.getBytes("UTF-8"));
        md5Binary = md5.digest();
        md5Hex = bytesToHexString(md5Binary, true);

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"sign\":\"").append(md5Hex).append("\"");

        if (ext != null && ext.length() > 0)
            sb.append(", \"ext\":\"").append(ext).append("\"");

        if (filename != null && filename.length() > 0)
            sb.append(", \"filename\":\"").append(filename).append("\"");

        sb.append("}");

        return sb.toString();
    }

    public static class AudioInfo{
        public String sourceLanguage;
        public String recognizedLanguage;
        public String recognizedText;
        public int duration;

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
        public AudioInfo audioInfo = null;   //for serverpush and history

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
