# RTM Server Java SDK Token API Docs

# Index

[TOC]

### -------------------------Token related interface--------------------------

### Get user login token

    // sync methods
    String getToken(long uid)
    String getToken(long uid, int timeoutInseconds);

    // async methods
    boolean getToken(long uid, GetTokenLambdaCallback callback);
    boolean getToken(long uid, GetTokenLambdaCallback callback, int timeoutInseconds);
    
Parameter Description:  
  
* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.
  
* `GetTokenLambdaCallback callback`: return interface for asynchronous callback, error code and error message will be returned through callback
          
        public interface GetTokenLambdaCallback{
            void done(String token, int errorCode, String errorMessage);
        }
  
return value:       
  
* **sync**: The synchronous interface returns the token when it is normal, and when the error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.
  
* **async**: The asynchronous interface does not throw an exception, and returns the token through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it is returned as an error. You can view the message error information.

### Delete user login token

    // sync methods
    void removeToken(long uid);
    void removeToken(long uid, int timeoutInseconds);
    
    // async methods
    void removeToken(long uid, DoneLambdaCallback callback);
    void removeToken(long uid, DoneLambdaCallback callback, int timeoutInseconds);
    
Parameter Description:  
  
* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.
  
* `DoneLambdaCallback callback`: return interface for asynchronous callback, error code and error message will be returned through callback
          
        public interface DoneLambdaCallback {
            void done(int errorCode, String message);
        }
  
return value:       
  
* **sync**: When the synchronization interface is normal, it returns empty. When the error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.
  
* **async**: The asynchronous interface will not throw an exception, and the interface call result will be returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it is returned as an error. You can view the message error information.