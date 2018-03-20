package com.github.youyinnn.server.ws;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.youyinnn.common.OpCode;
import com.github.youyinnn.common.WsServerDecoder;
import com.github.youyinnn.common.WsServerEncoder;
import com.github.youyinnn.common.WsSessionContext;
import com.github.youyinnn.common.intf.Const;
import com.github.youyinnn.common.intf.MsgType;
import com.github.youyinnn.common.packets.BaseWsPacket;
import com.github.youyinnn.common.utils.BASE64Util;
import com.github.youyinnn.common.utils.SHA1Util;
import com.github.youyinnn.server.BasicImWorkflowHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
 * The type Abstract ws server aio handler.
 *
 * @author youyinnn
 */
public abstract class AbstractWsServerAioHandler implements ServerAioHandler {

    private static final Logger SERVER_LOG = LogManager.getLogger("$im_server");

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
     * @param request        the request
     * @param channelContext the channel context
     * @return http response
     */
    private static HttpResponse updateWebSocketProtocol(HttpRequest request, ChannelContext channelContext) {
        Map<String, String> headers = request.getHeaders();
        String secWebSocketKey = headers.get(HttpConst.RequestHeaderKey.Sec_WebSocket_Key);

        if (StringUtils.isNotBlank(secWebSocketKey)) {
            String secWebSocketKeyMagic = secWebSocketKey + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
            byte[] keyArray = SHA1Util.SHA1(secWebSocketKeyMagic);
            String acceptKey = BASE64Util.byteArrayToBase64(keyArray);
            HttpResponse httpResponse = new HttpResponse(request);

            httpResponse.setStatus(HttpResponseStatus.C101);

            HashMap<String, String> respHeaders = new HashMap<>(10);
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
        WsSessionContext wsSessionContext = (WsSessionContext) channelContext.getAttribute();
        HttpRequest request = wsSessionContext.getHandshakeRequestPacket();
        HttpResponse response = wsSessionContext.getHandshakeResponsePacket();

        if (wsRequestPacket.isHandshake()) {
            beforeHandshake(request, response, channelContext);
            HttpResponse handshakeResponse = handshake(response);
            if (handshakeResponse == null) {
                Aio.remove(channelContext, "业务层不同意握手");
            } else {
                wsSessionContext.setHandshakeResponsePacket(handshakeResponse);
                BaseWsPacket wsResponsePacket = new BaseWsPacket();
                wsResponsePacket.setHandshake(true);
                wsSessionContext.setHandshaked(true);
                Aio.send(channelContext, wsResponsePacket);
            }
            afterHandshaked(request, response, channelContext);
        } else {
            beforeHandle(request, response, channelContext);
            handle(wsRequestPacket.getBody(), wsRequestPacket.getWsOpCode(), channelContext);
            afterHandled(request, response, channelContext);
        }
    }

    /**
     * 按照类型处理webSocket消息
     */
    private void handle(byte[] bytes, OpCode opCode, ChannelContext channelContext) throws Exception {
        if (opCode == OpCode.TEXT) {
            if (bytes == null || bytes.length == 0) {
                Aio.remove(channelContext, "错误的webSocket包, body无效");
            } else {
                String textBody = new String(bytes, Const.CHARSET);
                JSONObject textBodyJson = JSON.parseObject(textBody);
                Byte msgType = textBodyJson.getByte("msgType");
                if (msgType == null) {
                    SERVER_LOG.error("无效的msgType");
                } else {
                    String msgBody = textBodyJson.getString("msgBody");
                    if (msgType == MsgType.LOGIN_REQ) {
                        BasicImWorkflowHandler.loginRequestHandle(msgBody, channelContext, getToken());
                    } else if (msgType == MsgType.GROUP_MSG_REQ) {

                    } else if (msgType == MsgType.JOIN_GROUP_REQ) {

                    } else if (msgType == MsgType.P2P_REQ) {

                    } else if (msgType == MsgType.LOGOUT_REQ) {

                    } else if (msgType == MsgType.QUIT_GROUP_REQ) {

                    }
                }

            }
        } else if (opCode == OpCode.BINARY) {
            if (bytes == null || bytes.length == 0) {
                Aio.remove(channelContext, "错误的webSocket包, body无效");
            } else {
                onBytes(bytes);
            }
        } else if (opCode == OpCode.PING || opCode == OpCode.PONG) {
            System.err.println("收到" + opCode);
        } else if (opCode == OpCode.CLOSE) {
            onClose(channelContext);
        } else {
            Aio.remove(channelContext, "错误的webSocket包，错误的Opcode");
        }
    }

    private HttpResponse handshake(HttpResponse httpResponse) {
        return httpResponse;
    }


    private ByteBuffer onBytes(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.put(bytes);
        return buffer;
    }

    private void onClose(ChannelContext channelContext) {
        Aio.remove(channelContext, "receive close flag");
    }

    /**
     * 必须实现一个Token获取方法
     *
     * @return token token
     */
    protected abstract String getToken();

    /**
     * After handshaked.
     *
     * @param request        the request
     * @param response       the response
     * @param channelContext the channel context
     */
    protected abstract void afterHandshaked(HttpRequest request, HttpResponse response, ChannelContext channelContext);

    /**
     * Before handshake.
     *
     * @param request        the request
     * @param response       the response
     * @param channelContext the channel context
     */
    protected abstract void beforeHandshake(HttpRequest request, HttpResponse response, ChannelContext channelContext);

    /**
     * After handled.
     *
     * @param request        the request
     * @param response       the response
     * @param channelContext the channel context
     */
    protected abstract void afterHandled(HttpRequest request, HttpResponse response, ChannelContext channelContext);

    /**
     * Before handle.
     *
     * @param request        the request
     * @param response       the response
     * @param channelContext the channel context
     */
    protected abstract void beforeHandle(HttpRequest request, HttpResponse response, ChannelContext channelContext);
}
