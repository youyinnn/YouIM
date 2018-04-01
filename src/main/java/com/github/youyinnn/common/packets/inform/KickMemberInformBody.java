package com.github.youyinnn.common.packets.inform;

import com.github.youyinnn.common.packets.BaseBody;

/**
 * @author youyinnn
 */
public class KickMemberInformBody extends BaseBody {

    private String fromAdministratorUserId;

    private String fromGroup;

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

    public KickMemberInformBody(String fromAdministratorUserId, String fromGroup, String toUserId) {

        this.fromAdministratorUserId = fromAdministratorUserId;
        this.fromGroup = fromGroup;
        this.toUserId = toUserId;
    }
}
