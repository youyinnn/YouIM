package com.github.youyinnn.server;

import com.github.youyinnn.common.OpCode;
import com.github.youyinnn.common.WsServerDecoder;
import com.github.youyinnn.common.WsServerEncoder;
import com.github.youyinnn.common.WsSessionContext;
import com.github.youyinnn.common.intf.Const;
import com.github.youyinnn.common.packets.BaseWsPacket;
import com.github.youyinnn.common.utils.BASE64Util;
import com.github.youyinnn.common.utils.SHA1Util;
import com.github.youyinnn.server.intf.WsMsgHandler;
import org.apache.commons.lang3.StringUtils;
import org.tio.core.Aio;
import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.Packet;
import org.tio.http.common.*;
import org.tio.server.intf.ServerAioHandler;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * @author youyinnn
 */
public class WsServerAioHandler implements ServerAioHandler {

    private WsMsgHandler wsMsgHandler;

    public WsServerAioHandler(WsMsgHandler wsMsgHandler) {
        this.wsMsgHandler = wsMsgHandler;
    }

    @Override
    public Packet decode(ByteBuffer buffer, ChannelContext channelContext) throws AioDecodeException {
        WsSessionContext wsSessionContext = (WsSessionContext) channelContext.getAttribute();
        BaseWsPacket wsRequestPacket;

        // 如果还没握手
        if (!wsSessionContext.isHandshaked()) {
            HttpRequest request = HttpRequestDecoder.decode(buffer, channelContext);
            if (request == null) {
                return null;
            }

            HttpResponse response = updateWebSocketProtocol(request, channelContext);
            if (response == null) {
                throw new AioDecodeException("http协议升级到webSocket协议失败");
            }

            wsSessionContext.setHandshakeRequestPacket(request);
            wsSessionContext.setHandshakeResponsePacket(response);

            wsRequestPacket = new BaseWsPacket();
            wsRequestPacket.setHandshake(true);
        } else {
            // 如果已经握手
            wsRequestPacket = WsServerDecoder.decode(buffer, channelContext);
        }
        return wsRequestPacket;
    }

    @Override
    public ByteBuffer encode(Packet packet, GroupContext groupContext, ChannelContext channelContext) {
        BaseWsPacket wsResponsePacket = (BaseWsPacket) packet;

        if (wsResponsePacket.isHandshake()) {
            WsSessionContext imSessionContext = (WsSessionContext) channelContext.getAttribute();
            HttpResponse handshakeResponsePacket = imSessionContext.getHandshakeResponsePacket();
            return HttpResponseEncoder.encode(handshakeResponsePacket, groupContext, channelContext, false);
        }

        return WsServerEncoder.encode(wsResponsePacket, groupContext, channelContext);
    }

    /**
     * 该方法把http请求协议升级为webSocket协议
     *
     * @param request
     * @param channelContext
     * @return
     */
    public static HttpResponse updateWebSocketProtocol(HttpRequest request, ChannelContext channelContext) {
        Map<String, String> headers = request.getHeaders();
        String secWebSocketKey = headers.get(HttpConst.RequestHeaderKey.Sec_WebSocket_Key);

        if (StringUtils.isNotBlank(secWebSocketKey)) {
            String secWebSocketKeyMagic = secWebSocketKey + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
            byte[] keyArray = SHA1Util.SHA1(secWebSocketKeyMagic);
            String acceptKey = BASE64Util.byteArrayToBase64(keyArray);
            HttpResponse httpResponse = new HttpResponse(request);

            httpResponse.setStatus(HttpResponseStatus.C101);

            HashMap<String, String> respHeaders = new HashMap<>();
            respHeaders.put(HttpConst.ResponseHeaderKey.Connection, HttpConst.ResponseHeaderValue.Connection.Upgrade);
            respHeaders.put(HttpConst.ResponseHeaderKey.Upgrade, "WebSocket");
            respHeaders.put(HttpConst.ResponseHeaderKey.Sec_WebSocket_Accept, acceptKey);
            httpResponse.setHeaders(respHeaders);
            return httpResponse;
        }
        return null;
    }

    @Override
    public void handler(Packet packet, ChannelContext channelContext) throws Exception {
        BaseWsPacket wsRequestPacket = (BaseWsPacket) packet;

        if (wsRequestPacket.isHandshake()) {
            WsSessionContext wsSessionContext = (WsSessionContext) channelContext.getAttribute();
            HttpRequest request = wsSessionContext.getHandshakeRequestPacket();
            HttpResponse response = wsSessionContext.getHandshakeResponsePacket();
            HttpResponse handshakeResponse = wsMsgHandler.handshake(request, response, channelContext);
            if (handshakeResponse == null) {
                Aio.remove(channelContext, "业务层不同意握手");
            } else {
                wsSessionContext.setHandshakeResponsePacket(handshakeResponse);

                BaseWsPacket wsResponse = new BaseWsPacket();
                wsResponse.setHandshake(true);
                Aio.send(channelContext, wsRequestPacket);
                wsSessionContext.setHandshaked(true);
            }
        } else {
            BaseWsPacket wsResponse =
                    handle(wsRequestPacket,
                        wsRequestPacket.getBody(),
                        wsRequestPacket.getWsOpCode(),
                            channelContext);

            if (wsResponse != null) {
                Aio.send(channelContext, wsResponse);
            }
        }
    }

    /**
     * 按照类型处理webSocket消息
     */
    private BaseWsPacket handle(BaseWsPacket webSocketPacket, byte[] bytes, OpCode opCode, ChannelContext channelContext) throws Exception {
        BaseWsPacket wsResponse = null;
        if (opCode == OpCode.TEXT) {
            if (bytes == null || bytes.length == 0) {
                Aio.remove(channelContext, "错误的webSocket包, body无效");
            } else {
                String onText = wsMsgHandler.onText(webSocketPacket, new String(bytes, Const.CHARSET), channelContext);
                if (onText != null) {
                    wsResponse = BaseWsPacket.fromText(onText, Const.CHARSET);
                }
            }
        } else if (opCode == OpCode.BINARY) {
            if (bytes == null || bytes.length == 0) {
                Aio.remove(channelContext, "错误的webSocket包, body无效");
            } else {
                ByteBuffer onBytes = wsMsgHandler.onBytes(webSocketPacket, bytes, channelContext);
                if (onBytes != null) {
                    byte[] array = onBytes.array();
                    wsResponse = BaseWsPacket.fromBytes(array);
                }
            }
        } else if (opCode == OpCode.PING || opCode == OpCode.PONG) {
            System.err.println("收到" + opCode);
        } else if (opCode == OpCode.CLOSE) {
            wsMsgHandler.onClose(webSocketPacket, bytes, channelContext);
        } else {
            Aio.remove(channelContext, "错误的webSocket包，错误的Opcode");
        }

        return wsResponse;
    }

}
