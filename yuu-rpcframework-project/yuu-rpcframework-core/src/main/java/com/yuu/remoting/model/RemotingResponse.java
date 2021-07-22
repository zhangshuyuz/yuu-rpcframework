package com.yuu.remoting.model;

import com.yuu.common.enums.RpcResponseCodeEnum;
import lombok.*;

import java.io.Serializable;

/**
 * 远程网络请求的返回
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class RemotingResponse<T> implements Serializable {

    private static final long serialVersionUID = 715745410605631233L;
    private String requestId;
    /**
     * response code
     */
    private Integer code;
    /**
     * response message
     */
    private String message;
    /**
     * response body
     */
    private T data;

    public static <T> RemotingResponse<T> success(T data, String requestId) {
        RemotingResponse<T> response = new RemotingResponse<>();
        response.setCode(RpcResponseCodeEnum.SUCCESS.getCode());
        response.setMessage(RpcResponseCodeEnum.SUCCESS.getMessage());
        response.setRequestId(requestId);
        if (null != data) {
            response.setData(data);
        }
        return response;
    }

    public static <T> RemotingResponse<T> fail(RpcResponseCodeEnum RemotingResponseCodeEnum) {
        RemotingResponse<T> response = new RemotingResponse<>();
        response.setCode(RemotingResponseCodeEnum.getCode());
        response.setMessage(RemotingResponseCodeEnum.getMessage());
        return response;
    }

}
