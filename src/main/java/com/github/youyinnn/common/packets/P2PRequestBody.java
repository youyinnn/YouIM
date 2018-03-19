package com.github.youyinnn.common.packets;

/**
 * @author youyinnn
 */
public class P2PRequestBody extends BaseBody {

    /**
     * 消息内容
     */
    private String msg;

    /**
     * 消息接收方id, 以此为鉴权.
     */
    private String toUserId;

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

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public P2PRequestBody() {
    }

    public P2PRequestBody(String msg, String toUserId, String fromUserId) {
        this.msg = msg;
        this.toUserId = toUserId;
        this.fromUserId = fromUserId;
    }
}
