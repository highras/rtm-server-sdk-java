# RTM Server Java SDK RealTimeVoice API Docs

# Index

[TOC]

### -------------------------实时语音相关接口--------------------------

### 邀请用户加入语音房间

    // sync methods
    void inviteUserIntoVoiceRoom(long rid, Set<Long> toUids, long fromUid);
    void inviteUserIntoVoiceRoom(long rid, Set<Long> toUids, long fromUid, int timeoutInseconds);
    
    // async methods
    void inviteUserIntoVoiceRoom(long rid, Set<Long> toUids, long fromUid, DoneLambdaCallback callback);
    void inviteUserIntoVoiceRoom(long rid, Set<Long> toUids, long fromUid, DoneLambdaCallback callback, int timeoutInseconds);
    
参数说明: 
  
* `long fromUid`: 发送邀请的用户(必须在语音房间里才能发起邀请指令).

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

### 强拉用户进入语音房间(房间不存在会自动创建该房间)

    // sync methods
    void pullIntoVoiceRoom(long rid, Set<Long> toUids);
    void pullIntoVoiceRoom(long rid, Set<Long> toUids, int timeoutInseconds);
    
    // async methods
    void pullIntoVoiceRoom(long rid, Set<Long> toUids, DoneLambdaCallback callback);
    void pullIntoVoiceRoom(long rid, Set<Long> toUids, DoneLambdaCallback callback, int timeoutInseconds);
    
参数说明:   
 
* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
 client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.
 
* `DoneLambdaCallback callback`: 为异步回调返回接口, 错误码和错误信息将通过callback返回
         
         public interface DoneLambdaCallback {
             void done(int errorCode, String message);
         }
 
返回值:      
  
* **sync**: 同步接口正常时返回空，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.
 
* **async**: 异步接口不会抛出异常，通过callback返回接口调用结果，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息.      

### 关闭语音房间

    // sync methods
    void closeVoiceRoom(long rid);
    void closeVoiceRoom(long rid,  int timeoutInseconds);
    
    // async methods
    void closeVoiceRoom(long rid, DoneLambdaCallback callback);
    void closeVoiceRoom(long rid, DoneLambdaCallback callback, int timeoutInseconds);
    
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

### 从语音房间踢出

    // sync methods
    void kickoutFromVoiceRoom(long rid, long uid, long fromUid);
    void kickoutFromVoiceRoom(long rid, long uid, long fromUid, int timeoutInseconds);
    
    // async methods
    void kickoutFromVoiceRoom(long rid, long uid, long fromUid, DoneLambdaCallback callback);
    void kickoutFromVoiceRoom(long rid, long uid, long fromUid, DoneLambdaCallback callback, int timeoutInseconds);
 
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

### 设置语音房间默认麦克风状态(用户刚进入房间默认是否开启麦克风)

    // sync methods
    void setVoiceRoomMicStatus(long rid, boolean status);
    void setVoiceRoomMicStatus(long rid, boolean status, int timeoutInseconds);
    
    // async methods
    void setVoiceRoomMicStatus(long rid, boolean status, DoneLambdaCallback callback);
    void setVoiceRoomMicStatus(long rid, boolean status, DoneLambdaCallback callback, int timeoutInseconds);

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

### 获取该项目当前的语音房间id列表

    // sync methods
    Set<Long> getVoiceRoomList();
    Set<Long> getVoiceRoomList(int timeoutInseconds);
    
    // async methods
    void getVoiceRoomList(GetVoiceRoomListLambdaCallBack callBack);
    void getVoiceRoomList(GetVoiceRoomListLambdaCallBack callBack, int timeoutInseconds);

参数说明:   
 
* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
 client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.
 
* `GetVoiceRoomListLambdaCallBack callback`: 为异步回调返回接口, 错误码和错误信息将通过callback返回
         
         public interface GetVoiceRoomListLambdaCallBack{
            void done(Set<Long> rids, int errorCode, String errorMessage);
         }
 
返回值:
        
* **sync**: 同步接口正常时返回房间列表，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.
 
* **async**: 异步接口不会抛出异常，通过callback返回接口调用结果，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息.

### 获取语音房间当前的用户id列表以及管理员id列表

    // sync methods
    void getVoiceRoomMembers(long rid, Set<Long> uids, Set<Long> managers);
    void getVoiceRoomMembers(long rid, Set<Long> uids, Set<Long> managers, int timeoutInseconds);
    
    // async methods
    void getVoiceRoomMembers(long rid, GetVoiceRoomMembersLambdaCallBack callBack);
    void getVoiceRoomMembers(long rid, GetVoiceRoomMembersLambdaCallBack callBack, int timeoutInseconds);

参数说明:   
 
* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
 client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.
 
* `GetVoiceRoomMembersLambdaCallBack callback`: 为异步回调返回接口, 错误码和错误信息将通过callback返回
         
        public interface GetVoiceRoomMembersLambdaCallBack{
            void done(Set<Long> uids, Set<Long> managers, int errorCode, String errorMessage);
        }
 
返回值:
        
* **sync**: 同步接口正常时通过参数回传房间用户列表和管理员用户列表，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.
 
* **async**: 异步接口不会抛出异常，通过callback返回接口调用结果，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息.

### 获取当前语音房间人数

    // sync methods
    int getVoiceRoomMemberCount(long rid);
    int getVoiceRoomMemberCount(long rid, int timeoutInseconds);
    
    // async methods
    void getVoiceRoomMemberCount(long rid, GetVoiceRoomMemberCountLambdaCallBack callBack);
    void getVoiceRoomMemberCount(long rid, GetVoiceRoomMemberCountLambdaCallBack callBack, int timeoutInseconds);

参数说明:   
 
* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
 client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.
 
* `GetVoiceRoomMemberCountLambdaCallBack callback`: 为异步回调返回接口, 错误码和错误信息将通过callback返回
         
        public interface GetVoiceRoomMemberCountLambdaCallBack{
            void done(int count, int errorCode, String errorMessage);
        }
 
返回值:
        
* **sync**: 同步接口正常时返回房间人数，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.
 
* **async**: 异步接口不会抛出异常，通过callback返回接口调用结果，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息.
          