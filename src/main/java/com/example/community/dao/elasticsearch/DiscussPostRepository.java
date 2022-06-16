package com.example.community.dao.elasticsearch;

import com.example.community.entity.DiscussPost;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author zx
 * @date 2022/6/16 15:42
 */
@Repository
//处理的实体类、主键类型
public interface DiscussPostRepository extends ElasticsearchRepository<DiscussPost, Integer> {
}
