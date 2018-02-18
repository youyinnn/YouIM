package com.github.youyinnn.common;

import cn.youyinnn.helloworld.common.MyPacket;
import com.github.youyinnn.common.packet.BasePacket;
import org.tio.core.ChannelContext;
import org.tio.core.GroupContext;
import org.tio.core.exception.AioDecodeException;
import org.tio.core.intf.AioHandler;
import org.tio.core.intf.Packet;

import java.nio.ByteBuffer;

/**
 * @author youyinnn
 */
public abstract class AbstractAioHandler implements AioHandler {

    /**
     * 解码：把接收到的ByteBuffer，解码成应用可以识别的业务消息包
     * 总的消息结构：消息头 + 消息体
     * 消息头结构：    4个字节，存储消息体的长度
     * 消息体结构：   对象的json串的byte[]
     * @param byteBuffer
     * @param channelContext
     * @return
     * @throws AioDecodeException
     */
    @Override
    public Packet decode(ByteBuffer byteBuffer, ChannelContext channelContext) throws AioDecodeException {
        int readableLength = byteBuffer.limit() - byteBuffer.position();
        //收到的数据不足以组成业务包, 则返回null告诉框架数据不够
        if (readableLength < MyPacket.HEADER_LENGTH) {
            return null;
        }

        //读取消息类型
        int type = byteBuffer.get();
        //读取的消息长度
        int bodyLength = byteBuffer.getInt();

        /*
         * 如果数据不正确 则抛出异常
         * 这里允许bodyLength为0 因为我们发送的心跳检测包是null的body
         */
        if (bodyLength < 0) {
            throw new AioDecodeException("bodyLength [" + bodyLength + "] is not right, remote:" + channelContext.getClientNode());
        }

        //计算本次需要的数据长度
        int neededLength = BasePacket.HEADER_LENGTH + bodyLength;
        //收到的数据是否足够组包
        int isDataEnough = readableLength - neededLength;
        //不够消息长度(剩下的buffer组不了消息体)
        if (isDataEnough < 0) {
            return null;
        } else {
            byte[] dest = null;
            if (bodyLength > 0) {
                //够消息长度, 则组包
                dest = new byte[bodyLength];
                byteBuffer.get(dest);
            }
            return new BasePacket((byte) type, dest);
        }
    }

    /**
     * 编码:  把业务消息编码为课发送的ByteBuffer
     * 总的消息结构:  消息头 + 消息体
     * 消息头结构:   4字节,存储消息体的长度
     * 消息体结构:   对象的json串的byte[]
     *
     * @param packet
     * @param groupContext
     * @param channelContext
     * @return
     */
    @Override
    public ByteBuffer encode(Packet packet, GroupContext groupContext, ChannelContext channelContext) {
        BasePacket basePacket = (BasePacket) packet;
        byte[] body = basePacket.getMsgBody();
        int bodyLen = 0;
        if (body != null) {
            bodyLen = body.length;
        }

        //bytebuffer的总长度 = 消息头长度 + 消息体长度
        int allLen = BasePacket.HEADER_LENGTH + bodyLen;
        //创建一个新的bytebuffer
        ByteBuffer buffer = ByteBuffer.allocate(allLen);
        //设置字节序
        buffer.order(groupContext.getByteOrder());

        //写入消息类型
        buffer.put(basePacket.getMsgType());
        //写入消息体长度
        buffer.putInt(bodyLen);

        //写入消息体
        if (body != null) {
            buffer.put(body);
        }

        return buffer;
    }

}
