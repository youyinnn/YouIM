package com.github.youyinnn.common.packets;

/**
 * @author youyinnn
 */
public class QuitGroupInformBody extends BaseBody {

    private String fromUserId;
    private String groupId;

    public QuitGroupInformBody() {
    }

    public String getFromUserId() {

        return fromUserId;
    }

    public String getGroupId() {
        return groupId;
    }

    public QuitGroupInformBody(String fromUserId, String groupId) {
        this.fromUserId = fromUserId;
        this.groupId = groupId;
    }
}
