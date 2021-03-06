package com.example.community.controller;

import com.example.community.annotation.LoginRequired;
import com.example.community.entity.Comment;
import com.example.community.entity.DiscussPost;
import com.example.community.entity.Page;
import com.example.community.entity.User;
import com.example.community.service.*;
import com.example.community.util.CommunityConstant;
import com.example.community.util.CommunityUtil;
import com.example.community.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zx
 * @date 2022/5/19 14:52
 */
@Controller
@RequestMapping("/user")
public class UserController implements CommunityConstant {
    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private FollowService followService;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private CommentService commentService;


    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${community.path.domain}")
    private String domain;

    @Autowired
    private HostHolder hostHolder;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @LoginRequired//??????????????????
    @GetMapping("/setting")
    public String getSettingPage() {
        return "/site/setting";
    }

    @LoginRequired
    @PostMapping("/upload")
    public String uploadHeader(MultipartFile headerImage, Model model) {
        if (headerImage == null) {
            model.addAttribute("error", "????????????????????????!");
            return "/site/setting";
        }

        String filename = headerImage.getOriginalFilename();
        String suffix = filename.substring(filename.indexOf("."));//???????????????
        if (StringUtils.isBlank(suffix)) {
            model.addAttribute("error", "????????????????????????!");
            return "/site/setting";
        }

        //?????????????????????
        filename = CommunityUtil.generateUUID() + suffix;
        //???????????????????????????
        File dest = new File(uploadPath + "/" + filename);
        try {
            headerImage.transferTo(dest);//?????????????????????????????????
        } catch (IOException e) {
            logger.error("??????????????????" + e.getMessage());
            throw new RuntimeException("??????????????????????????????????????????!", e);
        }

        //????????????????????????????????????(web????????????)
        //http://localhost:8080/community/user/header/xxx.png
        User user = hostHolder.getUser();
        String headerUrl = domain + contextPath + "/user/header/" + filename;
        userService.updateHeader(user.getId(), headerUrl);

        return "redirect:/index";
    }

    @GetMapping("/header/{fileName}")
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response) {//????????????????????????????????????????????????
        //?????????????????????
        fileName = uploadPath + "/" + fileName;
        //????????????
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        response.setContentType("image/" + suffix);//???????????????????????????
        try (
                FileInputStream fileInputStream = new FileInputStream(fileName);
                OutputStream os = response.getOutputStream();
        ) {
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fileInputStream.read(buffer)) != -1) {//??????-1????????????????????????
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.error("??????????????????" + e.getMessage());
        }
    }

    @PostMapping("/updatepassowrd")
    public String updatePassword(String oldPassword, String newPassword, Model model) {
        User user = hostHolder.getUser();
        Map<String, Object> map = userService.updatePassword(user.getId(), oldPassword, newPassword);
        if (map.containsKey("success")) {
            return "redirect:/index";
        } else {
            model.addAttribute("oldpasswordMsg", map.get("oldpasswordMsg"));
            model.addAttribute("newpasswordMsg", map.get("newpasswordMsg"));
            return "/site/setting";
        }
    }

    //????????????????????????????????????????????????????????????????????????id
    @GetMapping("/profile/{userId}")
    public String getProfilePage(@PathVariable("userId") int userId, Model model) {
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("??????????????????!");
        }

        //??????
        model.addAttribute("user", user);
        //????????????
        int likeCount = likeService.findUserLikeCount(userId);
        model.addAttribute("likeCount", likeCount);

        //????????????
        long followeeCount = followService.findFolloweeCount(userId, ENTITY_TYPE_USER);
        model.addAttribute("followeeCount", followeeCount);
        //????????????
        long followerCount = followService.findFollowerCount(ENTITY_TYPE_USER, userId);
        model.addAttribute("followerCount", followerCount);
        //???????????????
        boolean hasFollowed = false;
        if (hostHolder.getUser() != null) {
            hasFollowed = followService.hasFollowed(hostHolder.getUser().getId(), ENTITY_TYPE_USER, userId);
        }
        model.addAttribute("hasFollowed", hasFollowed);

        return "/site/profile";
    }

    //????????????
    @GetMapping("/mypost/{userId}")
    public String getMyPost(@PathVariable("userId") int userId, Page page,Model model) {
        User user= userService.findUserById(userId);
        if (user==null) {
            throw new RuntimeException("??????????????????!");
        }
        //??????
        model.addAttribute("user",user);

        page.setPath("/user/mypost/"+userId);
        page.setRows(discussPostService.findDiscussPostRows(userId));

        //????????????
        List<Map<String,Object>> mypostVO=new ArrayList<>();
        List<DiscussPost> mypost=discussPostService.findDiscussPosts(userId,page.getOffset(),page.getLimit());
        if (mypost!=null) {
            for (DiscussPost post : mypost) {
                Map<String,Object> map=new HashMap<>();
                map.put("post",post);
                map.put("likeCount",likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId()));
                mypostVO.add(map);
            }
        }
        model.addAttribute("posts",mypostVO);
        return "/site/my-post";
    }

    //???????????????????????????????????????????????????
    @GetMapping("/myreply/{userId}")
    public String getMyReply(@PathVariable("userId") int userId,Page page,Model model) {
        User user= userService.findUserById(userId);
        if (user==null) {
            throw new RuntimeException("??????????????????!");
        }
        //??????
        model.addAttribute("user",user);

        page.setPath("/user/myreply/"+userId);
        page.setRows(commentService.findCommentsCountByUser(userId));

        //????????????
        List<Comment> myReplys=commentService.findCommentsByUser(userId,page.getOffset(),page.getLimit());
        List<Map<String,Object>> myReplysVO=new ArrayList<>();
        if (myReplys!=null) {
            for (Comment reply : myReplys) {
                Map<String,Object> map=new HashMap<>();
                map.put("reply",reply);
                DiscussPost discussPost=discussPostService.findDiscussPostById(reply.getEntityId());
                map.put("discusspost",discussPost);
                myReplysVO.add(map);
            }
        }
        model.addAttribute("replys",myReplysVO);
        return "/site/my-reply";
    }
}
