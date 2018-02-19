package com.github.youyinnn.client;


import com.github.youyinnn.common.MsgType;
import com.github.youyinnn.common.packet.*;
import org.apache.commons.lang3.StringUtils;
import org.tio.core.Aio;
import org.tio.utils.json.Json;

/**
 * @author youyinnn
 */
public class MyClientStarter {

    public static void main(String[] args) throws Exception {
        Client.init(5556);
        Client.connect();
        command();
    }

    private static void command() throws Exception {
        java.util.Scanner sc = new java.util.Scanner(System.in);
        int i = 1;
        StringBuilder sb = new StringBuilder();
        sb.append("使用指南:\r\n");
        sb.append(i++ + "、需要帮助，输入 '?'.\r\n");
        sb.append(i++ + "、登录，输入 'login userId'.\r\n");
        sb.append(i++ + "、进入群组，输入 'join groupId'.\r\n");
        sb.append(i++ + "、群聊，输入 '2group groupId msg'.\r\n");
        sb.append(i++ + "、点对点聊天，输入 'p2p userId msg'.\r\n");

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

        Client.stop();
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

            Aio.send(Client.getClientChannelContext(), loginRequestPacket);
        } else if ("join".equalsIgnoreCase(command)) {
            String group = args[1];

            JoinGroupRequestBody joinGroupRequestBody = new JoinGroupRequestBody();
            joinGroupRequestBody.setGroup(group);

            BasePacket joinGroupRequestPacket = new BasePacket(MsgType.JOIN_GROUP_REQ, joinGroupRequestBody);
            Aio.send(Client.getClientChannelContext(), joinGroupRequestPacket);

        } else if ("2group".equalsIgnoreCase(command)) {
            String group = args[1];
            String msg = args[2];

            GroupMsgRequestBody groupMsgRequestBody = new GroupMsgRequestBody();
            groupMsgRequestBody.setMsg(msg);
            groupMsgRequestBody.setToGroup(group);

            BasePacket groupMsgRequestPacket = new BasePacket(MsgType.GROUP_MSG_REQ, groupMsgRequestBody);
            Aio.send(Client.getClientChannelContext(), groupMsgRequestPacket);

        } else if ("p2p".equalsIgnoreCase(command)) {
            String toUserId = args[1];
            String msg = args[2];

            P2PRequestBody p2PRequestBody = new P2PRequestBody();
            p2PRequestBody.setToUserId(toUserId);
            p2PRequestBody.setMsg(msg);

            BasePacket p2pRequestPacket = new BasePacket(MsgType.P2P_REQ,
                    Json.toJson(p2PRequestBody).getBytes(BasePacket.CHARSET));

            Aio.send(Client.getClientChannelContext(), p2pRequestPacket);
        }
    }

}
