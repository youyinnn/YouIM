package com.github.youyinnn.demo.client;


import com.github.youyinnn.client.Client;
import com.github.youyinnn.client.ClientConfig;
import org.apache.commons.lang3.StringUtils;

/**
 * @author youyinnn
 */
public class MyClientStarter {

    private static String loginUserId;

    public static void main(String[] args) throws Exception {
        Client.init(new ClientConfig(5566));
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
        sb.append(i++ + "、退出群组，输入 'quit groupId'.\r\n");
        sb.append(i++ + "、群聊，输入 'p2g groupId msg'.\r\n");
        sb.append(i++ + "、点对点聊天，输入 'p2P userId msg'.\r\n");
        sb.append(i++ + "、登出，输入 'logout'.\r\n");

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
            Client.p2G(msg, group, loginUserId);
        } else if ("p2P".equalsIgnoreCase(command)) {
            String toUserId = args[1];
            String msg = args[2];
            Client.p2P(msg, toUserId, loginUserId);
        } else if ("quit".equalsIgnoreCase(command)) {
            String groupId = args[1];
            Client.quit(groupId);
        } else if ("logout".equalsIgnoreCase(command)) {
            Client.logout();
        }
    }

}
