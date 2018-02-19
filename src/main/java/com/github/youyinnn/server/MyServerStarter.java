package com.github.youyinnn.server;

import java.io.IOException;

/**
 * @author youyinnn
 */
public class MyServerStarter {

    public static void main(String[] args) throws IOException {
        Server.init();
        Server.setServerPort(5556);
        Server.start();
    }

}
