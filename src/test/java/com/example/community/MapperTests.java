package com.example.community;

import com.example.community.dao.DisscussPostMapper;
import com.example.community.dao.UserMapper;
import com.example.community.entity.DiscussPost;
import com.example.community.entity.User;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author zx
 * @date 2022/5/12 0:09
 */
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTests {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DisscussPostMapper disscussPostMapper;

    @Test
    public void testselectUser(){
        User user=userMapper.selectById(101);
        System.out.println(user);
        user=userMapper.selectByName("liubei");
        System.out.println(user);
        user=userMapper.selectByEmail("nowcoder102@sina.com");
        System.out.println(user);
    }

    @Test
    public void testInsertUser() {
        User user=new User();
        user.setUsername("test");
        user.setPassword("123456");
        user.setSalt("abc");
        user.setEmail("test@qq.com");
        user.setHeaderUrl("http://www.nowcoder.com/101.png");
        user.setCreateTime(new Date());
        int rows=userMapper.insertUser(user);//返回行数
        System.out.println(rows);
        System.out.println(user.getId());
    }

    @Test
    public void testupdateUser() {
        int row=userMapper.updateStatus(150,1);
        System.out.println(row);
        row=userMapper.updateHeader(150,"http://www.nowcoder.com/102.png");
        System.out.println(row);
        row=userMapper.updatePassword(150,"hello");
        System.out.println(row);
    }

    @Test
    public void testSelectPosts() {
        List<DiscussPost> discussPosts=disscussPostMapper.selectDisscussPosts(149,0,10);
        for (Iterator<DiscussPost> iterator = discussPosts.iterator(); iterator.hasNext(); ) {
            DiscussPost next =  iterator.next();
            System.out.println(next);
        }
        int rows=disscussPostMapper.selectDiscussPostRows(149);
        System.out.println(rows);
    }
}
