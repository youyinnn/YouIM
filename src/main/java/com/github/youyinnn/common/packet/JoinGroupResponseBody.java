package com.github.youyinnn.common.packet;

/**
 * @author youyinnn
 */
public class JoinGroupResponseBody extends BaseBody{

    /**
     * 进群结果
     */
    private String resultCode;

    /**
     * 对结果的说明信息
     */
    private String msg;

    /**
     * 群id
     */
    private String group;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
