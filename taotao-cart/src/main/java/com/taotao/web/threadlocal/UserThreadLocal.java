package com.taotao.web.threadlocal;

import com.taotao.sso.pojo.User;

/**
 * 保存用户信息的线程域容器
 * 
 * @author huge
 *
 */
public class UserThreadLocal {

    private static final ThreadLocal<User> tl = new ThreadLocal<>();

    /**
     * 保存用户信息
     * 
     * @param user
     */
    public static void set(User user) {
        tl.set(user);
    }

    /**
     * 获取用户信息
     * 
     * @param user
     */
    public static User get() {
        return tl.get();
    }

    /**
     * 清空用户信息
     * 
     * @param user
     */
    public static void remove() {
        tl.remove();
    }
}
