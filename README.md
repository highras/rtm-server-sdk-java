# RTM Server Java SDK

[TOC]

## Depends

* [fpnn-sdk-java](https://github.com/highras/fpnn-sdk-java)

### Language Level:

Java 8

### Notice

* Before using the SDK, please make sure the server time is correct, RTM-Server will check whether the signature time has expired.

## Usage

### For Maven Users:
    <dependency>
        <groupId>com.github.highras</groupId>
        <artifactId>rtm-server-sdk</artifactId>
        <version>2.5.5-RELEASE</version>
    </dependency>
    
### Import package

    import com.fpnn.rtm.RTMServerClient;
    import com.fpnn.rtm.RTMException;
    import com.fpnn.rtm.RTMServerClientBase;
    import com.fpnn.rtm.api.APIBase;
    import com.fpnn.sdk.ErrorCode;
    import com.fpnn.sdk.ErrorRecorder;
    
### Create
    
    RTMServerClient client = RTMServerClient.create(int pid, String secretKey, String endpoint) throws IllegalArgumentException;

Please get your project params from RTM Console.

### Configure (Optional)

* Base configs

        client.setAutoCleanup(boolean auto);
        client.configureForMultipleSynchronousConcurrentAPIs(int taskThreadCount);
        client.setQuestTimeout(int timeout);
        client.setAutoConnect(boolean autoConnect);
        client.setDefaultRegressiveConnectStrategy(RTMRegressiveConnectStrategy strategy);
    
    **Note:**   
    **setAutoCleanup:** when client.setAutoCleanup(false); must call client.SDKCleanup() for clean up SDK, default is true.   
    **setAutoConnect:** means establishing the connection in implicit or explicit. NOT keep the connection, default is true.   
    **configureForMultipleSynchronousConcurrentAPIs:** Default max limitation is 4 threads.   
    **setQuestTimeout:** default timeout for global quest is 30 seconds.
    
* Set serverPush message monitor

        client.setServerPushMonitor(ServerPushMonitorAPI monitor);

* Set connection events callback

        client.setRTMClientConnectedCallback(RTMClientConnectCallback cb);
        client.setRTMClientWillCloseCallback(RTMClientWillCloseCallback cb);
        client.setRTMClientHasClosedCallback(RTMClientHasClosedCallback cb); 
        
        public interface RTMClientConnectCallback {
            void connectResult(InetSocketAddress peerAddress, boolean connected, boolean reConnect, RTMServerClient.RegressiveState connectState));
        }
        
        public interface RTMClientWillCloseCallback {
            void connectionWillClose(InetSocketAddress peerAddress, boolean causedByError);
        }
        
        public interface RTMClientHasClosedCallback {
            void connectionHasClosed(InetSocketAddress peerAddress, boolean causedByError, boolean reConnect, RTMServerClient.RegressiveState connectState));
        }
        
* Config encrypted connection

        public boolean client.enableEncryptorByDerFile(String curve, String keyFilePath);
        public boolean client.enableEncryptorByDerData(String curve, byte[] peerPublicKey);
        
    **Note:**   
    * RTM Server Java SDK using **ECC**/**ECDH** to exchange the secret key, and using **AES-128** or **AES-256** 
    in **CFB** mode to encrypt the whole session in **stream** way. 支持的**ECC曲线**(secp192r1,secp224r1,secp256r1,secp256k1).  
    * **curve** is the curve name of the der key for ECC encryption.Curve `secp256k1` is recommended. 
    
### Connect (Optional) 

    client.connect(boolean synchronous) throws InterruptedException;
         
Call connect method to do an explicit connecting action.       
If client.setAutoConnect(false) is called, this explicit connecting method MUST be called; otherwise this method is optional.

### Close (Optional)

    client.close();
    
### GetClientEndPoint

    String endpoint = client.endpoint();  // get RTM Server Client target endpoint
    
### ClientIsConnect

    boolean ok = client.connected();
    
### Client KeepAlive(Optional)
    
    client.setKeepAlive(boolean keepAlive)
       
Note: The default connection is not keepAlive     
    
### SDK Version

    System.out.println("RTM SDK Version: " + RTMServerClient.SDKVersion);
    System.out.println("RTM Interface Version: " + RTMServerClient.InterfaceVersion); 
   
### Send Messages

* Send Messages Async Callback Interface

         public interface SendMessageLambdaCallback{
            void done(long time, int errorCode, String errorMessage);
         }
                
* Send P2P Message
        
        // sync methods
        long time = client.sendMessage(long fromUid, long toUid, byte mType, String message, String attrs);
        long time = client.sendMessage(long fromUid, long toUid, byte mType, String message, String attrs, int timeoutInseconds);
        long time = client.sendMessage(long fromUid, long toUid, byte mType, byte[] message, String attrs);   
        long time = client.sendMessage(long fromUid, long toUid, byte mType, byte[] message, String attrs, int timeoutInseconds);
        
        // async methods
        client.sendMessage(long fromUid, long toUid, byte mType, String message, String attrs, SendMessageLambdaCallback callback);
        client.sendMessage(long fromUid, long toUid, byte mType, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);
        client.sendMessage(long fromUid, long toUid, byte mType, byte[] message, String attrs, SendMessageLambdaCallback callback);
        client.sendMessage(long fromUid, long toUid, byte mType, byte[] message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);
        
* Send Multi-Receivers P2P Message

        // sync methods
        long time = client.sendMessages(long fromUid, Set<Long> toUids, byte mType, String message, String attrs);
        long time = client.sendMessages(long fromUid, Set<Long> toUids, byte mType, String message, String attrs, int timeoutInseconds);
        long time = client.sendMessages(long fromUid, Set<Long> toUids, byte mType, byte[] message, String attrs);
        long sendMessages(long fromUid, Set<Long> toUids, byte mType, byte[] message, String attrs, int timeoutInseconds);
        
        //async methods
        client.sendMessages(long fromUid, Set<Long> toUids, byte mType, String message, String attrs, SendMessageLambdaCallback callback);
        client.sendMessages(long fromUid, Set<Long> toUids, byte mType, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);
        client.sendMessages(long fromUid, Set<Long> toUids, byte mType, byte[] message, String attrs, SendMessageLambdaCallback callback);
        client.sendMessages(long fromUid, Set<Long> toUids, byte mType, byte[] message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);
        
* Send Group Message

        // sync methods
        long time = client.sendGroupMessage(long fromUid, long groupId, byte mType, String message, String attrs);
        long time = client.sendGroupMessage(long fromUid, long groupId, byte mType, String message, String attrs, int timeoutInseconds);
        long time = client.sendGroupMessage(long fromUid, long groupId, byte mType, byte[] message, String attrs);
        long time = client.sendGroupMessage(long fromUid, long groupId, byte mType, byte[] message, String attrs, int timeoutInseconds);
        
        // async methods
        client.sendGroupMessage(long fromUid, long groupId, byte mType, String message, String attrs, SendMessageLambdaCallback callback);
        client.sendGroupMessage(long fromUid, long groupId, byte mType, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);
        client.sendGroupMessage(long fromUid, long groupId, byte mType, byte[] message, String attrs, SendMessageLambdaCallback callback);
        client.sendGroupMessage(long fromUid, long groupId, byte mType, byte[] message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);
        
* Send Room Message

        // sync methods
        long time = client.sendRoomMessage(long fromUid, long roomId, byte mType, String message, String attrs);
        long time = client.sendRoomMessage(long fromUid, long roomId, byte mType, String message, String attrs, int timeoutInseconds);
        long time = client.sendRoomMessage(long fromUid, long roomId, byte mType, byte[] message, String attrs);
        long time = client.sendRoomMessage(long fromUid, long roomId, byte mType, byte[] message, String attrs, int timeoutInseconds);
        
        // async methods
        client.sendRoomMessage(long fromUid, long roomId, byte mType, String message, String attrs, SendMessageLambdaCallback callback);
        client.sendRoomMessage(long fromUid, long roomId, byte mType, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);
        client.sendRoomMessage(long fromUid, long roomId, byte mType, byte[] message, String attrs, SendMessageLambdaCallback callback);
        client.sendRoomMessage(long fromUid, long roomId, byte mType, byte[] message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);
        
* Send Broadcast Message

        // sync methods
        long time = client.sendBroadcastMessage(long fromUid, byte mType, String message, String attrs);
        long time = client.sendBroadcastMessage(long fromUid, byte mType, String message, String attrs, int timeoutInseconds);
        long time = client.sendBroadcastMessage(long fromUid, byte mType, byte[] message, String attrs);
        long time = client.sendBroadcastMessage(long fromUid, byte mType, byte[] message, String attrs, int timeoutInseconds);
        
        // async methods
        client.sendBroadcastMessage(long fromUid, byte mType, String message, String attrs, SendMessageLambdaCallback callback);
        client.sendBroadcastMessage(long fromUid, byte mType, String message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);
        client.sendBroadcastMessage(long fromUid, byte mType, byte[] message, String attrs, SendMessageLambdaCallback callback);
        client.sendBroadcastMessage(long fromUid, byte mType, byte[] message, String attrs, SendMessageLambdaCallback callback, int timeoutInseconds);
                
## API Docs

Please refer: 

* [API docs](doc/API.md)       
* [API docs_EN](doc_en/API.md)

## Directory Structure

* **<rtm-server-sdk-java>/src**

    Codes of SDK.
    
* **<rtm-server-sdk-java>/examples**

    Examples codes for using this SDK.
    
* **<rtm-server-sdk-java>/doc**

    API documents in markdown format.