# RTM Server Java SDK RealTimeRTC API Docs

# Index

[TOC]

### -------------------------Real-time Communication related interface------------------ --------

### Invite users to join the RTC room

    // sync methods
    void inviteUserIntoRTCRoom(long rid, Set<Long> toUids, long fromUid);
    void inviteUserIntoRTCRoom(long rid, Set<Long> toUids, long fromUid, int timeoutInseconds);
    
    // async methods
    void inviteUserIntoRTCRoom(long rid, Set<Long> toUids, long fromUid, DoneLambdaCallback callback);
    void inviteUserIntoRTCRoom(long rid, Set<Long> toUids, long fromUid, DoneLambdaCallback callback, int timeoutInseconds);
    
Parameter Description: 
  
* `long fromUid`: The user who sent the invitation (must be in the RTC room to initiate the invitation command).

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

### Force the user to enter the RTC room (the room will be automatically created if the room does not exist)

    // sync methods
    void pullIntoRTCRoom(long rid, Set<Long> toUids, int type);
    void pullIntoRTCRoom(long rid, Set<Long> toUids, int type, int timeoutInseconds);
    
    // async methods
    void pullIntoRTCRoom(long rid, Set<Long> toUids, int type, DoneLambdaCallback callback);
    void pullIntoRTCRoom(long rid, Set<Long> toUids, int type, DoneLambdaCallback callback, int timeoutInseconds);
    
Parameter Description:   

* `int type`: room type ，1 voice room 2 video room

* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call
 The timeout time set by client.setQuestTimeout(int timeout), if the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.
 
* `DoneLambdaCallback callback`: return interface for asynchronous callback, error code and error message will be returned through callback
         
         public interface DoneLambdaCallback {
             void done(int errorCode, String message);
         }
 
return value:      
  
* **sync**: When the synchronization interface is normal, it returns empty. When the error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.
 
* **async**: The asynchronous interface will not throw an exception, and the interface call result will be returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it will be returned as an error. You can view the message error information.

### Close the RTC room

    // sync methods
    void closeRTCRoom(long rid);
    void closeRTCRoom(long rid, int timeoutInseconds);
    
    // async methods
    void closeRTCRoom(long rid, DoneLambdaCallback callback);
    void closeRTCRoom(long rid, DoneLambdaCallback callback, int timeoutInseconds);
    
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

### Kick out from the RTC room

    // sync methods
    void kickoutFromRTCRoom(long rid, long uid, long fromUid);
    void kickoutFromRTCRoom(long rid, long uid, long fromUid, int timeoutInseconds);
    
    // async methods
    void kickoutFromRTCRoom(long rid, long uid, long fromUid, DoneLambdaCallback callback);
    void kickoutFromRTCRoom(long rid, long uid, long fromUid, DoneLambdaCallback callback, int timeoutInseconds);
 
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

### Set the default microphone state of the RTC room (whether the microphone is turned on by default when the user enters the room)

    // sync methods
    void setRTCRoomMicStatus(long rid, boolean status);
    void setRTCRoomMicStatus(long rid, boolean status, int timeoutInseconds);
    
    // async methods
    void setRTCRoomMicStatus(long rid, boolean status, DoneLambdaCallback callback);
    void setRTCRoomMicStatus(long rid, boolean status, DoneLambdaCallback callback, int timeoutInseconds);

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

### Get the current RTC room id list of the project

    // sync methods
    Set<Long> getRTCRoomList();
    Set<Long> getRTCRoomList(int timeoutInseconds);
    
    // async methods
    void getRTCRoomList(GetRTCRoomListLambdaCallBack callBack);
    void getRTCRoomList(GetRTCRoomListLambdaCallBack callBack, int timeoutInseconds);

Parameter Description:   
 
* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call
 The timeout time set by client.setQuestTimeout(int timeout), if the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.
 
* `GetRTCRoomListLambdaCallBack callback`: return interface for asynchronous callback, error code and error message will be returned through callback
         
         public interface GetRTCRoomListLambdaCallBack{
            void done(Set<Long> rids, int errorCode, String errorMessage);
         }
 
return value:
        
* **sync**: When the synchronization interface is normal, it returns the room list. When an error is returned, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.
 
* **async**: The asynchronous interface will not throw an exception, and the interface call result will be returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it will be returned as an error. You can view the message error information.

### Get the current user id list and administrator id list and owner id of the RTC room

    // sync methods
    long getRTCRoomMembers(long rid, Set<Long> uids, Set<Long> managers);
    long getRTCRoomMembers(long rid, Set<Long> uids, Set<Long> managers, int timeoutInseconds);
    
    // async methods
    void getRTCRoomMembers(long rid, GetRTCRoomMembersLambdaCallBack callBack);
    void getRTCRoomMembers(long rid, GetRTCRoomMembersLambdaCallBack callBack, int timeoutInseconds);

Parameter Description:   
 
* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call
 The timeout time set by client.setQuestTimeout(int timeout), if the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.
 
* `GetRTCRoomMembersLambdaCallBack callback`: return interface for asynchronous callback, error code and error message will be returned through callback
         
        public interface GetRTCRoomMembersLambdaCallBack{
            void done(Set<Long> uids, Set<Long> managers, long owner, int errorCode, String errorMessage);
        }
 
return value:
        
* **sync**: When the synchronization interface is normal, the room user list and the administrator user list are returned through parameters,the owner id return by function. When an error is returned, an RTMException or other systemic exception will be thrown. For RTMException exceptions, you can view the error information through the toString method.
 
* **async**: The asynchronous interface will not throw an exception, and the interface call result will be returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it will be returned as an error. You can view the message error information.

### Get the current number of RTC room

    // sync methods
    int getRTCRoomMemberCount(long rid);
    int getRTCRoomMemberCount(long rid, int timeoutInseconds);
    
    // async methods
    void getRTCRoomMemberCount(long rid, GetRTCRoomMemberCountLambdaCallBack callBack);
    void getRTCRoomMemberCount(long rid, GetRTCRoomMemberCountLambdaCallBack callBack, int timeoutInseconds);

Parameter Description:   
 
* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call
 The timeout time set by client.setQuestTimeout(int timeout), if the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.
 
* `GetRTCRoomMemberCountLambdaCallBack callback`: return interface for asynchronous callback, error code and error information will be returned through callback
         
        public interface GetRTCRoomMemberCountLambdaCallBack{
            void done(int count, int errorCode, String errorMessage);
        }
 
return value:
        
* **sync**: The number of people in the room is returned when the synchronization interface is normal, and RTMException or other systemic exceptions will be thrown when an error is returned. For RTMException exceptions, you can view the error information through the toString method.
 
* **async**: The asynchronous interface will not throw an exception, and the interface call result will be returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it will be returned as an error. You can view the message error information.

### Room manager operation instructions

    // sync methods
    void adminCommand(long rid, Set<Long> uids, int command);
    void adminCommand(long rid, Set<Long> uids, int command, int timeoutInseconds);
    
    // async methods
    void adminCommand(long rid, Set<Long> uids, int command, DoneLambdaCallback callback);
    void adminCommand(long rid, Set<Long> uids, int command, DoneLambdaCallback callback, int timeoutInseconds);

Parameter Description:   
 
* `Set<Long> uids`: List of user ids operated by the administrator

* `int command`: Instructions for administrator operations: 0 grant administrator rights, 1 deprive administrator rights, 2 prohibit sending audio data, 3 allow sending audio data, 4 prohibit sending video data,
                 5 allow sending video data, 6 turn off others’ microphones, 7 turn off others webcam
 
* `int timeoutInseconds`: ending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call
                           The timeout time set by client.setQuestTimeout(int timeout), if the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.
 
* `DoneLambdaCallback callback`: return interface for asynchronous callback, error code and error information will be returned through callback
         
         public interface DoneLambdaCallback {
             void done(int errorCode, String message);
         }
 
return value:
        
* **sync**: When the synchronization interface is normal, it returns empty. When the error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.
 
* **async**: The asynchronous interface will not throw an exception, and the interface call result will be returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it will be returned as an error. You can view the message error information.
          