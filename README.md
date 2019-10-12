# fpnn rtm sdk java #

底层基于NIO实现, 支持FPNN加密.

#### 关于依赖 ####

* [fpnn.jar](https://github.com/highras/fpnn-sdk-java)
* [json.jar](https://github.com/stleary/JSON-java)
* [msgpack-core.jar](https://github.com/msgpack/msgpack-java)

#### 关于线程 ####

* 线程池接口`ThreadPool.IThreadPool` 
    * 默认实现`Executors.newFixedThreadPool(FPConfig.MAX_THREAD_COUNT)`
    * 如需自己管理线程，实现该接口并注册线程池`ThreadPool.getInstance().setPool(IThreadPool value)`

* 不要阻塞事件触发和回调, 否则线程池将被耗尽

#### 关于IPV6 ####

* `SOCKET`链接支持`IPV6`接口
* 兼容`DNS64/NAT64`网络环境

#### 关于连接 ####

* 默认连接会自动保持, 如实现按需连接则需要通过`connect()`和`close()`进行连接或关闭处理
* 或可通过`connect`和`close`事件以及注册`ping`服务来对连接进行管理

#### 关于编码格式 ####

* 消息发送接口仅支持`UTF-8`格式编码的`String`类型数据, `Binary`数据需进行`Base64`编解码

#### 一个例子 ####

```java
import com.fpnn.callback.CallbackData;
import com.fpnn.callback.FPCallback;
import com.fpnn.event.EventData;
import com.fpnn.event.FPEvent;
import com.rtm.RTMClient;
import com.rtm.RTMConfig;

// 创建Client
RTMClient client = new RTMClient(
    1017,
    "10d09e42-05d3-4d3c-b97a-50c8f27aa6c7",
    "rtm-nx-front.ifunplus.cn",
    13315,
    true,
    20 * 1000
);

// 添加监听
client.getEvent().addListener("connect", new FPEvent.IListener() {
    @Override
    public void fpEvent(EventData evd) {
        System.out.println("Connected!");
        // 发送消息
        client.sendMessage(778877, 778899, (byte) 8, "hello !", "", 0, 5 * 1000, new FPCallback.ICallback() {
            @Override
            public void callback(FPCallback cbd) {
                Object obj = cbd.getPayload();

                if (obj != null) {
                    Map payload = (Map) obj;
                    System.out.println("\n[DATA] sendMessage:");
                    System.out.println(payload.toString());
                } else {
                    System.err.println("\n[ERR] sendMessage:");
                    System.err.println(cbd.getException().getMessage());
                }
            }
        });
    }
});

client.getEvent().addListener("close", new FPEvent.IListener() {
    @Override
    public void fpEvent(EventData evd) {
        System.out.println("Closed!");
    }
});

client.getEvent().addListener("error", new FPEvent.IListener() {
    @Override
    public void fpEvent(EventData evd) {
        evd.getException().printStackTrace();
    }
});

// push service
RTMProcessor processor = client.rtmProcessor();

processor.addPushService(RTMConfig.SERVER_PUSH.recvMessage, new RTMProcessor.IService() {
    @Override
    public void Service(Map<String, Object> data) {
        System.out.println("[recvMessage]: " + JsonHelper.getInstance().getJson().toJSON(data));
    }
});

// 开启连接
client.connect();

// destroy
// client.destroy();
// client = null;
```

#### Events ####

* `event`:
    * `connect`: 连接成功 
    * `error`: 发生异常
    * `close`: 连接关闭

#### PushService ####

* `RTMProcessor::addPushService(String name, IService is)`: 添加推送回调
    * `name`: **(String)** 推送服务类型, 参考`RTMConfig.SERVER_PUSH`成员
    * `is`: **(IService)** 回调方法

* `RTMProcessor::removePushService(String name)`: 删除推送回调
    * `name`: **(String)** 推送服务类型, 参考`RTMConfig.SERVER_PUSH`成员

* `RTMProcessor::hasPushService(String name)`: 是否存在推送回调
    * `name`: **(String)** 推送服务类型, 参考`RTMConfig.SERVER_PUSH`成员

* `RTMConfig.SERVER_PUSH`:
    * `ping`: RTMGate主动ping
        * `data`: **(Map(String, Object))**
            * `data`: **(Map)**

    * `pushevent`: RTMGate主动推送事件
        * `data`: **(Map(String, Object))**
            * `data.event`: **(String)** 事件名称, 请参考 `RTMConfig.SERVER_EVENT` 成员
            * `data.uid`: **(long)** 触发者 id
            * `data.time`: **(int)** 触发时间(s)
            * `data.endpoint`: **(String)** 对应的RTMGate地址
            * `data.data`: **(String)** `预留`

    * `pushmsg`: RTMGate主动推送P2P消息
        * `data`: **(Map(String, Object))**
            * `data.from`: **(long)** 发送者 id
            * `data.to`: **(long)** 接收者 id
            * `data.mtype`: **(byte)** 消息类型
            * `data.mid`: **(long)** 消息 id, 当前链接会话内唯一
            * `data.msg`: **(String)** 消息内容
            * `data.attrs`: **(String)** 发送时附加的自定义内容
            * `data.mtime`: **(long)**

    * `pushgroupmsg`: RTMGate主动推送Group消息
        * `data`: **(Map(String, Object))**
            * `data.from`: **(long)** 发送者 id
            * `data.gid`: **(long)** Group id
            * `data.mtype`: **(byte)** 消息类型
            * `data.mid`: **(long)** 消息 id, 当前链接会话内唯一
            * `data.msg`: **(String)** 消息内容
            * `data.attrs`: **(String)** 发送时附加的自定义内容
            * `data.mtime`: **(long)**

    * `pushroommsg`: RTMGate主动推送Room消息
        * `data`: **(Map(String, Object))**
            * `data.from`: **(long)** 发送者 id
            * `data.rid`: **(long)** Room id
            * `data.mtype`: **(byte)** 消息类型
            * `data.mid`: **(long)** 消息 id, 当前链接会话内唯一
            * `data.msg`: **(String)** 消息内容
            * `data.attrs`: **(String)** 发送时附加的自定义内容
            * `data.mtime`: **(long)**

    * `pushfile`: RTMGate主动推送P2P文件
        * `data`: **(Map(String, Object))**
            * `data.from`: **(long)** 发送者 id
            * `data.to`: **(long)** 接收者 id
            * `data.mtype`: **(byte)** 文件类型, 请参考 `RTMConfig.FILE_TYPE` 成员
            * `data.mid`: **(long)** 消息 id, 当前链接会话内唯一
            * `data.msg`: **(String)** 文件获取地址(url)
            * `data.attrs`: **(String)** 发送时附加的自定义内容
            * `data.mtime`: **(long)**

    * `pushgroupfile`: RTMGate主动推送Group文件
        * `data`: **(Map(String, Object))**
            * `data.from`: **(long)** 发送者 id
            * `data.gid`: **(long)** Group id
            * `data.mtype`: **(byte)** 文件类型, 请参考 `RTMConfig.FILE_TYPE` 成员
            * `data.mid`: **(long)** 消息 id, 当前链接会话内唯一
            * `data.msg`: **(String)** 文件获取地址(url)
            * `data.attrs`: **(String)** 发送时附加的自定义内容
            * `data.mtime`: **(long)**

    * `pushroomfile`: RTMGate主动推送Room文件
        * `data`: **(Map(String, Object))**
            * `data.from`: **(long)** 发送者 id
            * `data.rid`: **(long)** Room id
            * `data.mtype`: **(byte)** 文件类型, 请参考 `RTMConfig.FILE_TYPE` 成员
            * `data.mid`: **(long)** 消息 id, 当前链接会话内唯一
            * `data.msg`: **(String)** 文件获取地址(url)
            * `data.attrs`: **(String)** 发送时附加的自定义内容
            * `data.mtime`: **(long)**

    * `pushchat`: RTMGate主动推送P2P消息
        * `data`: **(Map(String, Object))**
            * `data.from`: **(long)** 发送者 id
            * `data.to`: **(long)** 接收者 id
            * `data.mid`: **(long)** 消息 id, 当前链接会话内唯一
            * `data.attrs`: **(String)** 发送时附加的自定义内容
            * `data.mtime`: **(long)**
            * `data.msg`: **(JsonString)** 消息内容
                * `source`: **(String)** 原始消息语言类型, 参考`RTMConfig.TRANS_LANGUAGE`成员
                * `target`: **(String)** 翻译后的语言类型, 参考`RTMConfig.TRANS_LANGUAGE`成员
                * `sourceText`: **(String)** 原始消息
                * `targetText`: **(String)** 翻译后的消息

    * `pushgroupchat`: RTMGate主动推送Group消息
        * `data`: **(Map(String, Object))**
            * `data.from`: **(long)** 发送者 id
            * `data.gid`: **(long)** Group id
            * `data.mid`: **(long)** 消息 id, 当前链接会话内唯一
            * `data.attrs`: **(String)** 发送时附加的自定义内容
            * `data.mtime`: **(long)**
            * `data.msg`: **(JsonString)** 消息内容
                * `source`: **(String)** 原始消息语言类型, 参考`RTMConfig.TRANS_LANGUAGE`成员
                * `target`: **(String)** 翻译后的语言类型, 参考`RTMConfig.TRANS_LANGUAGE`成员
                * `sourceText`: **(String)** 原始消息
                * `targetText`: **(String)** 翻译后的消息 

    * `pushroomchat`: RTMGate主动推送Room消息
        * `data`: **(Map(String, Object))**
            * `data.from`: **(long)** 发送者 id
            * `data.rid`: **(long)** Room id
            * `data.mid`: **(long)** 消息 id, 当前链接会话内唯一
            * `data.attrs`: **(String)** 发送时附加的自定义内容
            * `data.mtime`: **(long)**
            * `data.msg`: **(JsonString)** 消息内容
                * `source`: **(String)** 原始消息语言类型, 参考`RTMConfig.TRANS_LANGUAGE`成员
                * `target`: **(String)** 翻译后的语言类型, 参考`RTMConfig.TRANS_LANGUAGE`成员
                * `sourceText`: **(String)** 原始消息
                * `targetText`: **(String)** 翻译后的消息 

#### API ####

* `constructor(int pid, String secret, String host, int port, boolean reconnect, int timeout)`: 构造RTMClient
    * `pid`: **(int)** 应用编号, RTM提供
    * `secret`: **(String)** 应用加密, RTM提供
    * `host`: **(String)** 地址, RTM提供
    * `port`: **(int)** 端口, RTM提供
    * `reconnect`: **(boolean)** 是否自动重连
    * `timeout`: **(int)** 超时时间(ms), 默认: `30 * 1000`

* `constructor(int pid, String secret, String endpoint, boolean reconnect, int timeout)`: 构造RTMClient
    * `pid`: **(int)** 应用编号, RTM提供
    * `secret`: **(String)** 应用加密, RTM提供
    * `endpoint`: **(String)** 地址与端口, RTM提供
    * `reconnect`: **(boolean)** 是否自动重连
    * `timeout`: **(int)** 超时时间(ms), 默认: `30 * 1000`

* `rtmProcessor`: **(RTMProcessor)** 监听PushService的句柄

* `destroy()`: 断开连接并销毁

* `connect()`: 开启连接(非加密模式)

* `connect(String curve, byte[] derKey, boolean streamMode, boolean reinforce)`: 开启加密连接
    * `curve`: **(String)** 加密协议
    * `derKey`: **(byte[])** 加密公钥, 秘钥文件RTM提供
    * `streamMode`: **(boolean)** 是否开启流加密, `目前Java API不支持流加密`
    * `reinforce`: **(boolean)** 是否开启加强 ` 128 : 256 `

* `connect(String curve, String derPath, boolean streamMode, boolean reinforce)`: 开启加密连接
    * `curve`: **(String)** 加密协议
    * `derPath`: **(String)** 加密公钥文件路径, 秘钥文件RTM提供
    * `streamMode`: **(boolean)** 是否开启流加密, `目前Java API不支持流加密`
    * `reinforce`: **(boolean)** 是否开启加强 ` 128 : 256 `

* `getToken(long uid, int timeout, FPCallback.ICallback callback)`: 获取auth token
    * `uid`: **(long)** 用户 id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(Map(token:String))**
            * `exception`: **(Exception)**

* `kickout(long uid, String ce, int timeout, FPCallback.ICallback callback)`: 踢掉一个用户或者一个链接
    * `uid`: **(long)** 用户 id
    * `ce`: **(String)** 踢掉`ce`对应链接, 多用户登录情况
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(Map)**
            * `exception`: **(Exception)**

* `addDevice(long uid, String apptype, String devicetoken, int timeout, FPCallback.ICallback callback)`: 添加设备, 应用信息
    * `uid`: **(long)** 用户 id
    * `apptype`: **(String)** 应用信息
    * `devicetoken`: **(String)** 设备 token
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(Map)**
            * `exception`: **(Exception)**
        
* `removeDevice(long uid, String devicetoken, int timeout, FPCallback.ICallback callback)`: 删除设备, 应用信息
    * `uid`: **(long)** 用户 id
    * `devicetoken`: **(String)** 设备 token
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(Map)**
            * `exception`: **(Exception)**

* `sendMessage(long from, long to, byte mtype, String msg, String attrs, long mid, int timeout, FPCallback.ICallback callback)`: 发送消息
    * `from`: **(long)** 发送方 id
    * `to`: **(long)** 接收方uid
    * `mtype`: **(byte)** 消息类型
    * `msg`: **(String)** 消息内容
    * `attrs`: **(String)** 消息附加信息, 没有可传`""`
    * `mid`: **(long)** 消息 id, 用于过滤重复消息, 非重发时为`0`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `mid`: **(long)**
            * `payload`: **(Map(mtime:long))**
            * `exception`: **(Exception)**

* `sendMessages(long from, List<Long> tos, byte mtype, String msg, String attrs, long mid, int timeout, FPCallback.ICallback callback)`: 发送多人消息
    * `from`: **(long)** 发送方 id
    * `tos`: **(List(Long))** 接收方uids
    * `mtype`: **(byte)** 消息类型
    * `msg`: **(String)** 消息内容
    * `attrs`: **(String)** 消息附加信息, 没有可传`""`
    * `mid`: **(long)** 消息 id, 用于过滤重复消息, 非重发时为`0`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `mid`: **(long)**
            * `payload`: **(Map(mtime:long))**
            * `exception`: **(Exception)**

* `sendGroupMessage(long from, long gid, byte mtype, String msg, String attrs, long mid, int timeout, FPCallback.ICallback callback)`: 发送group消息
    * `from`: **(long)** 发送方 id
    * `gid`: **(long)** group id
    * `mtype`: **(byte)** 消息类型
    * `msg`: **(String)** 消息内容
    * `attrs`: **(String)** 消息附加信息, 可传`""`
    * `mid`: **(long)** 消息 id, 用于过滤重复消息, 非重发时为`0`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `mid`: **(long)**
            * `payload`: **(Map(mtime:long))**
            * `exception`: **(Exception)**

* `sendRoomMessage(long from, long rid, byte mtype, String msg, String attrs, long mid, int timeout, FPCallback.ICallback callback)`: 发送room消息
    * `from`: **(long)** 发送方 id
    * `rid`: **(long)** room id
    * `mtype`: **(byte)** 消息类型
    * `msg`: **(String)** 消息内容
    * `attrs`: **(String)** 消息附加信息, 可传`""`
    * `mid`: **(long)** 消息 id, 用于过滤重复消息, 非重发时为`0`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `mid`: **(long)**
            * `payload`: **(Map(mtime:long))**
            * `exception`: **(Exception)**

* `broadcastMessage(long from, byte mtype, String msg, String attrs, long mid, int timeout, FPCallback.ICallback callback)`: 广播消息(andmin id)
    * `from`: **(long)** admin id
    * `mtype`: **(byte)** 消息类型
    * `msg`: **(String)** 消息内容
    * `attrs`: **(String)** 消息附加信息, 可传`""`
    * `mid`: **(long)** 消息 id, 用于过滤重复消息, 非重发时为`0`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `mid`: **(long)**
            * `payload`: **(Map(mtime:long))**
            * `exception`: **(Exception)**

* `getGroupMessage(long gid, boolean desc, int num, long begin, long end, long lastid, byte[] mtypes, int timeout, FPCallback.ICallback callback)`: 获取Group历史消息
    * `gid`: **(long)** Group id
    * `desc`: **(boolean)** `true`: 则从`end`的时间戳开始倒序翻页, `false`: 则从`begin`的时间戳顺序翻页
    * `num`: **(int)** 获取数量, **一次最多获取20条, 建议10条**
    * `begin`: **(long)** 开始时间戳, 毫秒, 默认`0`, 条件：`>=`
    * `end`: **(long)** 结束时间戳, 毫秒, 默认`0`, 条件：`<=`
    * `lastid`: **(long)** 最后一条消息的id, 第一次默认传`0`, 条件：`> or <`
    * `mtypes`: **(byte[])** 获取历史消息的消息类型集合
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `exception`: **(Exception)**
            * `payload`: **(Map(num:int,lastid:long,begin:long,end:long,msgs:List(GroupMsg)))**
                * `GroupMsg.id` **(long)**
                * `GroupMsg.from` **(long)**
                * `GroupMsg.mtype` **(byte)**
                * `GroupMsg.mid` **(long)**
                * `GroupMsg.deleted` **(boolean)**
                * `GroupMsg.msg` **(String)**
                * `GroupMsg.attrs` **(String)**
                * `GroupMsg.mtime` **(long)**

* `getRoomMessage(long rid, boolean desc, int num, long begin, long end, long lastid, byte[] mtypes, int timeout, FPCallback.ICallback callback)`: 获取Room历史消息
    * `rid`: **(long)** Room id
    * `desc`: **(boolean)** `true`: 则从`end`的时间戳开始倒序翻页, `false`: 则从`begin`的时间戳顺序翻页
    * `num`: **(int)** 获取数量, **一次最多获取20条, 建议10条**
    * `begin`: **(long)** 开始时间戳, 毫秒, 默认`0`, 条件：`>=`
    * `end`: **(long)** 结束时间戳, 毫秒, 默认`0`, 条件：`<=`
    * `lastid`: **(long)** 最后一条消息的id, 第一次默认传`0`, 条件：`> or <`
    * `mtypes`: **(byte[])** 获取历史消息的消息类型集合
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `exception`: **(Exception)**
            * `payload`: **(Map(num:int,lastid:long,begin:long,end:long,msgs:List(RoomMsg)))**
                * `RoomMsg.id` **(long)**
                * `RoomMsg.from` **(long)**
                * `RoomMsg.mtype` **(byte)**
                * `RoomMsg.mid` **(long)**
                * `RoomMsg.deleted` **(boolean)**
                * `RoomMsg.msg` **(String)**
                * `RoomMsg.attrs` **(String)**
                * `RoomMsg.mtime` **(long)**

* `getBroadcastMessage(boolean desc, int num, long begin, long end, long lastid, byte[] mtypes, int timeout, FPCallback.ICallback callback)`: 获取广播历史消息
    * `desc`: **(boolean)** `true`: 则从`end`的时间戳开始倒序翻页, `false`: 则从`begin`的时间戳顺序翻页
    * `num`: **(int)** 获取数量, **一次最多获取20条, 建议10条**
    * `begin`: **(long)** 开始时间戳, 毫秒, 默认`0`, 条件：`>=`
    * `end`: **(long)** 结束时间戳, 毫秒, 默认`0`, 条件：`<=`
    * `lastid`: **(long)** 最后一条消息的id, 第一次默认传`0`, 条件：`> or <`
    * `mtypes`: **(byte[])** 获取历史消息的消息类型集合
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `exception`: **(Exception)**
            * `payload`: **(Map(num:int,lastid:long,begin:long,end:long,msgs:List(BroadcastMsg)))**
                * `BroadcastMsg.id` **(long)**
                * `BroadcastMsg.from` **(long)**
                * `BroadcastMsg.mtype` **(byte)**
                * `BroadcastMsg.mid` **(long)**
                * `BroadcastMsg.deleted` **(boolean)**
                * `BroadcastMsg.msg` **(String)**
                * `BroadcastMsg.attrs` **(String)**
                * `BroadcastMsg.mtime` **(long)**

* `getP2PMessage(long uid, long ouid, boolean desc, int num, long begin, long end, long lastid, byte[] mtypes, int timeout, FPCallback.ICallback callback)`: 获取P2P历史消息
    * `uid`: **(long)** 获取和两个用户之间的历史消息
    * `ouid`: **(long)** 获取和两个用户之间的历史消息
    * `desc`: **(boolean)** `true`: 则从`end`的时间戳开始倒序翻页, `false`: 则从`begin`的时间戳顺序翻页
    * `num`: **(int)** 获取数量, **一次最多获取20条, 建议10条**
    * `begin`: **(long)** 开始时间戳, 毫秒, 默认`0`, 条件：`>=`
    * `end`: **(long)** 结束时间戳, 毫秒, 默认`0`, 条件：`<=`
    * `lastid`: **(long)** 最后一条消息的id, 第一次默认传`0`, 条件：`> or <`
    * `mtypes`: **(byte[])** 获取历史消息的消息类型集合
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `exception`: **(Exception)**
            * `payload`: **(Map(num:int,lastid:long,begin:long,end:long,msgs:List(P2PMsg)))**
                * `P2PMsg.id` **(long)**
                * `P2PMsg.direction` **(byte)**
                * `P2PMsg.mtype` **(byte)**
                * `P2PMsg.mid` **(long)**
                * `P2PMsg.deleted` **(boolean)**
                * `P2PMsg.msg` **(String)**
                * `P2PMsg.attrs` **(String)**
                * `P2PMsg.mtime` **(long)**

* `deleteMessage(long mid, long from, long xid, byte type, int timeout, FPCallback.ICallback callback)`: 删除消息
    * `mid`: **(long)** 消息 id
    * `from`: **(long)** 发送方 id
    * `xid`: **(long)** 接收放 id, `rid/gid/uid`
    * `type`: **(byte)** 消息发送分类 `1:P2P, 2:Group, 3:Room, 4:Broadcast`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(Map)**
            * `exception`: **(Exception)**

* `sendChat(long from, long to, String msg, String attrs, long mid, int timeout, FPCallback.ICallback callback)`: 发送消息, `mtype=30`
    * `from`: **(long)** 发送方 id
    * `to`: **(long)** 接收方uid
    * `msg`: **(String)** 消息内容
    * `attrs`: **(String)** 消息附加信息, 没有可传`""`
    * `mid`: **(long)** 消息 id, 用于过滤重复消息, 非重发时为`0`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `mid`: **(long)**
            * `payload`: **(Map(mtime:long))**
            * `exception`: **(Exception)**

* `sendChats(long from, List<Long> tos, String msg, String attrs, long mid, int timeout, FPCallback.ICallback callback)`: 发送多人消息, `mtype=30`
    * `from`: **(long)** 发送方 id
    * `tos`: **(List(Long))** 接收方uids
    * `msg`: **(String)** 消息内容
    * `attrs`: **(String)** 消息附加信息, 没有可传`""`
    * `mid`: **(long)** 消息 id, 用于过滤重复消息, 非重发时为`0`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `mid`: **(long)**
            * `payload`: **(Map(mtime:long))**
            * `exception`: **(Exception)**

* `sendGroupChat(long from, long gid, String msg, String attrs, long mid, int timeout, FPCallback.ICallback callback)`: 发送group消息, `mtype=30`
    * `from`: **(long)** 发送方 id
    * `gid`: **(long)** group id
    * `msg`: **(String)** 消息内容
    * `attrs`: **(String)** 消息附加信息, 可传`""`
    * `mid`: **(long)** 消息 id, 用于过滤重复消息, 非重发时为`0`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `mid`: **(long)**
            * `payload`: **(Map(mtime:long))**
            * `exception`: **(Exception)**

* `sendRoomChat(long from, long rid, String msg, String attrs, long mid, int timeout, FPCallback.ICallback callback)`: 发送room消息, `mtype=30`
    * `from`: **(long)** 发送方 id
    * `rid`: **(long)** room id
    * `msg`: **(String)** 消息内容
    * `attrs`: **(String)** 消息附加信息, 可传`""`
    * `mid`: **(long)** 消息 id, 用于过滤重复消息, 非重发时为`0`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `mid`: **(long)**
            * `payload`: **(Map(mtime:long))**
            * `exception`: **(Exception)**

* `broadcastChat(long from, String msg, String attrs, long mid, int timeout, FPCallback.ICallback callback)`: 广播消息(andmin id), `mtype=30`
    * `from`: **(long)** admin id
    * `msg`: **(String)** 消息内容
    * `attrs`: **(String)** 消息附加信息, 可传`""`
    * `mid`: **(long)** 消息 id, 用于过滤重复消息, 非重发时为`0`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `mid`: **(long)**
            * `payload`: **(Map(mtime:long))**
            * `exception`: **(Exception)**

* `getGroupChat(long gid, boolean desc, int num, long begin, long end, long lastid, int timeout, FPCallback.ICallback callback)`: 获取Group历史消息, `mtypes=new byte[] { 30 }`
    * `gid`: **(long)** Group id
    * `desc`: **(boolean)** `true`: 则从`end`的时间戳开始倒序翻页, `false`: 则从`begin`的时间戳顺序翻页
    * `num`: **(int)** 获取数量, **一次最多获取20条, 建议10条**
    * `begin`: **(long)** 开始时间戳, 毫秒, 默认`0`, 条件：`>=`
    * `end`: **(long)** 结束时间戳, 毫秒, 默认`0`, 条件：`<=`
    * `lastid`: **(long)** 最后一条消息的id, 第一次默认传`0`, 条件：`> or <`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `exception`: **(Exception)**
            * `payload`: **(Map(num:int,lastid:long,begin:long,end:long,msgs:List(GroupMsg)))**
                * `GroupMsg.id` **(long)**
                * `GroupMsg.from` **(long)**
                * `GroupMsg.mid` **(long)**
                * `GroupMsg.deleted` **(boolean)**
                * `GroupMsg.msg` **(String)**
                * `GroupMsg.attrs` **(String)**
                * `GroupMsg.mtime` **(long)**

* `getRoomChat(long rid, boolean desc, int num, long begin, long end, long lastid, int timeout, FPCallback.ICallback callback)`: 获取Room历史消息, `mtypes=new byte[] { 30 }`
    * `rid`: **(long)** Room id
    * `desc`: **(boolean)** `true`: 则从`end`的时间戳开始倒序翻页, `false`: 则从`begin`的时间戳顺序翻页
    * `num`: **(int)** 获取数量, **一次最多获取20条, 建议10条**
    * `begin`: **(long)** 开始时间戳, 毫秒, 默认`0`, 条件：`>=`
    * `end`: **(long)** 结束时间戳, 毫秒, 默认`0`, 条件：`<=`
    * `lastid`: **(long)** 最后一条消息的id, 第一次默认传`0`, 条件：`> or <`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `exception`: **(Exception)**
            * `payload`: **(Map(num:int,lastid:long,begin:long,end:long,msgs:List(RoomMsg)))**
                * `RoomMsg.id` **(long)**
                * `RoomMsg.from` **(long)**
                * `RoomMsg.mid` **(long)**
                * `RoomMsg.deleted` **(boolean)**
                * `RoomMsg.msg` **(String)**
                * `RoomMsg.attrs` **(String)**
                * `RoomMsg.mtime` **(long)**

* `getBroadcastChat(boolean desc, int num, long begin, long end, long lastid, int timeout, FPCallback.ICallback callback)`: 获取广播历史消息, `mtypes=new byte[] { 30 }`
    * `desc`: **(boolean)** `true`: 则从`end`的时间戳开始倒序翻页, `false`: 则从`begin`的时间戳顺序翻页
    * `num`: **(int)** 获取数量, **一次最多获取20条, 建议10条**
    * `begin`: **(long)** 开始时间戳, 毫秒, 默认`0`, 条件：`>=`
    * `end`: **(long)** 结束时间戳, 毫秒, 默认`0`, 条件：`<=`
    * `lastid`: **(long)** 最后一条消息的id, 第一次默认传`0`, 条件：`> or <`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `exception`: **(Exception)**
            * `payload`: **(Map(num:int,lastid:long,begin:long,end:long,msgs:List(BroadcastMsg)))**
                * `BroadcastMsg.id` **(long)**
                * `BroadcastMsg.from` **(long)**
                * `BroadcastMsg.mid` **(long)**
                * `BroadcastMsg.deleted` **(boolean)**
                * `BroadcastMsg.msg` **(String)**
                * `BroadcastMsg.attrs` **(String)**
                * `BroadcastMsg.mtime` **(long)**

* `getP2PChat(long uid, long ouid, boolean desc, int num, long begin, long end, long lastid, int timeout, FPCallback.ICallback callback)`: 获取P2P历史消息, `mtypes=new byte[] { 30 }`
    * `uid`: **(long)** 获取和两个用户之间的历史消息
    * `ouid`: **(long)** 获取和两个用户之间的历史消息
    * `desc`: **(boolean)** `true`: 则从`end`的时间戳开始倒序翻页, `false`: 则从`begin`的时间戳顺序翻页
    * `num`: **(int)** 获取数量, **一次最多获取20条, 建议10条**
    * `begin`: **(long)** 开始时间戳, 毫秒, 默认`0`, 条件：`>=`
    * `end`: **(long)** 结束时间戳, 毫秒, 默认`0`, 条件：`<=`
    * `lastid`: **(long)** 最后一条消息的id, 第一次默认传`0`, 条件：`> or <`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `exception`: **(Exception)**
            * `payload`: **(Map(num:int,lastid:long,begin:long,end:long,msgs:List(P2PMsg)))**
                * `P2PMsg.id` **(long)**
                * `P2PMsg.direction` **(byte)**
                * `P2PMsg.mid` **(long)**
                * `P2PMsg.deleted` **(boolean)**
                * `P2PMsg.msg` **(String)**
                * `P2PMsg.attrs` **(String)**
                * `P2PMsg.mtime` **(long)**

* `deleteChat(long mid, long from, long xid, byte type, int timeout, FPCallback.ICallback callback)`: 删除消息
    * `mid`: **(long)** 消息 id
    * `from`: **(long)** 发送方 id
    * `xid`: **(long)** 接收放 id, `rid/gid/uid`
    * `type`: **(byte)** 消息发送分类 `1:P2P, 2:Group, 3:Room, 4:Broadcast`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(Map)**
            * `exception`: **(Exception)**

* `translate(String text, String src, String dst, String type, String profanity, int timeout, FPCallback.ICallback callback)`: 翻译消息, 返回{source:原始消息语言类型,target:翻译后的语言类型,sourceText:原始消息,targetText:翻译后的消息}
    * `text`: **(String)** 待翻译的原始消息
    * `src`: **(String)** 待翻译的消息的语言类型, 参考RTMConfig.TRANS_LANGUAGE成员
    * `dst`: **(String)** 本次翻译的目标语言类型, 参考RTMConfig.TRANS_LANGUAGE成员
    * `type`: **(String)** 可选值为`chat`或`mail`, 默认:`chat`
    * `profanity`: **(String)** 敏感语过滤, 设置为以下三项之一: `off` `stop` `censor`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(Map(source:String,target:String,sourceText:String,targetText:String))**
            * `exception`: **(Exception)**

* `profanity(String text, String action, int timeout, FPCallback.ICallback callback)`: 敏感词过滤, 返回过滤后的字符串或者以错误形式返回
    * `text`: **(String)** 待检查文本
    * `action`: **(String)** 检查结果返回形式, `stop`: 以错误形式返回, `censor`: 用`*`替换敏感词
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(Map(text:String))**
            * `exception`: **(Exception)**

* `fileToken(long from, String cmd, List<Long> tos, long to, long rid, long gid, int timeout, FPCallback.ICallback callback)`: 获取发送文件的token
    * `from`: **(long)** 发送方 id
    * `cmd`: **(string)** 文件发送方式`sendfile | sendfiles | sendroomfile | sendgroupfile | broadcastfile`
    * `tos`: **(List(Long))** 接收方 uids
    * `to`: **(long)** 接收方 uid
    * `rid`: **(long)** Room id
    * `gid`: **(long)** Group id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `exception`: **(Exception)**
            * `payload`: **(Map(token:String,endpoint:String))**

* `getOnlineUsers(List<Long> uids, int timeout, FPCallback.ICallback callback)`: 获取在线用户, 每次最多获取200个
    * `uids`: **(List(Long))** 多个用户 id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(List(Long))**
            * `exception`: **(Exception)**

* `addProjectBlack(long uid, int btime, int timeout, FPCallback.ICallback callback)`: 阻止用户消息(project)
    * `uid`: **(long)** 用户 id
    * `btime`: **(int)** 阻止时间(s)
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(Map)**
            * `exception`: **(Exception)**

* `removeProjectBlack(int uid, int timeout, FPCallback.ICallback callback)`: 取消阻止(project)
    * `uid`: **(long)** 用户 id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(Map)**
            * `exception`: **(Exception)**

* `setUserInfo(long uid, String oinfo, String pinfo, int timeout, FPCallback.ICallback callback)`: 设置用户的公开信息和私有信息
    * `uid`: **(long)** 用户 id
    * `oinfo`: **(String)** 公开信息
    * `pinfo`: **(String)** 私有信息
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(Map)**
            * `exception`: **(Exception)**

* `getUserInfo(long uid, int timeout, FPCallback.ICallback callback)`: 获取用户的公开信息和私有信息
    * `uid`: **(long)** 用户 id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(Map(oinfo:String,pinfo:String))**
            * `exception`: **(Exception)**

* `getUserOpenInfo(List<Long> uids, int timeout, FPCallback.ICallback callback)`: 获取其他用户的公开信息, 每次最多获取100人
    * `uids`: **(List<Long>)** 多个用户 id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(Map(String,String))**
            * `exception`: **(Exception)**

* `isProjectBlack(long uid, int timeout, FPCallback.ICallback callback)`: 检查阻止(project)
    * `uid`: **(long)** 用户 id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(boolean)**
            * `exception`: **(Exception)**

* `addFriends(long uid, List<Long> friends, int timeout, FPCallback.ICallback callback)`: 添加好友, 每次最多添加100人
    * `uid`: **(long)** 用户 id
    * `friends`: **(List(Long))** 多个好友 id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(Map)**
            * `exception`: **(Exception)**

* `deleteFriends(long uid, List<Long> friends, int timeout, FPCallback.ICallback callback)`: 删除好友, 每次最多删除100人
    * `uid`: **(long)** 用户 id
    * `friends`: **(List(Long))** 多个好友 id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(Map)**
            * `exception`: **(Exception)**

* `getFriends(long uid, int timeout, FPCallback.ICallback callback)`: 获取好友
    * `uid`: **(long)** 用户 id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(List(Long))**
            * `exception`: **(Exception)**

* `isFriend(long uid, long fuid, int timeout, FPCallback.ICallback callback)`: 判断好友关系
    * `uid`: **(long)** 用户 id
    * `fuid`: **(long)** 好友 id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(Map(ok:boolean))**
            * `exception`: **(Exception)**

* `isFriends(long uid, List<Long> fuids, int timeout, FPCallback.ICallback callback)`: 过滤好友关系, 每次最多过滤100人
    * `uid`: **(long)** 用户 id
    * `fuids`: **(List(Long))** 多个好友 id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(List(Long))**
            * `exception`: **(Exception)**

* `addGroupMembers(long gid, List<Long> uids, int timeout, FPCallback.ICallback callback)`: 添加group成员, 每次最多添加100人
    * `gid`: **(long)** group id
    * `uids`: **(List(Long))** 多个用户 id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(Map)**
            * `exception`: **(Exception)**

* `deleteGroupMembers(long gid, List<Long> uids, int timeout, FPCallback.ICallback callback)`:  删除group成员, 每次最多删除100人
    * `gid`: **(long)** group id
    * `uids`: **(List(Long))** 多个用户 id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(Map)**
            * `exception`: **(Exception)**

* `deleteGroup(long gid, int timeout, FPCallback.ICallback callback)`: 删除group
    * `gid`: **(long)** group id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(Map)**
            * `exception`: **(Exception)**

* `getGroupMembers(long gid, int timeout, FPCallback.ICallback callback)`: 获取group成员
    * `gid`: **(long)** group id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(List(Long))**
            * `exception`: **(Exception)**

* `isGroupMember(long gid, long uid, int timeout, FPCallback.ICallback callback)`: 是否group成员
    * `gid`: **(long)** group id
    * `uid`: **(long)** 用户 id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(Map(ok:boolean))**
            * `exception`: **(Exception)**

* `getUserGroups(long uid, int timeout, FPCallback.ICallback callback)`: 获取用户的group
    * `uid`: **(long)** 用户 id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(List(Long))**
            * `exception`: **(Exception)**

* `addGroupBan(long gid, long uid, int btime, int timeout, FPCallback.ICallback callback)`: 阻止用户消息(group)
    * `gid`: **(long)** group id
    * `uid`: **(long)** 用户 id
    * `btime`: **(int)** 阻止时间(s)
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(Map)**
            * `exception`: **(Exception)**

* `removeGroupBan(long gid, long uid, int timeout, FPCallback.ICallback callback)`: 取消阻止(group)
    * `gid`: **(long)** group id
    * `uid`: **(long)** 用户 id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(Map)**
            * `exception`: **(Exception)**

* `isBanOfGroup(long gid, long uid, int timeout, FPCallback.ICallback callback)`: 检查阻止(group)
    * `gid`: **(long)** group id
    * `uid`: **(long)** 用户 id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(Map(ok:boolean))**
            * `exception`: **(Exception)**

* `setGroupInfo(long gid, String oinfo, String pinfo, int timeout, FPCallback.ICallback callback)`: 设置Group的公开信息和私有信息
    * `gid`: **(long)** group id
    * `oinfo`: **(String)** 公开信息
    * `pinfo`: **(String)** 私有信息
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(Map)**
            * `exception`: **(Exception)**

* `getGroupInfo(long gid, int timeout, FPCallback.ICallback callback)`: 获取Group的公开信息和私有信息
    * `gid`: **(long)** group id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(Map(oinfo:String,pinfo:String))**
            * `exception`: **(Exception)**

* `addRoomBan(long rid, long uid, int btime, int timeout, FPCallback.ICallback callback)`: 阻止用户消息(room)
    * `rid`: **(long)** room id
    * `uid`: **(long)** 用户 id
    * `btime`: **(int)** 阻止时间(s)
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(Map)**
            * `exception`: **(Exception)**

* `removeRoomBan(long rid, long uid, int timeout, FPCallback.ICallback callback)`: 取消阻止(room)
    * `rid`: **(long)** room id
    * `uid`: **(long)** 用户 id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(Map)**
            * `exception`: **(Exception)**

* `isBanOfRoom(long rid, long uid, int timeout, FPCallback.ICallback callback)`: 检查阻止(room)
    * `rid`: **(long)** room id
    * `uid`: **(long)** 用户 id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(Map(ok:boolean))**
            * `exception`: **(Exception)**

* `addRoomMember(long rid, long uid, int timeout, FPCallback.ICallback callback)`: 添加Room成员
    * `rid`: **(long)** room id
    * `uid`: **(long)** 用户 id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(Map)**
            * `exception`: **(Exception)**

* `deleteRoomMember(long rid, long uid, int timeout, FPCallback.ICallback callback)`: 删除Room成员
    * `rid`: **(long)** room id
    * `uid`: **(long)** 用户 id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(Map)**
            * `exception`: **(Exception)**

* `setRoomInfo(long rid, String oinfo, String pinfo, int timeout, FPCallback.ICallback callback)`: 设置Room的公开信息和私有信息
    * `rid`: **(long)** room id
    * `oinfo`: **(String)** 公开信息
    * `pinfo`: **(String)** 私有信息
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(Map)**
            * `exception`: **(Exception)**

* `getRoomInfo(long rid, int timeout, FPCallback.ICallback callback)`: 获取Room的公开信息和私有信息
    * `rid`: **(long)** room id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(Map(oinfo:String,pinfo:String))**
            * `exception`: **(Exception)**

* `addEvtListener(List<Long> gids, List<Long> rids, List<Long> uids, List<String> events, int timeout, FPCallback.ICallback callback)`: 添加 `事件` / `消息` 监听, 仅对当前链接有效, 增量添加
    * `gids`: **(List(Long))** 多个Group id
    * `rids`: **(List(Long))** 多个Room id
    * `uids`: **(List(Long))** 多个用户 id
    * `events`: **(List(String))** 多个事件名称, 请参考 `RTMConfig.SERVER_EVENT` 成员
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(Map)**
            * `exception`: **(Exception)**

* `removeEvtListener(List<Long> gids, List<Long> rids, List<Long> uids, List<String> events, int timeout, FPCallback.ICallback callback)`: 删除 `事件` / `消息` 监听, 仅对当前链接有效, 增量取消
    * `gids`: **(List(Long))** 多个Group id
    * `rids`: **(List(Long))** 多个Room id
    * `List<Long>`: **(boolean)** 多个用户 id
    * `events`: **(List(String))** 多个事件名称, 请参考 `RTMConfig.SERVER_EVENT` 成员
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(Map)**
            * `exception`: **(Exception)**

* `setEvtListener(boolean p2p, boolean group, boolean room, boolean ev, int timeout, FPCallback.ICallback callback)`: 更新 `事件` / `消息` 监听, 仅对当前链接有效, 全量覆盖, 每个链接以本次设置为准
    * `p2p`: **(boolean)** `true`: 忽略uids, 监听全部p2p的 `事件` / `消息`,  `false`: 则只监听uids中的 `事件` / `消息`
    * `group`: **(boolean)** `true`: 忽略gids, 监听全部Group的 `事件` / `消息`,  `false`: 则只监听gids中的 `事件` / `消息`
    * `room`: **(boolean)** `true`: 忽略rids, 监听全部Room的 `事件` / `消息`,  `false`: 则只监听rids中的 `事件` / `消息`
    * `ev`: **(boolean)** `true`: 忽略events, 监听全部 `事件`,  `false`: 则只监听events中的 `事件`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(Map)**
            * `exception`: **(Exception)**
        
* `setEvtListener(List<Long> gids, List<Long> rids, List<Long> uids, List<String> events, int timeout, FPCallback.ICallback callback)`: 更新 `事件` / `消息` 监听, 仅对当前链接有效, 全量覆盖, 每个链接以本次设置为准
    * `gids`: **(List(Long))** 多个Group id
    * `rids`: **(List(Long))** 多个Room id
    * `uids`: **(List(Long))** 多个用户 id
    * `events`: **(List(String))** 多个事件名称, 请参考 `RTMConfig.SERVER_EVENT` 成员
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(Map)**
            * `exception`: **(Exception)**

* `dataGet(long uid, String key, int timeout, FPCallback.ICallback callback)`: 获取存储的数据信息
    * `uid`: **(long)** 用户 id
    * `key`: **(String)** 存储数据对应键值, 最长`128字节`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(Map(val:String))**
            * `exception`: **(Exception)**

* `dataSet(long uid, String key, String val, int timeout, FPCallback.ICallback callback)`: 设置存储的数据信息
    * `uid`: **(long)** 用户 id
    * `key`: **(String)** 存储数据对应键值, 最长`128字节`
    * `val`: **(String)** 存储数据实际内容, 最长`1024 * 1024 * 2字节`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(Map)**
            * `exception`: **(Exception)**

* `dataDelete(long uid, String key, int timeout, FPCallback.ICallback callback)`: 删除存储的数据信息
    * `uid`: **(long)** 用户 id
    * `key`: **(String)** 存储数据对应键值, 最长`128字节`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(Map)**
            * `exception`: **(Exception)**
        
* `sendFile(long from, long to, byte mtype, byte[] fileBytes, long mid, int timeout, FPCallback.ICallback callback)`: 发送文件
    * `from`: **(long)** 发送方 id
    * `to`: **(long)** 接收方 uid
    * `mtype`: **(byte)** 文件类型
    * `fileBytes`: **(byte[])** 文件内容
    * `mid`: **(long)** 消息 id, 用于过滤重复消息, 非重发时为`0`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `mid`: **(long)**
            * `payload`: **(Map(mtime:long))**
            * `exception`: **(Exception)**

* `sendFiles(long from, List<Long>tos, byte mtype, byte[] fileBytes, long mid, int timeout, FPCallback.ICallback callback)`: 给多人发送文件
    * `from`: **(long)** 发送方 id
    * `tos`: **(long)** 接收方 uids
    * `mtype`: **(byte)** 文件类型
    * `fileBytes`: **(byte[])** 文件内容
    * `mid`: **(long)** 消息 id, 用于过滤重复消息, 非重发时为`0`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `mid`: **(long)**
            * `payload`: **(Map(mtime:long))**
            * `exception`: **(Exception)**

* `sendGroupFile(long from, long gid, byte mtype, byte[] fileBytes, long mid, int timeout, FPCallback.ICallback callback)`: 给Group发送文件
    * `from`: **(long)** 发送方 id
    * `gid`: **(long)** Group id
    * `mtype`: **(byte)** 文件类型
    * `fileBytes`: **(byte[])** 文件内容
    * `mid`: **(long)** 消息 id, 用于过滤重复消息, 非重发时为`0`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `mid`: **(long)**
            * `payload`: **(Map(mtime:long))**
            * `exception`: **(Exception)**

* `sendRoomFile(long from, long rid, byte mtype, byte[] fileBytes, long mid, int timeout, FPCallback.ICallback callback)`: 给Room发送文件
    * `from`: **(long)** 发送方 id
    * `rid`: **(long)** Room id
    * `mtype`: **(byte)** 文件类型
    * `fileBytes`: **(byte[])** 文件内容
    * `mid`: **(long)** 消息 id, 用于过滤重复消息, 非重发时为`0`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `mid`: **(long)**
            * `payload`: **(Map(mtime:long))**
            * `exception`: **(Exception)**

* `sendRoomFile(long from, byte mtype, byte[] fileBytes, long mid, int timeout, FPCallback.ICallback callback)`: 给整个Project发送文件
    * `from`: **(long)** 发送方 id
    * `mtype`: **(byte)** 文件类型
    * `fileBytes`: **(byte[])** 文件内容
    * `mid`: **(long)** 消息 id, 用于过滤重复消息, 非重发时为`0`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `mid`: **(long)**
            * `payload`: **(Map(mtime:long))**
            * `exception`: **(Exception)**