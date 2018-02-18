package com.github.youyinnn.server.handler;

import com.github.youyinnn.common.AbstractMsgHandler;
import com.github.youyinnn.common.BaseSessionContext;
import com.github.youyinnn.common.Const;
import com.github.youyinnn.common.MsgType;
import com.github.youyinnn.common.packet.BasePacket;
import com.github.youyinnn.common.packet.LoginRequestBody;
import com.github.youyinnn.common.packet.LoginResponseBody;
import org.tio.core.Aio;
import org.tio.core.ChannelContext;
import org.tio.utils.json.Json;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author youyinnn
 */
public class LoginRequestHandler extends AbstractMsgHandler<LoginRequestBody> {

    private AtomicLong subToken = new AtomicLong();

    @Override
    public Class<LoginRequestBody> getMsgBodyClass() {
        return LoginRequestBody.class;
    }

    private String newToken() {
        return System.currentTimeMillis() + "_" + subToken.incrementAndGet();
    }

    @Override
    public Object handler(BasePacket packet, LoginRequestBody baseMsgBody, ChannelContext channelContext) throws Exception {
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

        //创建登陆响应的响应体
        LoginResponseBody loginResponseBody = new LoginResponseBody();
        loginResponseBody.setToken(newToken());
        loginResponseBody.setResultCode(Const.RequestCode.success);
        /*
         * 组登陆的响应包, 发送回登陆的请求方.
         */
        BasePacket responsePacket = new BasePacket(MsgType.LOGIN_RESP, Json.toJson(loginResponseBody).getBytes(Const.Handler.CHARSET));
        Aio.send(channelContext, responsePacket);
        return null;
    }
}