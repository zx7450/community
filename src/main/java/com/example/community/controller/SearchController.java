package com.example.community.controller;

import com.example.community.entity.DiscussPost;
import com.example.community.entity.Page;
import com.example.community.entity.SearchResult;
import com.example.community.service.ElasticsearchService;
import com.example.community.service.LikeService;
import com.example.community.service.UserService;
import com.example.community.util.CommunityConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zx
 * @date 2022/6/20 15:34
 */
@Controller
public class SearchController implements CommunityConstant {

    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);

    @Autowired
    private ElasticsearchService elasticsearchService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private UserService userService;

    @GetMapping("/search")
    public String search(String keyword, Page page, Model model) {
        //搜索帖子
        try {
            SearchResult searchResult = elasticsearchService.searchDiscussPost(keyword, (page.getCurrent() - 1) * 10, page.getLimit());
            //聚合数据
            List<Map<String, Object>> discuessposts = new ArrayList<>();
            List<DiscussPost> discussPostList = searchResult.getList();
            if (discussPostList != null) {
                for (DiscussPost post : discussPostList) {
                    Map<String, Object> map = new HashMap<>();
                    //帖子
                    map.put("post", post);
                    //作者
                    map.put("user", userService.findUserById(post.getUserId()));
                    //点赞数量
                    map.put("likeCount", likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId()));
                    discuessposts.add(map);
                }
            }
            model.addAttribute("discuessposts", discuessposts);
            model.addAttribute("keyword", keyword);

            //分页信息
            page.setPath("/search?keyword=" + keyword);
            page.setRows(searchResult.getTotal() == 0 ? 0 : (int) searchResult.getTotal());
        } catch (IOException e) {
            logger.error("系统出错，没有数据" + e.getMessage());
        }
        return "/site/search";
    }
}
