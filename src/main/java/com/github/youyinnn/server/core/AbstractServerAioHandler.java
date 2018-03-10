package com.github.youyinnn.server.core;

import com.github.youyinnn.common.AbstractAioHandler;
import com.github.youyinnn.common.Const;
import com.github.youyinnn.common.MsgType;
import com.github.youyinnn.common.packet.*;
import org.tio.core.ChannelContext;
import org.tio.server.intf.ServerAioHandler;
import org.tio.utils.json.Json;

/**
 * @author youyinnn
 */
public abstract class AbstractServerAioHandler extends AbstractAioHandler implements ServerAioHandler {

    @Override
    public Object handler(BasePacket packet, ChannelContext channelContext) throws Exception {
        String jsonStr;
        byte msgType = packet.getMsgType();
        BaseBody baseMsgBody;
        jsonStr = new String(packet.getMsgBody(), Const.Handler.CHARSET);
        if (packet.getMsgBody() != null) {
            if (msgType == MsgType.LOGIN_REQ) {
                baseMsgBody = Json.toBean(jsonStr, LoginRequestBody.class);
                return loginRequestHandler(packet, (LoginRequestBody) baseMsgBody, channelContext);
            }
            if (msgType == MsgType.GROUP_MSG_REQ) {
                baseMsgBody = Json.toBean(jsonStr, GroupMsgRequestBody.class);
                return groupMsgRequestHandler(packet, (GroupMsgRequestBody) baseMsgBody, channelContext);
            }
            if (msgType == MsgType.JOIN_GROUP_REQ) {
                baseMsgBody = Json.toBean(jsonStr, JoinGroupRequestBody.class);
                return joinGroupRequestHandler(packet, (JoinGroupRequestBody) baseMsgBody, channelContext);
            }
            if (msgType == MsgType.P2P_REQ) {
                baseMsgBody = Json.toBean(jsonStr, P2PRequestBody.class);
                return p2pRequestHandler(packet, (P2PRequestBody) baseMsgBody, channelContext);
            }
            if (msgType == MsgType.HEART_BEAT_REQ) {
                baseMsgBody = Json.toBean(jsonStr, GroupMsgRequestBody.class);
                return heartbeatRequestHandler(packet, (GroupMsgRequestBody) baseMsgBody, channelContext);
            }
        }

        return null;
    }

    /**
     * 必须实现一个Token获取方法
     * @return
     */
    protected abstract String getToken();

    /**
     * 登陆请求处理
     *
     * @param packet
     * @param baseMsgBody
     * @param channelContext
     * @return
     */
    protected abstract Object loginRequestHandler(BasePacket packet, LoginRequestBody baseMsgBody, ChannelContext channelContext);

    /**
     * 加入群组请求处理
     *
     * @param packet
     * @param baseMsgBody
     * @param channelContext
     * @return
     */
    protected abstract Object joinGroupRequestHandler(BasePacket packet, JoinGroupRequestBody baseMsgBody, ChannelContext channelContext);

    /**
     * 点对点请求处理
     *
     * @param packet
     * @param baseMsgBody
     * @param channelContext
     * @return
     */
    protected abstract Object p2pRequestHandler(BasePacket packet, P2PRequestBody baseMsgBody, ChannelContext channelContext);

    /**
     * 组消息请求处理
     *
     * @param packet
     * @param baseMsgBody
     * @param channelContext
     * @return
     */
    protected abstract Object groupMsgRequestHandler(BasePacket packet, GroupMsgRequestBody baseMsgBody, ChannelContext channelContext);

    /**
     * 心跳请求处理
     *
     * @param packet
     * @param baseMsgBody
     * @param channelContext
     * @return
     */
    protected abstract Object heartbeatRequestHandler(BasePacket packet, GroupMsgRequestBody baseMsgBody, ChannelContext channelContext);
}
