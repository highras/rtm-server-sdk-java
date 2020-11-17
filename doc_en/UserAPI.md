# RTM Server Java SDK User API Docs

# Index

[TOC]

### ------------------------User operation related interface--------------------------

### Kick users offline

    // sync methods
    void kickOut(long uid);
    void kickOut(long uid, String ce);
    void kickOut(long uid, String ce, int timeoutInseconds);
    
    // async methods
    void kickOut(long uid, DoneLambdaCallback callback);
    void kickOut(long uid, String ce, DoneLambdaCallback callback);
    void kickOut(long uid, String ce, DoneLambdaCallback callback, int timeoutInseconds);
    
Parameter Description:  

* `String ce`: When ce is not empty, only one of the links will be kicked out, multi-user login situation

* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.
  
* `DoneLambdaCallback callback`: return interface for asynchronous callback, error code and error message will be returned through callback
          
        public interface DoneLambdaCallback {
            void done(int errorCode, String message);
        }
  
return value:       
  
* **sync**: When the synchronization interface is normal, it returns empty. When the error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.
  
* **async**: The asynchronous interface will not throw an exception, and the interface call result will be returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it will be returned as an error. You can view the message error information.

### Get online users

    // sync methods
    Set<Long> getOnlineUsers(Set<Long> uids);
    Set<Long> getOnlineUsers(Set<Long> uids, int timeoutInseconds);
    
    // async methods
    void getOnlineUsers(Set<Long> uids, GetOnlineUsersCallback callback);
    void getOnlineUsers(Set<Long> uids, GetOnlineUsersCallback callback, int timeoutInseconds);
    
Parameter Description:  

* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.
  
* `GetOnlineUsersCallback callback`: return interface for asynchronous callback, error code and error message will be returned through callback
          
        public interface GetOnlineUsersCallback {
            void done(Set<Long> uids, int errorCode, String errorMessage);
        }
  
return value:       
  
* **sync**: When the synchronization interface is normal, it returns online users in the uids collection. When an error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.
  
* **async**: The asynchronous interface will not throw an exception. The online user in the uids collection is returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it is returned as an error. You can view the message error information.

### Add users to the project blacklist

    // sync method
    void addProjectBlack(long uid, int btime);
    void addProjectBlack(long uid, int btime, int timeoutInseconds);
    
    // async methods
    void addProjectBlack(long uid, int btime, DoneLambdaCallback callback);
    void addProjectBlack(long uid, int btime, DoneLambdaCallback callback, int timeoutInseconds);
    
Parameter Description:  

* `int btime`: duration, starting from the current time, in seconds
  
* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.
  
* `DoneLambdaCallback callback`: return interface for asynchronous callback, error code and error message will be returned through callback
          
        public interface DoneLambdaCallback {
            void done(int errorCode, String message);
        }
  
return value:       
  
* **sync**: When the synchronization interface is normal, it returns empty. When the error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.
  
* **async**: The asynchronous interface will not throw an exception, and the interface call result will be returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it will be returned as an error. You can view the message error information.

### Remove users from the project blacklist

    // sync methods
    void removeProjectBlack(long uid);
    void removeProjectBlack(long uid, int timeoutInseconds);
    
    // async methods
    void removeProjectBlack(long uid, DoneLambdaCallback callback);
    void removeProjectBlack(long uid, DoneLambdaCallback callback, int timeoutInseconds);
    
Parameter Description:  
  
* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.
  
* `DoneLambdaCallback callback`: return interface for asynchronous callback, error code and error message will be returned through callback
          
        public interface DoneLambdaCallback {
            void done(int errorCode, String message);
        }
  
return value:       
  
* **sync**: When the synchronization interface is normal, it returns empty. When the error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.
  
* **async**: The asynchronous interface will not throw an exception, and the interface call result will be returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it will be returned as an error. You can view the message error information.

### Determine whether the user is in the project blacklist

    // sync methods
    boolean isProjectBlack(long uid);
    boolean isProjectBlack(long uid, int timeoutInseconds);
    
    // async methods
    void isProjectBlack(long uid, IsProjectBlackCallback callback);
    void isProjectBlack(long uid, IsProjectBlackCallback callback, int timeoutInseconds);
    
Parameter Description:  
  
* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.
  
* `IsProjectBlackCallback callback`: return interface for asynchronous callback, error code and error message will be returned through callback
          
        public interface IsProjectBlackCallback{
            void done(boolean ok, int errorCode, String errorMessage);
        }
  
return value:       
  
* **sync**: The synchronization interface returns boolean when it is normal, and RTMException or other systemic exceptions will be thrown when the error returns. For RTMException exceptions, you can view the error information through the toString method.
  
* **async**: The asynchronous interface does not throw an exception, and returns a boolean through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it is returned as an error. You can view the message error information.

### Set user public and private information

    // sync methods
    void setUserInfo(long uid, String openInfo, String priInfo);
    void setUserInfo(long uid, String openInfo, String priInfo, int timeoutInseconds);
    
    // async methods
    void setUserInfo(long uid, String openInfo, String priInfo, DoneLambdaCallback callback);
    void setUserInfo(long uid, String openInfo, String priInfo, DoneLambdaCallback callback, int timeoutInseconds);

Parameter Description:  

* `String openInfo`: The public information that needs to be set, which can be null or empty string. **Maximum 65535 bytes**.

* `String priInfo`: Private information to be set, which can be null or empty string. **Maximum 65535 bytes**.
  
* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.
  
* `DoneLambdaCallback callback`: return interface for asynchronous callback, error code and error message will be returned through callback
          
        public interface DoneLambdaCallback {
            void done(int errorCode, String message);
        }
  
return value:       
  
* **sync**: When the synchronization interface is normal, it returns empty. When the error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.
  
* **async**: The asynchronous interface will not throw an exception, and the interface call result will be returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it will be returned as an error. You can view the message error information.

### Get user's public and private information

    // sync methods
    void getUserInfo(long uid, StringBuffer openInfo, StringBuffer priInfo);
    void getUserInfo(long uid, StringBuffer openInfo, StringBuffer priInfo, int timeoutInseconds);
    
    // async methods
    void getUserInfo(long uid, GetUserInfoCallback callback);
    void getUserInfo(long uid, GetUserInfoCallback callback, int timeoutInseconds);
    
Parameter Description:  

* `StringBuffer openInfo`: Returns the public information of rid.

* `StringBuffer priInfo`: Returns the private information of rid.
  
* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.
  
* `GetUserInfoCallback callback`: return interface for asynchronous callback, the call result, error code and error information will be returned through callback
          
        public interface GetUserInfoCallback{
            void done(String openInfo, String priInfo, int errorCode, String errorMessage);
        }
  
return value:       
  
* **sync**: The synchronization interface returns empty when it is normal, and returns public and private information of the uid through parameters. When an error is returned, an exception RTMException or other systemic exceptions will be thrown. For RTMException exceptions, you can view the error through the toString method information.
  
* **async**: The asynchronous interface will not throw an exception. The public and private information of the uid is returned through the callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), the error is returned. You can view the message error information.

### Get user public information in batches

    // sync methods
    Map<String, String> getUserOpenInfo(Set<Long> uids);
    Map<String, String> getUserOpenInfo(Set<Long> uids, int timeoutInseconds);
    
    // async
    void getUserOpenInfo(Set<Long> uids, GetUserOpenInfoCallback callback);
    void getUserOpenInfo(Set<Long> uids, GetUserOpenInfoCallback callback, int timeoutInseconds);
             
Parameter Description:  
  
* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.
  
* `GetUserOpenInfoCallback callback`: return interface for asynchronous callback, the call result, error code and error information will be returned through callback
          
        public interface GetUserOpenInfoCallback{
            void done(Map<String, String> info, int errorCode, String errorMessage);
        }
  
return value:       
  
* **sync**: When the synchronization interface is normal, it returns Map<String, String>, the key is uid, and the passed uid is converted to String for compatibility, and the value is public information. When an error is returned, an exception RTMException or other systems will be thrown Sexual exceptions, for RTMException exceptions, you can view the error information through the toString method.

* **async**: Asynchronous interface will not throw exceptions. Map<String, String> is returned through callback, and the key is uid. The passed uid will be converted to String for compatibility. Value is public information. When errorCode is not equal to ErrorCode .FPNN_EC_OK.value(), the error is returned, you can view the message error information.