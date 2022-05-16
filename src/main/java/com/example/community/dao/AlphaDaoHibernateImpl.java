package com.example.community.dao;

import org.springframework.stereotype.Repository;

/**
 * @author zx
 * @date 2022/5/10 21:33
 */

@Repository("alphahibernate") //给bean定义名字，以便特定情况下使用
public class AlphaDaoHibernateImpl implements Alphadao{
    @Override
    public String select() {
        return "Hibernate";
    }
}
