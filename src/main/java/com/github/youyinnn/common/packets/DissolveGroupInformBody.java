package com.github.youyinnn.common.packets;

/**
 * @author youyinnn
 */
public class DissolveGroupInformBody extends BaseBody {

    private String groupId;

    public DissolveGroupInformBody() {
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public DissolveGroupInformBody(String groupId) {
        this.groupId = groupId;
    }
}
