package com.yuu.registry.registryanddiscovery;

import com.yuu.common.spi.BaseServiceLoader;

/**
 * 服务注册和服务订阅的SPI
 */
public class RegistryAndDiscoveryHolder {

    private final static Registry registry = BaseServiceLoader.load(Registry.class);
    private final static Discovery discovery = BaseServiceLoader.load(Discovery.class);

    public static Registry registryImpl() {
        return registry;
    }

    public static Discovery discoveryImpl() {
        return discovery;
    }

}
