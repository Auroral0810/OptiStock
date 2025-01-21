package com.auroral.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 供应商信息表(Supplier)表实体类
 *
 * @author makejava
 * @since 2025-01-21 12:07:20
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("supplier")
public class Supplier  {
//供应商唯一标识符@TableId
@TableId(type = IdType.AUTO)  // MyBatis-Plus 识别 id 为自增
    private Long id;

//供应商名称
    private String name;
//联系人姓名
    private String contact;
//联系电话
    private String phone;
//电子邮件
    private String email;
//供应商地址
    private String address;
//创建时间
    private Date createdAt;



}

