package com.github.youyinnn.server;

import com.github.youyinnn.common.BaseSessionContext;
import com.github.youyinnn.common.intf.Const;
import com.github.youyinnn.common.packets.confirm.AddFriendConfirmMsgBody;
import com.github.youyinnn.common.packets.confirm.JoinGroupConfirmMsgBody;
import com.github.youyinnn.common.packets.request.*;
import com.github.youyinnn.common.utils.PacketFactory;
import com.github.youyinnn.youdbutils.exceptions.NoneffectiveUpdateExecuteException;
import com.github.youyinnn.youwebutils.third.Log4j2Helper;
import org.apache.logging.log4j.Logger;
import org.tio.core.Aio;
import org.tio.core.ChannelContext;
import org.tio.utils.json.Json;

/**
 * @author youyinnn
 */
public class BasicImWorkflowHandler {

    private static final Logger SERVER_LOG = Log4j2Helper.getLogger("$im_server");

    public static Boolean loginRequestHandle(String bodyJsonStr, ChannelContext channelContext, String token) {
        LoginRequestBody loginRequestBody = Json.toBean(bodyJsonStr, LoginRequestBody.class);
        if (Server.getServerConfig().isUserManagementModuleEnabled()) {
            if (!UserManagementHandler.isUserExist(loginRequestBody.getLoginUserId())) {
                if (Server.isServerHandlerLogEnabled()) {
                    SERVER_LOG.info("该用户不存在: userId:{}.", loginRequestBody.getLoginUserId());
                }
                Aio.send(channelContext, PacketFactory.loginResponsePacket(Const.RequestCode.FAIL, "该用户不存在!"));
                return false;
            }
        }
        // 验证重复登陆
        if (Server.isLogin(loginRequestBody.getLoginUserId())) {
            if (Server.isServerHandlerLogEnabled()) {
                SERVER_LOG.info("收到重复登陆请求: userId:{}.", loginRequestBody.getLoginUserId());
            }
            Aio.send(channelContext, PacketFactory.loginResponsePacket(Const.RequestCode.FAIL, "登陆请求重复!"));
            return false;
        }
        if (Server.isServerHandlerLogEnabled()) {
                SERVER_LOG.info("收到登陆请求: userId:{}.",loginRequestBody.getLoginUserId());
        }
        /*
         * 从请求登陆方获取请求者id,将该连接通道和该id进行绑定
         * 需要注意的是,这里的绑定的含义是告诉框架:我这个连接通道和该id绑定了
         * 此绑定的意义是面向框架的,绑定的id是提供给框架服务的.
         */
        String userId = loginRequestBody.getLoginUserId();
        Aio.bindUser(channelContext, userId);
        Aio.bindToken(channelContext, token);
        /*
         * 在该请求连接中获取属性对象,将该请求者id设置到连接通道的属性对象上.
         * 这个设置算是面向用户的设置,和框架无关,仅和用户业务有关.
         */
        BaseSessionContext sessionContext = (BaseSessionContext) channelContext.getAttribute();
        sessionContext.setUserId(userId);
        sessionContext.setToken(token);

        Boolean send = Aio.send(channelContext, PacketFactory.loginResponsePacket(Const.RequestCode.SUCCESS, token));

        // 用户登陆处理
        UserManagementHandler.userLoginHandle(loginRequestBody, channelContext);
        /*
         * 组成登陆的响应包, 发送回登陆的请求方.
         */
        return send;
    }

    public static void logoutRequestHandle(String bodyJsonStr, ChannelContext channelContext) {
        LogoutRequestBody logoutRequestBody = Json.toBean(bodyJsonStr, LogoutRequestBody.class);
        BaseSessionContext sessionContext = (BaseSessionContext) channelContext.getAttribute();
        if (verifySessionAndMsg(logoutRequestBody.getLogoutUserId(), logoutRequestBody.getToken(), sessionContext)) {
            if (Server.isServerHandlerLogEnabled()) {
                SERVER_LOG.info("收到登出请求: userId:{}.", sessionContext.getUserId());
            }
            sessionContext.setUserId(null);
            sessionContext.setToken(null);
            Aio.unbindUser(channelContext);
            Aio.unbindToken(channelContext);
            UserManagementHandler.userLogoutHandle(logoutRequestBody, channelContext);
        } else {
            differenceBetweenMsgUserIdAndSessionUserId(channelContext);
        }
    }

    public static Boolean p2PMsgRequestHandle(String bodyJsonStr, ChannelContext channelContext) {
        P2PRequestBody p2PRequestBody = Json.toBean(bodyJsonStr, P2PRequestBody.class);
        BaseSessionContext sessionContext = (BaseSessionContext) channelContext.getAttribute();
        if (verifySessionAndMsg(p2PRequestBody.getFromUserId(), p2PRequestBody.getToken(), sessionContext)) {
            if (Server.isServerHandlerLogEnabled()) {
                SERVER_LOG.info("收到P2P请求: fromUserId:{}, toUserId:{}, msg:{}.",
                        p2PRequestBody.getFromUserId(),
                        p2PRequestBody.getToUserId(),
                        p2PRequestBody.getMsg());
            }
            Boolean send = Aio.sendToUser(channelContext.getGroupContext(), p2PRequestBody.getToUserId(),
                    PacketFactory.p2PMsgResponsePacket(p2PRequestBody.getMsg(), sessionContext.getUserId()));
            UserManagementHandler.p2pMsgHandle(p2PRequestBody, channelContext, send);
            return send;
        } else {
            differenceBetweenMsgUserIdAndSessionUserId(channelContext);
            return false;
        }
    }

    public static void p2GRequestHandle(String bodyJsonStr, ChannelContext channelContext){
        P2GRequestBody p2GRequestBody = Json.toBean(bodyJsonStr, P2GRequestBody.class);
        BaseSessionContext sessionContext = (BaseSessionContext) channelContext.getAttribute();
        if (verifySessionAndMsg(p2GRequestBody.getFromUserId(), p2GRequestBody.getToken(), sessionContext)) {
            if (Server.isServerHandlerLogEnabled()) {
                SERVER_LOG.info("收到群组消息请求: fromUserId:{}, toGroup:{}, msg:{}.",
                        p2GRequestBody.getFromUserId(),
                        p2GRequestBody.getToGroup(),
                        p2GRequestBody.getMsg());
            }
            Aio.sendToGroup(channelContext.getGroupContext(), p2GRequestBody.getToGroup(),
                    PacketFactory.p2gResponsePacket(p2GRequestBody.getMsg(), sessionContext.getUserId(), p2GRequestBody.getToGroup()));
            UserManagementHandler.groupMsgHandle(p2GRequestBody, channelContext);
        } else {
            differenceBetweenMsgUserIdAndSessionUserId(channelContext);
        }
    }

    public static Boolean addFriendRequestHandle(String bodyJsonStr, ChannelContext channelContext) {
        AddFriendRequestBody addFriendRequestBody = Json.toBean(bodyJsonStr, AddFriendRequestBody.class);
        BaseSessionContext sessionContext = (BaseSessionContext) channelContext.getAttribute();
        if (verifySessionAndMsg(addFriendRequestBody.getFromUserId(), addFriendRequestBody.getToken(), sessionContext)) {
            if (Server.isServerHandlerLogEnabled()) {
                SERVER_LOG.info("收到加好友请求: fromUserId:{}, toUserId:{}.",
                        addFriendRequestBody.getFromUserId(),
                        addFriendRequestBody.getToUserId());
            }
            UserManagementHandler.addFriendRequestHandle(addFriendRequestBody, channelContext);
        }
        return null;
    }

    public static Boolean joinGroupRequestHandle(String bodyJsonStr, ChannelContext channelContext) {
        JoinGroupRequestBody joinGroupRequestBody = Json.toBean(bodyJsonStr, JoinGroupRequestBody.class);
        BaseSessionContext sessionContext = (BaseSessionContext) channelContext.getAttribute();
        String fromUserId = joinGroupRequestBody.getFromUserId();
        if (verifySessionAndMsg(fromUserId, joinGroupRequestBody.getToken(), sessionContext)) {
            String groupId = joinGroupRequestBody.getGroupId();
            if (!Server.isUserInGroup(joinGroupRequestBody.getFromUserId(), groupId)) {
                if (Server.isServerHandlerLogEnabled()) {
                    SERVER_LOG.info("收到有效加群请求: fromUserId:{}, toGroup:{}.",
                            fromUserId,
                            groupId);
                }
                return UserManagementHandler.joinGroupRequestHandle(joinGroupRequestBody, channelContext);
            } else {
                if (Server.isServerHandlerLogEnabled()) {
                    SERVER_LOG.info("收到无效加群请求: fromUserId:{}, toGroup:{}.",
                            fromUserId,
                            groupId);
                }
            }
        } else {
            differenceBetweenMsgUserIdAndSessionUserId(channelContext);
        }
        return false;
    }

    public static void quitGroupRequestHandle(String bodyJsonStr, ChannelContext channelContext) throws NoneffectiveUpdateExecuteException {
        QuitGroupRequestBody quitGroupRequestBody = Json.toBean(bodyJsonStr, QuitGroupRequestBody.class);
        BaseSessionContext sessionContext = (BaseSessionContext) channelContext.getAttribute();
        if (verifySessionAndMsg(quitGroupRequestBody.getFromUserId(), quitGroupRequestBody.getToken(), sessionContext)) {
            if (Server.isUserInGroup(quitGroupRequestBody.getFromUserId(), quitGroupRequestBody.getGroupId())) {
                if (Server.isServerHandlerLogEnabled()) {
                    SERVER_LOG.info("收到有效的退群请求: fromUserId:{}, toGroup:{}.",
                            quitGroupRequestBody.getFromUserId(),
                            quitGroupRequestBody.getGroupId());
                }
                Aio.unbindGroup(quitGroupRequestBody.getGroupId(), channelContext);
                UserManagementHandler.quitGroupRequestHandle(quitGroupRequestBody, channelContext);
            } else {
                if (Server.isServerHandlerLogEnabled()) {
                    SERVER_LOG.info("收到无效的退群请求: fromUserId:{}, toGroup:{}.",
                            quitGroupRequestBody.getFromUserId(),
                            quitGroupRequestBody.getGroupId());
                }
            }
        } else {
            differenceBetweenMsgUserIdAndSessionUserId(channelContext);
        }
    }

    public static void removeFriendRequestHandle(String bodyJsonStr, ChannelContext channelContext) throws NoneffectiveUpdateExecuteException {
        RemoveFriendRequestBody removeFriendRequestBody = Json.toBean(bodyJsonStr, RemoveFriendRequestBody.class);
        BaseSessionContext sessionContext = (BaseSessionContext) channelContext.getAttribute();
        if (verifySessionAndMsg(removeFriendRequestBody.getFromUserId(), removeFriendRequestBody.getToken(), sessionContext)) {
            if (Server.isServerHandlerLogEnabled()) {
                SERVER_LOG.info("收到删除好友请求: fromUserId:{}, toUserId:{}.",
                        removeFriendRequestBody.getFromUserId(),
                        removeFriendRequestBody.getToUserId());
            }
            UserManagementHandler.removeFriendRequestHandle(removeFriendRequestBody, channelContext);
        } else {
            differenceBetweenMsgUserIdAndSessionUserId(channelContext);
        }
    }

    public static void kickMemberRequestHandle(String bodyJsonStr, ChannelContext channelContext) throws NoneffectiveUpdateExecuteException {
        KickMemberRequestBody kickMemberRequestBody = Json.toBean(bodyJsonStr, KickMemberRequestBody.class);
        BaseSessionContext sessionContext = (BaseSessionContext) channelContext.getAttribute();
        if (verifySessionAndMsg(kickMemberRequestBody.getFromAdministratorId(), kickMemberRequestBody.getToken(), sessionContext)) {
            if (Server.isServerHandlerLogEnabled()) {
                SERVER_LOG.info("收到踢出用户的请求: administratorId:{}, toUserId:{}, groupId:{}.",
                        kickMemberRequestBody.getFromAdministratorId(),
                        kickMemberRequestBody.getToUserId(),
                        kickMemberRequestBody.getFromGroup());
            }
            UserManagementHandler.kickMemberRequestHandle(kickMemberRequestBody, channelContext);
        } else {
            differenceBetweenMsgUserIdAndSessionUserId(channelContext);
        }
    }

    public static void addAdminRequestHandle(String bodyJsonStr, ChannelContext channelContext) throws NoneffectiveUpdateExecuteException {
        AddAdminRequestBody addAdminRequestBody = Json.toBean(bodyJsonStr, AddAdminRequestBody.class);
        BaseSessionContext sessionContext = (BaseSessionContext) channelContext.getAttribute();
        if (verifySessionAndMsg(addAdminRequestBody.getOwnerId(), addAdminRequestBody.getToken(), sessionContext)) {
            if (Server.isServerHandlerLogEnabled()) {
                SERVER_LOG.info("收到添加管理员请求: groupId:{}, ownerId:{}, toUserId:{}.",
                        addAdminRequestBody.getGroupId(),
                        addAdminRequestBody.getOwnerId(),
                        addAdminRequestBody.getToUserId());
            }
            UserManagementHandler.addAdminRequestHandle(addAdminRequestBody, channelContext);
        } else {
            differenceBetweenMsgUserIdAndSessionUserId(channelContext);
        }
    }

    public static void removeAdminRequestHandle(String bodyJsonStr, ChannelContext channelContext) throws NoneffectiveUpdateExecuteException {
        RemoveAdminRequestBody removeAdminRequestBody = Json.toBean(bodyJsonStr, RemoveAdminRequestBody.class);
        BaseSessionContext sessionContext = (BaseSessionContext) channelContext.getAttribute();
        if (verifySessionAndMsg(removeAdminRequestBody.getOwnerId(), removeAdminRequestBody.getToken(), sessionContext)) {
            if (Server.isServerHandlerLogEnabled()) {
                SERVER_LOG.info("收到移除管理员权限请求: groupId:{}, ownerId:{}, fromUserId:{}.",
                        removeAdminRequestBody.getGroupId(),
                        removeAdminRequestBody.getOwnerId(),
                        removeAdminRequestBody.getFromUserId());
            }
            UserManagementHandler.removeAdminRequestHandle(removeAdminRequestBody, channelContext);
        } else {
            differenceBetweenMsgUserIdAndSessionUserId(channelContext);
        }
    }

    public static void dissolveGroupRequestHandle(String bodyJsonStr, ChannelContext channelContext) throws NoneffectiveUpdateExecuteException {
        DissolveGroupRequestBody groupDissolveRequestBody = Json.toBean(bodyJsonStr, DissolveGroupRequestBody.class);
        BaseSessionContext sessionContext = (BaseSessionContext) channelContext.getAttribute();
        if (verifySessionAndMsg(groupDissolveRequestBody.getOwnerId(), groupDissolveRequestBody.getToken(), sessionContext)) {
            if (Server.isServerHandlerLogEnabled()) {
                SERVER_LOG.info("收到解散群组请求: groupId:{}, ownerId:{}.",
                        groupDissolveRequestBody.getGroupId(),
                        groupDissolveRequestBody.getOwnerId());
            }
            UserManagementHandler.dissolveGroupRequestHandle(groupDissolveRequestBody, channelContext);
        } else {
            differenceBetweenMsgUserIdAndSessionUserId(channelContext);
        }
    }

    public static void addFriendConfirmMsgHandle(String bodyJsonStr, ChannelContext channelContext) throws NoneffectiveUpdateExecuteException {
        AddFriendConfirmMsgBody addFriendConfirmMsgBody = Json.toBean(bodyJsonStr, AddFriendConfirmMsgBody.class);
        BaseSessionContext sessionContext = (BaseSessionContext) channelContext.getAttribute();
        if (verifySessionAndMsg(addFriendConfirmMsgBody.getFromUserId(), addFriendConfirmMsgBody.getToken(), sessionContext)) {
            if (Server.isServerHandlerLogEnabled()) {
                SERVER_LOG.info("收到返回添加好友确认信息的请求: fromUserId:{}, toUserId:{}," +
                                " confirmResult:{}.",
                        addFriendConfirmMsgBody.getFromUserId(),
                        addFriendConfirmMsgBody.getToUserId(),
                        addFriendConfirmMsgBody.isConfirmResult());
            }
            UserManagementHandler.addFriendConfirmMsgHandle(addFriendConfirmMsgBody, channelContext);
        } else {
            differenceBetweenMsgUserIdAndSessionUserId(channelContext);
        }
    }

    public static void joinGroupConfirmMsgHandle(String bodyJsonStr, ChannelContext channelContext) throws NoneffectiveUpdateExecuteException {
        JoinGroupConfirmMsgBody joinGroupConfirmMsgBody = Json.toBean(bodyJsonStr, JoinGroupConfirmMsgBody.class);
        BaseSessionContext sessionContext = (BaseSessionContext) channelContext.getAttribute();
        if (verifySessionAndMsg(joinGroupConfirmMsgBody.getHandleAdministratorId(), joinGroupConfirmMsgBody.getToken(), sessionContext)) {
            if (Server.isServerHandlerLogEnabled()) {
                SERVER_LOG.info("收到返回的申请入群确认信息的请求: groupId:{}, handleAdministratorId:{}," +
                                " toUserId:{}, confirmResult:{}.",
                        joinGroupConfirmMsgBody.getGroupId(),
                        joinGroupConfirmMsgBody.getHandleAdministratorId(),
                        joinGroupConfirmMsgBody.getToUserId(),
                        joinGroupConfirmMsgBody.isConfirmResult());
            }
            UserManagementHandler.joinGroupConfirmMsgHandle(joinGroupConfirmMsgBody, channelContext);
        } else {
            differenceBetweenMsgUserIdAndSessionUserId(channelContext);
        }
    }

    public static void signInGroupRequestHandle(String bodyJsonStr, ChannelContext channelContext) throws NoneffectiveUpdateExecuteException {
        SignInGroupRequestBody signInGroupRequestBody = Json.toBean(bodyJsonStr, SignInGroupRequestBody.class);
        BaseSessionContext sessionContext = (BaseSessionContext) channelContext.getAttribute();
        if (verifySessionAndMsg(signInGroupRequestBody.getOwnerId(), signInGroupRequestBody.getToken(), sessionContext)) {
            if  (Server.isServerHandlerLogEnabled()) {
                SERVER_LOG.info("收到创建群组关系的申请: groupId: {}, ownerId:{}" ,
                        signInGroupRequestBody.getGroupId(),
                        signInGroupRequestBody.getOwnerId());
            }
            UserManagementHandler.signInGroupRequestBody(signInGroupRequestBody, channelContext);
        } else {
            differenceBetweenMsgUserIdAndSessionUserId(channelContext);
        }
    }

    public static void signInUserRequestHandle(String bodyJsonStr, ChannelContext channelContext) throws NoneffectiveUpdateExecuteException {
        SignInUserRequestBody signInUserRequestBody = Json.toBean(bodyJsonStr, SignInUserRequestBody.class);
        UserManagementHandler.signInUserRequestBody(signInUserRequestBody, channelContext);
    }

    private static void differenceBetweenMsgUserIdAndSessionUserId(ChannelContext channelContext) {
        if (Server.isServerHandlerLogEnabled()) {
            SERVER_LOG.error("通信方绑定ID和请求信息中的请求方ID不一致,无法完成请求!");
        }
        Aio.send(channelContext,
                PacketFactory.systemMsgToOnePacket("通信方绑定ID和请求信息中的请求方ID不一致,无法完成请求!"));
    }

    private static boolean verifySessionAndMsg(String msgSenderUserId, String token, BaseSessionContext sessionContext) {
        return (msgSenderUserId != null && msgSenderUserId.equals(sessionContext.getUserId())) &&
                (token != null && token.equals(sessionContext.getToken()));
    }

    public static Boolean heartbeatRequestHandler(String bodyJsonStr, ChannelContext channelContext) {
        System.err.println("Heartbeat from:"+channelContext);
        return null;
    }
}
