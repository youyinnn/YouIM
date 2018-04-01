package com.github.youyinnn.common.packets.request;

import com.github.youyinnn.common.packets.BaseBody;

/**
 * @author youyinnn
 */
public class SignInUserRequestBody extends BaseBody {

    private String userId;

    public SignInUserRequestBody() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public SignInUserRequestBody(String userId) {
        this.userId = userId;
    }
}
