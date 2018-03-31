package com.github.youyinnn.common.packets;

/**
 * @author youyinnn
 */
public class MemberOnlineInformBody extends BaseBody {

    private String userId;

    private String toGroupId;

    public MemberOnlineInformBody() {
    }

    public MemberOnlineInformBody(String userId, String toGroupId) {
        this.userId = userId;
        this.toGroupId = toGroupId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToGroupId() {
        return toGroupId;
    }

    public void setToGroupId(String toGroupId) {
        this.toGroupId = toGroupId;
    }
}
