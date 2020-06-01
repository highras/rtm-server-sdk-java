# RTM Server Java SDK Device API Docs

# Index

[TOC]

### -------------------------用户设备信息接口--------------------------

### 添加设备信息

    // sync methods
    void addDevice(long uid, String appType, String deviceToken);
    void addDevice(long uid, String appType, String deviceToken, int timeoutInseconds);
    
    // async methods
    void addDevice(long uid, String appType, String deviceToken, DoneLambdaCallback callback);
    void addDevice(long uid, String appType, String deviceToken, DoneLambdaCallback callback, int timeoutInseconds);
    
参数说明：   
* `String appType`: appType取值为 **"fcm"或者"apns"**

* `String deviceToken`: 设备id

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `DoneLambdaCallback callback`: 为异步回调返回接口, 错误码和错误信息将通过callback返回
        
        public interface DoneLambdaCallback {
            void done(int errorCode, String message);
        }

返回值:       

* **sync**: 同步接口正常时返回空，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回接口调用结果，当errorCode不等于ErrorCode.FPNN_EC_OK.value(),则为error返回,可查看message错误信息 

### 删除设备信息

    // sync methods
    void removeDevice(long uid, String deviceToken);
    void removeDevice(long uid, String deviceToken, int timeoutInseconds);
    
    // async methods
    void removeDevice(long uid, String deviceToken, DoneLambdaCallback callback);
    void removeDevice(long uid, String deviceToken, DoneLambdaCallback callback, int timeoutInseconds);

参数说明：   

* `String deviceToken`: 设备id

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `DoneLambdaCallback callback`: 为异步回调返回接口, 错误码和错误信息将通过callback返回
        
        public interface DoneLambdaCallback {
            void done(int errorCode, String message);
        }

返回值:     
  
* **sync**: 同步接口正常时返回空，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回接口调用结果，当errorCode不等于ErrorCode.FPNN_EC_OK.value(),则为error返回,可查看message错误信息    
    
        