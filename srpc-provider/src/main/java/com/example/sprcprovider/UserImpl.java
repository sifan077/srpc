package com.example.sprcprovider;

import com.sifan.boot.starter.User;
import com.sifan.boot.starter.annotation.RpcService;
import org.springframework.stereotype.Service;

@Service
@RpcService
public class UserImpl implements User {
    @Override
    public String satHi(String name) {
        System.out.println("调用了satHi方法");
        return "你好" + name;
    }
}