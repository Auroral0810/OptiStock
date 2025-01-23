package com.auroral.controller;

import com.auroral.dto.HandleReturnOrderDTO;
import com.auroral.dto.ReturnOrderListDTO;
import com.auroral.entity.ResponseResult;
import com.auroral.enums.AppHttpCodeEnum;
import com.auroral.service.ReturnOrdersService;
import com.auroral.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/returnorder")
public class ReturnOrderController {
    @Autowired
    private ReturnOrdersService returnOrdersService;

    //获取退货订单数据
    /*  {
    id: '',
    orderId: '',
    productId: '',
    productName: '',
    returnReason: '',
    returnStatus: '',
    rejectReason: '',
    returnTime: ''
  }*/
    @PostMapping("/getReturnOrderList")
    public ResponseResult getReturnOrderList(@RequestHeader("Authorization") String authHeader, @RequestBody ReturnOrderListDTO returnOrderListDTO) {
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
            return returnOrdersService.getReturnOrderList(returnOrderListDTO);
        } catch (Exception e) {
            log.error("出错", e);
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
    }

    //处理退货订单
    @PatchMapping("/handleReturnOrder")
    public ResponseResult handleReturnOrder(@RequestHeader("Authorization") String authHeader, @RequestBody @Valid HandleReturnOrderDTO handleReturnOrderDTO) {
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
            return returnOrdersService.handleReturnOrder(handleReturnOrderDTO);
        } catch (Exception e) {
            log.error("出错", e);
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
    }

    /// 处理删除退货订单
    @DeleteMapping("/deleteReturnOrder")
    public ResponseResult deleteReturnOrder(@RequestHeader("Authorization") String authHeader, @RequestParam("id") String id) {
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
            return returnOrdersService.deleteReturnOrder(id);
        } catch (Exception e) {
            log.error("出错", e);
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
    }
    //校验token看看是否有权限
}
