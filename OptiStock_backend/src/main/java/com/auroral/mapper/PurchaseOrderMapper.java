package com.auroral.mapper;

import com.auroral.vo.PurchaseOrderListVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.auroral.entity.PurchaseOrder;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


/**
 * 采购订单表(PurchaseOrder)表数据库访问层
 *
 * @author makejava
 * @since 2025-01-21 20:19:54
 */
public interface PurchaseOrderMapper extends BaseMapper<PurchaseOrder> {
    @Select("""
    SELECT 
        fo.id AS orderId, 
        p.name AS productName, 
        s.name AS supplierName, 
        pi.quantity, 
        pi.cost_price AS costPrice, 
        fo.total_cost AS totalCost, 
        fo.status, 
        fo.created_at AS createdAt,
        s.contact AS contact,       -- 供应商联系人姓名
        s.phone AS phone,           -- 供应商联系电话
        s.email AS email,           -- 供应商电子邮件
        s.address AS address        -- 供应商地址
    FROM (
        SELECT id, supplier_id, total_cost, status, created_at
        FROM purchase_order
        WHERE (#{status} IS NULL OR status = #{status})
          AND created_at BETWEEN #{startDate} AND #{endDate}
    ) AS fo
    JOIN supplier s ON fo.supplier_id = s.id
    JOIN purchase_items pi ON fo.id = pi.purchase_order_id
    JOIN product p ON pi.product_id = p.id
    WHERE (#{supplierName} IS NULL OR s.name LIKE CONCAT('%', #{supplierName}, '%'))
    ORDER BY fo.created_at DESC
""")
    IPage<PurchaseOrderListVo> getPurchaseOrderList(
            IPage<?> page,
            @Param("status") String status,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("supplierName") String supplierName
    );

}

