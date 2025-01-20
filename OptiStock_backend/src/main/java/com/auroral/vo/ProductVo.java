package com.auroral.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 商品信息VO对象
 * @author makejava
 * @since 2025-01-20 12:01:06
 */
@Data
public class ProductVo {
    private Long id;
    //商品名称
    private String name;
    //商品分类ID，关联product_category表
    private Long categoryId;
    //商品分类名称
    private String categoryName;
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
}

