package com.sifan.srpc.server;

import com.sifan.srpc.common.RPCRequest;
import com.sifan.srpc.common.RPCResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 因为是服务器端，我们知道接受到请求格式是RPCRequest
 * Object类型也行，强制转型就行
 */
@AllArgsConstructor
@NoArgsConstructor
public class NettyRPCServerHandler extends SimpleChannelInboundHandler<RPCRequest> {
    private ServiceProvider serviceProvider;


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RPCRequest msg) throws Exception {
        System.out.println("msg="+msg);
        RPCResponse response = getResponse(msg);
        ctx.writeAndFlush(response);
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    RPCResponse getResponse(RPCRequest request) {
        // 得到服务名
        String interfaceName = request.getInterfaceName();

        // 得到服务端相应服务实现类
        Object service = serviceProvider.getService(interfaceName);
        // 反射调用方法
        Method method = null;
        try {
            ClassLoader classLoader = this.getClass().getClassLoader();
            Class<?> clazz = classLoader.loadClass(interfaceName);
            Class<?>[] classes = new Class[request.getParamsTypes().length];
            for (int i = 0; i < classes.length; i++) {
                classes[i] = classLoader.loadClass(request.getParamsTypes()[i]);
            }

            method = clazz.getMethod(request.getMethodName(), classes);
            if (classes.length == 0) method = clazz.getMethod(request.getMethodName());

            if (request.getParams() != null) {
                for (int i = 0; i < request.getParams().length; i++) {
                    Object param = request.getParams()[i];
                    // 如果参数是map类型，那么需要将map转为对象
                    if (param instanceof Map) {
                        // 按照参数类型request.getParamsTypes对应的，将map转为对象
                        param = mapToObject(param, classes[i]);
                        if (param == null) throw new NoSuchFieldException("对象参数不匹配");
                    }
                    request.getParams()[i] = param;
                }
            }
//            method = service.getClass().getMethod(request.getMethodName(), request.getParamsTypes());
            System.out.println(method.getName());
            Object invoke = method.invoke(service, request.getParams());
            return RPCResponse.success(invoke);
        } catch (IllegalAccessException | InvocationTargetException | ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
            System.out.println("远程方法执行错误");
            return RPCResponse.fail();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Object mapToObject(Object param, Class<?> paramType) {
        try {
            // Assuming param is a Map
            if (param instanceof Map) {
                // 如果是Map类型，直接返回
                if (paramType.equals(Map.class)) return param;
                Map<String, Object> paramMap = (Map<String, Object>) param;

                // Assuming paramType has a no-argument constructor
                Object obj = paramType.getDeclaredConstructor().newInstance();

                // Assuming the class has setters for fields
                for (Field field : paramType.getDeclaredFields()) {
                    field.setAccessible(true);
                    if (paramMap.containsKey(field.getName())) {
                        field.set(obj, paramMap.get(field.getName()));
                    }
                }
                return obj;
            }
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
