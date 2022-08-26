package com.example.community.service;

import com.example.community.dao.DisscussPostMapper;
import com.example.community.entity.DiscussPost;
import com.example.community.util.SensitiveFilter;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author zx
 * @date 2022/5/12 20:14
 */
@Service
public class DiscussPostService {

    private static final Logger logger = LoggerFactory.getLogger(DiscussPostService.class);

    @Autowired
    private DisscussPostMapper disscussPostMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Value("${caffeine.posts.max-size}")
    private int maxsize;

    @Value("${caffeine.posts.expire-seconds}")
    private int expireSesonds;

    // Caffeine核心接口：Cache,LoadingCache,AsyncLoadingCache

    //帖子列表的缓存
    private LoadingCache<String, List<DiscussPost>> postListCache;

    //帖子总数缓存
    private LoadingCache<Integer, Integer> postRowCache;

    @PostConstruct
    public void init() {
        //初始化帖子列表缓存
        postListCache = Caffeine.newBuilder()
                .maximumSize(maxsize)
                .expireAfterWrite(expireSesonds, TimeUnit.SECONDS)
                .build(new CacheLoader<String, List<DiscussPost>>() {
                    @Override
                    public @Nullable List<DiscussPost> load(String key) throws Exception {//实现缓存里没有时怎么从数据库里查数据
                        if (key == null || key.length() == 0) {
                            throw new IllegalArgumentException("参数错误!");
                        }

                        String[] params = key.split(":");
                        if (params == null || params.length != 2) {
                            throw new IllegalArgumentException("参数错误!");
                        }
                        int offset = Integer.valueOf(params[0]);
                        int limit = Integer.valueOf(params[1]);
                        //可改进为二级缓存：Redis->mysql
                        logger.debug("load post list from DB.");
                        return disscussPostMapper.selectDisscussPosts(0, offset, limit, 1);
                    }
                });
        //初始化帖子总数缓存
        postRowCache = Caffeine.newBuilder()
                .maximumSize(maxsize)
                .expireAfterWrite(expireSesonds, TimeUnit.SECONDS)
                .build(new CacheLoader<Integer, Integer>() {
                    @Override
                    public @Nullable Integer load(Integer key) throws Exception {
                        logger.debug("load post rows from DB.");
                        return disscussPostMapper.selectDiscussPostRows(key);
                    }
                });
    }

    public List<DiscussPost> findDiscussPosts(int userId, int offset, int limit, int orderMode) {
        if (userId == 0 && orderMode == 1) {//只缓存热门帖子（orderMode=1）和首页（不传userId即等于0的情况）
            return postListCache.get(offset + ":" + limit);
        }
        //不走缓存，查数据库时记录日志并返回结果
        logger.debug("load post list from DB.");
        return disscussPostMapper.selectDisscussPosts(userId, offset, limit, orderMode);
    }

    public int findDiscussPostRows(int userId) {
        if (userId == 0) { //当用户查自己的帖子(即userId不等于0)时不走缓存
            return postRowCache.get(userId);//userId永远为0，仅作key用
        }
        logger.debug("load post rows from DB.");
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

    public int updateCommentCount(int id, int commentCount) {
        return disscussPostMapper.updateCommentCount(id, commentCount);
    }

    public int updateType(int id, int type) {
        return disscussPostMapper.updateType(id, type);
    }

    public int updateStatus(int id, int status) {
        return disscussPostMapper.updateStatus(id, status);
    }

    public int updateScore(int id, double score) {
        return disscussPostMapper.updateScore(id, score);
    }
}
