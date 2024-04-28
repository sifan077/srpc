package com.sifan.srpc.server;


import com.sifan.srpc.service.BlogService;
import com.sifan.srpc.service.BlogServiceImpl;
import com.sifan.srpc.service.UserService;
import com.sifan.srpc.service.UserServiceImpl;

public class TestServer2 {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        BlogService blogService = new BlogServiceImpl();

        ServiceProvider serviceProvider = new ServiceProvider("127.0.0.1", 8900);
        // System.out.println("hahah");
        serviceProvider.provideServiceInterface(userService);
        serviceProvider.provideServiceInterface(blogService);
        RPCServer RPCServer = new NettyRPCServer(serviceProvider);

        RPCServer.start(8900);
    }
}
