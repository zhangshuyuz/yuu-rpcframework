package com.yuu.remoting.transport;

import com.yuu.common.spi.BaseServiceLoader;
import com.yuu.remoting.transport.RpcRequestTransport;

public class RpcRequestTransportHolder {

    private final static RpcRequestTransport rpcRequestTransport = BaseServiceLoader.load(com.yuu.remoting.transport.RpcRequestTransport.class);

    public static RpcRequestTransport rpcRequestTransportImpl() {return rpcRequestTransport;}
}
