/*
 * Created by IntelliJ IDEA.
 * User: 思凡
 * Date: 2022/6/7
 * Time: 9:25
 * Describe:
 */

package com.example.sprcprovider.entity;

import com.baomidou.mybatisplus.annotation.*;


@TableName("user")
public class User {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    @TableField(select = false)
    private Integer age;
    private String email;
    // 逻辑删除字段，标记是否被删除
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;

    @TableField(exist = false)
    private String onLine;

    public User() {
    }

    public User(Integer id, String name, Integer age, String email, Integer deleted, String onLine) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.email = email;
        this.deleted = deleted;
        this.onLine = onLine;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public String getOnLine() {
        return onLine;
    }

    public void setOnLine(String onLine) {
        this.onLine = onLine;
    }
}
