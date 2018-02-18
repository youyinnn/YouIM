package com.github.youyinnn.server.handler;

import com.github.youyinnn.common.AbstractMsgHandler;
import com.github.youyinnn.common.packet.BasePacket;
import com.github.youyinnn.common.packet.GroupMsgRequestBody;
import org.tio.core.ChannelContext;

/**
 * @author youyinnn
 */
public class HeartbeatRequestHandler extends AbstractMsgHandler<GroupMsgRequestBody>{

    @Override
    public Class<GroupMsgRequestBody> getMsgBodyClass() {
        return GroupMsgRequestBody.class;
    }

    @Override
    public Object handler(BasePacket packet, GroupMsgRequestBody baseMsgBody, ChannelContext channelContext) throws Exception {
        // 心跳消息 啥也不做
        return null;
    }
}
