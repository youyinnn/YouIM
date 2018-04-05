package com.github.youyinnn.common.packets.inform;

import com.github.youyinnn.common.packets.BaseBody;

/**
 * @author youyinnn
 */
public class KickMemberInformBody extends BaseBody {

    private String fromAdministratorUserId;

    private String fromGroupId;

    private String toUserId;

    public KickMemberInformBody() {
    }

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

    public KickMemberInformBody(String fromAdministratorUserId, String fromGroupId, String toUserId) {

        this.fromAdministratorUserId = fromAdministratorUserId;
        this.fromGroupId = fromGroupId;
        this.toUserId = toUserId;
    }
}
