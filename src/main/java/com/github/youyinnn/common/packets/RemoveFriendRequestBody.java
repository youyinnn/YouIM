package com.github.youyinnn.common.packets;

/**
 * @author youyinnn
 */
public class RemoveFriendRequestBody extends BaseBody{

    private String fromUserId;

    private String toUserId;

    public RemoveFriendRequestBody() {
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public RemoveFriendRequestBody(String fromUserId, String toUserId) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
    }
}
