## YouIM
[![Travis](https://img.shields.io/badge/version-0.7.1-green.svg)]()
[![Travis](https://img.shields.io/badge/tio-2.1.0.v20180328-brightgreen.svg)]()
[![Travis](https://img.shields.io/badge/youdbutils-2.2.3-brightgreen.svg)]()

**基于t-io的轻巧部署的IM服务，支持tcp和ws连接。**

#### 使用

通过maven：
``` xml
    <dependency>
        <groupId>com.github.youyinnn</groupId>
        <artifactId>you-im</artifactId>
        <version>0.7.1</version>
    </dependency>
```

#### 特点

- 基于tio
- 实现了绝大部分的IM业务的处理，开箱即用

#### TODO
- [x] logout(done)
- [x] 群消息服务(done)
- [x] 适合用户的接口(done)
- [ ] 离线消息服务如何实现?
- [ ] 聊天记录本地存储服务?
    - 点对点聊天记录
    - 群组聊天记录
    - 聊天记录应该和离线消息挂钩
    
- 离线消息系统至关重要.    
- 加群/加好友的申请使用:异步+离线的方式解决, 绝不使用同步方式解决
    
- [x] 已确定的群组关系如何离线保持?(done)
    - [x] 用于群组关系的消息布告(done)
    - [x] 用户所属群组确认(done)
    - [x] 群组管理员关系确认, 可同意/拒绝加群申请(done)
    - [x] 群主确认,可解散该群, 可同意/拒绝加群申请(done)
    
- [x] 已确定的好友关如何离线保持?(done)
    - [x] 用于好友关系的消息布告(done)
    - [x] 好友关系确认(done)

- [x] 建立基本的IM用户关系管理, 用以消息布告:
    - [x] 好友关系消息布告,接受其好友消息的消息布告:
        - [x] 上线提醒(done)
        - [ ] ~~个人信息的更新~~(这个布告可以不用im来实现, 而是用户主动走http请求去用户管理系统获取)
        - [x] 好友添加的请求,请求到对方(done)
        
    - [x] 群组关系消息布告,接受其群组成员消息的消息布告(done):
        - [x] 上线提醒(done)
        - [x] 加群请求,请求到该群所有的管理员(done)
        - [x] 退群提醒,提醒到该群的所有管理员(done)
        - [x] 群解散提醒, 提醒到该群所有用户(done)
