package com.auroral.mapper;

import com.auroral.entity.ReturnOrders;
import com.auroral.vo.ReturnOrderVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


/**
 * 退货订单表(ReturnOrders)表数据库访问层
 *
 * @author makejava
 * @since 2025-01-22 14:55:08
 */
public interface ReturnOrdersMapper extends BaseMapper<ReturnOrders> {

    @Select("""
        SELECT 
            r.id AS id,
            r.order_id AS orderId,
            o.order_number AS orderNumber,
            r.product_id AS productId,
            p.name AS productName,
            r.return_reason AS returnReason,
            r.return_status AS returnStatus,
            r.reject_reason AS rejectReason,
            r.return_date AS returnTime
        FROM return_orders r
        JOIN `order` o ON r.order_id = o.id
        JOIN product p ON r.product_id = p.id
        WHERE 
            (#{orderNumber} IS NULL OR o.order_number LIKE #{orderNumber})
            AND (#{productName} IS NULL OR p.name LIKE #{productName})
            AND (#{returnStatus} IS NULL OR r.return_status = #{returnStatus})
            AND (r.return_date BETWEEN #{startDate} AND #{endDate})
        ORDER BY r.return_date DESC
    """)
    IPage<ReturnOrderVo> getReturnOrderList(Page<?> page,
                                            @Param("orderNumber") String orderNumber,
                                            @Param("productName") String productName,
                                            @Param("returnStatus") String returnStatus,
                                            @Param("startDate") String startDate,
                                            @Param("endDate") String endDate);
}

