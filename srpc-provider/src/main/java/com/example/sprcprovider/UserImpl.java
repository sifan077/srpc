package com.example.sprcprovider;

import com.example.sprcprovider.service.UserService;
import com.sifan.boot.starter.User;
import com.sifan.boot.starter.annotation.RpcService;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RpcService
public class UserImpl implements User {

    @Autowired
    private UserService userService;

    @Override
    @Trace
    public String satHi(String name) {
        userService.list().forEach(System.out::println);
        System.out.println("调用了satHi方法");
        return "你好" + name;
    }



}
