package com.github.youyinnn.server.service;

import com.github.youyinnn.server.dao.GroupRelationDao;
import com.github.youyinnn.server.dao.UserRelationDao;
import com.github.youyinnn.youdbutils.exceptions.NoneffectiveUpdateExecuteException;
import com.github.youyinnn.youdbutils.ioc.annotations.Autowired;
import com.github.youyinnn.youdbutils.ioc.annotations.YouService;

import java.util.HashSet;

/**
 * @author youyinnn
 */
@YouService(dataSourceName = "im")
public class ServerService {

    @Autowired
    private UserRelationDao userRelationDao;

    @Autowired
    private GroupRelationDao groupRelationDao;

    public boolean addUserRelation(String userId) throws NoneffectiveUpdateExecuteException {
        return userRelationDao.addUserRelation(userId);
    }

    /**
     * 加好友
     * @param aId
     * @param bId
     * @return
     * @throws NoneffectiveUpdateExecuteException
     */
    public boolean addFriend(String aId, String bId) throws NoneffectiveUpdateExecuteException {
        return userRelationDao.addFriend(aId, bId);
    }

    /**
     * 删除好友
     * @param aId
     * @param bId
     * @return
     * @throws NoneffectiveUpdateExecuteException
     */
    public boolean removeFriend(String aId, String bId) throws NoneffectiveUpdateExecuteException {
        return userRelationDao.removeFriend(aId, bId);
    }

    /**
     * 获取用户的好友id列表
     * @param userId
     * @return
     */
    public HashSet<String> getUserFriendSet(String userId) {
        return userRelationDao.getFriendSet(userId);
    }

    /**
     * 获取用户已加入的群组
     * @param userId
     * @return
     */
    public HashSet<String> getJoinGroupSet(String userId) {
        return userRelationDao.getGroupSet(userId);
    }

    /**
     * 用户退出群组
     * @param userId
     * @param groupId
     * @return
     * @throws NoneffectiveUpdateExecuteException
     */
    public boolean quitFromGroup(String userId, String groupId) throws NoneffectiveUpdateExecuteException {
        return userRelationDao.quitGroup(userId, groupId)
                && groupRelationDao.removeUserFromGroup(groupId, userId);
    }

    /**
     * 添加群组关系
     * @param groupId
     * @param ownerUserId
     * @return
     * @throws NoneffectiveUpdateExecuteException
     */
    public boolean addGroupRelation(String groupId, String ownerUserId) throws NoneffectiveUpdateExecuteException {
        return userRelationDao.joinGroup(ownerUserId, groupId)
                && groupRelationDao.addGroupRelation(groupId, ownerUserId);
    }

    /**
     * 获取群成员id
     * @param groupId
     * @return
     */
    public HashSet<String> getMemberIds(String groupId) {
        return groupRelationDao.getGroupMemberIds(groupId);
    }

    /**
     * 获取群管理员id
     * @param groupId
     * @return
     */
    public HashSet<String> getAdministratorIds(String groupId) {
        return groupRelationDao.getAdministratorIds(groupId);
    }

    /**
     * 添加用户到群成员
     * @param groupId
     * @param opUserId
     * @param userId
     * @return
     * @throws NoneffectiveUpdateExecuteException
     */
    public boolean addUserToGroup(String groupId, String opUserId,  String userId) throws NoneffectiveUpdateExecuteException {
        return userRelationDao.isUserRelationExist(userId)
                && groupRelationDao.isUserAdminTheGroup(opUserId, groupId)
                && groupRelationDao.addUserToGroup(groupId, userId)
                && userRelationDao.joinGroup(userId, groupId);
    }

    /**
     * 把用户升级为管理员
     * @param groupId
     * @param ownerUserId
     * @param userId
     * @return
     * @throws NoneffectiveUpdateExecuteException
     */
    public boolean addUserToAdmin(String groupId, String ownerUserId, String userId) throws NoneffectiveUpdateExecuteException {
        return groupRelationDao.isUserOwnedTheGroup(ownerUserId, groupId)
                && groupRelationDao.isUserInGroup(groupId, userId)
                && groupRelationDao.addUserToAdmin(groupId, userId);
    }

    /**
     * 踢用户出群
     *  群主可以踢管理和成员
     *  管理可以踢成员 不可以踢群主和其他管理
     * @param groupId
     * @param opUserId
     * @param userId
     * @return
     * @throws NoneffectiveUpdateExecuteException
     */
    public boolean kickUserOutOfGroup(String groupId, String opUserId, String userId) throws NoneffectiveUpdateExecuteException {
        if (groupRelationDao.isUserAdminTheGroup(userId, groupId)) {
            return groupRelationDao.isUserOwnedTheGroup(opUserId, groupId)
                    && groupRelationDao.removeUserFromAdmin(groupId, userId)
                    && groupRelationDao.removeUserFromGroup(groupId, userId)
                    && userRelationDao.quitGroup(userId, groupId);
        } else {
            return groupRelationDao.isUserAdminTheGroup(opUserId, groupId)
                    && groupRelationDao.removeUserFromGroup(groupId, userId)
                    && userRelationDao.quitGroup(userId, groupId);
        }
    }

    /**
     * 移除用户管理权限
     * @param groupId
     * @param ownerUserId
     * @param userId
     * @return
     * @throws NoneffectiveUpdateExecuteException
     */
    public boolean removeUserFromAdmin(String groupId, String ownerUserId, String userId) throws NoneffectiveUpdateExecuteException {
        return !ownerUserId.equals(userId)
                && groupRelationDao.isUserOwnedTheGroup(ownerUserId, groupId)
                && groupRelationDao.removeUserFromAdmin(groupId, userId);
    }

    /**
     * 解散群组
     * @param groupId
     * @param ownerUserId
     * @return
     * @throws NoneffectiveUpdateExecuteException
     */
    public boolean dissolveTheGroup(String groupId, String ownerUserId) throws NoneffectiveUpdateExecuteException {
        HashSet<String> groupMemberIds = groupRelationDao.getGroupMemberIds(groupId);
        if (groupMemberIds != null) {
            for (String groupMemberId : groupMemberIds) {
                userRelationDao.quitGroup(groupMemberId, groupId);
            }
            return groupRelationDao.dissolveTheGroup(groupId, ownerUserId);
        } else {
            return false;
        }
    }

}
