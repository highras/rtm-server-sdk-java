# RTM Server Java SDK Listening API Docs

# Index

[TOC]

### -------------------------Set up monitoring and ServerPush interface--------------------------

### Receive ServerPush monitoring interface

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
    
        // cmd
        default void pushRoomCmd(RTMMessage message){}
        default void pushP2PCmd(RTMMessage message){}
        default void pushGroupCmd(RTMMessage message){}
    
        //file
        default void pushP2PFile(RTMMessage message){}
        default void pushGroupFile(RTMMessage message){}
        default void pushRoomFile(RTMMessage message){}
    }
    
Please configure settings through the RTM Console, and after the connection is established, call `addListen()` or `setListen()` for listening settings

### Configure message monitoring interface

    client.setServerPushMonitor(ServerPushMonitorAPI monitor);
    
Specific reference: [ServerPushMonitorAPI](#Receive ServerPush monitoring interface)

### **Increment** Add monitor

    // sync methods
    void addListen(Set<Long> gids, Set<Long> rids, Set<Long> uids, Set<String> events);
    void addListen(Set<Long> gids, Set<Long> rids, Set<Long> uids, Set<String> events, int timeoutInseconds);
    
    // async methods
    void addListen(Set<Long> gids, Set<Long> rids, Set<Long> uids, Set<String> events, DoneLambdaCallback callback);
    void addListen(Set<Long> gids, Set<Long> rids, Set<Long> uids, Set<String> events, DoneLambdaCallback callback, int timeoutInseconds);
    
Parameter Description:  

* `Set<Long> gids`: Add the group to be monitored, which can be null or empty.

* `Set<Long> rids`: Add a room for monitoring, which can be null or empty.

* `Set<Long> uids`: Add monitoring P2P users, which can be null or empty.

* `Set<String> events`: Events to be monitored, which can be null or empty. Currently, `login` and `logout` events are supported. For the list of events that can be monitored, please refer to the RTM service documentation.
  
* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.
  
* `DoneLambdaCallback callback`: return interface for asynchronous callback, error code and error message will be returned through callback
          
        public interface DoneLambdaCallback {
            void done(int errorCode, String message);
        }
  
return value:       
  
* **sync**: When the synchronization interface is normal, it returns empty. When the error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.
  
* **async**: The asynchronous interface will not throw an exception, and the interface call result will be returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it will be returned as an error. You can view the message error information.

### **Increment** Cancel monitoring settings

    // sync methods
    void removeListen(Set<Long> gids, Set<Long> rids, Set<Long> uids, Set<String> events);
    void removeListen(Set<Long> gids, Set<Long> rids, Set<Long> uids, Set<String> events, int timeoutInseconds);
    
    // async methods
    void removeListen(Set<Long> gids, Set<Long> rids, Set<Long> uids, Set<String> events, DoneLambdaCallback callback);
    void removeListen(Set<Long> gids, Set<Long> rids, Set<Long> uids, Set<String> events, DoneLambdaCallback callback, int timeoutInseconds);

Parameter Description:  

* `Set<Long> gids`: The group to cancel monitoring, which can be null or empty.

* `Set<Long> rids`: The room to cancel the monitoring, which can be null or empty.

* `Set<Long> uids`: P2P users who cancel the monitoring, which can be null or empty.

* `Set<String> events`: events to cancel monitoring, which can be null or empty. Currently, `login` and `logout` events are supported. For the list of events that can be monitored, please refer to the RTM service documentation.
  
* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.
  
* `DoneLambdaCallback callback`: return interface for asynchronous callback, error code and error message will be returned through callback
          
        public interface DoneLambdaCallback {
            void done(int errorCode, String message);
        }
  
return value:       
  
* **sync**: When the synchronization interface is normal, it returns empty. When the error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.
  
* **async**: The asynchronous interface will not throw an exception, and the interface call result will be returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it will be returned as an error. You can view the message error information.

### Set the monitoring state, this interface will **overwrite** the previous setting

    // sync methods
    void setListen(Set<Long> gids, Set<Long> rids, Set<Long> uids, Set<String> events);
    void setListen(Set<Long> gids, Set<Long> rids, Set<Long> uids, Set<String> events, int timeoutInseconds);
    
    // async methods
    void setListen(Set<Long> gids, Set<Long> rids, Set<Long> uids, Set<String> events, DoneLambdaCallback callback);
    void setListen(Set<Long> gids, Set<Long> rids, Set<Long> uids, Set<String> events, DoneLambdaCallback callback, int timeoutInseconds);
    
Parameter Description:  

* `Set<Long> gids`: Set the monitoring group, which can be null or empty.

* `Set<Long> rids`: Set the listening room, which can be null or empty.

* `Set<Long> uids`: Set listening P2P users, which can be null or empty.

* `Set<String> events`: Set monitored events, which can be null or empty. Currently, `login` and `logout` events are supported. For the list of events that can be monitored, please refer to the RTM service documentation.
  
* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.
  
* `DoneLambdaCallback callback`: return interface for asynchronous callback, error code and error message will be returned through callback
          
        public interface DoneLambdaCallback {
            void done(int errorCode, String message);
        }
  
return value:       
  
* **sync**: When the synchronization interface is normal, it returns empty. When the error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.
  
* **async**: The asynchronous interface will not throw an exception, and the interface call result will be returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it will be returned as an error. You can view the message error information.

### Set the monitoring state, this interface will **overwrite** the previous setting

    // sync methods
    void setListen(boolean allP2P, boolean allGroups, boolean allRooms, boolean allEvents);
    void setListen(boolean allP2P, boolean allGroups, boolean allRooms, boolean allEvents, int timeoutInseconds);
    
    // async methods
    void setListen(boolean allP2P, boolean allGroups, boolean allRooms, boolean allEvents, DoneLambdaCallback callback);
    void setListen(boolean allP2P, boolean allGroups, boolean allRooms, boolean allEvents, DoneLambdaCallback callback, int timeoutInseconds);
    
Parameter Description:  

* `boolean allP2P`: Set whether to listen to all P2P messages.

* `boolean allGroups`: Set whether to monitor all groups.

* `boolean allRooms`: Set whether to monitor all rooms.

* `boolean allEvents`: Set whether to monitor all events. For the list of events that can be monitored, please refer to the RTM service documentation.
  
* `int timeoutInseconds`: Sending timeout, lack of timeoutInseconds parameter, or timeoutInseconds is 0, the configuration of the RTM Server Client instance will be used, that is, call The timeout time set by client.setQuestTimeout(int timeout). If the RTM Server Client instance is not configured, the corresponding timeout configuration of fpnn will be used, and the default is 5seconds.
  
* `DoneLambdaCallback callback`: return interface for asynchronous callback, error code and error message will be returned through callback
          
        public interface DoneLambdaCallback {
            void done(int errorCode, String message);
        }
  
return value:       
  
* **sync**: When the synchronization interface is normal, it returns empty. When the error returns, it will throw an exception RTMException or other systemic exceptions. For RTMException exceptions, you can view the error information through the toString method.
  
* **async**: The asynchronous interface will not throw an exception, and the interface call result will be returned through callback. When the errorCode is not equal to ErrorCode.FPNN_EC_OK.value(), it will be returned as an error. You can view the message error information.
  