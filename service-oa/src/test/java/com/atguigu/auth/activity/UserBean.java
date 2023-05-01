package com.atguigu.auth.activity;

import org.springframework.stereotype.Component;

/**
 * @author bestHandsomePeople
 * @creat 2023-04-18-12:11
 */
@Component
public class UserBean {
    public String getUsername(int id) {
        if (id == 1) {
            return "lihua";
        }
        if (id == 2) {
            return "kangkang";
        }
        return "admin";

    }
}
