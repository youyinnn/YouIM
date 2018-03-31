package com.github.youyinnn.common.packets;

/**
 * @author youyinnn
 */
public class AddAdminRequestBody extends BaseBody{

    private String ownerId;

    private String groupId;

    private String toUserId;

    public AddAdminRequestBody() {
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
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

    public AddAdminRequestBody(String ownerId, String groupId, String toUserId) {
        this.ownerId = ownerId;
        this.groupId = groupId;
        this.toUserId = toUserId;
    }
}
