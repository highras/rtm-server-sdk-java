# RTM Server Java SDK Chat API Docs

# Index

[TOC]

### -------------------------发送聊天信息接口--------------------------

### 发送 P2P 聊天消息

    // sync methods
    long sendChat(long fromUid, long toUid, String message, String attrs);
    long sendChat(long fromUid, long toUid, String message, String attrs, int timeoutInseconds);
    
    // async methods
    void sendChat(long fromUid, long toUid, String message, String attrs, SendMessageLambdaCallback callback);
    void sendChat(long fromUid, long toUid, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);
    
参数说明：   
* `String attrs`: 消息的属性信息，建议使用可解析的json字符串，默认为空字符串

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `SendMessageLambdaCallback callback`: 为异步回调返回接口, mtime以及 error信息将通过callback返回
        
        public interface SendMessageLambdaCallback{
            void done(long time, int errorCode, String errorMessage);
        }

返回值:       
* **sync**: 同步接口正常时返回mtime，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回mtime，或者error信息, 当errorCode不等于ErrorCode.FPNN_EC_OK.value(),则为error返回，可查看message错误信息.
   
### 发送 P2P 语音消息

    // sync methods
    long sendAudio(long fromUid, long toUid, byte[] message, String attrs);
    long sendAudio(long fromUid, long toUid, byte[] message, String attrs, int timeoutInseconds);
    
    // async methods
    void sendAudio(long fromUid, long toUid, byte[] message, String attrs, SendMessageLambdaCallback callback);
    void sendAudio(long fromUid, long toUid, byte[] message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);
    
参数说明：   
* `String attrs`: 消息的属性信息，建议使用可解析的json字符串，默认为空字符串

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `SendMessageLambdaCallback callback`: 为异步回调返回接口, mtime以及 error信息将通过callback返回
        
        public interface SendMessageLambdaCallback{
            void done(long time, int errorCode, String errorMessage);
        }

返回值:       
* **sync**: 同步接口正常时返回mtime，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回mtime，或者error信息, 当errorCode不等于ErrorCode.FPNN_EC_OK.value(),则为error返回，可查看message错误信息.   
    
### 发送 P2P 控制命令

    // sync methods
    long sendCmd(long fromUid, long toUid, String message, String attrs);
    long sendCmd(long fromUid, long toUid, String message, String attrs, int timeoutInseconds);
    
    // async methods
    void sendCmd(long fromUid, long toUid, String message, String attrs, SendMessageLambdaCallback callback);
    void sendCmd(long fromUid, long toUid, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);
 
参数说明：   
* `String attrs`: 消息的属性信息，建议使用可解析的json字符串，默认为空字符串

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `SendMessageLambdaCallback callback`: 为异步回调返回接口, mtime以及 error信息将通过callback返回
        
        public interface SendMessageLambdaCallback{
            void done(long time, int errorCode, String errorMessage);
        }

返回值:       
* **sync**: 同步接口正常时返回mtime，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回mtime，或者error信息, 当errorCode不等于ErrorCode.FPNN_EC_OK.value(),则为error返回，可查看message错误信息.

### 发送多人 P2P 聊天消息

    // sync methods
    long sendChats(long fromUid, Set<Long> toUids, String message, String attrs);
    long sendChats(long fromUid, Set<Long> toUids, String message, String attrs, int timeoutInseconds);
    
    // async methods
    void sendChats(long fromUid, Set<Long> toUids, String message, String attrs, SendMessageLambdaCallback callback);
    void sendChats(long fromUid, Set<Long> toUids, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);
    
参数说明：   
* `String attrs`: 消息的属性信息，建议使用可解析的json字符串，默认为空字符串

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `SendMessageLambdaCallback callback`: 为异步回调返回接口, mtime以及 error信息将通过callback返回
        
        public interface SendMessageLambdaCallback{
            void done(long time, int errorCode, String errorMessage);
        }

返回值:       
* **sync**: 同步接口正常时返回mtime，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回mtime，或者error信息, 当errorCode不等于ErrorCode.FPNN_EC_OK.value(),则为error返回，可查看message错误信息.

### 发送多人 P2P 语音消息

    // sync methods
    long sendAudios(long fromUid, Set<Long> toUids, byte[] message, String attrs);
    long sendAudios(long fromUid, Set<Long> toUids, byte[] message, String attrs, int timeoutInseconds);
    
    // async methods
    void sendAudios(long fromUid, Set<Long> toUids, byte[] message, String attrs, SendMessageLambdaCallback callback);
    void sendAudios(long fromUid, Set<Long> toUids, byte[] message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);

参数说明：   
* `String attrs`: 消息的属性信息，建议使用可解析的json字符串，默认为空字符串

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `SendMessageLambdaCallback callback`: 为异步回调返回接口, mtime以及 error信息将通过callback返回
        
        public interface SendMessageLambdaCallback{
            void done(long time, int errorCode, String errorMessage);
        }

返回值:       
* **sync**: 同步接口正常时返回mtime，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回mtime，或者error信息, 当errorCode不等于ErrorCode.FPNN_EC_OK.value(),则为error返回，可查看message错误信息.

### 发送多人 P2P 控制命令

    // sync methods
    long sendCmds(long fromUid, Set<Long> toUids, String message, String attrs);
    long sendCmds(long fromUid, Set<Long> toUids, String message, String attrs, int timeoutInseconds);
    
    // async methods
    void sendCmds(long fromUid, Set<Long> toUids, String message, String attrs, SendMessageLambdaCallback callback);
    void sendCmds(long fromUid, Set<Long> toUids, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);
    
参数说明：   
* `String attrs`: 消息的属性信息，建议使用可解析的json字符串，默认为空字符串

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `SendMessageLambdaCallback callback`: 为异步回调返回接口, mtime以及 error信息将通过callback返回
        
        public interface SendMessageLambdaCallback{
            void done(long time, int errorCode, String errorMessage);
        }

返回值:       
* **sync**: 同步接口正常时返回mtime，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回mtime，或者error信息, 当errorCode不等于ErrorCode.FPNN_EC_OK.value(),则为error返回，可查看message错误信息.

### 发送 Group 聊天消息

    // sync methods
    long sendGroupChat(long fromUid, long groupId, String message, String attrs);
    long sendGroupChat(long fromUid, long groupId, String message, String attrs, int timeoutInseconds);
    
    // async methods
    void sendGroupChat(long fromUid, long groupId, String message, String attrs, SendMessageLambdaCallback callback);
    void sendGroupChat(long fromUid, long groupId, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);
    
参数说明：   
* `String attrs`: 消息的属性信息，建议使用可解析的json字符串，默认为空字符串

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `SendMessageLambdaCallback callback`: 为异步回调返回接口, mtime以及 error信息将通过callback返回
        
        public interface SendMessageLambdaCallback{
            void done(long time, int errorCode, String errorMessage);
        }

返回值:       
* **sync**: 同步接口正常时返回mtime，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回mtime，或者error信息, 当errorCode不等于ErrorCode.FPNN_EC_OK.value(),则为error返回，可查看message错误信息.

### 发送 Group 语音消息

    // sync methods
    long sendGroupAudio(long fromUid, long groupId, byte[] message, String attrs);
    long sendGroupAudio(long fromUid, long groupId, byte[] message, String attrs, int timeoutInseconds);
    
    // async methods
    void sendGroupAudio(long fromUid, long groupId, byte[] message, String attrs, SendMessageLambdaCallback callback);
    void sendGroupAudio(long fromUid, long groupId, byte[] message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);

参数说明：   
* `String attrs`: 消息的属性信息，建议使用可解析的json字符串，默认为空字符串

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `SendMessageLambdaCallback callback`: 为异步回调返回接口, mtime以及 error信息将通过callback返回
        
        public interface SendMessageLambdaCallback{
            void done(long time, int errorCode, String errorMessage);
        }

返回值:       
* **sync**: 同步接口正常时返回mtime，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回mtime，或者error信息, 当errorCode不等于ErrorCode.FPNN_EC_OK.value(),则为error返回，可查看message错误信息.

### 发送 Group 控制命令

    // sync methods
    long sendGroupCmd(long fromUid, long groupId, String message, String attrs);
    long sendGroupCmd(long fromUid, long groupId, String message, String attrs, int timeoutInseconds);
    
    // async methods
    void sendGroupCmd(long fromUid, long groupId, String message, String attrs, SendMessageLambdaCallback callback);
    void sendGroupCmd(long fromUid, long groupId, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);
    
参数说明：   
* `String attrs`: 消息的属性信息，建议使用可解析的json字符串，默认为空字符串

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `SendMessageLambdaCallback callback`: 为异步回调返回接口, mtime以及 error信息将通过callback返回
        
        public interface SendMessageLambdaCallback{
            void done(long time, int errorCode, String errorMessage);
        }

返回值:       
* **sync**: 同步接口正常时返回mtime，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回mtime，或者error信息, 当errorCode不等于ErrorCode.FPNN_EC_OK.value(),则为error返回，可查看message错误信息.

### 发送 Room 聊天消息

    // sync methods
    long sendRoomChat(long fromUid, long roomId, String message, String attrs);
    long sendRoomChat(long fromUid, long roomId, String message, String attrs, int timeoutInseconds);
    
    // async methods
    void sendRoomChat(long fromUid, long roomId, String message, String attrs, SendMessageLambdaCallback callback);
    void sendRoomChat(long fromUid, long roomId, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);
    
参数说明：   
* `String attrs`: 消息的属性信息，建议使用可解析的json字符串，默认为空字符串

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `SendMessageLambdaCallback callback`: 为异步回调返回接口, mtime以及 error信息将通过callback返回
        
        public interface SendMessageLambdaCallback{
            void done(long time, int errorCode, String errorMessage);
        }

返回值:       
* **sync**: 同步接口正常时返回mtime，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回mtime，或者error信息, 当errorCode不等于ErrorCode.FPNN_EC_OK.value(),则为error返回，可查看message错误信息.

### 发送 Room 语音消息

    // sync methods
    long sendRoomAudio(long fromUid, long roomId, byte[] message, String attrs);
    long sendRoomAudio(long fromUid, long roomId, byte[] message, String attrs, int timeoutInseconds);
    
    // async methods
    void sendRoomAudio(long fromUid, long roomId, byte[] message, String attrs, SendMessageLambdaCallback callback);
    void sendRoomAudio(long fromUid, long roomId,  byte[] message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);
    
参数说明：   
* `String attrs`: 消息的属性信息，建议使用可解析的json字符串，默认为空字符串

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `SendMessageLambdaCallback callback`: 为异步回调返回接口, mtime以及 error信息将通过callback返回
        
        public interface SendMessageLambdaCallback{
            void done(long time, int errorCode, String errorMessage);
        }

返回值:       
* **sync**: 同步接口正常时返回mtime，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回mtime，或者error信息, 当errorCode不等于ErrorCode.FPNN_EC_OK.value(),则为error返回，可查看message错误信息.

### 发送 Room 控制命令

    // sync methods
    long sendRoomCmd(long fromUid, long roomId,  String message, String attrs);
    long sendRoomCmd(long fromUid, long roomId, String message, String attrs, int timeoutInseconds);
    
    // async methods
    void sendRoomCmd(long fromUid, long roomId, String message, String attrs, SendMessageLambdaCallback callback);
    void sendRoomCmd(long fromUid, long roomId, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);
    
参数说明：   
* `String attrs`: 消息的属性信息，建议使用可解析的json字符串，默认为空字符串

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `SendMessageLambdaCallback callback`: 为异步回调返回接口, mtime以及 error信息将通过callback返回
        
        public interface SendMessageLambdaCallback{
            void done(long time, int errorCode, String errorMessage);
        }

返回值:       
* **sync**: 同步接口正常时返回mtime，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回mtime，或者error信息, 当errorCode不等于ErrorCode.FPNN_EC_OK.value(),则为error返回，可查看message错误信息.

### 发送 Broadcast 聊天消息

    // sync methods
    long sendBroadcastChat(long fromUid, String message, String attrs);
    long sendBroadcastChat(long fromUid, String message, String attrs, int timeoutInseconds);
    
    // async methods
    void sendBroadcastChat(long fromUid, String message, String attrs, SendMessageLambdaCallback callback);
    void sendBroadcastChat(long fromUid, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);
    
参数说明:   
* `String attrs`: 消息的属性信息，建议使用可解析的json字符串，默认为空字符串

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `SendMessageLambdaCallback callback`: 为异步回调返回接口, mtime以及 error信息将通过callback返回
        
        public interface SendMessageLambdaCallback{
            void done(long time, int errorCode, String errorMessage);
        }

返回值:       

* **sync**: 同步接口正常时返回mtime，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回mtime，或者error信息, 当errorCode不等于ErrorCode.FPNN_EC_OK.value(),则为error返回，可查看message错误信息.

### 发送 Broadcast 语音消息

    // sync methods
    long sendBroadcastAudio(long fromUid, byte[] message, String attrs);
    long sendBroadcastAudio(long fromUid, byte[] message, String attrs, int timeoutInseconds);
    
    // async methods
    void sendBroadcastAudio(long fromUid, byte[] message, String attrs, SendMessageLambdaCallback callback);
    void sendBroadcastAudio(long fromUid, byte[] message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);

参数说明:   

* `String attrs`: 消息的属性信息，建议使用可解析的json字符串，默认为空字符串

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `SendMessageLambdaCallback callback`: 为异步回调返回接口, mtime以及 error信息将通过callback返回
        
        public interface SendMessageLambdaCallback{
            void done(long time, int errorCode, String errorMessage);
        }

返回值:       

* **sync**: 同步接口正常时返回mtime，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回mtime，或者error信息, 当errorCode不等于ErrorCode.FPNN_EC_OK.value(),则为error返回，可查看message错误信息.

### 发送 Broadcast 控制命令

    // sync methods
    long sendBroadcastCmd(long fromUid, String message, String attrs);
    long sendBroadcastCmd(long fromUid, String message, String attrs, int timeoutInseconds);
    
    // async methods
    void sendBroadcastCmd(long fromUid, String message, String attrs, SendMessageLambdaCallback callback);
    void sendBroadcastCmd(long fromUid, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);

参数说明:   

* `String attrs`: 消息的属性信息，建议使用可解析的json字符串，默认为空字符串

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `SendMessageLambdaCallback callback`: 为异步回调返回接口， mtime以及error信息将通过callback返回
        
        public interface SendMessageLambdaCallback{
            void done(long time, int errorCode, String errorMessage);
        }

返回值:     
  
* **sync**: 同步接口正常时返回mtime，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回mtime，或者error信息，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息.

### 聊天的类型
   
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
    
### 获取聊天

    // sync methods
    RTMRetrievedMessage getChat(long mid, long from, long xid, MessageType type);
    RTMRetrievedMessage getChat(long mid, long from, long xid, MessageType type, int timeoutInseconds);
    
    // async methods
    void getChat(long mid, long from, long xid, MessageType type, GetRetrievedMessageLambdaCallback callback);
    void getChat(long mid, long from, long xid, MessageType type, GetRetrievedMessageLambdaCallback callback, int timeoutInseconds);

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


### 删除聊天

    // sync methods
    void deleteChat(long mid, long from, long xid, MessageType type);
    void deleteChat(long mid, long from, long xid, MessageType type, int timeoutInseconds);
    
    // async methods
    void deleteChat(long mid, long from, long xid, MessageType type, DoneLambdaCallback callback);
    void deleteChat(long mid, long from, long xid, MessageType type, DoneLambdaCallback callback, int timeoutInseconds);
    
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
   