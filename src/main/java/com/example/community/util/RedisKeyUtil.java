package com.example.community.util;

/**
 * @author zx
 * @date 2022/5/31 14:58
 */
public class RedisKeyUtil {
    private static final String SPLIT = ":";
    private static final String PREFIX_ENTITY_LIKE = "like:entity";//实体的赞（前缀）

    //某个实体的赞
    //like:entity:entityType:entityid ->set(userId)，存点赞人的用户id，需要统计数量时用统计数量的方法
    public static String getEntityLikeKey(int entityType, int entityid) {
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityid;
    }
}
