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
public class StockThresholdVo {
    private Long id;
    //商品名称
    private String name;
    //库存单位编号（SKU）
    private String sku;
    //库存数量
    private Integer stockQuantity;
    //库存预警阈值
    private Double warningThreshold;
    //创建时间
}

