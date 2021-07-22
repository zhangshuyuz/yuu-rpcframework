package com.yuu.remoting.model;

import lombok.*;

import java.io.Serializable;

/**
 * 远程数据传输的唯一对象
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class RemotingTransport implements Serializable {

    //rpc message type
    private byte messageType;
    //request id
    private int requestId;
    //request data
    private Object data;
    //codec type
    private byte codec;

}
