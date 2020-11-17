# RTM Server Java SDK Black API Docs

# Index

[TOC]

### -------------------------Block user related interfaces--------------------------

### Blocking users After blocking users, the other party cannot send chat messages to themselves, but they can send chat messages to each other, and both parties can obtain session and historical messages normally.

    // sync methods
    void addBlacks(long uid, Set<Long> blackIds);
    void addBlacks(long uid, Set<Long> blackIds, int timeoutInseconds);
    
    // async methods
    void addBlacks(long uid, Set<Long> blackIds, DoneLambdaCallback callback);
    void addBlacks(long uid, Set<Long> blackIds, DoneLambdaCallback callback, int timeoutInseconds);
    
Parameter Description: 
  
* `Set<Long> blackIds`: **Add up to 100 people** each time.

* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5 seconds.

* `DoneLambdaCallback callback`: Return interface for asynchronous callback, error code and error message will be returned through callback
        
        public interface DoneLambdaCallback {
            void done(int errorCode, String message);
        }

return value:   
    
* **sync**: When the synchronization interface is normal, it returns empty, and when the error returns, it will throw an RTMException or other systemic exception. For RTMException exceptions, you can view the error information through the toString method.

* **async**: The asynchronous interface will not throw an exception, and the interface call result will be returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it is returned as an error. You can view the message error information.

### Dissolve the black relationship 

    // sync methods
    void delBlacks(long uid, Set<Long> blackIds);
    void delBlacks(long uid, Set<Long> blackIds, int timeoutInseconds);
    
    // async methods
    void delBlacks(long uid, Set<Long> blackIds, DoneLambdaCallback callback);
    void delBlacks(long uid, Set<Long> blackIds, DoneLambdaCallback callback, int timeoutInseconds);
    
Parameter Description:   
 
* `Set<Long> blackIds`: **Up to 100 people can be lifted each time**.
 
* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout), if the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5 seconds.
 
* `DoneLambdaCallback callback`: Return interface for asynchronous callback, error code and error message will be returned through callback
         
         public interface DoneLambdaCallback {
             void done(int errorCode, String message);
         }
 
return value:      
  
* **sync**: When the synchronization interface is normal, it returns empty, and when the error returns, it will throw an RTMException or other systemic exception. For RTMException exceptions, you can view the error information through the toString method.
 
* **async**: The asynchronous interface will not throw an exception, and the interface call result will be returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it is returned as an error. You can view the message error information.     

### Get the list of users blocked by uid

    // sync methods
    Set<Long> getBlacks(long uid);
    Set<Long> getBlacks(long uid, int timeoutInseconds);
    
    // async methods
    void getBlacks(long uid, GetBlacksLambdaCallBack callback);
    void getBlacks(long uid, GetBlacksLambdaCallBack callback, int timeoutInseconds);
    
Parameter Description:   

* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout), if the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5 seconds.

* `GetBlacksLambdaCallBack callback`: Return interface for asynchronous callback, return result, error code and error message will be returned through callback
        
        public interface GetBlacksLambdaCallBack{
            void done(Set<Long> uids, int errorCode, String errorMessage);
        }

return value:      
 
* **sync**: When the synchronization interface is normal, it returns a list of users whose uid is blacked out. When an error is returned, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.

* **async**: The asynchronous interface will not throw an exception. The list of users whose uid has been hacked is returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it is returned as an error. You can view the message error information.

### Judge whether the uid is blocked by the buid, it will be used when sending a single chat message

    // sync methods
    boolean isBlack(long uid, long buid);
    boolean isBlack(long uid, long buid, int timeoutInseconds);
    
    // async methods
    void isBlack(long uid, long buid, IsBlackLambdaCallBack callback);
    void isBlack(long uid, long buid, IsBlackLambdaCallBack callback, int timeoutInseconds);
 
Parameter Description:   

* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout), if the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5 seconds.

* `IsBlackLambdaCallBack callback`: Return interface for asynchronous callback, return result, error code and error message will be returned through callback
        
        public interface IsBlackLambdaCallBack{
            void done(boolean ok, int errorCode, String errorMessage);
        }

return value:
       
* **sync**: The synchronization interface returns boolean when it is normal, and throws an RTMException or other systemic exception when an error is returned. For RTMException exceptions, you can view the error information through the toString method.

* **async**: The asynchronous interface does not throw an exception, and returns a boolean through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it is returned as an error. You can view the message error information.

### Judging whether the uid is blocked by the user in the buids, it is used when sending multi-person chat messages

    // sync methods
    Set<Long> isBlacks(long uid, Set<Long> buids);
    Set<Long> isBlacks(long uid, Set<Long> buids, int timeoutInseconds);
    
    // async methods
    void isBlacks(long uid, Set<Long> buids, IsBlacksLambdaCallBack callback);
    void isBlacks(long uid, Set<Long> buids, IsBlacksLambdaCallBack callback, int timeoutInseconds);

Parameter Description:   
 
* `Set<Long> buids`: At most 100 people will be judged each time.
 
* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout), if the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.
 
* `IsBlacksLambdaCallBack callback`: Return interface for asynchronous callback, error code and error message will be returned through callback
         
        public interface IsBlacksLambdaCallBack{
            void done(Set<Long> buids, int errorCode, String errorMessage);
        }
 
return value:
        
* **sync**: The set returned when the synchronization interface is normal is the people who black out the uid. These people cannot receive the message sent by the uid. When the error returns, an exception RTMException or other systemic exceptions will be thrown. For RTMException exceptions, you can view the error information through the toString method.

* **async**: The asynchronous interface will not throw an exception. The buids returned by the callback are the people who blocked the uid. These people cannot receive the message sent by the uid. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), the error is returned. Check the message error information.   
          