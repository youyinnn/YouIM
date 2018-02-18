package com.github.youyinnn.common.packet;

/**
 * @author youyinnn
 */
public class LoginRequestBody extends BaseBody{

    private String loginUserId;

    public LoginRequestBody() {
    }

    public LoginRequestBody(String loginUserId) {
        this.loginUserId = loginUserId;
    }

    public String getLoginUserId() {
        return loginUserId;
    }

    public void setLoginUserId(String loginUserId) {
        this.loginUserId = loginUserId;
    }
}
