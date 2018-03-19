package com.github.youyinnn.demo.wsserver;

import com.github.youyinnn.server.Server;
import com.github.youyinnn.server.ServerConfig;
import com.github.youyinnn.server.WsServerAioHandler;

/**
 * @author youyinnn
 */
public class MyWsServerStarter {

    public static void main(String[] args) {
        Server.enableAllLogEnabled();
        Server.init(new ServerConfig(5999, new WsServerAioHandler(new MyWsServerAioHandler()), new MyWsServerAioListener()));
        Server.start();
    }

}
