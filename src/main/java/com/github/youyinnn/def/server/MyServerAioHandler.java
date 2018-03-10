package com.github.youyinnn.def.server;

import com.github.youyinnn.common.BaseSessionContext;
import com.github.youyinnn.common.Const;
import com.github.youyinnn.common.packet.*;
import com.github.youyinnn.server.core.AbstractServerAioHandler;
import org.tio.core.Aio;
import org.tio.core.ChannelContext;
import org.tio.utils.json.Json;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author youyinnn
 */
public class MyServerAioHandler extends AbstractServerAioHandler {

    private AtomicLong subToken = new AtomicLong();

    @Override
    protected String getToken() {
        return System.currentTimeMillis() + "_" + subToken.incrementAndGet();
    }

    @Override
    protected Object loginRequestHandler(BasePacket packet, LoginRequestBody baseMsgBody, ChannelContext channelContext) {
        System.out.println("收到登录请求消息: " + Json.toJson(baseMsgBody));
        /*
         * 从请求登陆方获取请求者id,将该连接通道和该id进行绑定
         * 需要注意的是,这里的绑定的含义是告诉框架:我这个连接通道和该id绑定了
         * 此绑定的意义是面向框架的,绑定的id是提供给框架服务的.
         */
        String userId = baseMsgBody.getLoginUserId();
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
        Aio.send(channelContext, responsePacket);
        return null;
    }

    @Override
    protected Object joinGroupRequestHandler(BasePacket packet, JoinGroupRequestBody baseMsgBody, ChannelContext channelContext) {
        System.out.println("收到进群请求消息:" + Json.toJson(baseMsgBody));

        Aio.bindGroup(channelContext, baseMsgBody.getGroup());

        BasePacket responsePacket =
                BasePacket.joinGroupResponsePacket(Const.RequestCode.SUCCESS,"",baseMsgBody.getGroup());
        Aio.send(channelContext, responsePacket);
        return null;
    }

    @Override
    protected Object p2pRequestHandler(BasePacket packet, P2PRequestBody baseMsgBody, ChannelContext channelContext) {
        System.out.println("收到点对点请求消息:" + Json.toJson(baseMsgBody));

        BaseSessionContext sessionContext = (BaseSessionContext) channelContext.getAttribute();
        BasePacket responsePacket =
                BasePacket.p2pMsgResponsePacket(baseMsgBody.getMsg(), sessionContext.getUserId());
        Aio.sendToUser(channelContext.getGroupContext(), baseMsgBody.getToUserId(), responsePacket);

        return null;
    }

    @Override
    protected Object groupMsgRequestHandler(BasePacket packet, GroupMsgRequestBody baseMsgBody, ChannelContext channelContext) {
        System.out.println("收到群聊请求消息:" + Json.toJson(baseMsgBody));

        BaseSessionContext sessionContext = (BaseSessionContext) channelContext.getAttribute();

        BasePacket responsePacket =
                BasePacket.groupMsgResponsePacket(baseMsgBody.getMsg(), sessionContext.getUserId(), baseMsgBody.getToGroup());
        Aio.sendToGroup(channelContext.getGroupContext(), baseMsgBody.getToGroup(), responsePacket);
        return null;
    }

    @Override
    protected Object heartbeatRequestHandler(BasePacket packet, GroupMsgRequestBody baseMsgBody, ChannelContext channelContext) {
        System.out.println("心跳消息");
        return null;
    }
}
