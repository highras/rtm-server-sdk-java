# RTM Server Java SDK HistoryMessage API Docs

# Index

[TOC]

### -------------------------Get historical message data interface--------------------------

### Historical message data unit

    public static class AudioInfo{
        public String sourceLanguage;
        public String recognizedLanguage;
        public String recognizedText;
        public int duration;

    }

    // for rtmmessage
    public static class RTMMessage{
        public byte messageType;
        public long toId; // for serverpush
        public long fromUid;
        public long modifiedTime;
        public long messageId;
        public String stringMessage;
        public byte[] binaryMessage;
        public String attrs;
        public AudioInfo audioInfo = null; //for serverpush and history

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
    
### Historical messages return results
    
    public static class RTMHistoryMessage {
        public int count; // The number of entries actually returned
        public long lastCursorId; // When the polling continues, the value of the lastId parameter used in the next call
        public long beginMsec; // When continuing to poll, the value of the begin parameter used in the next call
        public long endMsec; // When the polling continues, the value of the end parameter used in the next call
        public List<HistoryMessage> messageList = new ArrayList<HistoryMessage>();

        @Override
        public String toString(){
            String ss = "[HistoryMessage] count = "+ count +" ,lastId = "+ lastId +" ,beginMsec = "+ beginMsec +" ,endMsec = "+ endMsec;
            if(!messageList.isEmpty()){
                ss += ", one message info:" + messageList.get(0).toString();
            }
            else{
                ss += ",[HistoryMessage empty]";
            }
            return ss;

        }
    }
    
### Get P2P historical news

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
    
Parameter Description:   

* `boolean desc`: false: start from the timestamp of begin, turn pages in order. true: start from the timestamp of end, turn pages in reverse order.

* `int count`: Get the number of entries. Suggest 10 items, **maximum 20 items at a time**.

* `long begin`: start timestamp, accurate to **milliseconds**, default 0. Use the current server time. Condition: >=

* `long end`: end timestamp, accurate to **milliseconds**, default 0. Use the current server time. Condition: <=

* `long lastId`: The id of the last message. The default value is 0 for the first time. Condition:> or <

* `List<Byte> mtypes`: specify the obtained mtype type, which can be empty or null

* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.

* `GetHistoryMessagesLambdaCallback callback`: return interface for asynchronous callback, historical data, error code and error message will be returned through callback
        
        public interface GetHistoryMessagesLambdaCallback{
            void done(RTMServerClientBase.RTMHistoryMessage result, int errorCode, String errorMessage);
        }

return value:       

* **sync**: The historical data is returned when the synchronization interface is normal, and RTMException or other systemic exceptions will be thrown when an error is returned. For RTMException exceptions, you can view the error information through the toString method.

* **async**: The asynchronous interface does not throw exceptions, and returns historical data through callback. When errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it is returned as error. You can view the message error information.

### Get Group History News

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
    
Parameter Description:   

* `boolean desc`: false: start from the timestamp of begin, turn pages in order. true: start from the timestamp of end, turn pages in reverse order.

* `int count`: Get the number of entries. Suggest 10 items, **maximum 20 items at a time**.

* `long begin`: start timestamp, accurate to **milliseconds**, default 0. Use the current server time. Condition: >=

* `long end`: end timestamp, accurate to **milliseconds**, default 0. Use the current server time. Condition: <=

* `long lastId`: The id of the last message. The default value is 0 for the first time. Condition:> or <

* `List<Byte> mtypes`: specify the obtained mtype type, which can be empty or null

* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.

* `GetHistoryMessagesLambdaCallback callback`: return interface for asynchronous callback, historical data, error code and error message will be returned through callback
        
        public interface GetHistoryMessagesLambdaCallback{
            void done(RTMServerClientBase.RTMHistoryMessage result, int errorCode, String errorMessage);
        }

return value:       

* **sync**: The historical data is returned when the synchronization interface is normal, and RTMException or other systemic exceptions will be thrown when an error is returned. For RTMException exceptions, you can view the error information through the toString method.

* **async**: The asynchronous interface does not throw exceptions, and returns historical data through callback. When errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it is returned as error. You can view the message error information.

### Get Room History News

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
    
Parameter Description:   

* `boolean desc`: false: start from the timestamp of begin, turn pages in order. true: start from the timestamp of end, turn pages in reverse order.

* `int count`: Get the number of entries. Suggest 10 items, **maximum 20 items at a time**.

* `long begin`: start timestamp, accurate to **milliseconds**, default 0. Use the current server time. Condition: >=

* `long end`: end timestamp, accurate to **milliseconds**, default 0. Use the current server time. Condition: <=

* `long lastId`: The id of the last message. The default value is 0 for the first time. Condition:> or <

* `List<Byte> mtypes`: specify the obtained mtype type, which can be empty or null

* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.

* `GetHistoryMessagesLambdaCallback callback`: return interface for asynchronous callback, historical data, error code and error message will be returned through callback
        
        public interface GetHistoryMessagesLambdaCallback{
            void done(RTMServerClientBase.RTMHistoryMessage result, int errorCode, String errorMessage);
        }

return value:       

* **sync**: The historical data is returned when the synchronization interface is normal, and RTMException or other systemic exceptions will be thrown when an error is returned. For RTMException exceptions, you can view the error information through the toString method.

* **async**: The asynchronous interface does not throw exceptions, and returns historical data through callback. When errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it is returned as error. You can view the message error information.

### Get Broadcast history news

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
    
Parameter Description:   

* `boolean desc`: false: start from the timestamp of begin, turn pages in order. true: start from the timestamp of end, turn pages in reverse order.

* `int count`: Get the number of entries. Suggest 10 items, **maximum 20 items at a time**.

* `long begin`: start timestamp, accurate to **milliseconds**, default 0. Use the current server time. Condition: >=

* `long end`: end timestamp, accurate to **milliseconds**, default 0. Use the current server time. Condition: <=

* `long lastId`: The id of the last message. The default value is 0 for the first time. Condition:> or <

* `List<Byte> mtypes`: specify the obtained mtype type, which can be empty or null

* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.

* `GetHistoryMessagesLambdaCallback callback`: return interface for asynchronous callback, historical data, error code and error message will be returned through callback
        
        public interface GetHistoryMessagesLambdaCallback{
            void done(RTMServerClientBase.RTMHistoryMessage result, int errorCode, String errorMessage);
        }

return value:       

* **sync**: The historical data is returned when the synchronization interface is normal, and RTMException or other systemic exceptions will be thrown when an error is returned. For RTMException exceptions, you can view the error information through the toString method.

* **async**: The asynchronous interface does not throw exceptions, and returns historical data through callback. When errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it is returned as error. You can view the message error information.