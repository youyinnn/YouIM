package com.github.youyinnn.client;


import com.github.youyinnn.common.Const;
import com.github.youyinnn.common.MsgType;
import com.github.youyinnn.common.packet.BasePacket;
import com.github.youyinnn.common.packet.LoginRequestBody;
import com.github.youyinnn.common.packet.P2PRequestBody;
import org.apache.commons.lang3.StringUtils;
import org.tio.client.AioClient;
import org.tio.client.ClientChannelContext;
import org.tio.client.ClientGroupContext;
import org.tio.client.ReconnConf;
import org.tio.client.intf.ClientAioHandler;
import org.tio.client.intf.ClientAioListener;
import org.tio.core.Aio;
import org.tio.core.Node;
import org.tio.utils.json.Json;

/**
 * @author youyinnn
 */
public class MyClientStarter {

    private static String serverIp = Const.Server.LOCAL_SERVER_IP;
    private static int serverPort = Const.Server.PORT;

    private static Node serverNode = new Node(serverIp, serverPort);
    private static ReconnConf reconnConf = new ReconnConf(3000L);

    private static ClientAioHandler clientAioHandler = new MyClientAioHandler();
    private static ClientAioListener clientAioListener = new MyClientAioListener();
    private static ClientGroupContext clientGroupContext = new ClientGroupContext(clientAioHandler, clientAioListener, reconnConf);

    private static AioClient aioClient = null;

    private static ClientChannelContext clientChannelContext;

    public static void main(String[] args) throws Exception {
        aioClient = new AioClient(clientGroupContext);
        clientChannelContext = aioClient.connect(serverNode);
        command();
    }

    private static void command() throws Exception {
        java.util.Scanner sc = new java.util.Scanner(System.in);
        int i = 1;
        StringBuilder sb = new StringBuilder();
        sb.append("使用指南:\r\n");
        sb.append(i++ + "、需要帮助，输入 '?'.\r\n");
        sb.append(i++ + "、登录，输入 'login userId'.\r\n");
        sb.append(i++ + "、点对点聊天，输入 'p2p userId text'.\r\n");

        sb.append(i++ + "、退出程序，输入 'exit'.\r\n");

        System.out.println(sb);

        String line = sc.nextLine();
        while (true) {
            if ("exit".equalsIgnoreCase(line)) {
                System.out.println("谢谢使用,再见!.");
                break;
            } else if ("?".equals(line)) {
                System.out.println(sb);
            }

            processCommand(line);
            line = sc.nextLine();
        }

        aioClient.stop();
        System.exit(0);
    }

    private static void processCommand(String line) throws Exception {
        if (StringUtils.isBlank(line)) {
            return;
        }

        String[] args = StringUtils.split(line, " ");
        String command = args[0];

        if ("login".equalsIgnoreCase(command)) {
            String userId = args[1];

            LoginRequestBody loginRequestBody = new LoginRequestBody();
            loginRequestBody.setLoginUserId(userId);

            BasePacket loginRequestPacket = new BasePacket(MsgType.LOGIN_REQ,
                    Json.toJson(loginRequestBody).getBytes(BasePacket.CHARSET));

            Aio.send(clientChannelContext, loginRequestPacket);
        } else if ("p2p".equalsIgnoreCase(command)) {
            String toUserId = args[1];
            String msg = args[2];

            P2PRequestBody p2PRequestBody = new P2PRequestBody();
            p2PRequestBody.setToUserId(toUserId);
            p2PRequestBody.setMsg(msg);

            BasePacket p2pRequestPacket = new BasePacket(MsgType.P2P_REQ,
                    Json.toJson(p2PRequestBody).getBytes(BasePacket.CHARSET));

            Aio.send(clientChannelContext, p2pRequestPacket);
        }
    }

}
