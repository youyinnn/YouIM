package com.github.youyinnn.def.client;


import com.github.youyinnn.client.core.Client;
import org.apache.commons.lang3.StringUtils;

/**
 * @author youyinnn
 */
public class MyClientStarter {

    private static String loginUserId;

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
        sb.append(i++ + "、群聊，输入 'p2g groupId msg'.\r\n");
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
            loginUserId = userId;
            Client.login(userId);
        } else if ("join".equalsIgnoreCase(command)) {
            String group = args[1];
            Client.join(group, loginUserId);
        } else if ("p2g".equalsIgnoreCase(command)) {
            String group = args[1];
            String msg = args[2];
            Client.p2g(msg, group, loginUserId);
        } else if ("p2p".equalsIgnoreCase(command)) {
            String toUserId = args[1];
            String msg = args[2];
            Client.p2p(msg, toUserId, loginUserId);
        }
    }

}
