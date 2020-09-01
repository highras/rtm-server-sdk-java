package com.fpnn.rtm.api;

import com.fpnn.rtm.RTMException;
import com.fpnn.rtm.RTMServerClientBase;
import com.fpnn.rtm.RTMServerClientBase.RTMTranslateMessage;
import com.fpnn.rtm.RTMTranslateLanguage;
import com.fpnn.sdk.AnswerCallback;
import com.fpnn.sdk.ErrorCode;
import com.fpnn.sdk.ErrorRecorder;
import com.fpnn.sdk.proto.Answer;
import com.fpnn.sdk.proto.Quest;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface UtilitiesAPI extends APIBase{

    interface TranslateMessageLambdaCallback {
        void done(RTMTranslateMessage result, int errorCode, String errorMessage);
    }

    interface ProfanityLambdaCallback{
        void done(String text, Set<String> classification, int errorCode, String errorMessage);
    }

    interface TranscribeLambdaCallback{
        void done(String text, String lang, int errorCode, String errorMessage);
    }

    enum TranslateType{
        TRANSLATE_TYPE_CHAT,
        TRANSLATE_TYPE_MAIL
    }

    enum ProfanityType{
        PROFANITY_TYPE_OFF,
        PROFANITY_TYPE_CENSOR
    }


    //---------------------translate--------------------------
    default RTMTranslateMessage translate(String text, String src, String dst, TranslateType type, ProfanityType profanity, long uid, int timeoutInsecond)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        RTMServerClientBase client = getCoreClient();
        Quest quest = client.genBasicQuest("translate");
        quest.param("text", text);
        if(src != null && src.length() > 0)
            quest.param("src", src);
        quest.param("dst", dst);
        switch (type){
            case TRANSLATE_TYPE_MAIL:
                quest.param("type", "mail");
                break;
            default:
                quest.param("type","chat");
                break;
        }
        switch (profanity){
            case PROFANITY_TYPE_CENSOR:
                quest.param("profanity", "censor");
                break;
            default:
                quest.param("profanity", "off");
                break;
        }
        if(uid > 0)
            quest.param("uid", uid);
        Answer answer = client.sendQuestAndCheckAnswer(quest, timeoutInsecond);
        // because support min version for java8 , after java8 support private functions in interface
        RTMTranslateMessage result = new RTMTranslateMessage();
        result.source = (String)answer.get("source", "");
        result.target = (String)answer.get("target", "");
        result.sourceText = (String)answer.get("sourceText", "");
        result.targetText = (String)answer.get("targetText", "");
        return result;
    }

    default RTMTranslateMessage translate(String text, String src, String dst, TranslateType type, ProfanityType profanity, long uid)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return translate(text, src, dst, type, profanity, uid, 0);
    }

    default RTMTranslateMessage translate(String text, String dst)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return translate(text, dst, 0);
    }

    default RTMTranslateMessage translate(String text, String dst, int timeoutInsecond)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return translate(text, null, dst, TranslateType.TRANSLATE_TYPE_CHAT, ProfanityType.PROFANITY_TYPE_OFF, 0, timeoutInsecond);
    }

    default RTMTranslateMessage translate(String text, RTMTranslateLanguage dstLanguage, int timeoutInsecond)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return translate(text, dstLanguage.toString(), timeoutInsecond);
    }

    default RTMTranslateMessage translate(String text, RTMTranslateLanguage dstLanguage)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        return translate(text, dstLanguage, 0);
    }

    default RTMTranslateMessage translate(String text, RTMTranslateLanguage sourceLanguage, RTMTranslateLanguage dstLanguage, TranslateType type, ProfanityType profanity, long uid)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return translate(text, sourceLanguage, dstLanguage, type, profanity, uid, 0);
    }

    default RTMTranslateMessage translate(String text, RTMTranslateLanguage sourceLanguage, RTMTranslateLanguage dstLanguage, TranslateType type, ProfanityType profanity, long uid, int timeoutInseconds)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        return translate(text, sourceLanguage.toString(), dstLanguage.toString(), type, profanity, uid, timeoutInseconds);
    }


    default void translate(String text, String src, String dst, TranslateType type, ProfanityType profanity, long uid, TranslateMessageLambdaCallback callback, int timeoutInsecond){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("translate");
        }catch (Exception ex){
            ErrorRecorder.record("Generate translate message sign exception.", ex);
            callback.done(null, ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate translate message sign exception.");
            return;
        }
        quest.param("text", text);
        if(src != null && src.length() > 0)
            quest.param("src", src);
        quest.param("dst", dst);
        switch (type){
            case TRANSLATE_TYPE_MAIL:
                quest.param("type", "mail");
                break;
            default:
                quest.param("type","chat");
                break;
        }
        switch (profanity){
            case PROFANITY_TYPE_CENSOR:
                quest.param("profanity", "censor");
                break;
            default:
                quest.param("profanity", "off");
                break;
        }
        if(uid > 0)
            quest.param("uid", uid);
        AnswerCallback answerCallback = new AnswerCallback() {
            @Override
            public void onAnswer(Answer answer) {
                if(answer != null){
                    RTMTranslateMessage result = new RTMTranslateMessage();
                    result.source = (String)answer.get("source", "");
                    result.target = (String)answer.get("target", "");
                    result.sourceText = (String)answer.get("sourceText", "");
                    result.targetText = (String)answer.get("targetText", "");
                    callback.done(result, ErrorCode.FPNN_EC_OK.value(), "");
                }
            }

            @Override
            public void onException(Answer answer, int i) {
                String info = null;
                if(answer != null){
                    info = (String)answer.get("ex", "");
                }
                callback.done(null, i, info);
            }
        };
        client.sendQuest(quest, answerCallback, timeoutInsecond);
    }

    default void translate(String text, String src, String dst, TranslateType type, ProfanityType profanity, long uid, TranslateMessageLambdaCallback callback){
        translate(text, src, dst, type, profanity, uid, callback,0);
    }

    default void translate(String text, String dst, TranslateMessageLambdaCallback callback){
        translate(text, dst, callback,0);
    }

    default void translate(String text, String dst, TranslateMessageLambdaCallback callback, int timeoutInsecond){
        translate(text, null, dst, TranslateType.TRANSLATE_TYPE_CHAT, ProfanityType.PROFANITY_TYPE_OFF, 0, callback, timeoutInsecond);
    }

    default void translate(String text, RTMTranslateLanguage dstLanguage, TranslateMessageLambdaCallback callback){
        translate(text, dstLanguage, callback, 0);
    }

    default void translate(String text, RTMTranslateLanguage dstLanguage, TranslateMessageLambdaCallback callback, int timeoutInsecond){
        translate(text, dstLanguage.toString(), callback, timeoutInsecond);
    }

    default void translate(String text, RTMTranslateLanguage sourceLanguage, RTMTranslateLanguage dstLanguage, TranslateType type, ProfanityType profanity, long uid, TranslateMessageLambdaCallback callback){
        translate(text, sourceLanguage, dstLanguage, type, profanity, uid, callback,0);
    }

    default void translate(String text, RTMTranslateLanguage sourceLanguage, RTMTranslateLanguage dstLanguage, TranslateType type, ProfanityType profanity, long uid, TranslateMessageLambdaCallback callback, int timeoutInsecond){
        translate(text, sourceLanguage.toString(), dstLanguage.toString(), type, profanity, uid, callback,0);
    }


    //---------------------------------profanity----------------------------
    default void profanity(String text, boolean classify, long uid, StringBuffer resultText, Set<String> classification, int timeoutInsecond)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException{
        RTMServerClientBase client = getCoreClient();
        Quest quest = client.genBasicQuest("profanity");
        quest.param("text", text);
        quest.param("classify", classify);
        if(uid > 0)
            quest.param("uid", uid);
        Answer answer = client.sendQuestAndCheckAnswer(quest, timeoutInsecond);
        resultText.setLength(0);
        resultText.append((String)answer.get("text", ""));
        Object object = answer.get("classification", null);
        if(object != null){
            List<Object> data = (List<Object>)object;
            for(Object s : data){
                classification.add(String.valueOf(s));
            }
        }
    }

    default void profanity(String text, boolean classify, long uid, StringBuffer resultText, Set<String> classification)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException{
        profanity(text, classify, uid, resultText, classification, 0);
    }

    default void profanity(String text, StringBuffer resultText, Set<String> classification, int timeoutInsecond)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        RTMServerClientBase client = getCoreClient();
        Quest quest = client.genBasicQuest("profanity");
        quest.param("text", text);
        Answer answer = client.sendQuestAndCheckAnswer(quest, timeoutInsecond);
        resultText.setLength(0);
        resultText.append((String)answer.get("text", ""));
        Object object = answer.get("classification", null);
        if(object != null){
            List<Object> data = (List<Object>)object;
            for(Object s : data){
                classification.add(String.valueOf(s));
            }
        }
    }

    default void profanity(String text, StringBuffer resultText, Set<String> classification)
            throws RTMException, GeneralSecurityException, IOException, InterruptedException {
        profanity(text, resultText, classification,0);

    }

    default void profanity(String text, boolean classify, long uid, ProfanityLambdaCallback callback, int timeoutInsecond){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("profanity");
        }catch (Exception ex){
            ErrorRecorder.record("Generate profanity message sign exception.", ex);
            callback.done("", null, ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate profanity message sign exception.");
            return;
        }
        quest.param("text", text);
        quest.param("classify", classify);
        if(uid > 0)
            quest.param("uid", uid);
        AnswerCallback answerCallback = new AnswerCallback() {
            @Override
            public void onAnswer(Answer answer) {
                String text = (String)answer.get("text", "");
                Set<String> classification = new HashSet<>();
                Object object = answer.get("classification", null);
                if(object != null){
                    List<Object> data = (List<Object>)object;
                    for(Object s : data){
                        classification.add(String.valueOf(s));
                    }
                }
                callback.done(text, classification, ErrorCode.FPNN_EC_OK.value(), "");
            }

            @Override
            public void onException(Answer answer, int i) {
                String info = null;
                if(answer != null)
                {
                    info = (String)answer.get("ex", "");
                }
                callback.done("", null, i, info);
            }
        };
        client.sendQuest(quest, answerCallback, timeoutInsecond);
    }

    default void profanity(String text, boolean classify, long uid, ProfanityLambdaCallback callback){
        profanity(text, classify, uid, callback, 0);
    }

    default void profanity(String text, ProfanityLambdaCallback callback, int timeoutInsecond){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("profanity");
        }catch (Exception ex){
            ErrorRecorder.record("Generate profanity message sign exception.", ex);
            callback.done("", null, ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate profanity message sign exception.");
            return;
        }
        quest.param("text", text);

        AnswerCallback answerCallback = new AnswerCallback() {
            @Override
            public void onAnswer(Answer answer) {
                String text = (String)answer.get("text", "");
                Set<String> classification = new HashSet<>();
                Object object = answer.get("classification", null);
                if(object != null){
                    List<Object> data = (List<Object>)object;
                    for(Object s : data){
                        classification.add(String.valueOf(s));
                    }
                }
                callback.done(text, classification, ErrorCode.FPNN_EC_OK.value(), "");

            }

            @Override
            public void onException(Answer answer, int i) {
                String info = null;
                if(answer != null)
                {
                    info = (String)answer.get("ex","");
                }
                callback.done("", null, i, info);
            }
        };
        client.sendQuest(quest, answerCallback, timeoutInsecond);
    }

    default void profanity(String text, ProfanityLambdaCallback callback){
        profanity(text, callback, 0);
    }

    //-----------------------------transcribe------------------
    default void transcribe(byte[] audio, long uid, boolean profanityFilter, StringBuffer text, StringBuffer lang, int timeoutInsecond)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException{
        RTMServerClientBase client = getCoreClient();
        Quest quest = client.genBasicQuest("transcribe");
        quest.param("audio", new String(audio));
        if(uid > 0)
            quest.param("uid", uid);
        quest.param("profanityFilter", profanityFilter);
        Answer answer = client.sendQuestAndCheckAnswer(quest, timeoutInsecond);
        text.setLength(0);
        lang.setLength(0);
        text.append((String) answer.get("text", ""));
        lang.append((String)answer.get("lang", ""));
    }

    default void transcribe(byte[] audio, long uid, boolean profanityFilter, StringBuffer text, StringBuffer lang)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException{
        transcribe(audio, uid, profanityFilter, text, lang,120);
    }

    default void transcribe(byte[] audio, StringBuffer text, StringBuffer lang, int timeoutInsecond)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException{

        RTMServerClientBase client = getCoreClient();
        Quest quest = client.genBasicQuest("transcribe");
        quest.param("audio", new String(audio));
        Answer answer = client.sendQuestAndCheckAnswer(quest, timeoutInsecond);
        text.setLength(0);
        lang.setLength(0);
        text.append((String) answer.get("text", ""));
        lang.append((String)answer.get("lang", ""));
    }

    default void transcribe(byte[] audio, StringBuffer text, StringBuffer lang)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException{
        transcribe(audio, text, lang, 120);
    }

    default void transcribe(byte[] audio, long uid, boolean profanityFilter, TranscribeLambdaCallback callback, int timeoutInsecond){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("transcribe");
        }catch (Exception ex){
            ErrorRecorder.record("Generate transcribe message sign exception.", ex);
            callback.done("", "", ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate transcribe message sign exception.");
            return;
        }
        quest.param("audio", new String(audio));
        if(uid > 0)
            quest.param("uid", uid);
        quest.param("profanityFilter", profanityFilter);

        AnswerCallback answerCallback = new AnswerCallback() {
            @Override
            public void onAnswer(Answer answer) {
                if(answer != null){
                    callback.done((String)answer.get("text", ""), (String)answer.get("lang", ""), ErrorCode.FPNN_EC_OK.value(), "");
                }
            }

            @Override
            public void onException(Answer answer, int i) {
                String info = null;
                if(answer != null){
                    info = (String)answer.get("ex", "");
                }
                callback.done("", "", i, info);

            }
        };
        client.sendQuest(quest, answerCallback, timeoutInsecond);

    }

    default void transcribe(byte[] audio, long uid, boolean profanityFilter, TranscribeLambdaCallback callback){
        transcribe(audio, uid, profanityFilter, callback, 120);
    }

    default void transcribe(byte[] audio, TranscribeLambdaCallback callback, int timeoutInsecond){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("transcribe");
        }catch (Exception ex){
            ErrorRecorder.record("Generate transcribe message sign exception.", ex);
            callback.done("", "", ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate transcribe message sign exception.");
            return;
        }
        quest.param("audio", new String(audio));
        AnswerCallback answerCallback = new AnswerCallback() {
            @Override
            public void onAnswer(Answer answer) {
                if(answer != null){
                    callback.done((String)answer.get("text", ""), (String)answer.get("lang", ""), ErrorCode.FPNN_EC_OK.value(), "");
                }
            }

            @Override
            public void onException(Answer answer, int i) {
                String info = null;
                if(answer != null){
                    info = (String)answer.get("ex", "");
                }
                callback.done("", "", i, info);

            }
        };
        client.sendQuest(quest, answerCallback, timeoutInsecond);
    }

    default void transcribe(byte[] audio, TranscribeLambdaCallback callback){
        transcribe(audio, callback, 120);
    }

    //-----------------------------stranscribe------------------
    default void stranscribe(long from, long messageId, long xid, MessageType type, boolean profanityFilter, StringBuffer text, StringBuffer lang, int timeoutInsecond)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException{
        RTMServerClientBase client = getCoreClient();
        Quest quest = client.genBasicQuest("stranscribe");
        quest.param("from", from);
        quest.param("mid", messageId);
        quest.param("xid", xid);
        quest.param("type", type.value());
        quest.param("profanityFilter", profanityFilter);
        Answer answer = client.sendQuestAndCheckAnswer(quest, timeoutInsecond);
        text.setLength(0);
        lang.setLength(0);
        text.append((String) answer.get("text", ""));
        lang.append((String)answer.get("lang", ""));
    }

    default void stranscribeP2PAudio(long fromUid, long messageId, long toUid, boolean profanityFilter, StringBuffer text, StringBuffer lang)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException{
        stranscribe(fromUid, messageId, toUid, MessageType.MESSAGE_TYPE_P2P, profanityFilter, text, lang,120);
    }

    default void stranscribeP2PAudio(long fromUid, long messageId, long toUid, StringBuffer text, StringBuffer lang)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException{
        stranscribe(fromUid, messageId, toUid, MessageType.MESSAGE_TYPE_P2P, false, text, lang,120);
    }

    default void stranscribeP2PAudio(long fromUid, long messageId, long toUid, StringBuffer text, StringBuffer lang, int timeoutInsecond)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException{
        stranscribe(fromUid, messageId, toUid, MessageType.MESSAGE_TYPE_P2P, false, text, lang, timeoutInsecond);
    }

    default void stranscribeP2PAudio(long fromUid, long messageId, long toUid, boolean profanityFilter, StringBuffer text, StringBuffer lang, int timeoutInsecond)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException{
        stranscribe(fromUid, messageId, toUid, MessageType.MESSAGE_TYPE_P2P, profanityFilter, text, lang, timeoutInsecond);
    }

    default void stranscribeGroupAudio(long fromUid, long messageId, long groupId, boolean profanityFilter, StringBuffer text, StringBuffer lang)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException{
        stranscribe(fromUid, messageId, groupId, MessageType.MESSAGE_TYPE_GROUP, profanityFilter, text, lang,120);
    }

    default void stranscribeGroupAudio(long fromUid, long messageId, long groupId, StringBuffer text, StringBuffer lang)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException{
        stranscribe(fromUid, messageId, groupId, MessageType.MESSAGE_TYPE_GROUP, false, text, lang,120);
    }

    default void stranscribeGroupAudio(long from, long messageId, long groupId, StringBuffer text, StringBuffer lang, int timeoutInsecond)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException{
        stranscribe(from, messageId, groupId, MessageType.MESSAGE_TYPE_GROUP, false, text, lang, timeoutInsecond);
    }

    default void stranscribeGroupAudio(long fromUid, long messageId, long groupId, boolean profanityFilter, StringBuffer text, StringBuffer lang, int timeoutInsecond)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException{
        stranscribe(fromUid, messageId, groupId, MessageType.MESSAGE_TYPE_GROUP, profanityFilter, text, lang, timeoutInsecond);
    }

    default void stranscribeRoomAudio(long fromUid, long messageId, long roomId, boolean profanityFilter, StringBuffer text, StringBuffer lang)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException{
        stranscribe(fromUid, messageId, roomId, MessageType.MESSAGE_TYPE_ROOM, profanityFilter, text, lang,120);
    }

    default void stranscribeRoomAudio(long fromUid, long messageId, long roomId, StringBuffer text, StringBuffer lang)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException{
        stranscribe(fromUid, messageId, roomId, MessageType.MESSAGE_TYPE_ROOM, false, text, lang,120);
    }

    default void stranscribeRoomAudio(long fromUid, long messageId, long roomId, StringBuffer text, StringBuffer lang, int timeoutInsecond)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException{
        stranscribe(fromUid, messageId, roomId, MessageType.MESSAGE_TYPE_ROOM, false, text, lang, timeoutInsecond);
    }

    default void stranscribeRoomAudio(long fromUid, long messageId, long roomId, boolean profanityFilter, StringBuffer text, StringBuffer lang, int timeoutInsecond)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException{
        stranscribe(fromUid, messageId, roomId, MessageType.MESSAGE_TYPE_ROOM, profanityFilter, text, lang, timeoutInsecond);
    }

    default void stranscribeBroadcastAudio(long fromUid, long messageId, boolean profanityFilter, StringBuffer text, StringBuffer lang)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException{
        stranscribe(fromUid, messageId, 0, MessageType.MESSAGE_TYPE_BROADCAST, profanityFilter, text, lang,120);
    }

    default void stranscribeBroadcastAudio(long fromUid, long messageId, StringBuffer text, StringBuffer lang)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException{
        stranscribe(fromUid, messageId, 0, MessageType.MESSAGE_TYPE_BROADCAST, false, text, lang,120);
    }

    default void stranscribeBroadcastAudio(long fromUid, long messageId, StringBuffer text, StringBuffer lang, int timeoutInsecond)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException{
        stranscribe(fromUid, messageId, 0, MessageType.MESSAGE_TYPE_BROADCAST, false, text, lang, timeoutInsecond);
    }

    default void stranscribeBroadcastAudio(long fromUid, long messageId, boolean profanityFilter, StringBuffer text, StringBuffer lang, int timeoutInsecond)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException{
        stranscribe(fromUid, messageId, 0, MessageType.MESSAGE_TYPE_BROADCAST, profanityFilter, text, lang, timeoutInsecond);
    }

    default void stranscribe(long from, long messageId, long xid, MessageType type, boolean profanityFilter, TranscribeLambdaCallback callback, int timeoutInsecond){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("stranscribe");
        }catch (Exception ex){
            ErrorRecorder.record("Generate stranscribe message sign exception.", ex);
            callback.done("", "", ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate stranscribe message sign exception.");
            return;
        }
        quest.param("from", from);
        quest.param("mid", messageId);
        quest.param("xid", xid);
        quest.param("type", type.value());
        quest.param("profanityFilter", profanityFilter);

        AnswerCallback answerCallback = new AnswerCallback() {
            @Override
            public void onAnswer(Answer answer) {
                if(answer != null){
                    callback.done((String)answer.get("text", ""), (String)answer.get("lang", ""), ErrorCode.FPNN_EC_OK.value(), "");
                }
            }

            @Override
            public void onException(Answer answer, int i) {
                String info = null;
                if(answer != null){
                    info = (String)answer.get("ex", "");
                }
                callback.done("", "", i, info);

            }
        };
        client.sendQuest(quest, answerCallback, timeoutInsecond);
    }

    default void stranscribeP2PAudio(long fromUid, long messageId, long toUid, boolean profanityFilter, TranscribeLambdaCallback callback, int timeoutInsecond){
        stranscribe(fromUid, messageId, toUid, MessageType.MESSAGE_TYPE_P2P, profanityFilter, callback, timeoutInsecond);
    }

    default void stranscribeP2PAudio(long fromUid, long messageId, long toUid, boolean profanityFilter, TranscribeLambdaCallback callback){
        stranscribe(fromUid, messageId, toUid, MessageType.MESSAGE_TYPE_P2P, profanityFilter, callback, 120);
    }

    default void stranscribeP2PAudio(long fromUid, long messageId, long toUid, TranscribeLambdaCallback callback, int timeoutInsecond){
        stranscribe(fromUid, messageId, toUid, MessageType.MESSAGE_TYPE_P2P, false, callback, timeoutInsecond);
    }

    default void stranscribeP2PAudio(long fromUid, long messageId, long toUid, TranscribeLambdaCallback callback){
        stranscribe(fromUid, messageId, toUid, MessageType.MESSAGE_TYPE_P2P, false, callback, 120);
    }

    default void stranscribeGroupAudio(long fromUid, long messageId, long groupId, boolean profanityFilter, TranscribeLambdaCallback callback, int timeoutInsecond){
        stranscribe(fromUid, messageId, groupId, MessageType.MESSAGE_TYPE_GROUP, profanityFilter, callback, timeoutInsecond);
    }

    default void stranscribeGroupAudio(long fromUid, long messageId, long groupId, boolean profanityFilter, TranscribeLambdaCallback callback){
        stranscribe(fromUid, messageId, groupId, MessageType.MESSAGE_TYPE_GROUP, profanityFilter, callback, 120);
    }

    default void stranscribeGroupAudio(long fromUid, long messageId, long groupId, TranscribeLambdaCallback callback, int timeoutInsecond){
        stranscribe(fromUid, messageId, groupId, MessageType.MESSAGE_TYPE_GROUP, false, callback, timeoutInsecond);
    }

    default void stranscribeGroupAudio(long fromUid, long messageId, long groupId, TranscribeLambdaCallback callback){
        stranscribe(fromUid, messageId, groupId, MessageType.MESSAGE_TYPE_GROUP, false, callback, 120);
    }

    default void stranscribeRoomAudio(long fromUid, long messageId, long roomId, boolean profanityFilter, TranscribeLambdaCallback callback, int timeoutInsecond){
        stranscribe(fromUid, messageId, roomId, MessageType.MESSAGE_TYPE_ROOM, profanityFilter, callback, timeoutInsecond);
    }

    default void stranscribeRoomAudio(long fromUid, long messageId, long roomId, boolean profanityFilter, TranscribeLambdaCallback callback){
        stranscribe(fromUid, messageId, roomId, MessageType.MESSAGE_TYPE_ROOM, profanityFilter, callback, 120);
    }

    default void stranscribeRoomAudio(long fromUid, long messageId, long roomId, TranscribeLambdaCallback callback, int timeoutInsecond){
        stranscribe(fromUid, messageId, roomId, MessageType.MESSAGE_TYPE_ROOM, false, callback, timeoutInsecond);
    }

    default void stranscribeRoomAudio(long fromUid, long messageId, long roomId, TranscribeLambdaCallback callback){
        stranscribe(fromUid, messageId, roomId, MessageType.MESSAGE_TYPE_ROOM, false, callback, 120);
    }

    default void stranscribeBroadcastAudio(long fromUid, long messageId, boolean profanityFilter, TranscribeLambdaCallback callback, int timeoutInsecond){
        stranscribe(fromUid, messageId, 0, MessageType.MESSAGE_TYPE_BROADCAST, profanityFilter, callback, timeoutInsecond);
    }

    default void stranscribeBroadcastAudio(long fromUid, long messageId, boolean profanityFilter, TranscribeLambdaCallback callback){
        stranscribe(fromUid, messageId, 0, MessageType.MESSAGE_TYPE_BROADCAST, profanityFilter, callback, 120);
    }

    default void stranscribeBroadcastAudio(long fromUid, long messageId, TranscribeLambdaCallback callback, int timeoutInsecond){
        stranscribe(fromUid, messageId, 0, MessageType.MESSAGE_TYPE_BROADCAST, false, callback, timeoutInsecond);
    }

    default void stranscribeBroadcastAudio(long fromUid, long messageId, TranscribeLambdaCallback callback){
        stranscribe(fromUid, messageId, 0, MessageType.MESSAGE_TYPE_BROADCAST, false, callback, 120);
    }
}
