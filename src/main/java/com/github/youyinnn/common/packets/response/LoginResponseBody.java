package com.github.youyinnn.common.packets.response;

import com.github.youyinnn.common.packets.BaseBody;

/**
 * @author youyinnn
 */
public class LoginResponseBody extends BaseBody {

    private String resultCode;

    public LoginResponseBody(String resultCode, String token) {
        this.resultCode = resultCode;
        super.setToken(token);
    }

    public LoginResponseBody() {
    }

    public String getResultCode() {

        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

}
