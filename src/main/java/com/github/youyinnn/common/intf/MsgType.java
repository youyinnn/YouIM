package com.github.youyinnn.common.intf;

/**
 * The interface Msg type.
 *
 * @author youyinnn
 */
public interface MsgType {

    /**
     * 登录请求
     */
    byte LOGIN_REQ = 1;
    /**
     * 登录响应
     */
    byte LOGIN_RESP = 2;

    /**
     * 进入群组请求
     */
    byte JOIN_GROUP_REQ = 3;
    /**
     * 进入群组响应
     */
    byte JOIN_GROUP_RESP = 4;

    /**
     * 点对点消息请求
     */
    byte P2P_REQ = 5;
    /**
     * 点对点消息响应
     */
    byte P2P_RESP = 6;

    /**
     * 群聊消息请求
     */
    byte GROUP_MSG_REQ = 7;
    /**
     * 群聊消息响应
     */
    byte GROUP_MSG_RESP = 8;

    /**
     * 登出请求
     */
    byte LOGOUT_REQ = 9;

    /**
     * 登出响应
     */
    byte LOGOUT_RESP = 10;

    /**
     * 退群请求
     */
    byte QUIT_GROUP_REQ = 11;

    /**
     * 退群响应
     */
    byte QUIT_GROUP_RESP = 12;

    /**
     * 添加好友请求
     */
    byte ADD_FRIEND_REQ = 13;

    /**
     * 添加好友响应
     */
    byte ADD_FRIEND_RESP = 14;

    /**
     * 移除好友请求
     */
    byte REMOVE_FRIEND_REQ = 15;

    /**
     * 移除好友响应
     */
    byte REMOVE_FRIEND_RESP = 16;

    /**
     * 踢出成员请求
     */
    byte KICK_MEMBER_REQ = 17;

    /**
     * 踢出成员响应
     */
    byte KICK_MEMBER_RESP = 18;

    /**
     * 授权管理请求
     */
    byte ADD_ADMIN_REQ = 19;

    /**
     * 授权管理响应
     */
    byte ADD_ADMIN_RESP = 20;

    /**
     * 撤销管理请求
     */
    byte REMOVE_ADMIN_REQ = 21;

    /**
     * 撤销管理响应
     */
    byte REMOVE_ADMIN_RESP = 22;

    /**
     * 心跳
     */
    byte HEART_BEAT_REQ = 99;

    /**
     * 系统消息-所有人
     */
    byte SYS_MSG_2ALL = 100;

    /**
     * 系统消息-对某人
     */
    byte SYS_MSG_2ONE = 101;

    /**
     * 系统消息-对某组
     */
    byte SYS_MSG_2GROUP = 102;

    /**
     * 系统发送的加好友确认通知
     */
    byte SYS_FRIEND_ADD_CONFIRM = 103;

    /**
     * 用户返回的加好友确认
     */
    byte FRIEND_ADD_CONFIRM = 104;

    /**
     * 系统发送的群组加入确认通知
     */
    byte SYS_GROUP_ADD_CONFRIM = 105;

    /**
     * 用户返回的群组加入确认
     */
    byte GROUP_ADD_CONFIRM = 106;

    /**
     * 好友上线消息布告
     */
    byte FRIEND_ONLINE_INFORM = 105;

    /**
     * 好友下线消息布告
     */
    byte FRIEND_OFFLINE_INFORM = 106;

    /**
     * 群组成员上线布告
     */
    byte MEMBER_ONLINE_INFORM = 107;

    /**
     * 群组成员下线布告
     */
    byte MEMBER_OFFLINE_INFORM = 108;

}
