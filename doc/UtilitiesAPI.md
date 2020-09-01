# RTM Server Java SDK Utilities API Docs

# Index

[TOC]

### ------------------------增值服务工具相关接口--------------------------

* **增值服务相关接口都需要在管理控制台启动相关功能才能启用，否则调用为错误返回**

### 翻译参数以及返回结果

    enum TranslateType{         // 源数据类型
        TRANSLATE_TYPE_CHAT,
        TRANSLATE_TYPE_MAIL
    }
        
    enum ProfanityType{         // 敏感词过滤选项
        PROFANITY_TYPE_OFF,
        PROFANITY_TYPE_CENSOR
    }
    
    public static class RTMTranslateMessage {     // 翻译结果
        public String source;                     // 原始消息语言类型(经过翻译系统检测的)
        public String target;                     // 翻译后的语言类型
        public String sourceText;                 // 原始消息
        public String targetText;                 // 翻译后的消息

        @Override
        public String toString(){
            return "[RTMTranslateMessage] source = " + source + " ,target = " + target + " ,sourceText = " + sourceText + " ,targetText " + targetText;
        }
    }
    
    public enum RTMTranslateLanguage {
        AR("ar"),
        NL("nl"),
        EN("en"),
        FR("fr"),
        DE("de"),
        EL("el"),
        ID("id"),
        IT("it"),
        JA("ja"),
        KO("ko"),
        NO("no"),
        PL("pl"),
        PT("pt"),
        RU("ru"),
        ES("es"),
        SV("sv"),
        TL("tl"),
        TH("th"),
        TR("tr"),
        VI("vi"),
        ZH_CN("zh_cn"),
        ZH_TW("zh_tw");
    
        private final String value;
    
        RTMTranslateLanguage(String lang){
            this.value = lang;
        }
    
        @Override
        public String toString(){
            return this.value;
        }
    
    }
        
### 翻译聊天消息

    // old sync methods,please use new version
    RTMTranslateMessage translate(String text, String dst);
    RTMTranslateMessage translate(String text, String dst, int timeoutInsecond);
    RTMTranslateMessage translate(String text, String src, String dst, TranslateType type, ProfanityType profanity, long uid);
    RTMTranslateMessage translate(String text, String src, String dst, TranslateType type, ProfanityType profanity, long uid, int timeoutInsecond);
    // new version after 2.1.0(include)
    RTMTranslateMessage translate(String text, RTMTranslateLanguage dstLanguage, int timeoutInsecond);
    RTMTranslateMessage translate(String text, RTMTranslateLanguage dstLanguage);
    RTMTranslateMessage translate(String text, RTMTranslateLanguage sourceLanguage, RTMTranslateLanguage dstLanguage, TranslateType type, ProfanityType profanity, long uid);
    RTMTranslateMessage translate(String text, RTMTranslateLanguage sourceLanguage, RTMTranslateLanguage dstLanguage, TranslateType type, ProfanityType profanity, long uid, int timeoutInseconds);
    
    
    // old async methods  please use new version
    void translate(String text, String dst, TranslateMessageLambdaCallback callback);
    void translate(String text, String dst, TranslateMessageLambdaCallback callback, int timeoutInsecond);
    void translate(String text, String src, String dst, TranslateType type, ProfanityType profanity, long uid, TranslateMessageLambdaCallback callback);
    void translate(String text, String src, String dst, TranslateType type, ProfanityType profanity, long uid, TranslateMessageLambdaCallback callback, int timeoutInsecond);
    // new version after 2.1.0(include)
    void translate(String text, RTMTranslateLanguage dstLanguage, TranslateMessageLambdaCallback callback);
    void translate(String text, RTMTranslateLanguage dstLanguage, TranslateMessageLambdaCallback callback, int timeoutInsecond);
    void translate(String text, RTMTranslateLanguage sourceLanguage, RTMTranslateLanguage dstLanguage, TranslateType type, ProfanityType profanity, long uid, TranslateMessageLambdaCallback callback);
    void translate(String text, RTMTranslateLanguage sourceLanguage, RTMTranslateLanguage dstLanguage, TranslateType type, ProfanityType profanity, long uid, TranslateMessageLambdaCallback callback, int timeoutInsecond);
    
参数说明:  

* `String text`: 原始消息

* `String dst`: 目标语言 ISO 639-1 代码.

* `String src`: 源语言 ISO 639-1 代码可选参数. 如果为null或者空串，则系统将自动检测源语言种类.

* `RTMTranslateLanguage sourceLanguage`: 原始语言枚举值.

* `RTMTranslateLanguage dstLanguage`: 目标语言枚举值

* `TranslateType type`: 源数据类型，默认为TranslateType.TRANSLATE_TYPE_CHAT.   
    - 对于chat类型: '\t'、'\n'、' ' 在输出文本中可能被修改
    - 对于mail类型: 在输出文本中，'\t'、'\n'、' ' 将保持不变.

* `ProfanityType profanity`: 是否出发敏感词过滤. 默认为ProfanityType.PROFANITY_TYPE_OFF.    
    - ProfanityType.PROFANITY_TYPE_OFF: 不做敏感词过滤.
    - ProfanityType.PROFANITY_TYPE_CENSOR: 当发现敏感词时，敏感词将被`*`替代.
    
* `long uid`: 用户id，可选    
  
* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
  client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.
  
* `TranslateMessageLambdaCallback callback`: 为异步回调返回接口, 调用结果以及错误码和错误信息将通过callback返回
          
        public interface TranslateMessageLambdaCallback{
            void done(RTMTranslateMessage result, int errorCode, String errorMessage);
        }
  
返回值:       
  
* **sync**: 同步接口正常时返回翻译结果RTMTranslateMessage，通过参数回传返回uid的公开信息、私有信息，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.
  
* **async**: 异步接口不会抛出异常，通过callback返回翻译结果RTMTranslateMessage，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息.  

### 敏感词主动过滤

    // sync method
    void profanity(String text, StringBuffer resultText, Set<String> classification);
    void profanity(String text, StringBuffer resultText, Set<String> classification, int timeoutInsecond);
    void profanity(String text, boolean classify, long uid, StringBuffer resultText, Set<String> classification);
    void profanity(String text, boolean classify, long uid, StringBuffer resultText, Set<String> classification, int timeoutInsecond);
    
    // async methods
    void profanity(String text, ProfanityLambdaCallback callback);
    void profanity(String text, ProfanityLambdaCallback callback, int timeoutInsecond);
    void profanity(String text, boolean classify, long uid, ProfanityLambdaCallback callback);
    void profanity(String text, boolean classify, long uid, ProfanityLambdaCallback callback, int timeoutInsecond);
    
参数说明:  

* `String text`: 原始消息

* `StringBuffer resultText`: 敏感词过滤后的文本，含有的敏感词会被替换为`*`.

* `Set<String> classification`: 进行文本分类检测的结果

* `boolean classify`: 是否进行文本分类检测，默认为 false.
    
* `long uid`: 用户id，可选    
  
* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
  client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.
  
* `ProfanityLambdaCallback callback`: 为异步回调返回接口, 调用结果以及错误码和错误信息将通过callback返回
          
        public interface ProfanityLambdaCallback{
            void done(String text, Set<String> classification, int errorCode, String errorMessage);
        }
  
返回值:       
  
* **sync**: 同步接口正常时通过参数回传的方式返回敏感词过滤后的文本和文本分类检测结果，通过参数回传返回uid的公开信息、私有信息，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.
  
* **async**: 异步接口不会抛出异常，通过callback返回敏感词过滤后的文本和文本分类检测结果，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息.   

### 语音识别 

    // sync methods
    void transcribe(byte[] audio, StringBuffer text, StringBuffer lang);
    void transcribe(byte[] audio, StringBuffer text, StringBuffer lang, int timeoutInsecond);
    void transcribe(byte[] audio, long uid, boolean profanityFilter, StringBuffer text, StringBuffer lang);
    void transcribe(byte[] audio, long uid, boolean profanityFilter, StringBuffer text, StringBuffer lang, int timeoutInsecond);
    
    // async methods
    void transcribe(byte[] audio, TranscribeLambdaCallback callback);
    void transcribe(byte[] audio, TranscribeLambdaCallback callback, int timeoutInsecond);
    void transcribe(byte[] audio, long uid, boolean profanityFilter, TranscribeLambdaCallback callback);
    void transcribe(byte[] audio, long uid, boolean profanityFilter, TranscribeLambdaCallback callback, int timeoutInsecond);
    
参数说明:  

* `byte[] audio`: 语音数据，对于语音数据只支持规定的编码和头部格式，在client端的sdk会做处理，服务端sdk可能只是转发接口

* `StringBuffer text`: 语音识别后的文本.

* `StringBuffer lang`: 语音识别后的语言.

* `boolean profanityFilter`: 是否进行敏感词过滤，默认为 false.
    
* `long uid`: 用户id，可选    
  
* `int timeoutInseconds`: 该接口需要将超时时间设置到120s.
  
* `TranscribeLambdaCallback callback`: 为异步回调返回接口, 调用结果以及错误码和错误信息将通过callback返回
          
        public interface TranscribeLambdaCallback{
            void done(String text, String lang, int errorCode, String errorMessage);
        }
  
返回值:       
  
* **sync**: 同步接口正常时通过参数回传的方式返回语音识别的文本和语言，通过参数回传返回uid的公开信息、私有信息，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.
  
* **async**: 异步接口不会抛出异常，通过callback返回语音识别的文本和语言，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息.


### 语音识别 只支持rtm发送的语音消息，无需把原始语音发一遍，节省流量

    // sync method
    void stranscribeP2PAudio(long fromUid, long messageId, long toUid, boolean profanityFilter, StringBuffer text, StringBuffer lang);
    void stranscribeP2PAudio(long fromUid, long messageId, long toUid, StringBuffer text, StringBuffer lang);
    void stranscribeP2PAudio(long fromUid, long messageId, long toUid, StringBuffer text, StringBuffer lang, int timeoutInsecond);
    void stranscribeP2PAudio(long fromUid, long messageId, long toUid, boolean profanityFilter, StringBuffer text, StringBuffer lang, int timeoutInsecond);
    void stranscribeGroupAudio(long fromUid, long messageId, long groupId, boolean profanityFilter, StringBuffer text, StringBuffer lang);
    void stranscribeGroupAudio(long fromUid, long messageId, long groupId, StringBuffer text, StringBuffer lang);
    void stranscribeGroupAudio(long from, long messageId, long groupId, StringBuffer text, StringBuffer lang, int timeoutInsecond);
    void stranscribeGroupAudio(long fromUid, long messageId, long groupId, boolean profanityFilter, StringBuffer text, StringBuffer lang, int timeoutInsecond);
    void stranscribeRoomAudio(long fromUid, long messageId, long roomId, boolean profanityFilter, StringBuffer text, StringBuffer lang);
    void stranscribeRoomAudio(long fromUid, long messageId, long roomId, StringBuffer text, StringBuffer lang);
    void stranscribeRoomAudio(long fromUid, long messageId, long roomId, StringBuffer text, StringBuffer lang, int timeoutInsecond);
    void stranscribeRoomAudio(long fromUid, long messageId, long roomId, boolean profanityFilter, StringBuffer text, StringBuffer lang, int timeoutInsecond);
    void stranscribeBroadcastAudio(long fromUid, long messageId, boolean profanityFilter, StringBuffer text, StringBuffer lang);
    void stranscribeBroadcastAudio(long fromUid, long messageId, StringBuffer text, StringBuffer lang);
    void stranscribeBroadcastAudio(long fromUid, long messageId, StringBuffer text, StringBuffer lang, int timeoutInsecond);
    void stranscribeBroadcastAudio(long fromUid, long messageId, boolean profanityFilter, StringBuffer text, StringBuffer lang, int timeoutInsecond);
    
    // async method
    void stranscribeP2PAudio(long fromUid, long messageId, long toUid, boolean profanityFilter, TranscribeLambdaCallback callback, int timeoutInsecond);
    void stranscribeP2PAudio(long fromUid, long messageId, long toUid, boolean profanityFilter, TranscribeLambdaCallback callback);
    void stranscribeP2PAudio(long fromUid, long messageId, long toUid, TranscribeLambdaCallback callback, int timeoutInsecond);
    void stranscribeP2PAudio(long fromUid, long messageId, long toUid, TranscribeLambdaCallback callback);
    void stranscribeGroupAudio(long fromUid, long messageId, long groupId, boolean profanityFilter, TranscribeLambdaCallback callback, int timeoutInsecond);
    void stranscribeGroupAudio(long fromUid, long messageId, long groupId, boolean profanityFilter, TranscribeLambdaCallback callback);
    void stranscribeGroupAudio(long fromUid, long messageId, long groupId, TranscribeLambdaCallback callback, int timeoutInsecond);
    void stranscribeGroupAudio(long fromUid, long messageId, long groupId, TranscribeLambdaCallback callback);
    void stranscribeRoomAudio(long fromUid, long messageId, long roomId, boolean profanityFilter, TranscribeLambdaCallback callback, int timeoutInsecond);
    void stranscribeRoomAudio(long fromUid, long messageId, long roomId, boolean profanityFilter, TranscribeLambdaCallback callback);
    void stranscribeRoomAudio(long fromUid, long messageId, long roomId, TranscribeLambdaCallback callback, int timeoutInsecond);
    void stranscribeRoomAudio(long fromUid, long messageId, long roomId, TranscribeLambdaCallback callback);
    void stranscribeBroadcastAudio(long fromUid, long messageId, boolean profanityFilter, TranscribeLambdaCallback callback, int timeoutInsecond);
    void stranscribeBroadcastAudio(long fromUid, long messageId, boolean profanityFilter, TranscribeLambdaCallback callback);
    void stranscribeBroadcastAudio(long fromUid, long messageId, TranscribeLambdaCallback callback, int timeoutInsecond);
    void stranscribeBroadcastAudio(long fromUid, long messageId, TranscribeLambdaCallback callback);
    
参数说明:  

* `StringBuffer text`: 语音识别后的文本.

* `StringBuffer lang`: 语音识别后的语言.

* `boolean profanityFilter`: 是否进行敏感词过滤，默认为 false.
  
* `int timeoutInseconds`: 该接口需要将超时时间设置到120s.
  
* `TranscribeLambdaCallback callback`: 为异步回调返回接口, 调用结果以及错误码和错误信息将通过callback返回
          
        public interface TranscribeLambdaCallback{
            void done(String text, String lang, int errorCode, String errorMessage);
        }
  
返回值:       
  
* **sync**: 同步接口正常时通过参数回传的方式返回语音识别的文本和语言，通过参数回传返回uid的公开信息、私有信息，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.
  
* **async**: 异步接口不会抛出异常，通过callback返回语音识别的文本和语言，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息. 
    
 