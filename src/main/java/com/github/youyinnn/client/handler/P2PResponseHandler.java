package com.github.youyinnn.client.handler;

import com.github.youyinnn.common.AbstractMsgHandler;
import com.github.youyinnn.common.packet.BasePacket;
import com.github.youyinnn.common.packet.P2PResponseBody;
import org.tio.core.ChannelContext;
import org.tio.utils.json.Json;

/**
 * @author youyinnn
 */
public class P2PResponseHandler extends AbstractMsgHandler<P2PResponseBody> {

    @Override
    public Class<P2PResponseBody> getMsgBodyClass() {
        return P2PResponseBody.class;
    }

    @Override
    public Object handler(BasePacket packet, P2PResponseBody baseMsgBody, ChannelContext channelContext) throws Exception {
        System.out.println("收到P2P响应消息:" + Json.toJson(baseMsgBody));
        return null;
    }
}
