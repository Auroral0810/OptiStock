package com.auroral.controller;

import com.auroral.dto.AddPurchaseOrderDTO;
import com.auroral.dto.PurchaseOrderListDTO;
import com.auroral.entity.ResponseResult;
import com.auroral.enums.AppHttpCodeEnum;
import com.auroral.service.PurchaseOrderService;
import com.auroral.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/purchase")
public class PurchaseOrderController {
    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @PostMapping("/getPurchaseOrderList")
    public ResponseResult getPurchaseOrderList(@RequestHeader("Authorization") String authHeader, @RequestBody PurchaseOrderListDTO purchaseOrderListDTO) {
//校验token看看是否有权限
        String token = authHeader.replace("Bearer ", "");
        Claims claims = null; // 解析 Token
        try {
            claims = JwtUtil.parseJWT(token);
            String userId = claims.getSubject(); // 获取 UserId (subject 存的 userId)
            //判断是否合法
            if (userId == null) {
                //无权限
                return ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH);
            }
            return purchaseOrderService.getPurchaseOrderList(purchaseOrderListDTO);
        } catch (Exception e) {
            log.error("获取采购订单列表出错", e);
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
    }

    //删除采购订单
    @DeleteMapping("/deletePurchaseOrder")
    public ResponseResult deletePurchaseOrder(@RequestHeader("Authorization") String authHeader, @RequestParam("id") String id) {
        //校验token看看是否有权限
        String token = authHeader.replace("Bearer ", "");
        Claims claims = null; // 解析 Token
        try {
            claims = JwtUtil.parseJWT(token);
            String userId = claims.getSubject(); // 获取 UserId (subject 存的 userId)
            //判断是否合法
            if (userId == null) {
                //无权限
                return ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH);
            }
            return purchaseOrderService.deletePurchaseOrder(id);
        } catch (Exception e) {
            log.error("出错", e);
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
    }

    //新增采购订单
    @PutMapping("/addPurchaseOrder")
    public ResponseResult addPurchaseOrder(@RequestHeader("Authorization") String authHeader, @RequestBody AddPurchaseOrderDTO addPurchaseOrderDTO) {
        //校验token看看是否有权限
        String token = authHeader.replace("Bearer ", "");
        Claims claims = null; // 解析 Token
        try {
            claims = JwtUtil.parseJWT(token);
            String userId = claims.getSubject(); // 获取 UserId (subject 存的 userId)
            //判断是否合法
            if (userId == null) {
                //无权限
                return ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH);
            }
            return purchaseOrderService.addPurchaseOrder(addPurchaseOrderDTO);
        } catch (Exception e) {
            log.error("出错", e);
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
    }

    //审核采购订单
    @PatchMapping("/auditPurchaseOrder")
    public ResponseResult auditPurchaseOrder(@RequestHeader("Authorization") String authHeader, @RequestParam("id") Long id, @RequestParam("status") String status) {
        //校验token看看是否有权限
        String token = authHeader.replace("Bearer ", "");
        Claims claims = null; // 解析 Token
        try {
            claims = JwtUtil.parseJWT(token);
            String userId = claims.getSubject(); // 获取 UserId (subject 存的 userId)
            //判断是否合法
            if (userId == null) {
                //无权限
                return ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH);
            }
            return purchaseOrderService.auditPurchaseOrder(id, status);
        } catch (Exception e) {
            log.error("出错", e);
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
    }

    //返回供应商名称和商品名称
    @GetMapping("/getSupplierAndProduct")
    public ResponseResult getSupplierAndProduct(@RequestHeader("Authorization") String authHeader) {
        //校验token看看是否有权限
        String token = authHeader.replace("Bearer ", "");
        Claims claims = null; // 解析 Token
        try {
            claims = JwtUtil.parseJWT(token);
            String userId = claims.getSubject(); // 获取 UserId (subject 存的 userId)
            //判断是否合法
            if (userId == null) {
                //无权限
                return ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH);
            }
            return purchaseOrderService.getSupplierAndProduct();
        } catch (Exception e) {
            log.error("出错", e);
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
    }
}
