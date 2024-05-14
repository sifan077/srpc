package com.sifan.srpc.client;


import com.sifan.srpc.common.RPCRequest;
import com.sifan.srpc.common.RPCResponse;
import com.sifan.srpc.register.ConsulRegister;
import com.sifan.srpc.register.NacosRegister;
import com.sifan.srpc.register.ServiceRegister;
import com.sifan.srpc.register.ZkServiceRegister;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;
import java.util.HashMap;

/**
 * 实现RPCClient接口
 */
public class NettyRPCClient implements RPCClient {
    private static final Bootstrap bootstrap;
    private static final EventLoopGroup eventLoopGroup;

    // netty客户端初始化，重复使用
    static {
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                .handler(new NettyClientInitializer());
    }

    private String host;
    private int port;
    private ServiceRegister serviceRegister;

    private HashMap<String, Channel> channelFutureMap;

    public NettyRPCClient() {
//        this.serviceRegister = new ZkServiceRegister();
//        this.serviceRegister = new NacosRegister();
        this.serviceRegister = new ConsulRegister();

        this.channelFutureMap = new HashMap<>();
    }

    private Channel getChanel(String interfaceName) {
        InetSocketAddress address = serviceRegister.serviceDiscovery(interfaceName);
        if (address == null) {
            throw new RuntimeException("不存在正在运行的服务");
        }
        host = address.getHostName();
        port = address.getPort();
        try {
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            Channel channel = channelFuture.channel();
            channelFutureMap.put(interfaceName, channel);
            return channel;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 这里需要操作一下，因为netty的传输都是异步的，你发送request，会立刻返回一个值， 而不是想要的相应的response
     */
    @Override
    public RPCResponse sendRequest(RPCRequest request) {

//        InetSocketAddress address = serviceRegister.serviceDiscovery(request.getInterfaceName());
//        if (address == null) {
//            throw new RuntimeException("不存在正在运行的服务");
//        }
//        host = address.getHostName();
//        port = address.getPort();
        try {
//            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
//            Channel channel = channelFuture.channel();
            Channel channel = channelFutureMap.get(request.getInterfaceName());
            // 如果channel为空，或者不活跃，重新获取channel
            if (channel == null || !channel.isActive()) {
                channel = getChanel(request.getInterfaceName());
            }
            if (channel == null) {
                throw new Exception("未在注册中心获取到服务");
            }
            // 发送数据
            channel.writeAndFlush(request);
            channel.closeFuture().sync();
            // 阻塞的获得结果，通过给channel设计别名，获取特定名字下的channel中的内容（这个在handler中设置）
            // AttributeKey是，线程隔离的，不会由线程安全问题。
            // 实际上不应通过阻塞，可通过回调函数，后面可以再进行优化
            AttributeKey<RPCResponse> key = AttributeKey.valueOf("RPCResponse");
            RPCResponse response = channel.attr(key).get();
//            System.out.println(response);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void close() {
        eventLoopGroup.shutdownGracefully();
        bootstrap.group().shutdownGracefully();
        channelFutureMap.clear();
    }
}