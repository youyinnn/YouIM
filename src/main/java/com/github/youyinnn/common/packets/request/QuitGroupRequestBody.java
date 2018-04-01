package com.github.youyinnn.common.packets.request;

import com.github.youyinnn.common.packets.BaseBody;

/**
 * @author youyinnn
 */
public class QuitGroupRequestBody extends BaseBody {

    private String fromUserId;

    private String groupId;

    public QuitGroupRequestBody() {
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public QuitGroupRequestBody(String fromUserId, String groupId) {

        this.fromUserId = fromUserId;
        this.groupId = groupId;
    }
}
