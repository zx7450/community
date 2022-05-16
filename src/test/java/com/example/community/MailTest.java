package com.example.community;

import com.example.community.util.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * @author zx
 * @date 2022/5/16 10:58
 */
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTest {
    @Autowired
    private MailClient mailClient;


    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void testTextMail() {
        mailClient.sendMail("13405969241@qq.com","Test","Hello!");
    }

    @Test
    public void testHtmlMail() {
        //thymeleaf中的context
        Context context=new Context();
        context.setVariable("username","sunday");
        String content=templateEngine.process("mail/demo",context);
        System.out.println(content);
        mailClient.sendMail("13405969241@qq.com","HTML",content);
    }
}
