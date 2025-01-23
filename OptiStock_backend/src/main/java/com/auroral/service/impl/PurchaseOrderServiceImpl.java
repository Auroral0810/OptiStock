package com.auroral.service.impl;

import com.auroral.dto.AddPurchaseOrderDTO;
import com.auroral.dto.PurchaseOrderListDTO;
import com.auroral.entity.PurchaseItems;
import com.auroral.entity.PurchaseOrder;
import com.auroral.entity.ResponseResult;
import com.auroral.enums.AppHttpCodeEnum;
import com.auroral.mapper.ProductMapper;
import com.auroral.mapper.PurchaseItemsMapper;
import com.auroral.mapper.PurchaseOrderMapper;
import com.auroral.mapper.SupplierMapper;
import com.auroral.service.PurchaseItemsService;
import com.auroral.service.PurchaseOrderService;
import com.auroral.vo.PageVo;
import com.auroral.vo.PurchaseOrderListVo;
import com.auroral.vo.SupplierAndProductVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 采购订单表(PurchaseOrder)表服务实现类
 *
 * @author makejava
 * @since 2025-01-21 20:19:54
 */
@Service("purchaseOrderService")
public class PurchaseOrderServiceImpl extends ServiceImpl<PurchaseOrderMapper, PurchaseOrder> implements PurchaseOrderService {
    @Autowired
    private PurchaseOrderMapper purchaseOrderMapper;

    @Autowired
    private PurchaseItemsMapper purchaseItemsMapper;

    @Autowired
    private SupplierMapper supplierMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private PurchaseItemsService purchaseItemsService;
    @Override
    @Transactional
    public ResponseResult getPurchaseOrderList(PurchaseOrderListDTO purchaseOrderListDTO) {
        // 1. 解析查询的条件参数
        Integer pageNum = (purchaseOrderListDTO.getPageNum() != null) ? purchaseOrderListDTO.getPageNum() : 1;
        Integer pageSize = (purchaseOrderListDTO.getPageSize() != null) ? purchaseOrderListDTO.getPageSize() : 10;
        PurchaseOrderListDTO.FilterForm filter = purchaseOrderListDTO.getFilterForm();

        // 处理空值赋默认值
        String supplierName = (filter != null && filter.getSupplierId() != null && !filter.getSupplierId().trim().isEmpty())
                ? "%" + filter.getSupplierId().trim() + "%"
                : null;

        String status = (filter != null && filter.getStatus() != null && !filter.getStatus().trim().isEmpty())
                ? filter.getStatus().trim()
                : null;

        List<String> dateRange = (filter != null && filter.getDateRange() != null && !filter.getDateRange().isEmpty())
                ? filter.getDateRange()
                : Arrays.asList("1970-01-01 00:00:00", "2099-12-31 23:59:59");

        String startDate = dateRange.get(0);
        String endDate = dateRange.get(1);

        // 2. 设置分页参数
        Page<PurchaseOrderListVo> page = new Page<>(pageNum, pageSize);

        // 3. 执行查询
        IPage<PurchaseOrderListVo> result = purchaseOrderMapper.getPurchaseOrderList(
                page,
                status,
                startDate,
                endDate,
                supplierName
        );

        // 4. 返回结果
        PageVo pageVo = new PageVo(result.getRecords(), result.getTotal());
        return ResponseResult.okResult(pageVo);
    }
    // 实现删除采购订单
    @Override
    @Transactional
    public ResponseResult deletePurchaseOrder(String id) {
        //判断当前采购订单状态是否是待审核或者已驳回状态
        PurchaseOrder purchaseOrder = getById(id);
        if(!purchaseOrder.getStatus().equals("待审核") &&!purchaseOrder.getStatus().equals("已驳回")){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_ERROR, "当前订单状态不允许删除");
        }
        //删除采购订单
        removeById(id);
        //同时删除采购商品详情表中对应的订单
        LambdaQueryWrapper<PurchaseItems> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PurchaseItems::getPurchaseOrderId, id);
        purchaseItemsMapper.delete(queryWrapper);
        //返回成功结果
        return ResponseResult.okResult();
    }
    //实现新增采购订单

    @Override
    @Transactional
    public ResponseResult addPurchaseOrder(AddPurchaseOrderDTO addPurchaseOrderDTO) {
        // 1. 解析参数
        Long productId = addPurchaseOrderDTO.getProductId();
        Long supplierId = addPurchaseOrderDTO.getSupplierId();
        Integer quantity = addPurchaseOrderDTO.getQuantity();
        Double costPrice = addPurchaseOrderDTO.getCostPrice();
        Double totalPrice = addPurchaseOrderDTO.getTotalPrice();

        if (productId == null || supplierId == null || quantity == null || costPrice == null || totalPrice == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_ERROR, "缺少必要参数");
        }

        // 2. 查询商品和供应商是否存在
        if (productMapper.selectById(productId) == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_ERROR, "商品不存在");
        }
        if (supplierMapper.selectById(supplierId) == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_ERROR, "供应商不存在");
        }
        // 2. 新增 purchase_order 记录，使用 MyBatis-Plus save 方法
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setSupplierId(supplierId);
        purchaseOrder.setTotalCost(totalPrice);
        purchaseOrder.setStatus("待审核");

        // 保存 purchase_order 记录并获取 ID
        boolean orderSaved = save(purchaseOrder);
        if (!orderSaved) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_ERROR, "采购订单添加失败");
        }
        // 获取插入后的订单 ID
        Long purchaseOrderId = purchaseOrder.getId();

        // 3. 新增 purchase_items 记录
        PurchaseItems purchaseItem = new PurchaseItems();
        purchaseItem.setPurchaseOrderId(purchaseOrderId);
        purchaseItem.setProductId(productId);
        purchaseItem.setQuantity(quantity);
        purchaseItem.setCostPrice(costPrice);
        purchaseItem.setTotalPrice(totalPrice);

        // 保存 purchase_items 记录
        boolean itemSaved = purchaseItemsService.save(purchaseItem);
        if (!itemSaved) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_ERROR, "采购商品添加失败");
        }

        // 4. 返回结果
        return ResponseResult.okResult();
    }

    //审核采购订单
    @Override
    @Transactional
    public ResponseResult auditPurchaseOrder(Long id, String status) {
        //修改采购订单状态,判断是否是待审核状态
        //判断当前采购订单状态是否是待审核或者已驳回状态
        PurchaseOrder purchaseOrder = getById(id);
        if(!purchaseOrder.getStatus().equals("待审核")) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_ERROR, "当前订单状态不允许审核");
        }
        purchaseOrder.setStatus(status);
        updateById(purchaseOrder);
        //返回成功结果
        return ResponseResult.okResult();
    }
    //返回供应商名称和商品名称

    @Override
    @Transactional
    public ResponseResult getSupplierAndProduct() {
        // 1. 查询商品的名称和ID
        List<SupplierAndProductVo.ProductInfo> productList = productMapper.getAllProducts()
                .stream()
                .map(product -> new SupplierAndProductVo.ProductInfo(product.getProductId(), product.getProductName(), product.getCostPrice()))
                .collect(Collectors.toList());

        // 2. 查询供应商的名称和ID
        List<SupplierAndProductVo.SupplierInfo> supplierList = supplierMapper.getAllSuppliers()
                .stream()
                .map(supplier -> new SupplierAndProductVo.SupplierInfo(supplier.getSupplierId(), supplier.getSupplierName()))
                .collect(Collectors.toList());

        // 3. 封装VO
        SupplierAndProductVo supplierAndProductVo = new SupplierAndProductVo(productList, supplierList);

        // 4. 返回结果
        return ResponseResult.okResult(supplierAndProductVo);
    }

}


