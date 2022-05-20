package com.example.community.controller.interceptor;

import com.example.community.annotation.LoginRequired;
import com.example.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author zx
 * @date 2022/5/20 9:30
 */
@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {//判断拦截的是不是方法
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            LoginRequired loginRequired = method.getAnnotation(LoginRequired.class);//从方法对象中取得LoginRequired注解
            if (loginRequired != null && hostHolder.getUser() == null) {//需要登录且没有登录
                response.sendRedirect(request.getContextPath() + "/login");//使用response进行重定向
                return false;
            }
        }
        return true;
    }
}
