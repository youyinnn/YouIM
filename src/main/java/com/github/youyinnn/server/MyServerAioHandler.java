package com.github.youyinnn.server;

import com.github.youyinnn.common.AbstractAioHandler;
import com.github.youyinnn.common.AbstractMsgHandler;
import com.github.youyinnn.common.MsgType;
import com.github.youyinnn.common.packet.BasePacket;
import com.github.youyinnn.server.handler.HeartbeatRequestHandler;
import com.github.youyinnn.server.handler.LoginRequestHandler;
import com.github.youyinnn.server.handler.P2PRequestHandler;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.server.intf.ServerAioHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author youyinnn
 */
public class MyServerAioHandler extends AbstractAioHandler implements ServerAioHandler {

    private static Map<Byte, AbstractMsgHandler<?>> handlerMap = new HashMap<>();

    static {
        handlerMap.put(MsgType.LOGIN_REQ, new LoginRequestHandler());
        handlerMap.put(MsgType.P2P_REQ, new P2PRequestHandler());
        handlerMap.put(MsgType.HEART_BEAT_REQ, new HeartbeatRequestHandler());
    }

    @Override
    public void handler(Packet packet, ChannelContext channelContext) throws Exception {
        BasePacket basePacket = (BasePacket) packet;
        Byte type = basePacket.getMsgType();
        AbstractMsgHandler<?> msgHandler = handlerMap.get(type);
        if (msgHandler != null) {
            msgHandler.handler(basePacket,channelContext);
        }
    }
}
