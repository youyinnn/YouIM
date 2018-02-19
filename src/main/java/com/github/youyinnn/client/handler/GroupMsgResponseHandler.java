package com.github.youyinnn.client.handler;

import com.github.youyinnn.common.AbstractMsgHandler;
import com.github.youyinnn.common.packet.BasePacket;
import com.github.youyinnn.common.packet.GroupMsgResponseBody;
import org.tio.core.ChannelContext;
import org.tio.utils.json.Json;

/**
 * @author youyinnn
 */
public class GroupMsgResponseHandler extends AbstractMsgHandler<GroupMsgResponseBody> {

    @Override
    public Class<GroupMsgResponseBody> getMsgBodyClass() {
        return GroupMsgResponseBody.class;
    }

    @Override
    public Object handler(BasePacket packet, GroupMsgResponseBody baseMsgBody, ChannelContext channelContext) throws Exception {
        System.out.println("收到群组消息:" + Json.toJson(baseMsgBody));
        return null;
    }
}
