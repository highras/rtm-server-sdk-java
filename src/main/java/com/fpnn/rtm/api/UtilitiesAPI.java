package com.fpnn.rtm.api;

import com.fpnn.rtm.RTMException;
import com.fpnn.rtm.RTMServerClientBase;
import com.fpnn.rtm.RTMServerClientBase.RTMTranslateMessage;
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
        RTMServerClientBase client = getCoreClient();
        Quest quest = client.genBasicQuest("translate");
        quest.param("text", text);
        quest.param("dst", dst);
        Answer answer = client.sendQuestAndCheckAnswer(quest, timeoutInsecond);
        // because support min version for java8 , after java8 support private functions in interface
        RTMTranslateMessage result = new RTMTranslateMessage();
        result.source = (String)answer.get("source", "");
        result.target = (String)answer.get("target", "");
        result.sourceText = (String)answer.get("sourceText", "");
        result.targetText = (String)answer.get("targetText", "");
        return result;
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
        quest.param("dst", dst);
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
}
