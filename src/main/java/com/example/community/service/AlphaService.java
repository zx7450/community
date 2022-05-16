package com.example.community.service;

import com.example.community.dao.Alphadao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author zx
 * @date 2022/5/10 21:56
 */
@Service//增加service注解给容器管理
//@Scope("prototype") //每次访问bean都会补充新的实例
public class AlphaService {

    @Autowired
    private Alphadao alphadao;

    public AlphaService() {
        System.out.println("实例化alphaservice");
    }
    @PostConstruct//方法会在构造器之后调用
    public void init() {
        System.out.println("初始化alphaservice");
    }

    @PreDestroy//在销毁对象之前调用
    public void destroy() {
        System.out.println("销毁alphaservice");
    }

    public String find() {
        return alphadao.select();
    }
}
