package com.auroral.mapper;

import com.auroral.entity.Order;
import com.auroral.vo.OrderListVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;


/**
 * 订单信息表(Order)表数据库访问层
 *
 * @author makejava
 * @since 2025-01-22 14:54:35
 */
public interface OrderMapper extends BaseMapper<Order> {
    IPage<OrderListVo> getOrderList(
            Page<OrderListVo> page,
            @Param("keyword") String keyword,
            @Param("status") String status,
            @Param("platform") String platform,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate
    );

}

