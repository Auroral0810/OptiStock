package com.auroral.service;

import com.auroral.dto.AddPurchaseOrderDTO;
import com.auroral.dto.PurchaseOrderListDTO;
import com.auroral.entity.PurchaseOrder;
import com.auroral.entity.ResponseResult;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * 采购订单表(PurchaseOrder)表服务接口
 *
 * @author makejava
 * @since 2025-01-21 20:19:54
 */
public interface PurchaseOrderService extends IService<PurchaseOrder> {

    ResponseResult getPurchaseOrderList(PurchaseOrderListDTO purchaseOrderListDTO);

    ResponseResult deletePurchaseOrder(String id);

    ResponseResult auditPurchaseOrder(Long id, String status);

    ResponseResult getSupplierAndProduct();

    ResponseResult addPurchaseOrder(AddPurchaseOrderDTO addPurchaseOrderDTO);
}

