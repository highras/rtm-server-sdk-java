# RTM Server Java SDK RealTimeVoice API Docs

# Index

[TOC]

### -------------------------Real-time voice related interface------------------ --------

### Invite users to join the voice room

    // sync methods
    void inviteUserIntoVoiceRoom(long rid, Set<Long> toUids, long fromUid);
    void inviteUserIntoVoiceRoom(long rid, Set<Long> toUids, long fromUid, int timeoutInseconds);
    
    // async methods
    void inviteUserIntoVoiceRoom(long rid, Set<Long> toUids, long fromUid, DoneLambdaCallback callback);
    void inviteUserIntoVoiceRoom(long rid, Set<Long> toUids, long fromUid, DoneLambdaCallback callback, int timeoutInseconds);
    
Parameter Description: 
  
* `long fromUid`: The user who sent the invitation (must be in the voice room to initiate the invitation command).

* `Set<Long> toUids`: List of invited users

* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call
The timeout time set by client.setQuestTimeout(int timeout), if the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.

* `DoneLambdaCallback callback`: return interface for asynchronous callback, error code and error message will be returned through callback
        
        public interface DoneLambdaCallback {
            void done(int errorCode, String message);
        }

return value:   
    
* **sync**: When the synchronization interface is normal, it returns empty. When the error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.

* **async**: The asynchronous interface will not throw an exception, and the interface call result will be returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it will be returned as an error. You can view the message error information.

### Force the user to enter the voice room (the room will be automatically created if the room does not exist)

    // sync methods
    void pullIntoVoiceRoom(long rid, Set<Long> toUids);
    void pullIntoVoiceRoom(long rid, Set<Long> toUids, int timeoutInseconds);
    
    // async methods
    void pullIntoVoiceRoom(long rid, Set<Long> toUids, DoneLambdaCallback callback);
    void pullIntoVoiceRoom(long rid, Set<Long> toUids, DoneLambdaCallback callback, int timeoutInseconds);
    
Parameter Description:   
 
* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call
 The timeout time set by client.setQuestTimeout(int timeout), if the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.
 
* `DoneLambdaCallback callback`: return interface for asynchronous callback, error code and error message will be returned through callback
         
         public interface DoneLambdaCallback {
             void done(int errorCode, String message);
         }
 
return value:      
  
* **sync**: When the synchronization interface is normal, it returns empty. When the error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.
 
* **async**: The asynchronous interface will not throw an exception, and the interface call result will be returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it will be returned as an error. You can view the message error information.

### Close the voice room

    // sync methods
    void closeVoiceRoom(long rid);
    void closeVoiceRoom(long rid, int timeoutInseconds);
    
    // async methods
    void closeVoiceRoom(long rid, DoneLambdaCallback callback);
    void closeVoiceRoom(long rid, DoneLambdaCallback callback, int timeoutInseconds);
    
Parameter Description:   

* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call
The timeout time set by client.setQuestTimeout(int timeout), if the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.

* `DoneLambdaCallback callback`: return interface for asynchronous callback, error code and error message will be returned through callback
         
         public interface DoneLambdaCallback {
             void done(int errorCode, String message);
         }

return value:      
 
* **sync**: When the synchronization interface is normal, it returns empty. When the error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.
 
* **async**: The asynchronous interface will not throw an exception, and the interface call result will be returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it will be returned as an error. You can view the message error information.

### Kick out from the voice room

    // sync methods
    void kickoutFromVoiceRoom(long rid, long uid, long fromUid);
    void kickoutFromVoiceRoom(long rid, long uid, long fromUid, int timeoutInseconds);
    
    // async methods
    void kickoutFromVoiceRoom(long rid, long uid, long fromUid, DoneLambdaCallback callback);
    void kickoutFromVoiceRoom(long rid, long uid, long fromUid, DoneLambdaCallback callback, int timeoutInseconds);
 
Parameter Description:  

* `long uid`: the user who was kicked

* `long fromUid`: the user who initiated the kick command

* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call
The timeout time set by client.setQuestTimeout(int timeout), if the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.

* `DoneLambdaCallback callback`: return interface for asynchronous callback, error code and error message will be returned through callback
         
         public interface DoneLambdaCallback {
             void done(int errorCode, String message);
         }

return value:
       
* **sync**: When the synchronization interface is normal, it returns empty. When the error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.
 
* **async**: The asynchronous interface will not throw an exception, and the interface call result will be returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it will be returned as an error. You can view the message error information.

### Set the default microphone state of the voice room (whether the microphone is turned on by default when the user enters the room)

    // sync methods
    void setVoiceRoomMicStatus(long rid, boolean status);
    void setVoiceRoomMicStatus(long rid, boolean status, int timeoutInseconds);
    
    // async methods
    void setVoiceRoomMicStatus(long rid, boolean status, DoneLambdaCallback callback);
    void setVoiceRoomMicStatus(long rid, boolean status, DoneLambdaCallback callback, int timeoutInseconds);

Parameter Description:   
 
* `Set<Long> buids`: **At most 100 judges** each time.
 
* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call
 The timeout time set by client.setQuestTimeout(int timeout), if the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.
 
* `DoneLambdaCallback callback`: return interface for asynchronous callback, error code and error message will be returned through callback
         
         public interface DoneLambdaCallback {
             void done(int errorCode, String message);
         }
 
return value:
        
* **sync**: When the synchronization interface is normal, it returns empty. When the error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.
 
* **async**: The asynchronous interface will not throw an exception, and the interface call result will be returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it will be returned as an error. You can view the message error information.

### Get the current voice room id list of the project

    // sync methods
    Set<Long> getVoiceRoomList();
    Set<Long> getVoiceRoomList(int timeoutInseconds);
    
    // async methods
    void getVoiceRoomList(GetVoiceRoomListLambdaCallBack callBack);
    void getVoiceRoomList(GetVoiceRoomListLambdaCallBack callBack, int timeoutInseconds);

Parameter Description:   
 
* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call
 The timeout time set by client.setQuestTimeout(int timeout), if the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.
 
* `GetVoiceRoomListLambdaCallBack callback`: return interface for asynchronous callback, error code and error message will be returned through callback
         
         public interface GetVoiceRoomListLambdaCallBack{
            void done(Set<Long> rids, int errorCode, String errorMessage);
         }
 
return value:
        
* **sync**: When the synchronization interface is normal, it returns the room list. When an error is returned, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.
 
* **async**: The asynchronous interface will not throw an exception, and the interface call result will be returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it will be returned as an error. You can view the message error information.

### Get the current user id list and administrator id list of the voice room

    // sync methods
    void getVoiceRoomMembers(long rid, Set<Long> uids, Set<Long> managers);
    void getVoiceRoomMembers(long rid, Set<Long> uids, Set<Long> managers, int timeoutInseconds);
    
    // async methods
    void getVoiceRoomMembers(long rid, GetVoiceRoomMembersLambdaCallBack callBack);
    void getVoiceRoomMembers(long rid, GetVoiceRoomMembersLambdaCallBack callBack, int timeoutInseconds);

Parameter Description:   
 
* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call
 The timeout time set by client.setQuestTimeout(int timeout), if the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.
 
* `GetVoiceRoomMembersLambdaCallBack callback`: return interface for asynchronous callback, error code and error message will be returned through callback
         
        public interface GetVoiceRoomMembersLambdaCallBack{
            void done(Set<Long> uids, Set<Long> managers, int errorCode, String errorMessage);
        }
 
return value:
        
* **sync**: When the synchronization interface is normal, the room user list and the administrator user list are returned through parameters. When an error is returned, an RTMException or other systemic exception will be thrown. For RTMException exceptions, you can view the error information through the toString method.
 
* **async**: The asynchronous interface will not throw an exception, and the interface call result will be returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it will be returned as an error. You can view the message error information.

### Get the current number of voice room

    // sync methods
    int getVoiceRoomMemberCount(long rid);
    int getVoiceRoomMemberCount(long rid, int timeoutInseconds);
    
    // async methods
    void getVoiceRoomMemberCount(long rid, GetVoiceRoomMemberCountLambdaCallBack callBack);
    void getVoiceRoomMemberCount(long rid, GetVoiceRoomMemberCountLambdaCallBack callBack, int timeoutInseconds);

Parameter Description:   
 
* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call
 The timeout time set by client.setQuestTimeout(int timeout), if the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.
 
* `GetVoiceRoomMemberCountLambdaCallBack callback`: return interface for asynchronous callback, error code and error information will be returned through callback
         
        public interface GetVoiceRoomMemberCountLambdaCallBack{
            void done(int count, int errorCode, String errorMessage);
        }
 
return value:
        
* **sync**: The number of people in the room is returned when the synchronization interface is normal, and RTMException or other systemic exceptions will be thrown when an error is returned. For RTMException exceptions, you can view the error information through the toString method.
 
* **async**: The asynchronous interface will not throw an exception, and the interface call result will be returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it will be returned as an error. You can view the message error information.
          