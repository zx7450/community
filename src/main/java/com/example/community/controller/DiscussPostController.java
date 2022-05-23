package com.example.community.controller;

import com.example.community.entity.DiscussPost;
import com.example.community.entity.User;
import com.example.community.service.DiscussPostService;
import com.example.community.service.UserService;
import com.example.community.util.CommunityUtil;
import com.example.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author zx
 * @date 2022/5/22 21:29
 */
@Controller
@RequestMapping("/discuss")
public class DiscussPostController {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @PostMapping("/add")
    @ResponseBody
    public String addDiscussPost(String title, String content) {
        User user = hostHolder.getUser();
        if (user == null)
            return CommunityUtil.getJSONString(403, "你还没有登录！");
        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(user.getId());
        discussPost.setTitle(title);
        discussPost.setContent(content);
        discussPost.setCreateTime(new Date());
        discussPostService.addDiscussPost(discussPost);

        //报错的情况，将来统一处理
        return CommunityUtil.getJSONString(0, "发布成功!");
    }

    @GetMapping("/detail/{discussPostId}")
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model) {
        //帖子
        DiscussPost discussPost = discussPostService.findDiscussPostById(discussPostId);
        model.addAttribute("post", discussPost);
        //查询帖子的作者以获取作者的详细信息用于显示
        User user = userService.findUserById(discussPost.getUserId());
        model.addAttribute("user", user);

        return "/site/discuss-detail";
    }
}
