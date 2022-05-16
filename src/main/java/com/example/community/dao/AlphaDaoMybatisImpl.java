package com.example.community.dao;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

/**
 * @author zx
 * @date 2022/5/10 21:46
 */
@Repository
@Primary//两个都是Alphadao的实现类，加了@Primary注解的bean有更高的优先级被装配
public class AlphaDaoMybatisImpl implements Alphadao{

    @Override
    public String select() {
        return "Mybatis";
    }
}
