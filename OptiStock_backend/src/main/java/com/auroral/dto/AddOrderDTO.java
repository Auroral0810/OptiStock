package com.auroral.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Data
public class AddOrderDTO {
    @NotNull(message = "商品ID不能为空")
    private Long productId;

    @NotBlank(message = "商品名称不能为空")
    private String productName;

    @NotNull(message = "商品单价不能为空")
    @Positive(message = "单价必须为正数")
    private Double price;

    @NotNull(message = "商品数量不能为空")
    @Positive(message = "商品数量必须为正数")
    private Integer quantity;

    @NotNull(message = "订单总价不能为空")
    @Positive(message = "订单总价必须为正数")
    private Double totalPrice;

    @NotBlank(message = "客户姓名不能为空")
    private String customerName;

    @NotBlank(message = "平台不能为空")
    private String platform;

    @NotBlank(message = "手机号不能为空")
    private String phone;

    @NotBlank(message = "地址不能为空")
    private String address;

    @NotNull(message = "库存数量不能为空")
    private Integer stockQuantity;

    @NotBlank(message = "订单编号不能为空")
    private String orderId;
}
