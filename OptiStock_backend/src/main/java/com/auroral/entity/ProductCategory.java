package com.auroral.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 商品分类表，存储商品的层级分类(ProductCategory)表实体类
 *
 * @author makejava
 * @since 2025-01-20 12:00:37
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("product_category")
public class ProductCategory {
    //分类唯一标识符@TableId
    @TableId(type = IdType.AUTO)  // MyBatis-Plus 识别 id 为自增
    private Long id;

    //分类名称
    private String name;
    //父分类ID，顶级分类为NULL
    private Long parentId;
    //分类创建时间
    private Date createdAt;


}

