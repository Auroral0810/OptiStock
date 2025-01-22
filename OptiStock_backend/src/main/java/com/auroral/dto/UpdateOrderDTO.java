package com.auroral.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrderDTO {
    // 订单编号
    private String orderNumber;

    // 客户姓名
    private String customerName;

    // 联系电话
    private String phone;

    // 收货地址
    private String address;
}
