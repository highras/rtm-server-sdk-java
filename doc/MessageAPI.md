# RTM Server Java SDK Message API Docs

# Index

[TOC]

### -------------------------发送消息接口--------------------------

### 发送 P2P 消息

    // sync methods
    long sendMessage(long fromUid, long toUid, byte mType, String message, String attrs);
    long sendMessage(long fromUid, long toUid, byte mType, String message, String attrs, int timeoutInseconds);
    long sendMessage(long fromUid, long toUid, byte mType, byte[] message, String attrs);
    long sendMessage(long fromUid, long toUid, byte mType, byte[] message, String attrs, int timeoutInseconds);
    
    // async methods
    void sendMessage(long fromUid, long toUid, byte mType, String message, String attrs, SendMessageLambdaCallback callback);
    void sendMessage(long fromUid, long toUid, byte mType, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);
    void sendMessage(long fromUid, long toUid, byte mType, byte[] message, String attrs, SendMessageLambdaCallback callback);
    void sendMessage(long fromUid, long toUid, byte mType, byte[] message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);
    
参数说明：   
* `String attrs`: 消息的属性信息，建议使用可解析的json字符串，默认为空字符串

* `byte mType`: 消息类型 **mType请使用51-127**

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
    long sendMessages(long fromUid, Set<Long> toUids, byte mType, String message, String attrs);
    long sendMessages(long fromUid, Set<Long> toUids, byte mType, String message, String attrs, int timeoutInseconds);
    long sendMessages(long fromUid, Set<Long> toUids, byte mType, byte[] message, String attrs);
    long sendMessages(long fromUid, Set<Long> toUids, byte mType, byte[] message, String attrs, int timeoutInseconds);
    
    // async methods
    void sendMessages(long fromUid, Set<Long> toUids, byte mType, String message, String attrs, SendMessageLambdaCallback callback);
    void sendMessages(long fromUid, Set<Long> toUids, byte mType, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);
    void sendMessages(long fromUid, Set<Long> toUids, byte mType, byte[] message, String attrs, SendMessageLambdaCallback callback);
    void sendMessages(long fromUid, Set<Long> toUids, byte mType, byte[] message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);
    
参数说明：   
* `String attrs`: 消息的属性信息，建议使用可解析的json字符串，默认为空字符串

* `byte mType`: 消息类型 **mType请使用51-127**

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
    long sendGroupMessage(long fromUid, long groupId, byte mType, String message, String attrs);
    long sendGroupMessage(long fromUid, long groupId, byte mType, String message, String attrs, int timeoutInseconds);
    long sendGroupMessage(long fromUid, long groupId, byte mType, byte[] message, String attrs);
    long sendGroupMessage(long fromUid, long groupId, byte mType, byte[] message, String attrs, int timeoutInseconds);
    
    // async methods
    void sendGroupMessage(long fromUid, long groupId, byte mType, String message, String attrs, SendMessageLambdaCallback callback);
    void sendGroupMessage(long fromUid, long groupId, byte mType, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);
    void sendGroupMessage(long fromUid, long groupId, byte mType, byte[] message, String attrs, SendMessageLambdaCallback callback);
    void sendGroupMessage(long fromUid, long groupId, byte mType, byte[] message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);
    
参数说明：   
* `String attrs`: 消息的属性信息，建议使用可解析的json字符串，默认为空字符串

* `byte mType`: 消息类型 **mType请使用51-127**

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
    long sendRoomMessage(long fromUid, long roomId, byte mType, String message, String attrs);
    long sendRoomMessage(long fromUid, long roomId, byte mType, String message, String attrs, int timeoutInseconds);
    long sendRoomMessage(long fromUid, long roomId, byte mType, byte[] message, String attrs);
    long sendRoomMessage(long fromUid, long roomId, byte mType, byte[] message, String attrs, int timeoutInseconds);
    
    // async methods
    void sendRoomMessage(long fromUid, long roomId, byte mType, String message, String attrs, SendMessageLambdaCallback callback);
    void sendRoomMessage(long fromUid, long roomId, byte mType, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);
    void sendRoomMessage(long fromUid, long roomId, byte mType, byte[] message, String attrs, SendMessageLambdaCallback callback);
    void sendRoomMessage(long fromUid, long roomId, byte mType, byte[] message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);
    
参数说明：   
* `String attrs`: 消息的属性信息，建议使用可解析的json字符串，默认为空字符串

* `byte mType`: 消息类型 **mType请使用51-127**

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
    long sendBroadcastMessage(long fromUid, byte mType, String message, String attrs);
    long sendBroadcastMessage(long fromUid, byte mType, String message, String attrs, int timeoutInseconds);
    long sendBroadcastMessage(long fromUid, byte mType, byte[] message, String attrs);
    long sendBroadcastMessage(long fromUid, byte mType, byte[] message, String attrs, int timeoutInseconds);
    
    // async methods
    void sendBroadcastMessage(long fromUid, byte mType, String message, String attrs, SendMessageLambdaCallback callback);
    void sendBroadcastMessage(long fromUid, byte mType, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);
    void sendBroadcastMessage(long fromUid, byte mType, byte[] message, String attrs, SendMessageLambdaCallback callback);
    void sendBroadcastMessage(long fromUid, byte mType, byte[] message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);
 
参数说明：   
* `String attrs`: 消息的属性信息，建议使用可解析的json字符串，默认为空字符串

* `byte mType`: 消息类型 **mType请使用51-127**

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `SendMessageLambdaCallback callback`: 为异步回调返回接口, mtime以及 error信息将通过callback返回
        
        public interface SendMessageLambdaCallback{
            void done(long time, int errorCode, String errorMessage);
        }

返回值:       
* **sync**: 同步接口正常时返回mtime，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回mtime，或者error信息, 当errorCode不等于ErrorCode.FPNN_EC_OK.value(),则为error返回，可查看message错误信息.    

### 消息的类型

    enum MessageType{
        MESSAGE_TYPE_P2P((byte)1),
        MESSAGE_TYPE_ROOM((byte)2),
        MESSAGE_TYPE_GROUP((byte)3),
        MESSAGE_TYPE_BROADCAST((byte)4);
    
        private final int value;
        MessageType(byte type) {value = type;}
    
        public int value() {
            return value;
        }
    }
    
### 获取消息

    // sync methods
    RTMRetrievedMessage getMsg(long mid, long from, long xid, MessageType type);
    RTMRetrievedMessage getMsg(long mid, long from, long xid, MessageType type, int timeoutInseconds);
    
    // async methods
    void getMsg(long mid, long from, long xid, MessageType type, GetRetrievedMessageLambdaCallback callback);
    void getMsg(long mid, long from, long xid, MessageType type, GetRetrievedMessageLambdaCallback callback, int timeoutInseconds);
    
参数说明:   

* `MessageType type`: 当type为MessageType.MESSAGE_TYPE_P2P时, xid为接收方uid, type为MessageType.MESSAGE_TYPE_ROOM, xid为房间rid, 
type为MessageType.MESSAGE_TYPE_GROUP时, xid为群组gid, type为MessageType.MESSAGE_TYPE_BROADCAST时, xid为0
            
* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `GetRetrievedMessageLambdaCallback callback`: 为异步回调返回接口
        
        public interface GetRetrievedMessageLambdaCallback{
            void done(RTMServerClientBase.RTMRetrievedMessage result, int errorCode, String errorMessage);
        }
        
        public static class RTMRetrievedMessage {
            public byte mtype;
            public long mtime;
            public long id;
            public String stringMessage;
            public byte[] binaryMessage;
            public String attrs;
    
            @Override
            public String toString(){
                return "[RTMRetrievedMessage] message id = " + id + " ,mtype = " + mtype + " ,msg = " + stringMessage + " ,binary message " + binaryMessage
                        + " ,mtime = " + mtime + " ,attrs = " + attrs;
            }
        }

返回值:     
  
* **sync**: 同步接口正常时返回获取到的RTMRetrievedMessage对象，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回获取到的RTMRetrievedMessage对象，或者error信息, 当errorCode不等于ErrorCode.FPNN_EC_OK.value(),则为error返回，可查看message错误信息.  

### 删除消息

    // sync methods
    void deleteMsg(long mid, long from, long xid, MessageType type);
    void deleteMsg(long mid, long from, long xid, MessageType type, int timeoutInseconds);
    
    // async methods
    void deleteMsg(long mid, long from, long xid, MessageType type, DoneLambdaCallback callback);
    void deleteMsg(long mid, long from, long xid, MessageType type, DoneLambdaCallback callback, int timeoutInseconds);
    
参数说明:   

* `MessageType type`: 当type为MessageType.MESSAGE_TYPE_P2P时, xid为接收方uid, type为MessageType.MESSAGE_TYPE_ROOM, xid为房间rid, 
type为MessageType.MESSAGE_TYPE_GROUP时, xid为群组gid, type为MessageType.MESSAGE_TYPE_BROADCAST时, xid为0

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `DoneLambdaCallback callback`: 为异步回调返回接口, 错误码和错误信息将通过callback返回
        
        public interface DoneLambdaCallback {
            void done(int errorCode, String message);
        }

返回值:  
     
* **sync**: 同步接口正常时返回空，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回接口调用结果，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息.          