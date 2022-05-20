package com.example.community.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zx
 * @date 2022/5/20 9:27
 */
@Target(ElementType.METHOD)//表示该注解用于描述方法
@Retention(RetentionPolicy.RUNTIME)//运行时生效
public @interface LoginRequired {
}
