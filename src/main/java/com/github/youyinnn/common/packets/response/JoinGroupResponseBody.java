package com.github.youyinnn.common.packets.response;

import com.github.youyinnn.common.packets.BaseBody;

/**
 * @author youyinnn
 */
public class JoinGroupResponseBody extends BaseBody {

    private boolean confirmResult;

    private String groupId;

    private String toUserId;

    public JoinGroupResponseBody() {
    }

    public boolean isConfirmResult() {
        return confirmResult;
    }

    public void setConfirmResult(boolean confirmResult) {
        this.confirmResult = confirmResult;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public JoinGroupResponseBody(boolean confirmResult, String groupId, String toUserId) {
        this.confirmResult = confirmResult;
        this.groupId = groupId;
        this.toUserId = toUserId;
    }
}
