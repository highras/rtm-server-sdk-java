# RTM Server Java SDK Data API Docs

# Index

[TOC]

### -------------------------User Data Interface--------------------------

### Store user data

    // sync method
    void dataSet(long uid, String key, String value);
    void dataSet(long uid, String key, String value, int timeoutInseconds);
    
    // async methods
    void dataSet(long uid, String key, String value, DoneLambdaCallback callback);
    void dataSet(long uid, String key, String value, DoneLambdaCallback callback, int timeoutInseconds);
    
Parameter Description:   

* `String key`: Maximum 128 bytes.

* `String value`: Maximum 65535 bytes.

* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.

* `DoneLambdaCallback callback`: return interface for asynchronous callback, error code and error message will be returned through callback
        
        public interface DoneLambdaCallback {
            void done(int errorCode, String message);
        }

return value:       

* **sync**: When the synchronization interface is normal, it returns empty. When the error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.

* **async**: The asynchronous interface will not throw an exception, and the interface call result will be returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it will be returned as an error. You can view the message error information.

### Get user stored data

    // sync methods
    String dataGet(long uid, String key);
    String dataGet(long uid, String key, int timeoutInseconds);
    
    // async methods
    void dataGet(long uid, String key, DataGetCallback callback);
    void dataGet(long uid, String key, DataGetCallback callback, int timeoutInseconds);
    
Parameter Description:   

* `String key`: Maximum 128 bytes.

* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.

* `DataGetCallback callback`: return interface for asynchronous callback, return result, error code and error message will be returned through callback
        
        public interface DataGetCallback{
            void done(String value, int errorCode, String errorMessage);
        }

return value:       

* **sync**: The data of the key is returned when the synchronization interface is normal, and RTMException or other systemic exceptions will be thrown when the error returns. For RTMException exceptions, you can view the error information through the toString method.

* **async**: The asynchronous interface will not throw an exception. The key data is returned through the callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it is returned as an error. You can view the message error information.

### Delete user storage data

    // sync methods
    void dataDel(long uid, String key);
    void dataDel(long uid, String key, int timeoutInseconds);
    
    // async methods
    void dataDel(long uid, String key, DoneLambdaCallback callback);
    void dataDel(long uid, String key, DoneLambdaCallback callback, int timeoutInseconds);

Parameter Description:   

* `String key`: Maximum 128 bytes.

* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.

* `DoneLambdaCallback callback`: return interface for asynchronous callback, error code and error message will be returned through callback
        
        public interface DoneLambdaCallback {
            void done(int errorCode, String message);
        }

return value:       

* **sync**: When the synchronization interface is normal, it returns empty. When the error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.

* **async**: The asynchronous interface will not throw an exception, and the interface call result will be returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it is returned as an error. You can view the message error information.