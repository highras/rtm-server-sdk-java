# RTM Server Java SDK Group API Docs

# Index

[TOC]

### -------------------------Group relationship and group operation interface--------------------------

### Add group members

    // sync methods
    void addGroupMembers(long groupId, Set<Long> uids);
    void addGroupMembers(long groupId, Set<Long> uids, int timeoutInseconds);
    
    // async methods
    void addGroupMembers(long groupId, Set<Long> uids, DoneLambdaCallback callback);
    void addGroupMembers(long groupId, Set<Long> uids, DoneLambdaCallback callback, int timeoutInseconds);
    
Parameter Description:  
 
* `Set<Long> uids`: **Add up to 100 people** each time.

* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.

* `DoneLambdaCallback callback`: return interface for asynchronous callback, error code and error message will be returned through callback
        
        public interface DoneLambdaCallback {
            void done(int errorCode, String message);
        }

return value:       

* **sync**: When the synchronization interface is normal, it returns empty. When the error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.

* **async**: The asynchronous interface will not throw an exception, and the interface call result will be returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it will be returned as an error. You can view the message error information.

### Delete group members

    // sync methods
    void delGroupMembers(long groupId, Set<Long> uids)
    void delGroupMembers(long groupId, Set<Long> uids, int timeoutInseconds);
    
    // async methods
    void delGroupMembers(long groupId, Set<Long> uids, DoneLambdaCallback callback);
    void delGroupMembers(long groupId, Set<Long> uids, DoneLambdaCallback callback, int timeoutInseconds);
    
Parameter Description:  
 
* `Set<Long> uids`: **Delete up to 100 people** each time.
 
* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.
 
* `DoneLambdaCallback callback`: return interface for asynchronous callback, error code and error message will be returned through callback
         
        public interface DoneLambdaCallback {
            void done(int errorCode, String message);
        }
 
return value:       

* **sync**: When the synchronization interface is normal, it returns empty. When the error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.
 
* **async**: The asynchronous interface will not throw an exception, and the interface call result will be returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it will be returned as an error. You can view the message error information.
 
### Delete group
 
    // sync methods
    void delGroup(long groupId);
    void delGroup(long groupId, int timeoutInseconds);
    
    // async methods
    void delGroup(long groupId, DoneLambdaCallback callback);
    void delGroup(long groupId, DoneLambdaCallback callback, int timeoutInseconds);
    
Parameter Description:   
  
* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.
  
* `DoneLambdaCallback callback`: return interface for asynchronous callback, error code and error message will be returned through callback
          
        public interface DoneLambdaCallback {
            void done(int errorCode, String message);
        }
  
return value:       
  
* **sync**: When the synchronization interface is normal, it returns empty. When the error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.
  
* **async**: The asynchronous interface will not throw an exception, and the interface call result will be returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it will be returned as an error. You can view the message error information.
  
### Get group members
  
    // sync methods
    Set<Long> getGroupMembers(long groupId);
    Set<Long> getGroupMembers(long groupId, int timeoutInseconds);
    
    //async methods
    void getGroupMembers(long groupId, GetGroupMembersLambdaCallBack callback);
    void getGroupMembers(long groupId, GetGroupMembersLambdaCallBack callback, int timeoutInseconds);
    
Parameter Description:   

* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.

* `GetGroupMembersLambdaCallBack callback`: return interface for asynchronous callback, return result, error code and error information will be returned through callback
        
        public interface GetGroupMembersLambdaCallBack{
            void done(Set<Long> uids, int errorCode, String errorMessage);
        }

return value: 
      
* **sync**: The member list of groupId is returned when the synchronization interface is normal, and RTMException or other systemic exceptions will be thrown when an error is returned. For RTMException exceptions, you can view the error information through the toString method.

* **async**: The asynchronous interface will not throw an exception. The groupId member list is returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it is returned as an error. You can view the message error information.

### Are group members

    // sync methods
    boolean isGroupMember(long uid, long groupId);
    boolean isGroupMember(long uid, long groupId, int timeoutInseconds);
    
    // async methods
    void isGroupMember(long uid, long groupId, IsGroupMemberCallBack callback);
    void isGroupMember(long uid, long groupId, IsGroupMemberCallBack callback, int timeoutInseconds);
    
Parameter Description:   
 
* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.
 
* `GetGroupMembersLambdaCallBack callback`: return interface for asynchronous callback, return result, error code and error information will be returned through callback
         
        public interface IsGroupMemberCallBack{
            void done(boolean ok, int errorCode, String errorMessage);
        }
 
return value:
       
* **sync**: The synchronization interface returns boolean when it is normal, and RTMException or other systemic exceptions will be thrown when the error returns. For RTMException exceptions, you can view the error information through the toString method.
 
* **async**: The asynchronous interface does not throw an exception, and returns a boolean through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it is returned as an error. You can view the message error information.
 
### Get groups joined by users
 
    // sync methods
    Set<Long> getUserGroups(long uid);
    Set<Long> getUserGroups(long uid, int timeoutInseconds);
    
    // async methods
    void getUserGroups(long uid, GetUserGroupsCallBack callback);
    void getUserGroups(long uid, GetUserGroupsCallBack callback, int timeoutInseconds);
    
Parameter Description:   
   
* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.
   
* `GetUserGroupsCallBack callback`: return interface for asynchronous callback, the return result, error code and error message will be returned through callback
           
        public interface GetUserGroupsCallBack{
            void done(Set<Long> groupIds, int errorCode, String errorMessage);
        }
   
return value:
       
* **sync**: When the synchronization interface is normal, it returns the list of groups joined by the uid. When the error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.
   
* **async**: The asynchronous interface will not throw an exception, and the group list that the uid has joined is returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it is returned as an error. You can view the message error information.

### Forbid users to speak in the specified group

    // sync methods
    void addGroupBan(long groupId, long uid, int btime);
    void addGroupBan(long groupId, long uid, int btime, int timeoutInseconds);
    
    // async methods
    void addGroupBan(long groupId, long uid, int btime, DoneLambdaCallback callback);
    void addGroupBan(long groupId, long uid, int btime, DoneLambdaCallback callback, int timeoutInseconds);
    
Parameter Description:  

* `long groupId`: If **groupId <= 0**, all groups are muted

* `int btime`: The duration of the mute, starting from the current time, in seconds
  
* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.
  
* `DoneLambdaCallback callback`: return interface for asynchronous callback, error code and error message will be returned through callback
          
        public interface DoneLambdaCallback {
            void done(int errorCode, String message);
        }
  
return value:       
  
* **sync**: When the synchronization interface is normal, it returns empty. When the error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.
  
* **async**: The asynchronous interface will not throw an exception, and the interface call result will be returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it will be returned as an error. You can view the message error information.

### Unblock the user-specified group

    // sync methods
    void removeGroupBan(long groupId, long uid);
    void removeGroupBan(long groupId, long uid, int timeoutInseconds);
    
    // async methods
    void removeGroupBan(long groupId, long uid, DoneLambdaCallback callback);
    void removeGroupBan(long groupId, long uid, DoneLambdaCallback callback, int timeoutInseconds);
    
Parameter Description:  

* `long groupId`: If **groupId <= 0**, all groups are unblocked

* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.
  
* `DoneLambdaCallback callback`: return interface for asynchronous callback, error code and error message will be returned through callback
          
        public interface DoneLambdaCallback {
            void done(int errorCode, String message);
        }
  
return value:       
  
* **sync**: When the synchronization interface is normal, it returns empty. When the error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.
  
* **async**: The asynchronous interface will not throw an exception, and the interface call result will be returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it will be returned as an error. You can view the message error information.

### Determine whether the user is banned in the specified group

    // sync methods
    boolean isBanOfGroup(long uid, long groupId);
    boolean isBanOfGroup(long uid, long groupId, int timeoutInseconds);
    
    // async methods
    void isBanOfGroup(long uid, long groupId, IsBanOfGroupCallBack callback);
    void isBanOfGroup(long uid, long groupId, IsBanOfGroupCallBack callback, int timeoutInseconds);
    
Parameter Description:  
  
* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.
  
* `IsBanOfGroupCallBack callback`: return interface for asynchronous callback, error code and error message will be returned through callback
          
        public interface IsBanOfGroupCallBack{
            void done(boolean ok, int errorCode, String errorMessage);
        }
  
return value:       
  
* **sync**: The synchronization interface returns boolean when it is normal, and RTMException or other systemic exceptions will be thrown when the error returns. For RTMException exceptions, you can view the error information through the toString method.
  
* **async**: The asynchronous interface does not throw an exception, and returns a boolean through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it is returned as an error. You can view the message error information.

### Set group public and private information

    // sync methods
    void setGroupInfo(long groupId, String openInfo, String priInfo);
    void setGroupInfo(long groupId, String openInfo, String priInfo, int timeoutInseconds);
    
    // async methods
    void setGroupInfo(long groupId, String openInfo, String priInfo, DoneLambdaCallback callback);
    void setGroupInfo(long groupId, String openInfo, String priInfo, DoneLambdaCallback callback, int timeoutInseconds);
    
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

### Get group public and private information

    // sync methods
    void getGroupInfo(long groupId, StringBuffer openInfo, StringBuffer priInfo);
    void getGroupInfo(long groupId, StringBuffer openInfo, StringBuffer priInfo, int timeoutInseconds);
    
    // async methods
    void getGroupInfo(long groupId, GetGroupInfoCallback callback);
    void getGroupInfo(long groupId, GetGroupInfoCallback callback, int timeoutInseconds);
    
Parameter Description:  

* `StringBuffer openInfo`: Returns the public information of groupId.

* `StringBuffer priInfo`: Returns the private information of groupId.
  
* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.
  
* `GetGroupInfoCallback callback`: return interface for asynchronous callback, the call result, error code and error information will be returned through callback
          
        public interface GetGroupInfoCallback{
            void done(String openInfo, String priInfo, int errorCode, String errorMessage);
        }
  
return value:       
  
* **sync**: The synchronization interface returns empty when it is normal, and the public and private information of the groupId is returned through parameter return. When an error is returned, an exception RTMException or other systemic exceptions will be thrown. For RTMException exceptions, you can view the error through the toString method information.
  
* **async**: The asynchronous interface will not throw an exception. The public and private information of the groupId is returned through the callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), the error is returned. You can view the message error information.