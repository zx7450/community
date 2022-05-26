package com.example.community;

import com.example.community.dao.DisscussPostMapper;
import com.example.community.dao.LoginTicketMapper;
import com.example.community.dao.MessageMapper;
import com.example.community.dao.UserMapper;
import com.example.community.entity.DiscussPost;
import com.example.community.entity.LoginTicket;
import com.example.community.entity.Message;
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

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Test
    public void testselectUser() {
        User user = userMapper.selectById(101);
        System.out.println(user);
        user = userMapper.selectByName("liubei");
        System.out.println(user);
        user = userMapper.selectByEmail("nowcoder102@sina.com");
        System.out.println(user);
    }

    @Test
    public void testInsertUser() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("123456");
        user.setSalt("abc");
        user.setEmail("test@qq.com");
        user.setHeaderUrl("http://www.nowcoder.com/101.png");
        user.setCreateTime(new Date());
        int rows = userMapper.insertUser(user);//返回行数
        System.out.println(rows);
        System.out.println(user.getId());
    }

    @Test
    public void testupdateUser() {
        int row = userMapper.updateStatus(150, 1);
        System.out.println(row);
        row = userMapper.updateHeader(150, "http://www.nowcoder.com/102.png");
        System.out.println(row);
        row = userMapper.updatePassword(150, "hello");
        System.out.println(row);
    }

    @Test
    public void testSelectPosts() {
        List<DiscussPost> discussPosts = disscussPostMapper.selectDisscussPosts(149, 0, 10);
        for (Iterator<DiscussPost> iterator = discussPosts.iterator(); iterator.hasNext(); ) {
            DiscussPost next = iterator.next();
            System.out.println(next);
        }
        int rows = disscussPostMapper.selectDiscussPostRows(149);
        System.out.println(rows);
    }

    @Test
    public void testInsertLoginTicket() {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(101);
        loginTicket.setTicket("abc");
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 10));

        loginTicketMapper.insertLoginTicket(loginTicket);
    }

    @Test
    public void testSelectLoginTicket() {
        LoginTicket loginTicket = loginTicketMapper.selectByTicket("abc");
        System.out.println(loginTicket);
        loginTicketMapper.updateStatus("abc", 1);
        loginTicket = loginTicketMapper.selectByTicket("abc");
        System.out.println(loginTicket);
    }

    @Test
    public void testSelectLetters() {
        List<Message> messageList = messageMapper.selectConversation(111, 0, 20);
        for (Message message : messageList) {
            System.out.println(message);
        }
        int count = messageMapper.selectConversationCount(111);
        System.out.println(count);
        messageList = messageMapper.selectLetters("111_112", 0, 10);
        for (Message message : messageList) {
            System.out.println(message);
        }
        count = messageMapper.selectLettersCount("111_112");
        System.out.println(count);
        count = messageMapper.selectLetterUnreadCount(131, "111_131");
        System.out.println(count);
    }
}
