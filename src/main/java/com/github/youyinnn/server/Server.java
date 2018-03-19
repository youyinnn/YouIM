package com.github.youyinnn.server;

import com.github.youyinnn.common.intf.Const;
import com.github.youyinnn.common.packets.BasePacket;
import com.github.youyinnn.common.utils.WsTioUuId;
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

import java.io.IOException;

/**
 * @author youyinnn
 */
public class Server {

    private static ServerConfig serverConfig;

    private static ServerGroupContext serverGroupContext = null;
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
        if (serverConfig == null) {
            serverConfig = new ServerConfig();
        }
        init(serverConfig);
    }

    public static void init(ServerConfig serverConfig) {
        Server.serverConfig = serverConfig;
        YouDbManager.youDruid.initSQLiteDataSource("$serverConf/$sqlite.properties");
        YouDbManager.scanPackageForModel("com.github.youyinnn.server.model");
        YouDbManager.scanPackageForService("com.github.youyinnn.server.service");

        if (serverConfig.getGroupContextName() == null) {
            serverConfig.setGroupContextName(Const.Server.DEF_GROUP_CONTEXT_NAME);
        }
        if (serverConfig.getBindPort() == null) {
            serverConfig.setBindPort(Const.Server.PORT);
        }
        if (serverConfig.getHandler() == null) {
            serverConfig.setHandler(new MyServerAioHandler());
        }
        if (serverConfig.getListener() == null) {
            serverConfig.setListener(new MyServerAioListener());
        }
        if (serverConfig.getTioUuid() == null) {
            serverConfig.setTioUuid(new WsTioUuId());
        }
        serverGroupContext = new ServerGroupContext(serverConfig.getGroupContextName(),
                serverConfig.getHandler(), serverConfig.getListener());

        aioServer = new AioServer(serverGroupContext);

        serverGroupContext.setTioUuid(serverConfig.getTioUuid());
        if (serverLogEnabled) {
            if (serverConfig.getBindIp() != null) {
                SERVER_LOG.info("Server named: {} init with IP:{}, Port:{}, PID:{}.", serverGroupContext.getName(), serverConfig.getBindIp(), serverConfig.getBindPort(), PropertiesHelper.getPID());
            } else {
                SERVER_LOG.info("Server named: {} init with Port:{}, PID:{}", serverGroupContext.getName(), serverConfig.getBindPort(),PropertiesHelper.getPID());
            }
        }
    }

    public static void start() {
        try {
            aioServer.start(serverConfig.getBindIp(), serverConfig.getBindPort());
            SERVER_LOG.info("Started success!");
        } catch (IOException e) {
            SERVER_LOG.error(e.getMessage(), "Started fail because:" + e);
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

    public static ServerGroupContext getServerGroupContext() {
        return serverGroupContext;
    }

    public static AioServer getAioServer() {
        return aioServer;
    }
}
