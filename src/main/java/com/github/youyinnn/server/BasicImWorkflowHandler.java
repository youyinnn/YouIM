package com.github.youyinnn.server;

import com.alibaba.fastjson.JSON;
import com.github.youyinnn.common.BaseSessionContext;
import com.github.youyinnn.common.intf.Const;
import com.github.youyinnn.common.packets.*;
import com.github.youyinnn.common.utils.PacketFactory;
import com.github.youyinnn.server.service.GroupCheckService;
import com.github.youyinnn.youdbutils.exceptions.AutowiredException;
import com.github.youyinnn.youdbutils.ioc.YouServiceIocContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.tio.core.Aio;
import org.tio.core.ChannelContext;
import org.tio.utils.json.Json;

import java.util.List;

/**
 * @author youyinnn
 */
public class BasicImWorkflowHandler {

    private static GroupCheckService service;

    private static final Logger SERVER_LOG = LogManager.getLogger("$im_server");

    static {
        try {
            service = (GroupCheckService) YouServiceIocContainer.getYouService(GroupCheckService.class);
        } catch (AutowiredException e) {
            e.printStackTrace();
        }
    }

    public static Boolean loginRequestHandle(String bodyJsonStr, ChannelContext channelContext, String token) {
        LoginRequestBody loginRequestBody = Json.toBean(bodyJsonStr, LoginRequestBody.class);
        if (Server.isServerHandlerLogEnabled()) {
            SERVER_LOG.info("收到登陆请求: userId:{}.",loginRequestBody.getLoginUserId());
        }
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
        BasePacket responsePacket = PacketFactory.loginResponsePacket(Const.RequestCode.SUCCESS, token);
        Boolean send = Aio.send(channelContext, responsePacket);
        if (send) {
            String groupJsonStr = service.getGroupJsonStr(userId);
            if (groupJsonStr != null) {
                List<String> groups = JSON.parseArray(groupJsonStr, String.class);
                for (String group : groups) {
                    Aio.bindGroup(channelContext,group);
                }
            }
        }
        return send;
    }

    public static void groupMsgRequestHandle(String bodyJsonStr, ChannelContext channelContext){
        GroupMsgRequestBody groupMsgRequestBody = Json.toBean(bodyJsonStr, GroupMsgRequestBody.class);
        if (Server.isServerHandlerLogEnabled()) {
            SERVER_LOG.info("收到群组消息请求: fromUserId:{}, toGroup:{}, msg:{}.",
                    groupMsgRequestBody.getFromUserId(),
                    groupMsgRequestBody.getToGroup(),
                    groupMsgRequestBody.getMsg());
        }
        BaseSessionContext sessionContext = (BaseSessionContext) channelContext.getAttribute();

        BasePacket responsePacket =
                PacketFactory.groupMsgResponsePacket(groupMsgRequestBody.getMsg(), sessionContext.getUserId(), groupMsgRequestBody.getToGroup());
        Aio.sendToGroup(channelContext.getGroupContext(), groupMsgRequestBody.getToGroup(), responsePacket);
    }

    public static Boolean joinGroupRequestHandle(String bodyJsonStr, ChannelContext channelContext) {
        JoinGroupRequestBody joinGroupRequestBody = Json.toBean(bodyJsonStr, JoinGroupRequestBody.class);
        if (Server.isServerHandlerLogEnabled()) {
            SERVER_LOG.info("收到加群请求: fromUserId:{}, toGroup:{}.",
                    joinGroupRequestBody.getFromUserId(),
                    joinGroupRequestBody.getGroup());
        }
        Aio.bindGroup(channelContext, joinGroupRequestBody.getGroup());

        BasePacket responsePacket =
                PacketFactory.joinGroupResponsePacket(Const.RequestCode.SUCCESS,"",joinGroupRequestBody.getGroup());

        Boolean send = Aio.send(channelContext, responsePacket);
        if (send) {
            service.registerGroupInJson(joinGroupRequestBody.getFromUserId(),joinGroupRequestBody.getGroup());
        }
        return send;
    }

    public static Boolean p2PMsgRequestHandle(String bodyJsonStr, ChannelContext channelContext) {
        P2PRequestBody p2PRequestBody = Json.toBean(bodyJsonStr, P2PRequestBody.class);
        if (Server.isServerHandlerLogEnabled()) {
            SERVER_LOG.info("收到P2P请求: fromUserId:{}, toUserId:{}, msg:{}.",
                    p2PRequestBody.getFromUserId(),
                    p2PRequestBody.getToUserId(),
                    p2PRequestBody.getMsg());
        }
        BaseSessionContext sessionContext = (BaseSessionContext) channelContext.getAttribute();
        BasePacket responsePacket =
                PacketFactory.p2PMsgResponsePacket(p2PRequestBody.getMsg(), sessionContext.getUserId());
        return Aio.sendToUser(channelContext.getGroupContext(), p2PRequestBody.getToUserId(), responsePacket);
    }

    public static void logoutRequestHandle(String bodyJsonStr, ChannelContext channelContext) {
        LogoutRequestBody logoutRequestBody = Json.toBean(bodyJsonStr, LogoutRequestBody.class);
        if (Server.isServerHandlerLogEnabled()) {
            SERVER_LOG.info("收到登出请求: userId:{}.", logoutRequestBody.getLogoutUserId());
        }
        BaseSessionContext sessionContext = (BaseSessionContext) channelContext.getAttribute();
        sessionContext.setUserId(null);
        Aio.unbindUser(channelContext);
    }

    public static void quitGroupRequestHandle(String bodyJsonStr, ChannelContext channelContext) {
        QuitGroupRequestBody quitGroupRequestBody = Json.toBean(bodyJsonStr, QuitGroupRequestBody.class);
        Aio.unbindGroup(quitGroupRequestBody.getGroupId(), channelContext);
    }

    public static Boolean heartbeatRequestHandler(String bodyJsonStr, ChannelContext channelContext) {
        return null;
    }
}