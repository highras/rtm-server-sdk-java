# RTM Server Java SDK Black API Docs

# Index

[TOC]

### -------------------------拉黑用户相关接口--------------------------

### 拉黑用户 拉黑后对方不能给自己发聊天消息，自己可以给对方发聊天消息, 双方能正常获取session及历史消息

    // sync methods
    void addBlacks(long uid, Set<Long> blackIds);
    void addBlacks(long uid, Set<Long> blackIds, int timeoutInseconds);
    
    // async methods
    void addBlacks(long uid, Set<Long> blackIds, DoneLambdaCallback callback);
    void addBlacks(long uid, Set<Long> blackIds, DoneLambdaCallback callback, int timeoutInseconds);
    
参数说明: 
  
* `Set<Long> blackIds`: 每次**最多添加100人**.

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `DoneLambdaCallback callback`: 为异步回调返回接口, 错误码和错误信息将通过callback返回
        
        public interface DoneLambdaCallback {
            void done(int errorCode, String message);
        }

返回值:   
    
* **sync**: 同步接口正常时返回空，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回接口调用结果，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息. 

### 解除拉黑关系 

    // sync methods
    void delBlacks(long uid, Set<Long> blackIds);
    void delBlacks(long uid, Set<Long> blackIds, int timeoutInseconds);
    
    // async methods
    void delBlacks(long uid, Set<Long> blackIds, DoneLambdaCallback callback);
    void delBlacks(long uid, Set<Long> blackIds, DoneLambdaCallback callback, int timeoutInseconds);
    
参数说明:   
 
* `Set<Long> blackIds`: 每次**最多解除100人**.
 
* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
 client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.
 
* `DoneLambdaCallback callback`: 为异步回调返回接口, 错误码和错误信息将通过callback返回
         
         public interface DoneLambdaCallback {
             void done(int errorCode, String message);
         }
 
返回值:      
  
* **sync**: 同步接口正常时返回空，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.
 
* **async**: 异步接口不会抛出异常，通过callback返回接口调用结果，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息.      

### 获取被uid拉黑的用户列表

    // sync methods
    Set<Long> getBlacks(long uid);
    Set<Long> getBlacks(long uid, int timeoutInseconds);
    
    // async methods
    void getBlacks(long uid, GetBlacksLambdaCallBack callback);
    void getBlacks(long uid, GetBlacksLambdaCallBack callback, int timeoutInseconds);
    
参数说明：   

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `GetBlacksLambdaCallBack callback`: 为异步回调返回接口, 返回结果以及错误码和错误信息将通过callback返回
        
        public interface GetBlacksLambdaCallBack{
            void done(Set<Long> uids, int errorCode, String errorMessage);
        }

返回值:      
 
* **sync**: 同步接口正常时返回被uid拉黑的用户列表，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回被uid拉黑的用户列表，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息.

### 判断拉黑关系 uid是否被buid拉黑，会用在发送单人聊天消息的时候

    // sync methods
    boolean isBlack(long uid, long buid);
    boolean isBlack(long uid, long buid, int timeoutInseconds);
    
    // async methods
    void isBlack(long uid, long buid, IsBlackLambdaCallBack callback);
    void isBlack(long uid, long buid, IsBlackLambdaCallBack callback, int timeoutInseconds);
 
参数说明：   

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `IsBlackLambdaCallBack callback`: 为异步回调返回接口, 返回结果以及错误码和错误信息将通过callback返回
        
        public interface IsBlackLambdaCallBack{
            void done(boolean ok, int errorCode, String errorMessage);
        }

返回值:
       
* **sync**: 同步接口正常时返回boolean，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回boolean，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息. 

### 判断多人拉黑关系，uid是否被buids中的用户拉黑，用在发送多人聊天消息的时候

    // sync methods
    Set<Long> isBlacks(long uid, Set<Long> buids);
    Set<Long> isBlacks(long uid, Set<Long> buids, int timeoutInseconds);
    
    // async methods
    void isBlacks(long uid, Set<Long> buids, IsBlacksLambdaCallBack callback);
    void isBlacks(long uid, Set<Long> buids, IsBlacksLambdaCallBack callback, int timeoutInseconds);

参数说明:   
 
* `Set<Long> buids`: 每次**最多判断100人**.
 
* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
 client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.
 
* `IsBlacksLambdaCallBack callback`: 为异步回调返回接口, 错误码和错误信息将通过callback返回
         
        public interface IsBlacksLambdaCallBack{
            void done(Set<Long> buids, int errorCode, String errorMessage);
        }
 
返回值:
        
* **sync**: 同步接口正常时返回的集合即为把uid拉黑的人，这些人收不到uid发送的消息，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.
 
* **async**: 异步接口不会抛出异常，通过callback返回的buids即为把uid拉黑的人，这些人收不到uid发送的消息，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息.    
          