# RTM Server Java SDK Friend API Docs

# Index

[TOC]

### -------------------------Friendship interface--------------------------

### add friend

    // sync methods
    void addFriends(long uid, Set<Long> friends);
    void addFriends(long uid, Set<Long> friends, int timeoutInseconds);
    
    // sync methods
    void addFriends(long uid, Set<Long> friends, DoneLambdaCallback callback);
    void addFriends(long uid, Set<Long> friends, DoneLambdaCallback callback, int timeoutInseconds);
    
Parameter Description: 
  
* `Set<Long> friends`: **Add up to 100 people** each time.

* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.

* `DoneLambdaCallback callback`: return interface for asynchronous callback, error code and error message will be returned through callback
        
        public interface DoneLambdaCallback {
            void done(int errorCode, String message);
        }

return value:   
    
* **sync**: When the synchronization interface is normal, it returns empty. When the error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.

* **async**: The asynchronous interface will not throw an exception, and the interface call result will be returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it will be returned as an error. You can view the message error information.

### delete friend

    // sync methods
    void delFriends(long uid, Set<Long> friends);
    void delFriends(long uid, Set<Long> friends, int timeoutInseconds);
    
    // async methods
    void delFriends(long uid, Set<Long> friends, DoneLambdaCallback callback);
    void delFriends(long uid, Set<Long> friends, DoneLambdaCallback callback, int timeoutInseconds);
    
Parameter Description:   
 
* `Set<Long> friends`: **Delete up to 100 people** each time.
 
* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.
 
* `DoneLambdaCallback callback`: return interface for asynchronous callback, error code and error message will be returned through callback
         
         public interface DoneLambdaCallback {
             void done(int errorCode, String message);
         }
 
return value:      
  
* **sync**: When the synchronization interface is normal, it returns empty. When the error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.
 
* **async**: The asynchronous interface will not throw an exception, and the interface call result will be returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it will be returned as an error. You can view the message error information.

### Get friends list

    // sync methods
    Set<Long> getFriends(long uid);
    Set<Long> getFriends(long uid, int timeoutInseconds);
    
    // async methods
    void getFriends(long uid, GetFriendsLambdaCallBack callback);
    void getFriends(long uid, GetFriendsLambdaCallBack callback, int timeoutInseconds);
    
Parameter Description:   

* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.

* `GetFriendsLambdaCallBack callback`: return interface for asynchronous callback, return result, error code and error message will be returned through callback
        
        public interface GetFriendsLambdaCallBack{
            void done(Set<Long> fuids, int errorCode, String errorMessage);
        }

return value:      
 
* **sync**: The friend list of the uid is returned when the synchronization interface is normal. When an error is returned, an RTMException or other systemic exception will be thrown. For the RTMException, you can view the error information through the toString method.

* **async**: The asynchronous interface will not throw an exception. The uid's friend list is returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it is returned as an error. You can view the message error information.

### Determine the friendship

    // sync methods
    boolean isFriend(long uid, long fuid);
    boolean isFriend(long uid, long fuid, int timeoutInseconds);
    
    // async methods
    void isFriend(long uid, long fuid, IsFriendLambdaCallBack callback);
    void isFriend(long uid, long fuid, IsFriendLambdaCallBack callback, int timeoutInseconds);
 
Parameter Description:   

* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.

* `IsFriendLambdaCallBack callback`: return interface for asynchronous callback, return result, error code and error message will be returned through callback
        
        public interface IsFriendLambdaCallBack{
            void done(boolean ok, int errorCode, String errorMessage);
        }

return value:
       
* **sync**: The synchronization interface returns boolean when it is normal, and RTMException or other systemic exceptions will be thrown when the error returns. For RTMException exceptions, you can view the error information through the toString method.

* **async**: The asynchronous interface does not throw an exception, and returns a boolean through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it is returned as an error. You can view the message error information.

### Determine the multi-person friend relationship

    // sync methods
    Set<Long> isFriends(long uid, Set<Long> fuids);
    Set<Long> isFriends(long uid, Set<Long> fuids, int timeoutInseconds);
    
    // async methods
    void isFriends(long uid, Set<Long> fuids, IsFriendsLambdaCallBack callback);
    void isFriends(long uid, Set<Long> fuids, IsFriendsLambdaCallBack callback, int timeoutInseconds);

Parameter Description:   
 
* `Set<Long> fuids`: **At most 100 judges** each time.
 
* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.
 
* `IsFriendsLambdaCallBack callback`: return interface for asynchronous callback, error code and error message will be returned through callback
         
         public interface IsFriendsLambdaCallBack{
            void done(Set<Long> fuids, int errorCode, String errorMessage);
         }
 
return value:
        
* **sync**: The friend list of the uid is returned when the synchronization interface is normal. When an error is returned, an RTMException or other systemic exception will be thrown. For the RTMException, you can view the error information through the toString method.
 
* **async**: The asynchronous interface will not throw an exception. The uid's friend list is returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it is returned as an error. You can view the message error information.
          