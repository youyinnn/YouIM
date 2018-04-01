package com.github.youyinnn.common.packets.request;

import com.github.youyinnn.common.packets.BaseBody;

/**
 * @author youyinnn
 */
public class JoinGroupRequestBody extends BaseBody {

    /**
     * 要加入的群组
     */
    private String groupId;

    /**
     * 请求者Id
     */
    private String fromUserId;

    public String getGroup() {
        return groupId;
    }

    public void setGroup(String groupId) {
        this.groupId = groupId;
    }

    public String getFromUserId() {

        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public JoinGroupRequestBody() {
    }

    public JoinGroupRequestBody(String groupId, String fromUserId) {
        this.groupId = groupId;
        this.fromUserId = fromUserId;
    }

}
