package com.github.youyinnn.server.dao;

import com.alibaba.fastjson.JSON;
import com.github.youyinnn.server.model.GroupRelation;
import com.github.youyinnn.youdbutils.dao.YouDao;
import com.github.youyinnn.youdbutils.exceptions.NoneffectiveUpdateExecuteException;
import com.github.youyinnn.youwebutils.third.YouCollectionsUtils;

import java.util.HashSet;

/**
 * @author youyinnn
 */
public class GroupRelationDao extends YouDao<GroupRelation>{

    public boolean isGroupRelationExist(String groupId) {
        return modelHandler.getModel(YouCollectionsUtils.getYouHashMap("groupId", groupId),
                null, "AND") != null;
    }

    public boolean addGroupRelation(String groupId, String ownerId) throws NoneffectiveUpdateExecuteException {
        if (isGroupRelationExist(groupId)) {
            return false;
        } else {
            GroupRelation groupRelation = new GroupRelation();
            groupRelation.setGroupId(groupId);
            groupRelation.setOwnerId(ownerId);
            groupRelation.setAdministratorIds("[\""+ownerId+"\"]");
            groupRelation.setGroupMemberIds("[\""+ownerId+"\"]");
            return modelHandler.saveModel(groupRelation) == 1;
        }
    }

    public HashSet<String> getGroupMemberIds(String groupId) {
        String groupMemberIds = (String) modelHandler.getModelFieldValue("groupMemberIds",
                YouCollectionsUtils.getYouHashMap("groupId", groupId),"AND");
        return new HashSet<>(JSON.parseArray(groupMemberIds, String.class));
    }

    public HashSet<String> getAdministratorIds(String groupId){
        String administratorIds = (String) modelHandler.getModelFieldValue("administratorIds",
                YouCollectionsUtils.getYouHashMap("groupId", groupId),"AND");
        return new HashSet<>(JSON.parseArray(administratorIds, String.class));
    }

    public String getOwnerId(String groupId) {
        return (String) modelHandler.getModelFieldValue("ownerId",
                YouCollectionsUtils.getYouHashMap("groupId", groupId), "AND");
    }

    public boolean handOverGroupTo(String groupId, String newOwnerId) throws NoneffectiveUpdateExecuteException {
        return !getOwnerId(groupId).equals(newOwnerId) &&
                isUserInGroup(groupId, newOwnerId) &&
                modelHandler.updateModel(YouCollectionsUtils.getYouHashMap("ownerId", newOwnerId),
                        YouCollectionsUtils.getYouHashMap("groupId", groupId), "AND") == 1;
    }

    public boolean isUserOwnedTheGroup(String userId, String groupId) {
        return getOwnerId(groupId).equals(userId);
    }

    public boolean isUserInGroup(String groupId, String userId) {
        return getGroupMemberIds(groupId).contains(userId);
    }

    public boolean isUserAdminTheGroup(String userId, String groupId) {
        return getAdministratorIds(groupId).contains(userId);
    }

    public boolean updateAdministratorIds(String groupId, HashSet<String> administratorIds) throws NoneffectiveUpdateExecuteException {
        return modelHandler.updateModel(
                YouCollectionsUtils.getYouHashMap("administratorIds",JSON.toJSONString(administratorIds)),
                YouCollectionsUtils.getYouHashMap("groupId", groupId),"AND") == 1;
    }

    public boolean updateGroupMemberIds(String groupId, HashSet<String> groupMemberIds) throws NoneffectiveUpdateExecuteException {
        return modelHandler.updateModel(
                YouCollectionsUtils.getYouHashMap("groupMemberIds",JSON.toJSONString(groupMemberIds)),
                YouCollectionsUtils.getYouHashMap("groupId", groupId),"AND") == 1;
    }

    public boolean addUserToAdmin(String groupId, String userId) throws NoneffectiveUpdateExecuteException {
        if (isUserInGroup(groupId, userId)) {
            HashSet<String> administratorIds = getAdministratorIds(groupId);
            return administratorIds.add(userId) && updateAdministratorIds(groupId, administratorIds);
        } else {
            return false;
        }
    }

    public boolean removeUserFromAdmin(String groupId, String userId) throws NoneffectiveUpdateExecuteException {
        if (isUserInGroup(groupId, userId)) {
            HashSet<String> administratorIds = getAdministratorIds(groupId);
            return administratorIds.remove(userId) && updateAdministratorIds(groupId, administratorIds);
        } else {
            return false;
        }
    }

    public boolean addUserToGroup(String groupId, String userId) throws NoneffectiveUpdateExecuteException {
        HashSet<String> groupMemberIds = getGroupMemberIds(groupId);
        return groupMemberIds.add(userId) && updateGroupMemberIds(groupId, groupMemberIds);
    }

    public boolean removeUserFromGroup(String groupId, String userId) throws NoneffectiveUpdateExecuteException {
        HashSet<String> groupMemberIds = getGroupMemberIds(groupId);
        return groupMemberIds.remove(userId) && updateGroupMemberIds(groupId, groupMemberIds);
    }

    public boolean dissolveTheGroup(String groupId, String ownerId) throws NoneffectiveUpdateExecuteException {
        return modelHandler.deleteModel(
                YouCollectionsUtils.getYouHashMap("groupId", groupId, "ownerId", ownerId), "AND") == 1;
    }

}
