package com.github.youyinnn.server.handler;

import com.github.youyinnn.common.AbstractMsgHandler;
import com.github.youyinnn.common.BaseSessionContext;
import com.github.youyinnn.common.MsgType;
import com.github.youyinnn.common.packet.BasePacket;
import com.github.youyinnn.common.packet.P2PRequestBody;
import com.github.youyinnn.common.packet.P2PResponseBody;
import org.tio.core.Aio;
import org.tio.core.ChannelContext;
import org.tio.utils.json.Json;

/**
 * @author youyinnn
 */
public class P2PRequestHandler extends AbstractMsgHandler<P2PRequestBody> {

    @Override
    public Class<P2PRequestBody> getMsgBodyClass() {
        return P2PRequestBody.class;
    }

    @Override
    public Object handler(BasePacket packet, P2PRequestBody baseMsgBody, ChannelContext channelContext) throws Exception {
        System.out.println("收到点对点请求消息:{}" + Json.toJson(baseMsgBody));

        BaseSessionContext sessionContext = (BaseSessionContext) channelContext.getAttribute();
        P2PResponseBody responseBody = new P2PResponseBody();
        responseBody.setFromUserId(sessionContext.getUserId());
        responseBody.setMsg(baseMsgBody.getMsg());

        BasePacket responsePacket = new BasePacket(MsgType.P2P_RESP, responseBody);
        Aio.sendToUser(channelContext.getGroupContext(), baseMsgBody.getToUserId(), responsePacket);

        return null;
    }
}
