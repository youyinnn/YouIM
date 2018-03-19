package com.github.youyinnn.server;

import org.tio.core.intf.TioUuid;
import org.tio.server.intf.ServerAioHandler;
import org.tio.server.intf.ServerAioListener;

/**
 * @author youyinnn
 */
public class ServerConfig {

    private String groupContextName = null;

    private String bindIp = null;

    private Integer bindPort = null;

    private ServerAioHandler handler = null;

    private ServerAioListener listener = null;

    private TioUuid tioUuid;

    public ServerConfig(Integer bindPort, ServerAioHandler handler, ServerAioListener listener) {
        this.bindPort = bindPort;
        this.handler = handler;
        this.listener = listener;
    }

    public ServerConfig(String groupContextName) {
        this.groupContextName = groupContextName;
    }

    public ServerConfig(Integer bindPort) {
        this.bindPort = bindPort;
    }

    public ServerConfig(String bindIp, Integer bindPort) {
        this.bindIp = bindIp;
        this.bindPort = bindPort;
    }

    public ServerConfig() {
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

    public String getGroupContextName() {
        return groupContextName;
    }

    public void setGroupContextName(String groupContextName) {
        this.groupContextName = groupContextName;
    }

    public ServerAioHandler getHandler() {
        return handler;
    }

    public void setHandler(ServerAioHandler handler) {
        this.handler = handler;
    }

    public ServerAioListener getListener() {
        return listener;
    }

    public void setListener(ServerAioListener listener) {
        this.listener = listener;
    }

    public TioUuid getTioUuid() {
        return tioUuid;
    }

    public void setTioUuid(TioUuid tioUuid) {
        this.tioUuid = tioUuid;
    }
}
