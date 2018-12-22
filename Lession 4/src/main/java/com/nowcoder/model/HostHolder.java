package com.nowcoder.model;

import org.springframework.stereotype.Component;

/**
 * Created by nowcoder on 2016/7/3.
 */
@Component
// 这里加了一个component的注解？
public class HostHolder {
    // 线程的存在保证不同用户同时访问（发出请求）时的后续链路的“可处理性”
    private static ThreadLocal<User> users = new ThreadLocal<User>();


    public User getUser() {
        return users.get();
    }

    public void setUser(User user) {
        users.set(user);
    }

    public void clear() {
        users.remove();;
    }
}
    // 这里的get与set需要自己写，不能Refactor
    // 下面返回到PassportInterceptor
