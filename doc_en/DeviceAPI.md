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
    
        