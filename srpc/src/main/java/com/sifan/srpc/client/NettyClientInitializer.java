package com.sifan.srpc.client;

import com.sifan.srpc.codec.*;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * 同样的与服务端解码和编码格式
 */
public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 使用自定义的编解码器
        pipeline.addLast(new MyDecode());
        // 编码需要传入序列化器，默认可通过系统属性/环境变量配置（srpc.serializer）
//        pipeline.addLast(new MyEncode(new JsonSerializer()));
//        pipeline.addLast(new MyEncode(new HessianSerializer()));
//        pipeline.addLast(new MyEncode(new KryoSerializer()));
        pipeline.addLast(new MyEncode(Serializer.getDefaultSerializer()));
        pipeline.addLast(new NettyClientHandler());
    }
}
