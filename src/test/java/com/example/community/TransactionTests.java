package com.example.community;

import com.example.community.service.AlphaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author zx
 * @date 2022/5/23 23:28
 */
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class TransactionTests {

    @Autowired
    private AlphaService alphaService;

    @Test
    public void testSave1() {
        Object object=alphaService.save1();
        System.out.println(object);
    }

    @Test
    public void testSave2() {
        Object object=alphaService.save2();
        System.out.println(object);
    }
}
