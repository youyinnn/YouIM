package com.github.youyinnn.common.packets.inform;

import com.github.youyinnn.common.packets.BaseBody;

/**
 * @author youyinnn
 */
public class MemberOfflineInformBody extends BaseBody {

    private String userId;

    private String groupId;

    public MemberOfflineInformBody() {
    }

    public MemberOfflineInformBody(String userId, String groupId) {
        this.userId = userId;
        this.groupId = groupId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
