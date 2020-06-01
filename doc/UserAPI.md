# RTM Server Java SDK User API Docs

# Index

[TOC]

### ------------------------用户操作相关接口--------------------------

### 踢用户下线

    // sync methods
    void kickOut(long uid);
    void kickOut(long uid, String ce);
    void kickOut(long uid, String ce, int timeoutInseconds);
    
    // async methods
    void kickOut(long uid, DoneLambdaCallback callback);
    void kickOut(long uid, String ce, DoneLambdaCallback callback);
    void kickOut(long uid, String ce, DoneLambdaCallback callback, int timeoutInseconds);
    
参数说明:  

* `String ce`: 当ce不为空时，则只踢掉其中的一个链接，多用户登录情况

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
  client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.
  
* `DoneLambdaCallback callback`: 为异步回调返回接口, 错误码和错误信息将通过callback返回
          
        public interface DoneLambdaCallback {
            void done(int errorCode, String message);
        }
  
返回值:       
  
* **sync**: 同步接口正常时返回空，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.
  
* **async**: 异步接口不会抛出异常，通过callback返回接口调用结果，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息. 

### 获取在线用户

    // sync methods
    Set<Long> getOnlineUsers(Set<Long> uids);
    Set<Long> getOnlineUsers(Set<Long> uids, int timeoutInseconds);
    
    // async methods
    void getOnlineUsers(Set<Long> uids, GetOnlineUsersCallback callback);
    void getOnlineUsers(Set<Long> uids, GetOnlineUsersCallback callback, int timeoutInseconds);
    
参数说明:  

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
  client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.
  
* `GetOnlineUsersCallback callback`: 为异步回调返回接口, 错误码和错误信息将通过callback返回
          
        public interface GetOnlineUsersCallback {
            void done(Set<Long> uids, int errorCode, String errorMessage);
        }
  
返回值:       
  
* **sync**: 同步接口正常时返回uids集合里的在线用户，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.
  
* **async**: 异步接口不会抛出异常，通过callback返回uids集合里的在线用户，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息.

### 将用户加入到项目黑名单

    // sync method
    void addProjectBlack(long uid, int btime);
    void addProjectBlack(long uid, int btime, int timeoutInseconds);
    
    // async methods
    void addProjectBlack(long uid, int btime, DoneLambdaCallback callback);
    void addProjectBlack(long uid, int btime, DoneLambdaCallback callback, int timeoutInseconds);
    
参数说明:  

* `int btime`: 时长，从当前时间开始，以秒计算
  
* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
  client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.
  
* `DoneLambdaCallback callback`: 为异步回调返回接口, 错误码和错误信息将通过callback返回
          
        public interface DoneLambdaCallback {
            void done(int errorCode, String message);
        }
  
返回值:       
  
* **sync**: 同步接口正常时返回空，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.
  
* **async**: 异步接口不会抛出异常，通过callback返回接口调用结果，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息. 

### 将用户移出项目黑名单

    // sync methods
    void removeProjectBlack(long uid);
    void removeProjectBlack(long uid, int timeoutInseconds);
    
    // async methods
    void removeProjectBlack(long uid, DoneLambdaCallback callback);
    void removeProjectBlack(long uid, DoneLambdaCallback callback, int timeoutInseconds);
    
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

### 判断用户是否在项目黑名单中

    // sync methods
    boolean isProjectBlack(long uid);
    boolean isProjectBlack(long uid, int timeoutInseconds);
    
    // async methods
    void isProjectBlack(long uid, IsProjectBlackCallback callback);
    void isProjectBlack(long uid, IsProjectBlackCallback callback, int timeoutInseconds);
    
参数说明:  
  
* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
  client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.
  
* `IsProjectBlackCallback callback`: 为异步回调返回接口, 错误码和错误信息将通过callback返回
          
        public interface IsProjectBlackCallback{
            void done(boolean ok, int errorCode, String errorMessage);
        }
  
返回值:       
  
* **sync**: 同步接口正常时返回boolean，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.
  
* **async**: 异步接口不会抛出异常，通过callback返回boolean，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息.  

### 设置用户公开信息和私有信息

    // sync methods
    void setUserInfo(long uid, String openInfo, String priInfo);  
    void setUserInfo(long uid, String openInfo, String priInfo, int timeoutInseconds);
    
    // async methods
    void setUserInfo(long uid, String openInfo, String priInfo, DoneLambdaCallback callback);
    void setUserInfo(long uid, String openInfo, String priInfo, DoneLambdaCallback callback, int timeoutInseconds);

参数说明:  

* `String openInfo`: 需要设置的公开信息, 可为null或者空串. **最大 65535 字节**。

* `String priInfo`: 需要设置的私有信息, 可为null或者空串. **最大 65535 字节**。
  
* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
  client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.
  
* `DoneLambdaCallback callback`: 为异步回调返回接口, 错误码和错误信息将通过callback返回
          
        public interface DoneLambdaCallback {
            void done(int errorCode, String message);
        }
  
返回值:       
  
* **sync**: 同步接口正常时返回空，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.
  
* **async**: 异步接口不会抛出异常，通过callback返回接口调用结果，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息.

### 获取用户的公开信息和私有信息

    // sync methods
    void getUserInfo(long uid, StringBuffer openInfo, StringBuffer priInfo);
    void getUserInfo(long uid, StringBuffer openInfo, StringBuffer priInfo, int timeoutInseconds);
    
    // async methods
    void getUserInfo(long uid, GetUserInfoCallback callback);
    void getUserInfo(long uid, GetUserInfoCallback callback, int timeoutInseconds);
    
参数说明:  

* `StringBuffer openInfo`: 返回rid的公开信息.

* `StringBuffer priInfo`: 返回rid的私有信息.
  
* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
  client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.
  
* `GetUserInfoCallback callback`: 为异步回调返回接口, 调用结果以及错误码和错误信息将通过callback返回
          
        public interface GetUserInfoCallback{
            void done(String openInfo, String priInfo, int errorCode, String errorMessage);
        }
  
返回值:       
  
* **sync**: 同步接口正常时返回空，通过参数回传返回uid的公开信息、私有信息，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.
  
* **async**: 异步接口不会抛出异常，通过callback返回uid的公开信息、私有信息，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息. 

### 批量获取用户公开信息

    // sync methods
    Map<String, String> getUserOpenInfo(Set<Long> uids);
    Map<String, String> getUserOpenInfo(Set<Long> uids, int timeoutInseconds);
    
    // async
    void getUserOpenInfo(Set<Long> uids, GetUserOpenInfoCallback callback);
    void getUserOpenInfo(Set<Long> uids, GetUserOpenInfoCallback callback, int timeoutInseconds);
             
参数说明:  
  
* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
  client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.
  
* `GetUserOpenInfoCallback callback`: 为异步回调返回接口, 调用结果以及错误码和错误信息将通过callback返回
          
        public interface GetUserOpenInfoCallback{
            void done(Map<String, String> info, int errorCode, String errorMessage);
        }
  
返回值:       
  
* **sync**: 同步接口正常时返回Map<String, String>, key为uid 会将传入的uid转为String进行兼容，value为公开信息，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回Map<String, String>, key为uid 会将传入的uid转为String进行兼容，value为公开信息，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息.       