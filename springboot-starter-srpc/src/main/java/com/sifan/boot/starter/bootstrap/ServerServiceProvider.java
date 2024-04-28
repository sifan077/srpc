package com.sifan.boot.starter.bootstrap;

import com.sifan.srpc.server.ServiceProvider;

public class ServerServiceProvider {
    private static final ServiceProvider INSTANCE = new ServiceProvider("127.0.0.1", 8888);

    private ServerServiceProvider() {
        // 私有构造函数，防止外部实例化
    }

    public static ServiceProvider getInstance() {
        return INSTANCE;
    }
}
