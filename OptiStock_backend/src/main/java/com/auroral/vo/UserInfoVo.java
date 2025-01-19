package com.auroral.vo;

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
public class UserInfoVo {
    //用户名（唯一）
    private String username;
    //用户昵称（可选）
    private String nickname;
    //邮箱地址（可选）
    private String email;
    //手机号（可选）
    private String phone;
    //头像URL
    private String avatar;
    //用户角色：0=管理员，1=普通用户
    private Integer status;
}

