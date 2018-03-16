package com.github.youyinnn.common.packet;

/**
 * @author youyinnn
 */
public class LogoutRequestBody extends BaseBody {

    private String logoutUserId;

    public LogoutRequestBody() {
    }

    public LogoutRequestBody(String logoutUserId) {
        this.logoutUserId = logoutUserId;
    }

    public String getLogoutUserId() {
        return logoutUserId;
    }

    public void setLogoutUserId(String logoutUserId) {
        this.logoutUserId = logoutUserId;
    }
}
