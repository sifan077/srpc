package com.example.srpcconsumer;

import com.sifan.boot.starter.User;
import com.sifan.boot.starter.annotation.RpcReference;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SrpcConsumerApplicationTests {

    @RpcReference(interfaceClass = User.class)
    private User user;

    @Test
    void contextLoads() {
        String hiMessage = user.satHi("晓月老板");
        System.out.println(hiMessage);
    }



    @Autowired
    private AppController appController;

    @Test
    void test2() {
        System.out.println(appController.hello());
    }
}
