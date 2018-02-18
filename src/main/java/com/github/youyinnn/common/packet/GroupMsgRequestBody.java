package com.github.youyinnn.common.packet;

/**
 * @author youyinnn
 */
public class GroupMsgRequestBody extends BaseBody {

    /**
     * 消息内容
     */
    private String msg;

    /**
     * 发送到哪个组
     */
    private String toGroup;

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
}
