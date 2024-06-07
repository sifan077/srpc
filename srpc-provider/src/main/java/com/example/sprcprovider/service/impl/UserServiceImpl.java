/*
 * Created by IntelliJ IDEA.
 * User: 思凡
 * Date: 2022/6/7
 * Time: 17:59
 * Describe:
 */

package com.example.sprcprovider.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sprcprovider.dao.UserMapper;
import com.example.sprcprovider.entity.User;
import com.example.sprcprovider.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
