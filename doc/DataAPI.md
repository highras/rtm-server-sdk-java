# RTM Server Java SDK Data API Docs

# Index

[TOC]

### -------------------------用户数据接口--------------------------

### 存储用户数据

    // sync method
    void dataSet(long uid, String key, String value);
    void dataSet(long uid, String key, String value, int timeoutInseconds);
    
    // async methods
    void dataSet(long uid, String key, String value, DoneLambdaCallback callback);
    void dataSet(long uid, String key, String value, DoneLambdaCallback callback, int timeoutInseconds);
    
参数说明：   

* `String key`: 最大128字节.

* `String value`: 最大65535字节.

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `DoneLambdaCallback callback`: 为异步回调返回接口, 错误码和错误信息将通过callback返回
        
        public interface DoneLambdaCallback {
            void done(int errorCode, String message);
        }

返回值:       

* **sync**: 同步接口正常时返回空，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回接口调用结果，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息.

### 获取用户存储的数据

    // sync methods
    String dataGet(long uid, String key);
    String dataGet(long uid, String key, int timeoutInseconds);
    
    // async methods
    void dataGet(long uid, String key, DataGetCallback callback);
    void dataGet(long uid, String key, DataGetCallback callback, int timeoutInseconds);
    
参数说明：   

* `String key`: 最大128字节.

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `DataGetCallback callback`: 为异步回调返回接口, 返回结果以及错误码和错误信息将通过callback返回
        
        public interface DataGetCallback{
            void done(String value, int errorCode, String errorMessage);
        }

返回值:       

* **sync**: 同步接口正常时返回key的数据，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回key的数据，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息.  

### 删除用户存储数据

    // sync methods
    void dataDel(long uid, String key);
    void dataDel(long uid, String key, int timeoutInseconds);
    
    // async methods
    void dataDel(long uid, String key, DoneLambdaCallback callback);
    void dataDel(long uid, String key, DoneLambdaCallback callback, int timeoutInseconds);

参数说明：   

* `String key`: 最大128字节.

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `DoneLambdaCallback callback`: 为异步回调返回接口, 错误码和错误信息将通过callback返回
        
        public interface DoneLambdaCallback {
            void done(int errorCode, String message);
        }

返回值:       

* **sync**: 同步接口正常时返回空，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回接口调用结果，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息.     
     
