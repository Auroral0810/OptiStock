package com.auroral.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
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
public class StockVo {
    private Long id;
    //商品名称
    private String name;
    //库存单位编号（SKU）
    private String sku;
    //商品售价
    private Double price;
    //库存数量
    private Integer stockQuantity;
    //商品总值
    private Double totalValue;
    //更新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedAt;
}

