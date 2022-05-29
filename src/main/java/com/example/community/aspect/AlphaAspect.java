package com.example.community.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @author zx
 * @date 2022/5/30 0:51
 */
//示例代码
//@Component
//@Aspect
public class AlphaAspect {

    //切点，说明哪些bean哪些方法是处理目标，第一个星代表任意返回值，com.example.community.service下的所有类的所有方法，(..)表示所有参数
    @Pointcut("execution(* com.example.community.service.*.* (..))")
    public void pointout() {
    }

    @Before("pointout()")//连接点开始，针对pointout()有效
    public void before() {
        System.out.println("before");
    }

    @After("pointout()")//连接点后
    public void after() {
        System.out.println("after");
    }

    @AfterReturning("pointout()")//连接点返回值后
    public void afterReturning() {
        System.out.println("afterReturning");
    }

    @AfterThrowing("pointout()")//抛异常时织入
    public void afterThrowing() {
        System.out.println("afterThrowing");
    }

    @Around("pointout()")//前后都织入
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("around before");//调用目标组件前做的事
        Object obj = joinPoint.proceed();//调用处理目标组件的方法
        System.out.println("around after");//调用目标组件后做的事
        return obj;
    }
}
