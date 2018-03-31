package com.github.youyinnn.common.packets;

/**
 * @author youyinnn
 */
public class AddFriendConfirmInformBody extends BaseBody{

    private String fromUserId;

    public AddFriendConfirmInformBody() {
    }

    public AddFriendConfirmInformBody(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }
}
