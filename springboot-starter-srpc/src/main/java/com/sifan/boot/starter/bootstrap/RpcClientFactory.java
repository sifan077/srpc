package com.sifan.boot.starter.bootstrap;

import com.sifan.srpc.client.NettyRPCClient;

public class RpcClientFactory {
    private static final NettyRPCClient INSTANCE = new NettyRPCClient();

    private RpcClientFactory() {
    }

    public static NettyRPCClient getInstance() {
        return INSTANCE;
    }
}
