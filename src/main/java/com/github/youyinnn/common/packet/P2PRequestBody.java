package com.github.youyinnn.common.packet;

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

    public P2PRequestBody() {
    }

    public P2PRequestBody(String msg, String toUserId) {
        this.msg = msg;
        this.toUserId = toUserId;
    }
}
