## YouIM的Json请求与响应格式

- - -

### 请求格式

> ##### LOGIN_REQ - Type : 1
> ###### 说明 : 登陆请求, 消息体包含请求登陆的Id

``` json
{
  "msgType" : "1",
  "msgBody":{
    "loginUserId" : "123"
  }
}
```

> ##### JOIN_GROUP_REQ Type : 3
> ###### 说明 : 请求进入群组, 消息体包含请求者Id, 请求者绑定的token, 目标群组Id

``` json
{
  "msgType" : "3",
   "msgBody":{
     "token" : "1522913807330_5", 
     "fromUserId" : "5646",
     "groupId" : "ccc"
  }
}
```

> ##### P2P_REQ Type : 5
> ###### 说明 : 点对点消息请求, 消息体包含token, 接收者Id, 消息内容, 发送者Id

``` json
{
  "msgType" : "5", 
  "msgBody":{
    "token" : "1522917270570_9", 
    "toUserId" : "456",
    "msg" : "asdsad",
    "fromUserId" : "123"
  }
}
```

> ##### P2G_REQ Type : 7 
> ###### 说明 : 点对群消息请求, 消息体包含token, 发送者Id, 接收群组Id, 消息内容

``` json
{
  "msgType" : "7", 
  "msgBody":{
    "token" : "1522917270570_9",
     "toGroup" : "ccc",
     "msg" : "asdsad",
     "fromUserId" : "123"
  }
}
```

> ##### LOGOUT_REQ Type : 9
> ###### 说明 : 登出请求, 包含登出Id, token

``` json
{
  "msgType" : "9", 
  "msgBody":{
    "token" : "1522917270570_9", 
    "logoutUserId" : "123"
  }
}
```

> ##### QUIT_GROUP_REQ Type : 11
> ###### 说明 : 退出群组请求, 消息体包含请求者Id, token, 要退出的群组的Id

``` json
{
  "msgType" : "11", 
  "msgBody":{
    "token" : "1522918935301_11", 
    "fromUserId" : "999", 
    "groupId" : "aaa"
  }
}
```

> ##### ADD_FRIEND_REQ - Type : 13
> ###### 说明 : 添加好友请求, 消息体包含请求者Id, 目标用户Id, 请求者token

``` json
{
  "msgType" : "13",
  "msgBody":{
    "token" : "1522913807330_5", 
    "toUserId" : "456",
    "fromUserId":"123"
  }
}
```

> ##### REMOVE_FRIEND_REQ Type : 15
> ###### 说明 : 移除好友请求, 消息体包含token, 请求者Id, 要删除好友的Id

``` json
{
  "msgType" : "15", 
  "msgBody":{
    "token" : "1522918942580_12", 
    "fromUserId" : "123",
    "toUserId":"999"
  }
}
```

> ##### KICK_MEMBER_REQ Type : 17
> ###### 说明 : 踢出成员请求, 消息体包含token, 发出该请求的管理员Id, 从哪个组踢出的群Id, 被踢出人的Id

``` json
{
  "msgType" : "17", 
  "msgBody":{
    "token" : "1522918942580_12", 
    "fromAdministratorId" : "123" , 
    "fromGroupId" : "aaa", 
    "toUSerId" : "999"
  }
}
```

> ##### ADD_ADMIN_REQ Type : 19
> ###### 说明 : 授权管理请求, 消息体包含token, 请求者Id且这个Id必须是该群组的持有者, 被授权人的Id, 授权管理的群组Id

``` json
{
  "msgType" : "19", 
  "msgBody":{
    "token" : "1522918942580_12", 
    "ownerId" : "123", 
    "groupId" : "aaa", 
    "toUserId" : "999"
  }
}
```

> ##### REMOVE_ADMIN_REQ Type : 21
> ###### 说明 : 移除管理请求, 消息体包含token, 请求者Id且这个Id必须是该群组的持有者, 被移除授权人的Id, 移除授权的群组Id

``` json
{
  "msgType" : "21",
   "msgBody":{
     "token" : "1522918942580_12", 
     "ownerId" : "123", 
     "groupId" : "aaa", 
     "fromUserId" : "999"
  }
}
```

> ##### HEART_BEAT_REQ Type : 99
> ###### 说明 : 心跳请求

``` json
{
  "msgType" : "99"
}
```

> ##### ADD_FRIEND_CONFIRM_MSG Type : 104
> ###### 说明 : 用户发给系统的添加好友的确认结果请求, 包含token, 处理该申请的用户Id, 申请者Id, 确认结果

``` json
{
  "msgType" : "104", 
  "msgBody":{
    "token" : "1522918935301_11", 
    "fromUserId" : "999",
    "toUserId":"123",
    "confirmResult": true
  }
}
```

> ##### JOIN_GROUP_CONFIRM_MSG Type : 106
> ###### 说明 : 管理员发给系统的加入群组的确认结果请求, 包含token, 处理该申请的管理员Id, 群组Id, 申请者Id, 确认结果

``` json
{
  "msgType" : "106", 
  "msgBody":{
    "token" : "1522917278993_10", 
    "handleAdministratorId" : "456", 
    "groupId" : "ccc", 
    "toUserId" : "123",
    "confirmResult": true
  }
}
```

> ##### DISSOLVE_GROUP_REQ Type : 111
> ###### 说明 : 群组解散请求, 消息体包含请求者token, 群组持有者id, 该id必须和请求发送者id一致, 要解散的群组id

``` json
{
  "msgType" : "111", 
  "msgBody":{
    "token" : "1522916620426_8", 
    "groupId" : "ccc", 
    "ownerId" : "456" 
  }
}
```

> ##### SIGN_IN_USER_REQ Type : 113
> ###### 说明 : 注册用户请求, 消息体包含注册Id

``` json
{
  "msgType" : "113",
   "msgBody":{
     "userId" : "4564"
  }
}
```

> ##### SIGN_IN_GROUP_REQ Type : 114
> ###### 说明 : 注册群组请求, 消息体包含注册者Id, 和群组Id, token

``` json
{
  "msgType" : "114", 
  "msgBody":{
    "token" : "1522917278993_10", 
    "ownerId" : "456", 
    "groupId" : "ccc"
  }
}
```

- - -

### 响应格式

> ##### LOGIN_RESP - Type : 2
> ###### 说明 : 收到登陆请求的结果, 包含结果码和token, 如果结果码为success, 则token为有效的token串, 否则token返会的是登陆失败的原因

``` json
{
   "msgType":2,
   "resultCode":"success",
   "token":"1522650512561_24"
}
```

> ##### JOIN_GROUP_RESP Type : 4
> ###### 说明 : 申请入群响应, 包含确认结果, 申请人Id, 申请加入的群组, 如果确认结果为true, 那么这个响应还会发送给该群组内的每一个成员

``` json
{
  "msgType":4,
  "confirmResult":true,
  "groupId":"ccc",
  "toUserId":"123"
}
```

> ##### P2P_RESP Type : 6
> ###### 说明 : 点对点消息响应, 包含消息内容, 发送者Id

``` json
{
  "msg":"asdsad",
  "msgType":6,
  "fromUserId":"123"
}
```

> ##### P2G_RESP Type : 8
> ###### 说明 : 点对群消息响应, 包含消息内容, 发送者Id, 接收群组Id

``` json
{
  "msg":"asdsad",
  "msgType":8,
  "fromUserId":"123",
  "toGroup":"ccc"
}
```

> ##### LOGOUT_RESP Type : 10
> ###### 说明 : 登出响应

``` json
暂无用处
```

> ##### QUIT_GROUP_INFORM Type : 12
> ###### 说明 : 退群响应, 包含退群者Id, 退出的群Id, 该响应发送给群组中的所有用户

``` json
{
  "msgType":12,
  "fromUserId":"999",
  "groupId":"aaa"
}
```

> ##### ADD_FRIEND_RESP - Type : 14
> ###### 说明 : 好友添加相应, 该响应返回给添加好友请求的申请者, 包含受理申请者受理该申请的结果, 以及双方Id

``` json
{
  "msgType":14,
  "fromUserId":"456",
  "confirmResult":true,
  "toUserId":"123"
}
```

> ##### REMOVE_FRIEND_INFORM Type : 16
> ###### 说明 : 移除好友响应, 该响应发送到给删除人的用户, 包含双方Id

``` json
{
  "msgType":16,
  "fromUserId":"123",
  "toUserId":"999"
}
```

> ##### KICK_MEMBER_INFORM Type : 18
> ###### 说明 : 踢出成员响应, 该响应发送到群组所有用户, 包含群组Id, 

``` json
{
  "msgType":18,
  "fromGroupId":"aaa",
  "fromAdministratorUserId":"123",
  "toUserId":"999"
}
```

> ##### ADD_ADMIN_INFORM Type : 20
> ###### 说明 : 授权管理的通知, 包含群Id, 群持有者Id, 被授权人的Id

``` json
{
  "msgType":20,
  "groupId":"aaa",
  "ownerId":"123",
  "toUserId":"999"
}
```

> ##### REMOVE_ADMIN_INFORM Type : 22
> ###### 说明 : 移除管理的通知, 包含群Id, 群持有者Id, 被移除权限人的Id

``` json
{
  "msgType":22,
  "fromUserId":"999",
  "groupId":"aaa",
  "ownerId":"123"
}
```

> ##### SYS_MSG_2ALL Type : 100
> ###### 说明 : 系统对所有人发的消息, 包含系统Id, 消息内容

``` json
{
  "msg":"nihao",
  "msgType":100,
  "fromUserId":"SYSTEM"
}
```

> ##### SYS_MSG_2ONE Type : 101
> ###### 说明 : 系统对某人发的消息, 包含系统Id, 消息内容

``` json
{
  "msg":"nihao",
  "msgType":101,
  "fromUserId":"SYSTEM"
}
```
 
> ##### SYS_MSG_2GROUP Type : 102
> ###### 说明 : 系统对某群组发的消息, 包含群组Id, 系统Id, 消息内容

``` json
{
  "msg":"dawdawd",
  "msgType":102,
  "fromUserId":"SYSTEM",
  "toGroup":"aaa"
}
```

> ##### SYS_ADD_FRIEND_CONFIRM Type : 103
> ###### 说明 : 系统发来的关于某用户申请添加好友的确认请求, 包含申请者Id

``` json
{
  "msgType":103,
  "fromUserId":"123"
}
```

> ##### SYS_JOIN_GROUP_CONFIRM Type : 105
> ###### 说明 : 系统发来的关于某用户进入群组的确认请求, 包含申请者Id, 申请群组Id

``` json
{
  "msgType":105,
  "fromUserId":"123",
  "groupId":"ccc"
}
```

> ##### FRIEND_ONLINE_INFORM Type : 107
> ###### 说明 : 好友上线提醒, 包含好友Id

``` json
{
  "msgType":107,
  "friendId":"456"
}
```

> ##### FRIEND_OFFLINE_INFORM Type : 108
> ###### 说明 : 好友下线提醒, 包含好友Id

``` json
{
  "msgType":108,
  "friendId":"456"
}
```

> ##### MEMBER_ONLINE_INFORM - Type : 109
> ###### 说明 : 群成员上线提醒, 包含群Id, 上线成员Id
``` json
{
  "msgType":109,
  "groupId":"ccc",
  "userId":"456"
}
```

> ##### MEMBER_OFFLINE_INFORM Type : 110
> ###### 说明 : 群成员下线提醒, 包含群Id, 成员Id

``` json
{
  "msgType":110,
  "groupId":"ccc",
  "userId":"456"
}
```

> ##### DISSOLVE_GROUP_INFORM Type : 112
> ###### 说明 : 群组解散提醒, 包含被解散的群Id

``` json
{
  "msgType":112,
  "groupId":"ccc"
}
```