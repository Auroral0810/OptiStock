package com.auroral.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnOrderVo {
    private Long id;              // 退货订单ID
    private Long orderId;          // 关联订单ID
    private String orderNumber;    // 订单编号
    private Long productId;        // 退货商品ID
    private String productName;    // 退货商品名称
    private String returnReason;   // 退货原因
    private String returnStatus;   // 退货状态
    private String rejectReason;   // 拒绝原因
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date returnTime;       // 退货时间
}
