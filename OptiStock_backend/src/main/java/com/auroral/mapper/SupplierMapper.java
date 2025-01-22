package com.auroral.mapper;

import com.auroral.entity.Supplier;
import com.auroral.vo.SupplierAndProductVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * 供应商信息表(Supplier)表数据库访问层
 *
 * @author makejava
 * @since 2025-01-21 12:07:20
 */
public interface SupplierMapper extends BaseMapper<Supplier> {

    @Select("SELECT id AS supplierId, name AS supplierName FROM supplier")
    List<SupplierAndProductVo.SupplierInfo> getAllSuppliers();
}

