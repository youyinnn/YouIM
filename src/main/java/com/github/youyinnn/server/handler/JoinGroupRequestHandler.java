package com.github.youyinnn.server.handler;

import com.github.youyinnn.common.AbstractMsgHandler;
import com.github.youyinnn.common.Const;
import com.github.youyinnn.common.packet.BasePacket;
import com.github.youyinnn.common.packet.JoinGroupRequestBody;
import org.tio.core.Aio;
import org.tio.core.ChannelContext;
import org.tio.utils.json.Json;

/**
 * @author youyinnn
 */
public class JoinGroupRequestHandler extends AbstractMsgHandler<JoinGroupRequestBody> {

    @Override
    public Class<JoinGroupRequestBody> getMsgBodyClass() {
        return JoinGroupRequestBody.class;
    }

    @Override
    public Object handler(BasePacket packet, JoinGroupRequestBody baseMsgBody, ChannelContext channelContext) throws Exception {
        System.out.println("收到进群请求消息:" + Json.toJson(baseMsgBody));

        Aio.bindGroup(channelContext, baseMsgBody.getGroup());

        BasePacket responsePacket =
                BasePacket.joinGroupResponsePacket(Const.RequestCode.SUCCESS,"",baseMsgBody.getGroup());
        Aio.send(channelContext, responsePacket);
        return null;
    }
}
