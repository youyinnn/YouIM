package com.github.youyinnn.common.packet;

/**
 * @author youyinnn
 */
public class P2PResponseBody extends BaseBody {

    /**
     * 消息内容
     */
    private String msg;

    /**
     * 消息发送方id
     */
    private String fromUserId;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public P2PResponseBody() {
    }

    public P2PResponseBody(String msg, String fromUserId) {
        this.msg = msg;
        this.fromUserId = fromUserId;
    }
}
