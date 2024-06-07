package com.example.sprcprovider.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.sprcprovider.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}
