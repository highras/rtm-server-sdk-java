package com.fpnn.rtm.api;

import com.fpnn.rtm.RTMException;
import com.fpnn.rtm.RTMMessageType;
import com.fpnn.rtm.RTMResourceCenter;
import com.fpnn.rtm.RTMServerClientBase;
import com.fpnn.rtm.RTMServerClientBase.FileInfo;
import com.fpnn.sdk.AnswerCallback;
import com.fpnn.sdk.ErrorCode;
import com.fpnn.sdk.ErrorRecorder;
import com.fpnn.sdk.ClientEngine;
import com.fpnn.sdk.TCPClient;
import com.fpnn.sdk.proto.Answer;
import com.fpnn.sdk.proto.Quest;

import java.io.IOException;

import java.security.GeneralSecurityException;
import java.util.Set;

public interface FileAPI extends APIBase {

    interface SendFileLambdaCallback{
        void done(long time, int errorCode, String errorMessage);
    }

    //---------------send file--------------------
    default long sendFile(long fromUid, long toUid, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return sendFile(fromUid, toUid, filePath, attrs, rtmAudioFileInfo, 0);
    }

    default long sendFile(long fromUid, long toUid, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return sendFile(fromUid, toUid, RTMMessageType.NormalFile.value(), filePath, attrs, rtmAudioFileInfo, timeoutInseconds);
    }

    default long sendFile(long fromUid, long toUid, byte messageType, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, String filePath)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return sendFile(fromUid, toUid, messageType, filePath, attrs, rtmAudioFileInfo, 0);
    }

    default long sendFile(long fromUid, long toUid, byte messageType, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, int timeoutInseconds)
        throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        RTMServerClientBase client = getCoreClient();
        FileInfo info = client.readFileForSendAPI(filePath);
        return sendFile(fromUid, toUid, messageType, info.fileContent, info.fileName, info.filenameExtensiion, attrs, rtmAudioFileInfo, timeoutInseconds);
    }

    default long sendFile(long fromUid, long toUid, byte messageType, byte[] fileContent, String filename, String filenameExtension, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo)
            throws RTMException, IOException, GeneralSecurityException, InterruptedException {
        return sendFile(fromUid, toUid, messageType, fileContent, filename, filenameExtension, attrs, rtmAudioFileInfo, 0);
    }

    default long sendFile(long fromUid, long toUid, byte messageType, byte[] fileContent, String filename, String filenameExtension, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, int timeoutInseconds)
            throws RTMException, IOException, GeneralSecurityException, InterruptedException {

        String token;
        String endpoint;
        long adjustedTimeout = System.currentTimeMillis();
        RTMServerClientBase client = getCoreClient();
        //-- get file token
        {
            Quest quest = client.genBasicQuest("filetoken");
            quest.param("from", fromUid);
            quest.param("cmd", "sendfile");
            quest.param("to", toUid);

            Answer answer = client.sendQuestAndCheckAnswer(quest, timeoutInseconds);
            token = (String) answer.get("token");
            endpoint = (String) answer.get("endpoint");
        }

        String realAttr = client.buildFileAttrs(token, fileContent, filename, filenameExtension, attrs, rtmAudioFileInfo);

        //-- Recalculate the timeout.
        {
            if (timeoutInseconds == 0)
                timeoutInseconds = ClientEngine.getQuestTimeout();

            adjustedTimeout = System.currentTimeMillis() - adjustedTimeout;
            timeoutInseconds -= adjustedTimeout / 1000;

            if (timeoutInseconds <= 0) {
                throw new RTMException(ErrorCode.FPNN_EC_CORE_TIMEOUT.value(), "Timeout. Prepare sending P2P file is ready, but no data sent.");
            }
        }

        //-- send data
        {
            TCPClient fileGate = RTMResourceCenter.instance().getFileClient(endpoint, timeoutInseconds);

            Quest quest = new Quest("sendfile");
            quest.param("pid", client.getPid());
            quest.param("token", token);
            quest.param("mtype", messageType);
            quest.param("from", fromUid);

            quest.param("to", toUid);
            quest.param("mid", genMid());
            quest.param("file", fileContent);
            quest.param("attrs", realAttr);

            Answer answer = fileGate.sendQuest(quest, timeoutInseconds);
            if (answer.isErrorAnswer()) {
                int errorCode = answer.getErrorCode();
                String message = answer.getErrorMessage();
                throw new RTMException(errorCode, message);
            }
            else{
                return answer.getLong("mtime", 0);
            }

        }
    }

    default void sendFile(long fromUid, long toUid, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback){
        sendFile(fromUid, toUid, filePath, attrs, rtmAudioFileInfo, callback,0);
    }

    default void sendFile(long fromUid, long toUid, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback, int timeoutInseconds){
        sendFile(fromUid, toUid, RTMMessageType.NormalFile.value(), filePath, attrs, rtmAudioFileInfo, callback, timeoutInseconds);
    }

    default void sendFile(long fromUid, long toUid, byte messageType, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback){
        sendFile(fromUid, toUid, messageType, filePath, attrs, rtmAudioFileInfo, callback,0);
    }

    default void sendFile(long fromUid, long toUid, byte messageType, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback, int timeoutInseconds) {
        RTMServerClientBase client = getCoreClient();
        try{
            FileInfo info = client.readFileForSendAPI(filePath);
            sendFile(fromUid, toUid, messageType, info.fileContent, info.fileName, info.filenameExtensiion, attrs, rtmAudioFileInfo, callback, timeoutInseconds);
        }catch (Exception ex){
            ErrorRecorder.record("send file by read fileinfo exception.", ex);
            callback.done(-1, ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "send file by read fileinfo exception.");
        }
    }

    default void sendFile(long fromUid, long toUid, byte messageType, byte[] fileContent, String filename, String filenameExtension, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback){
        sendFile(fromUid, toUid, messageType, fileContent, filename, filenameExtension, attrs, rtmAudioFileInfo, callback,0);
    }

    default void sendFile(long fromUid, long toUid, byte messageType, byte[] fileContent, String filename, String filenameExtension, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest fileTokenQuest;
        try {
            fileTokenQuest = client.genBasicQuest("filetoken");
        } catch (Exception e) {
            ErrorRecorder.record("Prepare sending P2P file failed. Prepare file token exception.", e);
            callback.done(-1, ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Prepare sending P2P file failed. Prepare file token exception.");
            return;
        }


        fileTokenQuest.param("from", fromUid);
        fileTokenQuest.param("cmd", "sendfile");
        fileTokenQuest.param("to", toUid);

        long adjustedTimeout = System.currentTimeMillis();

        AnswerCallback fileTokenCallback = new AnswerCallback() {

            @Override
            public void onException(Answer answer, int errorCode) {
                String info = null;
                if (answer != null)
                    info = (String) answer.get("ex", "");

                String message = "Prepare sending P2P file failed. Cannot get file token. Message: " + info;
                callback.done(-1, errorCode, message);
            }

            @Override
            public void onAnswer(Answer answer) {
                String token = (String) answer.get("token");
                String endpoint = (String) answer.get("endpoint");

                String realAttr;
                try {
                    realAttr = client.buildFileAttrs(token, fileContent, filename, filenameExtension, attrs, rtmAudioFileInfo);
                } catch (Exception e) {
                    ErrorRecorder.record("Build attrs for sending P2P file exception.", e);
                    callback.done(-1, ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Build attrs for sending P2P file exception.");
                    return;
                }

                int originalTimeout = timeoutInseconds;
                if (originalTimeout == 0)
                    originalTimeout = ClientEngine.getQuestTimeout();

                int timeout = (int)(System.currentTimeMillis() - adjustedTimeout);
                timeout = originalTimeout - (timeout / 1000);

                if (timeout <= 0) {
                    callback.done(-1, ErrorCode.FPNN_EC_CORE_TIMEOUT.value(), "Timeout. Prepare sending P2P file is ready, but no data sent.");
                    return;
                }

                TCPClient fileGate = RTMResourceCenter.instance().getFileClient(endpoint, timeout);

                Quest quest = new Quest("sendfile");
                quest.param("pid", client.getPid());
                quest.param("token", token);
                quest.param("mtype", messageType);
                quest.param("from", fromUid);

                quest.param("to", toUid);
                quest.param("mid", genMid());
                quest.param("file", fileContent);
                quest.param("attrs", realAttr);

                AnswerCallback internalCallback = new AnswerCallback() {
                    @Override
                    public void onAnswer(Answer answer) {
                        callback.done(answer.getLong("mtime", 0), ErrorCode.FPNN_EC_OK.value(), "");
                    }

                    @Override
                    public void onException(Answer answer, int i) {
                        String info = null;
                        if(answer != null){
                            info = (String)answer.get("ex", "");
                        }
                        String message = "Prepare sending P2P file failed. send to filegate failed. Message: " + info;
                        callback.done(-1, i, message);
                    }
                };

                fileGate.sendQuest(quest, internalCallback, timeout);
            }
        };

        client.sendQuest(fileTokenQuest, fileTokenCallback, timeoutInseconds);
    }

    //---------------send files--------------------
    default long sendFiles(long fromUid, Set<Long> toUids, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return sendFiles(fromUid, toUids, filePath, attrs, rtmAudioFileInfo,0);
    }

    default long sendFiles(long fromUid, Set<Long> toUids, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return sendFiles(fromUid, toUids, RTMMessageType.NormalFile.value(), filePath, attrs, rtmAudioFileInfo, timeoutInseconds);
    }

    default long sendFiles(long fromUid, Set<Long> toUids, byte messageType, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return sendFiles(fromUid, toUids, messageType, filePath, attrs, rtmAudioFileInfo, 0);
    }

    default long sendFiles(long fromUid, Set<Long> toUids, byte messageType, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        RTMServerClientBase client = getCoreClient();
        FileInfo info = client.readFileForSendAPI(filePath);
        return sendFiles(fromUid, toUids, messageType, info.fileContent, info.fileName, info.filenameExtensiion, attrs, rtmAudioFileInfo, timeoutInseconds);
    }

    default long sendFiles(long fromUid, Set<Long> toUids, byte messageType, byte[] fileContent, String filename, String filenameExtension, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo)
            throws RTMException, IOException, GeneralSecurityException, InterruptedException {
        return sendFiles(fromUid, toUids, messageType, fileContent, filename, filenameExtension, attrs, rtmAudioFileInfo, 0);
    }

    default long sendFiles(long fromUid, Set<Long> toUids, byte messageType, byte[] fileContent, String filename, String filenameExtension, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, int timeoutInseconds)
            throws RTMException, IOException, GeneralSecurityException, InterruptedException {

        String token;
        String endpoint;
        long adjustedTimeout = System.currentTimeMillis();
        RTMServerClientBase client = getCoreClient();
        //-- get file token
        {
            Quest quest = client.genBasicQuest("filetoken");
            quest.param("from", fromUid);
            quest.param("cmd", "sendfiles");
            quest.param("tos", toUids);

            Answer answer = client.sendQuestAndCheckAnswer(quest, timeoutInseconds);
            token = (String) answer.get("token");
            endpoint = (String) answer.get("endpoint");
        }

        String realAttr = client.buildFileAttrs(token, fileContent, filename, filenameExtension, attrs, rtmAudioFileInfo);

        //-- Recalculate the timeout.
        {
            if (timeoutInseconds == 0)
                timeoutInseconds = ClientEngine.getQuestTimeout();

            adjustedTimeout = System.currentTimeMillis() - adjustedTimeout;
            timeoutInseconds -= adjustedTimeout / 1000;

            if (timeoutInseconds <= 0) {
                throw new RTMException(ErrorCode.FPNN_EC_CORE_TIMEOUT.value(), "Timeout. Prepare sending P2Ps file is ready, but no data sent.");
            }
        }

        //-- send data
        {
            TCPClient fileGate = RTMResourceCenter.instance().getFileClient(endpoint, timeoutInseconds);

            Quest quest = new Quest("sendfiles");
            quest.param("pid", client.getPid());
            quest.param("token", token);
            quest.param("mtype", messageType);
            quest.param("from", fromUid);

            quest.param("tos", toUids);
            quest.param("mid", genMid());
            quest.param("file", fileContent);
            quest.param("attrs", realAttr);

            Answer answer = fileGate.sendQuest(quest, timeoutInseconds);
            if (answer.isErrorAnswer()) {
                int errorCode = answer.getErrorCode();
                String message = answer.getErrorMessage();
                throw new RTMException(errorCode, message);
            }
            else{
                return answer.getLong("mtime", 0);
            }

        }
    }

    default void sendFiles(long fromUid, Set<Long> toUids, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback){
        sendFiles(fromUid, toUids, filePath, attrs, rtmAudioFileInfo, callback,0);
    }

    default void sendFiles(long fromUid, Set<Long> toUids, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback, int timeoutInseconds){
        sendFiles(fromUid, toUids, RTMMessageType.NormalFile.value(), filePath, attrs, rtmAudioFileInfo, callback, timeoutInseconds);
    }

    default void sendFiles(long fromUid, Set<Long> toUids, byte messageType, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback){
        sendFiles(fromUid, toUids, messageType, filePath, attrs, rtmAudioFileInfo, callback,0);
    }

    default void sendFiles(long fromUid, Set<Long> toUids, byte messageType, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback, int timeoutInseconds) {
        RTMServerClientBase client = getCoreClient();
        try{
            FileInfo info = client.readFileForSendAPI(filePath);
            sendFiles(fromUid, toUids, messageType, info.fileContent, info.fileName, info.filenameExtensiion, attrs, rtmAudioFileInfo, callback, timeoutInseconds);
        }catch (Exception ex){
            ErrorRecorder.record("send files by read fileinfo exception.", ex);
            callback.done(-1, ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "send files by read fileinfo exception.");
        }
    }

    default void sendFiles(long fromUid, Set<Long> toUids, byte messageType, byte[] fileContent, String filename, String filenameExtension, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback){
        sendFiles(fromUid, toUids, messageType, fileContent, filename, filenameExtension, attrs, rtmAudioFileInfo, callback,0);
    }

    default void sendFiles(long fromUid, Set<Long> toUids, byte messageType, byte[] fileContent, String filename, String filenameExtension, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest fileTokenQuest;
        try {
            fileTokenQuest = client.genBasicQuest("filetoken");
        } catch (Exception e) {
            ErrorRecorder.record("Prepare sending P2Ps file failed. Prepare file token exception.", e);
            callback.done(-1, ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Prepare sending P2Ps file failed. Prepare file token exception.");
            return;
        }


        fileTokenQuest.param("from", fromUid);
        fileTokenQuest.param("cmd", "sendfiles");
        fileTokenQuest.param("tos", toUids);

        long adjustedTimeout = System.currentTimeMillis();

        AnswerCallback fileTokenCallback = new AnswerCallback() {

            @Override
            public void onException(Answer answer, int errorCode) {
                String info = null;
                if (answer != null)
                    info = (String) answer.get("ex", "");

                String message = "Prepare sending P2Ps file failed. Cannot get file token. Message: " + info;
                callback.done(-1, errorCode, message);
            }

            @Override
            public void onAnswer(Answer answer) {

                String token = (String) answer.get("token");
                String endpoint = (String) answer.get("endpoint");

                String realAttr;
                try {
                    realAttr = client.buildFileAttrs(token, fileContent, filename, filenameExtension, attrs, rtmAudioFileInfo);
                } catch (Exception e) {
                    ErrorRecorder.record("Build attrs for sending P2Ps file exception.", e);
                    callback.done(-1, ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Build attrs for sending P2Ps file exception.");
                    return;
                }

                int originalTimeout = timeoutInseconds;
                if (originalTimeout == 0)
                    originalTimeout = ClientEngine.getQuestTimeout();

                int timeout = (int)(System.currentTimeMillis() - adjustedTimeout);
                timeout = originalTimeout - (timeout / 1000);

                if (timeout <= 0) {
                    callback.done(-1, ErrorCode.FPNN_EC_CORE_TIMEOUT.value(), "Timeout. Prepare sending P2Ps file is ready, but no data sent.");
                    return;
                }

                TCPClient fileGate = RTMResourceCenter.instance().getFileClient(endpoint, timeout);

                Quest quest = new Quest("sendfiles");
                quest.param("pid", client.getPid());
                quest.param("token", token);
                quest.param("mtype", messageType);
                quest.param("from", fromUid);

                quest.param("tos", toUids);
                quest.param("mid", genMid());
                quest.param("file", fileContent);
                quest.param("attrs", realAttr);

                AnswerCallback internalCallback = new AnswerCallback() {
                    @Override
                    public void onAnswer(Answer answer) {
                        callback.done(answer.getLong("mtime", 0), ErrorCode.FPNN_EC_OK.value(), "");
                    }

                    @Override
                    public void onException(Answer answer, int i) {
                        String info = null;
                        if(answer != null){
                            info = (String)answer.get("ex", "");

                        }
                        String message = "Prepare sending P2Ps file failed. send to filegate failed. Message: " + info;
                        callback.done(-1, i, message);
                    }
                };

                fileGate.sendQuest(quest, internalCallback, timeout);
            }
        };

        client.sendQuest(fileTokenQuest, fileTokenCallback, timeoutInseconds);
    }

    //---------------send group file--------------------
    default long sendGroupFile(long fromUid, long groupId, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return sendGroupFile(fromUid, groupId, filePath, attrs, rtmAudioFileInfo, 0);
    }

    default long sendGroupFile(long fromUid, long groupId, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return sendGroupFile(fromUid, groupId, RTMMessageType.NormalFile.value(), filePath, attrs, rtmAudioFileInfo, timeoutInseconds);
    }

    default long sendGroupFile(long fromUid, long groupId, byte messageType, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return sendGroupFile(fromUid, groupId, messageType, filePath, attrs, rtmAudioFileInfo, 0);
    }

    default long sendGroupFile(long fromUid, long groupId, byte messageType, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        RTMServerClientBase client = getCoreClient();
        FileInfo info = client.readFileForSendAPI(filePath);
        return sendGroupFile(fromUid, groupId, messageType, info.fileContent, info.fileName, info.filenameExtensiion, attrs, rtmAudioFileInfo, timeoutInseconds);
    }

    default long sendGroupFile(long fromUid, long groupId, byte messageType, byte[] fileContent, String filename, String filenameExtension, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo)
            throws RTMException, IOException, GeneralSecurityException, InterruptedException {
        return sendGroupFile(fromUid, groupId, messageType, fileContent, filename, filenameExtension, attrs, rtmAudioFileInfo, 0);
    }

    default long sendGroupFile(long fromUid, long groupId, byte messageType, byte[] fileContent, String filename, String filenameExtension, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, int timeoutInseconds)
            throws RTMException, IOException, GeneralSecurityException, InterruptedException {

        String token;
        String endpoint;
        long adjustedTimeout = System.currentTimeMillis();
        RTMServerClientBase client = getCoreClient();
        //-- get file token
        {
            Quest quest = client.genBasicQuest("filetoken");
            quest.param("from", fromUid);
            quest.param("cmd", "sendgroupfile");
            quest.param("gid", groupId);

            Answer answer = client.sendQuestAndCheckAnswer(quest, timeoutInseconds);
            token = (String) answer.get("token");
            endpoint = (String) answer.get("endpoint");
        }

        String realAttr = client.buildFileAttrs(token, fileContent, filename, filenameExtension, attrs, rtmAudioFileInfo);

        //-- Recalculate the timeout.
        {
            if (timeoutInseconds == 0)
                timeoutInseconds = ClientEngine.getQuestTimeout();

            adjustedTimeout = System.currentTimeMillis() - adjustedTimeout;
            timeoutInseconds -= adjustedTimeout / 1000;

            if (timeoutInseconds <= 0) {
                throw new RTMException(ErrorCode.FPNN_EC_CORE_TIMEOUT.value(), "Timeout. Prepare sending Group file is ready, but no data sent.");
            }
        }

        //-- send data
        {
            TCPClient fileGate = RTMResourceCenter.instance().getFileClient(endpoint, timeoutInseconds);

            Quest quest = new Quest("sendgroupfile");
            quest.param("pid", client.getPid());
            quest.param("token", token);
            quest.param("mtype", messageType);
            quest.param("from", fromUid);

            quest.param("gid", groupId);
            quest.param("mid", genMid());
            quest.param("file", fileContent);
            quest.param("attrs", realAttr);

            Answer answer = fileGate.sendQuest(quest, timeoutInseconds);
            if (answer.isErrorAnswer()) {
                int errorCode = answer.getErrorCode();
                String message = answer.getErrorMessage();
                throw new RTMException(errorCode, message);
            }
            else{
                return answer.getLong("mtime", 0);
            }

        }
    }

    default void sendGroupFile(long fromUid, long groupId, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback){
        sendGroupFile(fromUid, groupId, filePath, attrs, rtmAudioFileInfo, callback,0);
    }

    default void sendGroupFile(long fromUid, long groupId, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback, int timeoutInseconds){
        sendGroupFile(fromUid, groupId, RTMMessageType.NormalFile.value(), filePath, attrs, rtmAudioFileInfo, callback, timeoutInseconds);
    }

    default void sendGroupFile(long fromUid, long groupId, byte messageType, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback){
        sendGroupFile(fromUid, groupId, messageType, filePath, attrs, rtmAudioFileInfo, callback,0);
    }

    default void sendGroupFile(long fromUid, long groupId, byte messageType, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback, int timeoutInseconds) {
        RTMServerClientBase client = getCoreClient();
        try{
            FileInfo info = client.readFileForSendAPI(filePath);
            sendGroupFile(fromUid, groupId, messageType, info.fileContent, info.fileName, info.filenameExtensiion, attrs, rtmAudioFileInfo, callback, timeoutInseconds);
        }catch (Exception ex){
            ErrorRecorder.record("send file by read fileinfo exception.", ex);
            callback.done(-1, ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "send file by read fileinfo exception.");
        }
    }

    default void sendGroupFile(long fromUid, long groupId, byte messageType, byte[] fileContent, String filename, String filenameExtension, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback){
        sendGroupFile(fromUid, groupId, messageType, fileContent, filename, filenameExtension, attrs, rtmAudioFileInfo, callback,0);
    }

    default void sendGroupFile(long fromUid, long groupId, byte messageType, byte[] fileContent, String filename, String filenameExtension, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest fileTokenQuest;
        try {
            fileTokenQuest = client.genBasicQuest("filetoken");
        } catch (Exception e) {
            ErrorRecorder.record("Prepare sending Group file failed. Prepare file token exception.", e);
            callback.done(-1, ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Prepare sending Group file failed. Prepare file token exception.");
            return;
        }


        fileTokenQuest.param("from", fromUid);
        fileTokenQuest.param("cmd", "sendgroupfile");
        fileTokenQuest.param("gid", groupId);

        long adjustedTimeout = System.currentTimeMillis();

        AnswerCallback fileTokenCallback = new AnswerCallback() {

            @Override
            public void onException(Answer answer, int errorCode) {
                String info = null;
                if (answer != null)
                    info = (String) answer.get("ex", "");

                String message = "Prepare sending Group file failed. Cannot get file token. Message: " + info;
                callback.done(-1, errorCode, message);
            }

            @Override
            public void onAnswer(Answer answer) {

                String token = (String) answer.get("token");
                String endpoint = (String) answer.get("endpoint");

                String realAttr;
                try {
                    realAttr = client.buildFileAttrs(token, fileContent, filename, filenameExtension, attrs, rtmAudioFileInfo);
                } catch (Exception e) {
                    ErrorRecorder.record("Build attrs for sending Group file exception.", e);
                    callback.done(-1, ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Build attrs for sending Group file exception.");
                    return;
                }

                int originalTimeout = timeoutInseconds;
                if (originalTimeout == 0)
                    originalTimeout = ClientEngine.getQuestTimeout();

                int timeout = (int)(System.currentTimeMillis() - adjustedTimeout);
                timeout = originalTimeout - (timeout / 1000);

                if (timeout <= 0) {
                    callback.done(-1, ErrorCode.FPNN_EC_CORE_TIMEOUT.value(), "Timeout. Prepare sending Group file is ready, but no data sent.");
                    return;
                }

                TCPClient fileGate = RTMResourceCenter.instance().getFileClient(endpoint, timeout);

                Quest quest = new Quest("sendgroupfile");
                quest.param("pid", client.getPid());
                quest.param("token", token);
                quest.param("mtype", messageType);
                quest.param("from", fromUid);

                quest.param("gid", groupId);
                quest.param("mid", genMid());
                quest.param("file", fileContent);
                quest.param("attrs", realAttr);

                AnswerCallback internalCallback = new AnswerCallback() {
                    @Override
                    public void onAnswer(Answer answer) {
                        callback.done(answer.getLong("mtime", 0), ErrorCode.FPNN_EC_OK.value(), "");
                    }

                    @Override
                    public void onException(Answer answer, int i) {
                        String info = null;
                        if(answer != null){
                            info = (String)answer.get("ex", "");
                        }
                        String message = "Prepare sending Group file failed. send to filegate failed. Message: " + info;
                        callback.done(-1, i, message);
                    }
                };

                fileGate.sendQuest(quest, internalCallback, timeout);
            }
        };

        client.sendQuest(fileTokenQuest, fileTokenCallback, timeoutInseconds);
    }

    //---------------send Room file--------------------
    default long sendRoomFile(long fromUid, long roomId, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return sendRoomFile(fromUid, roomId, filePath, attrs, rtmAudioFileInfo, 0);
    }

    default long sendRoomFile(long fromUid, long roomId, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return sendRoomFile(fromUid, roomId, RTMMessageType.NormalFile.value(), filePath, attrs, rtmAudioFileInfo, timeoutInseconds);
    }

    default long sendRoomFile(long fromUid, long roomId, byte messageType, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return sendRoomFile(fromUid, roomId, messageType, filePath, attrs, rtmAudioFileInfo, 0);
    }

    default long sendRoomFile(long fromUid, long roomId, byte messageType, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        RTMServerClientBase client = getCoreClient();
        FileInfo info = client.readFileForSendAPI(filePath);
        return sendRoomFile(fromUid, roomId, messageType, info.fileContent, info.fileName, info.filenameExtensiion, attrs, rtmAudioFileInfo, timeoutInseconds);
    }

    default long sendRoomFile(long fromUid, long roomId, byte messageType, byte[] fileContent, String filename, String filenameExtension, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo)
            throws RTMException, IOException, GeneralSecurityException, InterruptedException {
        return sendRoomFile(fromUid, roomId, messageType, fileContent, filename, filenameExtension, attrs, rtmAudioFileInfo, 0);
    }

    default long sendRoomFile(long fromUid, long roomId, byte messageType, byte[] fileContent, String filename, String filenameExtension, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, int timeoutInseconds)
            throws RTMException, IOException, GeneralSecurityException, InterruptedException {

        String token;
        String endpoint;
        long adjustedTimeout = System.currentTimeMillis();
        RTMServerClientBase client = getCoreClient();
        //-- get file token
        {
            Quest quest = client.genBasicQuest("filetoken");
            quest.param("from", fromUid);
            quest.param("cmd", "sendroomfile");
            quest.param("rid", roomId);

            Answer answer = client.sendQuestAndCheckAnswer(quest, timeoutInseconds);
            token = (String) answer.get("token");
            endpoint = (String) answer.get("endpoint");
        }

        String realAttr = client.buildFileAttrs(token, fileContent, filename, filenameExtension, attrs, rtmAudioFileInfo);

        //-- Recalculate the timeout.
        {
            if (timeoutInseconds == 0)
                timeoutInseconds = ClientEngine.getQuestTimeout();

            adjustedTimeout = System.currentTimeMillis() - adjustedTimeout;
            timeoutInseconds -= adjustedTimeout / 1000;

            if (timeoutInseconds <= 0) {
                throw new RTMException(ErrorCode.FPNN_EC_CORE_TIMEOUT.value(), "Timeout. Prepare sending Room file is ready, but no data sent.");
            }
        }

        //-- send data
        {
            TCPClient fileGate = RTMResourceCenter.instance().getFileClient(endpoint, timeoutInseconds);

            Quest quest = new Quest("sendroomfile");
            quest.param("pid", client.getPid());
            quest.param("token", token);
            quest.param("mtype", messageType);
            quest.param("from", fromUid);

            quest.param("rid", roomId);
            quest.param("mid", genMid());
            quest.param("file", fileContent);
            quest.param("attrs", realAttr);

            Answer answer = fileGate.sendQuest(quest, timeoutInseconds);
            if (answer.isErrorAnswer()) {
                int errorCode = answer.getErrorCode();
                String message = answer.getErrorMessage();
                throw new RTMException(errorCode, message);
            }
            else{
                return answer.getLong("mtime", 0);
            }

        }
    }

    default void sendRoomFile(long fromUid, long roomId, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback){
        sendRoomFile(fromUid, roomId, filePath, attrs, rtmAudioFileInfo, callback,0);
    }

    default void sendRoomFile(long fromUid, long roomId, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback, int timeoutInseconds){
        sendRoomFile(fromUid, roomId, RTMMessageType.NormalFile.value(), filePath, attrs, rtmAudioFileInfo, callback, timeoutInseconds);
    }

    default void sendRoomFile(long fromUid, long roomId, byte messageType, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback){
        sendRoomFile(fromUid, roomId, messageType, filePath, attrs, rtmAudioFileInfo, callback,0);
    }

    default void sendRoomFile(long fromUid, long roomId, byte messageType, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback, int timeoutInseconds) {
        RTMServerClientBase client = getCoreClient();
        try{
            FileInfo info = client.readFileForSendAPI(filePath);
            sendRoomFile(fromUid, roomId, messageType, info.fileContent, info.fileName, info.filenameExtensiion, attrs, rtmAudioFileInfo, callback, timeoutInseconds);
        }catch (Exception ex){
            ErrorRecorder.record("send room file by read fileinfo exception.", ex);
            callback.done(-1, ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "send room file by read fileinfo exception.");
        }
    }

    default void sendRoomFile(long fromUid, long roomId, byte messageType, byte[] fileContent, String filename, String filenameExtension, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback){
        sendRoomFile(fromUid, roomId, messageType, fileContent, filename, filenameExtension, attrs, rtmAudioFileInfo, callback,0);
    }

    default void sendRoomFile(long fromUid, long roomId, byte messageType, byte[] fileContent, String filename, String filenameExtension, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest fileTokenQuest;
        try {
            fileTokenQuest = client.genBasicQuest("filetoken");
        } catch (Exception e) {
            ErrorRecorder.record("Prepare sending room file failed. Prepare file token exception.", e);
            callback.done(-1, ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Prepare sending room file failed. Prepare file token exception.");
            return;
        }


        fileTokenQuest.param("from", fromUid);
        fileTokenQuest.param("cmd", "sendroomfile");
        fileTokenQuest.param("rid", roomId);

        long adjustedTimeout = System.currentTimeMillis();

        AnswerCallback fileTokenCallback = new AnswerCallback() {

            @Override
            public void onException(Answer answer, int errorCode) {
                String info = null;
                if (answer != null)
                    info = (String) answer.get("ex", "");

                String message = "Prepare sending room file failed. Cannot get file token. Message: " + info;
                callback.done(-1, errorCode, message);
            }

            @Override
            public void onAnswer(Answer answer) {

                String token = (String) answer.get("token");
                String endpoint = (String) answer.get("endpoint");

                String realAttr;
                try {
                    realAttr = client.buildFileAttrs(token, fileContent, filename, filenameExtension, attrs, rtmAudioFileInfo);
                } catch (Exception e) {
                    ErrorRecorder.record("Build attrs for sending room file exception.", e);
                    callback.done(-1, ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Build attrs for sending room file exception.");
                    return;
                }

                int originalTimeout = timeoutInseconds;
                if (originalTimeout == 0)
                    originalTimeout = ClientEngine.getQuestTimeout();

                int timeout = (int)(System.currentTimeMillis() - adjustedTimeout);
                timeout = originalTimeout - (timeout / 1000);

                if (timeout <= 0) {
                    callback.done(-1, ErrorCode.FPNN_EC_CORE_TIMEOUT.value(), "Timeout. Prepare sending room file is ready, but no data sent.");
                    return;
                }

                TCPClient fileGate = RTMResourceCenter.instance().getFileClient(endpoint, timeout);

                Quest quest = new Quest("sendroomfile");
                quest.param("pid", client.getPid());
                quest.param("token", token);
                quest.param("mtype", messageType);
                quest.param("from", fromUid);

                quest.param("rid", roomId);
                quest.param("mid", genMid());
                quest.param("file", fileContent);
                quest.param("attrs", realAttr);

                AnswerCallback internalCallback = new AnswerCallback() {
                    @Override
                    public void onAnswer(Answer answer) {
                        callback.done(answer.getLong("mtime", 0), ErrorCode.FPNN_EC_OK.value(), "");
                    }

                    @Override
                    public void onException(Answer answer, int i) {
                        String info = null;
                        if(answer != null){
                            info = (String)answer.get("ex", "");
                        }
                        String message = "Prepare sending room file failed. send to filegate failed. Message: " + info;
                        callback.done(-1, i, message);
                    }
                };

                fileGate.sendQuest(quest, internalCallback, timeout);
            }
        };

        client.sendQuest(fileTokenQuest, fileTokenCallback, timeoutInseconds);
    }

    //---------------send broadcast file--------------------
    default long sendBroadcastFile(long fromUid, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return sendBroadcastFile(fromUid, filePath, attrs, rtmAudioFileInfo, 0);
    }

    default long sendBroadcastFile(long fromUid, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return sendBroadcastFile(fromUid, RTMMessageType.NormalFile.value(), filePath, attrs, rtmAudioFileInfo, timeoutInseconds);
    }

    default long sendBroadcastFile(long fromUid, byte messageType, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return sendBroadcastFile(fromUid, messageType, filePath, attrs, rtmAudioFileInfo, 0);
    }

    default long sendBroadcastFile(long fromUid, byte messageType, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        RTMServerClientBase client = getCoreClient();
        FileInfo info = client.readFileForSendAPI(filePath);
        return sendBroadcastFile(fromUid, messageType, info.fileContent, info.fileName, info.filenameExtensiion, attrs, rtmAudioFileInfo, timeoutInseconds);
    }

    default long sendBroadcastFile(long fromUid, byte messageType, byte[] fileContent, String filename, String filenameExtension, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo)
            throws RTMException, IOException, GeneralSecurityException, InterruptedException {
        return sendBroadcastFile(fromUid, messageType, fileContent, filename, filenameExtension, attrs, rtmAudioFileInfo, 0);
    }

    default long sendBroadcastFile(long fromUid, byte messageType, byte[] fileContent, String filename, String filenameExtension, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, int timeoutInseconds)
            throws RTMException, IOException, GeneralSecurityException, InterruptedException {

        String token;
        String endpoint;
        long adjustedTimeout = System.currentTimeMillis();
        RTMServerClientBase client = getCoreClient();
        //-- get file token
        {
            Quest quest = client.genBasicQuest("filetoken");
            quest.param("from", fromUid);
            quest.param("cmd", "broadcastfile");

            Answer answer = client.sendQuestAndCheckAnswer(quest, timeoutInseconds);
            token = (String) answer.get("token");
            endpoint = (String) answer.get("endpoint");
        }

        String realAttr = client.buildFileAttrs(token, fileContent, filename, filenameExtension, attrs, rtmAudioFileInfo);

        //-- Recalculate the timeout.
        {
            if (timeoutInseconds == 0)
                timeoutInseconds = ClientEngine.getQuestTimeout();

            adjustedTimeout = System.currentTimeMillis() - adjustedTimeout;
            timeoutInseconds -= adjustedTimeout / 1000;

            if (timeoutInseconds <= 0) {
                throw new RTMException(ErrorCode.FPNN_EC_CORE_TIMEOUT.value(), "Timeout. Prepare sending broadcast file is ready, but no data sent.");
            }
        }

        //-- send data
        {
            TCPClient fileGate = RTMResourceCenter.instance().getFileClient(endpoint, timeoutInseconds);

            Quest quest = new Quest("broadcastfile");
            quest.param("pid", client.getPid());
            quest.param("token", token);
            quest.param("mtype", messageType);
            quest.param("from", fromUid);

            quest.param("mid", genMid());
            quest.param("file", fileContent);
            quest.param("attrs", realAttr);

            Answer answer = fileGate.sendQuest(quest, timeoutInseconds);
            if (answer.isErrorAnswer()) {
                int errorCode = answer.getErrorCode();
                String message = answer.getErrorMessage();
                throw new RTMException(errorCode, message);
            }
            else{
                return answer.getLong("mtime", 0);
            }

        }
    }

    default void sendBroadcastFile(long fromUid, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback){
        sendBroadcastFile(fromUid, filePath, attrs, rtmAudioFileInfo, callback,0);
    }

    default void sendBroadcastFile(long fromUid, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback, int timeoutInseconds){
        sendBroadcastFile(fromUid, RTMMessageType.NormalFile.value(), filePath, attrs, rtmAudioFileInfo, callback, timeoutInseconds);
    }

    default void sendBroadcastFile(long fromUid, byte messageType, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback){
        sendBroadcastFile(fromUid, messageType, filePath, attrs, rtmAudioFileInfo, callback,0);
    }

    default void sendBroadcastFile(long fromUid,byte messageType, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback, int timeoutInseconds) {
        RTMServerClientBase client = getCoreClient();
        try{
            FileInfo info = client.readFileForSendAPI(filePath);
            sendBroadcastFile(fromUid, messageType, info.fileContent, info.fileName, info.filenameExtensiion, attrs, rtmAudioFileInfo, callback, timeoutInseconds);
        }catch (Exception ex){
            ErrorRecorder.record("send broadcast file by read fileinfo exception.", ex);
            callback.done(-1, ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "send broadcast file by read fileinfo exception.");
        }
    }

    default void sendBroadcastFile(long fromUid, byte messageType, byte[] fileContent, String filename, String filenameExtension, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback){
        sendBroadcastFile(fromUid, messageType, fileContent, filename, filenameExtension, attrs, rtmAudioFileInfo, callback, 0);
    }

    default void sendBroadcastFile(long fromUid, byte messageType, byte[] fileContent, String filename, String filenameExtension, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback, int timeoutInseconds){
        RTMServerClientBase client = getCoreClient();
        Quest fileTokenQuest;
        try {
            fileTokenQuest = client.genBasicQuest("filetoken");
        } catch (Exception e) {
            ErrorRecorder.record("Prepare sending broadcast file failed. Prepare file token exception.", e);
            callback.done(-1, ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Prepare sending broadcast file failed. Prepare file token exception.");
            return;
        }


        fileTokenQuest.param("from", fromUid);
        fileTokenQuest.param("cmd", "broadcastfile");

        long adjustedTimeout = System.currentTimeMillis();

        AnswerCallback fileTokenCallback = new AnswerCallback() {

            @Override
            public void onException(Answer answer, int errorCode) {
                String info = null;
                if (answer != null)
                    info = (String) answer.get("ex", "");

                String message = "Prepare sending broadcast file failed. Cannot get file token. Message: " + info;
                callback.done(-1, errorCode, message);
            }

            @Override
            public void onAnswer(Answer answer) {

                String token = (String) answer.get("token");
                String endpoint = (String) answer.get("endpoint");

                String realAttr;
                try {
                    realAttr = client.buildFileAttrs(token, fileContent, filename, filenameExtension, attrs, rtmAudioFileInfo);
                } catch (Exception e) {
                    ErrorRecorder.record("Build attrs for sending broadcast file exception.", e);
                    callback.done(-1, ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Build attrs for sending broadcast file exception.");
                    return;
                }

                int originalTimeout = timeoutInseconds;
                if (originalTimeout == 0)
                    originalTimeout = ClientEngine.getQuestTimeout();

                int timeout = (int)(System.currentTimeMillis() - adjustedTimeout);
                timeout = originalTimeout - (timeout / 1000);

                if (timeout <= 0) {
                    callback.done(-1, ErrorCode.FPNN_EC_CORE_TIMEOUT.value(), "Timeout. Prepare sending broadcast file is ready, but no data sent.");
                    return;
                }

                TCPClient fileGate = RTMResourceCenter.instance().getFileClient(endpoint, timeout);

                Quest quest = new Quest("broadcastfile");
                quest.param("pid", client.getPid());
                quest.param("token", token);
                quest.param("mtype", messageType);
                quest.param("from", fromUid);

                quest.param("mid", genMid());
                quest.param("file", fileContent);
                quest.param("attrs", realAttr);

                AnswerCallback internalCallback = new AnswerCallback() {
                    @Override
                    public void onAnswer(Answer answer) {
                        callback.done(answer.getLong("mtime", 0), ErrorCode.FPNN_EC_OK.value(), "");
                    }

                    @Override
                    public void onException(Answer answer, int i) {
                        String info = null;
                        if(answer != null){
                            info = (String)answer.get("ex", "");
                        }
                        String message = "Prepare sending broadcast file failed. send to filegate failed. Message: " + info;
                        callback.done(-1, i, message);
                    }
                };

                fileGate.sendQuest(quest, internalCallback, timeout);
            }
        };

        client.sendQuest(fileTokenQuest, fileTokenCallback, timeoutInseconds);
    }

}
