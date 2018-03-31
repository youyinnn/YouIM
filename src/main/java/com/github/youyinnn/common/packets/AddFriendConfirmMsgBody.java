package com.github.youyinnn.common.packets;

/**
 * @author youyinnn
 */
public class AddFriendConfirmMsgBody extends BaseBody{

    private boolean confirmResult;

    private String fromUserId;

    private String toUserId;

    public AddFriendConfirmMsgBody() {
    }

    public boolean isConfirmResult() {
        return confirmResult;
    }

    public void setConfirmResult(boolean confirmResult) {
        this.confirmResult = confirmResult;
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

    public AddFriendConfirmMsgBody(boolean confirmResult, String fromUserId, String toUserId) {
        this.confirmResult = confirmResult;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
    }
}
