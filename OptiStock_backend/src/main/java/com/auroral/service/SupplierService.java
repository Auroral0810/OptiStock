package com.auroral.service;

import com.auroral.dto.AddSupplierDTO;
import com.auroral.dto.SupplierListDTO;
import com.auroral.dto.UpdateSupplierDTO;
import com.auroral.entity.ResponseResult;
import com.auroral.entity.Supplier;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * 供应商信息表(Supplier)表服务接口
 *
 * @author makejava
 * @since 2025-01-21 12:07:20
 */
public interface SupplierService extends IService<Supplier> {

    ResponseResult getSupplierList(SupplierListDTO supplierListDTO);

    ResponseResult addSupplier(AddSupplierDTO addSupplierDTO);

    ResponseResult updateSupplier(UpdateSupplierDTO updateSupplierDTO);

    ResponseResult deleteSupplier(Long id);
}

