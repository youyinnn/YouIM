package com.github.youyinnn.client;

import com.github.youyinnn.common.intf.Const;
import com.github.youyinnn.common.packets.BasePacket;
import com.github.youyinnn.common.utils.PacketFactory;
import com.github.youyinnn.demo.client.MyClientAioHandler;
import com.github.youyinnn.demo.client.MyClientAioListener;
import org.tio.client.AioClient;
import org.tio.client.ClientChannelContext;
import org.tio.client.ClientGroupContext;
import org.tio.client.ReconnConf;
import org.tio.core.Aio;
import org.tio.core.Node;

import java.io.IOException;

/**
 * @author youyinnn
 */
public class Client {

    private static ClientConfig clientConfig = null;
    private static ClientGroupContext clientGroupContext = null;

    private static ClientChannelContext clientChannelContext = null;
    private static AioClient aioClient = null;

    private static String loginUserId = null;

    private Client() {
    }

    public static void init() {
        if (clientConfig == null) {
            clientConfig = ClientConfig.getTcpClientConfig();
        }
        init(clientConfig);
    }

    public static void init(ClientConfig clientConfig) {
        Client.clientConfig = clientConfig;
        if (clientConfig.getBindIp() == null) {
            clientConfig.setBindIp(Const.Server.LOCAL_SERVER_IP);
        }
        if (clientConfig.getBindPort() == null) {
            clientConfig.setBindPort(Const.Server.PORT);
        }
        if (clientConfig.getServerNode() == null) {
            clientConfig.setServerNode(new Node(clientConfig.getBindIp(), clientConfig.getBindPort()));
        }
        if (clientConfig.getReconnConf() == null) {
            clientConfig.setReconnConf(new ReconnConf(5000L));
        }
        if (clientConfig.getHandler() == null) {
            clientConfig.setHandler(new MyClientAioHandler());
        }
        if (clientConfig.getListener() == null) {
            clientConfig.setListener(new MyClientAioListener());
        }
        clientGroupContext = new ClientGroupContext(clientConfig.getHandler(), clientConfig.getListener(), clientConfig.getReconnConf());
        try {
            aioClient = new AioClient(clientGroupContext);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void connect() {
        try {
            if (clientConfig.getServerNode() != null) {
                clientChannelContext = aioClient.connect(clientConfig.getServerNode());
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
        Boolean send = aioSend(PacketFactory.loginRequestPacket(userId));
        if (send) {
            loginUserId = userId;
        }
        return send;
    }

    public static Boolean join(String group, String fromUserId) {
        if (isLogin()) {
            return aioSend(PacketFactory.joinGroupRequestPacket(group, fromUserId));
        } else {
            return false;
        }
    }

    public static Boolean p2G(String msg, String toGroup, String fromUserId) {
        if (isLogin()) {
            return aioSend(PacketFactory.groupMsgRequestPacket(msg, toGroup, fromUserId));
        } else {
            return false;
        }
    }

    public static Boolean p2P(String msg, String toUserId, String fromUserId) {
        if (isLogin()) {
            return aioSend(PacketFactory.p2PMsgRequestPacket(msg, toUserId, fromUserId));
        } else {
            return false;
        }
    }

    public static Boolean logout() {
        if (isLogin()) {
            return aioSend(PacketFactory.logoutRequestPacket(loginUserId));
        } else {
            return false;
        }
    }

    public static Boolean quit(String groupId) {
        if (isLogin()) {
            return aioSend(PacketFactory.quitGroupRequestPacket(loginUserId, groupId));
        } else {
            return false;
        }
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

    public static ClientConfig getClientConfig() {
        return clientConfig;
    }
}
