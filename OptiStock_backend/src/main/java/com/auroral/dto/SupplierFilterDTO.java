package com.auroral.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SupplierFilterDTO {
    private String name;         // 供应商名称
    private String phone;        // 联系电话
    private String contact;      // 联系人
}