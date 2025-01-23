package com.auroral.controller;

import com.auroral.dto.AdjustStockDTO;
import com.auroral.dto.StockListDTO;
import com.auroral.entity.ResponseResult;
import com.auroral.enums.AppHttpCodeEnum;
import com.auroral.service.ProductService;
import com.auroral.service.StockAdjustmentsService;
import com.auroral.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/stock")
@Validated
public class StockController {
    @Autowired
    private ProductService productService;

    @Autowired
    private StockAdjustmentsService stockAdjustmentsService;

    @PostMapping("/getStockList")
    public ResponseResult getStockList(@RequestHeader("Authorization") String authHeader, @RequestBody StockListDTO stockListDTO) {
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
            return productService.getStockList(stockListDTO);
        } catch (Exception e) {
            log.error("出错", e);
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
    }

    //调整库存
    @PutMapping("/adjustStock")
    public ResponseResult adjustStock(@RequestHeader("Authorization") String authHeader, @RequestBody AdjustStockDTO adjustStockDTO) {
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
            return stockAdjustmentsService.adjustStock(adjustStockDTO);
        } catch (Exception e) {
            log.error("出错", e);
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
    }

    //获取对应id的调整记录,以及搜索条件（）
//    http://localhost:9092/stock/getStockAdjustRecord?id=1&timeRange=week
    @GetMapping("/getStockAdjustRecord")
    public ResponseResult getStockAdjustRecord(@RequestHeader("Authorization") String authHeader, @RequestParam("id") Long id, @RequestParam("timeRange") String timeRange) {
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
            return stockAdjustmentsService.getStockAdjustRecord(id, timeRange);
        } catch (Exception e) {
            log.error("出错", e);
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
    }

    //获取阈值数据
    @PostMapping("/getThresholdDataList")
    public ResponseResult getThresholdDataList(@RequestHeader("Authorization") String authHeader, @RequestBody StockListDTO stockListDTO) {
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
            return productService.getThresholdDataList(stockListDTO);
        } catch (Exception e) {
            log.error("出错", e);
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
    }

    //更新阈值
    @PatchMapping("/updateThreshold")
    public ResponseResult updateThreshold(@RequestHeader("Authorization") String authHeader, @RequestBody Map<String, Object> requestData) {
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
            return productService.updateThreshold(requestData);
        } catch (Exception e) {
            log.error("出错", e);
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
    }
}
