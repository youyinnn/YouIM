package com.github.youyinnn.common;

import com.github.youyinnn.common.packets.BaseWsPacket;
import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
import org.tio.core.utils.ByteBufferUtils;

import java.nio.ByteBuffer;

/**
 * @author youyinnn
 */
public class WsServerEncoder {

    public static ByteBuffer encode(BaseWsPacket wsPacket, GroupContext groupContext, ChannelContext channelContext) {
        byte[] imBody = wsPacket.getBody();
        int wsBodyLength = 0;
        if (imBody != null) {
            wsBodyLength = imBody.length;
        }

        byte header0 = (byte) (0x8f & (wsPacket.getWsOpCode().getCode() | 0xf0));
        ByteBuffer buffer;
        if (wsBodyLength < 126) {
            buffer = ByteBuffer.allocate(2 + wsBodyLength);
            buffer.put(header0);
            buffer.put((byte) wsBodyLength);
        } else if (wsBodyLength < (1 << 16) - 1) {
            buffer = ByteBuffer.allocate(4 + wsBodyLength);
            buffer.put(header0);
            buffer.put((byte) 126);
            ByteBufferUtils.writeUB2WithBigEdian(buffer, wsBodyLength);
        } else {
            buffer = ByteBuffer.allocate(10 + wsBodyLength);
            buffer.put(header0);
            buffer.put((byte) 127);
            buffer.put(new byte[]{0, 0, 0, 0});
            ByteBufferUtils.writeUB4WithBigEdian(buffer, wsBodyLength);
        }

        if (imBody != null && imBody.length > 0) {
            buffer.put(imBody);
        }
        return buffer;
    }

}
