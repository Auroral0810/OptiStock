package com.auroral.service;

import com.auroral.dto.HandleReturnOrderDTO;
import com.auroral.dto.ReturnOrderListDTO;
import com.auroral.entity.ResponseResult;
import com.auroral.entity.ReturnOrders;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.validation.Valid;


/**
 * 退货订单表(ReturnOrders)表服务接口
 *
 * @author makejava
 * @since 2025-01-22 14:55:08
 */
public interface ReturnOrdersService extends IService<ReturnOrders> {

    ResponseResult getReturnOrderList(ReturnOrderListDTO returnOrderListDTO);

    ResponseResult handleReturnOrder(@Valid HandleReturnOrderDTO handleReturnOrderDTO);

    ResponseResult deleteReturnOrder(String id);
}

