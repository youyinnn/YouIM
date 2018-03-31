package com.github.youyinnn.common.packets;

/**
 * @author youyinnn
 */
public class KickMemberRequestBody extends BaseBody{

    private String fromAdministratorUserId;

    private String fromGroup;

    private String toUserId;

    public String getFromAdministratorId() {
        return fromAdministratorUserId;
    }

    public void setFromAdministratorId(String fromAdministratorUserId) {
        this.fromAdministratorUserId = fromAdministratorUserId;
    }

    public String getFromGroup() {
        return fromGroup;
    }

    public void setFromGroup(String fromGroup) {
        this.fromGroup = fromGroup;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public KickMemberRequestBody() {

    }

    public KickMemberRequestBody(String fromAdministratorUserId, String fromGroup, String toUserId) {

        this.fromAdministratorUserId = fromAdministratorUserId;
        this.fromGroup = fromGroup;
        this.toUserId = toUserId;
    }
}
