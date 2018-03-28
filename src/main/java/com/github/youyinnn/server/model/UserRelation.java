package com.github.youyinnn.server.model;

/**
 * @author youyinnn
 */
public class UserRelation {

    private Integer id;

    private String userId;

    private String friendSet;

    private String groupSet;

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

    public String getFriendSet() {
        return friendSet;
    }

    public void setFriendSet(String friendSet) {
        this.friendSet = friendSet;
    }

    public String getGroupSet() {
        return groupSet;
    }

    public void setGroupSet(String groupSet) {
        this.groupSet = groupSet;
    }
}
