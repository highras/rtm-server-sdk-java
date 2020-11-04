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

    interface SpeechToTextLambdaCallback {
        void done(String text, String lang, int errorCode, String errorMessage);
    }

    interface TextCheckLambdaCallback {
        void done(int result, String text, Set<Integer> tags, Set<String> wlist, int errorCode, String errorMessage);
    }

    interface OtherCheckLambdaCallback {
        void done(int result, Set<Integer> tags, int errorCode, String errorMessage);
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
    // Explain: maybe in after version this interface will be deprecated，recommend use TextCheck interface replace
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

    //-----------------------------speechToText------------------
    default void speechToText(String audio, int audioType, RTMTranslateLanguage lang, String codec, int srate, long uid,  StringBuffer text, StringBuffer dstLang, int timeoutInsecond)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException{
        RTMServerClientBase client = getCoreClient();
        Quest quest = client.genBasicQuest("speech2text");
        quest.param("audio", audio);
        quest.param("type", audioType);
        quest.param("lang", lang.toString());
        if(codec.length() > 0) {
            quest.param("codec", codec);
        }

        if(srate > 0) {
            quest.param("srate", srate);
        }

        if(uid > 0){
            quest.param("uid", uid);
        }
        Answer answer = client.sendQuestAndCheckAnswer(quest, timeoutInsecond);
        text.setLength(0);
        dstLang.setLength(0);
        text.append((String) answer.get("text", ""));
        dstLang.append((String)answer.get("lang", ""));
    }

    default void speechToText(String audio, int audioType, RTMTranslateLanguage lang, String codec, int srate, long uid,  StringBuffer text, StringBuffer dstLang)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException{
        speechToText(audio, audioType, lang, codec, srate, uid, text, dstLang, 120);
    }

    default void speechToText(String audio, int audioType, RTMTranslateLanguage lang, StringBuffer text, StringBuffer dstLang, int timeoutInsecond)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException{
        speechToText(audio, audioType, lang, "", 0, 0, text, dstLang, timeoutInsecond);
    }

    default void speechToText(String audio, int audioType, RTMTranslateLanguage lang, StringBuffer text, StringBuffer dstLang)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException{
        speechToText(audio, audioType, lang, text, dstLang, 120);
    }

    default void speechToText(String audio, int audioType, RTMTranslateLanguage lang, String codec, int srate, long uid, SpeechToTextLambdaCallback callback, int timeoutInsecond){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("speech2text");
        }catch (Exception ex){
            ErrorRecorder.record("Generate speech2text message sign exception.", ex);
            callback.done("", "", ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate speech2text message sign exception.");
            return;
        }
        quest.param("audio", audio);
        quest.param("type", audioType);
        quest.param("lang", lang.toString());
        if(codec.length() > 0) {
            quest.param("codec", codec);
        }

        if(srate > 0) {
            quest.param("srate", srate);
        }

        if(uid > 0){
            quest.param("uid", uid);
        }

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

    default void speechToText(String audio, int audioType, RTMTranslateLanguage lang, String codec, int srate, long uid, SpeechToTextLambdaCallback callback){
        speechToText(audio, audioType, lang, codec, srate, uid, callback, 120);
    }

    default void speechToText(String audio, int audioType, RTMTranslateLanguage lang, SpeechToTextLambdaCallback callback, int timeoutInsecond){
        speechToText(audio, audioType, lang, "", 0, 0, callback, timeoutInsecond);
    }

    default void speechToText(String audio, int audioType, RTMTranslateLanguage lang, SpeechToTextLambdaCallback callback){
        speechToText(audio, audioType, lang, callback, 120);
    }

    //-----------------------------textCheck------------------
    default int textCheck(String text, long uid, StringBuffer resultText, Set<Integer> tags, Set<String> wlist, int timeoutInsecond)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException{
        RTMServerClientBase client = getCoreClient();
        Quest quest = client.genBasicQuest("tcheck");
        quest.param("text", text);
        if(uid > 0) {
            quest.param("uid", uid);
        }
        Answer answer = client.sendQuestAndCheckAnswer(quest, timeoutInsecond);
        resultText.setLength(0);
        resultText.append((String) answer.get("text", ""));
        int result = answer.getInt("result", -1);
        Object object = answer.get("tags", null);
        if(object != null){
            List<Object> data = (List<Object>)object;
            for(Object o : data){
                tags.add(Integer.valueOf(String.valueOf(o)));
            }
        }
        object = answer.get("wlist", null);
        if(object != null) {
            List<Object> data = (List<Object>)object;
            for(Object o : data){
                wlist.add(String.valueOf(o));
            }
        }
        return result;
    }

    default int textCheck(String text, long uid, StringBuffer resultText, Set<Integer> tags, Set<String> wlist)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException{
        return textCheck(text, uid, resultText, tags, wlist, 0);
    }

    default int textCheck(String text, StringBuffer resultText, Set<Integer> tags, Set<String> wlist)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException{
        return textCheck(text, 0, resultText, tags, wlist, 0);
    }

    default int textCheck(String text, StringBuffer resultText, Set<Integer> tags, Set<String> wlist, int timeoutInsecond)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException{
        return textCheck(text, 0, resultText, tags, wlist, timeoutInsecond);
    }

    default void textCheck(String text, long uid, TextCheckLambdaCallback callback, int timeoutInsecond){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("tcheck");
        }catch (Exception ex){
            ErrorRecorder.record("Generate tcheck message sign exception.", ex);
            callback.done(-1, "", null, null, ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate tcheck message sign exception.");
            return;
        }
        quest.param("text", text);
        if(uid > 0){
            quest.param("uid", uid);
        }

        AnswerCallback answerCallback = new AnswerCallback() {
            @Override
            public void onAnswer(Answer answer) {
                if(answer != null){
                    Set<Integer> tags = new HashSet<>();
                    Object object = answer.get("tags", null);
                    if(object != null){
                        List<Object> data = (List<Object>)object;
                        for(Object o : data){
                            tags.add(Integer.valueOf(String.valueOf(o)));
                        }
                    }
                    Set<String> wlist = new HashSet<>();
                    object = answer.get("wlist", null);
                    if(object != null) {
                        List<Object> data = (List<Object>)object;
                        for(Object o : data){
                            wlist.add(String.valueOf(o));
                        }
                    }
                    callback.done(answer.getInt("result", -1), (String)answer.get("text", ""), tags, wlist, ErrorCode.FPNN_EC_OK.value(), "");
                }
            }

            @Override
            public void onException(Answer answer, int i) {
                String info = null;
                if(answer != null){
                    info = (String)answer.get("ex", "");
                }
                callback.done(-1, "", null, null, i, info);

            }
        };
        client.sendQuest(quest, answerCallback, timeoutInsecond);
    }

    default void textCheck(String text, long uid, TextCheckLambdaCallback callback) {
        textCheck(text, uid, callback, 0);
    }

    default void textCheck(String text, TextCheckLambdaCallback callback) {
        textCheck(text, 0, callback);
    }

    default void textCheck(String text, TextCheckLambdaCallback callback, int timeoutInsecond) {
        textCheck(text, 0, callback, timeoutInsecond);
    }

    ////////////////////////////////// imageCheck ///////////////////////////
    default int imageCheck(String image, int imageType, long uid, Set<Integer>tags,  int timeoutInsecond)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException{
        RTMServerClientBase client = getCoreClient();
        Quest quest = client.genBasicQuest("icheck");
        quest.param("image", image);
        quest.param("type", imageType);
        if(uid > 0) {
            quest.param("uid", uid);
        }
        Answer answer = client.sendQuestAndCheckAnswer(quest, timeoutInsecond);
        int result = answer.getInt("result", -1);
        Object object = answer.get("tags", null);
        if(object != null){
            List<Object> data = (List<Object>)object;
            for(Object o : data){
                tags.add(Integer.valueOf(String.valueOf(o)));
            }
        }
        return result;
    }

    default int imageCheck(String image, int imageType, long uid, Set<Integer>tags)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException{
        return imageCheck(image, imageType, uid, tags, 120);
    }

    default int imageCheck(String image, int imageType, Set<Integer>tags)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException{
        return imageCheck(image, imageType, tags, 120);
    }

    default int imageCheck(String image, int imageType, Set<Integer>tags, int timeoutInsecond)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException{
        return imageCheck(image, imageType, 0, tags, timeoutInsecond);
    }

    default void imageCheck(String image, int imageType, long uid, OtherCheckLambdaCallback callback, int timeoutInsecond){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("icheck");
        }catch (Exception ex){
            ErrorRecorder.record("Generate icheck message sign exception.", ex);
            callback.done(-1, null, ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate icheck message sign exception.");
            return;
        }
        quest.param("image", image);
        quest.param("type", imageType);
        if(uid > 0){
            quest.param("uid", uid);
        }

        AnswerCallback answerCallback = new AnswerCallback() {
            @Override
            public void onAnswer(Answer answer) {
                if(answer != null){
                    Set<Integer> tags = new HashSet<>();
                    Object object = answer.get("tags", null);
                    if(object != null){
                        List<Object> data = (List<Object>)object;
                        for(Object o : data){
                            tags.add(Integer.valueOf(String.valueOf(o)));
                        }
                    }
                    callback.done(answer.getInt("result", -1),  tags, ErrorCode.FPNN_EC_OK.value(), "");
                }
            }

            @Override
            public void onException(Answer answer, int i) {
                String info = null;
                if(answer != null){
                    info = (String)answer.get("ex", "");
                }
                callback.done(-1, null, i, info);

            }
        };
        client.sendQuest(quest, answerCallback, timeoutInsecond);
    }

    default void imageCheck(String image, int imageType, long uid, OtherCheckLambdaCallback callback) {
        imageCheck(image, imageType, uid, callback, 120);
    }

    default void imageCheck(String image, int imageType, OtherCheckLambdaCallback callback) {
        imageCheck(image, imageType, callback,120);
    }

    default void imageCheck(String image, int imageType, OtherCheckLambdaCallback callback, int timeoutInsecond) {
        imageCheck(image, imageType, 0, callback, timeoutInsecond);
    }

    ////////////////////////////////// audioCheck ///////////////////////////
    default int audioCheck(String audio, int audioType, RTMTranslateLanguage lang, long uid, String codec, int srate, Set<Integer>tags,  int timeoutInsecond)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException{
        RTMServerClientBase client = getCoreClient();
        Quest quest = client.genBasicQuest("acheck");
        quest.param("audio", audio);
        quest.param("type", audioType);
        quest.param("lang", lang);

        if(codec.length() > 0) {
            quest.param("codec", codec);
        }
        if(srate > 0) {
            quest.param("srate", srate);
        }
        if(uid > 0) {
            quest.param("uid", uid);
        }
        Answer answer = client.sendQuestAndCheckAnswer(quest, timeoutInsecond);
        int result = answer.getInt("result", -1);
        Object object = answer.get("tags", null);
        if(object != null){
            List<Object> data = (List<Object>)object;
            for(Object o : data){
                tags.add(Integer.valueOf(String.valueOf(o)));
            }
        }
        return result;
    }

    default int audioCheck(String audio, int audioType, RTMTranslateLanguage lang, long uid, String codec, int srate, Set<Integer>tags)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException{
        return audioCheck(audio, audioType, lang, uid, codec, srate, tags, 120);
    }

    default int audioCheck(String audio, int audioType, RTMTranslateLanguage lang, Set<Integer>tags)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException{
        return audioCheck(audio, audioType, lang, tags, 120);
    }

    default int audioCheck(String audio, int audioType, RTMTranslateLanguage lang, Set<Integer>tags, int timeoutInsecond)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException{
        return audioCheck(audio, audioType, lang, 0, "", 0, tags, timeoutInsecond);
    }

    default void audioCheck(String audio, int audioType, RTMTranslateLanguage lang, long uid, String codec, int srate, OtherCheckLambdaCallback callback, int timeoutInsecond){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("acheck");
        }catch (Exception ex){
            ErrorRecorder.record("Generate acheck message sign exception.", ex);
            callback.done(-1, null, ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate acheck message sign exception.");
            return;
        }
        quest.param("audio", audio);
        quest.param("type", audioType);
        quest.param("lang", lang.toString());

        if(codec.length() > 0) {
            quest.param("codec", codec);
        }
        if(srate > 0) {
            quest.param("srate", srate);
        }
        if(uid > 0){
            quest.param("uid", uid);
        }

        AnswerCallback answerCallback = new AnswerCallback() {
            @Override
            public void onAnswer(Answer answer) {
                if(answer != null){
                    Set<Integer> tags = new HashSet<>();
                    Object object = answer.get("tags", null);
                    if(object != null){
                        List<Object> data = (List<Object>)object;
                        for(Object o : data){
                            tags.add(Integer.valueOf(String.valueOf(o)));
                        }
                    }
                    callback.done(answer.getInt("result", -1),  tags, ErrorCode.FPNN_EC_OK.value(), "");
                }
            }

            @Override
            public void onException(Answer answer, int i) {
                String info = null;
                if(answer != null){
                    info = (String)answer.get("ex", "");
                }
                callback.done(-1, null, i, info);

            }
        };
        client.sendQuest(quest, answerCallback, timeoutInsecond);
    }

    default void audioCheck(String audio, int audioType, RTMTranslateLanguage lang, long uid, String codec, int srate, OtherCheckLambdaCallback callback) {
        audioCheck(audio, audioType, lang, uid, codec, srate, callback, 120);
    }

    default void audioCheck(String audio, int audioType, RTMTranslateLanguage lang, OtherCheckLambdaCallback callback) {
        audioCheck(audio, audioType, lang, callback,120);
    }

    default void audioCheck(String audio, int audioType, RTMTranslateLanguage lang, OtherCheckLambdaCallback callback, int timeoutInsecond) {
        audioCheck(audio, audioType, lang, 0, "", 0, callback, timeoutInsecond);
    }

    ////////////////////////////////// videoCheck ///////////////////////////
    default int videoCheck(String video, int videoType, String videoName, long uid, Set<Integer> tags, int timeoutInsecond)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException{
        RTMServerClientBase client = getCoreClient();
        Quest quest = client.genBasicQuest("vcheck");
        quest.param("video", video);
        quest.param("type", videoType);
        quest.param("videoName", videoName);
        if(uid > 0) {
            quest.param("uid", uid);
        }
        Answer answer = client.sendQuestAndCheckAnswer(quest, timeoutInsecond);
        int result = answer.getInt("result", -1);
        Object object = answer.get("tags", null);
        if(object != null){
            List<Object> data = (List<Object>)object;
            for(Object o : data){
                tags.add(Integer.valueOf(String.valueOf(o)));
            }
        }
        return result;
    }

    default int videoCheck(String video, int videoType, String videoName, long uid, Set<Integer>tags)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException{
        return videoCheck(video, videoType, videoName, uid, tags, 120);
    }

    default int videoCheck(String video, int videoType, String videoName, Set<Integer>tags)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException{
        return videoCheck(video, videoType, videoName, tags, 120);
    }

    default int videoCheck(String video, int videoType, String videoName, Set<Integer>tags, int timeoutInsecond)
            throws RTMException, GeneralSecurityException, InterruptedException, IOException{
        return videoCheck(video, videoType, videoName,0, tags, timeoutInsecond);
    }

    default void videoCheck(String video, int videoType, String videoName, long uid, OtherCheckLambdaCallback callback, int timeoutInsecond){
        RTMServerClientBase client = getCoreClient();
        Quest quest;
        try{
            quest = client.genBasicQuest("vcheck");
        }catch (Exception ex){
            ErrorRecorder.record("Generate vcheck message sign exception.", ex);
            callback.done(-1, null, ErrorCode.FPNN_EC_CORE_UNKNOWN_ERROR.value(), "Generate vcheck message sign exception.");
            return;
        }
        quest.param("video", video);
        quest.param("type", videoType);
        quest.param("videoName", videoName);
        if(uid > 0){
            quest.param("uid", uid);
        }

        AnswerCallback answerCallback = new AnswerCallback() {
            @Override
            public void onAnswer(Answer answer) {
                if(answer != null){
                    Set<Integer> tags = new HashSet<>();
                    Object object = answer.get("tags", null);
                    if(object != null){
                        List<Object> data = (List<Object>)object;
                        for(Object o : data){
                            tags.add(Integer.valueOf(String.valueOf(o)));
                        }
                    }
                    callback.done(answer.getInt("result", -1),  tags, ErrorCode.FPNN_EC_OK.value(), "");
                }
            }

            @Override
            public void onException(Answer answer, int i) {
                String info = null;
                if(answer != null){
                    info = (String)answer.get("ex", "");
                }
                callback.done(-1, null, i, info);

            }
        };
        client.sendQuest(quest, answerCallback, timeoutInsecond);
    }

    default void videoCheck(String video, int videoType, String videoName, long uid, OtherCheckLambdaCallback callback) {
        videoCheck(video, videoType, videoName, uid, callback, 120);
    }

    default void videoCheck(String video, int videoType, String videoName, OtherCheckLambdaCallback callback) {
        videoCheck(video, videoType, videoName, callback,120);
    }

    default void videoCheck(String video, int videoType, String videoName, OtherCheckLambdaCallback callback, int timeoutInsecond) {
        videoCheck(video, videoType, videoName, 0, callback, timeoutInsecond);
    }

}
