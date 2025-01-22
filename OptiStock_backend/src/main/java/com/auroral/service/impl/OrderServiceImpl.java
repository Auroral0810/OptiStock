package com.auroral.service.impl;

import com.auroral.dto.AddOrderDTO;
import com.auroral.dto.OrderListDTO;
import com.auroral.dto.PurchaseOrderListDTO;
import com.auroral.dto.UpdateOrderDTO;
import com.auroral.entity.*;
import com.auroral.enums.AppHttpCodeEnum;
import com.auroral.mapper.OrderMapper;
import com.auroral.mapper.ProductMapper;
import com.auroral.service.OrderItemsService;
import com.auroral.service.OrderService;
import com.auroral.service.ProductService;
import com.auroral.service.StockOutService;
import com.auroral.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 订单信息表(Order)表服务实现类
 *
 * @author makejava
 * @since 2025-01-22 14:54:35
 */
@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private OrderItemsService orderItemsService;
    @Autowired
    private ProductService productService;
    @Autowired
    private StockOutService stockOutService;
    @Override
    public ResponseResult getOrderList(OrderListDTO orderListDTO) {
        // 1. 解析查询的条件参数
        Integer pageNum = (orderListDTO.getPageNum() != null) ? orderListDTO.getPageNum() : 1;
        Integer pageSize = (orderListDTO.getPageSize() != null) ? orderListDTO.getPageSize() : 10;
        OrderListDTO.FilterForm filter = orderListDTO.getFilterForm();
        // 处理空值赋默认值
        String keyword = (filter != null && filter.getKeyword() != null && !filter.getKeyword().trim().isEmpty())
                ? "%" + filter.getKeyword().trim() + "%"
                : null;

        String status = (filter != null && filter.getStatus() != null && !filter.getStatus().trim().isEmpty())
                ? filter.getStatus().trim()
                : null;
        String platform = (filter != null && filter.getPlatform() != null && !filter.getPlatform().trim().isEmpty())
                ? filter.getPlatform().trim()
                 : null;
        List<String> dateRange = (filter != null && filter.getDateRange() != null && !filter.getDateRange().isEmpty())
                ? filter.getDateRange()
                : Arrays.asList("1970-01-01 00:00:00", "2099-12-31 23:59:59");

        String startDate = dateRange.get(0);
        String endDate = dateRange.get(1);

        // 2. 设置分页参数
        Page<OrderListVo> page = new Page<>(pageNum, pageSize);

        // 3. 执行查询
        IPage<OrderListVo> result = orderMapper.getOrderList(
                page, keyword, status, platform, startDate, endDate
        );

        // 4. 返回结果
        PageVo pageVo = new PageVo(result.getRecords(), result.getTotal());
        return ResponseResult.okResult(pageVo);
    }
    //订单发货
    @Override
    @Transactional
    public ResponseResult changeOrderStatus(String orderNumber) {
        //查询是否有当前订单
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        //是字符串类型
        wrapper.eq(Order::getOrderNumber, orderNumber).last("FOR UPDATE");

        Order order = getOne(wrapper);
        if(order == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_ERROR,"订单不存在");
        }
        if(!order.getStatus().equals("待发货")){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_ERROR,"订单状态不正确");
        }
        //同时修改库存
        //根据当前的order中的id查询order_items表中的对应订单，获取quantity字段和product_id字段
        //根据product_id字段查询product表中的库存，减少库存
        LambdaQueryWrapper<OrderItems> itemQuery = new LambdaQueryWrapper<>();
        itemQuery.eq(OrderItems::getOrderId, order.getId());
        OrderItems orderItems = orderItemsService.getOne(itemQuery);

        if(orderItems == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_ERROR,"订单商品详情有误");
        }
        Long productId = orderItems.getProductId();
        Integer quantity = orderItems.getQuantity();
        //根据productId查询product表中的库存
        Product product = productService.getById(productId);
        if(!product.getStatus().equals("上架")){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_ERROR,"商品已下架，不能发货");
        }
        if(product.getStockQuantity() < quantity){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_ERROR,"商品库存不足");
        }
        product.setStockQuantity(product.getStockQuantity() - quantity);
        productService.updateById(product);
        StockOut stockOut = new StockOut();
        stockOut.setOrderId(order.getId());
        stockOut.setProductId(productId);
        stockOut.setQuantity(quantity);
        //TODO 修改传入参数，带上当前的用户信息
        stockOut.setShippedBy(1L);
        stockOutService.save(stockOut);
        //修改库存
        //修改订单状态
        order.setStatus("已发货");
        updateById(order);
        //新增出库记录
        //TODO
        return ResponseResult.okResult();
    }

    //新增订单
    @Override
    @Transactional
    public ResponseResult addOrder(AddOrderDTO addOrderDTO) {
        // 1. 校验库存是否足够
        Product product = productService.getById(addOrderDTO.getProductId());
        if (product == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_ERROR, "商品不存在");
        }
        if (product.getStockQuantity() < addOrderDTO.getQuantity()) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_ERROR, "库存不足");
        }

        // 2. 插入订单表 (order)
        Order order = new Order();
        order.setOrderNumber(addOrderDTO.getOrderId());
        order.setCustomerName(addOrderDTO.getCustomerName());
        order.setPlatform(addOrderDTO.getPlatform());
        order.setPhone(addOrderDTO.getPhone());
        order.setAddress(addOrderDTO.getAddress());
        order.setTotalAmount(addOrderDTO.getTotalPrice());
        order.setStatus("待付款");

        boolean orderSaved = save(order);
        if (!orderSaved) {
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR, "订单创建失败");
        }

        // 3. 获取订单ID，插入订单商品详情表 (order_items)
        OrderItems orderItems = new OrderItems();
        orderItems.setOrderId(order.getId());
        orderItems.setProductId(addOrderDTO.getProductId());
        orderItems.setQuantity(addOrderDTO.getQuantity());
        orderItems.setPrice(addOrderDTO.getPrice());
        orderItems.setTotalPrice(addOrderDTO.getTotalPrice());

        boolean itemSaved = orderItemsService.save(orderItems);
        if (!itemSaved) {
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR, "订单商品详情创建失败");
        }

        // 4. 返回成功结果
        return ResponseResult.okResult();
    }


    //删除订单
    @Transactional
    @Override
    public ResponseResult deleteOrder(String orderNumber) {
        // 1. 根据ID查询订单
        Order order = getOne(new LambdaQueryWrapper<Order>().eq(Order::getOrderNumber, orderNumber));
        if (order == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_ERROR, "订单不存在");
        }
        //2.判断订单状态，只有已取消、待付款的订单可以删除
        if ( !order.getStatus().equals("已取消") && !order.getStatus().equals("待付款")) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_ERROR, "订单状态不正确，不能删除");
        }
        Long orderId = order.getId();
        //3.删除订单
        boolean orderDeleted = removeById(orderId);
        if (!orderDeleted) {
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR, "订单删除失败");
        }
        //4.删除订单商品详情
        boolean itemDeleted = orderItemsService.remove(new LambdaQueryWrapper<OrderItems>().eq(OrderItems::getOrderId, orderId));
        if (!itemDeleted) {
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR, "订单商品详情删除失败");
        }
        //4.返回成功结果
        return ResponseResult.okResult();
    }

    //获取商品名称
    @Override
    public ResponseResult getProductName() {
        List<ProductNameVo.ProductInfo> productNameList = productMapper.getProductName();
        ProductNameVo productNameVo = new ProductNameVo();
        productNameVo.setProductNameList(productNameList);
        return ResponseResult.okResult(productNameVo);
    }

    //修改订单
    @Override
    @Transactional
    public ResponseResult updateOrder(UpdateOrderDTO updateOrderDTO) {
        // 1. 查询是否存在此订单
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getOrderNumber, updateOrderDTO.getOrderNumber());
        Order order = getOne(wrapper);

        if (order == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_ERROR, "订单不存在");
        }

        // 2. 更新订单信息
        order.setCustomerName(updateOrderDTO.getCustomerName());
        order.setPhone(updateOrderDTO.getPhone());
        order.setAddress(updateOrderDTO.getAddress());

        boolean success = updateById(order);
        if (!success) {
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR, "更新订单失败");
        }

        return ResponseResult.okResult("订单更新成功");
    }
}

