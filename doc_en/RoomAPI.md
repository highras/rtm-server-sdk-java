# RTM Server Java SDK Room API Docs

# Index

[TOC]

### ------------------------- Room related interface--------------------------

### Add room member

    // sync methods
    void addRoomMember(long roomId, long uid);
    void addRoomMember(long roomId, long uid, int timeoutInseconds);
    
    // async methods
    void addRoomMember(long roomId, long uid, DoneLambdaCallback callback);
    void addRoomMember(long roomId, long uid, DoneLambdaCallback callback, int timeoutInseconds);
    
Parameter Description:   

* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.

* `DoneLambdaCallback callback`: return interface for asynchronous callback, error code and error message will be returned through callback
        
        public interface DoneLambdaCallback {
            void done(int errorCode, String message);
        }

return value:  
     
* **sync**: When the synchronization interface is normal, it returns empty. When the error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.

* **async**: The asynchronous interface will not throw an exception, and the interface call result will be returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it will be returned as an error. You can view the message error information.

### Delete room member

    // sync methods
    void delRoomMember(long roomId, long uid);
    void delRoomMember(long roomId, long uid, int timeoutInseconds);
    
    // async methods
    void delRoomMember(long roomId, long uid, DoneLambdaCallback callback);
    void delRoomMember(long roomId, long uid, DoneLambdaCallback callback, int timeoutInseconds);

Parameter Description:   

* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.

* `DoneLambdaCallback callback`: return interface for asynchronous callback, error code and error message will be returned through callback
        
        public interface DoneLambdaCallback {
            void done(int errorCode, String message);
        }

return value:  
     
* **sync**: When the synchronization interface is normal, it returns empty. When the error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.

* **async**: The asynchronous interface will not throw an exception, and the interface call result will be returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it will be returned as an error. You can view the message error information.

### Forbid users to speak in the designated room

    // sync methods
    void addRoomBan(long roomId, long uid, int btime);
    void addRoomBan(long roomId, long uid, int btime, int timeoutInseconds);
    
    // async methods
    void addRoomBan(long roomId, long uid, int btime, DoneLambdaCallback callback);
    void addRoomBan(long roomId, long uid, int btime, DoneLambdaCallback callback, int timeoutInseconds);
    
Parameter Description:  

* `int btime`: The duration of the mute, starting from the current time, in seconds
  
* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.
  
* `DoneLambdaCallback callback`: return interface for asynchronous callback, error code and error message will be returned through callback
          
        public interface DoneLambdaCallback {
            void done(int errorCode, String message);
        }
  
return value:       
  
* **sync**: When the synchronization interface is normal, it returns empty. When the error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.
  
* **async**: The asynchronous interface will not throw an exception, and the interface call result will be returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it will be returned as an error. You can view the message error information.
       
### Remove the mute in the room specified by the user

    // sync methods
    void removeRoomBan(long roomId, long uid);
    void removeRoomBan(long roomId, long uid, int timeoutInseconds);
    
    // async methods
    void removeRoomBan(long roomId, long uid, DoneLambdaCallback callback);
    void removeRoomBan(long roomId, long uid, DoneLambdaCallback callback, int timeoutInseconds);
    
Parameter Description:  
  
* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.
  
* `DoneLambdaCallback callback`: return interface for asynchronous callback, error code and error message will be returned through callback
          
        public interface DoneLambdaCallback {
            void done(int errorCode, String message);
        }
  
return value:       
  
* **sync**: When the synchronization interface is normal, it returns empty. When the error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.
  
* **async**: The asynchronous interface will not throw an exception, and the interface call result will be returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it will be returned as an error. You can view the message error information.

### Determine whether the user is muted in the specified room

    // sync methods
    boolean isBanOfRoom(long roomId, long uid);
    boolean isBanOfRoom(long roomId, long uid, int timeoutInseconds);
    
    // async methods
    void isBanOfRoom(long roomId, long uid, IsBanOfRoomCallBack callback);
    void isBanOfRoom(long roomId, long uid, IsBanOfRoomCallBack callback, int timeoutInseconds);
    
Parameter Description:  
  
* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.
  
* `IsBanOfRoomCallBack callback`: return interface for asynchronous callback, error code and error message will be returned through callback
          
        public interface IsBanOfRoomCallBack{
            void done(boolean ok, int errorCode, String errorMessage);
        }
  
return value:       
  
* **sync**: The synchronization interface returns boolean when it is normal, and RTMException or other systemic exceptions will be thrown when the error returns. For RTMException exceptions, you can view the error information through the toString method.
  
* **async**: The asynchronous interface does not throw an exception, and returns a boolean through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it is returned as an error. You can view the message error information.

### Set room public information and private information
    
    // sync methods
    void setRoomInfo(long roomId, String openInfo, String priInfo);
    void setRoomInfo(long roomId, String openInfo, String priInfo, int timeoutInseconds);
    
    // async methods
    void setRoomInfo(long roomId, String openInfo, String priInfo, DoneLambdaCallback callback);
    void setRoomInfo(long roomId, String openInfo, String priInfo, DoneLambdaCallback callback, int timeoutInseconds);
    
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

### Get public and private information of the room

    // sync methods
    void getRoomInfo(long roomId, StringBuffer openInfo, StringBuffer priInfo);
    void getRoomInfo(long roomId, StringBuffer openInfo, StringBuffer priInfo, int timeoutInseconds);
    
    // async methods
    void getRoomInfo(long roomId, GetRoomInfoCallback callback);
    void getRoomInfo(long roomId, GetRoomInfoCallback callback, int timeoutInseconds);
    
Parameter Description:  

* `StringBuffer openInfo`: Returns the public information of roomId.

* `StringBuffer priInfo`: Returns the private information of roomId.
  
* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.
  
* `GetRoomInfoCallback callback`: return interface for asynchronous callback, the call result, error code and error information will be returned through callback
          
        public interface GetRoomInfoCallback{
            void done(String openInfo, String priInfo, int errorCode, String errorMessage);
        }
  
return value:       
  
* **sync**: The synchronization interface returns empty when it is normal, and the public and private information of the roomId is returned through parameter return. When an error is returned, an exception RTMException or other systemic exceptions will be thrown. For RTMException exceptions, you can view the error through the toString method information.
  
* **async**: The asynchronous interface will not throw an exception. The public and private information of the roomId is returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it is returned as an error. You can view the message error information.
