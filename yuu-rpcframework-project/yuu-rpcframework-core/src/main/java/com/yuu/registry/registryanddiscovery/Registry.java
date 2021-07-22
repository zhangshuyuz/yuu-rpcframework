package com.yuu.registry.registryanddiscovery;

import java.net.InetSocketAddress;

/**
 * 注册中心注册
 */
public interface Registry {

    /**
     * 注册
     * @param rpcServiceName
     * @param inetSocketAddress
     */
    void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress);

}
