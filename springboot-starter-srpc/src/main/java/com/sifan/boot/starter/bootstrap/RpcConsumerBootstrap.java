package com.sifan.boot.starter.bootstrap;

import com.sifan.boot.starter.annotation.RpcReference;
import com.sifan.srpc.client.RPCClientProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;

@Slf4j
public class RpcConsumerBootstrap implements BeanPostProcessor {

    /**
     * Bean 初始化后执行，注入服务
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        // 遍历对象的所有属性
        Field[] declaredFields = beanClass.getDeclaredFields();
        for (Field field : declaredFields) {
            // 如果字段有RpcReference注解
            RpcReference rpcReference = field.getAnnotation(RpcReference.class);
            if (rpcReference != null) {
                // 为属性生成代理对象
                Class<?> interfaceClass = rpcReference.interfaceClass();
                if (interfaceClass == void.class) {
                    interfaceClass = field.getType();
                }
//                log.info("interfaceClass:{}", interfaceClass.getTypeName());
//                log.info("interfaceClass:{}", interfaceClass.getName());
                // 构建一个使用java Socket/ netty/....传输的客户端
//                RPCClient rpcClient = new NettyRPCClient();
                // 把这个客户端传入代理客户端
                RPCClientProxy rpcClientProxy = new RPCClientProxy(RpcClientFactory.getInstance());
                // 我不想要创建代理对象，直接创建代理对象
//                Object proxyObject = rpcClientProxy.getProxy(interfaceClass);
//                log.info("代理对象：{}", proxyObject);
                try {
                    // 允许访问私有属性
                    field.setAccessible(true);
                    // 把代理对象注入到属性中
                    field.set(bean, rpcClientProxy.getProxy(interfaceClass, interfaceClass.getName()));
                    log.info("注入代理对象成功");
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("为字段注入代理对象失败", e);
                }
            }
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }

}