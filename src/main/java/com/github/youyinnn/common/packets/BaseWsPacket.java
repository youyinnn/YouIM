package com.github.youyinnn.common.packets;

import com.github.youyinnn.common.OpCode;
import org.tio.core.intf.Packet;

/**
 * @author youyinnn
 */
public class BaseWsPacket extends Packet {

    private static final long serialVersionUID = 8917071928251381128L;

    public static final int MINIMUM_HEADER_LENGTH = 2;

    public static final int MAX_BODY_LENGTH = 1024 * 512;

    /**
     * 是否是握手包
     */
    private boolean isHandshake = false;

    private byte[] body;

    private boolean wsEof;

    private OpCode wsOpCode = OpCode.BINARY;

    private boolean wsHasMask;

    private long wsBodyLength;

    private byte[] wsMask;

    /**
     * 只有消息是文本的时候才有这个字段
     */
    private String wsBodyText;

    public BaseWsPacket() {
    }

    public BaseWsPacket(byte[] body) {
        this.body = body;
    }

    public boolean isHandshake() {
        return isHandshake;
    }

    public void setHandshake(boolean handshake) {
        isHandshake = handshake;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public boolean isWsEof() {
        return wsEof;
    }

    public void setWsEof(boolean wsEof) {
        this.wsEof = wsEof;
    }

    public OpCode getWsOpCode() {
        return wsOpCode;
    }

    public void setWsOpCode(OpCode wsOpCode) {
        this.wsOpCode = wsOpCode;
    }

    public boolean isWsHasMask() {
        return wsHasMask;
    }

    public void setWsHasMask(boolean wsHasMask) {
        this.wsHasMask = wsHasMask;
    }

    public long getWsBodyLength() {
        return wsBodyLength;
    }

    public void setWsBodyLength(long wsBodyLength) {
        this.wsBodyLength = wsBodyLength;
    }

    public byte[] getWsMask() {
        return wsMask;
    }

    public void setWsMask(byte[] wsMask) {
        this.wsMask = wsMask;
    }

    public String getWsBodyText() {
        return wsBodyText;
    }

    public void setWsBodyText(String wsBodyText) {
        this.wsBodyText = wsBodyText;
    }

    @Override
    public String logstr() {
        return "webSocket";
    }
}
