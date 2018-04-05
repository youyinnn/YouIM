package com.github.youyinnn.server;

import com.github.youyinnn.common.BaseSessionContext;
import com.github.youyinnn.common.intf.Const;
import com.github.youyinnn.common.utils.PacketFactory;
import com.github.youyinnn.common.utils.WsTioUuId;
import com.github.youyinnn.demo.server.MyServerAioHandler;
import com.github.youyinnn.demo.server.MyServerAioListener;
import com.github.youyinnn.demo.wsserver.MyWsServerAioHandler;
import com.github.youyinnn.demo.wsserver.MyWsServerAioListener;
import com.github.youyinnn.youdbutils.YouDbManager;
import com.github.youyinnn.youdbutils.druid.YouDruid;
import com.github.youyinnn.youdbutils.exceptions.DataSourceInitException;
import com.github.youyinnn.youdbutils.exceptions.YouDbManagerException;
import com.github.youyinnn.youwebutils.second.PropertiesHelper;
import com.github.youyinnn.youwebutils.third.Log4j2Helper;
import org.apache.logging.log4j.Logger;
import org.dom4j.DocumentException;
import org.tio.core.Aio;
import org.tio.core.ChannelContext;
import org.tio.server.AioServer;
import org.tio.server.ServerGroupContext;
import org.tio.utils.lock.SetWithLock;

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

    public static boolean isServerLogEnabled() {
        return serverLogEnabled;
    }

    public static boolean isServerHandlerLogEnabled() {
        return serverHandlerLogEnabled;
    }

    public static boolean isServerListenerLogEnabled() {
        return serverListenerLogEnabled;
    }

    static {
        try {
            Log4j2Helper.useConfig(Server.class.getResource("/$imconf/$youimlog4j2.xml"));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public static void init() {
        if (serverConfig == null) {
            serverConfig = ServerConfig.getTcpServerConfig();
        }
        init(serverConfig);
    }

    public static void init(ServerConfig serverConfig) {
        Server.serverConfig = serverConfig;
        if (serverConfig.isUserManagementModuleEnabled()) {
            try {
                YouDbManager.signInYouDruid(YouDruid.initMySQLDataSource(serverConfig.getSqlPropertiesFile(), "im", true));
                YouDbManager.scanPackageForModelAndService("com.github.youyinnn.server.model",
                        "com.github.youyinnn.server.service", "im");
            } catch (DataSourceInitException | YouDbManagerException e) {
                e.printStackTrace();
            }
        }
        if (serverConfig.getGroupContextName() == null) {
            serverConfig.setGroupContextName(Const.Server.DEF_GROUP_CONTEXT_NAME);
        }
        if (serverConfig.getBindPort() == null) {
            serverConfig.setBindPort(Const.Server.PORT);
        }
        if (serverConfig.getBindIp() == null) {
            serverConfig.setBindIp(Const.Server.LOCAL_SERVER_IP);
        }
        if (serverConfig.getHandler() == null) {
            if (ServerConfig.PROTOCOL_TCP.equalsIgnoreCase(serverConfig.getServerProtocol())) {
                serverConfig.setHandler(new MyServerAioHandler());
            } else {
                serverConfig.setHandler(new MyWsServerAioHandler());
            }
        }
        if (serverConfig.getListener() == null) {
            if (ServerConfig.PROTOCOL_TCP.equalsIgnoreCase(serverConfig.getServerProtocol())) {
                serverConfig.setListener(new MyServerAioListener());
            } else {
                serverConfig.setListener(new MyWsServerAioListener());
            }
        }
        if (serverConfig.getTioUuid() == null) {
            serverConfig.setTioUuid(new WsTioUuId());
        }
        serverGroupContext = new ServerGroupContext(serverConfig.getGroupContextName(),
                serverConfig.getHandler(), serverConfig.getListener());

        aioServer = new AioServer(serverGroupContext);
        serverGroupContext.setTioUuid(serverConfig.getTioUuid());
        if (ServerConfig.PROTOCOL_WEBSOCKET.equalsIgnoreCase(serverConfig.getServerProtocol())) {
            serverGroupContext.setHeartbeatTimeout(-1);
        }
        if (serverLogEnabled) {
            Logger serverLog = Log4j2Helper.getLogger("$im_server");
            serverLog.info("Server init with Protocol:{}, Port:{}, PID:{}.",
                    serverConfig.getServerProtocol(),
                    serverConfig.getBindPort(),
                    PropertiesHelper.getPID());
        }
    }

    public static void start() {
        Logger serverLog = Log4j2Helper.getLogger("$im_server");
        try {
            aioServer.start(serverConfig.getBindIp(), serverConfig.getBindPort());
            serverLog.info("Started success in: Host: {}, PID: {}", aioServer.getServerNode(), PropertiesHelper.getPID());
        } catch (IOException e) {
            serverLog.error(e.getMessage(), "Started fail because:" + e);
        }
    }

    public static void stop() {
        aioServer.stop();
    }

    public static void toAllUser(String msg) {
        Aio.sendToAll(serverGroupContext, PacketFactory.systemMsgToAllPacket(msg));
    }

    public static void toUser(String msg, String userId) {
        Aio.sendToUser(serverGroupContext, userId, PacketFactory.systemMsgToOnePacket(msg));
    }

    public static void toGroup(String msg, String toGroup) {
        Aio.sendToGroup(serverGroupContext, toGroup, PacketFactory.systemMsgToGroupPacket(msg, toGroup));
    }

    public static boolean isLogin(String userId) {
        return serverGroupContext.users.find(serverGroupContext, userId) != null;
    }

    public static ChannelContext getChannelContext(String userId) {
        for (ChannelContext next : serverGroupContext.connecteds.getObj()) {
            BaseSessionContext sessionContext = (BaseSessionContext) next.getAttribute();
            if (sessionContext.getUserId().equals(userId)) {
                return next;
            }
        }
        return null;
    }

    public static boolean isUserInGroup(String userId, String groupId) {
        SetWithLock<ChannelContext> clients1 = serverGroupContext.groups.clients(serverGroupContext, groupId);
        for (ChannelContext channelContext : clients1.getObj()) {
            if (channelContext.getUserid().equals(userId)) {
                return true;
            }
        }
        return false;
    }

    public static ServerGroupContext getServerGroupContext() {
        return serverGroupContext;
    }

    public static AioServer getAioServer() {
        return aioServer;
    }

    public static ServerConfig getServerConfig() {
        return serverConfig;
    }

    public static boolean isWebSocketProtocol() {
        return ServerConfig.PROTOCOL_WEBSOCKET.equalsIgnoreCase(serverConfig.getServerProtocol());
    }
}
