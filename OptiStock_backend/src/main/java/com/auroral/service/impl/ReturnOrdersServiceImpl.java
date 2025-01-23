package com.auroral.service.impl;

import com.auroral.dto.HandleReturnOrderDTO;
import com.auroral.dto.ReturnOrderListDTO;
import com.auroral.entity.OrderItems;
import com.auroral.entity.Product;
import com.auroral.entity.ResponseResult;
import com.auroral.entity.ReturnOrders;
import com.auroral.enums.AppHttpCodeEnum;
import com.auroral.mapper.OrderItemsMapper;
import com.auroral.mapper.ProductMapper;
import com.auroral.mapper.ReturnOrdersMapper;
import com.auroral.service.ReturnOrdersService;
import com.auroral.vo.PageVo;
import com.auroral.vo.ReturnOrderVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * 退货订单表(ReturnOrders)表服务实现类
 *
 * @author makejava
 * @since 2025-01-22 14:55:08
 */
@Service("returnOrdersService")
public class ReturnOrdersServiceImpl extends ServiceImpl<ReturnOrdersMapper, ReturnOrders> implements ReturnOrdersService {

    @Autowired
    private ReturnOrdersMapper returnOrdersMapper;

    @Autowired
    private OrderItemsMapper orderItemsMapper;
    @Autowired
    private ProductMapper productMapper;
    @Override
    @Transactional
    public ResponseResult getReturnOrderList(ReturnOrderListDTO returnOrderListDTO) {
        // 1. 解析查询条件参数
        Integer pageNum = returnOrderListDTO.getPageNum() != null ? returnOrderListDTO.getPageNum() : 1;
        Integer pageSize = returnOrderListDTO.getPageSize() != null ? returnOrderListDTO.getPageSize() : 10;
        ReturnOrderListDTO.FilterForm filter = returnOrderListDTO.getFilterForm();

        String orderNumber = (filter != null && filter.getOrderNumber() != null && !filter.getOrderNumber().trim().isEmpty())
                ? "%" + filter.getOrderNumber().trim() + "%"
                : null;

        String productName = (filter != null && filter.getProductName() != null && !filter.getProductName().trim().isEmpty())
                ? "%" + filter.getProductName().trim() + "%"
                : null;

        String returnStatus = (filter != null && filter.getReturnStatus() != null && !filter.getReturnStatus().trim().isEmpty())
                ? filter.getReturnStatus().trim()
                : null;

        List<String> dateRange = (filter != null && filter.getDateRange() != null && !filter.getDateRange().isEmpty())
                ? filter.getDateRange()
                : Arrays.asList("1970-01-01 00:00:00", "2099-12-31 23:59:59");

        String startDate = dateRange.get(0);
        String endDate = dateRange.get(1);

        // 2. 设置分页参数
        Page<ReturnOrderVo> page = new Page<>(pageNum, pageSize);

        // 3. 执行查询
        IPage<ReturnOrderVo> result = baseMapper.getReturnOrderList(page, orderNumber, productName, returnStatus, startDate, endDate);

        // 4. 返回结果
        PageVo pageVo = new PageVo(result.getRecords(), result.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    //审核订单
    @Override
    @Transactional
    public ResponseResult handleReturnOrder(HandleReturnOrderDTO handleReturnOrderDTO) {
        // 1. 查询退货订单
        ReturnOrders returnOrder = returnOrdersMapper.selectById(handleReturnOrderDTO.getId());
        if (returnOrder == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_ERROR, "退货订单不存在");
        }
        //判断状态是否是同意或者拒绝
        if(!handleReturnOrderDTO.getReturnStatus().equals("同意") && !handleReturnOrderDTO.getReturnStatus().equals("拒绝")){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_ERROR, "退货状态错误");
        }
        // 2. 处理退货状态
        if ("拒绝".equals(handleReturnOrderDTO.getReturnStatus())) {
            if (handleReturnOrderDTO.getReason() == null || handleReturnOrderDTO.getReason().trim().isEmpty()) {
                return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_ERROR, "拒绝原因不能为空");
            }
            returnOrder.setRejectReason(handleReturnOrderDTO.getReason());
        }

        returnOrder.setReturnStatus(handleReturnOrderDTO.getReturnStatus());
        returnOrdersMapper.updateById(returnOrder);

        // 3. 如果是同意退货，更新库存
        if ("同意".equals(handleReturnOrderDTO.getReturnStatus())) {
            // 根据 order_id 获取 order_items 信息
            OrderItems orderItem = orderItemsMapper.selectByOrderId(returnOrder.getOrderId());

            if (orderItem != null) {
                Long productId = orderItem.getProductId();
                Integer returnQuantity = orderItem.getQuantity();

                // 查询商品信息
                Product product = productMapper.selectById(productId);
                if (product != null) {
                    product.setStockQuantity(product.getStockQuantity() + returnQuantity);
                    productMapper.updateById(product);
                }
            }
        }

        return ResponseResult.okResult("退货订单处理成功");
    }

    //删除退货订单

    @Override
    @Transactional
    public ResponseResult deleteReturnOrder(String id) {
        //1.根据id查询退货订单
        ReturnOrders returnOrder = returnOrdersMapper.selectById(id);
        if(returnOrder == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_ERROR, "退货订单不存在");
        }
        //2.判断订单状态是否为拒绝，只有拒绝的订单能够删除
        if(!returnOrder.getReturnStatus().equals("拒绝")){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_ERROR, "只有拒绝的订单才能删除");
        }
        //3.删除退货订单
        returnOrdersMapper.deleteById(id);
        return ResponseResult.okResult("退货订单删除成功");
    }
}

