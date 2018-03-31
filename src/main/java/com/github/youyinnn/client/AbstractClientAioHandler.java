package com.github.youyinnn.client;

import com.github.youyinnn.common.AbstractAioHandler;
import com.github.youyinnn.common.intf.Const;
import com.github.youyinnn.common.intf.MsgType;
import com.github.youyinnn.common.packets.*;
import com.github.youyinnn.common.utils.PacketFactory;
import org.tio.client.intf.ClientAioHandler;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.utils.json.Json;

/**
 * The type Abstract client aio handler.
 *
 * @author youyinnn
 */
public abstract class AbstractClientAioHandler extends AbstractAioHandler implements ClientAioHandler {

    private static BasePacket heartbeatPacket = PacketFactory.heartbeatRequestPacket();

    @Override
    public Packet heartbeatPacket() {
        return heartbeatPacket;
    }

    @Override
    public Object handler(BasePacket packet, ChannelContext channelContext) throws Exception {
        String jsonStr;
        byte msgType = packet.getMsgType();
        BaseBody baseMsgBody;
        jsonStr = new String(packet.getMsgBody(), Const.CHARSET);
        if (packet.getMsgBody() != null) {
            if (msgType == MsgType.LOGIN_RESP) {
                baseMsgBody = Json.toBean(jsonStr, LoginResponseBody.class);
                return loginResponseHandler(packet, (LoginResponseBody) baseMsgBody, channelContext);
            }
            if (msgType == MsgType.P2P_RESP) {
                baseMsgBody = Json.toBean(jsonStr, P2PResponseBody.class);
                return p2PResponseHandler(packet, (P2PResponseBody) baseMsgBody, channelContext);
            }
            if (msgType == MsgType.JOIN_GROUP_RESP) {
                baseMsgBody = Json.toBean(jsonStr, JoinGroupResponseBody.class);
                return joinGroupResponseHandler(packet, (JoinGroupResponseBody) baseMsgBody, channelContext);
            }
            if (msgType == MsgType.P2G_RESP) {
                baseMsgBody = Json.toBean(jsonStr, P2GResponseBody.class);
                return groupMsgResponseHandler(packet, (P2GResponseBody) baseMsgBody, channelContext);
            }
            if (msgType == MsgType.SYS_MSG_2ONE || msgType == MsgType.SYS_MSG_2ALL) {
                baseMsgBody = Json.toBean(jsonStr, P2PResponseBody.class);
                return s2PHandler(packet, (P2PResponseBody) baseMsgBody, channelContext);
            }
            if (msgType == MsgType.SYS_MSG_2GROUP) {
                baseMsgBody = Json.toBean(jsonStr, P2GResponseBody.class);
                return s2GHandler(packet, (P2GResponseBody) baseMsgBody, channelContext);
            }
        }

        return null;
    }

    /**
     * 系统-用户消息处理
     *
     * @param packet         the packets
     * @param baseMsgBody    the base msg body
     * @param channelContext the channel context
     * @return the object
     */
    protected abstract Object s2PHandler(BasePacket packet, P2PResponseBody baseMsgBody, ChannelContext channelContext) ;

    /**
     * 系统-群组消息处理
     *
     * @param packet         the packets
     * @param baseMsgBody    the base msg body
     * @param channelContext the channel context
     * @return the object
     */
    protected abstract Object s2GHandler(BasePacket packet, P2GResponseBody baseMsgBody, ChannelContext channelContext) ;

    /**
     * 登陆响应处理
     *
     * @param packet         the packets
     * @param baseMsgBody    the base msg body
     * @param channelContext the channel context
     * @return object
     */
    protected abstract Object loginResponseHandler(BasePacket packet, LoginResponseBody baseMsgBody, ChannelContext channelContext) ;

    /**
     * 点对点响应处理
     *
     * @param packet         the packets
     * @param baseMsgBody    the base msg body
     * @param channelContext the channel context
     * @return object
     */
    protected abstract Object p2PResponseHandler(BasePacket packet, P2PResponseBody baseMsgBody, ChannelContext channelContext) ;

    /**
     * 群组加入响应处理
     *
     * @param packet         the packets
     * @param baseMsgBody    the base msg body
     * @param channelContext the channel context
     * @return object
     */
    protected abstract Object joinGroupResponseHandler(BasePacket packet, JoinGroupResponseBody baseMsgBody, ChannelContext channelContext) ;

    /**
     * 群组消息响应处理
     *
     * @param packet         the packets
     * @param baseMsgBody    the base msg body
     * @param channelContext the channel context
     * @return object
     */
    protected abstract Object groupMsgResponseHandler(BasePacket packet, P2GResponseBody baseMsgBody, ChannelContext channelContext) ;
}
