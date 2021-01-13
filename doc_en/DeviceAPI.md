# RTM Server Java SDK Device API Docs

# Index

[TOC]

### -------------------------User equipment information interface--------------------------

### Add device information

    // sync methods
    void addDevice(long uid, String appType, String deviceToken);
    void addDevice(long uid, String appType, String deviceToken, int timeoutInseconds);
    
    // async methods
    void addDevice(long uid, String appType, String deviceToken, DoneLambdaCallback callback);
    void addDevice(long uid, String appType, String deviceToken, DoneLambdaCallback callback, int timeoutInseconds);
    
Parameter Description:   
* `String appType`: appType value is **"fcm" or "apns"**

* `String deviceToken`: device id

* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.

* `DoneLambdaCallback callback`: return interface for asynchronous callback, error code and error message will be returned through callback
        
        public interface DoneLambdaCallback {
            void done(int errorCode, String message);
        }

return value:       

* **sync**: When the synchronization interface is normal, it returns empty. When the error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.

* **async**: The asynchronous interface will not throw an exception, and the interface call result will be returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it will be returned as an error. You can view the message error information

### Delete device information

    // sync methods
    void removeDevice(long uid, String deviceToken);
    void removeDevice(long uid, String deviceToken, int timeoutInseconds);
    
    // async methods
    void removeDevice(long uid, String deviceToken, DoneLambdaCallback callback);
    void removeDevice(long uid, String deviceToken, DoneLambdaCallback callback, int timeoutInseconds);

Parameter Description:   

* `String deviceToken`: device id

* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.

* `DoneLambdaCallback callback`: return interface for asynchronous callback, error code and error message will be returned through callback
        
        public interface DoneLambdaCallback {
            void done(int errorCode, String message);
        }

return value:     
  
* **sync**: When the synchronization interface is normal, it returns empty. When the error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.

* **async**: The asynchronous interface will not throw an exception, and the interface call result will be returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it will be returned as an error. You can view the message error information

### Set device push properties

    // sync methods
    void addDevicePushOption(long uid, int pushType, long xid, Set<Integer> mtype);
    void addDevicePushOption(long uid, int pushType, long xid, Set<Integer> mtype, int timeoutInseconds);
    
    // async methods
    void addDevicePushOption(long uid, int pushType, long xid, Set<Integer> mtype, DoneLambdaCallback callback);

Parameter Description:  

* `int pushType` : pushType** can be selected as (0,1)**, pushType == 0, cancel a p2p without pushing, pushType == 1, cancel a group without pushing

* `long xid`: When pushType is 0, xid is from, when pushType is 1, xid is groupId

* `Set<Integer> mtype`: If mtype is null or empty, all mtypes will be canceled and will not be pushed. For other values, the specified mtype will not be pushed. Note that the chat involves text, cmd, and several mtypes of files

* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.

* `DoneLambdaCallback callback`: Return interface for asynchronous callback, error code and error message will be returned through callback
        
        public interface DoneLambdaCallback {
            void done(int errorCode, String message);
        }

return value：    
  
* **sync**: When the synchronization interface is normal, it returns empty. 
When the error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.

* **async**: The asynchronous interface will not throw an exception, and the interface call result will be returned through callback. 
When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it will be returned as an error. You can view the message error information

### Delete device push attributes

    // sync methods
    void removeDevicePushOption(long uid, int pushType, long xid, Set<Integer> mtype);
    void removeDevicePushOption(long uid, int pushType, long xid, Set<Integer> mtype, int timeoutInseconds);
    
    // async methods
    void removeDevicePushOption(long uid, int pushType, long xid, Set<Integer> mtype, DoneLambdaCallback callback);
    void removeDevicePushOption(long uid, int pushType, long xid, Set<Integer> mtype, DoneLambdaCallback callback, int timeoutInseconds);

Parameter Description:   

* `int pushType` : pushType** can be selected as (0,1)**, pushType == 0, cancel a p2p without pushing, pushType == 1, cancel a group without pushing

* `long xid`: When pushType is 0, xid is from, when pushType is 1, xid is groupId

* `Set<Integer> mtype`: If mtype is null or empty, all mtypes will be canceled and will not be pushed. For other values, the specified mtype will not be pushed. Note that the chat involves text, cmd, and several mtypes of files

* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.

* `DoneLambdaCallback callback`: Return interface for asynchronous callback, error code and error message will be returned through callback
        
        public interface DoneLambdaCallback {
            void done(int errorCode, String message);
        }

return value：    
  
* **sync**: When the synchronization interface is normal, it returns empty. 
When the error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.

* **async**: The asynchronous interface will not throw an exception, and the interface call result will be returned through callback. 
When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it will be returned as an error. You can view the message error information

### Get device push properties

    // sync methods
    void getDevicePushOption(long uid, Map<Long, Set<Integer>> p2pOption, Map<Long, Set<Integer>> groupOption);
    void getDevicePushOption(long uid, Map<Long, Set<Integer>> p2pOption, Map<Long, Set<Integer>> groupOption, int timeoutInseconds);
    
    // async methods
    void getDevicePushOption(long uid, GetPushOptionLambdaCallBack callback);
    void getDevicePushOption(long uid, GetPushOptionLambdaCallBack callback, int timeoutInseconds);

Parameter Description:   

* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.

* `GetPushOptionLambdaCallBack callback`: Return interface for asynchronous callback, real data, error code and error information will be returned through callback
        
        public interface GetPushOptionLambdaCallBack{
            void done(Map<Long, Set<Integer>> p2pOption, Map<Long, Set<Integer>> groupOption, int errorCode, String errorMessage);
        }

return value：     
  
* **sync**: When the synchronization interface is normal, it returns empty, and the real data is returned through parameters(Do not push the mtype mapping corresponding to the user or group).
When the error returns, an exception RTMException or other systemic exceptions will be thrown. For RTMException exceptions, you can view the error information through the toString method.

* **async**: The asynchronous interface will not throw an exception, and the real result of the interface call is returned through the callback(Do not push the mtype mapping corresponding to the user or group).
When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), the error is returned, and the message error information can be viewed
    
        