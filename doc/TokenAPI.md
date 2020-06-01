# RTM Server Java SDK Token API Docs

# Index

[TOC]

### -------------------------Token相关接口--------------------------

### 获取用户登陆的 token

    // sync methods
    String getToken(long uid)
    String getToken(long uid, int timeoutInseconds);

    // async methods
    boolean getToken(long uid, GetTokenLambdaCallback callback);
    boolean getToken(long uid, GetTokenLambdaCallback callback, int timeoutInseconds);
    
参数说明:  
  
* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
  client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.
  
* `GetTokenLambdaCallback callback`: 为异步回调返回接口, 错误码和错误信息将通过callback返回
          
        public interface GetTokenLambdaCallback{
            void done(String token, int errorCode, String errorMessage);
        }
  
返回值:       
  
* **sync**: 同步接口正常时返回token，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.
  
* **async**: 异步接口不会抛出异常，通过callback返回token，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息.

### 删除用户登陆的 token

    // sync methods
    void removeToken(long uid);
    void removeToken(long uid, int timeoutInseconds);
    
    // async methods
    void removeToken(long uid,  DoneLambdaCallback callback);
    void removeToken(long uid,  DoneLambdaCallback callback, int timeoutInseconds);
    
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