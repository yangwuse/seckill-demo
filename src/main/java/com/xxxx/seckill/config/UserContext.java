package com.xxxx.seckill.config;

import com.xxxx.seckill.entity.User;

/**
 *
 * @author yangWu
 * @date 2022/4/29 10:32 PM
 * @version 1.0
 */
public class ThreadContext {

    private static ThreadLocal<User> userHolder = new ThreadLocal<>();

    public static void setUser(User user) {
        userHolder.set(user);
    }

    public static User getUser() {
        return userHolder.get();
    }
}
