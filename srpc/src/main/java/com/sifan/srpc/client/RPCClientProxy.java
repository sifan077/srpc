package com.sifan.srpc.client;


import com.sifan.srpc.common.RPCRequest;
import com.sifan.srpc.common.RPCResponse;
import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

@AllArgsConstructor
public class RPCClientProxy implements InvocationHandler {
    private RPCClient client;

    // jdk 动态代理， 每一次代理对象调用方法，会经过此方法增强（反射获取request对象，socket发送至客户端）
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // request的构建，使用了lombok中的builder，代码简洁
        String[] paramsTypes = new String[method.getParameterTypes().length];
        for (int i = 0; i < paramsTypes.length; i++) {
            paramsTypes[i] = method.getParameterTypes()[i].getName();
        }
        RPCRequest request = RPCRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .params(args)
                .paramsTypes(paramsTypes)
                .requestId(String.valueOf(UUID.randomUUID()))
                .build();
        //数据传输
//        System.out.println(request);
        RPCResponse response = client.sendRequest(request);
        //System.out.println(response);
        if (response == null) return null;
        return response.getData();
    }

    public <T> T getProxy(Class<T> clazz) {
        Object o = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
        return (T) o;
    }

    public <T> T getProxy(Class<T> clazz, String className) {
        ClassLoader classLoader = clazz.getClassLoader();
        Object o = null;
        try {
            o = Proxy.newProxyInstance(classLoader, new Class[]{classLoader.loadClass(className)}, this);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return (T) o;
    }
}
