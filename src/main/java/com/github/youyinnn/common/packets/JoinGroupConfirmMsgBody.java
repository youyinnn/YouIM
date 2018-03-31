package com.github.youyinnn.common.packets;

/**
 * @author youyinnn
 */
public class JoinGroupConfirmMsgBody extends BaseBody{

    private boolean confirmResult;

    private String groupId;

    private String toUserId;

    private String handleAdministratorId;

    public JoinGroupConfirmMsgBody() {
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

    public String getHandleAdministratorId() {
        return handleAdministratorId;
    }

    public void setHandleAdministratorId(String handleAdministratorId) {
        this.handleAdministratorId = handleAdministratorId;
    }

    public JoinGroupConfirmMsgBody(boolean confirmResult, String groupId, String toUserId, String handleAdministratorId) {
        this.confirmResult = confirmResult;
        this.groupId = groupId;
        this.toUserId = toUserId;
        this.handleAdministratorId = handleAdministratorId;
    }
}
