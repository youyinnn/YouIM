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
public class Server {

    private static String serverIp = null;
    private static Integer serverPort = null;

    private static ServerAioHandler serverAioHandler = null;
    private static ServerAioListener serverAioListener = null;
    private static ServerGroupContext serverGroupContext = null;

    private static AioServer aioServer = null;

    public static void init() {
        if (serverPort == null) {
            serverPort = Const.Server.PORT;
        }
        if (serverAioHandler == null) {
            serverAioHandler = new MyServerAioHandler();
        }
        if (serverAioListener == null) {
            serverAioListener = new MyServerAioListener();
        }
        serverGroupContext = new ServerGroupContext(serverAioHandler, serverAioListener);
        aioServer = new AioServer(serverGroupContext);
    }

    public static void init(String serverIp) {
        Server.serverIp = serverIp;
        init();
    }

    public static void init(String serverIp, int serverPort) {
        Server.serverIp = serverIp;
        Server.serverPort = serverPort;
        init();
    }

    public static void init(String serverIp, int serverPort, ServerAioHandler serverAioHandler) {
        Server.serverIp = serverIp;
        Server.serverPort = serverPort;
        Server.serverAioHandler = serverAioHandler;
        init();
    }

    public static void init(String serverIp, int serverPort, ServerAioHandler serverAioHandler, ServerAioListener serverAioListener) {
        Server.serverIp = serverIp;
        Server.serverPort = serverPort;
        Server.serverAioHandler = serverAioHandler;
        Server.serverAioListener = serverAioListener;
        init();
    }

    public static void start() {
        try {
            aioServer.start(serverIp, serverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void stop() {
        aioServer.stop();
    }

    public static void setServerIp(String serverIp) {
        Server.serverIp = serverIp;
    }

    public static void setServerPort(Integer serverPort) {
        Server.serverPort = serverPort;
    }

    public static void setServerAioHandler(ServerAioHandler serverAioHandler) {
        Server.serverAioHandler = serverAioHandler;
    }

    public static void setServerAioListener(ServerAioListener serverAioListener) {
        Server.serverAioListener = serverAioListener;
    }

    public static String getServerIp() {
        return serverIp;
    }

    public static Integer getServerPort() {
        return serverPort;
    }

    public static ServerAioHandler getServerAioHandler() {
        return serverAioHandler;
    }

    public static ServerAioListener getServerAioListener() {
        return serverAioListener;
    }

    public static ServerGroupContext getServerGroupContext() {
        return serverGroupContext;
    }

    public static AioServer getAioServer() {
        return aioServer;
    }
}
