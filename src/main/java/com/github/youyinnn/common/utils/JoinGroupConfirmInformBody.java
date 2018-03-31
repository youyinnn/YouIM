package com.github.youyinnn.common.utils;

import com.github.youyinnn.common.packets.BaseBody;

/**
 * @author youyinnn
 */
public class JoinGroupConfirmInformBody extends BaseBody {

    private String groupId;

    private String fromUserId;

    public JoinGroupConfirmInformBody() {
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public JoinGroupConfirmInformBody(String groupId, String fromUserId) {
        this.groupId = groupId;
        this.fromUserId = fromUserId;
    }
}
