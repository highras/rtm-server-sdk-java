# RTM Server Java SDK HistoryChat API Docs

# Index

[TOC]

### -------------------------获取历史聊天数据接口--------------------------

### 历史聊天数据单元

    public static class RTMHistoryMessageUnit{
        public byte mtype;
        public long id;
        public long fromUid;
        public long mtime;
        public long mid;
        public String stringMessage;
        public byte[] binaryMessage;
        public String attrs;
        
        @Override
        public String toString(){
            return " ,[RTMHistoryMessageUnit mtype = " + mtype + " ,id = " + id + " ,fromuid = " + fromUid + " ,mtime = " + mtime
                + " ,mid = " + mid + " ,message = " + stringMessage + " ,binaryMessage = " + binaryMessage + " ,attrs = " + attrs + "]";
        }
    }
    
### 历史聊天返回结果
    
    public static class RTMHistoryMessage {
        public int count;            // 实际返回的条目数量
        public long lastId;          // 继续轮询时，下次调用使用的 lastId 参数的值
        public long beginMsec;       // 继续轮询时，下次调用使用的 begin 参数的值
        public long endMsec;         // 继续轮询时，下次调用使用的 end 参数的值
        public List<RTMHistoryMessageUnit> messageList = new ArrayList<RTMHistoryMessageUnit>();
    
        @Override
        public String toString(){
            String ss = "[RTMHistoryMessage] count = " + count + " ,lastId = " + lastId + " ,beginMsec = " + beginMsec + " ,endMsec = " + endMsec;
            if(!messageList.isEmpty()){
                ss += messageList.get(0).toString();
            }
            else{
                ss += " ,[RTMHistoryMessageUnit empty]";
            }
            return ss;
        }
    }
    
### 获取 P2P 历史聊天

    // sync methods
    RTMHistoryMessage getP2PChat(long uid, long peerUid, boolean desc, int count);
    RTMHistoryMessage getP2PChat(long uid, long peerUid, boolean desc, int count, int timeoutInseconds);
    RTMHistoryMessage getP2PChat(long uid, long peerUid, boolean desc, int count, long begin, long end, long lastId);
    RTMHistoryMessage getP2PChat(long uid, long peerUid, boolean desc, int count, long begin, long end, long lastId, int timeoutInseconds);
    
    // async methods
    void getP2PChat(long uid, long peerUid, boolean desc, int count, GetHistoryMessagesLambdaCallback callback);
    void getP2PChat(long uid, long peerUid, boolean desc, int count, GetHistoryMessagesLambdaCallback callback, int timeoutInseconds);
    void getP2PChat(long uid, long peerUid, boolean desc, int count, long begin, long end, long lastId, GetHistoryMessagesLambdaCallback callback);
    void getP2PChat(long uid, long peerUid, boolean desc, int count, long begin, long end, long lastId, GetHistoryMessagesLambdaCallback callback, int timeoutInseconds);
    
参数说明：   

* `boolean desc`: false: 从begin的时间戳开始，顺序翻页. true: 从end的时间戳开始，倒序翻页.

* `int count`: 获取条目数量。建议10条，**最多一次20条**.

* `long begin`: 开始时间戳，精确到**毫秒**，默认0。使用服务器当前时间。条件：>=

* `long end`: 结束时间戳，精确到**毫秒**，默认0。使用服务器当前时间。条件：<=

* `long lastId`: 最后一条消息的id，第一次填默认0。条件：> 或者 <

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `GetHistoryMessagesLambdaCallback callback`: 为异步回调返回接口, 历史数据以及错误码和错误信息将通过callback返回
        
        public interface GetHistoryMessagesLambdaCallback{
            void done(RTMServerClientBase.RTMHistoryMessage result, int errorCode, String errorMessage);
        }

返回值:       

* **sync**: 同步接口正常时返回历史数据，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回历史数据，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息.  

### 获取 Group 历史聊天

    // sync methods
    RTMHistoryMessage getGroupChat(long uid, long gid, boolean desc, int count);
    RTMHistoryMessage getGroupChat(long uid, long gid, boolean desc, int count, int timeoutInseconds);
    RTMHistoryMessage getGroupChat(long uid, long gid, boolean desc, int count, long begin, long end, long lastId);
    RTMHistoryMessage getGroupChat(long uid, long gid, boolean desc, int count, long begin, long end, long lastId, int timeoutInseconds);
    
    // async methods
    void getGroupChat(long uid, long gid, boolean desc, int count, GetHistoryMessagesLambdaCallback callback);
    void getGroupChat(long uid, long gid, boolean desc, int count, GetHistoryMessagesLambdaCallback callback, int timeoutInseconds);
    void getGroupChat(long uid, long gid, boolean desc, int count, long begin, long end, long lastId, GetHistoryMessagesLambdaCallback callback);
    void getGroupChat(long uid, long gid, boolean desc, int count, long begin, long end, long lastId, GetHistoryMessagesLambdaCallback callback, int timeoutInseconds);
    
参数说明：   

* `boolean desc`: false: 从begin的时间戳开始，顺序翻页. true: 从end的时间戳开始，倒序翻页.

* `int count`: 获取条目数量。建议10条，**最多一次20条**.

* `long begin`: 开始时间戳，精确到**毫秒**，默认0。使用服务器当前时间。条件：>=

* `long end`: 结束时间戳，精确到**毫秒**，默认0。使用服务器当前时间。条件：<=

* `long lastId`: 最后一条消息的id，第一次填默认0。条件：> 或者 <

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `GetHistoryMessagesLambdaCallback callback`: 为异步回调返回接口, 历史数据以及错误码和错误信息将通过callback返回
        
        public interface GetHistoryMessagesLambdaCallback{
            void done(RTMServerClientBase.RTMHistoryMessage result, int errorCode, String errorMessage);
        }

返回值:       

* **sync**: 同步接口正常时返回历史数据，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回历史数据，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息.

### 获取 Room 历史聊天

    // sync methods
    RTMHistoryMessage getRoomChat(long uid, long rid, boolean desc, int count);
    RTMHistoryMessage getRoomChat(long uid, long rid, boolean desc, int count, int timeoutInseconds);
    RTMHistoryMessage getRoomChat(long uid, long rid, boolean desc, int count, long begin, long end, long lastId);
    RTMHistoryMessage getRoomChat(long uid, long rid, boolean desc, int count, long begin, long end, long lastId, int timeoutInseconds);
    
    // async methods
    void getRoomChat(long uid, long rid, boolean desc, int count, GetHistoryMessagesLambdaCallback callback);
    void getRoomChat(long uid, long rid, boolean desc, int count, GetHistoryMessagesLambdaCallback callback, int timeoutInseconds);
    void getRoomChat(long uid, long rid, boolean desc, int count, long begin, long end, long lastId, GetHistoryMessagesLambdaCallback callback);
    void getRoomChat(long uid, long rid, boolean desc, int count, long begin, long end, long lastId, GetHistoryMessagesLambdaCallback callback, int timeoutInseconds);
    
参数说明：   

* `boolean desc`: false: 从begin的时间戳开始，顺序翻页. true: 从end的时间戳开始，倒序翻页.

* `int count`: 获取条目数量。建议10条，**最多一次20条**.

* `long begin`: 开始时间戳，精确到**毫秒**，默认0。使用服务器当前时间。条件：>=

* `long end`: 结束时间戳，精确到**毫秒**，默认0。使用服务器当前时间。条件：<=

* `long lastId`: 最后一条消息的id，第一次填默认0。条件：> 或者 <

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `GetHistoryMessagesLambdaCallback callback`: 为异步回调返回接口, 历史数据以及错误码和错误信息将通过callback返回
        
        public interface GetHistoryMessagesLambdaCallback{
            void done(RTMServerClientBase.RTMHistoryMessage result, int errorCode, String errorMessage);
        }

返回值:       

* **sync**: 同步接口正常时返回历史数据，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回历史数据，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息. 

### 获取 Broadcast 历史聊天

    // sync methods
    RTMHistoryMessage getBroadCastChat(long uid, boolean desc, int count);
    RTMHistoryMessage getBroadCastChat(long uid, boolean desc, int count, int timeoutInseconds);
    RTMHistoryMessage getBroadCastChat(long uid, boolean desc, int count, long begin, long end, long lastId);
    RTMHistoryMessage getBroadCastChat(long uid, boolean desc, int count, long begin, long end, long lastId, int timeoutInseconds);
    
    // async methods
    void getBroadCastChat(long uid, boolean desc, int count, GetHistoryMessagesLambdaCallback callback);
    void getBroadCastChat(long uid, boolean desc, int count, GetHistoryMessagesLambdaCallback callback, int timeoutInseconds);
    void getBroadCastChat(long uid, boolean desc, int count, long begin, long end, long lastId, GetHistoryMessagesLambdaCallback callback);
    void getBroadCastChat(long uid, boolean desc, int count, long begin, long end, long lastId, GetHistoryMessagesLambdaCallback callback, int timeoutInseconds);
    
参数说明：   

* `boolean desc`: false: 从begin的时间戳开始，顺序翻页. true: 从end的时间戳开始，倒序翻页.

* `int count`: 获取条目数量。建议10条，**最多一次20条**.

* `long begin`: 开始时间戳，精确到**毫秒**，默认0。使用服务器当前时间。条件：>=

* `long end`: 结束时间戳，精确到**毫秒**，默认0。使用服务器当前时间。条件：<=

* `long lastId`: 最后一条消息的id，第一次填默认0。条件：> 或者 <

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `GetHistoryMessagesLambdaCallback callback`: 为异步回调返回接口, 历史数据以及错误码和错误信息将通过callback返回
        
        public interface GetHistoryMessagesLambdaCallback{
            void done(RTMServerClientBase.RTMHistoryMessage result, int errorCode, String errorMessage);
        }

返回值:       

* **sync**: 同步接口正常时返回历史数据，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回历史数据，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息.   