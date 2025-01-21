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
public class StockAdjustVo {
    //记录id
    private Long id;
    //商品ID
    private Long productId;
    //库存调整类型
    private String adjustmentType;
    //调整数量
    private Integer quantity;
    //调整备注
    private String remarks;
    //调整时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
}

