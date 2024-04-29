package com.sifan.boot.starter.bootstrap;

import com.sifan.srpc.server.ServiceProvider;

public class ServerServiceProvider {

    private static ServiceProvider INSTANCE;

    private ServerServiceProvider() {
        // 私有构造函数，防止外部实例化
    }

    public static void init(String host, int port) {
        INSTANCE = new ServiceProvider(host, port);
    }

    public static ServiceProvider getInstance() {

        return INSTANCE;
    }
}
