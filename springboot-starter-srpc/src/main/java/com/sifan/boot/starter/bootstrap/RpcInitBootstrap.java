package com.sifan.boot.starter.bootstrap;

import com.sifan.boot.starter.annotation.EnableRpc;
import com.sifan.srpc.server.NettyRPCServer;
import com.sifan.srpc.server.ServiceProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

@Slf4j
public class RpcInitBootstrap implements ImportBeanDefinitionRegistrar {


    /**
     * Spring 初始化时执行，初始化 RPC 框架
     *
     * @param importingClassMetadata
     * @param registry
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        // 获取 EnableRpc 注解的属性值
        boolean needServer = (boolean) importingClassMetadata.getAnnotationAttributes(EnableRpc.class.getName())
                .get("needServer");

        // RPC 框架初始化（配置和注册中心）
        // 全局配置
        // 启动服务器
        if (needServer) {
            log.info("RPC 服务器启动");
//            ServiceProvider serviceProvider = new ServiceProvider("127.0.0.1", 8888);
            ServiceProvider instance = ServerServiceProvider.getInstance();
            NettyRPCServer server = new NettyRPCServer(instance);
            new Thread(() -> {
                server.start(8888);
            }).start();
        } else {
            log.info("RPC 客户端启动");
        }

    }
}
