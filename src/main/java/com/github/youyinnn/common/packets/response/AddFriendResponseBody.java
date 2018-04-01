package com.github.youyinnn.common.packets.response;

import com.github.youyinnn.common.packets.BaseBody;

/**
 * @author youyinnn
 */
public class AddFriendResponseBody extends BaseBody {

    private String fromUserId;

    private String toUserId;

    private boolean confirmResult;

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

    public boolean isConfirmResult() {
        return confirmResult;
    }

    public void setConfirmResult(boolean confirmResult) {
        this.confirmResult = confirmResult;
    }

    public AddFriendResponseBody() {
    }

    public AddFriendResponseBody(String fromUserId, String toUserId, boolean confirmResult) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.confirmResult = confirmResult;
    }
}
