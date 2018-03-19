package com.github.youyinnn.common;

import com.github.youyinnn.common.intf.Const;
import com.github.youyinnn.common.packets.BaseWsPacket;
import org.tio.core.Aio;
import org.tio.core.ChannelContext;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.utils.ByteBufferUtils;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * @author youyinnn
 */
public class WsServerDecoder {

    public static BaseWsPacket decode(ByteBuffer buffer, ChannelContext channelContext) throws AioDecodeException {
        WsSessionContext imSessionContext = (WsSessionContext) channelContext.getAttribute();
        List<byte[]> lastParts = imSessionContext.getLastParts();

        /*
         * 第一阶段解析
         */
        int initPosition = buffer.position();
        int readableLength = buffer.limit() - initPosition;
        int headLength = BaseWsPacket.MINMUM_HEADER_LENGTH;

        if (readableLength < headLength) {
            return null;
        }

        byte first = buffer.get();
        // 得到第8位 10000000>0
        boolean fin = (first & 0x80) > 0;
        //得到5、6、7 为01110000 然后右移四位为00000111
        int rsv = (first & 0x70) >>> 4;
        // //后四位为opCode 00001111
        byte opCodeByte = (byte) (first & 0x0F);
        OpCode opCode = OpCode.valueOf(opCodeByte);
        if (opCode == OpCode.CLOSE) {

        }
        if (!fin) {
            Aio.remove(channelContext, "暂时不支持fin为false的请求");
            return null;
        } else {
            imSessionContext.setLastParts(null);
        }

        // 继续向后取字符
        byte second = buffer.get();
        /*
         * 用于标识PayloadData是否经过掩码处理。
         * 如果是1，Masking-key域的数据即是掩码密钥，用于解码PayloadData。
         * 客户端发出的数据帧需要进行掩码处理，所以此位是1。
         */
        boolean hasMask = (second & 0xFF) >> 7 == 1;

        // 客户端数据必须进行掩码处理
        if (!hasMask) {
            headLength += 4;
        }
        // 读取后7位
        int payloadLength = second & 0x7F;

        byte[] mask = null;
        if (payloadLength == 126) {
            //为126读2个字节，后两个字节为payloadLength
            headLength += 2;
            if (readableLength < headLength) {
                return null;
            }
            payloadLength = ByteBufferUtils.readUB2WithBigEdian(buffer);
        } else if (payloadLength == 127){
            //127读8个字节,后8个字节为payloadLength
            headLength = 8;
            if (readableLength < headLength) {
                return null;
            }
            payloadLength = (int) buffer.getLong();
        }

        if (payloadLength < 0 || payloadLength > BaseWsPacket.MAX_BODY_LENGTH) {
            throw new AioDecodeException("body length(" + payloadLength + ") is not right");
        }

        if (readableLength < headLength + payloadLength) {
            return null;
        }

        if (hasMask) {
            mask = ByteBufferUtils.readBytes(buffer, 4);
        }

        /*
         * 第二阶段解析
         */
        BaseWsPacket wsPacket = new BaseWsPacket();
        wsPacket.setWsEof(fin);
        wsPacket.setWsHasMask(hasMask);
        wsPacket.setWsMask(mask);
        wsPacket.setWsOpCode(opCode);
        wsPacket.setWsBodyLength(payloadLength);

        if (payloadLength == 0) {
            return wsPacket;
        }

        byte[] array = ByteBufferUtils.readBytes(buffer, payloadLength);
        if (hasMask) {
            for (int i = 0; i < array.length; i++) {
                array[i] = (byte) (array[i] ^ mask[i % 4]);
            }
        }

        if (!fin) {
            return wsPacket;
        } else {
            int allLength = array.length;
            if (lastParts != null) {
                for (byte[] part : lastParts) {
                    allLength += part.length;
                }
                byte[] allByte = new byte[allLength];

                int offset = 0;
                for (byte[] part : lastParts) {
                    System.arraycopy(part, 0, allByte, offset, part.length);
                    offset += part.length;
                }
                System.arraycopy(array, 0, allByte, offset, array.length);
                array = allByte;
            }

            wsPacket.setBody(array);

            if (opCode == OpCode.BINARY) {

            } else {
                try {
                    String text = new String(array, Const.CHARSET);
                    wsPacket.setWsBodyText(text);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return wsPacket;
    }

}
