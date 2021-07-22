package com.yuu.loadbalance.impl;

import com.yuu.loadbalance.AbstractLoadBalance;
import com.yuu.remoting.model.RemotingRequest;

import java.util.List;
import java.util.Random;

public class RandomLoadBalance extends AbstractLoadBalance {


    @Override
    protected String doSelect(List<String> serviceAddresses, RemotingRequest rpcRequest) {
        Random random = new Random();
        return serviceAddresses.get(random.nextInt(serviceAddresses.size()));
    }

}
