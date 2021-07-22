package com.yuu.remoting.serializer;

/**
 * 序列化和反序列化的接口
 */
public interface Serializer {

    /**
     * 序列化方法，将对象序列化为byte[]
     * @param obj
     * @param <T>
     * @return
     */
    <T> byte[] serialize(T obj);

    /**
     * 将序列化后的数据，重新反序列化为对象
     * @param bytes
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T deserialize(byte[] bytes, Class<T> clazz);
}
