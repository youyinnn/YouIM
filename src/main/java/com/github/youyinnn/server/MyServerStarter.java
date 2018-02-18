package com.github.youyinnn.server;

import com.github.youyinnn.common.Const;
import org.tio.server.AioServer;
import org.tio.server.ServerGroupContext;
import org.tio.server.intf.ServerAioHandler;
import org.tio.server.intf.ServerAioListener;

import java.io.IOException;

/**
 * @author youyinnn
 */
public class MyServerStarter {

    private static String serverIp = null;
    private static int serverPort = Const.PORT;

    private static ServerAioHandler serverAioHandler = new MyServerAioHandler();
    private static ServerAioListener serverAioListener = new MyServerAioListener();
    private static ServerGroupContext serverGroupContext = new ServerGroupContext(serverAioHandler, serverAioListener);

    private static AioServer aioServer = new AioServer(serverGroupContext);

    public static void main(String[] args) throws IOException {
        aioServer.start(serverIp, serverPort);
    }

}
