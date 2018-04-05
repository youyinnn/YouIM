package com.github.youyinnn.common.packets.request;

import com.github.youyinnn.common.packets.BaseBody;

/**
 * @author youyinnn
 */
public class KickMemberRequestBody extends BaseBody {

    private String fromAdministratorUserId;

    private String fromGroupId;

    private String toUserId;

    public String getFromAdministratorId() {
        return fromAdministratorUserId;
    }

    public void setFromAdministratorId(String fromAdministratorUserId) {
        this.fromAdministratorUserId = fromAdministratorUserId;
    }

    public String getFromGroup() {
        return fromGroupId;
    }

    public void setFromGroup(String fromGroupId) {
        this.fromGroupId = fromGroupId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public KickMemberRequestBody() {

    }

    public KickMemberRequestBody(String fromAdministratorUserId, String fromGroupId, String toUserId) {

        this.fromAdministratorUserId = fromAdministratorUserId;
        this.fromGroupId = fromGroupId;
        this.toUserId = toUserId;
    }
}
