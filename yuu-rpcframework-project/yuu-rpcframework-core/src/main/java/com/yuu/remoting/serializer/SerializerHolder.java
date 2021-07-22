package com.yuu.remoting.serializer;

import com.yuu.common.spi.BaseServiceLoader;

/**
 * 序列化的Holder
 */
public final class SerializerHolder {

    // SPI
    private static final Serializer serializer = BaseServiceLoader.load(Serializer.class);

    public static Serializer serializerImpl() {
        return serializer;
    }

}
