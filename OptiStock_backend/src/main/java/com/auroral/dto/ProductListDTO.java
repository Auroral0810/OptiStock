package com.auroral.dto;

import lombok.Data;

@Data
public class ProductListDTO {
    private Integer pageNum;  // 当前页码
    private Integer pageSize;     // 每页数量
    private ProductFilterDTO filterForm; // 查询条件
}