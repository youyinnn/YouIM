package com.github.youyinnn.common.packet;

/**
 * @author youyinnn
 */
public class JoinGroupRequestBody extends BaseBody {

    /**
     * 要加入的群组
     */
    private String group;

    /**
     * 请求者Id
     */
    private String fromUserId;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getFromUserId() {

        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public JoinGroupRequestBody() {
    }

    public JoinGroupRequestBody(String group, String fromUserId) {
        this.group = group;
        this.fromUserId = fromUserId;
    }

}
