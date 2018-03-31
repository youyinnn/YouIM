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
            return getTextWsPacket(MsgType.LOGIN_RESP, "resultCode", resultCode, "token", token);
        } else {
            return new BasePacket(MsgType.LOGIN_RESP, new LoginResponseBody(resultCode, token));
        }
    }

    public static BasePacket joinGroupRequestPacket(String groupId, String fromUserId) {
        return new BasePacket(MsgType.JOIN_GROUP_REQ, new JoinGroupRequestBody(groupId, fromUserId));
    }

    public static Packet joinGroupResponsePacket(boolean confirmResult,  String groupId, String toUserId) {
        if (Server.isWebSocketProtocol()) {
            return getTextWsPacket( MsgType.JOIN_GROUP_RESP,"confirmResult", confirmResult, "groupId", groupId, "toUserId", toUserId);
        } else {
            return new BasePacket(MsgType.JOIN_GROUP_RESP, new JoinGroupResponseBody(confirmResult, groupId, toUserId));
        }
    }

    public static BasePacket p2PMsgRequestPacket(String msg, String toUserId, String fromUserId) {
        return new BasePacket(MsgType.P2P_REQ, new P2PRequestBody(msg, toUserId, fromUserId));
    }

    public static Packet p2PMsgResponsePacket(String msg, String fromUserId) {
        if (Server.isWebSocketProtocol()) {
            return getTextWsPacket( MsgType.P2P_RESP,"msg", msg, "fromUserId", fromUserId);
        } else {
            return new BasePacket(MsgType.P2P_RESP, new P2PResponseBody(msg, fromUserId));
        }
    }

    public static BasePacket p2GRequestPacket(String msg, String toGroup, String fromUserId) {
        return new BasePacket(MsgType.P2G_REQ, new P2GRequestBody(msg, toGroup, fromUserId));
    }

    public static Packet p2gResponsePacket(String msg, String fromUserId, String toGroup) {
        if (Server.isWebSocketProtocol()) {
            return getTextWsPacket( MsgType.P2G_RESP,"msg", msg, "fromUserId", fromUserId, "toGroup", toGroup);
        } else {
            return new BasePacket(MsgType.P2G_RESP, new P2GResponseBody(msg, fromUserId, toGroup));
        }
    }

    public static BasePacket heartbeatRequestPacket() {
        return new BasePacket(MsgType.HEART_BEAT_REQ, new BaseBody());
    }

    public static Packet systemMsgToAllPacket(String msg) {
        if (Server.isWebSocketProtocol()) {
            return getTextWsPacket( MsgType.SYS_MSG_2ALL,"msg", msg, "fromUserId", "SYSTEM");
        } else {
            return new BasePacket(MsgType.SYS_MSG_2ALL, new P2PResponseBody(msg, "SYSTEM"));
        }
    }

    public static Packet systemMsgToOnePacket(String msg) {
        if (Server.isWebSocketProtocol()) {
            return getTextWsPacket( MsgType.SYS_MSG_2ONE,"msg", msg, "fromUserId", "SYSTEM");
        } else {
            return new BasePacket(MsgType.SYS_MSG_2ONE, new P2PResponseBody(msg, "SYSTEM"));
        }
    }

    public static Packet systemMsgToGroupPacket(String msg, String toGroup) {
        if (Server.isWebSocketProtocol()) {
            return getTextWsPacket( MsgType.SYS_MSG_2GROUP,"msg", msg, "toGroup", toGroup, "fromUserId", "SYSTEM");
        } else {
            return new BasePacket(MsgType.SYS_MSG_2GROUP, new P2GResponseBody(msg, "SYSTEM", toGroup));
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
            return getTextWsPacket(MsgType.MEMBER_ONLINE_INFORM, "userId", userId, "groupId", groupId);
        } else {
            return new BasePacket(MsgType.MEMBER_ONLINE_INFORM, new MemberOnlineInformBody(userId, groupId));
        }
    }

    public static Packet friendOnlineInformPacket(String friendId) {
        if (Server.isWebSocketProtocol()) {
            return getTextWsPacket(MsgType.FRIEND_ONLINE_INFORM, "friendId", friendId);
        } else {
            return new BasePacket(MsgType.FRIEND_ONLINE_INFORM, new FriendOnlineInformBody(friendId));
        }
    }

    public static Packet memberOfflineInformPacket(String userId, String groupId) {
        if (Server.isWebSocketProtocol()) {
            return getTextWsPacket(MsgType.MEMBER_OFFLINE_INFORM, "userId", userId, "groupId", groupId);
        } else {
            return new BasePacket(MsgType.MEMBER_OFFLINE_INFORM, new MemberOfflineInformBody(userId, groupId));
        }
    }

    public static Packet friendOfflineInformPacket(String friendId) {
        if (Server.isWebSocketProtocol()) {
            return getTextWsPacket(MsgType.FRIEND_OFFLINE_INFORM, "friendId", friendId);
        } else {
            return new BasePacket(MsgType.FRIEND_OFFLINE_INFORM, new FriendOfflineInformBody(friendId));
        }
    }

    public static Packet addFriendConfirmInformPacket(String fromUserId) {
        if (Server.isWebSocketProtocol()) {
            return getTextWsPacket(MsgType.SYS_ADD_FRIEND_CONFIRM, "fromUserId", fromUserId);
        } else {
            return new BasePacket(MsgType.SYS_ADD_FRIEND_CONFIRM, new AddFriendConfirmInformBody(fromUserId));
        }
    }

    public static Packet joinGroupConfirmInformPacket(String groupId, String fromUserId) {
        if (Server.isWebSocketProtocol()) {
            return getTextWsPacket(MsgType.SYS_JOIN_GROUP_CONFIRM,  "groupId", groupId, "fromUserId", fromUserId);
        } else {
            return new BasePacket(MsgType.SYS_JOIN_GROUP_CONFIRM, new JoinGroupConfirmInformBody(groupId, fromUserId));
        }
    }

    public static Packet quitGroupInformPacket(String fromUserId, String groupId) {
        if (Server.isWebSocketProtocol()) {
            return getTextWsPacket(MsgType.QUIT_GROUP_INFORM, "fromUserId", fromUserId, "groupId", groupId);
        } else {
            return new BasePacket(MsgType.QUIT_GROUP_INFORM, new QuitGroupInformBody(fromUserId, groupId));
        }
    }

    public static Packet removeFriendInformPacket(String fromUserId, String toUserId) {
        if (Server.isWebSocketProtocol()) {
            return getTextWsPacket(MsgType.REMOVE_FRIEND_INFORM, "fromUserId", fromUserId, "toUserId", toUserId);
        } else {
            return new BasePacket(MsgType.REMOVE_FRIEND_INFORM, new RemoveFriendInformBody(fromUserId, toUserId));
        }
    }

    public static Packet kickMemberInformPacket(String fromAdministratorUserId, String toUserId, String fromGroup) {
        if (Server.isWebSocketProtocol()) {
            return getTextWsPacket(MsgType.KICK_MEMBER_INFORM,
                    "fromAdministratorUserId", fromAdministratorUserId, "fromGroup", fromGroup, "toUserId", toUserId);
        } else {
            return new BasePacket(MsgType.KICK_MEMBER_INFORM, new KickMemberInformBody(fromAdministratorUserId, fromGroup, toUserId));
        }
    }

    public static Packet addAdminInformPacket(String ownerId, String toUserId, String groupId) {
        if (Server.isWebSocketProtocol()) {
            return getTextWsPacket(MsgType.ADD_ADMIN_INFORM, "ownerId", ownerId,
                    "toUserId", toUserId, "groupId", groupId);
        } else {
            return new BasePacket(MsgType.ADD_ADMIN_INFORM, new AddAdminInformBody(ownerId, toUserId, groupId));
        }
    }

    public static Packet removeAdminInformPacket(String ownerId, String groupId, String fromUserId) {
        if (Server.isWebSocketProtocol()) {
            return getTextWsPacket(MsgType.REMOVE_ADMIN_INFORM, "ownerId", ownerId, "groupId" , groupId,
                    "fromUserId", fromUserId);
        } else {
            return new BasePacket(MsgType.REMOVE_ADMIN_INFORM, new RemoveAdminInformBody(ownerId, groupId, fromUserId));
        }
    }

    public static Packet dissolveGroupInformPacket(String groupId) {
        if (Server.isWebSocketProtocol()) {
            return getTextWsPacket(MsgType.DISSOLVE_GROUP_INFORM, "groupId", groupId);
        } else {
            return new BasePacket(MsgType.DISSOLVE_GROUP_INFORM, new DissolveGroupInformBody(groupId));
        }
    }

    public static Packet addFriendResponsePacket(String fromUserId, String toUserId, boolean confirmResult) {
        if (Server.isWebSocketProtocol()) {
            return getTextWsPacket(MsgType.ADD_FRIEND_RESP, "fromUserId", fromUserId, "toUserId", toUserId, "confirmResult", confirmResult);
        } else {
            return new BasePacket(MsgType.ADD_FRIEND_RESP, new AddFriendResponseBody(fromUserId, toUserId, confirmResult));
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
