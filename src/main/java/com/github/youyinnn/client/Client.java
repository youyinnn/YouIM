package com.github.youyinnn.client;

import com.github.youyinnn.def.client.MyClientAioHandler;
import com.github.youyinnn.def.client.MyClientAioListener;
import com.github.youyinnn.common.Const;
import com.github.youyinnn.common.packet.BasePacket;
import org.tio.client.AioClient;
import org.tio.client.ClientChannelContext;
import org.tio.client.ClientGroupContext;
import org.tio.client.ReconnConf;
import org.tio.client.intf.ClientAioHandler;
import org.tio.client.intf.ClientAioListener;
import org.tio.core.Aio;
import org.tio.core.Node;

import java.io.IOException;

/**
 * @author youyinnn
 */
public class Client {

    private static String serverIp = null;
    private static Integer serverPort = null;
    private static Node serverNode = null;
    private static ReconnConf reconnConf = null;

    private static ClientAioHandler clientAioHandler = null;
    private static AbstractClientAioListener clientAioListener = null;
    private static ClientGroupContext clientGroupContext = null;

    private static ClientChannelContext clientChannelContext = null;
    private static AioClient aioClient = null;
    private static String loginUserId = null;

    private Client() {
    }

    public static void init(String serverIp) {
        Client.serverIp = serverIp;
        init();
    }

    public static void init(int serverPort) {
        Client.serverPort = serverPort;
        init();
    }

    public static void init(String serverIp, int serverPort) {
        Client.serverIp = serverIp;
        Client.serverPort = serverPort;
        init();
    }

    public static void init(String serverIp, int serverPort, ClientAioHandler clientAioHandler) {
        Client.serverIp = serverIp;
        Client.serverPort = serverPort;
        Client.clientAioHandler = clientAioHandler;
        init();
    }

    public static void init(String serverIp, int serverPort, ClientAioHandler clientAioHandler, AbstractClientAioListener clientAioListener) {
        Client.serverIp = serverIp;
        Client.serverPort = serverPort;
        Client.clientAioHandler = clientAioHandler;
        Client.clientAioListener = clientAioListener;
        init();
    }

    public static void init() {
        if (serverIp == null) {
            serverIp = Const.Server.LOCAL_SERVER_IP;
        }
        if (serverPort == null) {
            serverPort = Const.Server.PORT;
        }
        if (serverNode == null) {
            serverNode = new Node(serverIp, serverPort);
        }
        if (reconnConf == null) {
            reconnConf = new ReconnConf(5000L);
        }
        if (clientAioHandler == null) {
            clientAioHandler = new MyClientAioHandler();
        }
        if (clientAioListener == null) {
            clientAioListener = new MyClientAioListener();
        }
        clientGroupContext = new ClientGroupContext(clientAioHandler, clientAioListener, reconnConf);
        try {
            aioClient = new AioClient(clientGroupContext);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void connect() {
        try {
            if (serverNode != null) {
                clientChannelContext = aioClient.connect(serverNode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stop() {
        aioClient.stop();
    }

    private static Boolean aioSend(BasePacket packet) {
        if (clientChannelContext != null) {
            return Aio.send(clientChannelContext, packet);
        }
        return false;
    }

    public static Boolean login(String userId) {
        Boolean send = aioSend(BasePacket.loginRequestPacket(userId));
        if (send) {
            loginUserId = userId;
        }
        return send;
    }

    public static Boolean join(String group, String fromUserId) {
        if (isLogin()) {
            return aioSend(BasePacket.joinGroupRequestPacket(group, fromUserId));
        } else {
            return false;
        }
    }

    public static Boolean p2g(String msg, String toGroup, String fromUserId) {
        if (isLogin()) {
            return aioSend(BasePacket.groupMsgRequestPacket(msg, toGroup, fromUserId));
        } else {
            return false;
        }
    }

    public static Boolean p2p(String msg, String toUserId, String fromUserId) {
        if (isLogin()) {
            return aioSend(BasePacket.p2pMsgRequestPacket(msg, toUserId, fromUserId));
        } else {
            return false;
        }
    }

    public static void setReconnConf(long interval) {
        reconnConf = new ReconnConf(interval);
    }

    public static void setClientAioHandler(ClientAioHandler clientAioHandler) {
        Client.clientAioHandler = clientAioHandler;
        if (clientGroupContext != null) {
            clientGroupContext.setClientAioHandler(clientAioHandler);
        }
    }

    public static void setClientAioListener(AbstractClientAioListener clientAioListener) {
        Client.clientAioListener = clientAioListener;
        if (clientGroupContext != null) {
            clientGroupContext.setClientAioListener(clientAioListener);
        }
    }

    public static String getServerIp() {
        return serverIp;
    }

    public static void setServerIp(String serverIp) {
        Client.serverIp = serverIp;
        if (serverNode != null) {
            serverNode.setIp(serverIp);
        }
    }

    public static int getServerPort() {
        return serverPort;
    }

    public static void setServerPort(int serverPort) {
        Client.serverPort = serverPort;
        if (serverNode != null) {
            serverNode.setPort(serverPort);
        }
    }

    public static Node getServerNode() {
        return serverNode;
    }

    public static ReconnConf getReconnConf() {
        return reconnConf;
    }

    public static ClientAioHandler getClientAioHandler() {
        return clientAioHandler;
    }

    public static ClientAioListener getClientAioListener() {
        return clientAioListener;
    }

    public static ClientGroupContext getClientGroupContext() {
        return clientGroupContext;
    }

    public static ClientChannelContext getClientChannelContext() {
        return clientChannelContext;
    }

    public static AioClient getAioClient() {
        return aioClient;
    }

    public static String getLoginUserId() {
        return loginUserId;
    }

    public static boolean isLogin() {
        return loginUserId != null;
    }
}
