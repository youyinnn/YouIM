package com.github.youyinnn.common;

import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;

import java.util.List;

/**
 * @author youyinnn
 */
public class WsSessionContext extends BaseSessionContext {

    /**
     * 是否已经握过手
     */
    private boolean isHandshaked = false;

    /**
     * webSocket 握手请求包
     */
    private HttpRequest handshakeRequestPacket = null;

    /**
     * webSocket 握手响应包
     */
    private HttpResponse handshakeResponsePacket = null;

    /**
     * webSocket 协议用到的，有时候数据包是分几个到的，注意那个fin字段，本im暂时不支持
     */
    private List<byte[]> lastParts = null;

    public boolean isHandshaked() {
        return isHandshaked;
    }

    public void setHandshaked(boolean handshaked) {
        isHandshaked = handshaked;
    }

    public HttpRequest getHandshakeRequestPacket() {
        return handshakeRequestPacket;
    }

    public void setHandshakeRequestPacket(HttpRequest handshakeRequestPacket) {
        this.handshakeRequestPacket = handshakeRequestPacket;
    }

    public HttpResponse getHandshakeResponsePacket() {
        return handshakeResponsePacket;
    }

    public void setHandshakeResponsePacket(HttpResponse handshakeResponsePacket) {
        this.handshakeResponsePacket = handshakeResponsePacket;
    }

    public List<byte[]> getLastParts() {
        return lastParts;
    }

    public void setLastParts(List<byte[]> lastParts) {
        this.lastParts = lastParts;
    }
}
