package com.yuu.registry.registryanddiscovery.zk;

import com.yuu.common.enums.RpcErrorMessageEnum;
import com.yuu.common.exception.RpcException;
import com.yuu.loadbalance.LoadBalance;
import com.yuu.loadbalance.LoadBalanceHolder;
import com.yuu.registry.registryanddiscovery.Discovery;
import com.yuu.registry.registryanddiscovery.zk.util.CuratorUtils;
import com.yuu.remoting.model.RemotingRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;

import java.net.InetSocketAddress;
import java.util.List;

@Slf4j
public class DiscoveryImpl implements Discovery {
    private final LoadBalance loadBalance;

    public DiscoveryImpl() {
        this.loadBalance = LoadBalanceHolder.loadBalanceImpl();
    }

    @Override
    public InetSocketAddress lookupService(RemotingRequest rpcRequest) {
        String rpcServiceName = rpcRequest.getRpcServiceName();
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        List<String> serviceUrlList = CuratorUtils.getChildrenNodes(zkClient, rpcServiceName);
        if (serviceUrlList == null || serviceUrlList.size() == 0) {
            throw new RpcException(RpcErrorMessageEnum.SERVICE_CAN_NOT_BE_FOUND, rpcServiceName);
        }
        // load balancing
        String targetServiceUrl = loadBalance.selectServiceAddress(serviceUrlList, rpcRequest);
        log.info("Successfully found the service address:[{}]", targetServiceUrl);
        String[] socketAddressArray = targetServiceUrl.split(":");
        String host = socketAddressArray[0];
        int port = Integer.parseInt(socketAddressArray[1]);
        return new InetSocketAddress(host, port);
    }
}
