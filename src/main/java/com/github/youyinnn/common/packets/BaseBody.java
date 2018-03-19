package com.github.youyinnn.common.packets;

import java.time.LocalDateTime;

/**
 * @author youyinnn
 */
public class BaseBody {

    /**
     * 消息发送时间
     */
    private String time = String.valueOf(LocalDateTime.now());

    public String getTime() {
        return time;
    }
}
