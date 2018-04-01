package com.github.youyinnn.common.packets.inform;

import com.github.youyinnn.common.packets.BaseBody;

/**
 * @author youyinnn
 */
public class FriendOnlineInformBody extends BaseBody {

    private String friendId;

    public FriendOnlineInformBody() {
    }

    public FriendOnlineInformBody(String friendId) {
        this.friendId = friendId;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }
}
