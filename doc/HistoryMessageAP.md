# RTM Server Java SDK HistoryMessage API Docs

# Index

[TOC]

### -------------------------获取历史消息数据接口--------------------------

### 历史消息数据单元

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
    
### 历史消息返回结果
    
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
    
### 获取 P2P 历史消息

    // sync methods
    RTMHistoryMessage getP2PMsg(long uid, long peerUid, boolean desc, int count);
    RTMHistoryMessage getP2PMsg(long uid, long peerUid, boolean desc, int count, int timeoutInseconds);
    RTMHistoryMessage getP2PMsg(long uid, long peerUid, boolean desc, int count, long begin, long end, long lastId, List<Byte> mtypes);
    RTMHistoryMessage getP2PMsg(long uid, long peerUid, boolean desc, int count, long begin, long end, long lastId, List<Byte> mtypes, int timeoutInseconds);
    
    // async methods
    void getP2PMsg(long uid, long peerUid, boolean desc, int count, GetHistoryMessagesLambdaCallback callback);
    void getP2PMsg(long uid, long peerUid, boolean desc, int count, GetHistoryMessagesLambdaCallback callback, int timeoutInseconds);
    void getP2PMsg(long uid, long peerUid, boolean desc, int count, long begin, long end, long lastId, List<Byte> mtype, GetHistoryMessagesLambdaCallback callback);
    void getP2PMsg(long uid, long peerUid, boolean desc, int count, long begin, long end, long lastId, List<Byte> mtypes, GetHistoryMessagesLambdaCallback callback, int timeoutInseconds);
    
参数说明：   

* `boolean desc`: false: 从begin的时间戳开始，顺序翻页. true: 从end的时间戳开始，倒序翻页.

* `int count`: 获取条目数量。建议10条，**最多一次20条**.

* `long begin`: 开始时间戳，精确到**毫秒**，默认0。使用服务器当前时间。条件：>=

* `long end`: 结束时间戳，精确到**毫秒**，默认0。使用服务器当前时间。条件：<=

* `long lastId`: 最后一条消息的id，第一次填默认0。条件：> 或者 <

* `List<Byte> mtypes`: 指定获取的 mtype类型，可为空或null

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `GetHistoryMessagesLambdaCallback callback`: 为异步回调返回接口, 历史数据以及错误码和错误信息将通过callback返回
        
        public interface GetHistoryMessagesLambdaCallback{
            void done(RTMServerClientBase.RTMHistoryMessage result, int errorCode, String errorMessage);
        }

返回值:       

* **sync**: 同步接口正常时返回历史数据，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回历史数据，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息. 

### 获取 Group 历史消息

    // sync methods
    RTMHistoryMessage getGroupMsg(long uid, long gid, boolean desc, int count);
    RTMHistoryMessage getGroupMsg(long uid, long gid, boolean desc, int count, int timeoutInseconds);
    RTMHistoryMessage getGroupMsg(long uid, long gid, boolean desc, int count, long begin, long end, long lastId, List<Byte> mtypes);
    RTMHistoryMessage getGroupMsg(long uid, long gid, boolean desc, int count, long begin, long end, long lastId, List<Byte> mtypes, int timeoutInseconds);
    
    // async methods
    void getGroupMsg(long uid, long gid, boolean desc, int count, GetHistoryMessagesLambdaCallback callback);
    void getGroupMsg(long uid, long gid, boolean desc, int count, GetHistoryMessagesLambdaCallback callback, int timeoutInseconds);
    void getGroupMsg(long uid, long gid, boolean desc, int count, long begin, long end, long lastId, List<Byte> mtype, GetHistoryMessagesLambdaCallback callback);
    void getGroupMsg(long uid, long gid, boolean desc, int count, long begin, long end, long lastId, List<Byte> mtypes, GetHistoryMessagesLambdaCallback callback, int timeoutInseconds);
    
参数说明：   

* `boolean desc`: false: 从begin的时间戳开始，顺序翻页. true: 从end的时间戳开始，倒序翻页.

* `int count`: 获取条目数量。建议10条，**最多一次20条**.

* `long begin`: 开始时间戳，精确到**毫秒**，默认0。使用服务器当前时间。条件：>=

* `long end`: 结束时间戳，精确到**毫秒**，默认0。使用服务器当前时间。条件：<=

* `long lastId`: 最后一条消息的id，第一次填默认0。条件：> 或者 <

* `List<Byte> mtypes`: 指定获取的 mtype类型，可为空或null

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `GetHistoryMessagesLambdaCallback callback`: 为异步回调返回接口, 历史数据以及错误码和错误信息将通过callback返回
        
        public interface GetHistoryMessagesLambdaCallback{
            void done(RTMServerClientBase.RTMHistoryMessage result, int errorCode, String errorMessage);
        }

返回值:       

* **sync**: 同步接口正常时返回历史数据，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回历史数据，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息. 

### 获取 Room 历史消息

    // sync methods     
    RTMHistoryMessage getRoomMsg(long uid, long rid, boolean desc, int count);
    RTMHistoryMessage getRoomMsg(long uid, long rid, boolean desc, int count, int timeoutInseconds);
    RTMHistoryMessage getRoomMsg(long uid, long rid, boolean desc, int count, long begin, long end, long lastId, List<Byte> mtypes);
    RTMHistoryMessage getRoomMsg(long uid, long rid, boolean desc, int count, long begin, long end, long lastId, List<Byte> mtypes, int timeoutInseconds);
    
    // async methods
    void getRoomMsg(long uid, long rid, boolean desc, int count, GetHistoryMessagesLambdaCallback callback);
    void getRoomMsg(long uid, long rid, boolean desc, int count, GetHistoryMessagesLambdaCallback callback, int timeoutInseconds);
    void getRoomMsg(long uid, long rid, boolean desc, int count, long begin, long end, long lastId, List<Byte> mtype, GetHistoryMessagesLambdaCallback callback);
    void getRoomMsg(long uid, long rid, boolean desc, int count, long begin, long end, long lastId, List<Byte> mtypes, GetHistoryMessagesLambdaCallback callback, int timeoutInseconds);
    
参数说明：   

* `boolean desc`: false: 从begin的时间戳开始，顺序翻页. true: 从end的时间戳开始，倒序翻页.

* `int count`: 获取条目数量。建议10条，**最多一次20条**.

* `long begin`: 开始时间戳，精确到**毫秒**，默认0。使用服务器当前时间。条件：>=

* `long end`: 结束时间戳，精确到**毫秒**，默认0。使用服务器当前时间。条件：<=

* `long lastId`: 最后一条消息的id，第一次填默认0。条件：> 或者 <

* `List<Byte> mtypes`: 指定获取的 mtype类型，可为空或null

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `GetHistoryMessagesLambdaCallback callback`: 为异步回调返回接口, 历史数据以及错误码和错误信息将通过callback返回
        
        public interface GetHistoryMessagesLambdaCallback{
            void done(RTMServerClientBase.RTMHistoryMessage result, int errorCode, String errorMessage);
        }

返回值:       

* **sync**: 同步接口正常时返回历史数据，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回历史数据，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息. 

### 获取 Broadcast 历史消息

    // sync methods
    RTMHistoryMessage getBroadCastMsg(long uid, boolean desc, int count);
    RTMHistoryMessage getBroadCastMsg(long uid, boolean desc, int count, int timeoutInseconds);
    RTMHistoryMessage getBroadCastMsg(long uid, boolean desc, int count, long begin, long end, long lastId, List<Byte> mtypes);
    RTMHistoryMessage getBroadCastMsg(long uid, boolean desc, int count, long begin, long end, long lastId, List<Byte> mtypes, int timeoutInseconds);
    
    // async methods
    void getBroadCastMsg(long uid, boolean desc, int count, GetHistoryMessagesLambdaCallback callback);
    void getBroadCastMsg(long uid, boolean desc, int count, GetHistoryMessagesLambdaCallback callback, int timeoutInseconds);
    void getBroadCastMsg(long uid, boolean desc, int count, long begin, long end, long lastId, List<Byte> mtype, GetHistoryMessagesLambdaCallback callback);
    void getBroadCastMsg(long uid, boolean desc, int count, long begin, long end, long lastId, List<Byte> mtypes, GetHistoryMessagesLambdaCallback callback, int timeoutInseconds);
    
参数说明：   

* `boolean desc`: false: 从begin的时间戳开始，顺序翻页. true: 从end的时间戳开始，倒序翻页.

* `int count`: 获取条目数量。建议10条，**最多一次20条**.

* `long begin`: 开始时间戳，精确到**毫秒**，默认0。使用服务器当前时间。条件：>=

* `long end`: 结束时间戳，精确到**毫秒**，默认0。使用服务器当前时间。条件：<=

* `long lastId`: 最后一条消息的id，第一次填默认0。条件：> 或者 <

* `List<Byte> mtypes`: 指定获取的 mtype类型，可为空或null

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `GetHistoryMessagesLambdaCallback callback`: 为异步回调返回接口, 历史数据以及错误码和错误信息将通过callback返回
        
        public interface GetHistoryMessagesLambdaCallback{
            void done(RTMServerClientBase.RTMHistoryMessage result, int errorCode, String errorMessage);
        }

返回值:       

* **sync**: 同步接口正常时返回历史数据，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回历史数据，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息.       