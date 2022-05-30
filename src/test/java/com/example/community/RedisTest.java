package com.example.community;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.test.context.ContextConfiguration;

import java.util.concurrent.TimeUnit;

/**
 * @author zx
 * @date 2022/5/31 0:43
 */
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class RedisTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testStrings() {
        String rediskey = "test:count";
        redisTemplate.opsForValue().set(rediskey, 1);
        System.out.println(redisTemplate.opsForValue().get(rediskey));
        System.out.println(redisTemplate.opsForValue().increment(rediskey));
        System.out.println(redisTemplate.opsForValue().decrement(rediskey));
    }

    @Test
    public void testHashes() {
        String rediskey = "test:user";

        redisTemplate.opsForHash().put(rediskey, "id", 1);
        redisTemplate.opsForHash().put(rediskey, "username", "zhangsan");

        System.out.println(redisTemplate.opsForHash().get(rediskey, "id"));
        System.out.println(redisTemplate.opsForHash().get(rediskey, "username"));
    }

    @Test
    public void testLists() {
        String rediskey = "test:ids";

        redisTemplate.opsForList().leftPush(rediskey, 101);
        redisTemplate.opsForList().leftPush(rediskey, 102);
        redisTemplate.opsForList().leftPush(rediskey, 103);

        System.out.println(redisTemplate.opsForList().size(rediskey));
        System.out.println(redisTemplate.opsForList().index(rediskey, 0));
        System.out.println(redisTemplate.opsForList().range(rediskey, 0, 2));

        System.out.println(redisTemplate.opsForList().leftPop(rediskey));
        System.out.println(redisTemplate.opsForList().leftPop(rediskey));
        System.out.println(redisTemplate.opsForList().leftPop(rediskey));
    }

    @Test
    public void testSets() {
        String rediskey = "test:teachers";

        redisTemplate.opsForSet().add(rediskey, "刘备", "关羽", "张飞", "赵云", "诸葛亮");

        System.out.println(redisTemplate.opsForSet().size(rediskey));
        System.out.println(redisTemplate.opsForSet().pop(rediskey));
        System.out.println(redisTemplate.opsForSet().members(rediskey));
    }

    @Test
    public void testSortedSets() {
        String rediskey = "test:students";

        redisTemplate.opsForZSet().add(rediskey, "唐僧", 80);
        redisTemplate.opsForZSet().add(rediskey, "悟空", 90);
        redisTemplate.opsForZSet().add(rediskey, "八戒", 50);
        redisTemplate.opsForZSet().add(rediskey, "沙僧", 70);
        redisTemplate.opsForZSet().add(rediskey, "白龙马", 60);

        System.out.println(redisTemplate.opsForZSet().zCard(rediskey));//统计一共多少条数据
        System.out.println(redisTemplate.opsForZSet().score(rediskey, "八戒"));//统计某个人分数
        System.out.println(redisTemplate.opsForZSet().reverseRank(rediskey, "八戒"));//倒序统计某个人排名(由大到小)
        System.out.println(redisTemplate.opsForZSet().reverseRange(rediskey, 0, 2));//倒序统计前3名(由大到小)
    }

    @Test
    public void testKeys() {
        redisTemplate.delete("test:user");

        System.out.println(redisTemplate.hasKey("test:user"));//判断每个key是否存在

        redisTemplate.expire("test:students", 10, TimeUnit.SECONDS);//设置过期时间
    }

    // 多次访问同一个key
    @Test
    public void testBoundOperations() {
        String rediskey = "test:count";
        BoundValueOperations operations = redisTemplate.boundValueOps(rediskey);//绑定keys
        operations.increment();
        operations.increment();
        operations.increment();
        operations.increment();
        operations.increment();
        System.out.println(operations.get());
    }

    //编程式事务
    @Test
    public void testTransactional() {
        Object obj = redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String rediskey = "test:tx";

                operations.multi();//启用事务

                operations.opsForSet().add(rediskey, "zhangsan");
                operations.opsForSet().add(rediskey, "lisi");
                operations.opsForSet().add(rediskey, "wangwu");

                //redis开启事务和提交事务之间命令会被放入队列，不会立刻执行
                System.out.println(operations.opsForSet().members(rediskey));//输出为空

                return operations.exec();//提交事务
            }
        });
        System.out.println(obj);
    }
}
