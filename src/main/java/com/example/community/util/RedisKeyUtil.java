package com.example.community.util;

/**
 * @author zx
 * @date 2022/5/31 14:58
 */
public class RedisKeyUtil {
    private static final String SPLIT = ":";
    private static final String PREFIX_ENTITY_LIKE = "like:entity";//实体的赞（前缀）
    private static final String PREFIX_USER_LIKE = "like:user";

    //某个实体的赞
    //like:entity:entityType:entityid ->set(userId)，存点赞人的用户id，需要统计数量时用统计数量的方法
    public static String getEntityLikeKey(int entityType, int entityid) {
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityid;
    }

    //某个用户的赞
    //like:user:userId -> int
    public static String getUserLikeKey(int userId) {
        return PREFIX_USER_LIKE + SPLIT + userId;
    }
}
