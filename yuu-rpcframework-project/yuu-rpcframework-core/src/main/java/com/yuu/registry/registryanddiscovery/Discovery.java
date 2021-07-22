package com.yuu.registry.registryanddiscovery;

import com.yuu.remoting.model.RemotingRequest;

import java.net.InetSocketAddress;

/**
 * 注册中心获取
 */
public interface Discovery {

    /**
     * 获取
     * @param rpcRequest
     * @return
     */
    InetSocketAddress lookupService(RemotingRequest rpcRequest);

}
