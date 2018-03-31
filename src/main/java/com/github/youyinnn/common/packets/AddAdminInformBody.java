package com.github.youyinnn.common.packets;

/**
 * @author youyinnn
 */
public class AddAdminInformBody extends BaseBody {

    private String ownerId;

    private String toUserId;

    private String groupId;

    public AddAdminInformBody() {
    }

    public AddAdminInformBody(String ownerId, String toUserId, String groupId) {
        this.ownerId = ownerId;
        this.toUserId = toUserId;
        this.groupId = groupId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
