package com.yuu.remoting.transport.netty.codec.encode;

import com.yuu.remoting.constants.RpcConstants;
import com.yuu.remoting.model.RemotingTransport;
import com.yuu.remoting.serializer.Serializer;
import com.yuu.remoting.serializer.SerializerHolder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;


/**
 *               3          1             4                  1          1           4
 *   +-----+-----+-----+-----+--------+----+----+----+------+-----------+-------+----- --+-----+
 *   |     magic       |version | full length         | messageType| codec|   RequestId       |
 *   +-----------------------+--------+---------------------+-----------+-----------+-----------+
 *   |                                                                                           |
 *   |                                         body                                               |
 *   |                                                                                            |
 *   |                                        ... ...                                             |
 *   +--------------------------------------------------------------------------------------------+
 */
@Slf4j
public class RemotingTransportEncoder extends MessageToByteEncoder<RemotingTransport> {

    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger(0);

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RemotingTransport remotingTransport, ByteBuf byteBuf) throws Exception {
        try {
            // 写入magicNum
            byteBuf.writeBytes(RpcConstants.MAGIC_NUMBER);
            // 写入版本号
            byteBuf.writeByte(RpcConstants.VERSION);
            // 预留4个字节，用来填入body的总字节数
            byteBuf.writerIndex(byteBuf.writerIndex() + 4);
            // 获取数据类型，并写入
            byte messageType = remotingTransport.getMessageType();
            byteBuf.writeByte(messageType);
            // 获取并写入序列化类型
            byteBuf.writeByte(remotingTransport.getCodec());
            // 写入本次请求的requestId
            byteBuf.writeInt(ATOMIC_INTEGER.getAndIncrement());

            // 填入body的总字节数
            byte[] bodyBytes = null;
            int fullLength = RpcConstants.HEAD_LENGTH;
            // 如果不是netty的心跳包,fullLength = head length + body length
            if (messageType != RpcConstants.HEARTBEAT_REQUEST_TYPE
                    && messageType != RpcConstants.HEARTBEAT_RESPONSE_TYPE) {
                // 序列化RemotingTransport中的data
                log.info("codec name: protoStuff ");
                Serializer serializer = SerializerHolder.serializerImpl();
                bodyBytes = serializer.serialize(remotingTransport.getData());
                fullLength += bodyBytes.length;
            }

            if (bodyBytes != null) {
                byteBuf.writeBytes(bodyBytes);
            }
            int writeIndex = byteBuf.writerIndex();
            byteBuf.writerIndex(writeIndex - fullLength + RpcConstants.MAGIC_NUMBER.length + 1);
            byteBuf.writeInt(fullLength);
            byteBuf.writerIndex(writeIndex);

        } catch (Exception e) {
            log.error("Encode request error!", e);
        }
    }
}
