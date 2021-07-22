package com.yuu.remoting.transport;

import com.yuu.remoting.model.RemotingRequest;

/**
 * 发送请求的接口
 */
public interface RpcRequestTransport {

    /**
     * 发送RemotingRequest对象，返回RemotingResponse对象
     * @param request
     * @return
     */
    Object sendRpcRequest(RemotingRequest request);

}
