package com.example.srpcconsumer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SrpcConsumerApplicationTests {

    @Test
    void contextLoads() {
        try {
            Class<?> clazz = Class.forName("com.sifan.boot.starter.User");
            System.out.println(clazz.getTypeName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
