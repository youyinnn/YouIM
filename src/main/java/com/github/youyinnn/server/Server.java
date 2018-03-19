package com.github.youyinnn.server;

import com.github.youyinnn.common.Const;
import com.github.youyinnn.common.packets.BasePacket;
import com.github.youyinnn.demo.server.MyServerAioHandler;
import com.github.youyinnn.demo.server.MyServerAioListener;
import com.github.youyinnn.youdbutils.YouDbManager;
import com.github.youyinnn.youwebutils.second.PropertiesHelper;
import com.github.youyinnn.youwebutils.third.Log4j2Helper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.DocumentException;
import org.tio.core.Aio;
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
    private static String groupContextName = null;

    private static AioServer aioServer = null;

    private static boolean serverLogEnabled = false;

    private static boolean serverHandlerLogEnabled = false;
    private static boolean serverListenerLogEnabled = false;
    public static void enableServerLog() {
        serverLogEnabled = true;
    }

    public static void enableServerHandlerLog() {
        serverHandlerLogEnabled = true;
    }

    public static void enableServerListenerLog() {
        serverListenerLogEnabled = true;
    }

    public static void enableAllLogEnabled() {
        enableServerHandlerLog();
        enableServerListenerLog();
        enableServerLog();
    }

    static boolean isServerLogEnabled() {
        return serverLogEnabled;
    }

    static boolean isServerHandlerLogEnabled() {
        return serverHandlerLogEnabled;
    }

    static boolean isServerListenerLogEnabled() {
        return serverListenerLogEnabled;
    }

    static {
        try {
            Log4j2Helper.useConfig("$serverConf/$log4j2.xml");
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }

    private static final Logger SERVER_LOG = LogManager.getLogger("$im_server");

    public static void init() {
        YouDbManager.youDruid.initSQLiteDataSource("$serverConf/$sqlite.properties");
        YouDbManager.scanPackageForModel("com.github.youyinnn.server.model");
        YouDbManager.scanPackageForService("com.github.youyinnn.server.service");
        if (serverPort == null) {
            serverPort = Const.Server.PORT;
        }
        if (serverAioHandler == null) {
            serverAioHandler = new MyServerAioHandler();
        }
        if (serverAioListener == null) {
            serverAioListener = new MyServerAioListener();
        }
        serverGroupContext = new ServerGroupContext(groupContextName, serverAioHandler, serverAioListener);
        aioServer = new AioServer(serverGroupContext);
        if (serverLogEnabled) {
            if (serverIp != null) {
                SERVER_LOG.info("YouIM server named: {} init with IP:{}, Port:{}, PID:{}.", serverGroupContext.getName(), serverIp, serverPort, PropertiesHelper.getPID());
            } else {
                SERVER_LOG.info("YouIM server named: {} init with Port:{}, PID:{}", serverGroupContext.getName(), serverPort,PropertiesHelper.getPID());
            }
        }
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

    public static void toAllUser(String msg) {
        Aio.sendToAll(serverGroupContext, BasePacket.systemMsgToAllPacket(msg));
    }

    public static void toUser(String msg, String userId) {
        Aio.sendToUser(serverGroupContext, userId, BasePacket.systemMsgToOnePacket(msg));
    }

    public static void toGroup(String msg, String toGroup) {
        Aio.sendToGroup(serverGroupContext, toGroup, BasePacket.systemMsgToGroupPacket(msg, toGroup));
    }

    public static boolean isLogin(String userId) {
        return serverGroupContext.users.find(serverGroupContext, userId) != null;
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

    public static void setGroupContextName(String groupContextName) {
        Server.groupContextName = groupContextName;
    }

    public static String getGroupContextName() {
        return groupContextName;
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
