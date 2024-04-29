package com.example.sprcprovider;

import com.sifan.boot.starter.annotation.EnableRpc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRpc(needServer = true, port = 9999)
public class SrpcProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SrpcProviderApplication.class, args);
    }

}
