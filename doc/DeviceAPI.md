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

### 设置设备推送属性

    // sync methods
    void addDevicePushOption(long uid, int pushType, long xid, Set<Integer> mtype);
    void addDevicePushOption(long uid, int pushType, long xid, Set<Integer> mtype, int timeoutInseconds);
    
    // async methods
    void addDevicePushOption(long uid, int pushType, long xid, Set<Integer> mtype, DoneLambdaCallback callback);

参数说明：   

* `int pushType` : pushType**可选为(0,1)**, pushType == 0,设置某个p2p不推送，pushType == 1, 设置某个group不推送

* `long xid`: 当pushType为0时，xid为from，当pushType为1时，xid为groupId

* `Set<Integer> mtype`: mtype为null或者为空，则为所有mtype均不推送，其他值，则指定mtype不推送，注意聊天涉及到文本、cmd，文件几个mtype

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `DoneLambdaCallback callback`: 为异步回调返回接口, 错误码和错误信息将通过callback返回
        
        public interface DoneLambdaCallback {
            void done(int errorCode, String message);
        }

返回值:     
  
* **sync**: 同步接口正常时返回空，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回接口调用结果，当errorCode不等于ErrorCode.FPNN_EC_OK.value(),则为error返回,可查看message错误信息 

### 删除设备推送属性

    // sync methods
    void removeDevicePushOption(long uid, int pushType, long xid, Set<Integer> mtype);
    void removeDevicePushOption(long uid, int pushType, long xid, Set<Integer> mtype, int timeoutInseconds);
    
    // async methods
    void removeDevicePushOption(long uid, int pushType, long xid, Set<Integer> mtype, DoneLambdaCallback callback);
    void removeDevicePushOption(long uid, int pushType, long xid, Set<Integer> mtype, DoneLambdaCallback callback, int timeoutInseconds);

参数说明：   

* `int pushType` : pushType**可选为(0,1)**, pushType == 0,取消某个p2p不推送，pushType == 1, 取消某个group不推送

* `long xid`: 当pushType为0时，xid为from，当pushType为1时，xid为groupId

* `Set<Integer> mtype`: mtype为null或者为空，则为取消所有mtype均不推送，其他值，则取消指定mtype不推送，注意聊天涉及到文本、cmd，文件几个mtype

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `DoneLambdaCallback callback`: 为异步回调返回接口, 错误码和错误信息将通过callback返回
        
        public interface DoneLambdaCallback {
            void done(int errorCode, String message);
        }

返回值:     
  
* **sync**: 同步接口正常时返回空，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回接口调用结果，当errorCode不等于ErrorCode.FPNN_EC_OK.value(),则为error返回,可查看message错误信息 

### 获取设备推送属性

    // sync methods
    void getDevicePushOption(long uid, Map<Long, Set<Integer>> p2pOption, Map<Long, Set<Integer>> groupOption);
    void getDevicePushOption(long uid, Map<Long, Set<Integer>> p2pOption, Map<Long, Set<Integer>> groupOption, int timeoutInseconds);
    
    // async methods
    void getDevicePushOption(long uid, GetPushOptionLambdaCallBack callback);
    void getDevicePushOption(long uid, GetPushOptionLambdaCallBack callback, int timeoutInseconds);

参数说明：   

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `GetPushOptionLambdaCallBack callback`: 为异步回调返回接口, 真实数据、错误码和错误信息将通过callback返回
        
        public interface GetPushOptionLambdaCallBack{
            void done(Map<Long, Set<Integer>> p2pOption, Map<Long, Set<Integer>> groupOption, int errorCode, String errorMessage);
        }

返回值:     
  
* **sync**: 同步接口正常时返回空，真实数据通过参数返回(不推送用户或者群组对应的mtype映射)。错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回接口返回真实结果(不推送用户或者群组对应的mtype映射)，当errorCode不等于ErrorCode.FPNN_EC_OK.value(),则为error返回,可查看message错误信息     
    
        