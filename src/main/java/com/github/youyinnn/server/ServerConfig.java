package com.github.youyinnn.server;

import org.tio.core.intf.TioUuid;
import org.tio.server.intf.ServerAioHandler;
import org.tio.server.intf.ServerAioListener;

/**
 * @author youyinnn
 */
public class ServerConfig {

    public static final String PROTOCOL_TCP = "tcp";

    public static final String PROTOCOL_WEBSOCKET = "webSocket";

    private String groupContextName = "def_context";

    private String bindIp = null;

    private Integer bindPort = null;

    private ServerAioHandler handler = null;

    private ServerAioListener listener = null;

    private TioUuid tioUuid = null;

    private String serverProtocol = PROTOCOL_TCP;

    public ServerConfig(Integer bindPort) {
        this.bindPort = bindPort;
    }

    private ServerConfig(String bindIp, Integer bindPort, ServerAioHandler handler, ServerAioListener listener) {
        this.bindIp = bindIp;
        this.bindPort = bindPort;
        this.handler = handler;
        this.listener = listener;
    }

    private ServerConfig() {

    }

    private ServerConfig(String serverProtocol) {
        this.serverProtocol = serverProtocol;
    }

    public static ServerConfig getTcpServerConfig(){
        return new ServerConfig();
    }

    public static ServerConfig getTcpServerConfig(String bindIp, Integer bindPort, ServerAioHandler handler, ServerAioListener listener){
        return new ServerConfig(bindIp ,bindPort, handler, listener);
    }

    public static ServerConfig getTcpServerConfig(Integer bindPort){
        return new ServerConfig(null, bindPort, null, null);
    }

    public static ServerConfig getTcpServerConfig(String bindIp, Integer bindPort){
        return new ServerConfig(bindIp, bindPort, null, null);
    }

    public static ServerConfig getWsServerConfig(String bindIp, Integer bindPort, ServerAioHandler handler, ServerAioListener listener){
        ServerConfig serverConfig = new ServerConfig(PROTOCOL_WEBSOCKET);
        serverConfig.setBindIp(bindIp);
        serverConfig.setBindPort(bindPort);
        serverConfig.setHandler(handler);
        serverConfig.setListener(listener);
        return serverConfig;
    }

    public static ServerConfig getWsServerConfig(Integer bindPort) {
        return getWsServerConfig(null, bindPort, null, null);
    }

    public static ServerConfig getWsServerConfig(Integer bindPort, ServerAioHandler handler, ServerAioListener listener) {
        return getWsServerConfig(null, bindPort, handler, listener);
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

    public String getServerProtocol() {
        return serverProtocol;
    }
}
