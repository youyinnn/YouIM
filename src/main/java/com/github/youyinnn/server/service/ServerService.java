package com.github.youyinnn.server.service;

import com.github.youyinnn.server.dao.GroupRelationDao;
import com.github.youyinnn.server.dao.UserRelationDao;
import com.github.youyinnn.youdbutils.exceptions.NoneffectiveUpdateExecuteException;
import com.github.youyinnn.youdbutils.ioc.annotations.Autowired;
import com.github.youyinnn.youdbutils.ioc.annotations.YouService;

import java.util.HashSet;

/**
 * The type Server service.
 *
 * @author youyinnn
 */
@YouService(dataSourceName = "im")
public class ServerService {

    @Autowired
    private UserRelationDao userRelationDao;

    @Autowired
    private GroupRelationDao groupRelationDao;

    /**
     * 添加好友关系
     *
     * @param userId the user id
     * @return the boolean
     * @throws NoneffectiveUpdateExecuteException the noneffective update execute exception
     */
    public boolean addUserRelation(String userId) throws NoneffectiveUpdateExecuteException {
        return userRelationDao.addUserRelation(userId);
    }

    /**
     * 加好友
     *
     * @param aId the a id
     * @param bId the b id
     * @return boolean
     * @throws NoneffectiveUpdateExecuteException the noneffective update execute exception
     */
    public boolean addFriend(String aId, String bId) throws NoneffectiveUpdateExecuteException {
        return userRelationDao.addFriend(aId, bId);
    }

    /**
     * 删除好友
     *
     * @param aId the a id
     * @param bId the b id
     * @return boolean
     * @throws NoneffectiveUpdateExecuteException the noneffective update execute exception
     */
    public boolean removeFriend(String aId, String bId) throws NoneffectiveUpdateExecuteException {
        return userRelationDao.removeFriend(aId, bId);
    }

    /**
     * 获取用户的好友id列表
     *
     * @param userId the user id
     * @return user friend set
     */
    public HashSet<String> getUserFriendSet(String userId) {
        return userRelationDao.getFriendSet(userId);
    }

    /**
     * 获取用户已加入的群组
     *
     * @param userId the user id
     * @return join group set
     */
    public HashSet<String> getJoinGroupSet(String userId) {
        return userRelationDao.getGroupSet(userId);
    }

    /**
     * 用户退出群组
     *
     * @param userId  the user id
     * @param groupId the group id
     * @return boolean
     * @throws NoneffectiveUpdateExecuteException the noneffective update execute exception
     */
    public boolean quitFromGroup(String userId, String groupId) throws NoneffectiveUpdateExecuteException {
        return userRelationDao.quitGroup(userId, groupId)
                && groupRelationDao.removeUserFromGroup(groupId, userId);
    }

    /**
     * 添加群组关系
     *
     * @param groupId the group id
     * @param ownerId the owner id
     * @return boolean
     * @throws NoneffectiveUpdateExecuteException the noneffective update execute exception
     */
    public boolean addGroupRelation(String groupId, String ownerId) throws NoneffectiveUpdateExecuteException {
        return userRelationDao.joinGroup(ownerId, groupId)
                && groupRelationDao.addGroupRelation(groupId, ownerId);
    }

    /**
     * 获取群成员id
     *
     * @param groupId the group id
     * @return member ids
     */
    public HashSet<String> getMemberIds(String groupId) {
        return groupRelationDao.getGroupMemberIds(groupId);
    }

    /**
     * 获取群管理员id
     *
     * @param groupId the group id
     * @return administrator ids
     */
    public HashSet<String> getAdministratorIds(String groupId) {
        return groupRelationDao.getAdministratorIds(groupId);
    }

    /**
     * 添加用户到群成员
     *
     * @param groupId  the group id
     * @param opUserId the op user id
     * @param userId   the user id
     * @return boolean
     * @throws NoneffectiveUpdateExecuteException the noneffective update execute exception
     */
    public boolean addUserToGroup(String groupId, String opUserId,  String userId) throws NoneffectiveUpdateExecuteException {
        return userRelationDao.isUserRelationExist(userId)
                && groupRelationDao.isUserAdminTheGroup(opUserId, groupId)
                && groupRelationDao.addUserToGroup(groupId, userId)
                && userRelationDao.joinGroup(userId, groupId);
    }

    /**
     * 把用户升级为管理员
     *
     * @param groupId the group id
     * @param ownerId the owner id
     * @param userId  the user id
     * @return boolean
     * @throws NoneffectiveUpdateExecuteException the noneffective update execute exception
     */
    public boolean addUserToAdmin(String groupId, String ownerId, String userId) throws NoneffectiveUpdateExecuteException {
        return groupRelationDao.isUserOwnedTheGroup(ownerId, groupId)
                && groupRelationDao.isUserInGroup(groupId, userId)
                && groupRelationDao.addUserToAdmin(groupId, userId);
    }

    /**
     * 踢用户出群
     * 群主可以踢管理和成员
     * 管理可以踢成员 不可以踢群主和其他管理
     *
     * @param groupId  the group id
     * @param opUserId the op user id
     * @param userId   the user id
     * @return boolean
     * @throws NoneffectiveUpdateExecuteException the noneffective update execute exception
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
     *
     * @param groupId the group id
     * @param ownerId the owner id
     * @param userId  the user id
     * @return boolean
     * @throws NoneffectiveUpdateExecuteException the noneffective update execute exception
     */
    public boolean removeUserFromAdmin(String groupId, String ownerId, String userId) throws NoneffectiveUpdateExecuteException {
        return !ownerId.equals(userId)
                && groupRelationDao.isUserOwnedTheGroup(ownerId, groupId)
                && groupRelationDao.removeUserFromAdmin(groupId, userId);
    }

    /**
     * 解散群组
     *
     * @param groupId the group id
     * @param ownerId the owner id
     * @return boolean
     * @throws NoneffectiveUpdateExecuteException the noneffective update execute exception
     */
    public boolean dissolveTheGroup(String groupId, String ownerId) throws NoneffectiveUpdateExecuteException {
        HashSet<String> groupMemberIds = groupRelationDao.getGroupMemberIds(groupId);
        if (groupMemberIds != null) {
            for (String groupMemberId : groupMemberIds) {
                userRelationDao.quitGroup(groupMemberId, groupId);
            }
            return groupRelationDao.dissolveTheGroup(groupId, ownerId);
        } else {
            return false;
        }
    }

}
