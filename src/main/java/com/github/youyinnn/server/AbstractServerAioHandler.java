package com.github.youyinnn.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.youyinnn.common.AbstractAioHandler;
import com.github.youyinnn.common.Const;
import com.github.youyinnn.common.MsgType;
import com.github.youyinnn.common.packet.*;
import com.github.youyinnn.server.service.GroupCheckService;
import com.github.youyinnn.youdbutils.exceptions.AutowiredException;
import com.github.youyinnn.youdbutils.ioc.YouServiceIocContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.tio.core.ChannelContext;
import org.tio.server.intf.ServerAioHandler;
import org.tio.utils.json.Json;

/**
 * @author youyinnn
 */
public abstract class AbstractServerAioHandler extends AbstractAioHandler implements ServerAioHandler {

    private static GroupCheckService service;
    private static final Logger SERVER_LOG = LogManager.getLogger("$im_server");

    static {
        try {
            service = (GroupCheckService) YouServiceIocContainer.getYouService(GroupCheckService.class);
        } catch (AutowiredException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object handler(BasePacket packet, ChannelContext channelContext) throws Exception {
        String bodyJsonStr;
        byte msgType = packet.getMsgType();
        BaseBody baseMsgBody;
        bodyJsonStr = new String(packet.getMsgBody(), Const.Handler.CHARSET);
        if (packet.getMsgBody() != null) {
            boolean handler = false;
            JSONObject bodyJsonObj = JSON.parseObject(bodyJsonStr);
            if (msgType == MsgType.LOGIN_REQ) {
                baseMsgBody = Json.toBean(bodyJsonStr, LoginRequestBody.class);
                LoginRequestBody loginRequestBody = (LoginRequestBody) baseMsgBody;
                SERVER_LOG.info("登陆请求: userId:{}.",loginRequestBody.getLoginUserId());
                handler = loginRequestHandler(packet, loginRequestBody, channelContext);
                String userId = bodyJsonObj.getString("loginUserId");
                String groupJsonStr = service.getGroupJsonStr(userId);
            }
            if (msgType == MsgType.GROUP_MSG_REQ) {
                baseMsgBody = Json.toBean(bodyJsonStr, GroupMsgRequestBody.class);
                GroupMsgRequestBody groupMsgRequestBody = (GroupMsgRequestBody) baseMsgBody;
                SERVER_LOG.info("群组消息请求: fromUserId:{}, toGroup:{}, msg:{}.",
                        groupMsgRequestBody.getFromUserId(),
                        groupMsgRequestBody.getToGroup(),
                        groupMsgRequestBody.getMsg());
                handler = groupMsgRequestHandler(packet, groupMsgRequestBody, channelContext);
            }
            if (msgType == MsgType.JOIN_GROUP_REQ) {
                baseMsgBody = Json.toBean(bodyJsonStr, JoinGroupRequestBody.class);
                JoinGroupRequestBody joinGroupRequestBody = (JoinGroupRequestBody) baseMsgBody;
                SERVER_LOG.info("加群请求: fromUserId:{}, toGroup:{}.",
                        joinGroupRequestBody.getFromUserId(),
                        joinGroupRequestBody.getGroup());
                handler = joinGroupRequestHandler(packet, joinGroupRequestBody, channelContext);
            }
            if (msgType == MsgType.P2P_REQ) {
                baseMsgBody = Json.toBean(bodyJsonStr, P2PRequestBody.class);
                P2PRequestBody p2PRequestBody = (P2PRequestBody) baseMsgBody;
                SERVER_LOG.info("P2P请求: fromUserId:{}, toUserId:{}, msg:{}.",
                        p2PRequestBody.getFromUserId(),
                        p2PRequestBody.getToUserId(),
                        p2PRequestBody.getMsg());
                handler = p2pRequestHandler(packet, p2PRequestBody, channelContext);
            }
            if (msgType == MsgType.HEART_BEAT_REQ) {
                baseMsgBody = Json.toBean(bodyJsonStr, GroupMsgRequestBody.class);
                handler = heartbeatRequestHandler(packet, (GroupMsgRequestBody) baseMsgBody, channelContext);
            }
            return handler;
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
    protected abstract boolean loginRequestHandler(BasePacket packet, LoginRequestBody baseMsgBody, ChannelContext channelContext);

    /**
     * 加入群组请求处理
     *
     * @param packet
     * @param baseMsgBody
     * @param channelContext
     * @return
     */
    protected abstract boolean joinGroupRequestHandler(BasePacket packet, JoinGroupRequestBody baseMsgBody, ChannelContext channelContext);

    /**
     * 点对点请求处理
     *
     * @param packet
     * @param baseMsgBody
     * @param channelContext
     * @return
     */
    protected abstract boolean p2pRequestHandler(BasePacket packet, P2PRequestBody baseMsgBody, ChannelContext channelContext);

    /**
     * 组消息请求处理
     *
     * @param packet
     * @param baseMsgBody
     * @param channelContext
     * @return
     */
    protected abstract boolean groupMsgRequestHandler(BasePacket packet, GroupMsgRequestBody baseMsgBody, ChannelContext channelContext);

    /**
     * 心跳请求处理
     *
     * @param packet
     * @param baseMsgBody
     * @param channelContext
     * @return
     */
    protected abstract boolean heartbeatRequestHandler(BasePacket packet, GroupMsgRequestBody baseMsgBody, ChannelContext channelContext);
}
