package com.github.youyinnn.demo.wsserver;

import com.github.youyinnn.demo.server.MyServerStarter;
import com.github.youyinnn.server.Server;
import com.github.youyinnn.server.ServerConfig;

/**
 * @author youyinnn
 */
public class MyWsServerStarter {

    public static void main(String[] args) {
        Server.enableAllLogEnabled();
        Server.init(ServerConfig.getWsServerConfig(5999));
        Server.start();
        MyServerStarter.command();
    }

}
