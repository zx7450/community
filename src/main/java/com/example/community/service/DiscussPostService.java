package com.example.community.service;

import com.example.community.dao.DisscussPostMapper;
import com.example.community.entity.DiscussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zx
 * @date 2022/5/12 20:14
 */
@Service
public class DiscussPostService {
    @Autowired
    private DisscussPostMapper disscussPostMapper;

    public List<DiscussPost> findDiscussPosts(int userId, int offset, int limit) {
        return disscussPostMapper.selectDisscussPosts(userId, offset, limit);
    }

    public int findDiscussPostRows(int userId) {
        return disscussPostMapper.selectDiscussPostRows(userId);
    }
}
