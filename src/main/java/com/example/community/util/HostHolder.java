package com.example.community.util;

import com.example.community.entity.User;
import org.springframework.stereotype.Component;

/**
 * @author zx
 * @date 2022/5/18 10:03
 */
//起到容器的作用，持有用户信息，用于代替session对象
@Component
public class HostHolder {
    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user) {
        users.set(user);
    }

    public User getUser() {
        return users.get();
    }

    public void clear() {
        users.remove();
    }
}
