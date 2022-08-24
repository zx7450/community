package com.example.community.dao;

import com.example.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zx
 * @date 2022/5/12 19:32
 */
@Mapper
public interface DisscussPostMapper {
    List<DiscussPost> selectDisscussPosts(int userId, int offset, int limit, int orderMode);//动态实现，有时需要userId有时不需要

    int selectDiscussPostRows(@Param("userId") int userId);//Param注解用于给参数取别名，如果方法仅有一个参数，并在<if>里使用则必须加此参数

    int insertDiscussPost(DiscussPost discussPost);

    DiscussPost selectDiscussPostById(int id);

    int updateCommentCount(int id, int commentCount);

    int updateType(int id, int type);

    int updateStatus(int id, int status);

    int updateScore(int id, double score);
}
