package com.example.srpcconsumer;

import com.sifan.boot.starter.annotation.EnableRpc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRpc(needServer = false)
public class SrpcConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SrpcConsumerApplication.class, args);
    }

}
