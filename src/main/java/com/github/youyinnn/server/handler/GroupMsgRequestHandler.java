package com.github.youyinnn.server.handler;

import com.github.youyinnn.common.AbstractMsgHandler;
import com.github.youyinnn.common.BaseSessionContext;
import com.github.youyinnn.common.packet.BasePacket;
import com.github.youyinnn.common.packet.GroupMsgRequestBody;
import org.tio.core.Aio;
import org.tio.core.ChannelContext;
import org.tio.utils.json.Json;

/**
 * @author youyinnn
 */
public class GroupMsgRequestHandler extends AbstractMsgHandler<GroupMsgRequestBody> {

    @Override
    public Class<GroupMsgRequestBody> getMsgBodyClass() {
        return GroupMsgRequestBody.class;
    }

    @Override
    public Object handler(BasePacket packet, GroupMsgRequestBody baseMsgBody, ChannelContext channelContext) throws Exception {
        System.out.println("收到群聊请求消息:" + Json.toJson(baseMsgBody));

        BaseSessionContext sessionContext = (BaseSessionContext) channelContext.getAttribute();

        BasePacket responsePacket =
                BasePacket.groupMsgResponsePacket(baseMsgBody.getMsg(), sessionContext.getUserId(), baseMsgBody.getToGroup());
        Aio.sendToGroup(channelContext.getGroupContext(), baseMsgBody.getToGroup(), responsePacket);
        return null;
    }
}
