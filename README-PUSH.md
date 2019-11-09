# fpnn rtm sdk java #

#### PushService ####

* `RTMProcessor::addPushService(String name, IService is)`: 添加推送回调
    * `name`: **(String)** 推送服务类型, 参考`RTMConfig.SERVER_PUSH`成员
    * `is`: **(IService)** 回调方法

* `RTMProcessor::removePushService(String name)`: 删除推送回调
    * `name`: **(String)** 推送服务类型, 参考`RTMConfig.SERVER_PUSH`成员

* `RTMProcessor::hasPushService(String name)`: 是否存在推送回调
    * `name`: **(String)** 推送服务类型, 参考`RTMConfig.SERVER_PUSH`成员
    
> action push

* `ping`: RTM主动ping, 依赖于监听`pushevent`事件
    * `data`: **(Map(String, Object))**

* `pushevent`: RTM主动推送事件
    * `data`: **(Map(String, Object))**
        * `data.event`: **(String)** 事件名称, 请参考`RTMConfig.SERVER_EVENT`成员
        * `data.uid`: **(long)** 触发者 id
        * `data.time`: **(int)** 触发时间(s)
        * `data.endpoint`: **(String)** 对应的RTMGate地址
        * `data.data`: **(String)** `预留`
            
> message push

* `pushmsg`: RTM主动推送P2P业务消息
    * `data`: **(Map(String, Object))**
        * `data.from`: **(long)** 发送者 id
        * `data.to`: **(long)** 接收者 id
        * `data.mtype`: **(byte)** 业务消息类型
        * `data.mid`: **(long)** 业务消息 id, 当前链接会话内唯一
        * `data.msg`: **(String)** 业务消息内容
        * `data.attrs`: **(String)** 发送时附加的自定义内容
        * `data.mtime`: **(long)**

* `pushgroupmsg`: RTM主动推送Group业务消息
    * `data`: **(Map(String, Object))**
        * `data.from`: **(long)** 发送者 id
        * `data.gid`: **(long)** Group id
        * `data.mtype`: **(byte)** 业务消息类型
        * `data.mid`: **(long)** 业务消息 id, 当前链接会话内唯一
        * `data.msg`: **(String)** 业务消息内容
        * `data.attrs`: **(String)** 发送时附加的自定义内容
        * `data.mtime`: **(long)**

* `pushroommsg`: RTM主动推送Room业务消息
    * `data`: **(Map(String, Object))**
        * `data.from`: **(long)** 发送者 id
        * `data.rid`: **(long)** Room id
        * `data.mtype`: **(byte)** 业务消息类型
        * `data.mid`: **(long)** 业务消息 id, 当前链接会话内唯一
        * `data.msg`: **(String)** 业务消息内容
        * `data.attrs`: **(String)** 发送时附加的自定义内容
        * `data.mtime`: **(long)**

> file push

* `pushfile`: RTM主动推送P2P文件
    * `data`: **(Map(String, Object))**
        * `data.from`: **(long)** 发送者 id
        * `data.to`: **(long)** 接收者 id
        * `data.mtype`: **(byte)** 文件类型, 请参考`RTMConfig.FILE_TYPE`成员
        * `data.mid`: **(long)** 业务文件消息 id, 当前链接会话内唯一
        * `data.msg`: **(String)** 文件获取地址(url)
        * `data.attrs`: **(String)** 发送时附加的自定义内容
        * `data.mtime`: **(long)**

* `pushgroupfile`: RTM主动推送Group文件
    * `data`: **(Map(String, Object))**
        * `data.from`: **(long)** 发送者 id
        * `data.gid`: **(long)** Group id
        * `data.mtype`: **(byte)** 文件类型, 请参考`RTMConfig.FILE_TYPE`成员
        * `data.mid`: **(long)** 业务文件消息 id, 当前链接会话内唯一
        * `data.msg`: **(String)** 文件获取地址(url)
        * `data.attrs`: **(String)** 发送时附加的自定义内容
        * `data.mtime`: **(long)**

* `pushroomfile`: RTM主动推送Room文件
    * `data`: **(Map(String, Object))**
        * `data.from`: **(long)** 发送者 id
        * `data.rid`: **(long)** Room id
        * `data.mtype`: **(byte)** 文件类型, 请参考`RTMConfig.FILE_TYPE`成员
        * `data.mid`: **(long)** 业务文件消息 id, 当前链接会话内唯一
        * `data.msg`: **(String)** 文件获取地址(url)
        * `data.attrs`: **(String)** 发送时附加的自定义内容
        * `data.mtime`: **(long)**
  
> chat push

* `pushchat`: RTM主动推送P2P聊天消息
    * `data`: **(Map(String, Object))**
        * `data.from`: **(long)** 发送者 id
        * `data.to`: **(long)** 接收者 id
        * `data.mid`: **(long)** 聊天消息 id, 当前链接会话内唯一
        * `data.attrs`: **(String)** 发送时附加的自定义内容
        * `data.mtime`: **(long)**
        * `data.msg`: **(JsonString)** 聊天消息内容
            * `source`: **(String)** 原始聊天消息语言类型, 参考`RTMConfig.TRANS_LANGUAGE`成员
            * `target`: **(String)** 翻译后的语言类型, 参考`RTMConfig.TRANS_LANGUAGE`成员
            * `sourceText`: **(String)** 原始聊天消息
            * `targetText`: **(String)** 翻译后的聊天消息

* `pushgroupchat`: RTM主动推送Group聊天消息
    * `data`: **(Map(String, Object))**
        * `data.from`: **(long)** 发送者 id
        * `data.gid`: **(long)** Group id
        * `data.mid`: **(long)** 聊天消息 id, 当前链接会话内唯一
        * `data.attrs`: **(String)** 发送时附加的自定义内容
        * `data.mtime`: **(long)**
        * `data.msg`: **(JsonString)** 聊天消息内容
            * `source`: **(String)** 原始聊天消息语言类型, 参考`RTMConfig.TRANS_LANGUAGE`成员
            * `target`: **(String)** 翻译后的语言类型, 参考`RTMConfig.TRANS_LANGUAGE`成员
            * `sourceText`: **(String)** 原始聊天消息
            * `targetText`: **(String)** 翻译后的聊天消息 

* `pushroomchat`: RTM主动推送Room聊天消息
    * `data`: **(Map(String, Object))**
        * `data.from`: **(long)** 发送者 id
        * `data.rid`: **(long)** Room id
        * `data.mid`: **(long)** 聊天消息 id, 当前链接会话内唯一
        * `data.attrs`: **(String)** 发送时附加的自定义内容
        * `data.mtime`: **(long)**
        * `data.msg`: **(JsonString)** 聊天消息内容
            * `source`: **(String)** 原始聊天消息语言类型, 参考`RTMConfig.TRANS_LANGUAGE`成员
            * `target`: **(String)** 翻译后的语言类型, 参考`RTMConfig.TRANS_LANGUAGE`成员
            * `sourceText`: **(String)** 原始聊天消息
            * `targetText`: **(String)** 翻译后的聊天消息 

> audio push

* `pushaudio`: RTM主动推送P2P聊天语音
    * `data`: **(Map(String, Object))**
        * `data.from`: **(long)** 发送者 id
        * `data.to`: **(long)** 接收者 id
        * `data.mid`: **(long)** 语音消息 id, 当前链接会话内唯一
        * `data.msg`: **(byte[])** 聊天语音数据
        * `data.attrs`: **(String)** 发送时附加的自定义内容
        * `data.mtime`: **(long)**
        
* `pushgroupaudio`: RTM主动推送Group聊天语音
    * `data`: **(Map(String, Object))**
        * `data.from`: **(long)** 发送者 id
        * `data.gid`: **(long)** Group id
        * `data.mid`: **(long)** 语音消息 id, 当前链接会话内唯一
        * `data.msg`: **(byte[])** 聊天语音数据
        * `data.attrs`: **(String)** 发送时附加的自定义内容
        * `data.mtime`: **(long)**
        
 * `pushroomaudio`: RTM主动推送Room聊天语音
     * `data`: **(Map(String, Object))**
         * `data.from`: **(long)** 发送者 id
         * `data.rid`: **(long)** Room id
         * `data.mid`: **(long)** 语音消息 id, 当前链接会话内唯一
         * `data.msg`: **(byte[])** 聊天语音数据
         * `data.attrs`: **(String)** 发送时附加的自定义内容
         * `data.mtime`: **(long)**

> cmd push

* `pushcmd`: RTM主动推送聊天命令
    * `data`: **(Map(String, Object))**
        * `data.from`: **(long)** 发送者 id
        * `data.to`: **(long)** 接收者 id
        * `data.mid`: **(long)** 命令消息 id, 当前链接会话内唯一
        * `data.msg`: **(String)** 命令内容
        * `data.attrs`: **(String)** 发送时附加的自定义内容
        * `data.mtime`: **(long)**
        
* `pushgroupcmd`: RTM主动推送Group聊天命令
    * `data`: **(Map(String, Object))**
        * `data.from`: **(long)** 发送者 id
        * `data.gid`: **(long)** Group id
        * `data.mid`: **(long)** 命令消息 id, 当前链接会话内唯一
        * `data.msg`: **(String)** 命令内容
        * `data.attrs`: **(String)** 发送时附加的自定义内容
        * `data.mtime`: **(long)**

* `pushroomcmd`: RTM主动推送Room聊天命令
    * `data`: **(Map(String, Object))**
        * `data.from`: **(long)** 发送者 id
        * `data.rid`: **(long)** Room id
        * `data.mid`: **(long)** 命令消息 id, 当前链接会话内唯一
        * `data.msg`: **(String)** 命令内容
        * `data.attrs`: **(String)** 发送时附加的自定义内容
        * `data.mtime`: **(long)**
