package com.auroral.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 用户表(Users)表实体类
 *
 * @author makejava
 * @since 2025-01-19 13:58:19
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("Users")
public class User {
    //用户唯一标识@TableId
    @TableId(type = IdType.AUTO)  // MyBatis-Plus 识别 id 为自增
    private Long id;

    //用户名（唯一）
    private String username;
    //用户昵称（可选）
    private String nickname;
    //MD5加密后的密码
    private String passwordHash;
    //邮箱地址（可选）
    private String email;
    //手机号（可选）
    private String phone;
    //头像URL
    private String avatar;
    //用户角色：0=管理员，1=普通用户
    private Integer role;
    //账户状态：0=未激活，1=正常，2=封禁
    private Integer status;
    //账户创建时间
    private Date createdAt;
    //更新时间
    private Date updatedAt;
    //最近一次登录时间
    private Date lastLogin;


}

