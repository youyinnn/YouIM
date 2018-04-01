package com.github.youyinnn.common.packets.request;

import com.github.youyinnn.common.packets.BaseBody;

/**
 * @author youyinnn
 */
public class DissolveGroupRequestBody extends BaseBody {

    private String groupId;

    private String ownerId;

    public DissolveGroupRequestBody() {
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public DissolveGroupRequestBody(String groupId, String ownerId) {
        this.groupId = groupId;
        this.ownerId = ownerId;
    }
}
