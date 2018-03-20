package com.github.youyinnn.common.packets;

import com.github.youyinnn.common.intf.Const;
import org.tio.core.intf.Packet;
import org.tio.utils.json.Json;

import java.io.UnsupportedEncodingException;

/**
 * @author youyinnn
 */
public class BasePacket extends Packet {

    public static final int HEADER_LENGTH = 5;

    private static final long serialVersionUID = 1533379214657665716L;

    private byte msgType;

    private byte[] msgBody;

    public byte getMsgType() {
        return msgType;
    }

    public void setMsgType(byte msgType) {
        this.msgType = msgType;
    }

    public byte[] getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(byte[] msgBody) {
        this.msgBody = msgBody;
    }

    public BasePacket(byte msgType, byte[] msgBody) {
        this.msgType = msgType;
        this.msgBody = msgBody;
    }

    public BasePacket(byte msgType, BaseBody baseBody) {
        this.msgType = msgType;
        try {
            this.msgBody = Json.toJson(baseBody).getBytes(Const.CHARSET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
