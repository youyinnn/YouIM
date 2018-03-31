package com.github.youyinnn.common.packets;

/**
 * @author youyinnn
 */
public class P2GRequestBody extends BaseBody {

    /**
     * 消息内容
     */
    private String msg;

    /**
     * 发送到哪个组
     */
    private String toGroup;

    /**
     * 发送者id
     */
    private String fromUserId;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getToGroup() {
        return toGroup;
    }

    public void setToGroup(String toGroup) {
        this.toGroup = toGroup;
    }

    public P2GRequestBody() {
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public P2GRequestBody(String msg, String toGroup, String fromUserId) {
        this.msg = msg;
        this.toGroup = toGroup;
        this.fromUserId = fromUserId;
    }
}
