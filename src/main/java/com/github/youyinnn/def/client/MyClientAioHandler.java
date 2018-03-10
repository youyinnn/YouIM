package com.github.youyinnn.def.client;

import com.github.youyinnn.client.core.AbstractClientAioHandler;
import com.github.youyinnn.common.BaseSessionContext;
import com.github.youyinnn.common.packet.*;
import org.tio.core.ChannelContext;
import org.tio.utils.json.Json;

/**
 * @author youyinnn
 */
public class MyClientAioHandler extends AbstractClientAioHandler {

    @Override
    protected Object sysMsgHandler(BasePacket packet, P2PResponseBody baseMsgBody, ChannelContext channelContext) {
        System.out.println("收到系统消息:" + Json.toJson(baseMsgBody));
        return null;
    }

    @Override
    protected Object loginResponseHandler(BasePacket packet, LoginResponseBody baseMsgBody, ChannelContext channelContext) {
        System.out.println("登陆收到响应:" + Json.toJson(baseMsgBody));
        String token = baseMsgBody.getToken();
        if (token != null) {
            BaseSessionContext sessionContext = (BaseSessionContext) channelContext.getAttribute();
            sessionContext.setToken(token);
            System.out.println("登录成功，token是:" + baseMsgBody.getToken());
        }
        return null;
    }

    @Override
    protected Object p2PResponseHandler(BasePacket packet, P2PResponseBody baseMsgBody, ChannelContext channelContext) {
        System.out.println("收到P2P响应:" + Json.toJson(baseMsgBody));
        return null;
    }

    @Override
    protected Object joinGroupResponseHandler(BasePacket packet, JoinGroupResponseBody baseMsgBody, ChannelContext channelContext) {
        System.out.println("收到进群响应:" + Json.toJson(baseMsgBody));
        return null;
    }

    @Override
    protected Object groupMsgResponseHandler(BasePacket packet, GroupMsgResponseBody baseMsgBody, ChannelContext channelContext) {
        System.out.println("收到群组消息:" + Json.toJson(baseMsgBody));
        return null;
    }
}
