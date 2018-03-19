package com.github.youyinnn.demo.server;

import com.github.youyinnn.server.Server;
import com.github.youyinnn.server.ServerConfig;
import org.apache.commons.lang3.StringUtils;

/**
 * @author youyinnn
 */
public class MyServerStarter {

    public static void main(String[] args) {
        Server.enableAllLogEnabled();
        Server.init(new ServerConfig(5578));
        Server.start();
        command();
    }

    private static void command() {
        java.util.Scanner sc = new java.util.Scanner(System.in);
        int i = 1;
        StringBuilder sb = new StringBuilder();
        sb.append("使用指南:\r\n");
        sb.append(i++ + "、给点发消息，输入 '2p userId msg'.\r\n");
        sb.append(i++ + "、给所有点发消息，输入 '2ps msg'.\r\n");
        sb.append(i++ + "、给某群组发消息，输入 '2g toGroup msg'.\r\n");
        sb.append(i++ + "、查找某用户是否登陆，输入 'ing userId'.\r\n");

        sb.append(i++ + "、退出程序，输入 'exit'.\r\n");

        System.out.println(sb);

        String line = sc.nextLine();
        while (true) {
            if ("exit".equalsIgnoreCase(line)) {
                System.out.println("谢谢使用,再见!.");
                break;
            }
            processCommand(line);
            line = sc.nextLine();
        }

        System.exit(0);
    }

    private static void processCommand(String line) {
        if (StringUtils.isBlank(line)) {
            return;
        }

        String[] args = StringUtils.split(line, " ");
        String command = args[0];

        if ("2p".equalsIgnoreCase(command)) {
            String userId = args[1];
            String msg = args[2];
            Server.toUser(msg, userId);
        } else if ("2ps".equalsIgnoreCase(command)) {
            String msg = args[1];
            Server.toAllUser(msg);
        } else if ("2g".equalsIgnoreCase(command)) {
            String toGroup = args[1];
            String msg = args[2];
            Server.toGroup(msg, toGroup);
        } else if ("ing".equalsIgnoreCase(command)) {
            String userId = args[1];
            Server.isLogin(userId);
        }
    }

}
