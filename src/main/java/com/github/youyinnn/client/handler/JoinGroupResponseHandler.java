package com.github.youyinnn.client.handler;

import com.github.youyinnn.common.AbstractMsgHandler;
import com.github.youyinnn.common.packet.BasePacket;
import com.github.youyinnn.common.packet.JoinGroupResponseBody;
import org.tio.core.ChannelContext;
import org.tio.utils.json.Json;

/**
 * @author youyinnn
 */
public class JoinGroupResponseHandler extends AbstractMsgHandler<JoinGroupResponseBody> {

    @Override
    public Class<JoinGroupResponseBody> getMsgBodyClass() {
        return JoinGroupResponseBody.class;
    }

    @Override
    public Object handler(BasePacket packet, JoinGroupResponseBody baseMsgBody, ChannelContext channelContext) throws Exception {
        System.out.println("收到进群响应消息:" + Json.toJson(baseMsgBody));
        return null;
    }
}
