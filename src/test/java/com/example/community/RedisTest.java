package com.example.community;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.*;
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

        redisTemplate.opsForSet().add(rediskey, "??????", "??????", "??????", "??????", "?????????");

        System.out.println(redisTemplate.opsForSet().size(rediskey));
        System.out.println(redisTemplate.opsForSet().pop(rediskey));
        System.out.println(redisTemplate.opsForSet().members(rediskey));
    }

    @Test
    public void testSortedSets() {
        String rediskey = "test:students";

        redisTemplate.opsForZSet().add(rediskey, "??????", 80);
        redisTemplate.opsForZSet().add(rediskey, "??????", 90);
        redisTemplate.opsForZSet().add(rediskey, "??????", 50);
        redisTemplate.opsForZSet().add(rediskey, "??????", 70);
        redisTemplate.opsForZSet().add(rediskey, "?????????", 60);

        System.out.println(redisTemplate.opsForZSet().zCard(rediskey));//???????????????????????????
        System.out.println(redisTemplate.opsForZSet().score(rediskey, "??????"));//?????????????????????
        System.out.println(redisTemplate.opsForZSet().reverseRank(rediskey, "??????"));//???????????????????????????(????????????)
        System.out.println(redisTemplate.opsForZSet().reverseRange(rediskey, 0, 2));//???????????????3???(????????????)
    }

    @Test
    public void testKeys() {
        redisTemplate.delete("test:user");

        System.out.println(redisTemplate.hasKey("test:user"));//????????????key????????????

        redisTemplate.expire("test:students", 10, TimeUnit.SECONDS);//??????????????????
    }

    // ?????????????????????key
    @Test
    public void testBoundOperations() {
        String rediskey = "test:count";
        BoundValueOperations operations = redisTemplate.boundValueOps(rediskey);//??????keys
        operations.increment();
        operations.increment();
        operations.increment();
        operations.increment();
        operations.increment();
        System.out.println(operations.get());
    }

    //???????????????
    @Test
    public void testTransactional() {
        Object obj = redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String rediskey = "test:tx";

                operations.multi();//????????????

                operations.opsForSet().add(rediskey, "zhangsan");
                operations.opsForSet().add(rediskey, "lisi");
                operations.opsForSet().add(rediskey, "wangwu");

                //redis??????????????????????????????????????????????????????????????????????????????
                System.out.println(operations.opsForSet().members(rediskey));//????????????

                return operations.exec();//????????????
            }
        });
        System.out.println(obj);
    }

    //??????20?????????????????????????????????
    @Test
    public void testHyperLogLog() {
        String rediskey = "test:hll:01";

        for (int i = 1; i <= 100000; i++) {
            redisTemplate.opsForHyperLogLog().add(rediskey, i);
        }

        for (int i = 1; i <= 100000; i++) {
            int r = (int) (Math.random() * 100000 + 1);
            redisTemplate.opsForHyperLogLog().add(rediskey, r);
        }

        long size = redisTemplate.opsForHyperLogLog().size(rediskey);
        System.out.println(size);
    }

    //???3????????????????????????????????????????????????????????????
    @Test
    public void testHyperLogLogUnion() {
        String rediskey2 = "test:hll:02";

        for (int i = 1; i <= 10000; i++) {
            redisTemplate.opsForHyperLogLog().add(rediskey2, i);
        }

        String rediskey3 = "test:hll:03";
        for (int i = 5001; i <= 15000; i++) {
            redisTemplate.opsForHyperLogLog().add(rediskey3, i);
        }

        String rediskey4 = "test:hll:04";
        for (int i = 10001; i <= 20000; i++) {
            redisTemplate.opsForHyperLogLog().add(rediskey4, i);
        }

        String unionKey = "test:hll:union";
        redisTemplate.opsForHyperLogLog().union(unionKey, rediskey2, rediskey3, rediskey4);

        long size = redisTemplate.opsForHyperLogLog().size(unionKey);
        System.out.println(size);
    }

    //??????????????????????????????
    @Test
    public void testBitMap() {
        String rediskey = "test:bm:01";

        //??????
        redisTemplate.opsForValue().setBit(rediskey, 1, true);//?????????????????????????????????????????????????????????false???
        redisTemplate.opsForValue().setBit(rediskey, 4, true);
        redisTemplate.opsForValue().setBit(rediskey, 7, true);

        //??????
        System.out.println(redisTemplate.opsForValue().getBit(rediskey, 0));
        System.out.println(redisTemplate.opsForValue().getBit(rediskey, 1));
        System.out.println(redisTemplate.opsForValue().getBit(rediskey, 2));

        //??????
        Object object = redisTemplate.execute(new RedisCallback() {//????????????
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.bitCount(rediskey.getBytes());
            }
        });

        System.out.println(object);
    }

    //??????????????????????????????????????????3????????????OR??????
    @Test
    public void testBitMapOperation() {
        String rediskey2 = "test:bm:02";
        redisTemplate.opsForValue().setBit(rediskey2, 0, true);
        redisTemplate.opsForValue().setBit(rediskey2, 1, true);
        redisTemplate.opsForValue().setBit(rediskey2, 2, true);
        String rediskey3 = "test:bm:03";
        redisTemplate.opsForValue().setBit(rediskey3, 2, true);
        redisTemplate.opsForValue().setBit(rediskey3, 3, true);
        redisTemplate.opsForValue().setBit(rediskey3, 4, true);
        String rediskey4 = "test:bm:04";
        redisTemplate.opsForValue().setBit(rediskey4, 4, true);
        redisTemplate.opsForValue().setBit(rediskey4, 5, true);
        redisTemplate.opsForValue().setBit(rediskey4, 6, true);

        String rediskey = "test:bm:or";
        Object object = redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.bitOp(RedisStringCommands.BitOperation.OR,
                        rediskey.getBytes(), rediskey2.getBytes(), rediskey3.getBytes(), rediskey4.getBytes());
                return connection.bitCount(rediskey.getBytes());
            }
        });
        System.out.println(object);

        System.out.println(redisTemplate.opsForValue().getBit(rediskey, 0));
        System.out.println(redisTemplate.opsForValue().getBit(rediskey, 1));
        System.out.println(redisTemplate.opsForValue().getBit(rediskey, 2));
        System.out.println(redisTemplate.opsForValue().getBit(rediskey, 3));
        System.out.println(redisTemplate.opsForValue().getBit(rediskey, 4));
        System.out.println(redisTemplate.opsForValue().getBit(rediskey, 5));
        System.out.println(redisTemplate.opsForValue().getBit(rediskey, 6));
    }
}
