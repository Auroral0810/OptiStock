package com.auroral.service.impl;

import com.auroral.entity.Supplier;
import com.auroral.mapper.SupplierMapper;
import com.auroral.service.SupplierService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 供应商信息表(Supplier)表服务实现类
 *
 * @author makejava
 * @since 2025-01-21 12:07:20
 */
@Service("supplierService")
public class SupplierServiceImpl extends ServiceImpl<SupplierMapper, Supplier> implements SupplierService {

}

