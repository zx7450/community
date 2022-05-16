package com.example.community.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author zx
 * @date 2022/5/11 23:31
 */
@Data
public class User {

    private int id;
    private String username;
    private String password;
    private String salt;
    private String email;
    private int type;
    private int status;
    private String activationCode;
    private String headerUrl;
    private Date createTime;

}
