# RTM Server Java SDK Message API Docs

# Index

[TOC]

### -------------------------发送消息接口--------------------------

### 发送 P2P 消息

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
    
参数说明：   
* `String attrs`: 消息的属性信息，建议使用可解析的json字符串，默认为空字符串

* `byte messageType`: 消息类型 **messageType请使用51-127**

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `SendMessageLambdaCallback callback`: 为异步回调返回接口, mtime以及 error信息将通过callback返回
        
        public interface SendMessageLambdaCallback{
            void done(long time, int errorCode, String errorMessage);
        }

返回值:       
* **sync**: 同步接口正常时返回mtime，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回mtime，或者error信息, 当errorCode不等于ErrorCode.FPNN_EC_OK.value(),则为error返回，可查看message错误信息.

### 发送多人 P2P 消息

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
    
参数说明：   
* `String attrs`: 消息的属性信息，建议使用可解析的json字符串，默认为空字符串

* `byte messageType`: 消息类型 **messageType请使用51-127**

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `SendMessageLambdaCallback callback`: 为异步回调返回接口, mtime以及 error信息将通过callback返回
        
        public interface SendMessageLambdaCallback{
            void done(long time, int errorCode, String errorMessage);
        }

返回值:       
* **sync**: 同步接口正常时返回mtime，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回mtime，或者error信息, 当errorCode不等于ErrorCode.FPNN_EC_OK.value(),则为error返回，可查看message错误信息.

### 发送 Group 消息

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
    
参数说明：   
* `String attrs`: 消息的属性信息，建议使用可解析的json字符串，默认为空字符串

* `byte messageType`: 消息类型 **messageType请使用51-127**

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `SendMessageLambdaCallback callback`: 为异步回调返回接口, mtime以及 error信息将通过callback返回
        
        public interface SendMessageLambdaCallback{
            void done(long time, int errorCode, String errorMessage);
        }

返回值:       
* **sync**: 同步接口正常时返回mtime，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回mtime，或者error信息, 当errorCode不等于ErrorCode.FPNN_EC_OK.value(),则为error返回，可查看message错误信息.        

### 发送 Room 消息

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
    
参数说明：   
* `String attrs`: 消息的属性信息，建议使用可解析的json字符串，默认为空字符串

* `byte messageType`: 消息类型 **messageType请使用51-127**

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `SendMessageLambdaCallback callback`: 为异步回调返回接口, mtime以及 error信息将通过callback返回
        
        public interface SendMessageLambdaCallback{
            void done(long time, int errorCode, String errorMessage);
        }

返回值:       
* **sync**: 同步接口正常时返回mtime，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回mtime，或者error信息, 当errorCode不等于ErrorCode.FPNN_EC_OK.value(),则为error返回，可查看message错误信息. 

### 发送 Broadcast 消息

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
 
参数说明：   
* `String attrs`: 消息的属性信息，建议使用可解析的json字符串，默认为空字符串

* `byte messageType`: 消息类型 **messageType请使用51-127**

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `SendMessageLambdaCallback callback`: 为异步回调返回接口, mtime以及 error信息将通过callback返回
        
        public interface SendMessageLambdaCallback{
            void done(long time, int errorCode, String errorMessage);
        }

返回值:       
* **sync**: 同步接口正常时返回mtime，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回mtime，或者error信息, 当errorCode不等于ErrorCode.FPNN_EC_OK.value(),则为error返回，可查看message错误信息.    
    
### 获取消息

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
    
    
参数说明:   
         
* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `GetRetrievedMessageLambdaCallback callback`: 为异步回调返回接口
        
        public interface GetRetrievedMessageLambdaCallback{
            void done(RTMServerClientBase.HistoryMessage result, int errorCode, String errorMessage);
        }
        
        public static class RTMMessage{
            public byte messageType;
            public long toId;     // for serverpush
            public long fromUid;
            public long modifiedTime;
            public long messageId;
            public String stringMessage;
            public byte[] binaryMessage;
            public String attrs;
            public FileMsgInfo fileMsgInfo = null;
    
            @Override
            public String toString(){
                return " ,[One RTMMessage: mtype = " + messageType + " ,fromuid = " + fromUid + " ,mtime = " + modifiedTime
                        + " ,mid = " + messageId + " ,message = " + stringMessage + " ,binaryMessage = " + binaryMessage + " ,attrs = " + attrs + "]";
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

参见: [FileMsgInfo](HistoryMessageAPI.md#历史消息数据单元)  

返回值:     
  
* **sync**: 同步接口正常时返回获取到的HistoryMessage对象，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回获取到的HistoryMessage对象，或者error信息, 当errorCode不等于ErrorCode.FPNN_EC_OK.value(),则为error返回，可查看message错误信息.  

### 删除消息

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
    
参数说明:   

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `DoneLambdaCallback callback`: 为异步回调返回接口, 错误码和错误信息将通过callback返回
        
        public interface DoneLambdaCallback {
            void done(int errorCode, String message);
        }

返回值:  
     
* **sync**: 同步接口正常时返回空，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回接口调用结果，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息.    

### 获取房间或者群组内发送消息的统计

Note：
    只有被保存的消息才会进行统计，目前聊天消息默认保存，外加用户配置的消息类型会保存。 

    // sync method
    RTMMessageCount getMsgCount(MessageType type, long xid, Set<Long> mtypes, long begin, long end)；
    RTMMessageCount getMsgCount(MessageType type, long xid, Set<Long> mtypes, long begin, long end, int timeoutInseconds)；
    
    // async method 
    void getMsgCount(MessageType type, long xid, Set<Long> mtypes, long begin, long end, GetMssageCountLambdaCallback callback);
    void getMsgCount(MessageType type, long xid, Set<Long> mtypes, long begin, long end,  GetMssageCountLambdaCallback callback, int timeoutInseconds);
    
参数说明：

* `MessageType type`: 获取消息的类别，**可接受MessageType.MESSAGE_TYPE_GROUP 和 MessageType.MESSAGE_TYPE_ROOM**

*  `long xid`: 当type等于MessageType.MESSAGE_TYPE_GROUP， **xid为groupId**， 当type等于MessageType.MESSAGE_TYPE_ROOM，**xid为roomId**

* `Set<Long> mtypes`: 如果mtypes为null或者为空时，则返回所有

* `long begin`: 毫秒级时间戳，开始时间，为0则忽略时间

* `long end`: 毫秒级时间戳，结束时间，为0则忽略时间

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `GetMssageCountLambdaCallback callback`: 为异步回调返回接口, 结果和错误码以及错误信息将通过callback返回
        
        public interface DoneLambdaCallback {
            void done(RTMMessageCount result, int errorCode, String errorMessage);
        }

返回值:  

        public static class RTMMessageCount {
            public int sender;
            public int count;
        }
     
* **sync**: 同步接口正常时返回RTMMessageCount对象， 成员sender为发送消息的人数(去重的)，成员count为消息的数量；错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回接口调用结果RTMMessageCount对象， 成员sender为发送消息的人数(去重的)，成员count为消息的数量；当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息. 


