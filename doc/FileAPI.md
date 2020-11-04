# RTM Server Java SDK File API Docs

# Index

[TOC]

### -------------------------发送文件接口--------------------------

### 发送 P2P 文件

    // sync methods
    long sendFile(long fromUid, long toUid, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo);
    long sendFile(long fromUid, long toUid, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, int timeoutInseconds);
    long sendFile(long fromUid, long toUid, byte mType, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, String filePath);
    long sendFile(long fromUid, long toUid, byte mType, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, int timeoutInseconds);
    long sendFile(long fromUid, long toUid, byte mType, byte[] fileContent, String filename, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, String filenameExtension);
    long sendFile(long fromUid, long toUid, byte mType, byte[] fileContent, String filename, String filenameExtension, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, int timeoutInseconds);
    
    // async methods
    void sendFile(long fromUid, long toUid, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback);
    void sendFile(long fromUid, long toUid, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback, int timeoutInseconds);
    void sendFile(long fromUid, long toUid, byte mType, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback);
    void sendFile(long fromUid, long toUid, byte mType, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback, int timeoutInseconds);
    void sendFile(long fromUid, long toUid, byte mType, byte[] fileContent, String filename, String filenameExtension, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback);
    void sendFile(long fromUid, long toUid, byte mType, byte[] fileContent, String filename, String filenameExtension, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback, int timeoutInseconds);
    
参数说明：   

* `String filePath`: 文件路径，将通过文件路径提取出文件名或者扩展名

* `byte mType`: 消息类型 默认为50 文件类型

* `String filename`: 文件名，建议不为空

* `String filenameExtension`: 文件扩展名 建议不为空

*  `String attrs`: 自定义属性 **不为空时必须为json字符串**

* `RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo`: rtm语音消息参数，结构内容参见：[RTMAudioFileInfo](HistoryMessageAPI.md#历史消息数据单元)

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `SendFileLambdaCallback callback`: 为异步回调返回接口, mtime以及错误码和错误信息将通过callback返回
        
        public interface SendFileLambdaCallback{
            void done(long time, int errorCode, String errorMessage);
        }

返回值:       

* **sync**: 同步接口正常时返回空，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回接口调用结果，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息. 

### 发送多人 P2P 文件

    // sync methods
    long sendFiles(long fromUid, Set<Long> toUids, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo);
    long sendFiles(long fromUid, Set<Long> toUids, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, int timeoutInseconds);
    long sendFiles(long fromUid, Set<Long> toUids, byte mType, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo);
    long sendFiles(long fromUid, Set<Long> toUids, byte mType, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, int timeoutInseconds);
    long sendFiles(long fromUid, Set<Long> toUids, byte mType, byte[] fileContent, String filename, String filenameExtension, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo);
    long sendFiles(long fromUid, Set<Long> toUids, byte mType, byte[] fileContent, String filename, String filenameExtension, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, int timeoutInseconds);
    
    // async methods
    void sendFiles(long fromUid, Set<Long> toUids, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback);
    void sendFiles(long fromUid, Set<Long> toUids, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback, int timeoutInseconds);
    void sendFiles(long fromUid, Set<Long> toUids, byte mType, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback);
    void sendFiles(long fromUid, Set<Long> toUids, byte mType, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback, int timeoutInseconds);
    void sendFiles(long fromUid, Set<Long> toUids, byte mType, byte[] fileContent, String filename, String filenameExtension, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback);
    void sendFiles(long fromUid, Set<Long> toUids, byte mType, byte[] fileContent, String filename, String filenameExtension, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback, int timeoutInseconds);
    
参数说明：   

* `String filePath`: 文件路径，将通过文件路径提取出文件名或者扩展名

* `byte mType`: 消息类型 默认为50 文件类型

* `String filename`: 文件名，建议不为空

* `String filenameExtension`: 文件扩展名 建议不为空

*  `String attrs`: 自定义属性 **不为空时必须为json字符串**

* `RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo`: rtm语音消息参数，结构内容参见：[RTMAudioFileInfo](HistoryMessageAPI.md#历史消息数据单元)

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `SendFileLambdaCallback callback`: 为异步回调返回接口, mtime以及错误码和错误信息将通过callback返回
        
        public interface SendFileLambdaCallback{
            void done(long time, int errorCode, String errorMessage);
        }

返回值:       

* **sync**: 同步接口正常时返回空，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回接口调用结果，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息. 

### 发送 Group 文件

    // sync methods
    long sendGroupFile(long fromUid, long groupId, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo);
    long sendGroupFile(long fromUid, long groupId, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, int timeoutInseconds);
    long sendGroupFile(long fromUid, long groupId, byte mType, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo);
    long sendGroupFile(long fromUid, long groupId, byte mType, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, int timeoutInseconds);
    long sendGroupFile(long fromUid, long groupId, byte mType, byte[] fileContent, String filename, String filenameExtension, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo);
    long sendGroupFile(long fromUid, long groupId, byte mType, byte[] fileContent, String filename, String filenameExtension, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, int timeoutInseconds);
  
    // async methods
    void sendGroupFile(long fromUid, long groupId, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback);
    void sendGroupFile(long fromUid, long groupId, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback, int timeoutInseconds);
    void sendGroupFile(long fromUid, long groupId, byte mType, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback);
    void sendGroupFile(long fromUid, long groupId, byte mType, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback, int timeoutInseconds);
    void sendGroupFile(long fromUid, long groupId, byte mType, byte[] fileContent, String filename, String filenameExtension, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback);
    void sendGroupFile(long fromUid, long groupId, byte mType, byte[] fileContent, String filename, String filenameExtension, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback, int timeoutInseconds);
    
参数说明：   

* `String filePath`: 文件路径，将通过文件路径提取出文件名或者扩展名

* `byte mType`: 消息类型 默认为50 文件类型

* `String filename`: 文件名，建议不为空

* `String filenameExtension`: 文件扩展名 建议不为空

*  `String attrs`: 自定义属性 **不为空时必须为json字符串**

* `RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo`: rtm语音消息参数，结构内容参见：[RTMAudioFileInfo](HistoryMessageAPI.md#历史消息数据单元)

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `SendFileLambdaCallback callback`: 为异步回调返回接口, mtime以及错误码和错误信息将通过callback返回
        
        public interface SendFileLambdaCallback{
            void done(long time, int errorCode, String errorMessage);
        }

返回值:       

* **sync**: 同步接口正常时返回空，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回接口调用结果，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息.

### 发送 Room 文件

    // sync methods
    long sendRoomFile(long fromUid, long roomId, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo);
    long sendRoomFile(long fromUid, long roomId, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, int timeoutInseconds);
    long sendRoomFile(long fromUid, long roomId, byte mType, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo);
    long sendRoomFile(long fromUid, long roomId, byte mType, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, int timeoutInseconds);
    long sendRoomFile(long fromUid, long roomId, byte mType, byte[] fileContent, String filename, String filenameExtension, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo);
    long sendRoomFile(long fromUid, long roomId, byte mType, byte[] fileContent, String filename, String filenameExtension, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, int timeoutInseconds);
    
    // async methods
    void sendRoomFile(long fromUid, long roomId, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback);
    void sendRoomFile(long fromUid, long roomId, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback, int timeoutInseconds);
    void sendRoomFile(long fromUid, long roomId, byte mType, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback);
    void sendRoomFile(long fromUid, long roomId, byte mType, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback, int timeoutInseconds);
    void sendRoomFile(long fromUid, long roomId, byte mType, byte[] fileContent, String filename, String filenameExtension, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback);
    void sendRoomFile(long fromUid, long roomId, byte mType, byte[] fileContent, String filename, String filenameExtension, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback, int timeoutInseconds);      
   
 参数说明：   
 
 * `String filePath`: 文件路径，将通过文件路径提取出文件名或者扩展名
 
 * `byte mType`: 消息类型 默认为50 文件类型
 
 * `String filename`: 文件名，建议不为空
 
 * `String filenameExtension`: 文件扩展名 建议不为空
 
 *  `String attrs`: 自定义属性 **不为空时必须为json字符串**
 
 * `RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo`: rtm语音消息参数，结构内容参见：[RTMAudioFileInfo](HistoryMessageAPI.md#历史消息数据单元)
 
 * `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
 client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.
 
 * `SendFileLambdaCallback callback`: 为异步回调返回接口, mtime以及错误码和错误信息将通过callback返回
         
         public interface SendFileLambdaCallback{
             void done(long time, int errorCode, String errorMessage);
         }
 
 返回值:      
  
 * **sync**: 同步接口正常时返回空，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.
 
 * **async**: 异步接口不会抛出异常，通过callback返回接口调用结果，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息. 
 
 ### 发送 Broadcast 文件
 
    // sync methods 
    long sendBroadcastFile(long fromUid, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo);
    long sendBroadcastFile(long fromUid, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, int timeoutInseconds);
    long sendBroadcastFile(long fromUid, byte mType, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo);
    long sendBroadcastFile(long fromUid, byte mType, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, int timeoutInseconds);
    long sendBroadcastFile(long fromUid, byte mType, byte[] fileContent, String filename, String filenameExtension, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo);
    long sendBroadcastFile(long fromUid, byte mType, byte[] fileContent, String filename, String filenameExtension, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, int timeoutInseconds);
    
    // async methods
    void sendBroadcastFile(long fromUid, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback);
    void sendBroadcastFile(long fromUid, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback, int timeoutInseconds);
    void sendBroadcastFile(long fromUid, byte mType, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback);
    void sendBroadcastFile(long fromUid,byte mType, String filePath, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback, int timeoutInseconds);
    void sendBroadcastFile(long fromUid, byte mType, byte[] fileContent, String filename, String filenameExtension, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback);
    void sendBroadcastFile(long fromUid, byte mType, byte[] fileContent, String filename, String filenameExtension, String attrs, RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo, SendFileLambdaCallback callback, int timeoutInseconds);
   
  参数说明：  
   
  * `String filePath`: 文件路径，将通过文件路径提取出文件名或者扩展名
  
  * `byte mType`: 消息类型 默认为50 文件类型
  
  * `String filename`: 文件名，建议不为空
  
  * `String filenameExtension`: 文件扩展名 建议不为空
  
  *  `String attrs`: 自定义属性 **不为空时必须为json字符串**
  
  * `RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo`: rtm语音消息参数，结构内容参见：[RTMAudioFileInfo](HistoryMessageAPI.md#历史消息数据单元)
  
  * `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
  client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.
  
  * `SendFileLambdaCallback callback`: 为异步回调返回接口, mtime以及错误码和错误信息将通过callback返回
          
          public interface SendFileLambdaCallback{
              void done(long time, int errorCode, String errorMessage);
          }
  
  返回值: 
        
  * **sync**: 同步接口正常时返回空，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.
  
  * **async**: 异步接口不会抛出异常，通过callback返回接口调用结果，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息. 