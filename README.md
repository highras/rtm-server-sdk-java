# FPNN RTM Java SDK #

底层基于NIO实现, 支持FPNN加密.

#### 关于依赖 ####

* [json-20180130.jar](https://github.com/stleary/JSON-java)
* [msgpack-core-0.8.16.jar](https://github.com/msgpack/msgpack-java)

#### 关于线程 ####

* 一个计时器线程`ThreadPool.getInstance().startTimerThread()`, 负责超时检测/安全检查 
    * 默认实现`Executors.newScheduledThreadPool(1)`, 构造`RTMClient`时可以选择是否启用该线程
    * 如果已有计时器, `NIOCore.getInstance().checkSecond()` 周期性调用该方法，以进行超时检查（建议频率1s）

* 一个线程池, 接口`ThreadPool.IThreadPool` 
    * 默认实现`Executors.newFixedThreadPool(FPConfig.MAX_THREAD_COUNT)`
    * 如需自己管理线程，实现该接口并注册线程池`ThreadPool.getInstance().setPool(IThreadPool value)`

* 不要阻塞事件触发和回调, 否则线程池将被耗尽

#### 一个例子 ####

```java

// 创建Client
RTMClient client = new RTMClient(
    1017,
    "10d09e42-05d3-4d3c-b97a-50c8f27aa6c7",
    "highras-rtm-svrgated.ifunplus.cn",
    13315,
    true,
    20 * 1000,
    true
);

// 添加监听
client.getEvent().addListener("connect", listener);
client.getEvent().addListener("close", listener);
client.getEvent().addListener("error", listener);

// 开启连接
client.enableConnect();

// push service
client.getProcessor().getEvent().addListener(RTMConfig.SERVER_PUSH.recvPing, new FPEvent.IListener() {

    @Override
    public void fpEvent(FPEvent event) {

        System.out.println("\n[PUSH] ".concat(event.getType()).concat(":"));
        System.out.println(event.getPayload().toString());
    }
});

FPEvent.IListener listener = new FPEvent.IListener() {

    @Override
    public void fpEvent(FPEvent event) {

        switch (event.getType()) {
            case "connect":
                System.out.println("Connected!");
                // 发送消息
                client.sendMessage(778877, 778899, (byte) 8, "hello !", "", 5 * 1000, new FPCallback.ICallback() {
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
                break;
            case "close":
                System.out.println("Closed!");
                break;
            case "error":
                event.getException().printStackTrace();
                break;
        }
    }
};
```

#### 测试 ####

参考`TestMain`:

```java

// case 1
baseTest();

// case 2
// asyncStressTest();

// case 3
// singleClientConcurrentTest();
```

#### Events ####

* `event`:
    * `connect`: 连接成功 
    * `error`: 发生异常
    * `close`: 连接关闭

#### PushService ####

请参考 `RTMConfig.SERVER_PUSH` 成员

* `ping`: RTMGate主动ping
    * `data`: **(Map)**

* `pushmsg`: RTMGate主动推送P2P消息
    * `data.pid`: **(int)** 应用编号
    * `data.from`: **(long)** 发送者 id
    * `data.to`: **(long)** 接收者 id
    * `data.mtype`: **(byte)** 消息类型
    * `data.mid`: **(long)** 消息 id, 当前链接会话内唯一
    * `data.msg`: **(String)** 消息内容
    * `data.attrs`: **(String)** 发送时附加的自定义内容

* `pushmsgs`: RTMGate主动推送多个接收者P2P消息
    * `data.pid`: **(int)** 应用编号
    * `data.from`: **(long)** 发送者 id
    * `data.tos`: **(List<Long>)** 多个接收者 id
    * `data.mtype`: **(byte)** 消息类型
    * `data.mid`: **(long)** 消息 id, 当前链接会话内唯一
    * `data.msg`: **(String)** 消息内容
    * `data.attrs`: **(String)** 发送时附加的自定义内容

* `pushgroupmsg`: RTMGate主动推送Group消息
    * `data.pid`: **(int)** 应用编号
    * `data.from`: **(long)** 发送者 id
    * `data.gid`: **(long)** Group id
    * `data.mtype`: **(byte)** 消息类型
    * `data.mid`: **(long)** 消息 id, 当前链接会话内唯一
    * `data.msg`: **(String)** 消息内容
    * `data.attrs`: **(String)** 发送时附加的自定义内容

* `pushroommsg`: RTMGate主动推送Room消息
    * `data.pid`: **(int)** 应用编号
    * `data.from`: **(long)** 发送者 id
    * `data.rid`: **(long)** Room id
    * `data.mtype`: **(byte)** 消息类型
    * `data.mid`: **(long)** 消息 id, 当前链接会话内唯一
    * `data.msg`: **(String)** 消息内容
    * `data.attrs`: **(String)** 发送时附加的自定义内容

* `pushfile`: RTMGate主动推送P2P文件
    * `data.pid`: **(int)** 应用编号
    * `data.from`: **(long)** 发送者 id
    * `data.to`: **(long)** 接收者 id
    * `data.mtype`: **(byte)** 消息类型
    * `data.ftype`: **(byte)** 文件类型, 请参考 `RTMConfig.FILE_TYPE` 成员
    * `data.mid`: **(long)** 消息 id, 当前链接会话内唯一
    * `data.msg`: **(String)** 文件获取地址(url)
    * `data.attrs`: **(String)** 发送时附加的自定义内容

* `pushfiles`: RTMGate主动推送多个接收者P2P文件
    * `data.pid`: **(int)** 应用编号
    * `data.from`: **(long)** 发送者 id
    * `data.tos`: **(List<Long>)** 多个接收者 id
    * `data.mtype`: **(byte)** 消息类型
    * `data.ftype`: **(byte)** 文件类型, 请参考 `RTMConfig.FILE_TYPE` 成员
    * `data.mid`: **(long)** 消息 id, 当前链接会话内唯一
    * `data.msg`: **(String)** 消息内容
    * `data.attrs`: **(String)** 发送时附加的自定义内容

* `pushgroupfile`: RTMGate主动推送Group文件
    * `data.pid`: **(int)** 应用编号
    * `data.from`: **(long)** 发送者 id
    * `data.gid`: **(long)** Group id
    * `data.mtype`: **(byte)** 消息类型
    * `data.ftype`: **(byte)** 文件类型, 请参考 `RTMConfig.FILE_TYPE` 成员
    * `data.mid`: **(long)** 消息 id, 当前链接会话内唯一
    * `data.msg`: **(String)** 文件获取地址(url)
    * `data.attrs`: **(String)** 发送时附加的自定义内容

* `pushroomfile`: RTMGate主动推送Room文件
    * `data.pid`: **(int)** 应用编号
    * `data.from`: **(long)** 发送者 id
    * `data.rid`: **(long)** Room id
    * `data.mtype`: **(byte)** 消息类型
    * `data.ftype`: **(byte)** 文件类型, 请参考 `RTMConfig.FILE_TYPE` 成员
    * `data.mid`: **(long)** 消息 id, 当前链接会话内唯一
    * `data.msg`: **(String)** 文件获取地址(url)
    * `data.attrs`: **(String)** 发送时附加的自定义内容

* `pushevent`: RTMGate主动推送事件 
    * `data.pid`: **(int)** 应用编号
    * `data.event`: **(String)** 事件名称, 请参考 `RTMConfig.SERVER_EVENT` 成员
    * `data.uid`: **(long)** 触发者 id
    * `data.time`: **(int)** 触发时间(s)
    * `data.endpoint`: **(String)** 对应的RTMGate地址
    * `data.data`: **(String)** `预留`

#### API ####

* `constructor(int pid, String secret, String host, int port, boolean reconnect, int timeout, boolean startTimerThread)`: 构造RTMClient
    * `pid`: **(int)** 应用编号, RTM提供
    * `secret`: **(String)** 应用加密, RTM提供
    * `host`: **(String)** 地址, RTM提供
    * `port`: **(int)** 端口, RTM提供
    * `reconnect`: **(boolean)** 是否自动重连
    * `timeout`: **(int)** 超时时间(ms), 默认: `30 * 1000`
    * `startTimerThread`: **(boolean)** 是否开启计时器线程 (负责超时检测/安全检查)

* `constructor(int pid, String secret, String endpoint, boolean reconnect, int timeout, boolean startTimerThread)`: 构造RTMClient
    * `pid`: **(int)** 应用编号, RTM提供
    * `secret`: **(String)** 应用加密, RTM提供
    * `endpoint`: **(String)** 地址与端口, RTM提供
    * `reconnect`: **(boolean)** 是否自动重连
    * `timeout`: **(int)** 超时时间(ms), 默认: `30 * 1000`
    * `startTimerThread`: **(boolean)** 是否开启计时器线程 (负责超时检测/安全检查)

* `getProcessor`: **(FPProcessor)** 监听PushService的句柄

* `enableConnect()`: 开启连接(非加密模式) 

* `enableEncryptorByData(String curve, byte[] derKey, boolean streamMode, boolean reinforce)`: 开启加密连接
    * `curve`: **(String)** 加密协议
    * `derKey`: **(byte[])** 加密公钥, 秘钥文件RTM提供
    * `streamMode`: **(boolean)** 是否开启流加密, `目前Java API不支持流加密`
    * `reinforce`: **(boolean)** 是否开启加强 ` 128 : 256 `

* `enableEncryptorByFile(String curve, String derPath, boolean streamMode, boolean reinforce)`: 开启加密连接
    * `curve`: **(String)** 加密协议
    * `derPath`: **(String)** 加密公钥文件路径, 秘钥文件RTM提供
    * `streamMode`: **(boolean)** 是否开启流加密, `目前Java API不支持流加密`
    * `reinforce`: **(boolean)** 是否开启加强 ` 128 : 256 `

* `sendMessage(long from, long to, byte mtype, String msg, String attrs, int timeout, CallbackData.ICallback callback)`: 发送消息
    * `from`: **(long)** 发送方 id
    * `to`: **(long)** 接收方uid
    * `mtype`: **(byte)** 消息类型
    * `msg`: **(String)** 消息内容
    * `attrs`: **(String)** 消息附加信息, 没有可传`""`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(CallbackData.ICallback)** 回调方法
        * `exception`: **(Exception)** 
        * `payload`: **(Map)** 

* `sendMessages(long from, List<Long> tos, byte mtype, String msg, String attrs, int timeout, CallbackData.ICallback callback)`: 发送多人消息
    * `from`: **(long)** 发送方 id
    * `tos`: **(List<Long>)** 接收方uids
    * `mtype`: **(byte)** 消息类型
    * `msg`: **(String)** 消息内容
    * `attrs`: **(String)** 消息附加信息, 没有可传`""`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(CallbackData.ICallback)** 回调方法
        * `exception`: **(Exception)** 
        * `payload`: **(Map)** 

* `sendGroupMessage(long from, long gid, byte mtype, String msg, String attrs, int timeout, CallbackData.ICallback callback)`: 发送group消息
    * `from`: **(long)** 发送方 id
    * `gid`: **(long)** group id
    * `mtype`: **(byte)** 消息类型
    * `msg`: **(String)** 消息内容
    * `attrs`: **(String)** 消息附加信息, 可传`""`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(CallbackData.ICallback)** 回调方法
        * `exception`: **(Exception)** 
        * `payload`: **(Map)** 

* `sendRoomMessage(long from, long rid, byte mtype, String msg, String attrs, int timeout, CallbackData.ICallback callback)`: 发送room消息
    * `from`: **(long)** 发送方 id
    * `rid`: **(long)** room id
    * `mtype`: **(byte)** 消息类型
    * `msg`: **(String)** 消息内容
    * `attrs`: **(String)** 消息附加信息, 可传`""`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(CallbackData.ICallback)** 回调方法
        * `exception`: **(Exception)** 
        * `payload`: **(Map)** 

* `broadcastMessage(long from, byte mtype, String msg, String attrs, int timeout, CallbackData.ICallback callback)`: 广播消息(andmin id)
    * `from`: **(long)** admin id
    * `mtype`: **(byte)** 消息类型
    * `msg`: **(String)** 消息内容
    * `attrs`: **(String)** 消息附加信息, 可传`""`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(CallbackData.ICallback)** 回调方法
        * `exception`: **(Exception)** 
        * `payload`: **(Map)** 

* `addFriends(long uid, List<Long> friends, int timeout, CallbackData.ICallback callback)`: 添加好友
    * `uid`: **(long)** 用户 id
    * `friends`: **(List<Long>)** 多个好友 id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(CallbackData.ICallback)** 回调方法
        * `exception`: **(Exception)** 
        * `payload`: **(Map)** 

* `deleteFriends(long uid, List<Long> friends, int timeout, CallbackData.ICallback callback)`: 删除好友
    * `uid`: **(long)** 用户 id
    * `friends`: **(List<Long>)** 多个好友 id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(CallbackData.ICallback)** 回调方法
        * `exception`: **(Exception)** 
        * `payload`: **(Map)** 

* `getFriends(long uid, int timeout, CallbackData.ICallback callback)`: 获取好友
    * `uid`: **(long)** 用户 id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(CallbackData.ICallback)** 回调方法
        * `exception`: **(Exception)** 
        * `payload`: **(List<Long>)** 

* `isFriend(long uid, long fuid, int timeout, CallbackData.ICallback callback)`: 是否好友
    * `uid`: **(long)** 用户 id
    * `fuid`: **(long)** 好友 id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(CallbackData.ICallback)** 回调方法
        * `exception`: **(Exception)** 
        * `payload`: **(boolean)** 

* `isFriends(long uid, List<Long> fuids, int timeout, CallbackData.ICallback callback)`: 是否好友
    * `uid`: **(long)** 用户 id
    * `fuids`: **(List<Long>)** 多个好友 id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(CallbackData.ICallback)** 回调方法
        * `exception`: **(Exception)** 
        * `payload`: **(List<Long>)** 

* `addGroupMembers(long gid, List<Long> uids, int timeout, CallbackData.ICallback callback)`: 添加group成员
    * `gid`: **(long)** group id
    * `uids`: **(List<Long>)** 多个用户 id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(CallbackData.ICallback)** 回调方法
        * `exception`: **(Exception)** 
        * `payload`: **(Map)** 

* `deleteGroupMembers(long gid, List<Long> uids, int timeout, CallbackData.ICallback callback)`:  删除group成员
    * `gid`: **(long)** group id
    * `uids`: **(List<Long>)** 多个用户 id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(CallbackData.ICallback)** 回调方法
        * `exception`: **(Exception)** 
        * `payload`: **(Map)** 

* `deleteGroup(long gid, int timeout, CallbackData.ICallback callback)`: 删除group
    * `gid`: **(long)** group id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(CallbackData.ICallback)** 回调方法
        * `exception`: **(Exception)** 
        * `payload`: **(Map)** 

* `getGroupMembers(long gid, int timeout, CallbackData.ICallback callback)`: 获取group成员
    * `gid`: **(long)** group id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(CallbackData.ICallback)** 回调方法
        * `exception`: **(Exception)** 
        * `payload`: **(List<Long>)** 

* `isGroupMember(long gid, long uid, int timeout, CallbackData.ICallback callback)`: 是否group成员
    * `gid`: **(long)** group id
    * `uid`: **(long)** 用户 id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(CallbackData.ICallback)** 回调方法
        * `exception`: **(Exception)** 
        * `payload`: **(boolean)** 

* `getUserGroups(long uid, int timeout, CallbackData.ICallback callback)`: 获取用户的group
    * `uid`: **(long)** 用户 id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(CallbackData.ICallback)** 回调方法
        * `exception`: **(Exception)** 
        * `payload`: **(List<Long>)** 

* `getToken(long uid, int timeout, CallbackData.ICallback callback)`: 获取token
    * `uid`: **(long)** 用户 id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(CallbackData.ICallback)** 回调方法
        * `exception`: **(Exception)** 
        * `payload`: **(String)** 

* `getOnlineUsers(List<Long> uids, int timeout, CallbackData.ICallback callback)`: 获取在线用户
    * `uids`: **(List<Long>)** 多个用户 id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(CallbackData.ICallback)** 回调方法
        * `exception`: **(Exception)** 
        * `payload`: **(List<Long>)** 

* `addGroupBan(long gid, long uid, int btime, int timeout, CallbackData.ICallback callback)`: 阻止用户消息(group)
    * `gid`: **(long)** group id
    * `uid`: **(long)** 用户 id
    * `btime`: **(int)** 阻止时间(s)
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(CallbackData.ICallback)** 回调方法
        * `exception`: **(Exception)** 
        * `payload`: **(Map)** 

* `removeGroupBan(long gid, long uid, int timeout, CallbackData.ICallback callback)`: 取消阻止(group)
    * `gid`: **(long)** group id
    * `uid`: **(long)** 用户 id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(CallbackData.ICallback)** 回调方法
        * `exception`: **(Exception)** 
        * `payload`: **(Map)** 

* `addRoomBan(long rid, long uid, int btime, int timeout, CallbackData.ICallback callback)`: 阻止用户消息(room)
    * `rid`: **(long)** room id
    * `uid`: **(long)** 用户 id
    * `btime`: **(int)** 阻止时间(s)
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(CallbackData.ICallback)** 回调方法
        * `exception`: **(Exception)** 
        * `payload`: **(Map)** 

* `removeRoomBan(long rid, long uid, int timeout, CallbackData.ICallback callback)`: 取消阻止(room)
    * `rid`: **(long)** room id
    * `uid`: **(long)** 用户 id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(CallbackData.ICallback)** 回调方法
        * `exception`: **(Exception)** 
        * `payload`: **(Map)** 

* `addProjectBlack(long uid, int btime, int timeout, CallbackData.ICallback callback)`: 阻止用户消息(project)
    * `uid`: **(long)** 用户 id
    * `btime`: **(int)** 阻止时间(s)
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(CallbackData.ICallback)** 回调方法
        * `exception`: **(Exception)** 
        * `payload`: **(Map)** 

* `removeProjectBlack(int uid, int timeout, CallbackData.ICallback callback)`: 取消阻止(project)
    * `uid`: **(long)** 用户 id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(CallbackData.ICallback)** 回调方法
        * `exception`: **(Exception)** 
        * `payload`: **(Map)** 

* `isBanOfGroup(long gid, long uid, int timeout, CallbackData.ICallback callback)`: 检查阻止(group)
    * `gid`: **(long)** group id
    * `uid`: **(long)** 用户 id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(CallbackData.ICallback)** 回调方法
        * `exception`: **(Exception)** 
        * `payload`: **(boolean)** 

* `isBanOfRoom(long rid, long uid, int timeout, CallbackData.ICallback callback)`: 检查阻止(room)
    * `rid`: **(long)** room id
    * `uid`: **(long)** 用户 id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(CallbackData.ICallback)** 回调方法
        * `exception`: **(Exception)** 
        * `payload`: **(boolean)** 

* `isProjectBlack(long uid, int timeout, CallbackData.ICallback callback)`: 检查阻止(project)
    * `uid`: **(long)** 用户 id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(CallbackData.ICallback)** 回调方法
        * `exception`: **(Exception)** 
        * `payload`: **(boolean)** 

* `setGeo(long uid, double lat, double lng, int timeout, CallbackData.ICallback callback)`: 设置位置
    * `uid`: **(long)** 用户 id
    * `lat`: **(double)** 纬度
    * `lng`: **(double)** 经度
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(CallbackData.ICallback)** 回调方法
        * `exception`: **(Exception)** 
        * `payload`: **(Map)** 

* `getGeo(long uid, int timeout, CallbackData.ICallback callback)`: 获取位置
    * `uid`: **(long)** 用户 id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(CallbackData.ICallback)** 回调方法
        * `exception`: **(Exception)** 
        * `payload`: **(Map)** 

* `getGeos(List<Long> uids, int timeout, CallbackData.ICallback callback)`: 获取位置
    * `uids`: **(List<Long>)** 多个用户 id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(CallbackData.ICallback)** 回调方法
        * `exception`: **(Exception)** 
        * `payload`: **(List<ArrayList>)** 

* `sendFile(long from, long to, byte mtype, String filePath, int timeout, CallbackData.ICallback callback)`: 发送文件
    * `from`: **(long)** 发送方 id
    * `to`: **(long)** 接收方uid
    * `mtype`: **(byte)** 消息类型
    * `filePath`: **(String)** 文件路径 
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(CallbackData.ICallback)** 回调方法
        * `exception`: **(Exception)** 
        * `payload`: **(Map)** 
        
* `sendFile(long from, long to, byte mtype, byte[] fileBytes, int timeout, CallbackData.ICallback callback)`: 发送文件
    * `from`: **(long)** 发送方 id
    * `to`: **(long)** 接收方uid
    * `mtype`: **(byte)** 消息类型
    * `fileBytes`: **(byte[])** 文件内容
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(CallbackData.ICallback)** 回调方法
        * `exception`: **(Exception)** 
        * `payload`: **(Map)** 

* `addEvtListener(List<Long> gids, List<Long> rids, boolean p2p, List<String> events, int timeout, CallbackData.ICallback callback)`: 添加 `事件` / `消息` 监听
    * `gids`: **(List<Long>)** 多个Group id
    * `rids`: **(List<Long>)** 多个Room id
    * `p2p`: **(boolean)** P2P消息
    * `events`: **(List<String>)** 多个事件名称, 请参考 `RTMConfig.SERVER_EVENT` 成员
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(CallbackData.ICallback)** 回调方法
        * `exception`: **(Exception)** 
        * `payload`: **(Map)** 

* `removeEvtListener(List<Long> gids, List<Long> rids, boolean p2p, List<String> events, int timeout, CallbackData.ICallback callback)`: 删除 `事件` / `消息` 监听
    * `gids`: **(List<Long>)** 多个Group id
    * `rids`: **(List<Long>)** 多个Room id
    * `p2p`: **(boolean)** P2P消息
    * `events`: **(List<String>)** 多个事件名称, 请参考 `RTMConfig.SERVER_EVENT` 成员
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(CallbackData.ICallback)** 回调方法
        * `exception`: **(Exception)** 
        * `payload`: **(Map)** 

* `setEvtListener(boolean all, int timeout, CallbackData.ICallback callback)`: 更新 `事件` / `消息` 监听
    * `all`: **(boolean)** `true`: 监听所有 `事件` / `消息`, `false`: 取消所有 `事件` / `消息` 监听
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(CallbackData.ICallback)** 回调方法
        * `exception`: **(Exception)** 
        * `payload`: **(Map)** 
        
* `setEvtListener(List<Long> gids, List<Long> rids, boolean p2p, List<String> events, int timeout, CallbackData.ICallback callback)`: 更新 `事件` / `消息` 监听
    * `gids`: **(List<Long>)** 多个Group id
    * `rids`: **(List<Long>)** 多个Room id
    * `p2p`: **(boolean)** P2P消息, `true`: 监听, `false`: 取消监听
    * `events`: **(List<String>)** 多个事件名称, 请参考 `RTMConfig.SERVER_EVENT` 成员
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(CallbackData.ICallback)** 回调方法
        * `exception`: **(Exception)** 
        * `payload`: **(Map)** 

* `addDevice(long uid, String apptype, String devicetoken, int timeout, CallbackData.ICallback callback)`: 添加设备, 应用信息
    * `uid`: **(long])** 用户 id
    * `apptype`: **(String)** 应用信息
    * `devicetoken`: **(String)** 设备 token
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(CallbackData.ICallback)** 回调方法
        * `exception`: **(Exception)** 
        * `payload`: **(Map)** 
        
* `removeDevice(long uid, String devicetoken, int timeout, CallbackData.ICallback callback)`: 删除设备, 应用信息
    * `uid`: **(long)** 用户 id
    * `devicetoken`: **(String)** 设备 token
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(CallbackData.ICallback)** 回调方法
        * `exception`: **(Exception)** 
        * `payload`: **(Map)** 
