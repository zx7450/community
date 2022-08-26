package com.example.community;

import com.example.community.entity.DiscussPost;
import com.example.community.service.DiscussPostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;

/**
 * @author zx
 * @date 2022/8/26 17:22
 */
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class CaffeineTests {
    @Autowired
    private DiscussPostService discussPostService;

    @Test
    public void initDataForTest() {//添加多条数据，以测试差异
        for (int i = 0; i < 300000; i++) {
            DiscussPost discussPost = new DiscussPost();
            discussPost.setUserId(111);
            discussPost.setTitle("互联网求职暖春计划");
            discussPost.setContent("今年的就业形势，确实不容乐观。过了个年，仿佛跳水一般，整个讨论区哀鸿遍野！19届真的没人要了吗？！18届被优化真的没有出路了吗？！大家的“哀嚎”与“悲惨遭遇”牵动了每日潜伏于讨论区的牛客小哥哥小姐姐们的心，于是牛客决定：是时候为大家做点什么了！为了帮助大家度过“寒冬”，牛客网特别联合60+家企业，开启互联网求职暖春计划，面向18届&19届，拯救0 offer！");
            discussPost.setCreateTime(new Date());
            discussPost.setScore(Math.random() * 2000);
            discussPostService.addDiscussPost(discussPost);
        }
    }

    @Test
    public void testCache() {
        System.out.println(discussPostService.findDiscussPosts(0, 0, 10, 1));//第一次访问，缓存里没有，从数据库读取并写入缓存
        System.out.println(discussPostService.findDiscussPosts(0, 0, 10, 1));//第二次访问，从缓存读取
        System.out.println(discussPostService.findDiscussPosts(0, 0, 10, 1));//同上
        System.out.println(discussPostService.findDiscussPosts(0, 0, 10, 0));//帖子按时间排序，不走缓存
    }
}
