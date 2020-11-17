# RTM Server Java SDK Message API Docs

# Index

[TOC]

### -------------------------Send message interface------------------- -------

### Send P2P message

    // sync methods
    long sendMessage(long fromUid, long toUid, byte messageType, String message, String attrs);
    long sendMessage(long fromUid, long toUid, byte messageType, String message, String attrs, int timeoutInseconds);
    long sendMessage(long fromUid, long toUid, byte messageType, byte[] message, String attrs);
    long sendMessage(long fromUid, long toUid, byte messageType, byte[] message, String attrs, int timeoutInseconds);
    
    // async methods
    void sendMessage(long fromUid, long toUid, byte messageType, String message, String attrs, SendMessageLambdaCallback callback);
    void sendMessage(long fromUid, long toUid, byte messageType, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);
    void sendMessage(long fromUid, long toUid, byte messageType, byte[] message, String attrs, SendMessageLambdaCallback callback);
    void sendMessage(long fromUid, long toUid, byte messageType, byte[] message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);
    
Parameter Description:   
* `String attrs`: message attribute information, it is recommended to use a parseable json string, the default is an empty string

* `byte messageType`: message type **messageType please use 51-127**

* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.

* `SendMessageLambdaCallback callback`: return interface for asynchronous callback, mtime and error information will be returned through callback
        
        public interface SendMessageLambdaCallback{
            void done(long time, int errorCode, String errorMessage);
        }

return value:       
* **sync**: The synchronization interface returns mtime when it is normal, and RTMException or other systemic exceptions will be thrown when an error is returned. For RTMException exceptions, you can view the error information through the toString method.

* **async**: The asynchronous interface will not throw an exception. It returns mtime or error information through callback. When errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it is returned as error. You can view the message error information.

### Send multi-person P2P messages

    // sync methods
    long sendMessages(long fromUid, Set<Long> toUids, byte messageType, String message, String attrs);
    long sendMessages(long fromUid, Set<Long> toUids, byte messageType, String message, String attrs, int timeoutInseconds);
    long sendMessages(long fromUid, Set<Long> toUids, byte messageType, byte[] message, String attrs);
    long sendMessages(long fromUid, Set<Long> toUids, byte messageType, byte[] message, String attrs, int timeoutInseconds);
    
    // async methods
    void sendMessages(long fromUid, Set<Long> toUids, byte messageType, String message, String attrs, SendMessageLambdaCallback callback);
    void sendMessages(long fromUid, Set<Long> toUids, byte messageType, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);
    void sendMessages(long fromUid, Set<Long> toUids, byte messageType, byte[] message, String attrs, SendMessageLambdaCallback callback);
    void sendMessages(long fromUid, Set<Long> toUids, byte messageType, byte[] message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);
    
Parameter Description:   
* `String attrs`: message attribute information, it is recommended to use a parseable json string, the default is an empty string

* `byte messageType`: message type **messageType please use 51-127**

* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.

* `SendMessageLambdaCallback callback`: return interface for asynchronous callback, mtime and error information will be returned through callback
        
        public interface SendMessageLambdaCallback{
            void done(long time, int errorCode, String errorMessage);
        }

return value:       
* **sync**: The synchronization interface returns mtime when it is normal, and RTMException or other systemic exceptions will be thrown when an error is returned. For RTMException exceptions, you can view the error information through the toString method.

* **async**: The asynchronous interface will not throw an exception. It returns mtime or error information through callback. When errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it is returned as error. You can view the message error information.

### Send Group message

    // sync methods
    long sendGroupMessage(long fromUid, long groupId, byte messageType, String message, String attrs);
    long sendGroupMessage(long fromUid, long groupId, byte messageType, String message, String attrs, int timeoutInseconds);
    long sendGroupMessage(long fromUid, long groupId, byte messageType, byte[] message, String attrs);
    long sendGroupMessage(long fromUid, long groupId, byte messageType, byte[] message, String attrs, int timeoutInseconds);
    
    // async methods
    void sendGroupMessage(long fromUid, long groupId, byte messageType, String message, String attrs, SendMessageLambdaCallback callback);
    void sendGroupMessage(long fromUid, long groupId, byte messageType, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);
    void sendGroupMessage(long fromUid, long groupId, byte messageType, byte[] message, String attrs, SendMessageLambdaCallback callback);
    void sendGroupMessage(long fromUid, long groupId, byte messageType, byte[] message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);
    
Parameter Description:   
* `String attrs`: message attribute information, it is recommended to use a parseable json string, the default is an empty string

* `byte messageType`: message type **messageType please use 51-127**

* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.

* `SendMessageLambdaCallback callback`: return interface for asynchronous callback, mtime and error information will be returned through callback
        
        public interface SendMessageLambdaCallback{
            void done(long time, int errorCode, String errorMessage);
        }

return value:       
* **sync**: The synchronization interface returns mtime when it is normal, and RTMException or other systemic exceptions will be thrown when an error is returned. For RTMException exceptions, you can view the error information through the toString method.

* **async**: The asynchronous interface will not throw an exception. It returns mtime or error information through callback. When errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it is returned as error. You can view the message error information.

### Send Room message

    // sync methods
    long sendRoomMessage(long fromUid, long roomId, byte messageType, String message, String attrs);
    long sendRoomMessage(long fromUid, long roomId, byte messageType, String message, String attrs, int timeoutInseconds);
    long sendRoomMessage(long fromUid, long roomId, byte messageType, byte[] message, String attrs);
    long sendRoomMessage(long fromUid, long roomId, byte messageType, byte[] message, String attrs, int timeoutInseconds);
    
    // async methods
    void sendRoomMessage(long fromUid, long roomId, byte messageType, String message, String attrs, SendMessageLambdaCallback callback);
    void sendRoomMessage(long fromUid, long roomId, byte messageType, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);
    void sendRoomMessage(long fromUid, long roomId, byte messageType, byte[] message, String attrs, SendMessageLambdaCallback callback);
    void sendRoomMessage(long fromUid, long roomId, byte messageType, byte[] message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);
    
Parameter Description:   
* `String attrs`: message attribute information, it is recommended to use a parseable json string, the default is an empty string

* `byte messageType`: message type **messageType please use 51-127**

* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.

* `SendMessageLambdaCallback callback`: return interface for asynchronous callback, mtime and error information will be returned through callback
        
        public interface SendMessageLambdaCallback{
            void done(long time, int errorCode, String errorMessage);
        }

return value:       
* **sync**: The synchronization interface returns mtime when it is normal, and RTMException or other systemic exceptions will be thrown when an error is returned. For RTMException exceptions, you can view the error information through the toString method.

* **async**: The asynchronous interface will not throw an exception. It returns mtime or error information through callback. When errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it is returned as error. You can view the message error information.

### Send Broadcast message

    // sync methods
    long sendBroadcastMessage(long fromUid, byte messageType, String message, String attrs);
    long sendBroadcastMessage(long fromUid, byte messageType, String message, String attrs, int timeoutInseconds);
    long sendBroadcastMessage(long fromUid, byte messageType, byte[] message, String attrs);
    long sendBroadcastMessage(long fromUid, byte messageType, byte[] message, String attrs, int timeoutInseconds);
    
    // async methods
    void sendBroadcastMessage(long fromUid, byte messageType, String message, String attrs, SendMessageLambdaCallback callback);
    void sendBroadcastMessage(long fromUid, byte messageType, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);
    void sendBroadcastMessage(long fromUid, byte messageType, byte[] message, String attrs, SendMessageLambdaCallback callback);
    void sendBroadcastMessage(long fromUid, byte messageType, byte[] message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);
 
Parameter Description:   
* `String attrs`: message attribute information, it is recommended to use a parseable json string, the default is an empty string

* `byte messageType`: message type **messageType please use 51-127**

* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.

* `SendMessageLambdaCallback callback`: return interface for asynchronous callback, mtime and error information will be returned through callback
        
        public interface SendMessageLambdaCallback{
            void done(long time, int errorCode, String errorMessage);
        }

return value:       
* **sync**: The synchronization interface returns mtime when it is normal, and RTMException or other systemic exceptions will be thrown when an error is returned. For RTMException exceptions, you can view the error information through the toString method.

* **async**: The asynchronous interface will not throw an exception. It returns mtime or error information through callback. When errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it is returned as error. You can view the message error information.
    
### Get news

    // sync methods
    RTMHistoryMessageUnit getP2pMsg(long messageId, long fromUid, long toUid);
    RTMHistoryMessageUnit getP2pMsg(long messageId, long fromUid, long toUid, int timeInseconds);
    RTMHistoryMessageUnit getGroupMsg(long messageId, long fromUid, long groupId);
    RTMHistoryMessageUnit getGroupMsg(long messageId, long fromUid, long groupId, int timeInseconds);
    RTMHistoryMessageUnit getRoomMsg(long messageId, long fromUid, long roomId);
    RTMHistoryMessageUnit getRoomMsg(long messageId, long fromUid, long roomId, int timeInseconds);
    RTMHistoryMessageUnit getBroadcastMsg(long messageId, long fromUid);
    RTMHistoryMessageUnit getBroadcastMsg(long messageId, long fromUid, int timeInseconds);
    
    // async methods
    void getP2PMsg(long messageId, long fromUid, long toUid, GetRetrievedMessageLambdaCallback callback);
    void getP2PMsg(long messageId, long fromUid, long toUid, GetRetrievedMessageLambdaCallback callback, int timeInseconds);
    void getGroupMsg(long messageId, long fromUid, long groupId, GetRetrievedMessageLambdaCallback callback);
    void getGroupMsg(long messageId, long fromUid, long groupId, GetRetrievedMessageLambdaCallback callback, int timeInseconds);
    void getRoomMsg(long messageId, long fromUid, long roomId, GetRetrievedMessageLambdaCallback callback);
    void getRoomMsg(long messageId, long fromUid, long roomId, GetRetrievedMessageLambdaCallback callback, int timeInseconds);
    void getBroadcastMsg(long messageId, long fromUid, GetRetrievedMessageLambdaCallback callback);
    void getBroadcastMsg(long messageId, long fromUid, GetRetrievedMessageLambdaCallback callback, int timeInseconds);
    
    
Parameter Description:   
         
* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.

* `GetRetrievedMessageLambdaCallback callback`: return interface for asynchronous callback
        
        public interface GetRetrievedMessageLambdaCallback{
            void done(RTMServerClientBase.HistoryMessage result, int errorCode, String errorMessage);
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

See: [FileMsgInfo](HistoryMessageAPI.md#historical message data unit)

return value:     
  
* **sync**: When the synchronization interface is normal, it returns the HistoryMessage object obtained. When the error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.

* **async**: The asynchronous interface will not throw an exception. The HistoryMessage object or error information obtained through callback is returned. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it is returned as an error. You can view the message error information .

### Delete message

    // sync methods
    void deleteP2PMsg(long messageId, long fromUid, long toUid);
    void deleteP2PMsg(long messageId, long fromUid, long toUid, int timeInseconds);
    void deleteGroupMsg(long messageId, long fromUid, long groupId);
    void deleteGroupMsg(long messageId, long fromUid, long groupId, int timeInseconds);
    void deleteRoomMsg(long messageId, long fromUid, long roomId);
    void deleteRoomMsg(long messageId, long fromUid, long roomId, int timeInseconds);
    void deleteBroadcastMsg(long messageId, long fromUid);
    void deleteBroadcastMsg(long messageId, long fromUid, int timeInseconds);
    
    // async methods
    void deleteP2PMsg(long messageId, long fromUid, long toUid, DoneLambdaCallback callback);
    void deleteP2PMsg(long messageId, long fromUid, long toUid, DoneLambdaCallback callback, int timeInseconds);
    void deleteGroupMsg(long messageId, long fromUid, long groupId, DoneLambdaCallback callback);
    void deleteGroupMsg(long messageId, long fromUid, long groupId, DoneLambdaCallback callback, int timeInseconds);
    void deleteRoomMsg(long messageId, long fromUid, long roomId, DoneLambdaCallback callback);
    void deleteRoomMsg(long messageId, long fromUid, long roomId, DoneLambdaCallback callback, int timeInseconds);
    void deleteBroadcastMsg(long messageId, long fromUid, DoneLambdaCallback callback);
    void deleteBroadcastMsg(long messageId, long fromUid, DoneLambdaCallback callback, int timeInseconds);
    
Parameter Description:   

* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.

* `DoneLambdaCallback callback`: return interface for asynchronous callback, error code and error message will be returned through callback
        
        public interface DoneLambdaCallback {
            void done(int errorCode, String message);
        }

return value:  
     
* **sync**: When the synchronization interface is normal, it returns empty. When the error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.

* **async**: The asynchronous interface will not throw an exception, and the interface call result will be returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it will be returned as an error. You can view the message error information.