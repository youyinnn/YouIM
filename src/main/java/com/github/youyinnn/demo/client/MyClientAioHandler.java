package com.github.youyinnn.demo.client;

import com.github.youyinnn.client.AbstractClientAioHandler;
import com.github.youyinnn.common.BaseSessionContext;
import com.github.youyinnn.common.packets.*;
import com.github.youyinnn.common.packets.response.JoinGroupResponseBody;
import com.github.youyinnn.common.packets.response.LoginResponseBody;
import com.github.youyinnn.common.packets.response.P2GResponseBody;
import com.github.youyinnn.common.packets.response.P2PResponseBody;
import org.tio.core.ChannelContext;
import org.tio.utils.json.Json;

/**
 * @author youyinnn
 */
public class MyClientAioHandler extends AbstractClientAioHandler {

    @Override
    protected Object s2PHandler(BasePacket packet, P2PResponseBody baseMsgBody, ChannelContext channelContext) {
        System.err.println("个人收到系统消息:" + Json.toJson(baseMsgBody));
        return null;
    }

    @Override
    protected Object s2GHandler(BasePacket packet, P2GResponseBody baseMsgBody, ChannelContext channelContext) {
        System.err.println("群组收到系统消息:" + Json.toJson(baseMsgBody));
        return null;
    }

    @Override
    protected Object loginResponseHandler(BasePacket packet, LoginResponseBody baseMsgBody, ChannelContext channelContext) {
        System.err.println("登陆收到响应:" + Json.toJson(baseMsgBody));
        String token = baseMsgBody.getToken();
        if (token != null) {
            BaseSessionContext sessionContext = (BaseSessionContext) channelContext.getAttribute();
            sessionContext.setToken(token);
            System.err.println("登录成功，token是:" + baseMsgBody.getToken());
        }
        return null;
    }

    @Override
    protected Object p2PResponseHandler(BasePacket packet, P2PResponseBody baseMsgBody, ChannelContext channelContext) {
        System.err.println("收到P2P响应:" + Json.toJson(baseMsgBody));
        return null;
    }

    @Override
    protected Object joinGroupResponseHandler(BasePacket packet, JoinGroupResponseBody baseMsgBody, ChannelContext channelContext) {
        System.err.println("收到进群响应:" + Json.toJson(baseMsgBody));
        return null;
    }

    @Override
    protected Object groupMsgResponseHandler(BasePacket packet, P2GResponseBody baseMsgBody, ChannelContext channelContext) {
        System.err.println("收到群组消息:" + Json.toJson(baseMsgBody));
        return null;
    }
}
