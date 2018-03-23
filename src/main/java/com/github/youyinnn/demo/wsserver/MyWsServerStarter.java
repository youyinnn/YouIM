package com.github.youyinnn.demo.wsserver;

import com.github.youyinnn.demo.server.MyServerStarter;
import com.github.youyinnn.server.Server;
import com.github.youyinnn.server.ServerConfig;
import org.dom4j.DocumentException;

import java.io.IOException;

/**
 * @author youyinnn
 */
public class MyWsServerStarter {

    public static void main(String[] args) throws IOException, DocumentException {
        Server.enableAllLogEnabled();
        Server.init(ServerConfig.getWsServerConfig(5999));
        Server.start();
        MyServerStarter.command();
    }

}
