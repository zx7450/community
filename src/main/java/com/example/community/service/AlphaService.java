package com.example.community.service;

import com.example.community.dao.Alphadao;
import com.example.community.dao.DisscussPostMapper;
import com.example.community.dao.UserMapper;
import com.example.community.entity.DiscussPost;
import com.example.community.entity.User;
import com.example.community.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Date;

/**
 * @author zx
 * @date 2022/5/10 21:56
 */
@Service//增加service注解给容器管理
//@Scope("prototype") //每次访问bean都会补充新的实例
public class AlphaService {

    @Autowired
    private Alphadao alphadao;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DisscussPostMapper disscussPostMapper;

    @Autowired
    private TransactionTemplate transactionTemplate;

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

    //声明式事务，通过注解声明某方法的事务特征
    //3个常用的传播机制，解决交叉问题，如业务方法A调用业务方法B，而两个方法都加了Transactional注解管理事务，那么应该以哪个业务机制为准的问题
    //REQUIRED:支持当前事务（外部事务，如A调B则A就是外部事务），如果不存在则创建新事物(如果A有事务就按A来，否则新建事务)
    //REQUIRES_NEW:创建一个新事物，并且暂停当前事务（外部事务）
    //NESTED:如果当前存在事务（外部事务），则嵌套在该事务中执行（即B有独立的提交和回滚），否则就会和REQUIRED一样
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)//读取已提交的数据
    public Object save1() {
        //新增用户
        User user = new User();
        user.setUsername("alpha");
        user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
        user.setPassword(CommunityUtil.md5("123" + user.getSalt()));
        user.setEmail("alpha@qq.com");
        user.setHeaderUrl("http://static.nowcoder.com/images/head/99t.png");
        user.setCreateTime(new Date());
        userMapper.insertUser(user);
        //新增帖子
        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle("Hello");
        post.setContent("新人报道！");
        post.setCreateTime(new Date());
        disscussPostMapper.insertDiscussPost(post);

        Integer.valueOf("abc");//报错，要测试的是前面的数据有没有插入数据库，结果数据库没有影响（发生异常后回滚了）

        return "OK";
    }

    //编程式事务，通过TransactionTemplate（由spring创建，直接注入即可）管理事务，并通过它执行数据库的操作
    public Object save2() {
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);//设置隔离级别
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);//设置传播机制
        
        return transactionTemplate.execute(new TransactionCallback<Object>() {
            @Override
            public Object doInTransaction(TransactionStatus status) {//回调方法，由Template底层自动调用
                //新增用户
                User user = new User();
                user.setUsername("beta");
                user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
                user.setPassword(CommunityUtil.md5("123" + user.getSalt()));
                user.setEmail("beta@qq.com");
                user.setHeaderUrl("http://static.nowcoder.com/images/head/999t.png");
                user.setCreateTime(new Date());
                userMapper.insertUser(user);
                //新增帖子
                DiscussPost post = new DiscussPost();
                post.setUserId(user.getId());
                post.setTitle("你好");
                post.setContent("我是新人！");
                post.setCreateTime(new Date());
                disscussPostMapper.insertDiscussPost(post);

                Integer.valueOf("abc");//报错，要测试的是前面的数据有没有插入数据库，结果数据库没有影响，和save1()一样也回滚了
                return "ok";
            }
        });
    }
}
