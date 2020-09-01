# RTM Server Java SDK Listening API Docs

# Index

[TOC]

### -------------------------设置监听以及ServerPush接口--------------------------

### 接收ServerPush的监听接口

    public interface ServerPushMonitorAPI {
        default void pushEvent(int projectId, String event, long uid, int time, String endpoint, String data){}
        // message
        default void pushRoomMessage(RTMMessage message){}
        default void pushP2PMessage(RTMMessage message){}
        default void pushGroupMessage(RTMMessage message) {}
    
        // chat
        default void pushRoomChat(RTMMessage message){}
        default void pushP2PChat(RTMMessage message){}
        default void pushGroupChat(RTMMessage message){}
    
        // audio
        default void pushRoomAudio(RTMMessage message){}
        default void pushP2PAudio(RTMMessage message){}
        default void pushGroupAudio(RTMMessage message){}
    
        // cmd
        default void pushRoomCmd(RTMMessage message){}
        default void pushP2PCmd(RTMMessage message){}
        default void pushGroupCmd(RTMMessage message){}
    
        //file
        default void pushP2PFile(RTMMessage message){}
        default void pushGroupFile(RTMMessage message){}
        default void pushRoomFile(RTMMessage message){}
    }
    
请通过 RTM Console 进行配置设置，并在建立连接后，调用 `addListen()`或者`setListen()`进行监听设置

### 配置消息监听接口

    client.setServerPushMonitor(ServerPushMonitorAPI monitor);  
    
具体参考: [ServerPushMonitorAPI](#接收ServerPush的监听接口)

### **增量**添加监听

    // sync methods
    void addListen(Set<Long> gids, Set<Long> rids, Set<Long> uids, Set<String> events);
    void addListen(Set<Long> gids, Set<Long> rids, Set<Long> uids, Set<String> events, int timeoutInseconds);
    
    // async methods
    void addListen(Set<Long> gids, Set<Long> rids, Set<Long> uids, Set<String> events, DoneLambdaCallback callback);
    void addListen(Set<Long> gids, Set<Long> rids, Set<Long> uids, Set<String> events, DoneLambdaCallback callback, int timeoutInseconds);
    
参数说明:  

* `Set<Long> gids`: 增加监听的群组，可为null或者空.

* `Set<Long> rids`: 增加监听的房间，可为null或者空.

* `Set<Long> uids`: 增加监听的 P2P 用户，可为null或者空.

* `Set<String> events`: 需要监听的事件，可为null或者空，当前支持`login`、`logout`事件，具体可监听的事件列表，请参考 RTM 服务文档.
  
* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
  client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.
  
* `DoneLambdaCallback callback`: 为异步回调返回接口, 错误码和错误信息将通过callback返回
          
        public interface DoneLambdaCallback {
            void done(int errorCode, String message);
        }
  
返回值:       
  
* **sync**: 同步接口正常时返回空，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.
  
* **async**: 异步接口不会抛出异常，通过callback返回接口调用结果，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息.

### **增量**取消监听设置

    // sync methods
    void removeListen(Set<Long> gids, Set<Long> rids, Set<Long> uids, Set<String> events);
    void removeListen(Set<Long> gids, Set<Long> rids, Set<Long> uids, Set<String> events, int timeoutInseconds);
    
    // async methods
    void removeListen(Set<Long> gids, Set<Long> rids, Set<Long> uids, Set<String> events, DoneLambdaCallback callback);
    void removeListen(Set<Long> gids, Set<Long> rids, Set<Long> uids, Set<String> events, DoneLambdaCallback callback, int timeoutInseconds);

参数说明:  

* `Set<Long> gids`: 取消监听的群组，可为null或者空.

* `Set<Long> rids`: 取消监听的房间，可为null或者空.

* `Set<Long> uids`: 取消监听的 P2P 用户，可为null或者空.

* `Set<String> events`: 取消监听的事件，可为null或者空，当前支持`login`、`logout`事件，具体可监听的事件列表，请参考 RTM 服务文档.
  
* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
  client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.
  
* `DoneLambdaCallback callback`: 为异步回调返回接口, 错误码和错误信息将通过callback返回
          
        public interface DoneLambdaCallback {
            void done(int errorCode, String message);
        }
  
返回值:       
  
* **sync**: 同步接口正常时返回空，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.
  
* **async**: 异步接口不会抛出异常，通过callback返回接口调用结果，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息. 

### 设置监听状态, 该接口将**覆盖**以前的设置 

    // sync methods
    void setListen(Set<Long> gids, Set<Long> rids, Set<Long> uids, Set<String> events);
    void setListen(Set<Long> gids, Set<Long> rids, Set<Long> uids, Set<String> events, int timeoutInseconds);
    
    // async methods
    void setListen(Set<Long> gids, Set<Long> rids, Set<Long> uids, Set<String> events, DoneLambdaCallback callback);
    void setListen(Set<Long> gids, Set<Long> rids, Set<Long> uids, Set<String> events, DoneLambdaCallback callback, int timeoutInseconds);
    
参数说明:  

* `Set<Long> gids`: 设置监听的群组，可为null或者空.

* `Set<Long> rids`: 设置监听的房间，可为null或者空.

* `Set<Long> uids`: 设置监听的 P2P 用户，可为null或者空.

* `Set<String> events`: 设置监听的事件，可为null或者空，当前支持`login`、`logout`事件，具体可监听的事件列表，请参考 RTM 服务文档.
  
* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
  client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.
  
* `DoneLambdaCallback callback`: 为异步回调返回接口, 错误码和错误信息将通过callback返回
          
        public interface DoneLambdaCallback {
            void done(int errorCode, String message);
        }
  
返回值:       
  
* **sync**: 同步接口正常时返回空，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.
  
* **async**: 异步接口不会抛出异常，通过callback返回接口调用结果，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息. 

### 设置监听状态, 该接口将**覆盖**以前的设置

    // sync methods
    void setListen(boolean allP2P, boolean allGroups, boolean allRooms, boolean allEvents);
    void setListen(boolean allP2P, boolean allGroups, boolean allRooms, boolean allEvents, int timeoutInseconds);
    
    // async methods
    void setListen(boolean allP2P, boolean allGroups, boolean allRooms, boolean allEvents, DoneLambdaCallback callback);
    void setListen(boolean allP2P, boolean allGroups, boolean allRooms, boolean allEvents, DoneLambdaCallback callback, int timeoutInseconds);
    
参数说明:  

* `boolean allP2P`: 设置是否监听所有的 P2P 消息.

* `boolean allGroups`: 设置是否监听所有群组.

* `boolean allRooms`: 设置是否监听所有房间.

* `boolean allEvents`: 设置是否监听所有的事件，具体可监听的事件列表，请参考 RTM 服务文档.
  
* `int timeoutInseconds`: 发送超时，缺少timeoutInseconds参数，或timeoutInseconds为0时，将采用RTM Server Client实例的配置，即调用   
  client.setQuestTimeout(int timeout)设置的超时时间，若RTM Server Client实例未配置，将采用 fpnn相应的超时配置，默认为5seconds.
  
* `DoneLambdaCallback callback`: 为异步回调返回接口, 错误码和错误信息将通过callback返回
          
        public interface DoneLambdaCallback {
            void done(int errorCode, String message);
        }
  
返回值:       
  
* **sync**: 同步接口正常时返回空，错误返回时将抛出异常RTMException或者其他系统性异常，对于RTMException异常可通过toString方法查看error信息.
  
* **async**: 异步接口不会抛出异常，通过callback返回接口调用结果，当errorCode不等于ErrorCode.FPNN_EC_OK.value()，则为error返回，可查看message错误信息.    
  