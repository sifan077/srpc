package com.sifan.srpc.server;

import com.sifan.srpc.codec.MyDecode;
import com.sifan.srpc.codec.MyEncode;
import com.sifan.srpc.codec.Serializer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import lombok.AllArgsConstructor;

/**
 * 初始化，主要负责序列化的编码解码， 需要解决netty的粘包问题
 */
@AllArgsConstructor
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {
    private ServiceProvider serviceProvider;

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
        pipeline.addLast(new NettyRPCServerHandler(serviceProvider));
    }
}
