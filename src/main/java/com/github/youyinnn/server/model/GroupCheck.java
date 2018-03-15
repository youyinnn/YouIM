package com.github.youyinnn.server.model;

/**
 * @author youyinnn
 */
public class GroupCheck {

    private Integer id;

    private String userId;

    private String joinGroupJsonStr;

    @Override
    public String toString() {
        return "GroupCheck{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", joinGroupJsonStr='" + joinGroupJsonStr + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getJoinGroupJsonStr() {
        return joinGroupJsonStr;
    }

    public void setJoinGroupJsonStr(String joinGroupJsonStr) {
        this.joinGroupJsonStr = joinGroupJsonStr;
    }
}
