package com.yuu.remoting.transport.netty.client;

import com.yuu.remoting.model.RemotingResponse;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 异步通知
 */
public class UnprocessedRequests {

    private static final Map<String, CompletableFuture<RemotingResponse<Object>>> UNPROCESSED_RESPONSE_FUTURES = new ConcurrentHashMap<>();

    public void put(String requestId, CompletableFuture<RemotingResponse<Object>> future) {
        UNPROCESSED_RESPONSE_FUTURES.put(requestId, future);
    }

    public void complete(RemotingResponse<Object> RemotingResponse) {
        CompletableFuture<RemotingResponse<Object>> future = UNPROCESSED_RESPONSE_FUTURES.remove(RemotingResponse.getRequestId());
        if (null != future) {
            future.complete(RemotingResponse);
        } else {
            throw new IllegalStateException();
        }
    }
}
