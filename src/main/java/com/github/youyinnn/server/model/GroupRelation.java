package com.github.youyinnn.server.model;

/**
 * @author youyinnn
 */
public class GroupRelation {

    private Integer id;

    private String groupId;

    private String ownerUserId;

    private String administratorIds;

    private String groupMemberIds;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(String ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public String getAdministratorIds() {
        return administratorIds;
    }

    public void setAdministratorIds(String administratorIds) {
        this.administratorIds = administratorIds;
    }

    public String getGroupMemberIds() {
        return groupMemberIds;
    }

    public void setGroupMemberIds(String groupMemberIds) {
        this.groupMemberIds = groupMemberIds;
    }
}
