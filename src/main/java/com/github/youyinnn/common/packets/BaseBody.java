package com.github.youyinnn.common.packets;

import java.time.LocalDateTime;

/**
 * @author youyinnn
 */
public class BaseBody {

    private String token;

    /**
     * 消息发送时间
     */
    private String time = String.valueOf(LocalDateTime.now());

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }
}
