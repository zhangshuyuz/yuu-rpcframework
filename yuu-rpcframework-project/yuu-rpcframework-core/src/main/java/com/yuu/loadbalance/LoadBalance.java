package com.yuu.loadbalance;

import com.yuu.remoting.model.RemotingRequest;

import java.util.List;

public interface LoadBalance {

    String selectServiceAddress(List<String> serviceAddresses, RemotingRequest rpcRequest);

}
