package com.github.youyinnn.common.packets.inform;

import com.github.youyinnn.common.packets.BaseBody;

/**
 * @author youyinnn
 */
public class RemoveFriendInformBody extends BaseBody {

    private String toUserId;
    private String fromUserId;

    public RemoveFriendInformBody() {
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public RemoveFriendInformBody(String fromUserId, String toUserId) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
    }
}
