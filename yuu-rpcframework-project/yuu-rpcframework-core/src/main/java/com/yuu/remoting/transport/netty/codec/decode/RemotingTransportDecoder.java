package com.yuu.remoting.transport.netty.codec.decode;

import com.yuu.remoting.constants.RpcConstants;
import com.yuu.remoting.model.RemotingRequest;
import com.yuu.remoting.model.RemotingResponse;
import com.yuu.remoting.model.RemotingTransport;
import com.yuu.remoting.serializer.Serializer;
import com.yuu.remoting.serializer.SerializerHolder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

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
public class RemotingTransportDecoder extends LengthFieldBasedFrameDecoder {

    public RemotingTransportDecoder() {
        this(RpcConstants.MAX_FRAME_LENGTH, 4, 4, -8, 0);
    }

    public RemotingTransportDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength,
                             int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        Object o = super.decode(ctx, in);
        if (o instanceof ByteBuf) {
            ByteBuf frame = (ByteBuf) o;
            // 说明有对象传入，需要进行解码
            if (frame.readableBytes() >= RpcConstants.TOTAL_LENGTH) {
                try {
                    return decodeFrame(frame);
                } catch (Exception e) {
                    log.error("Decode frame error!", e);
                    throw e;
                } finally {
                    frame.release();
                }
            }
        }
        return o;
    }

    private Object decodeFrame(ByteBuf in) {
        // 检查魔数和版本号
        checkMagicNumber(in);
        checkVersion(in);
        // 获取数据的大小
        int fullLength = in.readInt();

        // 还原传输对象RemotingTransport
        byte messageType = in.readByte();
        byte codecType = in.readByte();
        int requestId = in.readInt();
        RemotingTransport rpcMessage = RemotingTransport.builder()
                .codec(codecType)
                .requestId(requestId)
                .messageType(messageType).build();

        // 如果是心跳包
        if (messageType == RpcConstants.HEARTBEAT_REQUEST_TYPE) {
            rpcMessage.setData(RpcConstants.PING);
            return rpcMessage;
        }
        if (messageType == RpcConstants.HEARTBEAT_RESPONSE_TYPE) {
            rpcMessage.setData(RpcConstants.PONG);
            return rpcMessage;
        }

        // 获取数据
        int bodyLength = fullLength - RpcConstants.HEAD_LENGTH;
        if (bodyLength > 0) {
            byte[] bs = new byte[bodyLength];
            in.readBytes(bs);
            // 反序列化对象
            log.info("codec name: ProtoStuff ");
            Serializer serializer = SerializerHolder.serializerImpl();
            if (messageType == RpcConstants.REQUEST_TYPE) {
                RemotingRequest tmpValue = serializer.deserialize(bs, RemotingRequest.class);
                rpcMessage.setData(tmpValue);
            } else {
                RemotingResponse tmpValue = serializer.deserialize(bs, RemotingResponse.class);
                rpcMessage.setData(tmpValue);
            }
        }
        return rpcMessage;

    }

    private void checkVersion(ByteBuf in) {
        // read the version
        byte version = in.readByte();
        if (version != RpcConstants.VERSION) {
            throw new RuntimeException("version isn't compatible" + version);
        }
    }

    private void checkMagicNumber(ByteBuf in) {
        // read the first 3 bit, which is the magic number
        int len = RpcConstants.MAGIC_NUMBER.length;
        byte[] tmp = new byte[len];
        in.readBytes(tmp);
        for (int i = 0; i < len; i++) {
            if (tmp[i] != RpcConstants.MAGIC_NUMBER[i]) {
                throw new IllegalArgumentException("Unknown magic code: " + Arrays.toString(tmp));
            }
        }
    }
}
