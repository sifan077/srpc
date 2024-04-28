package com.sifan.boot.starter.bootstrap;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class CustomApplicationRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 在 Spring Boot 应用程序启动后执行你的逻辑
        System.out.println("在 Spring Boot 应用程序启动完成后执行一些操作");
    }
}