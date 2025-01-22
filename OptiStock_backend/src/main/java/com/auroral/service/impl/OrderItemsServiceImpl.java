package com.auroral.service.impl;

import com.auroral.entity.OrderItems;
import com.auroral.mapper.OrderItemsMapper;
import com.auroral.service.OrderItemsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 订单商品详情表(OrderItems)表服务实现类
 *
 * @author makejava
 * @since 2025-01-22 14:54:57
 */
@Service("orderItemsService")
public class OrderItemsServiceImpl extends ServiceImpl<OrderItemsMapper, OrderItems> implements OrderItemsService {

}

