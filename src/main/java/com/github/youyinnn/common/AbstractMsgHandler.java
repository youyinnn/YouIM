package com.github.youyinnn.common;

import com.github.youyinnn.common.packet.BaseBody;
import com.github.youyinnn.common.packet.BasePacket;
import org.tio.core.ChannelContext;
import org.tio.utils.json.Json;

/**
 * @author youyinnn
 */
public abstract class AbstractMsgHandler<T extends BaseBody> {

    /**
     * 返回具体消息体的类
     *
     * @return
     */
    public abstract Class<T> getMsgBodyClass();

    /**
     * 第二层handler:
     * 该方法需要在框架自调用的handler方法里最后调用,
     * 意图统一抽出对应的msgBody对象,紧接着调用第三层handler.
     *
     * @param packet
     * @param channelContext
     * @return
     * @throws Exception
     */
    public Object handler(BasePacket packet, ChannelContext channelContext) throws Exception {
        String jsonStr;
        T baseMsgBody = null;
        if (packet.getMsgBody() != null) {
            jsonStr = new String(packet.getMsgBody(), Const.Handler.CHARSET);
            baseMsgBody = Json.toBean(jsonStr, getMsgBodyClass());
        }
        return handler(packet, baseMsgBody, channelContext);
    }

    /**
     * 第三层handler:
     * 该方法由以上第二层handler最后调用,
     * 该方法需要根据对应的消息类型而做出对应的业务,
     * 所以该方法需要根据对应的消息类型而进行对应的实现.
     *
     * @param packet
     * @param baseMsgBody
     * @param channelContext
     * @return
     * @throws Exception
     */
    public abstract Object handler(BasePacket packet, T baseMsgBody, ChannelContext channelContext) throws Exception;
}
