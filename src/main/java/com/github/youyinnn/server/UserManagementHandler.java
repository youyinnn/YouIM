package com.github.youyinnn.server;

import com.github.youyinnn.common.packets.confirm.AddFriendConfirmMsgBody;
import com.github.youyinnn.common.packets.confirm.JoinGroupConfirmMsgBody;
import com.github.youyinnn.common.packets.request.*;
import com.github.youyinnn.common.utils.PacketFactory;
import com.github.youyinnn.server.service.ServerService;
import com.github.youyinnn.youdbutils.exceptions.AutowiredException;
import com.github.youyinnn.youdbutils.exceptions.NoneffectiveUpdateExecuteException;
import com.github.youyinnn.youdbutils.ioc.YouServiceIocContainer;
import com.github.youyinnn.youwebutils.third.Log4j2Helper;
import org.apache.logging.log4j.Logger;
import org.tio.core.Aio;
import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
import org.tio.core.intf.Packet;

import java.util.HashSet;

/**
 * @author youyinnn
 */
public class UserManagementHandler {

    private static ServerService service;

    private static final Logger SERVER_LOG = Log4j2Helper.getLogger("$im_server");

    static {
        if (Server.getServerConfig().isUserManagementModuleEnabled()) {
            try {
                service = (ServerService) YouServiceIocContainer.getYouService(ServerService.class);
            } catch (AutowiredException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isUserExist(String userId) {
        return service.isUserExist(userId);
    }

    public static void userLoginHandle(LoginRequestBody loginRequestBody, ChannelContext channelContext) {
        if (service != null) {
            GroupContext groupContext = channelContext.getGroupContext();
            String userId = loginRequestBody.getLoginUserId();
            HashSet<String> joinGroupSet = service.getJoinGroupSet(userId);
            // 恢复群组关系 通知群组成员该用户上线
            for (String groupId : joinGroupSet) {
                Aio.bindGroup(channelContext, groupId);
                Aio.sendToGroup(groupContext, groupId, PacketFactory.memberOnlineInformPacket(userId, groupId));
            }
            // 通知好友该用户上线
            HashSet<String> userFriendSet = service.getUserFriendSet(userId);
            for (String friendId : userFriendSet) {
                if (Server.isLogin(friendId)) {
                    Aio.sendToUser(groupContext, friendId, PacketFactory.friendOnlineInformPacket(userId));
                }
            }
        }
    }

    public static void userLogoutHandle(LogoutRequestBody logoutRequestBody, ChannelContext channelContext) {
        if (service != null) {
            GroupContext groupContext = channelContext.getGroupContext();
            String userId = logoutRequestBody.getLogoutUserId();
            HashSet<String> joinGroupSet = service.getJoinGroupSet(userId);
            for (String groupId : joinGroupSet) {
                Aio.sendToGroup(groupContext, groupId, PacketFactory.memberOfflineInformPacket(userId, groupId));
                Aio.unbindGroup(groupId, channelContext);
            }
            HashSet<String> userFriendSet = service.getUserFriendSet(userId);
            for (String friendId : userFriendSet) {
                if (Server.isLogin(friendId)) {
                    Aio.sendToUser(groupContext, friendId, PacketFactory.friendOfflineInformPacket(userId));
                }
            }
        }
    }

    public static void p2pMsgHandle(P2PRequestBody p2PRequestBody, ChannelContext channelContext, Boolean send) {
        if (service != null) {
            // TODO: p2p消息记录
        }
    }

    public static void groupMsgHandle(P2GRequestBody p2GRequestBody, ChannelContext channelContext) {
        if (service != null) {
            // TODO: groupMsg消息记录
        }
    }


    public static void addFriendRequestHandle(AddFriendRequestBody addFriendRequestBody, ChannelContext channelContext) {
        if (service != null) {
            String toUserId = addFriendRequestBody.getToUserId();
            String fromUserId = addFriendRequestBody.getFromUserId();
            GroupContext groupContext = channelContext.getGroupContext();
            Boolean send = Aio.sendToUser(groupContext, toUserId, PacketFactory.addFriendConfirmInformPacket(fromUserId));
            // TODO: 记录消息
        }
    }

    public static boolean joinGroupRequestHandle(JoinGroupRequestBody joinGroupRequestBody, ChannelContext channelContext) {
        String groupId = joinGroupRequestBody.getGroupId();
        if (service != null) {
            String fromUserId = joinGroupRequestBody.getFromUserId();
            GroupContext groupContext = channelContext.getGroupContext();
            HashSet<String> administratorIds = service.getAdministratorIds(groupId);
            Boolean send = false;
            for (String administratorId : administratorIds) {
                send = Aio.sendToUser(groupContext, administratorId,
                        PacketFactory.joinGroupConfirmInformPacket(groupId, fromUserId));
                // TODO: 记录消息
            }
            return send;
        } else {
            Aio.bindGroup(channelContext, groupId);
            return Aio.send(channelContext,
                    PacketFactory.joinGroupResponsePacket(true,"无用户管理模块, 无条件进入群组.", groupId));
        }
    }

    public static void quitGroupRequestHandle(QuitGroupRequestBody quitGroupRequestBody, ChannelContext channelContext) throws NoneffectiveUpdateExecuteException {
        if (service != null) {
            String fromUserId = quitGroupRequestBody.getFromUserId();
            String groupId = quitGroupRequestBody.getGroupId();
            GroupContext groupContext = channelContext.getGroupContext();
            service.quitFromGroup(fromUserId, groupId);
            Aio.sendToGroup(groupContext, groupId,
                    PacketFactory.quitGroupInformPacket(fromUserId, groupId));
            // TODO: 消息记录
        }
    }

    public static void removeFriendRequestHandle(RemoveFriendRequestBody removeFriendRequestBody, ChannelContext channelContext) throws NoneffectiveUpdateExecuteException {
        if (service != null) {
            String fromUserId = removeFriendRequestBody.getFromUserId();
            String toUserId = removeFriendRequestBody.getToUserId();
            service.removeFriend(fromUserId, toUserId);
            Aio.sendToUser(channelContext.getGroupContext(), toUserId,
                    PacketFactory.removeFriendInformPacket(fromUserId, toUserId));
            // TODO: 消息记录
        }
    }


    public static void kickMemberRequestHandle(KickMemberRequestBody kickMemberRequestBody, ChannelContext channelContext) throws NoneffectiveUpdateExecuteException {
        if (service != null) {
            String fromAdministratorUserId = kickMemberRequestBody.getFromAdministratorId();
            String toUserId = kickMemberRequestBody.getToUserId();
            String fromGroup = kickMemberRequestBody.getFromGroup();
            boolean kick = service.kickUserOutOfGroup(fromGroup, fromAdministratorUserId, toUserId);
            if (kick) {
                ChannelContext toUserChannelContext = Server.getChannelContext(toUserId);
                if (toUserChannelContext != null) {
                    Aio.unbindGroup(fromGroup, toUserChannelContext);
                }
                Packet kickMemberInformPacket = PacketFactory.kickMemberInformPacket(fromAdministratorUserId, toUserId, fromGroup);
                Boolean send = Aio.sendToUser(channelContext.getGroupContext(), toUserId,
                        kickMemberInformPacket);
                Aio.sendToGroup(channelContext.getGroupContext(), fromGroup, kickMemberInformPacket);
                // TODO: 消息记录
            } else {

            }
        }
    }

    public static void addAdminRequestHandle(AddAdminRequestBody addAdminRequestBody, ChannelContext channelContext) throws NoneffectiveUpdateExecuteException {
        if (service != null) {
            String groupId = addAdminRequestBody.getGroupId();
            String ownerId = addAdminRequestBody.getOwnerId();
            String toUserId = addAdminRequestBody.getToUserId();
            GroupContext groupContext = channelContext.getGroupContext();
            boolean addUserToAdmin = service.addUserToAdmin(groupId, ownerId, toUserId);
            if (addUserToAdmin) {
                Aio.sendToUser(groupContext, toUserId, PacketFactory.addAdminInformPacket(ownerId, toUserId, groupId));
                // TODO: 消息记录
            } else {

            }
        }
    }


    public static void removeAdminRequestHandle(RemoveAdminRequestBody removeAdminRequestBody, ChannelContext channelContext) throws NoneffectiveUpdateExecuteException {
        if (service != null) {
            String ownerId = removeAdminRequestBody.getOwnerId();
            String groupId = removeAdminRequestBody.getGroupId();
            String fromUserId = removeAdminRequestBody.getFromUserId();
            GroupContext groupContext = channelContext.getGroupContext();
            boolean removeUserFromAdmin = service.removeUserFromAdmin(groupId, ownerId, fromUserId);
            if (removeUserFromAdmin) {
                Boolean send = Aio.sendToUser(groupContext, fromUserId,
                        PacketFactory.removeAdminInformPacket(ownerId, groupId, fromUserId));
                // TODO: message record
            }
        }
    }

    public static void dissolveGroupRequestHandle(DissolveGroupRequestBody groupDissolveRequestBody, ChannelContext channelContext) throws NoneffectiveUpdateExecuteException {
        if (service != null) {
            String groupId = groupDissolveRequestBody.getGroupId();
            String ownerId = groupDissolveRequestBody.getOwnerId();
            GroupContext groupContext = channelContext.getGroupContext();
            HashSet<String> memberIds = service.getMemberIds(groupId);
            boolean dissolveTheGroup = service.dissolveTheGroup(groupId, ownerId);
            if (dissolveTheGroup) {
                Packet dissolveGroupInformPacket = PacketFactory.dissolveGroupInformPacket(groupId);
                for (String memberId : memberIds) {
                    boolean login = Server.isLogin(memberId);
                    Boolean send = false;
                    if (login) {
                        send = Aio.sendToUser(groupContext, memberId, dissolveGroupInformPacket);
                    }
                }
            }
        }
    }

    public static void addFriendConfirmMsgHandle(AddFriendConfirmMsgBody addFriendConfirmMsgBody, ChannelContext channelContext) throws NoneffectiveUpdateExecuteException {
        if (service != null) {
            String fromUserId = addFriendConfirmMsgBody.getFromUserId();
            String toUserId = addFriendConfirmMsgBody.getToUserId();
            boolean confirmResult = addFriendConfirmMsgBody.isConfirmResult();
            if (confirmResult) {
                service.addFriend(fromUserId, toUserId);
            }
            boolean login = Server.isLogin(toUserId);
            if (login) {
                Aio.sendToUser(channelContext.getGroupContext(), toUserId,
                        PacketFactory.addFriendResponsePacket(fromUserId, toUserId, confirmResult));
            }
        }
    }

    public static void joinGroupConfirmMsgHandle(JoinGroupConfirmMsgBody joinGroupConfirmMsgBody, ChannelContext channelContext) throws NoneffectiveUpdateExecuteException {
        if (service != null) {
            String groupId = joinGroupConfirmMsgBody.getGroupId();
            String handleAdministratorId = joinGroupConfirmMsgBody.getHandleAdministratorId();
            String toUserId = joinGroupConfirmMsgBody.getToUserId();
            boolean confirmResult = joinGroupConfirmMsgBody.isConfirmResult();
            boolean login = Server.isLogin(toUserId);
            Packet joinGroupResponsePacket = PacketFactory.joinGroupResponsePacket(confirmResult, groupId, toUserId);
            if (confirmResult) {
                Aio.sendToGroup(channelContext.getGroupContext(), groupId, joinGroupResponsePacket);
                ChannelContext newMemberChannelContext = Server.getChannelContext(toUserId);
                if (newMemberChannelContext != null) {
                    Aio.bindGroup(newMemberChannelContext, groupId);
                }
                service.addUserToGroup(groupId, handleAdministratorId, toUserId);
            }
            if (login) {
                Aio.sendToUser(channelContext.getGroupContext(), toUserId,
                        joinGroupResponsePacket);
            }
        }
    }

    public static void signInGroupRequestBody(SignInGroupRequestBody signInGroupRequestBody, ChannelContext channelContext) throws NoneffectiveUpdateExecuteException {
        if (service != null) {
            service.addGroupRelation(signInGroupRequestBody.getGroupId(),
                    signInGroupRequestBody.getOwnerId());
        }
    }

    public static void signInUserRequestBody(SignInUserRequestBody signInUserRequestBody, ChannelContext channelContext) throws NoneffectiveUpdateExecuteException {
        if (service != null) {
            service.addUserRelation(signInUserRequestBody.getUserId());
        }
    }
}
