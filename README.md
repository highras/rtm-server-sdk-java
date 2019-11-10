# fpnn rtm sdk java #

#### 依赖 ####
* [fpnn.jar](https://github.com/highras/fpnn-sdk-java)
* [json.jar](https://github.com/stleary/JSON-java)
* [msgpack-core.jar](https://github.com/msgpack/msgpack-java)

#### IPV6 ####
* `SOCKET`链接支持`IPV6`接口
* 兼容`DNS64/NAT64`网络环境

#### 其他 ####
* 首先注册`RTMRegistration`, `RTMClient`可在任意线程中构造和使用(线程安全)
* 异步函数均由子线程呼叫,不要在其中使用仅UI线程的函数,不要阻塞异步函数
* 消息发送接口仅支持`UTF-8`格式编码的`String`类型数据,`Binary`数据需进行`Base64`编解码

#### Events ####
* `event`:
    * `connect`: 连接成功 
    * `error`: 发生异常
    * `close`: 连接关闭
        * `retry`: **(boolean)** 是否执行自动重连

#### 一个例子 ####

```java
import com.fpnn.callback.CallbackData;
import com.fpnn.callback.FPCallback;
import com.fpnn.event.EventData;
import com.fpnn.event.FPEvent;
import com.rtm.RTMClient;
import com.rtm.RTMConfig;
import com.rtm.RTMProcessor;

//注册
RTMClient.RTMRegistration.register();

//构造
RTMClient client = new RTMClient(
    1017,
    "10d09e42-05d3-4d3c-b97a-50c8f27aa6c7",
    "rtm-nx-front.ifunplus.cn:13315",
    true,
    20 * 1000,
    true
);

//添加监听
client.getEvent().addListener("connect", new FPEvent.IListener() {
    @Override
    public void fpEvent(EventData evd) {
        System.out.println("Connected!");
        //发送业务消息
        send();
    }
});
client.getEvent().addListener("close", new FPEvent.IListener() {
    @Override
    public void fpEvent(EventData evd) {
        System.out.println("Closed!");
    }
});

//添加推送监听
RTMProcessor processor = client.getProcessor();
processor.addPushService(RTMConfig.SERVER_PUSH.recvMessage, new RTMProcessor.IService() {
    @Override
    public void Service(Map<String, Object> data) {
        //收到推送数据
        System.out.println(data.toString());
    }
});

// 开启连接
client.connect();

// destroy
// client.destroy();
// client = null;

void send() {
    client.sendMessage(778877, 778899, (byte) 8, "hello !", "", 0, 5 * 1000, new FPCallback.ICallback() {
        @Override
        public void callback(FPCallback cbd) {
            Exception ex = cbd.getException();
            if (ex != null) {
                System.err.println(ex);
                return;
            }
            Object obj = cbd.getPayload();
            if (obj != null) {
                Map<String, Objecgt> payload = (Map<String, Objecgt>) obj;
                System.out.println(payload.toString());
            }
        }
    });
}
```

#### 接口说明 ####
* [API-SDK接口](README-API.md)
* [PushService-RTM服务主动推送接口](README-PUSH.md)
