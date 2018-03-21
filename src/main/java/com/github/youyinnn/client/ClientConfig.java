package com.github.youyinnn.client;

import org.tio.client.ReconnConf;
import org.tio.client.intf.ClientAioHandler;
import org.tio.client.intf.ClientAioListener;
import org.tio.core.Node;

/**
 * @author youyinnn
 */
public class ClientConfig {

    private String bindIp = null;
    private Integer bindPort = null;
    private Node serverNode = null;
    private ReconnConf reconnConf = null;
    private ClientAioHandler handler = null;
    private ClientAioListener listener = null;

    public ClientConfig(Integer bindPort) {
        this.bindPort = bindPort;
    }

    public ClientConfig(String bindIp, Integer bindPort) {
        this.bindIp = bindIp;
        this.bindPort = bindPort;
    }

    private ClientConfig(String bindIp, Integer bindPort, ClientAioHandler handler, ClientAioListener listener, long reConn) {
        this.bindIp = bindIp;
        this.bindPort = bindPort;
        this.handler = handler;
        this.listener = listener;
        if (reConn != 0) {
            this.reconnConf = new ReconnConf(reConn);
        }
    }

    private ClientConfig() {

    }

    public static ClientConfig getTcpClientConfig() {
        return new ClientConfig();
    }

    public static ClientConfig getTcpClientConfig(String bindIp, Integer bindPort, ClientAioHandler handler, ClientAioListener listener, long reConn) {
        return new ClientConfig(bindIp, bindPort, handler, listener, reConn);
    }

    public static ClientConfig getTcpClientConfig(Integer bindPort) {
        return getTcpClientConfig(null, bindPort, null, null, 0);
    }

    public static ClientConfig getTcpClientConfig(Integer bindPort, ClientAioHandler handler, ClientAioListener listener) {
        return getTcpClientConfig(null, bindPort, handler, listener, 0);
    }

    public static ClientConfig getTcpClientConfig(Integer bindPort, ClientAioHandler handler, ClientAioListener listener, long reConn) {
        return getTcpClientConfig(null, bindPort, handler, listener, reConn);
    }

    public String getBindIp() {
        return bindIp;
    }

    public void setBindIp(String bindIp) {
        this.bindIp = bindIp;
    }

    public Integer getBindPort() {
        return bindPort;
    }

    public void setBindPort(Integer bindPort) {
        this.bindPort = bindPort;
    }

    public Node getServerNode() {
        return serverNode;
    }

    public void setServerNode(Node serverNode) {
        this.serverNode = serverNode;
    }

    public ReconnConf getReconnConf() {
        return reconnConf;
    }

    public void setReconnConf(ReconnConf reconnConf) {
        this.reconnConf = reconnConf;
    }

    public ClientAioHandler getHandler() {
        return handler;
    }

    public void setHandler(ClientAioHandler handler) {
        this.handler = handler;
    }

    public ClientAioListener getListener() {
        return listener;
    }

    public void setListener(ClientAioListener listener) {
        this.listener = listener;
    }
}
