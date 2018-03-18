package com.github.youyinnn.common;

/**
 * @author youyinnn
 */
public interface Const {

    interface RequestCode {
        String SUCCESS = "success";
        String FAIL = "fail";
    }

    String CHARSET = "utf-8";

    interface Server {
        int PORT = 5678;
        String LOCAL_SERVER_IP = "127.0.0.1";
    }
}
