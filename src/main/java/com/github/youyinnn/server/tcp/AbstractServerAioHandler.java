package com.github.youyinnn.server.tcp;

import com.github.youyinnn.common.AbstractAioHandler;
import com.github.youyinnn.common.intf.Const;
import com.github.youyinnn.common.intf.MsgType;
import com.github.youyinnn.common.packets.BasePacket;
import com.github.youyinnn.server.BasicImWorkflowHandler;
import org.tio.core.ChannelContext;
import org.tio.server.intf.ServerAioHandler;

/**
 * The type Abstract server aio handler.
 *
 * @author youyinnn
 */
public abstract class AbstractServerAioHandler extends AbstractAioHandler implements ServerAioHandler {

    @Override
    public Object handler(BasePacket packet, ChannelContext channelContext) throws Exception {
        String bodyJsonStr;
        byte msgType = packet.getMsgType();
        bodyJsonStr = new String(packet.getMsgBody(), Const.CHARSET);
        if (packet.getMsgBody() != null) {
            Boolean handle = true;
            if (msgType == MsgType.LOGIN_REQ) {
                beforeHandle(msgType);
                handle = BasicImWorkflowHandler.loginRequestHandle(bodyJsonStr, channelContext, getToken());
                afterHandled(msgType);
            } else if (msgType == MsgType.GROUP_MSG_REQ) {
                beforeHandle(msgType);
                BasicImWorkflowHandler.groupMsgRequestHandle(bodyJsonStr, channelContext);
                afterHandled(msgType);
            } else if (msgType == MsgType.JOIN_GROUP_REQ) {
                beforeHandle(msgType);
                handle = BasicImWorkflowHandler.joinGroupRequestHandle(bodyJsonStr, channelContext);
                afterHandled(msgType);
            } else if (msgType == MsgType.P2P_REQ) {
                beforeHandle(msgType);
                handle = BasicImWorkflowHandler.p2PMsgRequestHandle(bodyJsonStr, channelContext);
                afterHandled(msgType);
            } else if (msgType == MsgType.LOGOUT_REQ) {
                beforeHandle(msgType);
                BasicImWorkflowHandler.logoutRequestHandle(bodyJsonStr, channelContext);
                afterHandled(msgType);
            } else if (msgType == MsgType.QUIT_GROUP_REQ) {
                beforeHandle(msgType);
                BasicImWorkflowHandler.quitGroupRequestHandle(bodyJsonStr, channelContext);
                afterHandled(msgType);
            } else if (msgType == MsgType.HEART_BEAT_REQ) {
                handle = BasicImWorkflowHandler.heartbeatRequestHandler(bodyJsonStr, channelContext);
            }
            return handle;
        }
        return null;
    }



    /**
     * 必须实现一个Token获取方法
     *
     * @return token token
     */
    protected abstract String getToken();

    /**
     * Before handle.
     *
     * @param msgType the msg type
     */
    protected abstract void beforeHandle(byte msgType);

    /**
     * After handle.
     *
     * @param msgType the msg type
     */
    protected abstract void afterHandled(byte msgType);
}
