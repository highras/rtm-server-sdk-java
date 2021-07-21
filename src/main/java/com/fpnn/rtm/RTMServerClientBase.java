package com.fpnn.rtm;

import com.fpnn.rtm.api.ServerPushMonitorAPI;
import com.fpnn.sdk.*;
import com.fpnn.sdk.proto.Answer;
import com.fpnn.sdk.proto.Quest;
import com.google.gson.*;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.*;
import java.util.zip.CRC32;

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
        static private final long randBits = 4;
        static private int macCode = 0;
        static private final long macBits = 4;
        static private int ipCode = 0;
        static private final long ipBits = 8;
        static private final long sequenceBits = 6;
        static private final long sequenceMask = -1 ^ (-1 << sequenceBits);
        static private long lastTime = 0;
        private static int getMacAddrCode(InetAddress ia) throws Exception {
            byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
            StringBuffer sb = new StringBuffer();
            for(int i = 0; i < mac.length; i++){
                if(i != 0){
                    sb.append("-");
                }
                String s = Integer.toHexString(mac[i] & 0xFF);
                sb.append(s.length() == 1 ? 0 + s : s);
            }
            String ss = sb.toString().toUpperCase();
            CRC32 crc32 = new CRC32();
            crc32.update(ss.getBytes());
            long value = crc32.getValue() % 15 + 1;
            return (int)value;
        }

        private static int getIpCode(InetAddress ia){
            int code = 0;
            String localIp = ia.getHostAddress();
            String[] datas = localIp.split("\\.");
            if(datas.length == 4){
                try{
                    code = Integer.parseInt(datas[3]);
                }catch(NumberFormatException ex){
                }
            }

            if(code == 0){
                Random random = new Random(System.currentTimeMillis());
                code = random.nextInt(255) + 1;
            }
            return code;
        }

        static public synchronized long gen() {
            if(randId == 0 ){
                try{
                    boolean find = false;
                    Enumeration<NetworkInterface> nifs = NetworkInterface.getNetworkInterfaces();
                    while(nifs.hasMoreElements()) {
                        NetworkInterface nif = nifs.nextElement();
                        if (nif.isUp() && !nif.isLoopback()){
                            Enumeration<InetAddress> addrs = nif.getInetAddresses();
                            while(addrs.hasMoreElements()){
                                InetAddress address = addrs.nextElement();
                                if(address instanceof Inet4Address){
                                    ipCode = MidGenerator.getIpCode(address);
                                    macCode = MidGenerator.getMacAddrCode(address);
                                    find = true;
                                    break;
                                }
                            }
                            if(find){
                                break;
                            }
                        }
                    }

                    if(ipCode <= 0){
                        Random random1 = new Random(System.currentTimeMillis());
                        ipCode = random1.nextInt(255) + 1;
                    }
                    if(macCode <= 0){
                        Random random2 = new Random(System.currentTimeMillis());
                        macCode = random2.nextInt(15) + 1;
                    }

                    Random random3 = new Random(System.currentTimeMillis());
                    randId = random3.nextInt(15) + 1;
                }catch(Exception e){
                    ErrorRecorder.record(e);
                }
            }
            long time = System.currentTimeMillis();
            count = (count + 1) & sequenceMask;
            if(count == 0){
                time = getNextMillis(lastTime);
            }
            lastTime = time;
            long id = (time<< (randBits + macBits +ipBits + sequenceBits)) | randId<< (sequenceBits + ipBits + macBits) | macCode << (ipBits + sequenceBits) | ipCode << sequenceBits | count;
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
