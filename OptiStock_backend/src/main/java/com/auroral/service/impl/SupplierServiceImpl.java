package com.auroral.service.impl;

import com.auroral.dto.AddSupplierDTO;
import com.auroral.dto.StockListDTO;
import com.auroral.dto.SupplierListDTO;
import com.auroral.dto.UpdateSupplierDTO;
import com.auroral.entity.Product;
import com.auroral.entity.PurchaseOrder;
import com.auroral.entity.ResponseResult;
import com.auroral.entity.Supplier;
import com.auroral.enums.AppHttpCodeEnum;
import com.auroral.mapper.PurchaseOrderMapper;
import com.auroral.mapper.SupplierMapper;
import com.auroral.service.SupplierService;
import com.auroral.utils.BeanCopyUtils;
import com.auroral.vo.PageVo;
import com.auroral.vo.StockVo;
import com.auroral.vo.SupplierVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 供应商信息表(Supplier)表服务实现类
 *
 * @author makejava
 * @since 2025-01-21 12:07:20
 */
@Service("supplierService")
public class SupplierServiceImpl extends ServiceImpl<SupplierMapper, Supplier> implements SupplierService {
    @Autowired
    private PurchaseOrderMapper purchaseOrderMapper;
    //获取供应商信息列表
    @Override
    public ResponseResult getSupplierList(SupplierListDTO supplierListDTO) {
        /*1.根据条件查询供应商信息
         * 2.封装Vo对象
         * 3.返回结果
         * */
        //先获取查询条件
        Integer pageNum = supplierListDTO.getPageNum() != null ? supplierListDTO.getPageNum() : 1;
        Integer pageSize = supplierListDTO.getPageSize() != null ? supplierListDTO.getPageSize() : 10;
        String name = supplierListDTO.getFilterForm().getName();
        String phone = supplierListDTO.getFilterForm().getPhone();
        String contact = supplierListDTO.getFilterForm().getContact();
        Page<Supplier> page = new Page<>(pageNum, pageSize);
        //构建查询条件
        LambdaQueryWrapper<Supplier> queryWrapper = new LambdaQueryWrapper<>();
        if (name != null && !name.trim().isEmpty()) {
            queryWrapper.like(Supplier::getName, name.trim());
        }
        if (phone != null && !phone.trim().isEmpty()) {
            queryWrapper.like(Supplier::getPhone, phone.trim());
        }
        if (contact != null && !contact.trim().isEmpty()) {
            queryWrapper.like(Supplier::getContact, contact.trim());
        }
        queryWrapper.orderByAsc(Supplier::getId);
        //执行查询
        page(page, queryWrapper);
        //获取数据
        List<Supplier> supplierList = page.getRecords();
        //封装VO
        List<SupplierVo> supplierVoList = BeanCopyUtils.copyBeanList(supplierList, SupplierVo.class);
        //返回结果
        PageVo pageVo = new PageVo(supplierVoList, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }
    //更新供应商信息
    @Override
    public ResponseResult updateSupplier(UpdateSupplierDTO updateSupplierDTO) {
        //根据供应商id查询是否存在
        Supplier supplier = getById(updateSupplierDTO.getId());
        if (supplier == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_ERROR, "该供应商不存在");
        }
        //保存供应商信息
        Supplier supplierEntity = BeanCopyUtils.copyBean(updateSupplierDTO, Supplier.class);
        updateById(supplierEntity);
        return ResponseResult.okResult("更新成功");
    }
    //添加供应商信息
    @Override
    public ResponseResult addSupplier(AddSupplierDTO addSupplierDTO) {
        //根据供应商名称查询是否存在
        LambdaQueryWrapper<Supplier> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Supplier::getName, addSupplierDTO.getName());
        Supplier supplier = getOne(queryWrapper);
        if (supplier != null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_ERROR,"该供应商名称已存在，请重新输入");
        }
        //保存供应商信息
        Supplier supplierEntity = BeanCopyUtils.copyBean(addSupplierDTO, Supplier.class);
        save(supplierEntity);
        return ResponseResult.okResult("添加成功");
    }

    //删除供应商信息
    @Override
    public ResponseResult deleteSupplier(Long id) {
        //查询采购订单表是否存在当前供应商还未完成的订单
        LambdaQueryWrapper<PurchaseOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PurchaseOrder::getSupplierId, id);
        //状态不等于“已完成”
        queryWrapper.ne(PurchaseOrder::getStatus, "已完成");
        //如果存在，则不能删除
        if (purchaseOrderMapper.selectCount(queryWrapper) > 0) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_ERROR,"该供应商存在未完成的采购订单，不能删除");
        }
        //根据id删除供应商信息
        removeById(id);
        return ResponseResult.okResult("删除成功");
    }
}

