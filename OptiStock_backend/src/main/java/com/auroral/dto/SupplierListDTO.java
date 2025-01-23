package com.auroral.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SupplierListDTO {
    private Integer pageNum;  // 当前页码
    private Integer pageSize;     // 每页数量
    private SupplierFilterDTO filterForm; // 查询条件
}