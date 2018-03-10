package com.github.youyinnn.client.core;

import com.github.youyinnn.common.AbstractAioHandler;
import com.github.youyinnn.common.Const;
import com.github.youyinnn.common.MsgType;
import com.github.youyinnn.common.packet.*;
import org.tio.client.intf.ClientAioHandler;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;
import org.tio.utils.json.Json;

/**
 * @author youyinnn
 */
public abstract class AbstractClientAioHandler extends AbstractAioHandler implements ClientAioHandler {

    private static BasePacket heartbeatPacket = BasePacket.heartbeatRequestPacket();

    @Override
    public Packet heartbeatPacket() {
        return heartbeatPacket;
    }

    @Override
    public Object handler(BasePacket packet, ChannelContext channelContext) throws Exception {
        String jsonStr;
        byte msgType = packet.getMsgType();
        BaseBody baseMsgBody;
        jsonStr = new String(packet.getMsgBody(), Const.Handler.CHARSET);
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
            if (msgType == MsgType.GROUP_MSG_RESP) {
                baseMsgBody = Json.toBean(jsonStr, GroupMsgResponseBody.class);
                return groupMsgResponseHandler(packet, (GroupMsgResponseBody) baseMsgBody, channelContext);
            }
            if (msgType == MsgType.SYS_MSG_2ONE || msgType == MsgType.SYS_MSG_2ALL || msgType == MsgType.SYS_MSG_2GROUP) {
                baseMsgBody = Json.toBean(jsonStr, P2PResponseBody.class);
                return sysMsgHandler(packet, (P2PResponseBody) baseMsgBody, channelContext);
            }
        }

        return null;
    }

    /**
     * 系统消息处理
     *
     * @param packet
     * @param baseMsgBody
     * @param channelContext
     * @return
     */
    protected abstract Object sysMsgHandler(BasePacket packet, P2PResponseBody baseMsgBody, ChannelContext channelContext);

    /**
     * 登陆响应处理
     *
     * @param packet
     * @param baseMsgBody
     * @param channelContext
     * @return
     */
    protected abstract Object loginResponseHandler(BasePacket packet, LoginResponseBody baseMsgBody, ChannelContext channelContext) ;

    /**
     * 点对点响应处理
     *
     * @param packet
     * @param baseMsgBody
     * @param channelContext
     * @return
     */
    protected abstract Object p2PResponseHandler(BasePacket packet, P2PResponseBody baseMsgBody, ChannelContext channelContext) ;

    /**
     * 群组加入响应处理
     *
     * @param packet
     * @param baseMsgBody
     * @param channelContext
     * @return
     */
    protected abstract Object joinGroupResponseHandler(BasePacket packet, JoinGroupResponseBody baseMsgBody, ChannelContext channelContext) ;

    /**
     * 群组消息响应处理
     *
     * @param packet
     * @param baseMsgBody
     * @param channelContext
     * @return
     */
    protected abstract Object groupMsgResponseHandler(BasePacket packet, GroupMsgResponseBody baseMsgBody, ChannelContext channelContext) ;
}
