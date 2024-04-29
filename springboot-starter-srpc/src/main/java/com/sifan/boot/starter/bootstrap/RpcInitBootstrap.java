package com.sifan.boot.starter.bootstrap;

import com.sifan.boot.starter.annotation.EnableRpc;
import com.sifan.srpc.server.NettyRPCServer;
import com.sifan.srpc.server.ServiceProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

@Slf4j
public class RpcInitBootstrap implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        boolean needServer = (boolean) importingClassMetadata
                .getAnnotationAttributes(EnableRpc.class.getName())
                .get("needServer");
        String host = (String) importingClassMetadata.getAnnotationAttributes(EnableRpc.class.getName()).get("host");
        int port = (int) importingClassMetadata.getAnnotationAttributes(EnableRpc.class.getName()).get("port");
        ServerServiceProvider.init(host, port);

        if (needServer) {
            log.info("RPC 服务器启动");
            ServiceProvider instance = ServerServiceProvider.getInstance();
            NettyRPCServer server = new NettyRPCServer(instance);

            Thread serverThread = new Thread(() -> {
                server.start(port);
            });
            serverThread.start();

            // 注册 NettyRPCServer bean
            registerNettyRPCServerBean(serverThread, registry);
        } else {
            log.info("RPC 客户端启动");
        }
    }

    private void registerNettyRPCServerBean(Thread server, BeanDefinitionRegistry registry) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(Thread.class, () -> server);
        registry.registerBeanDefinition("nettyRPCServer", builder.getBeanDefinition());
    }
}