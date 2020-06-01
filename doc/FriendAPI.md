# RTM Server Java SDK Friend API Docs

# Index

[TOC]

### -------------------------好友关系接口--------------------------

### 添加好友

    // sync methods
    void addFriends(long uid, Set<Long> friends);
    void addFriends(long uid, Set<Long> friends, int timeoutInseconds);
    
    // sync methods
    void addFriends(long uid, Set<Long> friends, DoneLambdaCallback callback);
    void addFriends(long uid, Set<Long> friends, DoneLambdaCallback callback, int timeoutInseconds);
    
参数说明: 
  
* `Set<Long> friends`: 每次**最多添加100人**.

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `DoneLambdaCallback callback`: 为异步回调返回接口, 错误码和错误信息将通过callback返回
        
        public interface DoneLambdaCallback {
            void done(int errorCode, String message);
        }

返回值:   
    
* **sync**: 同步接口正常时返回空，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回接口调用结果，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息. 

### 删除好友

    // sync methods
    void delFriends(long uid, Set<Long> friends);
    void delFriends(long uid, Set<Long> friends, int timeoutInseconds);
    
    // async methods
    void delFriends(long uid, Set<Long> friends, DoneLambdaCallback callback);
    void delFriends(long uid, Set<Long> friends, DoneLambdaCallback callback, int timeoutInseconds);
    
参数说明:   
 
* `Set<Long> friends`: 每次**最多删除100人**.
 
* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
 client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.
 
* `DoneLambdaCallback callback`: 为异步回调返回接口, 错误码和错误信息将通过callback返回
         
         public interface DoneLambdaCallback {
             void done(int errorCode, String message);
         }
 
返回值:      
  
* **sync**: 同步接口正常时返回空，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.
 
* **async**: 异步接口不会抛出异常，通过callback返回接口调用结果，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息.      

### 获取好友列表

    // sync methods
    Set<Long> getFriends(long uid);
    Set<Long> getFriends(long uid, int timeoutInseconds);
    
    // async methods
    void getFriends(long uid, GetFriendsLambdaCallBack callback);
    void getFriends(long uid, GetFriendsLambdaCallBack callback, int timeoutInseconds);
    
参数说明：   

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `GetFriendsLambdaCallBack callback`: 为异步回调返回接口, 返回结果以及错误码和错误信息将通过callback返回
        
        public interface GetFriendsLambdaCallBack{
            void done(Set<Long> fuids, int errorCode, String errorMessage);
        }

返回值:      
 
* **sync**: 同步接口正常时返回uid的好友列表，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回uid的好友列表，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息.

### 判断好友关系

    // sync methods
    boolean isFriend(long uid, long fuid);
    boolean isFriend(long uid, long fuid, int timeoutInseconds);
    
    // async methods
    void isFriend(long uid, long fuid, IsFriendLambdaCallBack callback);
    void isFriend(long uid, long fuid, IsFriendLambdaCallBack callback, int timeoutInseconds);
 
参数说明：   

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `IsFriendLambdaCallBack callback`: 为异步回调返回接口, 返回结果以及错误码和错误信息将通过callback返回
        
        public interface IsFriendLambdaCallBack{
            void done(boolean ok, int errorCode, String errorMessage);
        }

返回值:
       
* **sync**: 同步接口正常时返回boolean，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回boolean，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息. 

### 判断多人好友关系

    // sync methods
    Set<Long> isFriends(long uid, Set<Long> fuids);
    Set<Long> isFriends(long uid, Set<Long> fuids, int timeoutInseconds);
    
    // async methods
    void isFriends(long uid, Set<Long> fuids, IsFriendsLambdaCallBack callback);
    void isFriends(long uid, Set<Long> fuids, IsFriendsLambdaCallBack callback, int timeoutInseconds);

参数说明:   
 
* `Set<Long> fuids`: 每次**最多判断100人**.
 
* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
 client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.
 
* `IsFriendsLambdaCallBack callback`: 为异步回调返回接口, 错误码和错误信息将通过callback返回
         
         public interface IsFriendsLambdaCallBack{
            void done(Set<Long> fuids, int errorCode, String errorMessage);
         }
 
返回值:
        
* **sync**: 同步接口正常时返回uid的好友列表，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.
 
* **async**: 异步接口不会抛出异常，通过callback返回uid的好友列表，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息.    
          