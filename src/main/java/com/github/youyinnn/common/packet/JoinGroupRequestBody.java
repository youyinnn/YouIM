package com.github.youyinnn.common.packet;

/**
 * @author youyinnn
 */
public class JoinGroupRequestBody extends BaseBody {

    private String group;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
