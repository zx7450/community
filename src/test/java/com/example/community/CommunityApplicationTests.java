package com.example.community;

import com.example.community.dao.Alphadao;
import com.example.community.service.AlphaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class) //设置配置类
class CommunityApplicationTests implements ApplicationContextAware {

//    @Test
//    void contextLoads() {
//    }

    private ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }

    @Test
    public void testApplicationcontext() {
        System.out.println(applicationContext);
        Alphadao alphadao=applicationContext.getBean(Alphadao.class);//从容器中获取bean
        System.out.println(alphadao.select());
        alphadao=applicationContext.getBean("alphahibernate",Alphadao.class);
        System.out.println(alphadao.select());
    }

    @Test
    public void testBeanManagement() {
        AlphaService alphaService=applicationContext.getBean(AlphaService.class);//容器在实例化和销毁前调用相关方法
        System.out.println(alphaService);

        alphaService=applicationContext.getBean(AlphaService.class);//容器只实例化一次，被spring容器管理的bean默认是单例，若要使用多例则应加@scope注解
        System.out.println(alphaService);
    }

    @Test
    public void testBeanConfig() {
        SimpleDateFormat simpleDateFormat=
                applicationContext.getBean(SimpleDateFormat.class);
        System.out.println(simpleDateFormat.format(new Date()));
    }

    @Autowired//依赖注入，把Alphadao注入给该属性
    @Qualifier("alphahibernate") //默认注入的是优先级高的alphamybatis，若要注入alphahibernate则添加此注解
    private Alphadao alphadao;

    @Autowired
    private AlphaService alphaService;
    @Autowired
    private SimpleDateFormat simpleDateFormat;
    @Test
    public void testDI() {
        System.out.println(alphadao);
        System.out.println(alphaService);
        System.out.println(simpleDateFormat);
    }
}
