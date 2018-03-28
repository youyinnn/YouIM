package com.github.youyinnn.server.dao;

import com.alibaba.fastjson.JSON;
import com.github.youyinnn.server.model.UserRelation;
import com.github.youyinnn.youdbutils.dao.YouDao;
import com.github.youyinnn.youdbutils.exceptions.NoneffectiveUpdateExecuteException;
import com.github.youyinnn.youwebutils.third.YouCollectionsUtils;

import java.util.HashSet;

/**
 * @author youyinnn
 */
public class UserRelationDao extends YouDao<UserRelation> {

    public boolean isUserRelationExist(String userId) {
        return modelHandler.getModel(YouCollectionsUtils.getYouHashMap("userId", userId),
                null, "AND") != null;
    }

    public boolean addUserRelation(String userId) throws NoneffectiveUpdateExecuteException {
        if (isUserRelationExist(userId)) {
            return false;
        } else {
            UserRelation userRelation = new UserRelation();
            userRelation.setUserId(userId);
            return modelHandler.saveModel(userRelation) == 1;
        }
    }

    public boolean addUserRelation(UserRelation userRelation) throws NoneffectiveUpdateExecuteException {
        if (isUserRelationExist(userRelation.getUserId())) {
            return false;
        } else {
            return modelHandler.saveModel(userRelation) == 1;
        }
    }

    public HashSet<String> getFriendSet(String userId) {
        String fieldValue = (String) modelHandler.getModelFieldValue("friendSet",
                YouCollectionsUtils.getYouHashMap("userId", userId), "AND");
        if (fieldValue != null) {
            return new HashSet<>(JSON.parseArray(fieldValue, String.class));
        } else {
            return null;
        }
    }

    public boolean addFriend(String userId, String friendId) throws NoneffectiveUpdateExecuteException {
        HashSet<String> aFriendList = getFriendSet(userId);
        if (aFriendList.add(friendId)) {
            HashSet<String> bFriendSet = getFriendSet(friendId);
            if (bFriendSet != null) {
                bFriendSet.add(userId);
                return updateFriendSet(userId, aFriendList) && updateFriendSet(friendId, bFriendSet);
            } else {
                UserRelation b = new UserRelation();
                b.setUserId(friendId);
                b.setFriendSet("[\"" + userId + "\"]");
                return updateFriendSet(userId, aFriendList) && addUserRelation(b);
            }
        } else {
            return false;
        }
    }

    public boolean updateFriendSet(String userId, HashSet<String> friendSet) throws NoneffectiveUpdateExecuteException {
        int result = modelHandler.updateModel(
                YouCollectionsUtils.getYouHashMap("friendSet", JSON.toJSONString(friendSet)),
                YouCollectionsUtils.getYouHashMap("userId", userId), "AND");
        if (result == 0) {
            UserRelation friendShip = new UserRelation();
            friendShip.setUserId(userId);
            friendShip.setFriendSet(JSON.toJSONString(friendSet));
            result = modelHandler.saveModel(friendShip);
        }
        return result == 1;
    }

    public boolean removeFriend(String userId, String friendId) throws NoneffectiveUpdateExecuteException {
        HashSet<String> aFriendList = getFriendSet(userId);
        if (aFriendList.remove(friendId)) {
            HashSet<String> bFriendSet = getFriendSet(friendId);
            bFriendSet.remove(userId);
            return updateFriendSet(userId, aFriendList)
                    && updateFriendSet(friendId, bFriendSet);
        } else {
            return false;
        }
    }

    public HashSet<String> getGroupSet(String userId) {
        String groupSetValue = (String) modelHandler.getModelFieldValue("groupSet",
                YouCollectionsUtils.getYouHashMap("userId", userId), "AND");
        return new HashSet<>(JSON.parseArray(groupSetValue, String.class));
    }

    public boolean joinGroup(String userId, String groupId) throws NoneffectiveUpdateExecuteException {
        HashSet<String> groupSet = getGroupSet(userId);
        return groupSet.add(groupId) && updateGroupSet(userId, groupSet);
    }

    public boolean quitGroup(String userId, String groupId) throws NoneffectiveUpdateExecuteException {
        HashSet<String> groupSet = getGroupSet(userId);
        return groupSet.remove(groupId) && updateGroupSet(userId, groupSet);
    }

    public boolean updateGroupSet(String userId, HashSet<String> groupSet) throws NoneffectiveUpdateExecuteException {
        return modelHandler.updateModel(
                YouCollectionsUtils.getYouHashMap("groupSet", JSON.toJSONString(groupSet)),
                YouCollectionsUtils.getYouHashMap("userId", userId), "AND") == 1;
    }
}
