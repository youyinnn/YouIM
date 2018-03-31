package com.github.youyinnn.common.packets;

/**
 * @author youyinnn
 */
public class FriendOfflineInformBody extends BaseBody {

    private String friendId;

    public FriendOfflineInformBody() {
    }

    public FriendOfflineInformBody(String friendId) {
        this.friendId = friendId;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }
}
