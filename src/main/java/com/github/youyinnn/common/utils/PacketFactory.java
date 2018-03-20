package com.github.youyinnn.common.utils;

import com.github.youyinnn.common.OpCode;
import com.github.youyinnn.common.intf.Const;
import com.github.youyinnn.common.intf.MsgType;
import com.github.youyinnn.common.packets.*;
import com.github.youyinnn.youwebutils.second.JsonHelper;

import java.io.UnsupportedEncodingException;

/**
 * @author youyinnn
 */
public class PacketFactory {

    public static BasePacket loginRequestPacket(String userId){
        return new BasePacket(MsgType.LOGIN_REQ, new LoginRequestBody(userId));
    }

    public static BasePacket loginResponsePacket(String resultCode, String token) {
        return new BasePacket(MsgType.LOGIN_RESP, new LoginResponseBody(resultCode, token));
    }

    public static BasePacket joinGroupRequestPacket(String group, String fromUserId) {
        return new BasePacket(MsgType.JOIN_GROUP_REQ, new JoinGroupRequestBody(group, fromUserId));
    }

    public static BasePacket joinGroupResponsePacket(String resultCode, String msg, String group) {
        return new BasePacket(MsgType.JOIN_GROUP_RESP, new JoinGroupResponseBody(resultCode, msg, group));
    }

    public static BasePacket p2PMsgRequestPacket(String msg, String toUserId, String fromUserId) {
        return new BasePacket(MsgType.P2P_REQ, new P2PRequestBody(msg, toUserId, fromUserId));
    }

    public static BasePacket p2PMsgResponsePacket(String msg, String fromUserId) {
        return new BasePacket(MsgType.P2P_RESP, new P2PResponseBody(msg, fromUserId));
    }

    public static BasePacket groupMsgRequestPacket(String msg, String toGroup, String fromUserId) {
        return new BasePacket(MsgType.GROUP_MSG_REQ, new GroupMsgRequestBody(msg, toGroup, fromUserId));
    }

    public static BasePacket groupMsgResponsePacket(String msg, String fromUserId, String toGroup) {
        return new BasePacket(MsgType.GROUP_MSG_RESP, new GroupMsgResponseBody(msg, fromUserId, toGroup));
    }

    public static BasePacket heartbeatRequestPacket() {
        return new BasePacket(MsgType.HEART_BEAT_REQ, new BaseBody());
    }

    public static BasePacket systemMsgToAllPacket(String msg) {
        return new BasePacket(MsgType.SYS_MSG_2ALL, new P2PResponseBody(msg, "SYSTEM"));
    }

    public static BasePacket systemMsgToOnePacket(String msg) {
        return new BasePacket(MsgType.SYS_MSG_2ONE, new P2PResponseBody(msg, "SYSTEM"));
    }

    public static BasePacket systemMsgToGroupPacket(String msg, String toGroup) {
        return new BasePacket(MsgType.SYS_MSG_2GROUP, new GroupMsgResponseBody(msg, "SYSTEM", toGroup));
    }

    public static BasePacket logoutRequestPacket(String userId) {
        return new BasePacket(MsgType.LOGOUT_REQ, new LogoutRequestBody(userId));
    }

    public static BasePacket quitGroupRequestPacket(String userId, String groupId) {
        return new BasePacket(MsgType.QUIT_GROUP_REQ, new QuitGroupRequestBody(userId, groupId));
    }

    public static BaseWsPacket loginResponseWsPacket(String resultCode, String token) {
        return getTextWsPacket(JsonHelper.getJsonStr("resultCode", resultCode, "token", token));
    }

    public static BaseWsPacket getTextWsPacket(String msgBody) {
        try {
            BaseWsPacket wsPacket;
            wsPacket = new BaseWsPacket(msgBody.getBytes(Const.CHARSET));
            wsPacket.setWsOpCode(OpCode.TEXT);
            return wsPacket;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
