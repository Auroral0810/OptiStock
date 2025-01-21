package com.auroral.dto;

import lombok.Data;

@Data
public class StockListDTO {
    private Integer pageNum;  // 当前页码
    private Integer pageSize;     // 每页数量
    private StockFilterDTO filterForm; // 查询条件
}