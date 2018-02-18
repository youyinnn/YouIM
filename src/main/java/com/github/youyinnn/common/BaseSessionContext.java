package com.github.youyinnn.common;

/**
 * @author youyinnn
 */
public class BaseSessionContext {

    private String userId = null;

    private String token = null;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
