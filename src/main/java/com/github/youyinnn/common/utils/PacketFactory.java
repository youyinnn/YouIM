package com.github.youyinnn.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.github.youyinnn.common.OpCode;
import com.github.youyinnn.common.intf.Const;
import com.github.youyinnn.common.intf.MsgType;
import com.github.youyinnn.common.packets.*;
import com.github.youyinnn.server.Server;
import com.github.youyinnn.youwebutils.second.JsonHelper;
import org.tio.core.intf.Packet;

import java.io.UnsupportedEncodingException;

/**
 * @author youyinnn
 */
public class PacketFactory {

    public static BasePacket loginRequestPacket(String userId){
        return new BasePacket(MsgType.LOGIN_REQ, new LoginRequestBody(userId));
    }

    public static Packet loginResponsePacket(String resultCode, String token) {
        if (Server.isWebSocketProtocol()) {
            return getTextWsPacket(2, "resultCode", resultCode, "token", token);
        } else {
            return new BasePacket(MsgType.LOGIN_RESP, new LoginResponseBody(resultCode, token));
        }
    }

    public static BasePacket joinGroupRequestPacket(String group, String fromUserId) {
        return new BasePacket(MsgType.JOIN_GROUP_REQ, new JoinGroupRequestBody(group, fromUserId));
    }

    public static Packet joinGroupResponsePacket(String resultCode, String msg, String group) {
        if (Server.isWebSocketProtocol()) {
            return getTextWsPacket( 4,"resultCode", resultCode, "msg", msg, "group", group);
        } else {
            return new BasePacket(MsgType.JOIN_GROUP_RESP, new JoinGroupResponseBody(resultCode, msg, group));
        }
    }

    public static BasePacket p2PMsgRequestPacket(String msg, String toUserId, String fromUserId) {
        return new BasePacket(MsgType.P2P_REQ, new P2PRequestBody(msg, toUserId, fromUserId));
    }

    public static Packet p2PMsgResponsePacket(String msg, String fromUserId) {
        if (Server.isWebSocketProtocol()) {
            return getTextWsPacket( 6,"msg", msg, "fromUserId", fromUserId);
        } else {
            return new BasePacket(MsgType.P2P_RESP, new P2PResponseBody(msg, fromUserId));
        }
    }

    public static BasePacket groupMsgRequestPacket(String msg, String toGroup, String fromUserId) {
        return new BasePacket(MsgType.GROUP_MSG_REQ, new GroupMsgRequestBody(msg, toGroup, fromUserId));
    }

    public static Packet groupMsgResponsePacket(String msg, String fromUserId, String toGroup) {
        if (Server.isWebSocketProtocol()) {
            return getTextWsPacket( 8,"msg", msg, "fromUserId", fromUserId, "toGroup", toGroup);
        } else {
            return new BasePacket(MsgType.GROUP_MSG_RESP, new GroupMsgResponseBody(msg, fromUserId, toGroup));
        }
    }

    public static BasePacket heartbeatRequestPacket() {
        return new BasePacket(MsgType.HEART_BEAT_REQ, new BaseBody());
    }

    public static Packet systemMsgToAllPacket(String msg) {
        if (Server.isWebSocketProtocol()) {
            return getTextWsPacket( 100,"msg", msg, "fromUserId", "SYSTEM");
        } else {
            return new BasePacket(MsgType.SYS_MSG_2ALL, new P2PResponseBody(msg, "SYSTEM"));
        }
    }

    public static Packet systemMsgToOnePacket(String msg) {
        if (Server.isWebSocketProtocol()) {
            return getTextWsPacket( 101,"msg", msg, "fromUserId", "SYSTEM");
        } else {
            return new BasePacket(MsgType.SYS_MSG_2ONE, new P2PResponseBody(msg, "SYSTEM"));
        }
    }

    public static Packet systemMsgToGroupPacket(String msg, String toGroup) {
        if (Server.isWebSocketProtocol()) {
            return getTextWsPacket( 102,"msg", msg, "toGroup", toGroup, "fromUserId", "SYSTEM");
        } else {
            return new BasePacket(MsgType.SYS_MSG_2GROUP, new GroupMsgResponseBody(msg, "SYSTEM", toGroup));
        }
    }

    public static BasePacket logoutRequestPacket(String userId) {
        return new BasePacket(MsgType.LOGOUT_REQ, new LogoutRequestBody(userId));
    }

    public static BasePacket quitGroupRequestPacket(String fromUserId, String groupId) {
        return new BasePacket(MsgType.QUIT_GROUP_REQ, new QuitGroupRequestBody(fromUserId, groupId));
    }

    public static Packet memberOnlineInformPacket(String userId, String groupId) {
        if (Server.isWebSocketProtocol()) {
            return getTextWsPacket(107, "userId", userId, "groupId", groupId);
        } else {
            return new BasePacket(MsgType.MEMBER_ONLINE_INFORM, new MemberOnlineInformBody(userId, groupId));
        }
    }

    private static BaseWsPacket getTextWsPacket(Object msgType, Object... objects) {
        JSONObject jsonObject = JsonHelper.getJsonObject(objects);
        jsonObject.put("msgType", msgType);
        try {
            BaseWsPacket wsPacket =new BaseWsPacket(jsonObject.toJSONString().getBytes(Const.CHARSET));
            wsPacket.setWsOpCode(OpCode.TEXT);
            return wsPacket;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
