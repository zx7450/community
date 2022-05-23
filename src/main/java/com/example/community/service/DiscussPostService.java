package com.example.community.service;

import com.example.community.dao.DisscussPostMapper;
import com.example.community.entity.DiscussPost;
import com.example.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @author zx
 * @date 2022/5/12 20:14
 */
@Service
public class DiscussPostService {
    @Autowired
    private DisscussPostMapper disscussPostMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    public List<DiscussPost> findDiscussPosts(int userId, int offset, int limit) {
        return disscussPostMapper.selectDisscussPosts(userId, offset, limit);
    }

    public int findDiscussPostRows(int userId) {
        return disscussPostMapper.selectDiscussPostRows(userId);
    }

    public int addDiscussPost(DiscussPost discussPost) {
        if (discussPost == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }

        //转义html标记，以防影响页面
        discussPost.setTitle(HtmlUtils.htmlEscape(discussPost.getTitle()));
        discussPost.setContent(HtmlUtils.htmlEscape(discussPost.getContent()));
        //过滤敏感词
        discussPost.setTitle(sensitiveFilter.filter(discussPost.getTitle()));
        discussPost.setContent(sensitiveFilter.filter(discussPost.getContent()));

        return disscussPostMapper.insertDiscussPost(discussPost);
    }

    public DiscussPost findDiscussPostById(int id) {
        return disscussPostMapper.selectDiscussPostById(id);
    }
}
