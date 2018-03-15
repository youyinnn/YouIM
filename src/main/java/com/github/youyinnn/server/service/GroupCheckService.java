package com.github.youyinnn.server.service;

import com.alibaba.fastjson.JSON;
import com.github.youyinnn.server.model.GroupCheck;
import com.github.youyinnn.youdbutils.dao.YouDao;
import com.github.youyinnn.youdbutils.exceptions.NoneffectiveUpdateExecuteException;
import com.github.youyinnn.youdbutils.ioc.annotations.YouService;
import com.github.youyinnn.youdbutils.utils.YouCollectionsUtils;

import java.util.List;

/**
 * @author youyinnn
 */
@YouService
public class GroupCheckService extends YouDao<GroupCheck> {

    public String getGroupJsonStr(String userId) {
        return (String) modelHandler.getModelFieldValue("joinGroupJsonStr",
                YouCollectionsUtils.getYouHashMap("userId", userId), "AND");
    }

    public boolean updateGroupJson(String userId, String joinGroupJsonStr) {
        try {
            return modelHandler.updateModel(YouCollectionsUtils.getYouHashMap("joinGroupJsonStr", joinGroupJsonStr),
                    YouCollectionsUtils.getYouHashMap("userId", userId), "AND") == 1;
        } catch (NoneffectiveUpdateExecuteException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean registerGroupInJson(String userId, String groupId) {
        String groupJsonStr = getGroupJsonStr(userId);
        List<String> groupList;
        if (groupJsonStr == null) {
            GroupCheck groupCheck = new GroupCheck();
            groupCheck.setJoinGroupJsonStr("[\""+groupId+"\"]");
            groupCheck.setUserId(userId);
            try {
                return modelHandler.saveModel(groupCheck) == 1;
            } catch (NoneffectiveUpdateExecuteException e) {
                e.printStackTrace();
            }
        } else {
            groupList = JSON.parseArray(groupJsonStr, String.class);
            if (!groupList.contains(groupId)) {
                groupList.add(groupId);
                return updateGroupJson(userId, JSON.toJSONString(groupList));
            }
        }
        return false;
    }

}
