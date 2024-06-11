package com.sifan.srpc.test;


import com.sifan.srpc.server.NettyRPCServer;
import com.sifan.srpc.server.RPCServer;
import com.sifan.srpc.server.ServiceProvider;
import com.sifan.srpc.test.service.BlogService;
import com.sifan.srpc.test.service.BlogServiceImpl;
import com.sifan.srpc.test.service.UserService;
import com.sifan.srpc.test.service.UserServiceImpl;

public class TestServer2 {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        BlogService blogService = new BlogServiceImpl();

        ServiceProvider serviceProvider = new ServiceProvider("127.0.0.1", 8900);
        serviceProvider.provideServiceInterface(userService);
        serviceProvider.provideServiceInterface(blogService);
        RPCServer RPCServer = new NettyRPCServer(serviceProvider);

        RPCServer.start(8900);
    }
}
