package com.github.youyinnn.common;

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
     * 心跳
     */
    byte HEART_BEAT_REQ = 99;
}
