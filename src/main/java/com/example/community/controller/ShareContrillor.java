package com.example.community.controller;

import com.example.community.entity.Event;
import com.example.community.event.EventConsumer;
import com.example.community.event.EventProducer;
import com.example.community.util.CommunityConstant;
import com.example.community.util.CommunityUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zx
 * @date 2022/8/23 18:39
 */
@Controller
public class ShareContrillor implements CommunityConstant {
    private static final Logger logger = LoggerFactory.getLogger(ShareContrillor.class);

    @Autowired
    private EventProducer eventProducer;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Value("${wk.image.storage}")
    private String wkImageStorage;

    //生成长图
    @GetMapping("/share")
    @ResponseBody
    public String share(String htmlUrl) {
        //生成随机文件名
        String fileName = CommunityUtil.generateUUID();

        //异步生成长图，使用异步方法当访问人数多时可不用等待内容处理完继续执行其他任务，效率更高
        Event event = new Event()
                .setTopic(TOPIC_SHARE)
                .setData("htmlUrl", htmlUrl)
                .setData("fileName", fileName)
                .setData("suffix", ".png");

        eventProducer.fireEvent(event);

        //返回访问路径
        Map<String, Object> map = new HashMap<>();
        map.put("shareUrl", domain + contextPath + "/share/image/" + fileName);
        return CommunityUtil.getJSONString(0, null, map);
    }

    //获取长图
    @GetMapping("/share/image/{fileName}")
    public void getShareImage(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        if (StringUtils.isBlank(fileName)) {
            throw new IllegalArgumentException("文件名不能为空");
        }
        //声明输出的是什么，表示输出png格式的图片
        response.setContentType("image/png");
        File file = new File(wkImageStorage + "/" + fileName + ".png");
        try {
            OutputStream os = response.getOutputStream();
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1) {//不等于-1表示读到数据
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.error("获取长图失败:" + e.getMessage());
        }
    }
}
