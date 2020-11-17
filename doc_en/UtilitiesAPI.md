# RTM Server Java SDK Utilities API Docs

# Index

[TOC]

### ------------------------Value-added service tool related interface--------------------------

* **Value-added service related interfaces need to be activated in the management console to enable the relevant functions, otherwise the call will return an error**

### Translate parameters and return results

    enum TranslateType{         // source data type
        TRANSLATE_TYPE_CHAT,
        TRANSLATE_TYPE_MAIL
    }
        
    enum ProfanityType{         // Sensitive word filtering options
        PROFANITY_TYPE_OFF,
        PROFANITY_TYPE_CENSOR
    }
    
    public static class RTMTranslateMessage {   // Translation result
        public String source;    // Original message language type (tested by the translation system)
        public String target;    // translated language type
        public String sourceText;  // original message
        public String targetText;  // translated message

        @Override
        public String toString(){
            return "[RTMTranslateMessage] source = "+ source +" ,target = "+ target +" ,sourceText = "+ sourceText +" ,targetText "+ targetText;
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
        
### Translate chat messages

    // old sync methods, please use new version
    RTMTranslateMessage translate(String text, String dst);
    RTMTranslateMessage translate(String text, String dst, int timeoutInsecond);
    RTMTranslateMessage translate(String text, String src, String dst, TranslateType type, ProfanityType profanity, long uid);
    RTMTranslateMessage translate(String text, String src, String dst, TranslateType type, ProfanityType profanity, long uid, int timeoutInsecond);
    // new version after 2.1.0(include)
    RTMTranslateMessage translate(String text, RTMTranslateLanguage dstLanguage, int timeoutInsecond);
    RTMTranslateMessage translate(String text, RTMTranslateLanguage dstLanguage);
    RTMTranslateMessage translate(String text, RTMTranslateLanguage sourceLanguage, RTMTranslateLanguage dstLanguage, TranslateType type, ProfanityType profanity, long uid);
    RTMTranslateMessage translate(String text, RTMTranslateLanguage sourceLanguage, RTMTranslateLanguage dstLanguage, TranslateType type, ProfanityType profanity, long uid, int timeoutInseconds);
    
    
    // old async methods please use new version
    void translate(String text, String dst, TranslateMessageLambdaCallback callback);
    void translate(String text, String dst, TranslateMessageLambdaCallback callback, int timeoutInsecond);
    void translate(String text, String src, String dst, TranslateType type, ProfanityType profanity, long uid, TranslateMessageLambdaCallback callback);
    void translate(String text, String src, String dst, TranslateType type, ProfanityType profanity, long uid, TranslateMessageLambdaCallback callback, int timeoutInsecond);
    // new version after 2.1.0(include)
    void translate(String text, RTMTranslateLanguage dstLanguage, TranslateMessageLambdaCallback callback);
    void translate(String text, RTMTranslateLanguage dstLanguage, TranslateMessageLambdaCallback callback, int timeoutInsecond);
    void translate(String text, RTMTranslateLanguage sourceLanguage, RTMTranslateLanguage dstLanguage, TranslateType type, ProfanityType profanity, long uid, TranslateMessageLambdaCallback callback);
    void translate(String text, RTMTranslateLanguage sourceLanguage, RTMTranslateLanguage dstLanguage, TranslateType type, ProfanityType profanity, long uid, TranslateMessageLambdaCallback callback, int timeoutInsecond);
    
Parameter Description:  

* `String text`: original message

* `String dst`: Target language ISO 639-1 code.

* `String src`: Source language ISO 639-1 code optional parameter. If it is a null or empty string, the system will automatically detect the source language.

* `RTMTranslateLanguage sourceLanguage`: Original language enumeration value.

* `RTMTranslateLanguage dstLanguage`: target language enumeration value

* `TranslateType type`: Source data type, the default is TranslateType.TRANSLATE_TYPE_CHAT.
    -For chat type:'\t','\n', '' may be modified in the output text
    -For mail type: In the output text,'\t','\n', '' will remain unchanged.

* `ProfanityType profanity`: Whether to start sensitive word filtering. The default is ProfanityType.PROFANITY_TYPE_OFF.
    -ProfanityType.PROFANITY_TYPE_OFF: No sensitive word filtering.
    -ProfanityType.PROFANITY_TYPE_CENSOR: When a sensitive word is found, the sensitive word will be replaced by `*`.
    
* `long uid`: user id, optional
  
* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.
  
* `TranslateMessageLambdaCallback callback`: return interface for asynchronous callback, the call result, error code and error message will be returned through callback
          
        public interface TranslateMessageLambdaCallback{
            void done(RTMTranslateMessage result, int errorCode, String errorMessage);
        }
  
return value:       
  
* **sync**: When the synchronization interface is normal, the translation result RTMTranslateMessage will be returned. When an error is returned, an RTMException or other systemic exception will be thrown. For RTMException exceptions, you can view the error information through the toString method.
  
* **async**: The asynchronous interface will not throw an exception. The translation result RTMTranslateMessage is returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it is returned as an error. You can view the message error message.

### Active filtering of sensitive words

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
* Maybe in after version this interface will be deprecated, recommend use textCheck interface replace
    
Parameter Description:  

* `String text`: original message

* `StringBuffer resultText`: The filtered text of sensitive words, the sensitive words contained in it will be replaced with `*`.

* `Set<String> classification`: the result of text classification detection

* `boolean classify`: Whether to perform text classification detection, the default is false.
    
* `long uid`: user id, optional
  
* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.
  
* `ProfanityLambdaCallback callback`: Return interface for asynchronous callback, call result, error code and error message will be returned through callback
          
        public interface ProfanityLambdaCallback{
            void done(String text, Set<String> classification, int errorCode, String errorMessage);
        }
  
return value:       
  
* **sync**: When the synchronization interface is normal, the sensitive word filtered text and text classification detection results will be returned through parameter return. When the error is returned, an exception RTMException or other systemic exceptions will be thrown. For RTMException exceptions, you can use toString Method to view error information.
  
* **async**: The asynchronous interface will not throw exceptions. The sensitive word filtered text and text classification detection results are returned through callback. When errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it is returned as error. You can view the message Error message.

### Speech to text

    // sync method
    void speechToText(String audio, int audioType, RTMTranslateLanguage lang, String codec, int srate, long uid, StringBuffer text, StringBuffer dstLang, int timeoutInsecond);
    void speechToText(String audio, int audioType, RTMTranslateLanguage lang, String codec, int srate, long uid, StringBuffer text, StringBuffer dstLang);
    void speechToText(String audio, int audioType, RTMTranslateLanguage lang, StringBuffer text, StringBuffer dstLang, int timeoutInsecond);
    void speechToText(String audio, int audioType, RTMTranslateLanguage lang, StringBuffer text, StringBuffer dstLang);
    
    // async method
    void speechToText(String audio, int audioType, RTMTranslateLanguage lang, String codec, int srate, long uid, SpeechToTextLambdaCallback callback, int timeoutInsecond);
    void speechToText(String audio, int audioType, RTMTranslateLanguage lang, String codec, int srate, long uid, SpeechToTextLambdaCallback callback);
    void speechToText(String audio, int audioType, RTMTranslateLanguage lang, SpeechToTextLambdaCallback callback, int timeoutInsecond);
    void speechToText(String audio, int audioType, RTMTranslateLanguage lang, SpeechToTextLambdaCallback callback)
    
 Parameter Description:  
 
 * `String audio`: the url or content of the audio (lang&codec&srate is required)
 
 * `int audioType`: 1: url, 2: content
 
 * `RTMTranslateLanguage lang`: See: [RTMTranslateLanguage](#Translation parameters and return results)
 
 * `String codec`: If codec is empty, the default is AMR_WB
     
 * `int srate`: If srate is 0 or empty, the default is 16000
 
 * `long uid`: optional user id
 
 * `StringBuffer text`: the recognized text
 
 * `StringBuffer dstLang`: recognized language
   
 * `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.
   
 * `SpeechToTextLambdaCallback callback`: return interface for asynchronous callback, call result, error code and error information will be returned through callback
           
         interface SpeechToTextLambdaCallback {
             void done(String text, String lang, int errorCode, String errorMessage);
         }
   
 return value:       
   
 * **sync**: When the synchronization interface is normal, the recognized text and language will be returned through parameter return. When an error is returned, an RTMException or other systemic exception will be thrown. For RTMException exceptions, you can view the error information through the toString method.
   
 * **async**: The asynchronous interface does not throw an exception, and the recognized text result is returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it is returned as an error. You can view the message error information.
 
 ### Text review
 
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
    
 Parameter Description:  
  
 * `String text`: text content
  
 * `long uid`: optional user id
  
 * `StringBuffer resultText`: Text after review
  
 * `Set<Integer> tags`: Triggered categories, such as pornography and politics, etc., see text review categories for details
 
 * `Set<String> wlist`: sensitive word list
    
 * `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.
    
 * `TextCheckLambdaCallback callback`: return interface for asynchronous callback, the call result, error code and error message will be returned through callback
            
        interface TextCheckLambdaCallback {
            void done(int result, String text, Set<Integer> tags, Set<String> wlist, int errorCode, String errorMessage);
        }
    
return value:       
result: 0: pass, 2, fail
resultText: text content filtered by sensitive words, the sensitive words contained in it will be replaced with *, if not marked with a star, there will be no such field
  
* **sync**: When the synchronization interface is normal, the audited result is returned by parameter return. When the returned result=2, the normal processing is: if the text is not empty, it can be sent directly (using the returned text), Otherwise block (maybe advertising or obscure pornography, etc.),
When the error returns, an exception RTMException or other systemic exceptions will be thrown. For RTMException exceptions, you can view the error information through the toString method.
    
* **async**: The asynchronous interface will not throw an exception, and the audited result will be returned through the callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it will be returned as an error. You can view the message error information.

Note: If you need detailed return results, please call the native interface of the audit product

### Picture review

    // sync method
    int imageCheck(String image, int imageType, long uid, Set<Integer>tags, int timeoutInsecond);
    int imageCheck(String image, int imageType, long uid, Set<Integer>tags);
    int imageCheck(String image, int imageType, Set<Integer>tags);
    int imageCheck(String image, int imageType, Set<Integer>tags, int timeoutInsecond);
    
    // async method
    void imageCheck(String image, int imageType, long uid, OtherCheckLambdaCallback callback, int timeoutInsecond);
    void imageCheck(String image, int imageType, long uid, OtherCheckLambdaCallback callback);
    void imageCheck(String image, int imageType, OtherCheckLambdaCallback callback);
    void imageCheck(String image, int imageType, OtherCheckLambdaCallback callback, int timeoutInsecond);
    
 Parameter Description:  
  
 * `String image`: the url or content of the image
  
 * `long uid`: optional user id
  
 * `int imageType`: 1: url, 2: content
  
 * `Set<Integer> tags`: Triggered categories, such as pornography and politics, etc., see image review category for details
    
 * `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.
    
 * `OtherCheckLambdaCallback callback`: Return interface for asynchronous callback, the call result, error code and error message will be returned through callback
            
        interface OtherCheckLambdaCallback {
            void done(int result, Set<Integer> tags, int errorCode, String errorMessage);
        }
    
return value:       
result: 0: pass, 2, fail
  
* **sync**: When the synchronization interface is normal, the audited result will be returned by parameter return. When an error is returned, an RTMException or other systemic exception will be thrown. For RTMException, you can view the error information through the toString method.
    
* **async**: The asynchronous interface will not throw an exception, and the audited result will be returned through the callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it will be returned as an error. You can view the message error information.

Note: If you need detailed return results, please call the native interface of the audit product

### Video review

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
    
 Parameter Description:  
  
 * `String video`: the url or content of the video
  
 * `long uid`: optional user id
  
 * `int imageType`: 1: url, 2: content
 
 * `String videoName`: video file name, required when type=2, the file format can be obtained by the file name
  
 * `Set<Integer> tags`: Triggered categories, such as pornography and politics, etc., see image review category for details
    
 * `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.
    
 * `OtherCheckLambdaCallback callback`: return interface for asynchronous callback, call result, error code and error message will be returned through callback
            
        interface OtherCheckLambdaCallback {
            void done(int result, Set<Integer> tags, int errorCode, String errorMessage);
        }
    
return value:       
result: 0: pass, 2, fail
  
* **sync**: When the synchronization interface is normal, the audited result will be returned by parameter return. When an error is returned, an RTMException or other systemic exception will be thrown. For RTMException, you can view the error information through the toString method.
    
* **async**: The asynchronous interface will not throw an exception, and the audited result will be returned through the callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it will be returned as an error. You can view the message error information.

Note: If you need detailed return results, please call the native interface of the audit product

### Audio review

    // sync method
    int audioCheck(String audio, int audioType, RTMTranslateLanguage lang, long uid, String codec, int srate, Set<Integer>tags, int timeoutInsecond);
    int audioCheck(String audio, int audioType, RTMTranslateLanguage lang, long uid, String codec, int srate, Set<Integer>tags);
    int audioCheck(String audio, int audioType, RTMTranslateLanguage lang, Set<Integer>tags);
    int audioCheck(String audio, int audioType, RTMTranslateLanguage lang, Set<Integer>tags, int timeoutInsecond);
    
    // async method
    void audioCheck(String audio, int audioType, RTMTranslateLanguage lang, long uid, String codec, int srate, OtherCheckLambdaCallback callback, int timeoutInsecond);
    void audioCheck(String audio, int audioType, RTMTranslateLanguage lang, long uid, String codec, int srate, OtherCheckLambdaCallback callback);
    void audioCheck(String audio, int audioType, RTMTranslateLanguage lang, OtherCheckLambdaCallback callback);
    void audioCheck(String audio, int audioType, RTMTranslateLanguage lang, OtherCheckLambdaCallback callback, int timeoutInsecond);
    
 Parameter Description:  
  
 * `String audio`: the url or content of the audio (lang&codec&srate is required)
 
 * `int audioType`: 1: url, 2: content
 
 * `RTMTranslateLanguage lang`: See: [RTMTranslateLanguage](#Translation parameters and return results)
 
 * `String codec`: If codec is empty, the default is AMR_WB
     
 * `int srate`: If srate is 0 or empty, the default is 16000
 
 * `long uid`: optional user id
  
 * `Set<Integer> tags`: Triggered categories, such as pornography and politics, etc., see image review category for details
    
 * `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.
    
 * `OtherCheckLambdaCallback callback`: return interface for asynchronous callback, call result, error code and error message will be returned through callback
            
        interface OtherCheckLambdaCallback {
            void done(int result, Set<Integer> tags, int errorCode, String errorMessage);
        }
    
return value:       
result: 0: pass, 2, fail
  
* **sync**: When the synchronization interface is normal, the audited result will be returned by parameter return. When an error is returned, an RTMException or other systemic exception will be thrown. For RTMException, you can view the error information through the toString method.
    
* **async**: The asynchronous interface will not throw an exception, and the audited result will be returned through the callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it will be returned as an error. You can view the message error information.

Note: If you need detailed return results, please call the native interface of the audit product
