package com.example.community.service;

import com.example.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author zx
 * @date 2022/5/31 15:05
 */
@Service
public class LikeService {

    @Autowired
    private RedisTemplate redisTemplate;

    //点赞
    public void like(int userId, int entityType, int entityid) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityid);
        //判断是否点过赞，只要userId在不在集合里即可
        boolean isMember = redisTemplate.opsForSet().isMember(entityLikeKey, userId);
        if (isMember) {
            redisTemplate.opsForSet().remove(entityLikeKey, userId);
        } else {
            redisTemplate.opsForSet().add(entityLikeKey, userId);
        }
    }

    //查询某实体点赞数量
    public long findEntityLikeCount(int entityType, int entityid) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityid);
        return redisTemplate.opsForSet().size(entityLikeKey);
    }

    //查询某人对某实体的点赞状态(用int型考虑业务拓展：即点踩动能，要有3种状态)
    public int findEntityLikeStatus(int userId, int entityType, int entityid) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityid);
        return redisTemplate.opsForSet().isMember(entityLikeKey, userId) ? 1 : 0;
    }
}
