package com.auroral.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 库存调整请求 DTO
 */
@Data
@NoArgsConstructor
public class AdjustStockDTO {

    @NotNull(message = "商品ID不能为空")
    private Long productId;

    @NotBlank(message = "商品名称不能为空")
    private String name;

    @NotBlank(message = "SKU不能为空")
    private String sku;

    @NotNull(message = "当前库存不能为空")
    @Min(value = 0, message = "当前库存不能为负数")
    private Integer currentStock;

    @NotNull(message = "调整数量不能为空")
    @Min(value = 1, message = "调整数量必须大于0")
    private Integer quantity;

    @NotBlank(message = "调整类型不能为空")
    private String adjustmentType;
    @NotBlank(message = "调整原因不能为空")
    private String remarks;
}


