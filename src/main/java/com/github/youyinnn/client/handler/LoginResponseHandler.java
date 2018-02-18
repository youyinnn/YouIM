package com.github.youyinnn.client.handler;

import com.github.youyinnn.common.AbstractMsgHandler;
import com.github.youyinnn.common.BaseSessionContext;
import com.github.youyinnn.common.packet.BasePacket;
import com.github.youyinnn.common.packet.LoginResponseBody;
import org.tio.core.ChannelContext;
import org.tio.utils.json.Json;

/**
 * @author youyinnn
 */
public class LoginResponseHandler extends AbstractMsgHandler<LoginResponseBody> {

    @Override
    public Class<LoginResponseBody> getMsgBodyClass() {
        return LoginResponseBody.class;
    }

    @Override
    public Object handler(BasePacket packet, LoginResponseBody baseMsgBody, ChannelContext channelContext) throws Exception {
        System.out.println("登陆收到响应:" + Json.toJson(baseMsgBody));
        String token = baseMsgBody.getToken();
        if (token != null) {
            BaseSessionContext sessionContext = (BaseSessionContext) channelContext.getAttribute();
            sessionContext.setToken(token);
            System.out.println("登录成功，token是:" + baseMsgBody.getToken());
        }
        return null;
    }
}
