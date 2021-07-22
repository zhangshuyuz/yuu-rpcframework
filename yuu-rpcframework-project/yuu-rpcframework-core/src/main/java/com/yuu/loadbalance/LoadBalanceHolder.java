package com.yuu.loadbalance;

import com.yuu.common.spi.BaseServiceLoader;

public class LoadBalanceHolder {

    private final static LoadBalance loadBalance = BaseServiceLoader.load(com.yuu.loadbalance.LoadBalance.class);

    public static LoadBalance loadBalanceImpl() {return loadBalance;}

}
