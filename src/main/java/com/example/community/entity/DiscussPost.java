package com.example.community.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author zx
 * @date 2022/5/12 19:25
 */
@Data
public class DiscussPost {
    private int id;
    private int userId;
    private String title;
    private String content;
    private int type;
    private int status;
    private Date createTime;
    private int commentCount;
    private double score;
}
