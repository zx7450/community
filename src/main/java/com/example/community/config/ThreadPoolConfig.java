package com.example.community.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author zx
 * @date 2022/7/13 15:50
 */
@Configuration
@EnableScheduling//启用定时任务
@EnableAsync
public class ThreadPoolConfig {
}
