package com.github.youyinnn.server.intf;

import com.github.youyinnn.common.packets.BaseWsPacket;
import org.tio.core.ChannelContext;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;

import java.nio.ByteBuffer;

public interface WsMsgHandler {

    /**
     * 对httpResponse参数进行补充并返回，如果返回null表示不想和对方建立连接，框架会断开连接，如果返回非null，框架会把这个对象发送给对方
     *
     * @param httpRequest
     * @param httpResponse
     * @param channelContext
     * @return
     * @throws Exception
     */
    HttpResponse handshake(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) throws Exception;

    /**
     * 该方法处理Byte类型的消息
     *
     * @param wsPacket
     * @param bytes
     * @param channelContext
     * @return
     * @throws Exception
     */
    ByteBuffer onBytes(BaseWsPacket wsPacket, byte[] bytes, ChannelContext channelContext) throws Exception;

    /**
     * 该方法处理Close类型的消息
     *
     * @param wsPacket
     * @param bytes
     * @param channelContext
     * @return
     * @throws Exception
     */
    void onClose(BaseWsPacket wsPacket, byte[] bytes, ChannelContext channelContext) throws Exception;

    /**
     * 该方法处理文字消息
     *
     * @param wsPacket
     * @param text
     * @param channelContext
     * @return
     * @throws Exception
     */
    String onText(BaseWsPacket wsPacket, String text, ChannelContext channelContext) throws Exception;
}
