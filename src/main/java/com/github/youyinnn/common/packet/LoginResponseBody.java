package com.github.youyinnn.common.packet;

/**
 * @author youyinnn
 */
public class LoginResponseBody extends BaseBody {

    private String resultCode;

    private String token;

    public LoginResponseBody(String resultCode, String token) {
        this.resultCode = resultCode;
        this.token = token;
    }

    public LoginResponseBody() {
    }

    public String getResultCode() {

        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
