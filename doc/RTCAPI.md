# RTM Server Java SDK RTC API Docs

# Index

[TOC]

### -------------------------实时音视频相关接口--------------------------

### 邀请用户加入RTC房间

    // sync methods
    void inviteUserIntoRTCRoom(long rid, Set<Long> toUids, long fromUid);
    void inviteUserIntoRTCRoom(long rid, Set<Long> toUids, long fromUid, int timeoutInseconds);
    
    // async methods
    void inviteUserIntoRTCRoom(long rid, Set<Long> toUids, long fromUid, DoneLambdaCallback callback);
    void inviteUserIntoRTCRoom(long rid, Set<Long> toUids, long fromUid, DoneLambdaCallback callback, int timeoutInseconds);
    
参数说明: 
  
* `long fromUid`: 发送邀请的用户(必须在RTC房间里才能发起邀请指令).

* `Set<Long> toUids`: 被邀请的用户列表

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `DoneLambdaCallback callback`: 为异步回调返回接口, 错误码和错误信息将通过callback返回
        
        public interface DoneLambdaCallback {
            void done(int errorCode, String message);
        }

返回值:   
    
* **sync**: 同步接口正常时返回空，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回接口调用结果，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息. 

### 强拉用户进入RTC房间(房间不存在会自动创建该房间)

    // sync methods
    void pullIntoRTCRoom(long rid, Set<Long> toUids, int type );
    void pullIntoRTCRoom(long rid, Set<Long> toUids, int type, int timeoutInseconds);
    
    // async methods
    void pullIntoRTCRoom(long rid, Set<Long> toUids, int type, DoneLambdaCallback callback);
    void pullIntoRTCRoom(long rid, Set<Long> toUids, int type, DoneLambdaCallback callback, int timeoutInseconds);
    
参数说明:   

* `int type`: 房间类型，1 语音房间 2 视频房间

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
 client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.
 
* `DoneLambdaCallback callback`: 为异步回调返回接口, 错误码和错误信息将通过callback返回
         
         public interface DoneLambdaCallback {
             void done(int errorCode, String message);
         }
 
返回值:      
  
* **sync**: 同步接口正常时返回空，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.
 
* **async**: 异步接口不会抛出异常，通过callback返回接口调用结果，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息.      

### 关闭RTC房间

    // sync methods
    void closeRTCRoom(long rid);
    void closeRTCRoom(long rid,  int timeoutInseconds);
    
    // async methods
    void closeRTCRoom(long rid, DoneLambdaCallback callback);
    void closeRTCRoom(long rid, DoneLambdaCallback callback, int timeoutInseconds);
    
参数说明：   

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `DoneLambdaCallback callback`: 为异步回调返回接口, 错误码和错误信息将通过callback返回
         
         public interface DoneLambdaCallback {
             void done(int errorCode, String message);
         }

返回值:      
 
* **sync**: 同步接口正常时返回空，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.
 
* **async**: 异步接口不会抛出异常，通过callback返回接口调用结果，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息. 

### 从RTC房间踢出

    // sync methods
    void kickoutFromRTCRoom(long rid, long uid, long fromUid);
    void kickoutFromRTCRoom(long rid, long uid, long fromUid, int timeoutInseconds);
    
    // async methods
    void kickoutFromRTCRoom(long rid, long uid, long fromUid, DoneLambdaCallback callback);
    void kickoutFromRTCRoom(long rid, long uid, long fromUid, DoneLambdaCallback callback, int timeoutInseconds);
 
参数说明：  

* `long uid` : 被踢的用户

* `long fromUid`: 发起踢人指令的用户

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `DoneLambdaCallback callback`: 为异步回调返回接口, 错误码和错误信息将通过callback返回
         
         public interface DoneLambdaCallback {
             void done(int errorCode, String message);
         }

返回值:
       
* **sync**: 同步接口正常时返回空，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.
 
* **async**: 异步接口不会抛出异常，通过callback返回接口调用结果，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息. 

### 设置RTC房间默认麦克风状态(用户刚进入房间默认是否开启麦克风)

    // sync methods
    void setRTCRoomMicStatus(long rid, boolean status);
    void setRTCRoomMicStatus(long rid, boolean status, int timeoutInseconds);
    
    // async methods
    void setRTCRoomMicStatus(long rid, boolean status, DoneLambdaCallback callback);
    void setRTCRoomMicStatus(long rid, boolean status, DoneLambdaCallback callback, int timeoutInseconds);

参数说明:   
 
* `Set<Long> buids`: 每次**最多判断100人**.
 
* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
 client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.
 
* `DoneLambdaCallback callback`: 为异步回调返回接口, 错误码和错误信息将通过callback返回
         
         public interface DoneLambdaCallback {
             void done(int errorCode, String message);
         }
 
返回值:
        
* **sync**: 同步接口正常时返回空，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.
 
* **async**: 异步接口不会抛出异常，通过callback返回接口调用结果，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息. 

### 获取该项目当前的RTC房间id列表

    // sync methods
    Set<Long> getRTCRoomList();
    Set<Long> getRTCRoomList(int timeoutInseconds);
    
    // async methods
    void getRTCRoomList(GetRTCRoomListLambdaCallBack callBack);
    void getRTCRoomList(GetRTCRoomListLambdaCallBack callBack, int timeoutInseconds);

参数说明:   
 
* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
 client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.
 
* `GetRTCRoomListLambdaCallBack callback`: 为异步回调返回接口, 错误码和错误信息将通过callback返回
         
         public interface GetRTCRoomListLambdaCallBack{
            void done(Set<Long> rids, int errorCode, String errorMessage);
         }
 
返回值:
        
* **sync**: 同步接口正常时返回房间列表，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.
 
* **async**: 异步接口不会抛出异常，通过callback返回接口调用结果，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息.

### 获取RTC房间当前的用户id列表以及管理员id列表

    // sync methods
    long getRTCRoomMembers(long rid, Set<Long> uids, Set<Long> managers);
    long getRTCRoomMembers(long rid, Set<Long> uids, Set<Long> managers, int timeoutInseconds);
    
    // async methods
    void getRTCRoomMembers(long rid, GetRTCRoomMembersLambdaCallBack callBack);
    void getRTCRoomMembers(long rid, GetRTCRoomMembersLambdaCallBack callBack, int timeoutInseconds);

参数说明:   
 
* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
 client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.
 
* `GetRTCRoomMembersLambdaCallBack callback`: 为异步回调返回接口, 错误码和错误信息将通过callback返回
         
        public interface GetRTCRoomMembersLambdaCallBack{
            void done(Set<Long> uids, Set<Long> managers, long owner, int errorCode, String errorMessage);
        }
 
返回值:
        
* **sync**: 同步接口正常时通过参数回传房间用户列表和管理员用户列表，通过方法返回房主id，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.
 
* **async**: 异步接口不会抛出异常，通过callback返回接口调用结果，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息.

### 获取当前RTC房间人数

    // sync methods
    int getRTCRoomMemberCount(long rid);
    int getRTCRoomMemberCount(long rid, int timeoutInseconds);
    
    // async methods
    void getRTCRoomMemberCount(long rid, GetRTCRoomMemberCountLambdaCallBack callBack);
    void getRTCRoomMemberCount(long rid, GetRTCRoomMemberCountLambdaCallBack callBack, int timeoutInseconds);

参数说明:   
 
* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
 client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.
 
* `GetRTCRoomMemberCountLambdaCallBack callback`: 为异步回调返回接口, 错误码和错误信息将通过callback返回
         
        public interface GetRTCRoomMemberCountLambdaCallBack{
            void done(int count, int errorCode, String errorMessage);
        }
 
返回值:
        
* **sync**: 同步接口正常时返回房间人数，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.
 
* **async**: 异步接口不会抛出异常，通过callback返回接口调用结果，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息.

### 房间管理员操作指令

    // sync methods
    void adminCommand(long rid, Set<Long> uids, int command);
    void adminCommand(long rid, Set<Long> uids, int command, int timeoutInseconds);
    
    // async methods
    void adminCommand(long rid, Set<Long> uids, int command, DoneLambdaCallback callback);
    void adminCommand(long rid, Set<Long> uids, int command, DoneLambdaCallback callback, int timeoutInseconds);

参数说明:   
 
* `Set<Long> uids`: 管理员操作的用户id列表.

* `int command`: 管理员操作的指令： 0 赋予管理员权限，1 剥夺管理员权限，2 禁止发送音频数据，3 允许发送音频数据，4 禁止发送视频数据，5 允许发送视频数据，6 关闭他人麦克风，7 关闭他人摄像头
 
* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
 client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.
 
* `DoneLambdaCallback callback`: 为异步回调返回接口, 错误码和错误信息将通过callback返回
         
         public interface DoneLambdaCallback {
             void done(int errorCode, String message);
         }
 
返回值:
        
* **sync**: 同步接口正常时返回空，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.
 
* **async**: 异步接口不会抛出异常，通过callback返回接口调用结果，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息. 

          