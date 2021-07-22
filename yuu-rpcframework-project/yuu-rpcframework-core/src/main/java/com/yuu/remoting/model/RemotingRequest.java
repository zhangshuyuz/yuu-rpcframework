package com.yuu.remoting.model;

import lombok.*;

import java.io.Serializable;

/**
 * 远程网络请求
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class RemotingRequest implements Serializable {

    private static final long serialVersionUID = 1905122041950251207L;
    private String requestId;
    private String interfaceName;
    private String methodName;
    private Object[] parameters;
    private Class<?>[] paramTypes;
    private String version;
    private String group;

    public String getRpcServiceName() {
        return this.getInterfaceName() + this.getGroup() + this.getVersion();
    }

}
