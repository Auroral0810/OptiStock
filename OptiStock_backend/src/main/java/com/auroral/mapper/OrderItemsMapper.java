package com.auroral.mapper;

import com.auroral.entity.OrderItems;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;


/**
 * 订单商品详情表(OrderItems)表数据库访问层
 *
 * @author makejava
 * @since 2025-01-22 14:54:57
 */
public interface OrderItemsMapper extends BaseMapper<OrderItems> {
    @Select("SELECT * FROM order_items WHERE order_id = #{orderId} LIMIT 1")
    OrderItems selectByOrderId(Long orderId);
}

