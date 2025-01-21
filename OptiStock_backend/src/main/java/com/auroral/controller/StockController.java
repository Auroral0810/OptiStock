package com.auroral.controller;

import com.auroral.dto.StockListDTO;
import com.auroral.dto.UserUpdateDTO;
import com.auroral.entity.ResponseResult;
import com.auroral.enums.AppHttpCodeEnum;
import com.auroral.mapper.ProductMapper;
import com.auroral.service.ProductService;
import com.auroral.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stock")
@Validated
public class StockController {
    @Autowired
    private ProductService productService;

    @PostMapping("/getStockList")
    public ResponseResult getStockList(@RequestHeader("Authorization") String authHeader, @RequestBody StockListDTO stockListDTO){
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
            return ResponseResult.errorResult(AppHttpCodeEnum.TOKEN_EXPIRED);
        }
    }
}
