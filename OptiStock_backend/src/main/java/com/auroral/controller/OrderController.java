package com.auroral.controller;

import com.auroral.dto.*;
import com.auroral.entity.Order;
import com.auroral.entity.ResponseResult;
import com.auroral.enums.AppHttpCodeEnum;
import com.auroral.service.OrderService;
import com.auroral.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/getOrderList")
    public ResponseResult getOrderList(@RequestHeader("Authorization") String authHeader, @RequestBody OrderListDTO orderListDTO) {
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
            return orderService.getOrderList(orderListDTO);
        } catch (Exception e) {
            return ResponseResult.errorResult(AppHttpCodeEnum.TOKEN_EXPIRED);
        }
    }
    //发货订单
    @PatchMapping("/changeOrderStatus")
    public ResponseResult changeOrderStatus(@RequestHeader("Authorization") String authHeader, @RequestParam("orderNumber")  String  orderNumber) {
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
            return orderService.changeOrderStatus(orderNumber);
        } catch (Exception e) {
            return ResponseResult.errorResult(AppHttpCodeEnum.TOKEN_EXPIRED);
        }
    }



    //新增订单
    @PutMapping("/addOrder")
    public ResponseResult addOrder(@RequestHeader("Authorization") String authHeader, @RequestBody AddOrderDTO addOrderDTO) {
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
            return orderService.addOrder(addOrderDTO);
        } catch (Exception e) {
            return ResponseResult.errorResult(AppHttpCodeEnum.TOKEN_EXPIRED);
        }
    }
    //获取商品名称数据
    @GetMapping("/getProductName")
    public ResponseResult getProductName(@RequestHeader("Authorization") String authHeader) {
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
            return orderService.getProductName();
        } catch (Exception e) {
            return ResponseResult.errorResult(AppHttpCodeEnum.TOKEN_EXPIRED);
        }
    }

    //删除订单
    @DeleteMapping("/deleteOrder")
    public ResponseResult deleteOrder(@RequestHeader("Authorization") String authHeader, @RequestParam("orderNumber")  String  orderNumber) {
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
            return orderService.deleteOrder(orderNumber);
        } catch (Exception e) {
            return ResponseResult.errorResult(AppHttpCodeEnum.TOKEN_EXPIRED);
        }
    }
    //编辑订单
    @PatchMapping("/updateOrder")
    public ResponseResult updateOrder(@RequestHeader("Authorization") String authHeader,@RequestBody UpdateOrderDTO updateOrderDTO) {
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
            return orderService.updateOrder(updateOrderDTO);
        } catch (Exception e) {
            return ResponseResult.errorResult(AppHttpCodeEnum.TOKEN_EXPIRED);
        }
    }


}
