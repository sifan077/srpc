package com.sifan.boot.starter.bootstrap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

@Component
public class ApplicationShutdownListener implements ApplicationListener<ContextClosedEvent> {

    @Autowired
    @Qualifier("nettyRPCServer")
    private Thread nettyRPCServerThread;

    // 在应用关闭事件中停止 RPC 服务器等应用
    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        // 停止 RPC 服务器
        if (nettyRPCServerThread != null)
            nettyRPCServerThread.interrupt();
    }
}