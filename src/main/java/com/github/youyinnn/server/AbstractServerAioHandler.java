package com.github.youyinnn.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.youyinnn.common.AbstractAioHandler;
import com.github.youyinnn.common.BaseSessionContext;
import com.github.youyinnn.common.intf.Const;
import com.github.youyinnn.common.intf.MsgType;
import com.github.youyinnn.common.packets.*;
import com.github.youyinnn.server.service.GroupCheckService;
import com.github.youyinnn.youdbutils.exceptions.AutowiredException;
import com.github.youyinnn.youdbutils.ioc.YouServiceIocContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.tio.core.Aio;
import org.tio.core.ChannelContext;
import org.tio.server.intf.ServerAioHandler;
import org.tio.utils.json.Json;

import java.util.List;

/**
 * The type Abstract server aio handler.
 *
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
        bodyJsonStr = new String(packet.getMsgBody(), Const.CHARSET);
        if (packet.getMsgBody() != null) {
            Boolean handler = true;
            if (msgType == MsgType.LOGIN_REQ) {
                LoginRequestBody loginRequestBody = Json.toBean(bodyJsonStr, LoginRequestBody.class);
                if (Server.isServerHandlerLogEnabled()) {
                    SERVER_LOG.info("收到登陆请求: userId:{}.",loginRequestBody.getLoginUserId());
                }
                beforeLoginRequestHandle();
                handler = loginRequestHandle(loginRequestBody, channelContext);
                if (handler) {
                    JSONObject bodyJsonObj = JSON.parseObject(bodyJsonStr);
                    String userId = bodyJsonObj.getString("loginUserId");
                    String groupJsonStr = service.getGroupJsonStr(userId);
                    if (groupJsonStr != null) {
                        List<String> groups = JSON.parseArray(groupJsonStr, String.class);
                        for (String group : groups) {
                            Aio.bindGroup(channelContext,group);
                        }
                    }
                }
                afterLoginRequestHandle();
            } else if (msgType == MsgType.GROUP_MSG_REQ) {
                GroupMsgRequestBody groupMsgRequestBody = Json.toBean(bodyJsonStr, GroupMsgRequestBody.class);
                if (Server.isServerHandlerLogEnabled()) {
                    SERVER_LOG.info("收到群组消息请求: fromUserId:{}, toGroup:{}, msg:{}.",
                            groupMsgRequestBody.getFromUserId(),
                            groupMsgRequestBody.getToGroup(),
                            groupMsgRequestBody.getMsg());
                }
                beforeGroupMsgRequestHandle();
                groupMsgRequestHandle(groupMsgRequestBody, channelContext);
                afterGroupMsgRequestHandle();
            } else if (msgType == MsgType.JOIN_GROUP_REQ) {
                JoinGroupRequestBody joinGroupRequestBody = Json.toBean(bodyJsonStr, JoinGroupRequestBody.class);
                if (Server.isServerHandlerLogEnabled()) {
                    SERVER_LOG.info("收到加群请求: fromUserId:{}, toGroup:{}.",
                            joinGroupRequestBody.getFromUserId(),
                            joinGroupRequestBody.getGroup());
                }
                beforeJoinGroupRequestHandle();
                handler = joinGroupRequestHandle(joinGroupRequestBody, channelContext);
                if (handler) {
                    service.registerGroupInJson(joinGroupRequestBody.getFromUserId(),joinGroupRequestBody.getGroup());
                }
                afterJoinGroupRequestHandle();
            } else if (msgType == MsgType.P2P_REQ) {
                P2PRequestBody p2PRequestBody = Json.toBean(bodyJsonStr, P2PRequestBody.class);
                if (Server.isServerHandlerLogEnabled()) {
                    SERVER_LOG.info("收到P2P请求: fromUserId:{}, toUserId:{}, msg:{}.",
                            p2PRequestBody.getFromUserId(),
                            p2PRequestBody.getToUserId(),
                            p2PRequestBody.getMsg());
                }
                beforeP2PMsgRequestHandle();
                handler = p2PMsgRequestHandle(p2PRequestBody, channelContext);
                afterP2PMsgRequestHandle();
            } else if (msgType == MsgType.LOGOUT_REQ) {
                LogoutRequestBody logoutRequestBody = Json.toBean(bodyJsonStr, LogoutRequestBody.class);
                if (Server.isServerHandlerLogEnabled()) {
                    SERVER_LOG.info("收到登出请求: userId:{}.", logoutRequestBody.getLogoutUserId());
                }
                beforeLogoutRequestHandle();
                logoutRequestHandle(channelContext);
                afterLogoutRequestHandle();
            } else if (msgType == MsgType.QUIT_GROUP_REQ) {
                QuitGroupRequestBody quitGroupRequestBody = Json.toBean(bodyJsonStr, QuitGroupRequestBody.class);
                beforeQuitGroupRequestHandle();
                quitGroupRequestHandle(channelContext, quitGroupRequestBody.getGroupId());
                afterQUitGroupRequestHandle();
            } else if (msgType == MsgType.HEART_BEAT_REQ) {
                handler = heartbeatRequestHandler(Json.toBean(bodyJsonStr, GroupMsgRequestBody.class), channelContext);
            }
            return handler;
        }
        return null;
    }

    private Boolean loginRequestHandle(LoginRequestBody loginRequestBody, ChannelContext channelContext) {
        /*
         * 从请求登陆方获取请求者id,将该连接通道和该id进行绑定
         * 需要注意的是,这里的绑定的含义是告诉框架:我这个连接通道和该id绑定了
         * 此绑定的意义是面向框架的,绑定的id是提供给框架服务的.
         */
        String userId = loginRequestBody.getLoginUserId();
        Aio.bindUser(channelContext, userId);

        /*
         * 在该请求连接中获取属性对象,将该请求者id设置到连接通道的属性对象上.
         * 这个设置算是面向用户的设置,和框架无关,仅和用户业务有关.
         */
        BaseSessionContext sessionContext = (BaseSessionContext) channelContext.getAttribute();
        sessionContext.setUserId(userId);

        /*
         * 组成登陆的响应包, 发送回登陆的请求方.
         */
        BasePacket responsePacket = BasePacket.loginResponsePacket(Const.RequestCode.SUCCESS,getToken());
        return Aio.send(channelContext, responsePacket);
    }

    private void groupMsgRequestHandle(GroupMsgRequestBody groupMsgRequestBody, ChannelContext channelContext){
        BaseSessionContext sessionContext = (BaseSessionContext) channelContext.getAttribute();

        BasePacket responsePacket =
                BasePacket.groupMsgResponsePacket(groupMsgRequestBody.getMsg(), sessionContext.getUserId(), groupMsgRequestBody.getToGroup());
        Aio.sendToGroup(channelContext.getGroupContext(), groupMsgRequestBody.getToGroup(), responsePacket);
    }

    private Boolean joinGroupRequestHandle(JoinGroupRequestBody joinGroupRequestBody, ChannelContext channelContext) {
        Aio.bindGroup(channelContext, joinGroupRequestBody.getGroup());

        BasePacket responsePacket =
                BasePacket.joinGroupResponsePacket(Const.RequestCode.SUCCESS,"",joinGroupRequestBody.getGroup());
        return Aio.send(channelContext, responsePacket);
    }

    private Boolean p2PMsgRequestHandle(P2PRequestBody p2PRequestBody, ChannelContext channelContext) {
        BaseSessionContext sessionContext = (BaseSessionContext) channelContext.getAttribute();
        BasePacket responsePacket =
                BasePacket.p2PMsgResponsePacket(p2PRequestBody.getMsg(), sessionContext.getUserId());
        return Aio.sendToUser(channelContext.getGroupContext(), p2PRequestBody.getToUserId(), responsePacket);
    }

    private void logoutRequestHandle(ChannelContext channelContext) {
        BaseSessionContext sessionContext = (BaseSessionContext) channelContext.getAttribute();
        sessionContext.setUserId(null);
        Aio.unbindUser(channelContext);
    }

    private void quitGroupRequestHandle(ChannelContext channelContext, String groupId) {
        Aio.unbindGroup(groupId, channelContext);
    }

    /**
     * 必须实现一个Token获取方法
     *
     * @return token
     */
    protected abstract String getToken();

    /**
     * Before login request handle.
     */
    protected abstract void beforeLoginRequestHandle();

    /**
     * After login request handle.
     */
    protected abstract void afterLoginRequestHandle();

    /**
     * Before group msg request handle.
     */
    protected abstract void beforeGroupMsgRequestHandle();

    /**
     * After group msg request handle.
     */
    protected abstract void afterGroupMsgRequestHandle();

    /**
     * Before join group request handle.
     */
    protected abstract void beforeJoinGroupRequestHandle();

    /**
     * After join group request handle.
     */
    protected abstract void afterJoinGroupRequestHandle();

    /**
     * Before p 2 p msg request handle.
     */
    protected abstract void beforeP2PMsgRequestHandle();

    /**
     * After p 2 p msg request handle.
     */
    protected abstract void afterP2PMsgRequestHandle();

    /**
     * Before logout request handle.
     */
    protected abstract void beforeLogoutRequestHandle();

    /**
     * After logout request handle.
     */
    protected abstract void afterLogoutRequestHandle();

    /**
     * Before quit group request handle.
     */
    protected abstract void beforeQuitGroupRequestHandle();

    /**
     * After q uit group request handle.
     */
    protected abstract void afterQUitGroupRequestHandle();

    /**
     * 心跳请求处理
     *
     * @param baseMsgBody    the base msg body
     * @param channelContext the channel context
     * @return boolean
     */
    protected abstract boolean heartbeatRequestHandler(GroupMsgRequestBody baseMsgBody, ChannelContext channelContext);
}
