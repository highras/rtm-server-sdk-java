# RTM Server Java SDK Room API Docs

# Index

[TOC]

### -------------------------房间相关接口--------------------------

### 添加房间成员

    // sync methods
    void addRoomMember(long roomId, long uid);
    void addRoomMember(long roomId, long uid, int timeoutInseconds);
    
    // async methods
    void addRoomMember(long roomId, long uid, DoneLambdaCallback callback);
    void addRoomMember(long roomId, long uid, DoneLambdaCallback callback, int timeoutInseconds);
    
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

### 删除房间成员

    // sync methods
    void delRoomMember(long roomId, long uid);
    void delRoomMember(long roomId, long uid, int timeoutInseconds);
    
    // async methods
    void delRoomMember(long roomId, long uid, DoneLambdaCallback callback);
    void delRoomMember(long roomId, long uid, DoneLambdaCallback callback, int timeoutInseconds);

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

### 禁止用户指定房间内发言

    // sync methods
    void addRoomBan(long roomId, long uid, int btime);
    void addRoomBan(long roomId, long uid, int btime, int timeoutInseconds);
    
    // async methods
    void addRoomBan(long roomId, long uid, int btime, DoneLambdaCallback callback);
    void addRoomBan(long roomId, long uid, int btime, DoneLambdaCallback callback, int timeoutInseconds);
    
参数说明:  

* `int btime`:  禁言时长，从当前时间开始，以秒计算
  
* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
  client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.
  
* `DoneLambdaCallback callback`: 为异步回调返回接口, 错误码和错误信息将通过callback返回
          
        public interface DoneLambdaCallback {
            void done(int errorCode, String message);
        }
  
返回值:       
  
* **sync**: 同步接口正常时返回空，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.
  
* **async**: 异步接口不会抛出异常，通过callback返回接口调用结果，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息.        
       
### 解除用户指定房间内禁言

    // sync methods
    void removeRoomBan(long roomId, long uid);
    void removeRoomBan(long roomId, long uid, int timeoutInseconds);
    
    // async methods
    void removeRoomBan(long roomId, long uid, DoneLambdaCallback callback);
    void removeRoomBan(long roomId, long uid, DoneLambdaCallback callback, int timeoutInseconds);
    
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

### 判断用户是否在指定房间内禁言

    // sync methods
    boolean isBanOfRoom(long roomId, long uid);
    boolean isBanOfRoom(long roomId, long uid, int timeoutInseconds);
    
    // async methods
    void isBanOfRoom(long roomId, long uid, IsBanOfRoomCallBack callback);
    void isBanOfRoom(long roomId, long uid, IsBanOfRoomCallBack callback, int timeoutInseconds);
    
参数说明:  
  
* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
  client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.
  
* `IsBanOfRoomCallBack callback`: 为异步回调返回接口, 错误码和错误信息将通过callback返回
          
        public interface IsBanOfRoomCallBack{
            void done(boolean ok, int errorCode, String errorMessage);
        }
  
返回值:       
  
* **sync**: 同步接口正常时返回boolean，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.
  
* **async**: 异步接口不会抛出异常，通过callback返回boolean，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息.

###  设置房间公开信息和私有信息
    
    // sync methods
    void setRoomInfo(long roomId, String openInfo, String priInfo);
    void setRoomInfo(long roomId, String openInfo, String priInfo, int timeoutInseconds);
    
    // async methods
    void setRoomInfo(long roomId, String openInfo, String priInfo, DoneLambdaCallback callback);
    void setRoomInfo(long roomId, String openInfo, String priInfo, DoneLambdaCallback callback, int timeoutInseconds);
    
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

### 获取房间公开信息和私有信息   

    // sync methods
    void getRoomInfo(long roomId, StringBuffer openInfo, StringBuffer priInfo);
    void getRoomInfo(long roomId, StringBuffer openInfo, StringBuffer priInfo, int timeoutInseconds);
    
    // async methods
    void getRoomInfo(long roomId, GetRoomInfoCallback callback);
    void getRoomInfo(long roomId, GetRoomInfoCallback callback, int timeoutInseconds);
    
参数说明:  

* `StringBuffer openInfo`: 返回roomId的公开信息.

* `StringBuffer priInfo`: 返回roomId的私有信息.
  
* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
  client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.
  
* `GetRoomInfoCallback callback`: 为异步回调返回接口, 调用结果以及错误码和错误信息将通过callback返回
          
        public interface GetRoomInfoCallback{
            void done(String openInfo, String priInfo, int errorCode, String errorMessage);
        }
  
返回值:       
  
* **sync**: 同步接口正常时返回空，通过参数回传返回roomId的公开信息、私有信息，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.
  
* **async**: 异步接口不会抛出异常，通过callback返回roomId的公开信息、私有信息，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息.     
    

   
           