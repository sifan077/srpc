package com.example.srpcconsumer;

import com.sifan.boot.starter.User;
import com.sifan.boot.starter.annotation.RpcReference;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app")
public class AppController {

    @RpcReference(interfaceClass = User.class)
    User user;

    @RequestMapping("/hello")
    @Trace
    public String hello() {
        return user.satHi("思凡");
    }
}
