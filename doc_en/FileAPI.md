# RTM Server Java SDK File API Docs

# Index

[TOC]

### -------------------------Send file interface--------------------------

### Send P2P file

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
    
Parameter Description:   

* `String filePath`: file path, the file name or extension will be extracted from the file path

* `byte mType`: message type defaults to 50 file type

* `String filename`: file name, it is recommended not to be empty

* `String filenameExtension`: File extension name is recommended not to be empty

* `String attrs`: custom attribute **must be a json string if not empty**

* `RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo`: rtm voice message parameters, structure content see: [RTMAudioFileInfo](HistoryMessageAPI.md#historical message data unit)

* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.

* `SendFileLambdaCallback callback`: return interface for asynchronous callback, mtime, error code and error message will be returned through callback
        
        public interface SendFileLambdaCallback{
            void done(long time, int errorCode, String errorMessage);
        }

return value:       

* **sync**: When the synchronization interface is normal, it returns empty. When the error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.

* **async**: The asynchronous interface will not throw an exception, and the interface call result will be returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it will be returned as an error. You can view the message error information.

### Send multiple P2P files

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
    
Parameter Description:   

* `String filePath`: file path, the file name or extension will be extracted from the file path

* `byte mType`: message type defaults to 50 file type

* `String filename`: file name, it is recommended not to be empty

* `String filenameExtension`: File extension name is recommended not to be empty

* `String attrs`: custom attribute **must be a json string if not empty**

* `RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo`: rtm voice message parameters, structure content see: [RTMAudioFileInfo](HistoryMessageAPI.md#historical message data unit)

* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.

* `SendFileLambdaCallback callback`: return interface for asynchronous callback, mtime, error code and error message will be returned through callback
        
        public interface SendFileLambdaCallback{
            void done(long time, int errorCode, String errorMessage);
        }

return value:       

* **sync**: When the synchronization interface is normal, it returns empty. When the error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.

* **async**: The asynchronous interface will not throw an exception, and the interface call result will be returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it will be returned as an error. You can view the message error information.

### Send Group File

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
    
Parameter Description:   

* `String filePath`: file path, the file name or extension will be extracted from the file path

* `byte mType`: message type defaults to 50 file type

* `String filename`: file name, it is recommended not to be empty

* `String filenameExtension`: File extension name is recommended not to be empty

* `String attrs`: custom attribute **must be a json string if not empty**

* `RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo`: rtm voice message parameters, structure content see: [RTMAudioFileInfo](HistoryMessageAPI.md#historical message data unit)

* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.

* `SendFileLambdaCallback callback`: return interface for asynchronous callback, mtime, error code and error message will be returned through callback
        
        public interface SendFileLambdaCallback{
            void done(long time, int errorCode, String errorMessage);
        }

return value:       

* **sync**: When the synchronization interface is normal, it returns empty. When the error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.

* **async**: The asynchronous interface will not throw an exception, and the interface call result will be returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it will be returned as an error. You can view the message error information.

### Send Room file

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
   
 Parameter Description:   
 
 * `String filePath`: file path, the file name or extension will be extracted from the file path
 
 * `byte mType`: message type defaults to 50 file type
 
 * `String filename`: file name, it is recommended not to be empty
 
 * `String filenameExtension`: File extension name is recommended not to be empty
 
 * `String attrs`: custom attribute **must be a json string if not empty**
 
 * `RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo`: rtm voice message parameters, structure content see: [RTMAudioFileInfo](HistoryMessageAPI.md#historical message data unit)
 
 * `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.
 
 * `SendFileLambdaCallback callback`: return interface for asynchronous callback, mtime, error code and error message will be returned through callback
         
         public interface SendFileLambdaCallback{
             void done(long time, int errorCode, String errorMessage);
         }
 
 return value:      
  
 * **sync**: When the synchronization interface is normal, it returns empty. When the error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.
 
 * **async**: The asynchronous interface will not throw an exception, and the interface call result will be returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it will be returned as an error. You can view the message error information.
 
 ### Send Broadcast file
 
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
   
  Parameter Description:  
   
  * `String filePath`: file path, the file name or extension will be extracted from the file path
  
  * `byte mType`: message type defaults to 50 file type
  
  * `String filename`: file name, it is recommended not to be empty
  
  * `String filenameExtension`: File extension name is recommended not to be empty
  
  * `String attrs`: custom attribute **must be a json string if not empty**
  
  * `RTMServerClientBase.RTMAudioFileInfo rtmAudioFileInfo`: rtm voice message parameters, structure content see: [RTMAudioFileInfo](HistoryMessageAPI.md#historical message data unit)
  
  * `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.
  
  * `SendFileLambdaCallback callback`: return interface for asynchronous callback, mtime, error code and error message will be returned through callback
          
          public interface SendFileLambdaCallback{
              void done(long time, int errorCode, String errorMessage);
          }
  
  return value: 
        
  * **sync**: When the synchronization interface is normal, it returns empty. When the error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.
  
  * **async**: The asynchronous interface will not throw an exception, and the interface call result will be returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it will be returned as an error. You can view the message error information.