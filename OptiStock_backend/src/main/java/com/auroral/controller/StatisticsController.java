package com.auroral.controller;

import com.auroral.dto.InventoryStatisticsDTO;
import com.auroral.dto.PurchaseStatisticsDTO;
import com.auroral.dto.SalesStatisticsDTO;
import com.auroral.entity.ResponseResult;
import com.auroral.enums.AppHttpCodeEnum;
import com.auroral.service.StatisticsService;
import com.auroral.utils.JwtUtil;
import com.auroral.vo.purchsaestatisticsVo.PurchaseStatisticsVO;
import com.auroral.vo.salestatisticVo.SalesStatisticsVO;
import com.auroral.vo.statisticsVo.InventoryStatisticsVO;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/statistics")
public class StatisticsController {


    @Autowired
    private StatisticsService statisticsService;

    @PostMapping("/getInventoryData")
    public ResponseResult getInventoryData(@RequestHeader("Authorization") String authHeader, @RequestBody InventoryStatisticsDTO inventoryStatisticsDTO) {
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
            InventoryStatisticsVO statisticsVO = statisticsService.getInventoryStatistics(inventoryStatisticsDTO);
            return ResponseResult.okResult(statisticsVO);
        } catch (Exception e) {
            log.error("出错", e);
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
    }

    @PostMapping("/getSalesStatistics")
    public ResponseResult getSalesStatistics(@RequestHeader("Authorization") String authHeader, @RequestBody SalesStatisticsDTO salesStatisticsDTO) {
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
            SalesStatisticsVO salesStatisticsVO = statisticsService.getSalesStatistics(salesStatisticsDTO);
            return ResponseResult.okResult(salesStatisticsVO);
        } catch (Exception e) {
            log.error("出错", e);
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
    }

    //获取采购统计数据
    @PostMapping("/getPurchaseStatistics")
    public ResponseResult getPurchaseStatistics(@RequestHeader("Authorization") String authHeader, @RequestBody PurchaseStatisticsDTO purchaseStatisticsDTO) {
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
            PurchaseStatisticsVO purchaseStatisticsVO = statisticsService.getPurchaseStatistics(purchaseStatisticsDTO);
            return ResponseResult.okResult(purchaseStatisticsVO);
        } catch (Exception e) {
            log.error("出错", e);
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
    }

    //获取系统概览数据
    @GetMapping("/overview")
    public ResponseResult getOverviewStatistics(@RequestHeader("Authorization") String authHeader) {
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
            return statisticsService.getOverviewStatistics();
        } catch (Exception e) {
            log.error("出错", e);
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
    }
}