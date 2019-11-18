# fpnn rtm sdk java #

#### API ####

* `RTMClient::RTMRegistration::register()`: 注册服务

> constructor

* `constructor(int pid, String secret, String endpoint, boolean reconnect, int timeout, boolean debug)`: 构造RTMClient
    * `pid`: **(int)** 应用编号, RTM提供
    * `secret`: **(String)** 应用加密, RTM提供
    * `endpoint`: **(String)** 地址与端口, RTM提供
    * `reconnect`: **(boolean)** 是否自动重连
    * `timeout`: **(int)** 超时时间(ms), 默认: `30 * 1000`
    * `debug`: **(boolean)** 是否开启调试日志
    
> action

* `getProcessor`: **(RTMProcessor)** 监听PushService的句柄

* `getEvent`: **(FPEvent)** 监听事件的句柄

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
    * `ce`: **(String)** 踢掉`ce`对应链接, 为`null`则踢掉该用户, 多点登录情况
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
            
* `removeToken(long uid, int timeout, FPCallback.ICallback callback)`: 删除一个用户的token
    * `uid`: **(long)** 用户 id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(Map)**
            * `exception`: **(Exception)**
 
> message action

* `sendMessage(long from, long to, byte mtype, String msg, String attrs, long mid, int timeout, FPCallback.ICallback callback)`: 发送业务消息
    * `from`: **(long)** 发送方 id
    * `to`: **(long)** 接收方uid
    * `mtype`: **(byte)** 业务消息类型（请使用51-127，禁止使用50及以下的值）
    * `msg`: **(String)** 业务消息内容
    * `attrs`: **(String)** 业务消息附加信息, 没有可传`""`
    * `mid`: **(long)** 业务消息 id, 用于过滤重复业务消息, 非重发时为`0`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `mid`: **(long)**
            * `payload`: **(Map(mtime:long))**
            * `exception`: **(Exception)**

* `sendMessages(long from, List<Long> tos, byte mtype, String msg, String attrs, long mid, int timeout, FPCallback.ICallback callback)`: 发送多人业务消息
    * `from`: **(long)** 发送方 id
    * `tos`: **(List(Long))** 接收方uids
    * `mtype`: **(byte)** 业务消息类型（请使用51-127，禁止使用50及以下的值）
    * `msg`: **(String)** 业务消息内容
    * `attrs`: **(String)** 业务消息附加信息, 没有可传`""`
    * `mid`: **(long)** 业务消息 id, 用于过滤重复业务消息, 非重发时为`0`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `mid`: **(long)**
            * `payload`: **(Map(mtime:long))**
            * `exception`: **(Exception)**

* `sendGroupMessage(long from, long gid, byte mtype, String msg, String attrs, long mid, int timeout, FPCallback.ICallback callback)`: 发送group业务消息
    * `from`: **(long)** 发送方 id
    * `gid`: **(long)** group id
    * `mtype`: **(byte)** 业务消息类型（请使用51-127，禁止使用50及以下的值）
    * `msg`: **(String)** 业务消息内容
    * `attrs`: **(String)** 业务消息附加信息, 可传`""`
    * `mid`: **(long)** 业务消息 id, 用于过滤重复业务消息, 非重发时为`0`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `mid`: **(long)**
            * `payload`: **(Map(mtime:long))**
            * `exception`: **(Exception)**

* `sendRoomMessage(long from, long rid, byte mtype, String msg, String attrs, long mid, int timeout, FPCallback.ICallback callback)`: 发送room业务消息
    * `from`: **(long)** 发送方 id
    * `rid`: **(long)** room id
    * `mtype`: **(byte)** 业务消息类型（请使用51-127，禁止使用50及以下的值）
    * `msg`: **(String)** 业务消息内容
    * `attrs`: **(String)** 业务消息附加信息, 可传`""`
    * `mid`: **(long)** 业务消息 id, 用于过滤重复业务消息, 非重发时为`0`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `mid`: **(long)**
            * `payload`: **(Map(mtime:long))**
            * `exception`: **(Exception)**

* `broadcastMessage(long from, byte mtype, String msg, String attrs, long mid, int timeout, FPCallback.ICallback callback)`: 广播业务消息(andmin id)
    * `from`: **(long)** admin id
    * `mtype`: **(byte)** 业务消息类型（请使用51-127，禁止使用50及以下的值）
    * `msg`: **(String)** 业务消息内容
    * `attrs`: **(String)** 业务消息附加信息, 可传`""`
    * `mid`: **(long)** 业务消息 id, 用于过滤重复业务消息, 非重发时为`0`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `mid`: **(long)**
            * `payload`: **(Map(mtime:long))**
            * `exception`: **(Exception)**

* `getGroupMessage(long gid, boolean desc, int num, long begin, long end, long lastid, List<Byte> mtypes, int timeout, FPCallback.ICallback callback)`: 获取Group历史业务消息
    * `gid`: **(long)** Group id
    * `desc`: **(boolean)** `true`: 则从`end`的时间戳开始倒序翻页, `false`: 则从`begin`的时间戳顺序翻页
    * `num`: **(int)** 获取数量, **一次最多获取20条, 建议10条**
    * `begin`: **(long)** 开始时间戳, 毫秒, 默认`0`, 条件：`>=`
    * `end`: **(long)** 结束时间戳, 毫秒, 默认`0`, 条件：`<=`
    * `lastid`: **(long)** 最后一条业务消息的id, 第一次默认传`0`, 条件：`> or <`
    * `mtypes`: **(List(Byte))** 获取历史业务消息的类型集合
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `exception`: **(Exception)**
            * `payload`: **(Map(num:int,lastid:long,begin:long,end:long,msgs:List(GroupMsg)))**
                * `GroupMsg.id` **(long)**
                * `GroupMsg.from` **(long)**
                * `GroupMsg.mtype` **(byte)**
                * `GroupMsg.mid` **(long)**
                * `GroupMsg.msg` **(String)**
                * `GroupMsg.attrs` **(String)**
                * `GroupMsg.mtime` **(long)**

* `getRoomMessage(long rid, boolean desc, int num, long begin, long end, long lastid, List<Byte> mtypes, int timeout, FPCallback.ICallback callback)`: 获取Room历史业务消息
    * `rid`: **(long)** Room id
    * `desc`: **(boolean)** `true`: 则从`end`的时间戳开始倒序翻页, `false`: 则从`begin`的时间戳顺序翻页
    * `num`: **(int)** 获取数量, **一次最多获取20条, 建议10条**
    * `begin`: **(long)** 开始时间戳, 毫秒, 默认`0`, 条件：`>=`
    * `end`: **(long)** 结束时间戳, 毫秒, 默认`0`, 条件：`<=`
    * `lastid`: **(long)** 最后一条业务消息的id, 第一次默认传`0`, 条件：`> or <`
    * `mtypes`: **(List(Byte))** 获取历史业务消息的类型集合
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `exception`: **(Exception)**
            * `payload`: **(Map(num:int,lastid:long,begin:long,end:long,msgs:List(RoomMsg)))**
                * `RoomMsg.id` **(long)**
                * `RoomMsg.from` **(long)**
                * `RoomMsg.mtype` **(byte)**
                * `RoomMsg.mid` **(long)**
                * `RoomMsg.msg` **(String)**
                * `RoomMsg.attrs` **(String)**
                * `RoomMsg.mtime` **(long)**

* `getBroadcastMessage(boolean desc, int num, long begin, long end, long lastid, List<Byte> mtypes, int timeout, FPCallback.ICallback callback)`: 获取广播历史业务消息
    * `desc`: **(boolean)** `true`: 则从`end`的时间戳开始倒序翻页, `false`: 则从`begin`的时间戳顺序翻页
    * `num`: **(int)** 获取数量, **一次最多获取20条, 建议10条**
    * `begin`: **(long)** 开始时间戳, 毫秒, 默认`0`, 条件：`>=`
    * `end`: **(long)** 结束时间戳, 毫秒, 默认`0`, 条件：`<=`
    * `lastid`: **(long)** 最后一条业务消息的id, 第一次默认传`0`, 条件：`> or <`
    * `mtypes`: **(List(Byte))** 获取历史业务消息的类型集合
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `exception`: **(Exception)**
            * `payload`: **(Map(num:int,lastid:long,begin:long,end:long,msgs:List(BroadcastMsg)))**
                * `BroadcastMsg.id` **(long)**
                * `BroadcastMsg.from` **(long)**
                * `BroadcastMsg.mtype` **(byte)**
                * `BroadcastMsg.mid` **(long)**
                * `BroadcastMsg.msg` **(String)**
                * `BroadcastMsg.attrs` **(String)**
                * `BroadcastMsg.mtime` **(long)**

* `getP2PMessage(long uid, long ouid, boolean desc, int num, long begin, long end, long lastid, List<Byte> mtypes, int timeout, FPCallback.ICallback callback)`: 获取P2P历史业务消息
    * `uid`: **(long)** 获取和两个用户之间的历史业务消息
    * `ouid`: **(long)** 获取和两个用户之间的历史业务消息
    * `desc`: **(boolean)** `true`: 则从`end`的时间戳开始倒序翻页, `false`: 则从`begin`的时间戳顺序翻页
    * `num`: **(int)** 获取数量, **一次最多获取20条, 建议10条**
    * `begin`: **(long)** 开始时间戳, 毫秒, 默认`0`, 条件：`>=`
    * `end`: **(long)** 结束时间戳, 毫秒, 默认`0`, 条件：`<=`
    * `lastid`: **(long)** 最后一条业务消息的id, 第一次默认传`0`, 条件：`> or <`
    * `mtypes`: **(List(Byte))** 获取历史业务消息的类型集合
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `exception`: **(Exception)**
            * `payload`: **(Map(num:int,lastid:long,begin:long,end:long,msgs:List(P2PMsg)))**
                * `P2PMsg.id` **(long)**
                * `P2PMsg.direction` **(byte)**
                * `P2PMsg.mtype` **(byte)**
                * `P2PMsg.mid` **(long)**
                * `P2PMsg.msg` **(String)**
                * `P2PMsg.attrs` **(String)**
                * `P2PMsg.mtime` **(long)**

* `deleteMessage(long mid, long from, long to, int timeout, FPCallback.ICallback callback)`: 删除P2P业务消息
    * `mid`: **(long)** 业务消息 id
    * `from`: **(long)** 发送方 id
    * `to`: **(long)** 业务消息接收方User id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(Map)**
            * `exception`: **(Exception)**
            
* `deleteGroupMessage(long mid, long from, long gid, int timeout, FPCallback.ICallback callback)`: 删除Gourp业务消息
    * `mid`: **(long)** 业务消息 id
    * `from`: **(long)** 发送方 id
    * `gid`: **(long)** 业务消息接收方Group id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(Map)**
            * `exception`: **(Exception)**
            
* `deleteRoomMessage(long mid, long from, long rid, int timeout, FPCallback.ICallback callback)`: 删除Room业务消息
    * `mid`: **(long)** 业务消息 id
    * `from`: **(long)** 发送方 id
    * `rid`: **(long)** 业务消息接收方Room id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(Map)**
            * `exception`: **(Exception)**
            
* `deleteBroadcastMessage(long mid, long from, int timeout, FPCallback.ICallback callback)`: 删除广播业务消息
    * `mid`: **(long)** 业务消息 id
    * `from`: **(long)** admin id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(Map)**
            * `exception`: **(Exception)**

> chat action

* `sendChat(long from, long to, String msg, String attrs, long mid, int timeout, FPCallback.ICallback callback)`: 发送聊天消息, 消息类型`RTMConfig.CHAT_TYPE.text`
    * `from`: **(long)** 发送方 id
    * `to`: **(long)** 接收方uid
    * `msg`: **(String)** 聊天消息内容，附加修饰信息不要放这里，方便后继的操作，比如翻译，敏感词过滤等等
    * `attrs`: **(String)** 聊天消息附加信息, 没有可传`""`
    * `mid`: **(long)** 聊天消息 id, 用于过滤重复聊天消息, 非重发时为`0`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `mid`: **(long)**
            * `payload`: **(Map(mtime:long))**
            * `exception`: **(Exception)**
            
* `sendAudio(long from, long to, byte[] audio, String attrs, long mid, int timeout, FPCallback.ICallback callback)`: 发送聊天语音, 消息类型`RTMConfig.CHAT_TYPE.audio`
    * `from`: **(long)** 发送方 id
    * `to`: **(long)** 接收方uid
    * `audio`: **(byte[])** 语音数据
    * `attrs`: **(String)** 附加信息, `Json`字符串, 至少带两个参数(`lang`: 语言类型, `duration`: 语音长度 ms)
    * `mid`: **(long)** 语音消息 id, 用于过滤重复聊天语音, 非重发时为`0`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `mid`: **(long)**
            * `payload`: **(Map(mtime:long))**
            * `exception`: **(Exception)**
            
* `sendCmd(long from, long to, String msg, String attrs, long mid, int timeout, FPCallback.ICallback callback)`: 发送聊天命令, 消息类型`RTMConfig.CHAT_TYPE.cmd`
    * `from`: **(long)** 发送方 id
    * `to`: **(long)** 接收方uid
    * `msg`: **(String)** 聊天命令
    * `attrs`: **(String)** 命令附加信息, 没有可传`""`
    * `mid`: **(long)** 命令消息 id, 用于过滤重复聊天消息, 非重发时为`0`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `mid`: **(long)**
            * `payload`: **(Map(mtime:long))**
            * `exception`: **(Exception)**

* `sendChats(long from, List<Long> tos, String msg, String attrs, long mid, int timeout, FPCallback.ICallback callback)`: 发送多人聊天消息, 消息类型`RTMConfig.CHAT_TYPE.text`
    * `from`: **(long)** 发送方 id
    * `tos`: **(List(Long))** 接收方uids
    * `msg`: **(String)** 聊天消息内容，附加修饰信息不要放这里，方便后继的操作，比如翻译，敏感词过滤等等
    * `attrs`: **(String)** 聊天消息附加信息, 没有可传`""`
    * `mid`: **(long)** 聊天消息 id, 用于过滤重复聊天消息, 非重发时为`0`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `mid`: **(long)**
            * `payload`: **(Map(mtime:long))**
            * `exception`: **(Exception)**
            
* `sendAudios(long from, List<Long> tos, byte[] audio, String attrs, long mid, int timeout, FPCallback.ICallback callback)`: 发送多人聊天语音, 消息类型`RTMConfig.CHAT_TYPE.audio`
    * `from`: **(long)** 发送方 id
    * `tos`: **(List(Long))** 接收方uids
    * `audio`: **(byte[])** 语音数据
    * `attrs`: **(String)** 附加信息, `Json`字符串, 至少带两个参数(`lang`: 语言类型, `duration`: 语音长度 ms)
    * `mid`: **(long)** 语音消息 id, 用于过滤重复聊天语音, 非重发时为`0`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `mid`: **(long)**
            * `payload`: **(Map(mtime:long))**
            * `exception`: **(Exception)**
            
* `sendCmds(long from, List<Long> tos, String msg, String attrs, long mid, int timeout, FPCallback.ICallback callback)`: 发送多人聊天命令, 消息类型`RTMConfig.CHAT_TYPE.cmd`
    * `from`: **(long)** 发送方 id
    * `tos`: **(List(Long))** 接收方uids
    * `msg`: **(String)** 聊天命令
    * `attrs`: **(String)** 命令附加信息, 没有可传`""`
    * `mid`: **(long)** 命令消息 id, 用于过滤重复聊天消息, 非重发时为`0`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `mid`: **(long)**
            * `payload`: **(Map(mtime:long))**
            * `exception`: **(Exception)**

* `sendGroupChat(long from, long gid, String msg, String attrs, long mid, int timeout, FPCallback.ICallback callback)`: 发送group聊天消息, 消息类型`RTMConfig.CHAT_TYPE.text`
    * `from`: **(long)** 发送方 id
    * `gid`: **(long)** group id
    * `msg`: **(String)** 聊天消息内容，附加修饰信息不要放这里，方便后继的操作，比如翻译，敏感词过滤等等
    * `attrs`: **(String)** 聊天消息附加信息, 可传`""`
    * `mid`: **(long)** 聊天消息 id, 用于过滤重复聊天消息, 非重发时为`0`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `mid`: **(long)**
            * `payload`: **(Map(mtime:long))**
            * `exception`: **(Exception)**
            
* `sendGroupAudio(long from, long gid, byte[] audio, String attrs, long mid, int timeout, FPCallback.ICallback callback)`: 发送group聊天语音, 消息类型`RTMConfig.CHAT_TYPE.audio`
    * `from`: **(long)** 发送方 id
    * `gid`: **(long)** group id
    * `audio`: **(byte[])** 语音数据
    * `attrs`: **(String)** 附加信息, `Json`字符串, 至少带两个参数(`lang`: 语言类型, `duration`: 语音长度 ms)
    * `mid`: **(long)** 语音消息 id, 用于过滤重复聊天语音, 非重发时为`0`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `mid`: **(long)**
            * `payload`: **(Map(mtime:long))**
            * `exception`: **(Exception)**
            
* `sendGroupCmd(long from, long gid, String msg, String attrs, long mid, int timeout, FPCallback.ICallback callback)`: 发送group聊天命令, 消息类型`RTMConfig.CHAT_TYPE.cmd`
    * `from`: **(long)** 发送方 id
    * `gid`: **(long)** group id
    * `msg`: **(String)** 聊天命令
    * `attrs`: **(String)** 命令附加信息, 没有可传`""`
    * `mid`: **(long)** 命令消息 id, 用于过滤重复聊天消息, 非重发时为`0`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `mid`: **(long)**
            * `payload`: **(Map(mtime:long))**
            * `exception`: **(Exception)**

* `sendRoomChat(long from, long rid, String msg, String attrs, long mid, int timeout, FPCallback.ICallback callback)`: 发送room聊天消息, 消息类型`RTMConfig.CHAT_TYPE.text`
    * `from`: **(long)** 发送方 id
    * `rid`: **(long)** room id
    * `msg`: **(String)** 聊天消息内容，附加修饰信息不要放这里，方便后继的操作，比如翻译，敏感词过滤等等
    * `attrs`: **(String)** 聊天消息附加信息, 可传`""`
    * `mid`: **(long)** 聊天消息 id, 用于过滤重复聊天消息, 非重发时为`0`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `mid`: **(long)**
            * `payload`: **(Map(mtime:long))**
            * `exception`: **(Exception)**
            
* `sendRoomAudio(long from, long rid, byte[] audio, String attrs, long mid, int timeout, FPCallback.ICallback callback)`: 发送room聊天语音, 消息类型`RTMConfig.CHAT_TYPE.audio`
    * `from`: **(long)** 发送方 id
    * `rid`: **(long)** room id
    * `audio`: **(byte[])** 语音数据
    * `attrs`: **(String)** 附加信息, `Json`字符串, 至少带两个参数(`lang`: 语言类型, `duration`: 语音长度 ms)
    * `mid`: **(long)** 语音消息 id, 用于过滤重复聊天语音, 非重发时为`0`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `mid`: **(long)**
            * `payload`: **(Map(mtime:long))**
            * `exception`: **(Exception)**
            
* `sendRoomCmd(long from, long rid, String msg, String attrs, long mid, int timeout, FPCallback.ICallback callback)`: 发送room聊天命令, 消息类型`RTMConfig.CHAT_TYPE.cmd`
    * `from`: **(long)** 发送方 id
    * `rid`: **(long)** room id
    * `msg`: **(String)** 聊天命令
    * `attrs`: **(String)** 命令附加信息, 没有可传`""`
    * `mid`: **(long)** 命令消息 id, 用于过滤重复聊天消息, 非重发时为`0`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `mid`: **(long)**
            * `payload`: **(Map(mtime:long))**
            * `exception`: **(Exception)**

* `broadcastChat(long from, String msg, String attrs, long mid, int timeout, FPCallback.ICallback callback)`: 广播聊天消息(andmin id), 消息类型`RTMConfig.CHAT_TYPE.text`
    * `from`: **(long)** admin id
    * `msg`: **(String)** 聊天消息内容，附加修饰信息不要放这里，方便后继的操作，比如翻译，敏感词过滤等等
    * `attrs`: **(String)** 聊天消息附加信息, 可传`""`
    * `mid`: **(long)** 聊天消息 id, 用于过滤重复聊天消息, 非重发时为`0`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `mid`: **(long)**
            * `payload`: **(Map(mtime:long))**
            * `exception`: **(Exception)**
            
* `broadcastAudio(long from, byte[] audio, String attrs, long mid, int timeout, FPCallback.ICallback callback)`: 广播聊天语音(andmin id), 消息类型`RTMConfig.CHAT_TYPE.audio`
    * `from`: **(long)** admin id
    * `audio`: **(byte[])** 语音数据
    * `attrs`: **(String)** 附加信息, `Json`字符串, 至少带两个参数(`lang`: 语言类型, `duration`: 语音长度 ms)
    * `mid`: **(long)** 语音消息 id, 用于过滤重复聊天语音, 非重发时为`0`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `mid`: **(long)**
            * `payload`: **(Map(mtime:long))**
            * `exception`: **(Exception)**
            
* `broadcastCmd(long from, String msg, String attrs, long mid, int timeout, FPCallback.ICallback callback)`: 广播聊天命令(andmin id), 消息类型`RTMConfig.CHAT_TYPE.cmd`
    * `from`: **(long)** admin id
    * `msg`: **(String)** 聊天命令
    * `attrs`: **(String)** 命令附加信息, 没有可传`""`
    * `mid`: **(long)** 命令消息 id, 用于过滤重复聊天消息, 非重发时为`0`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `mid`: **(long)**
            * `payload`: **(Map(mtime:long))**
            * `exception`: **(Exception)**

* `getGroupChat(long gid, boolean desc, int num, long begin, long end, long lastid, int timeout, FPCallback.ICallback callback)`: 获取Group历史聊天消息, `mtypes=Arrays.asList((byte)30)`
    * `gid`: **(long)** Group id
    * `desc`: **(boolean)** `true`: 则从`end`的时间戳开始倒序翻页, `false`: 则从`begin`的时间戳顺序翻页
    * `num`: **(int)** 获取数量, **一次最多获取20条, 建议10条**
    * `begin`: **(long)** 开始时间戳, 毫秒, 默认`0`, 条件：`>=`
    * `end`: **(long)** 结束时间戳, 毫秒, 默认`0`, 条件：`<=`
    * `lastid`: **(long)** 最后一条聊天消息的id, 第一次默认传`0`, 条件：`> or <`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `exception`: **(Exception)**
            * `payload`: **(Map(num:int,lastid:long,begin:long,end:long,msgs:List(GroupMsg)))**
                * `GroupMsg.id` **(long)**
                * `GroupMsg.from` **(long)**
                * `GroupMsg.mid` **(long)**
                * `GroupMsg.msg` **(String)**
                * `GroupMsg.attrs` **(String)**
                * `GroupMsg.mtime` **(long)**

* `getRoomChat(long rid, boolean desc, int num, long begin, long end, long lastid, int timeout, FPCallback.ICallback callback)`: 获取Room历史聊天消息, `mtypes=Arrays.asList((byte)30)`
    * `rid`: **(long)** Room id
    * `desc`: **(boolean)** `true`: 则从`end`的时间戳开始倒序翻页, `false`: 则从`begin`的时间戳顺序翻页
    * `num`: **(int)** 获取数量, **一次最多获取20条, 建议10条**
    * `begin`: **(long)** 开始时间戳, 毫秒, 默认`0`, 条件：`>=`
    * `end`: **(long)** 结束时间戳, 毫秒, 默认`0`, 条件：`<=`
    * `lastid`: **(long)** 最后一条聊天消息的id, 第一次默认传`0`, 条件：`> or <`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `exception`: **(Exception)**
            * `payload`: **(Map(num:int,lastid:long,begin:long,end:long,msgs:List(RoomMsg)))**
                * `RoomMsg.id` **(long)**
                * `RoomMsg.from` **(long)**
                * `RoomMsg.mid` **(long)**
                * `RoomMsg.msg` **(String)**
                * `RoomMsg.attrs` **(String)**
                * `RoomMsg.mtime` **(long)**

* `getBroadcastChat(boolean desc, int num, long begin, long end, long lastid, int timeout, FPCallback.ICallback callback)`: 获取广播历史聊天消息, `mtypes=Arrays.asList((byte)30)`
    * `desc`: **(boolean)** `true`: 则从`end`的时间戳开始倒序翻页, `false`: 则从`begin`的时间戳顺序翻页
    * `num`: **(int)** 获取数量, **一次最多获取20条, 建议10条**
    * `begin`: **(long)** 开始时间戳, 毫秒, 默认`0`, 条件：`>=`
    * `end`: **(long)** 结束时间戳, 毫秒, 默认`0`, 条件：`<=`
    * `lastid`: **(long)** 最后一条聊天消息的id, 第一次默认传`0`, 条件：`> or <`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `exception`: **(Exception)**
            * `payload`: **(Map(num:int,lastid:long,begin:long,end:long,msgs:List(BroadcastMsg)))**
                * `BroadcastMsg.id` **(long)**
                * `BroadcastMsg.from` **(long)**
                * `BroadcastMsg.mid` **(long)**
                * `BroadcastMsg.msg` **(String)**
                * `BroadcastMsg.attrs` **(String)**
                * `BroadcastMsg.mtime` **(long)**

* `getP2PChat(long uid, long ouid, boolean desc, int num, long begin, long end, long lastid, int timeout, FPCallback.ICallback callback)`: 获取P2P历史聊天消息, `mtypes=Arrays.asList((byte)30)`
    * `uid`: **(long)** 获取和两个用户之间的历史聊天消息
    * `ouid`: **(long)** 获取和两个用户之间的历史聊天消息
    * `desc`: **(boolean)** `true`: 则从`end`的时间戳开始倒序翻页, `false`: 则从`begin`的时间戳顺序翻页
    * `num`: **(int)** 获取数量, **一次最多获取20条, 建议10条**
    * `begin`: **(long)** 开始时间戳, 毫秒, 默认`0`, 条件：`>=`
    * `end`: **(long)** 结束时间戳, 毫秒, 默认`0`, 条件：`<=`
    * `lastid`: **(long)** 最后一条聊天消息的id, 第一次默认传`0`, 条件：`> or <`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `exception`: **(Exception)**
            * `payload`: **(Map(num:int,lastid:long,begin:long,end:long,msgs:List(P2PMsg)))**
                * `P2PMsg.id` **(long)**
                * `P2PMsg.direction` **(byte)**
                * `P2PMsg.mid` **(long)**
                * `P2PMsg.msg` **(String)**
                * `P2PMsg.attrs` **(String)**
                * `P2PMsg.mtime` **(long)**

* `deleteChat(long mid, long from, long to, int timeout, FPCallback.ICallback callback)`: 删除P2P聊天消息
    * `mid`: **(long)** 聊天消息 id
    * `from`: **(long)** 发送方 id
    * `to`: **(long)** 聊天消息接收方User id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(Map)**
            * `exception`: **(Exception)**
            
* `deleteGroupChat(long mid, long from, long gid, int timeout, FPCallback.ICallback callback)`: 删除Group聊天消息
    * `mid`: **(long)** 聊天消息 id
    * `from`: **(long)** 发送方 id
    * `gid`: **(long)** 聊天消息接收方Group id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(Map)**
            * `exception`: **(Exception)**
            
* `deleteRoomChat(long mid, long from, long rid, int timeout, FPCallback.ICallback callback)`: 删除Room聊天消息
    * `mid`: **(long)** 聊天消息 id
    * `from`: **(long)** 发送方 id
    * `rid`: **(long)** 聊天消息接收方Room id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(Map)**
            * `exception`: **(Exception)**
            
* `deleteBroadcastChat(long mid, long from, int timeout, FPCallback.ICallback callback)`: 删除广播聊天消息(admin id)
    * `mid`: **(long)** 聊天消息 id
    * `from`: **(long)** admin id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(Map)**
            * `exception`: **(Exception)**

* `translate(String text, String src, String dst, String type, String profanity, int timeout, FPCallback.ICallback callback)`: 翻译消息, 需启用翻译服务, 返回{source:原始聊天消息语言类型,target:翻译后的语言类型,sourceText:原始聊天消息,targetText:翻译后的聊天消息}
    * `text`: **(String)** 待翻译的原始聊天消息
    * `src`: **(String)** 待翻译的聊天消息的语言类型, 参考RTMConfig.TRANS_LANGUAGE成员
    * `dst`: **(String)** 本次翻译的目标语言类型, 参考RTMConfig.TRANS_LANGUAGE成员
    * `type`: **(String)** 可选值为`chat`或`mail`, 默认:`chat`
    * `profanity`: **(String)** 敏感语过滤, 设置为以下三项之一: `off` `stop` `censor`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(Map(source:String,target:String,sourceText:String,targetText:String))**
            * `exception`: **(Exception)**

* `profanity(String text, String action, int timeout, FPCallback.ICallback callback)`: 敏感词过滤, 返回过滤后的字符串或者以错误形式返回, 需启用翻译服务
    * `text`: **(String)** 待检查文本
    * `action`: **(String)** 检查结果返回形式, `stop`: 以错误形式返回, `censor`: 用`*`替换敏感词
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(Map(text:String))**
            * `exception`: **(Exception)**
            
* `transcribe(byte[] audio, String lang, String action, int timeout, FPCallback.ICallback callback)`: 语音识别, 返回过滤后的字符串或者以错误形式返回, 需启用翻译服务, 设置超时时间不低于60s
    * `audio`: **(byte[])** 待识别语音数据
    * `lang`: **(String)** 待识别语音的类型, 参考`RTMConfig.TRANS_LANGUAGE`成员
    * `action`: **(String)** 检查结果返回形式, `stop`: 以错误形式返回, `censor`: 用`*`替换敏感词
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(Map(text:String,lang:String))**
            * `exception`: **(Exception)**

> file token

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

> user action

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

* `isProjectBlack(long uid, int timeout, FPCallback.ICallback callback)`: 检查阻止(project)
    * `uid`: **(long)** 用户 id
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `payload`: **(Map(ok:boolean))**
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

> friends action

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

> group action

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

> room action

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

> monitor action

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

> data save

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
    * `val`: **(String)** 存储数据实际内容, 最长`65535字节`
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
        
> file send
 
* `sendFile(long from, long to, byte mtype, byte[] fileBytes, String fileExt, String fileName, long mid, int timeout, FPCallback.ICallback callback)`: 发送文件
    * `from`: **(long)** 发送方 id
    * `to`: **(long)** 接收方 uid
    * `mtype`: **(byte)** 文件类型
    * `fileBytes`: **(byte[])** 文件内容
    * `fileExt`: **(String)** 文件扩展名
    * `fileName`: **(String)** 文件名
    * `mid`: **(long)** 消息 id, 用于过滤重复消息, 非重发时为`0`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `mid`: **(long)**
            * `payload`: **(Map(mtime:long))**
            * `exception`: **(Exception)**

* `sendFiles(long from, List<Long> tos, byte mtype, byte[] fileBytes, String fileExt, String fileName, long mid, int timeout, FPCallback.ICallback callback)`: 给多人发送文件
    * `from`: **(long)** 发送方 id
    * `tos`: **(long)** 接收方 uids
    * `mtype`: **(byte)** 文件类型
    * `fileBytes`: **(byte[])** 文件内容
    * `fileExt`: **(String)** 文件扩展名
    * `fileName`: **(String)** 文件名
    * `mid`: **(long)** 消息 id, 用于过滤重复消息, 非重发时为`0`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `mid`: **(long)**
            * `payload`: **(Map(mtime:long))**
            * `exception`: **(Exception)**

* `sendGroupFile(long from, long gid, byte mtype, byte[] fileBytes, String fileExt, String fileName, long mid, int timeout, FPCallback.ICallback callback)`: 给Group发送文件
    * `from`: **(long)** 发送方 id
    * `gid`: **(long)** Group id
    * `mtype`: **(byte)** 文件类型
    * `fileBytes`: **(byte[])** 文件内容
    * `fileExt`: **(String)** 文件扩展名
    * `fileName`: **(String)** 文件名
    * `mid`: **(long)** 消息 id, 用于过滤重复消息, 非重发时为`0`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `mid`: **(long)**
            * `payload`: **(Map(mtime:long))**
            * `exception`: **(Exception)**

* `sendRoomFile(long from, long rid, byte mtype, byte[] fileBytes, String fileExt, String fileName, long mid, int timeout, FPCallback.ICallback callback)`: 给Room发送文件
    * `from`: **(long)** 发送方 id
    * `rid`: **(long)** Room id
    * `mtype`: **(byte)** 文件类型
    * `fileBytes`: **(byte[])** 文件内容
    * `fileExt`: **(String)** 文件扩展名
    * `fileName`: **(String)** 文件名
    * `mid`: **(long)** 消息 id, 用于过滤重复消息, 非重发时为`0`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `mid`: **(long)**
            * `payload`: **(Map(mtime:long))**
            * `exception`: **(Exception)**

* `broadcastFile(long from, byte mtype, byte[] fileBytes, String fileExt, String fileName, long mid, int timeout, FPCallback.ICallback callback)`: 给整个Project发送文件(admin id)
    * `from`: **(long)** admin id
    * `mtype`: **(byte)** 文件类型
    * `fileBytes`: **(byte[])** 文件内容
    * `fileExt`: **(String)** 文件扩展名
    * `fileName`: **(String)** 文件名
    * `mid`: **(long)** 消息 id, 用于过滤重复消息, 非重发时为`0`
    * `timeout`: **(int)** 超时时间(ms)
    * `callback`: **(FPCallback.ICallback)** 回调方法
        * `cbdata`: **(CallbackData)**
            * `mid`: **(long)**
            * `payload`: **(Map(mtime:long))**
            * `exception`: **(Exception)**
