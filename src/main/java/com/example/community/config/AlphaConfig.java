package com.example.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

/**
 * @author zx
 * @date 2022/5/10 22:10
 */
@Configuration//表面是配置类
public class AlphaConfig {
    @Bean//方法返回的对象将被装到容器里
    public SimpleDateFormat simpleDateFormat () {//方法名就是bean的名字
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
}
