package com.auroral.service.impl;

import com.auroral.entity.PurchaseOrder;
import com.auroral.mapper.PurchaseOrderMapper;
import com.auroral.service.PurchaseOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 采购订单表(PurchaseOrder)表服务实现类
 *
 * @author makejava
 * @since 2025-01-21 20:19:54
 */
@Service("purchaseOrderService")
public class PurchaseOrderServiceImpl extends ServiceImpl<PurchaseOrderMapper, PurchaseOrder> implements PurchaseOrderService {

}

