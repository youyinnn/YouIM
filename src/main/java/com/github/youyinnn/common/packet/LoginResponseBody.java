package com.github.youyinnn.common.packet;

/**
 * @author youyinnn
 */
public class LoginResponseBody extends BaseBody {

    private String token;

    public LoginResponseBody() {
    }

    public LoginResponseBody(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
