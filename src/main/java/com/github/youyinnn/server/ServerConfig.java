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

    private String sqlPropertiesFile = "mysql.properties";

    private boolean useUserManagementModule = true;

    public ServerConfig(Integer bindPort) {
        this.bindPort = bindPort;
    }

    public ServerConfig(String bindIp, Integer bindPort) {
        this.bindIp = bindIp;
        this.bindPort = bindPort;
    }

    private ServerConfig(String bindIp, Integer bindPort, ServerAioHandler handler, ServerAioListener listener, String sqlPropertiesFile) {
        this.bindIp = bindIp;
        this.bindPort = bindPort;
        this.handler = handler;
        this.listener = listener;
        if (sqlPropertiesFile != null) {
            this.sqlPropertiesFile = sqlPropertiesFile;
        }
    }

    private ServerConfig() {

    }

    private ServerConfig(String serverProtocol) {
        this.serverProtocol = serverProtocol;
    }

    public static ServerConfig getTcpServerConfig(){
        return new ServerConfig();
    }

    public static ServerConfig getTcpServerConfig(String bindIp, Integer bindPort, ServerAioHandler handler, ServerAioListener listener, String sqlPropertiesFile){
        return new ServerConfig(bindIp ,bindPort, handler, listener, sqlPropertiesFile);
    }

    public static ServerConfig getWsServerConfig(String bindIp, Integer bindPort, ServerAioHandler handler, ServerAioListener listener, String sqlPropertiesFile){
        ServerConfig serverConfig = new ServerConfig(PROTOCOL_WEBSOCKET);
        serverConfig.setBindIp(bindIp);
        serverConfig.setBindPort(bindPort);
        serverConfig.setHandler(handler);
        serverConfig.setListener(listener);
        if (sqlPropertiesFile != null) {
            serverConfig.setSqlPropertiesFile(sqlPropertiesFile);
        }
        return serverConfig;
    }

    public static ServerConfig getTcpServerConfig(Integer bindPort){
        return getTcpServerConfig(null, bindPort, null, null, null);
    }

    public static ServerConfig getTcpServerConfig(Integer bindPort, String sqlPropertiesFile){
        return getTcpServerConfig(null, bindPort, null, null, sqlPropertiesFile);
    }

    public static ServerConfig getTcpServerConfig(String bindIp, Integer bindPort){
        return getTcpServerConfig(bindIp, bindPort, null, null, null);
    }

    public static ServerConfig getTcpServerConfig(String bindIp, Integer bindPort, String sqlPropertiesFile){
        return getTcpServerConfig(bindIp, bindPort, null, null, sqlPropertiesFile);
    }

    public static ServerConfig getWsServerConfig(Integer bindPort) {
        return getWsServerConfig(null, bindPort, null, null, null);
    }

    public static ServerConfig getWsServerConfig(Integer bindPort, String sqlPropertiesFile) {
        return getWsServerConfig(null, bindPort, null, null, sqlPropertiesFile);
    }

    public static ServerConfig getWsServerConfig(Integer bindPort, ServerAioHandler handler, ServerAioListener listener) {
        return getWsServerConfig(null, bindPort, handler, listener, null);
    }

    public static ServerConfig getWsServerConfig(Integer bindPort, ServerAioHandler handler, ServerAioListener listener, String sqlPropertiesFile) {
        return getWsServerConfig(null, bindPort, handler, listener, sqlPropertiesFile);
    }

    public static ServerConfig getWsServerConfig() {
        return getWsServerConfig(null, null, null, null, null);
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

    public String getSqlPropertiesFile() {
        return sqlPropertiesFile;
    }

    public void setSqlPropertiesFile(String sqlPropertiesFile) {
        this.sqlPropertiesFile = sqlPropertiesFile;
    }

    public boolean isUserManagementModuleEnabled() {
        return useUserManagementModule;
    }

    public void disableUserManagementModule() {
        this.useUserManagementModule = false;
    }
}
