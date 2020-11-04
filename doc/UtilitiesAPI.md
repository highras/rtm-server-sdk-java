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
  
* **sync**: 同步接口正常时返回翻译结果RTMTranslateMessage，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.
  
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

Note:    
* Maybe in after version this interface will be deprecated，recommend use textCheck interface replace
    
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
  
* **sync**: 同步接口正常时通过参数回传的方式返回敏感词过滤后的文本和文本分类检测结果，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.
  
* **async**: 异步接口不会抛出异常，通过callback返回敏感词过滤后的文本和文本分类检测结果，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息.   

### 语音转文字

    // sync method
    void speechToText(String audio, int audioType, RTMTranslateLanguage lang, String codec, int srate, long uid,  StringBuffer text, StringBuffer dstLang, int timeoutInsecond);
    void speechToText(String audio, int audioType, RTMTranslateLanguage lang, String codec, int srate, long uid,  StringBuffer text, StringBuffer dstLang);
    void speechToText(String audio, int audioType, RTMTranslateLanguage lang, StringBuffer text, StringBuffer dstLang, int timeoutInsecond);
    void speechToText(String audio, int audioType, RTMTranslateLanguage lang, StringBuffer text, StringBuffer dstLang);
    
    // async method
    void speechToText(String audio, int audioType, RTMTranslateLanguage lang, String codec, int srate, long uid, SpeechToTextLambdaCallback callback, int timeoutInsecond);
    void speechToText(String audio, int audioType, RTMTranslateLanguage lang, String codec, int srate, long uid, SpeechToTextLambdaCallback callback);
    void speechToText(String audio, int audioType, RTMTranslateLanguage lang, SpeechToTextLambdaCallback callback, int timeoutInsecond);
    void speechToText(String audio, int audioType, RTMTranslateLanguage lang, SpeechToTextLambdaCallback callback)
    
 参数说明:  
 
 * `String audio`: 音频的url或者内容（需要提供lang&codec&srate)
 
 * `int audioType`: 1：url, 2：内容
 
 * `RTMTranslateLanguage lang`: 参见: [RTMTranslateLanguage](#翻译参数以及返回结果)
 
 * ` String codec`: codec为空则默认为AMR_WB
     
 * `int srate`: srate为0或者空则默认为16000   
 
 * `long uid`: 可选用户id
 
 * `StringBuffer text`: 识别后的文本
 
 * `StringBuffer dstLang`: 识别后的语言
   
 * `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
   client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.
   
 * `SpeechToTextLambdaCallback callback`: 为异步回调返回接口, 调用结果以及错误码和错误信息将通过callback返回
           
         interface SpeechToTextLambdaCallback {
             void done(String text, String lang, int errorCode, String errorMessage);
         }
   
 返回值:       
   
 * **sync**: 同步接口正常时通过参数回传的方式返回识别后的文本和语言，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.
   
 * **async**: 异步接口不会抛出异常，通过callback返回识别后的文本结果，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息.  
 
 ### 文本审核
 
    // sync method
    int textCheck(String text, long uid, StringBuffer resultText, Set<Integer> tags, Set<String> wlist, int timeoutInsecond);
    int textCheck(String text, long uid, StringBuffer resultText, Set<Integer> tags, Set<String> wlist);
    int textCheck(String text, StringBuffer resultText, Set<Integer> tags, Set<String> wlist);
    int textCheck(String text, StringBuffer resultText, Set<Integer> tags, Set<String> wlist, int timeoutInsecond);
    
    // async method
    void textCheck(String text, long uid, TextCheckLambdaCallback callback, int timeoutInsecond);
    void textCheck(String text, long uid, TextCheckLambdaCallback callback);
    void textCheck(String text, TextCheckLambdaCallback callback);
    void textCheck(String text, TextCheckLambdaCallback callback, int timeoutInsecond);
    
 参数说明:  
  
 * `String text`: 文本内容  
  
 * `long uid`: 可选用户id
  
 * `StringBuffer resultText`: 审核后的文本
  
 * `Set<Integer> tags`: 触发的分类，比如涉黄涉政等等，具体见文本审核分类
 
 * `Set<String> wlist`: 敏感词列表
    
 * `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
    client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.
    
 * `TextCheckLambdaCallback callback`: 为异步回调返回接口, 调用结果以及错误码和错误信息将通过callback返回
            
        interface TextCheckLambdaCallback {
            void done(int result, String text, Set<Integer> tags, Set<String> wlist, int errorCode, String errorMessage);
        }
    
返回值:       
result: 0: 通过，2，不通过  
resultText：敏感词过滤后的文本内容，含有的敏感词会被替换为*，如果没有被标星，则无此字段
  
* **sync**: 同步接口正常时通过参数回传的方式返回审核后的结果，当返回的result=2，正常处理是：如果text不为空则可以直接发出(用返回的text)，否则拦截（可能是广告或者隐晦色情等等），
错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.
    
* **async**: 异步接口不会抛出异常，通过callback返回审核后的结果，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息. 

注：如果需要详细的返回结果，请调用审核产品原生接口

### 图片审核

    // sync method
    int imageCheck(String image, int imageType, long uid, Set<Integer>tags,  int timeoutInsecond);
    int imageCheck(String image, int imageType, long uid, Set<Integer>tags);
    int imageCheck(String image, int imageType, Set<Integer>tags);
    int imageCheck(String image, int imageType, Set<Integer>tags, int timeoutInsecond);
    
    // async method
    void imageCheck(String image, int imageType, long uid, OtherCheckLambdaCallback callback, int timeoutInsecond);
    void imageCheck(String image, int imageType, long uid, OtherCheckLambdaCallback callback);
    void imageCheck(String image, int imageType, OtherCheckLambdaCallback callback);
    void imageCheck(String image, int imageType, OtherCheckLambdaCallback callback, int timeoutInsecond);
    
 参数说明:  
  
 * `String image`: 图片的url 或者内容  
  
 * `long uid`: 可选用户id
  
 * `int imageType`: 1: url, 2: 内容
  
 * `Set<Integer> tags`: 触发的分类，比如涉黄涉政等等，具体见图片审核分类
    
 * `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
    client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.
    
 * `OtherCheckLambdaCallback callback`: 为异步回调返回接口, 调用结果以及错误码和错误信息将通过callback返回
            
        interface OtherCheckLambdaCallback {
            void done(int result, Set<Integer> tags, int errorCode, String errorMessage);
        }
    
返回值:       
result: 0: 通过，2，不通过  
  
* **sync**: 同步接口正常时通过参数回传的方式返回审核后的结果，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.
    
* **async**: 异步接口不会抛出异常，通过callback返回审核后的结果，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息. 

注：如果需要详细的返回结果，请调用审核产品原生接口  

### 视频审核

    // sync method
    int videoCheck(String video, int videoType, String videoName, long uid, Set<Integer> tags, int timeoutInsecond);
    int videoCheck(String video, int videoType, String videoName, long uid, Set<Integer>tags);
    int videoCheck(String video, int videoType, String videoName, Set<Integer>tags);
    int videoCheck(String video, int videoType, String videoName, Set<Integer>tags, int timeoutInsecond);
    
    // async method
    void videoCheck(String video, int videoType, String videoName, long uid, OtherCheckLambdaCallback callback, int timeoutInsecond);
    void videoCheck(String video, int videoType, String videoName, long uid, OtherCheckLambdaCallback callback);
    void videoCheck(String video, int videoType, String videoName, OtherCheckLambdaCallback callback);
    void videoCheck(String video, int videoType, String videoName, OtherCheckLambdaCallback callback, int timeoutInsecond);
    
 参数说明:  
  
 * `String video`: 视频的url或者内容 
  
 * `long uid`: 可选用户id
  
 * `int imageType`: 1: url, 2: 内容
 
 * `String videoName`: 视频文件名，type=2时候必选，可以通过文件名获取文件格式
  
 * `Set<Integer> tags`: 触发的分类，比如涉黄涉政等等，具体见图片审核分类
    
 * `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
    client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.
    
 * `OtherCheckLambdaCallback callback`: 为异步回调返回接口, 调用结果以及错误码和错误信息将通过callback返回
            
        interface OtherCheckLambdaCallback {
            void done(int result, Set<Integer> tags, int errorCode, String errorMessage);
        }
    
返回值:       
result: 0: 通过，2，不通过  
  
* **sync**: 同步接口正常时通过参数回传的方式返回审核后的结果，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.
    
* **async**: 异步接口不会抛出异常，通过callback返回审核后的结果，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息. 

注：如果需要详细的返回结果，请调用审核产品原生接口 

### 音频审核

    // sync method
    int audioCheck(String audio, int audioType, RTMTranslateLanguage lang, long uid, String codec, int srate, Set<Integer>tags,  int timeoutInsecond);
    int audioCheck(String audio, int audioType, RTMTranslateLanguage lang, long uid, String codec, int srate, Set<Integer>tags);
    int audioCheck(String audio, int audioType, RTMTranslateLanguage lang, Set<Integer>tags);
    int audioCheck(String audio, int audioType, RTMTranslateLanguage lang, Set<Integer>tags, int timeoutInsecond);
    
    // async method
    void audioCheck(String audio, int audioType, RTMTranslateLanguage lang, long uid, String codec, int srate, OtherCheckLambdaCallback callback, int timeoutInsecond);
    void audioCheck(String audio, int audioType, RTMTranslateLanguage lang, long uid, String codec, int srate, OtherCheckLambdaCallback callback);
    void audioCheck(String audio, int audioType, RTMTranslateLanguage lang, OtherCheckLambdaCallback callback);
    void audioCheck(String audio, int audioType, RTMTranslateLanguage lang, OtherCheckLambdaCallback callback, int timeoutInsecond);
    
 参数说明:  
  
 * `String audio`: 音频的url或者内容（需要提供lang&codec&srate)
 
 * `int audioType`: 1：url, 2：内容
 
 * `RTMTranslateLanguage lang`: 参见: [RTMTranslateLanguage](#翻译参数以及返回结果)
 
 * ` String codec`: codec为空则默认为AMR_WB
     
 * `int srate`: srate为0或者空则默认为16000   
 
 * `long uid`: 可选用户id
  
 * `Set<Integer> tags`: 触发的分类，比如涉黄涉政等等，具体见图片审核分类
    
 * `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
    client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.
    
 * `OtherCheckLambdaCallback callback`: 为异步回调返回接口, 调用结果以及错误码和错误信息将通过callback返回
            
        interface OtherCheckLambdaCallback {
            void done(int result, Set<Integer> tags, int errorCode, String errorMessage);
        }
    
返回值:       
result: 0: 通过，2，不通过  
  
* **sync**: 同步接口正常时通过参数回传的方式返回审核后的结果，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.
    
* **async**: 异步接口不会抛出异常，通过callback返回审核后的结果，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息. 

注：如果需要详细的返回结果，请调用审核产品原生接口

