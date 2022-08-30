package com.example.community;

import com.example.community.entity.DiscussPost;
import com.example.community.service.DiscussPostService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.event.annotation.AfterTestClass;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.util.Assert;

import java.util.Date;

/**
 * @author zx
 * @date 2022/8/26 17:59
 */
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class SpringBootTests {

    @Autowired
    private DiscussPostService discussPostService;

    private DiscussPost data;

    @BeforeAll
    public static void beforeClass() {
        System.out.println("beforeClass");
    }

    @AfterAll
    public static void afterClass() {
        System.out.println("afterClass");
    }

    @BeforeEach
    public void before() {
        System.out.println("before");

        //初始化测试数据
        data=new DiscussPost();
        data.setUserId(111);
        data.setTitle("Test Title");
        data.setContent("Test COntent");
        data.setCreateTime(new Date());
        discussPostService.addDiscussPost(data);
    }

    @AfterEach
    public void after() {
        System.out.println("after");

        //删除数据
        discussPostService.updateStatus(data.getId(),2);

    }

    @Test
    public void test1() {
        System.out.println("test1");
    }

    @Test
    public void test2() {
        System.out.println("test2");
    }

    @Test
    public void testFindById() {
        DiscussPost post=discussPostService.findDiscussPostById(data.getId());
        Assertions.assertNotNull(post);
        Assertions.assertEquals(data.getTitle(),post.getTitle());
        Assertions.assertEquals(data.getContent(),post.getContent());
    }

    @Test
    public void testUpdateScore() {
        int rows=discussPostService.updateScore(data.getId(), 2000.00);
        Assertions.assertEquals(1,rows);

        DiscussPost discussPost=discussPostService.findDiscussPostById(data.getId());
        Assertions.assertEquals(2000.00,discussPost.getScore(),2);//判断到2位小数是否相等
    }
}
