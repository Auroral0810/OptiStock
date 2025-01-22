package com.auroral.service;

import com.auroral.dto.AddOrderDTO;
import com.auroral.dto.OrderListDTO;
import com.auroral.dto.UpdateOrderDTO;
import com.auroral.entity.Order;
import com.auroral.entity.ResponseResult;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * 订单信息表(Order)表服务接口
 *
 * @author makejava
 * @since 2025-01-22 14:54:35
 */
public interface OrderService extends IService<Order> {

    ResponseResult getOrderList(OrderListDTO orderListDTO);

    ResponseResult changeOrderStatus(String orderNumber);

    ResponseResult getProductName();

    ResponseResult addOrder(AddOrderDTO addOrderDTO);

    ResponseResult deleteOrder(String orderNumber);

    ResponseResult updateOrder(UpdateOrderDTO updateOrderDTO);
}

