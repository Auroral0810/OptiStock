package com.auroral.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 商品信息表(Product)表实体类
 *
 * @author makejava
 * @since 2025-01-20 12:01:06
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("product")
public class Product {
    @TableId(type = IdType.AUTO)  // MyBatis-Plus 识别 id 为自增
    private Long id;

    //商品名称
    private String name;
    //商品分类ID，关联product_category表
    private Long categoryId;
    //库存单位编号（SKU）
    private String sku;
    //商品售价
    private Double price;
    //商品成本价
    private Double costPrice;
    //库存数量
    private Integer stockQuantity;
    //计量单位，如件、盒、瓶等
    private String unit;
    //商品状态
    private String status;
    //商品创建时间
    private Date createdAt;
    //商品信息更新时间
    private Date updatedAt;
    //商品阈值
    private Double warningThreshold;

}

