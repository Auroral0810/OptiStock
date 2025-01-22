package com.auroral.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class HandleReturnOrderDTO {
    @NotNull(message = "退货订单ID不能为空")
    private Long id;

    @NotBlank(message = "退货状态不能为空")
    private String returnStatus;

    private String reason; // 拒绝原因，可能为空
}
