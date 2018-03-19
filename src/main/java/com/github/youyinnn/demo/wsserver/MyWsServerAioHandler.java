package com.github.youyinnn.demo.wsserver;

import com.github.youyinnn.common.intf.Const;
import com.github.youyinnn.common.packets.BaseWsPacket;
import com.github.youyinnn.server.intf.WsMsgHandler;
import org.tio.core.Aio;
import org.tio.core.ChannelContext;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;

import java.nio.ByteBuffer;

/**
 * @author youyinnn
 */
public class MyWsServerAioHandler implements WsMsgHandler{

    @Override
    public HttpResponse handshake(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {
        System.out.println(channelContext);
        return httpResponse;
    }

    @Override
    public ByteBuffer onBytes(BaseWsPacket wsPacket, byte[] bytes, ChannelContext channelContext) throws Exception {
        String byteStr = new String(bytes, Const.CHARSET);
        System.err.println("收到byte消息: " + byteStr);
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.put(bytes);
        return buffer;
    }

    @Override
    public void onClose(BaseWsPacket wsPacket, byte[] bytes, ChannelContext channelContext) throws Exception {
        Aio.remove(channelContext, "receive close flag");
    }

    @Override
    public String onText(BaseWsPacket wsPacket, String text, ChannelContext channelContext) throws Exception {
        System.err.println("收到text消息: " + text);
        return text;
    }
}
