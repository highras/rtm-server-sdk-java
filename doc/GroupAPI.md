# RTM Server Java SDK Group API Docs

# Index

[TOC]

### -------------------------群组关系以及群组操作接口--------------------------

### 添加群组成员

    // sync methods
    void addGroupMembers(long groupId, Set<Long> uids);
    void addGroupMembers(long groupId, Set<Long> uids, int timeoutInseconds);
    
    // async methods
    void addGroupMembers(long groupId, Set<Long> uids, DoneLambdaCallback callback);
    void addGroupMembers(long groupId, Set<Long> uids, DoneLambdaCallback callback, int timeoutInseconds);
    
参数说明:  
 
* `Set<Long> uids`: 每次**最多添加100人**.

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `DoneLambdaCallback callback`: 为异步回调返回接口, 错误码和错误信息将通过callback返回
        
        public interface DoneLambdaCallback {
            void done(int errorCode, String message);
        }

返回值:       

* **sync**: 同步接口正常时返回空，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回接口调用结果，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息. 

### 删除群组成员

    // sync methods
    void delGroupMembers(long groupId, Set<Long> uids)
    void delGroupMembers(long groupId, Set<Long> uids, int timeoutInseconds);
    
    // async methods
    void delGroupMembers(long groupId, Set<Long> uids, DoneLambdaCallback callback);
    void delGroupMembers(long groupId, Set<Long> uids, DoneLambdaCallback callback, int timeoutInseconds);
    
参数说明:  
 
* `Set<Long> uids`: 每次**最多删除100人**.
 
* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
 client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.
 
* `DoneLambdaCallback callback`: 为异步回调返回接口, 错误码和错误信息将通过callback返回
         
        public interface DoneLambdaCallback {
            void done(int errorCode, String message);
        }
 
返回值:       

* **sync**: 同步接口正常时返回空，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.
 
* **async**: 异步接口不会抛出异常，通过callback返回接口调用结果，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息.
 
### 删除群组
 
    // sync methods
    void delGroup(long groupId);
    void delGroup(long groupId, int timeoutInseconds);
    
    // async methods
    void delGroup(long groupId, DoneLambdaCallback callback);
    void delGroup(long groupId, DoneLambdaCallback callback, int timeoutInseconds);
    
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
  
### 获取群组成员
  
    // sync methods
    Set<Long> getGroupMembers(long groupId);
    Set<Long> getGroupMembers(long groupId, int timeoutInseconds);
    
    //async methods
    void getGroupMembers(long groupId, GetGroupMembersLambdaCallBack callback);
    void getGroupMembers(long groupId, GetGroupMembersLambdaCallBack callback, int timeoutInseconds);
    
参数说明：   

* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.

* `GetGroupMembersLambdaCallBack callback`: 为异步回调返回接口, 返回结果以及错误码和错误信息将通过callback返回
        
        public interface GetGroupMembersLambdaCallBack{
            void done(Set<Long> uids, int errorCode, String errorMessage);
        }

返回值: 
      
* **sync**: 同步接口正常时返回groupId的成员列表，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.

* **async**: 异步接口不会抛出异常，通过callback返回groupId的成员列表，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息.

### 是否群组成员

    // sync methods
    boolean isGroupMember(long uid, long groupId);
    boolean isGroupMember(long uid, long groupId, int timeoutInseconds);
    
    // async methods
    void isGroupMember(long uid, long groupId, IsGroupMemberCallBack callback);
    void isGroupMember(long uid, long groupId, IsGroupMemberCallBack callback, int timeoutInseconds);
    
参数说明：   
 
* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
 client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.
 
* `GetGroupMembersLambdaCallBack callback`: 为异步回调返回接口, 返回结果以及错误码和错误信息将通过callback返回
         
        public interface IsGroupMemberCallBack{
            void done(boolean ok, int errorCode, String errorMessage);
        }
 
返回值:
       
* **sync**: 同步接口正常时返回boolean，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.
 
* **async**: 异步接口不会抛出异常，通过callback返回boolean，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息. 
 
### 获取用户加入的群组
 
    // sync methods
    Set<Long> getUserGroups(long uid);
    Set<Long> getUserGroups(long uid, int timeoutInseconds);
    
    // async methods
    void getUserGroups(long uid, GetUserGroupsCallBack callback);
    void getUserGroups(long uid, GetUserGroupsCallBack callback, int timeoutInseconds);
    
参数说明：   
   
* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
   client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.
   
* `GetUserGroupsCallBack callback`: 为异步回调返回接口, 返回结果以及错误码和错误信息将通过callback返回
           
        public interface GetUserGroupsCallBack{
            void done(Set<Long> groupIds, int errorCode, String errorMessage);
        }
   
返回值:
       
* **sync**: 同步接口正常时返回uid加入的群组列表，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.
   
* **async**: 异步接口不会抛出异常，通过callback返回uid加入的群组列表，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息. 

### 禁止用户指定群组内发言

    // sync methods
    void addGroupBan(long groupId, long uid, int btime);
    void addGroupBan(long groupId, long uid, int btime, int timeoutInseconds);
    
    // async methods
    void addGroupBan(long groupId, long uid, int btime, DoneLambdaCallback callback);
    void addGroupBan(long groupId, long uid, int btime, DoneLambdaCallback callback, int timeoutInseconds);
    
参数说明:  

* `long groupId`: 如果**groupId <= 0**,则为全部群组禁言

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

### 解除用户指定群组内禁言

    // sync methods
    void removeGroupBan(long groupId, long uid);
    void removeGroupBan(long groupId, long uid, int timeoutInseconds);
    
    // async methods
    void removeGroupBan(long groupId, long uid, DoneLambdaCallback callback);
    void removeGroupBan(long groupId, long uid, DoneLambdaCallback callback, int timeoutInseconds);
    
参数说明:  

* `long groupId`: 如果**groupId <= 0**,则为全部群组解除禁言
  
* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
  client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.
  
* `DoneLambdaCallback callback`: 为异步回调返回接口, 错误码和错误信息将通过callback返回
          
        public interface DoneLambdaCallback {
            void done(int errorCode, String message);
        }
  
返回值:       
  
* **sync**: 同步接口正常时返回空，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.
  
* **async**: 异步接口不会抛出异常，通过callback返回接口调用结果，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息.

### 判断用户是否在指定群组中被禁言

    // sync methods
    boolean isBanOfGroup(long uid, long groupId);
    boolean isBanOfGroup(long uid, long groupId, int timeoutInseconds);
    
    // async methods
    void isBanOfGroup(long uid, long groupId, IsBanOfGroupCallBack callback);
    void isBanOfGroup(long uid, long groupId, IsBanOfGroupCallBack callback, int timeoutInseconds);
    
参数说明:  
  
* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
  client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.
  
* `IsBanOfGroupCallBack callback`: 为异步回调返回接口, 错误码和错误信息将通过callback返回
          
        public interface IsBanOfGroupCallBack{
            void done(boolean ok, int errorCode, String errorMessage);
        }
  
返回值:       
  
* **sync**: 同步接口正常时返回boolean，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.
  
* **async**: 异步接口不会抛出异常，通过callback返回boolean，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息.

### 设置群组公开信息和私有信息

    // sync methods
    void setGroupInfo(long groupId, String openInfo, String priInfo);
    void setGroupInfo(long groupId, String openInfo, String priInfo, int timeoutInseconds);
    
    // async methods
    void setGroupInfo(long groupId, String openInfo, String priInfo, DoneLambdaCallback callback);
    void setGroupInfo(long groupId, String openInfo, String priInfo, DoneLambdaCallback callback, int timeoutInseconds);
    
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

### 获取群组公开信息和私有信息

    // sync methods
    void getGroupInfo(long groupId, StringBuffer openInfo, StringBuffer priInfo);
    void getGroupInfo(long groupId, StringBuffer openInfo, StringBuffer priInfo, int timeoutInseconds);
    
    // async methods
    void getGroupInfo(long groupId, GetGroupInfoCallback callback);
    void getGroupInfo(long groupId, GetGroupInfoCallback callback, int timeoutInseconds);
    
参数说明:  

* `StringBuffer openInfo`: 返回groupId的公开信息.

* `StringBuffer priInfo`: 返回groupId的私有信息.
  
* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
  client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.
  
* `GetGroupInfoCallback callback`: 为异步回调返回接口, 调用结果以及错误码和错误信息将通过callback返回
          
        public interface GetGroupInfoCallback{
            void done(String openInfo, String priInfo, int errorCode, String errorMessage);
        }
  
返回值:       
  
* **sync**: 同步接口正常时返回空，通过参数回传返回groupId的公开信息、私有信息，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.
  
* **async**: 异步接口不会抛出异常，通过callback返回groupId的公开信息、私有信息，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息.     