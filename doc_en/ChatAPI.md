# RTM Server Java SDK Chat API Docs

# Index

[TOC]

### -------------------------Send chat message interface--------------------------

### Send P2P chat messages

    // sync methods
    long sendChat(long fromUid, long toUid, String message, String attrs);
    long sendChat(long fromUid, long toUid, String message, String attrs, int timeoutInseconds);
    
    // async methods
    void sendChat(long fromUid, long toUid, String message, String attrs, SendMessageLambdaCallback callback);
    void sendChat(long fromUid, long toUid, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);
    
Parameter Description:   
* `String attrs`: message attribute information, it is recommended to use a parseable json string, the default is an empty string

* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.

* `SendMessageLambdaCallback callback`: return interface for asynchronous callback, mtime and error information will be returned through callback
        
        public interface SendMessageLambdaCallback{
            void done(long time, int errorCode, String errorMessage);
        }

return value:       
* **sync**: When the synchronization interface is normal, it returns mtime, and when an error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.

* **async**: The asynchronous interface will not throw an exception. It returns mtime or error information through callback. When errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it is returned as error. You can view the message error information.
   
### Send P2P control commands

    // sync methods
    long sendCmd(long fromUid, long toUid, String message, String attrs);
    long sendCmd(long fromUid, long toUid, String message, String attrs, int timeoutInseconds);
    
    // async methods
    void sendCmd(long fromUid, long toUid, String message, String attrs, SendMessageLambdaCallback callback);
    void sendCmd(long fromUid, long toUid, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);
 
Parameter Description:   
* `String attrs`: message attribute information, it is recommended to use a parseable json string, the default is an empty string

* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.

* `SendMessageLambdaCallback callback`: return interface for asynchronous callback, mtime and error information will be returned through callback
        
        public interface SendMessageLambdaCallback{
            void done(long time, int errorCode, String errorMessage);
        }

return value:       
* **sync**: When the synchronization interface is normal, it returns mtime, and when an error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.

* **async**: The asynchronous interface will not throw an exception. It returns mtime or error information through callback. When errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it is returned as error. You can view the message error information.

### Send multi-person P2P chat messages

    // sync methods
    long sendChats(long fromUid, Set<Long> toUids, String message, String attrs);
    long sendChats(long fromUid, Set<Long> toUids, String message, String attrs, int timeoutInseconds);
    
    // async methods
    void sendChats(long fromUid, Set<Long> toUids, String message, String attrs, SendMessageLambdaCallback callback);
    void sendChats(long fromUid, Set<Long> toUids, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);
    
Parameter Description:   
* `String attrs`: message attribute information, it is recommended to use a parseable json string, the default is an empty string

* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.

* `SendMessageLambdaCallback callback`: return interface for asynchronous callback, mtime and error information will be returned through callback
        
        public interface SendMessageLambdaCallback{
            void done(long time, int errorCode, String errorMessage);
        }

return value:       
* **sync**: When the synchronization interface is normal, it returns mtime, and when an error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.

* **async**: The asynchronous interface will not throw an exception. It returns mtime or error information through callback. When errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it is returned as error. You can view the message error information.

### Send multi-person P2P control commands

    // sync methods
    long sendCmds(long fromUid, Set<Long> toUids, String message, String attrs);
    long sendCmds(long fromUid, Set<Long> toUids, String message, String attrs, int timeoutInseconds);
    
    // async methods
    void sendCmds(long fromUid, Set<Long> toUids, String message, String attrs, SendMessageLambdaCallback callback);
    void sendCmds(long fromUid, Set<Long> toUids, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);
    
Parameter Description:   
* `String attrs`: message attribute information, it is recommended to use a parseable json string, the default is an empty string

* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.

* `SendMessageLambdaCallback callback`: return interface for asynchronous callback, mtime and error information will be returned through callback
        
        public interface SendMessageLambdaCallback{
            void done(long time, int errorCode, String errorMessage);
        }

return value:       
* **sync**: When the synchronization interface is normal, it returns mtime, and when an error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.

* **async**: The asynchronous interface will not throw an exception. It returns mtime or error information through callback. When errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it is returned as error. You can view the message error information.

### Send group chat messages

    // sync methods
    long sendGroupChat(long fromUid, long groupId, String message, String attrs);
    long sendGroupChat(long fromUid, long groupId, String message, String attrs, int timeoutInseconds);
    
    // async methods
    void sendGroupChat(long fromUid, long groupId, String message, String attrs, SendMessageLambdaCallback callback);
    void sendGroupChat(long fromUid, long groupId, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);
    
Parameter Description:   
* `String attrs`: message attribute information, it is recommended to use a parseable json string, the default is an empty string

* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.

* `SendMessageLambdaCallback callback`: return interface for asynchronous callback, mtime and error information will be returned through callback
        
        public interface SendMessageLambdaCallback{
            void done(long time, int errorCode, String errorMessage);
        }

return value:       
* **sync**: When the synchronization interface is normal, it returns mtime, and when an error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.

* **async**: The asynchronous interface will not throw an exception. It returns mtime or error information through callback. When errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it is returned as error. You can view the message error information.

### Send Group control command

    // sync methods
    long sendGroupCmd(long fromUid, long groupId, String message, String attrs);
    long sendGroupCmd(long fromUid, long groupId, String message, String attrs, int timeoutInseconds);
    
    // async methods
    void sendGroupCmd(long fromUid, long groupId, String message, String attrs, SendMessageLambdaCallback callback);
    void sendGroupCmd(long fromUid, long groupId, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);
    
Parameter Description:   
* `String attrs`: message attribute information, it is recommended to use a parseable json string, the default is an empty string

* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.

* `SendMessageLambdaCallback callback`: return interface for asynchronous callback, mtime and error information will be returned through callback
        
        public interface SendMessageLambdaCallback{
            void done(long time, int errorCode, String errorMessage);
        }

return value:       
* **sync**: When the synchronization interface is normal, it returns mtime, and when an error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.

* **async**: The asynchronous interface will not throw an exception. It returns mtime or error information through callback. When errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it is returned as error. You can view the message error information.

### Send Room chat message

    // sync methods
    long sendRoomChat(long fromUid, long roomId, String message, String attrs);
    long sendRoomChat(long fromUid, long roomId, String message, String attrs, int timeoutInseconds);
    
    // async methods
    void sendRoomChat(long fromUid, long roomId, String message, String attrs, SendMessageLambdaCallback callback);
    void sendRoomChat(long fromUid, long roomId, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);
    
Parameter Description:   
* `String attrs`: message attribute information, it is recommended to use a parseable json string, the default is an empty string

* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.

* `SendMessageLambdaCallback callback`: return interface for asynchronous callback, mtime and error information will be returned through callback
        
        public interface SendMessageLambdaCallback{
            void done(long time, int errorCode, String errorMessage);
        }

return value:       
* **sync**: When the synchronization interface is normal, it returns mtime, and when an error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.

* **async**: The asynchronous interface will not throw an exception. It returns mtime or error information through callback. When errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it is returned as error. You can view the message error information.

### Send Room control commands

    // sync methods
    long sendRoomCmd(long fromUid, long roomId, String message, String attrs);
    long sendRoomCmd(long fromUid, long roomId, String message, String attrs, int timeoutInseconds);
    
    // async methods
    void sendRoomCmd(long fromUid, long roomId, String message, String attrs, SendMessageLambdaCallback callback);
    void sendRoomCmd(long fromUid, long roomId, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);
    
Parameter Description:   
* `String attrs`: message attribute information, it is recommended to use a parseable json string, the default is an empty string

* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.

* `SendMessageLambdaCallback callback`: return interface for asynchronous callback, mtime and error information will be returned through callback
        
        public interface SendMessageLambdaCallback{
            void done(long time, int errorCode, String errorMessage);
        }

return value:       
* **sync**: When the synchronization interface is normal, it returns mtime, and when an error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.

* **async**: The asynchronous interface will not throw an exception. It returns mtime or error information through callback. When errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it is returned as error. You can view the message error information.

### Send Broadcast chat message

    // sync methods
    long sendBroadcastChat(long fromUid, String message, String attrs);
    long sendBroadcastChat(long fromUid, String message, String attrs, int timeoutInseconds);
    
    // async methods
    void sendBroadcastChat(long fromUid, String message, String attrs, SendMessageLambdaCallback callback);
    void sendBroadcastChat(long fromUid, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);
    
Parameter Description:   
* `String attrs`: message attribute information, it is recommended to use a parseable json string, the default is an empty string

* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.

* `SendMessageLambdaCallback callback`: return interface for asynchronous callback, mtime and error information will be returned through callback
        
        public interface SendMessageLambdaCallback{
            void done(long time, int errorCode, String errorMessage);
        }

return value:       

* **sync**: When the synchronization interface is normal, it returns mtime, and when an error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.

* **async**: The asynchronous interface will not throw an exception. It returns mtime or error information through callback. When errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it is returned as error. You can view the message error information.

### Send Broadcast control command

    // sync methods
    long sendBroadcastCmd(long fromUid, String message, String attrs);
    long sendBroadcastCmd(long fromUid, String message, String attrs, int timeoutInseconds);
    
    // async methods
    void sendBroadcastCmd(long fromUid, String message, String attrs, SendMessageLambdaCallback callback);
    void sendBroadcastCmd(long fromUid, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);

Parameter Description:   

* `String attrs`: message attribute information, it is recommended to use a parseable json string, the default is an empty string

* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.

* `SendMessageLambdaCallback callback`: return interface for asynchronous callback, mtime and error information will be returned through callback
        
        public interface SendMessageLambdaCallback{
            void done(long time, int errorCode, String errorMessage);
        }

return value:     
  
* **sync**: The synchronization interface returns mtime when it is normal, and RTMException or other systemic exceptions will be thrown when an error is returned. For RTMException exceptions, you can view the error information through the toString method.

* **async**: The asynchronous interface does not throw exceptions, and returns mtime or error information through callback. When errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it is returned as error. You can view the message error information.
 
    
### Get chat

    // sync methods
    RTMHistoryMessageUnit getP2pChat(long messageId, long fromUid, long toUid);
    RTMHistoryMessageUnit getP2pChat(long messageId, long fromUid, long toUid, int timeInseconds);
    RTMHistoryMessageUnit getGroupChat(long messageId, long fromUid, long groupId);
    RTMHistoryMessageUnit getGroupChat(long messageId, long fromUid, long groupId, int timeInseconds);
    RTMHistoryMessageUnit getRoomChat(long messageId, long fromUid, long roomId);
    RTMHistoryMessageUnit getRoomChat(long messageId, long fromUid, long roomId, int timeInseconds);
    RTMHistoryMessageUnit getBroadcastChat(long messageId, long fromUid);
    RTMHistoryMessageUnit getBroadcastChat(long messageId, long fromUid, int timeInseconds);
    
    // async methods
    void getP2PChat(long messageId, long fromUid, long toUid, GetRetrievedMessageLambdaCallback callback);
    void getP2PChat(long messageId, long fromUid, long toUid, GetRetrievedMessageLambdaCallback callback, int timeInseconds);
    void getGroupChat(long messageId, long fromUid, long groupId, GetRetrievedMessageLambdaCallback callback);
    void getGroupChat(long messageId, long fromUid, long groupId, GetRetrievedMessageLambdaCallback callback, int timeInseconds);
    void getRoomChat(long messageId, long fromUid, long roomId, GetRetrievedMessageLambdaCallback callback);
    void getRoomChat(long messageId, long fromUid, long roomId, GetRetrievedMessageLambdaCallback callback, int timeInseconds);
    void getBroadcastChat(long messageId, long fromUid, GetRetrievedMessageLambdaCallback callback);
    void getBroadcastChat(long messageId, long fromUid, GetRetrievedMessageLambdaCallback callback, int timeInseconds);

Parameter Description:   
       
* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.

* `GetRetrievedMessageLambdaCallback callback`: return interface for asynchronous callback
        
        public interface GetRetrievedMessageLambdaCallback{
            void done(RTMServerClientBase.RTMHistoryMessageUnit result, int errorCode, String errorMessage);
        }
        
        public static class RTMMessage{
            public byte messageType;
            public long toId; // for serverpush
            public long fromUid;
            public long modifiedTime;
            public long messageId;
            public String stringMessage;
            public byte[] binaryMessage;
            public String attrs;
            public FileMsgInfo fileMsgInfo = null;
    
            @Override
            public String toString(){
                return ",[One RTMMessage: mtype =" + messageType + ",fromuid =" + fromUid + ",mtime =" + modifiedTime
                        + ",mid =" + messageId + ",message =" + stringMessage + ",binaryMessage =" + binaryMessage + ",attrs =" + attrs + "]";
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

See: [FileMsgInfo](HistoryChatAPI.md#historical message data unit)

return value:     
  
* **sync**: When the synchronization interface is normal, it returns the HistoryMessage object obtained. When the error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.

* **async**: The asynchronous interface will not throw an exception. The HistoryMessage object or error information obtained through callback is returned. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it is returned as an error. You can view the message error information .


### Delete chat

    // sync methods
    void deleteP2PChat(long messageId, long fromUid, long toUid);
    void deleteP2PChat(long messageId, long fromUid, long toUid, int timeInseconds);
    void deleteGroupChat(long messageId, long fromUid, long groupId);
    void deleteGroupChat(long messageId, long fromUid, long groupId, int timeInseconds);
    void deleteRoomChat(long messageId, long fromUid, long roomId);
    void deleteRoomChat(long messageId, long fromUid, long roomId, int timeInseconds);
    void deleteBroadcastChat(long messageId, long fromUid);
    void deleteBroadcastChat(long messageId, long fromUid, int timeInseconds);
    
    // async methods
    void deleteP2PChat(long messageId, long fromUid, long toUid, DoneLambdaCallback callback);
    void deleteP2PChat(long messageId, long fromUid, long toUid, DoneLambdaCallback callback, int timeInseconds);
    void deleteGroupChat(long messageId, long fromUid, long groupId, DoneLambdaCallback callback);
    void deleteGroupChat(long messageId, long fromUid, long groupId, DoneLambdaCallback callback, int timeInseconds);
    void deleteRoomChat(long messageId, long fromUid, long roomId, DoneLambdaCallback callback);
    void deleteRoomChat(long messageId, long fromUid, long roomId, DoneLambdaCallback callback, int timeInseconds);
    void deleteBroadcastChat(long messageId, long fromUid, DoneLambdaCallback callback);
    void deleteBroadcastChat(long messageId, long fromUid, DoneLambdaCallback callback, int timeInseconds);
    
Parameter Description:   

* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.

* `DoneLambdaCallback callback`: return interface for asynchronous callback, error code and error message will be returned through callback
        
        public interface DoneLambdaCallback {
            void done(int errorCode, String message);
        }

return value:  
     
* **sync**: When the synchronization interface is normal, it returns empty. When the error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.

* **async**: The asynchronous interface will not throw an exception, and the interface call result will be returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it will be returned as an error. You can view the message error information.
   