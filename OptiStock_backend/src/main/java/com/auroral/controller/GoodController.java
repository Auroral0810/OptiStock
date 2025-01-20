package com.auroral.controller;

import com.auroral.dto.AddCategoryRequest;
import com.auroral.dto.PageRequestDTO;
import com.auroral.dto.UpdateCategoryRequest;
import com.auroral.entity.ResponseResult;
import com.auroral.enums.AppHttpCodeEnum;
import com.auroral.service.ProductCategoryService;
import com.auroral.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class GoodController {

    @Autowired
    private ProductCategoryService productCategoryService;

    @PostMapping("/getAllProductCategory")
    public ResponseResult getAllProductCategory(@RequestHeader("Authorization") String authHeader,@RequestBody PageRequestDTO pageRequestDTO) {
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
            return productCategoryService.getAllProductCategory(pageRequestDTO.getPageNum(), pageRequestDTO.getPageSize());
        } catch (Exception e) {
            return ResponseResult.errorResult(AppHttpCodeEnum.TOKEN_EXPIRED);
        }
    }

    @GetMapping("/getParentCategoty")
    public ResponseResult getParentCategoty(@RequestHeader("Authorization") String authHeader) {
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
            return productCategoryService.getParentCategoty();
        } catch (Exception e) {
            return ResponseResult.errorResult(AppHttpCodeEnum.TOKEN_EXPIRED);
        }
    }
    //添加商品分类
    @PostMapping("/addCategory")
    public ResponseResult addCategory(@RequestHeader("Authorization") String authHeader, @RequestBody AddCategoryRequest addCategoryRequest) {
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
            return productCategoryService.addCategory(addCategoryRequest);
        } catch (Exception e) {
            return ResponseResult.errorResult(AppHttpCodeEnum.TOKEN_EXPIRED);
        }
    }
    //删除分类
    @DeleteMapping("/deleteCategory")
    public ResponseResult deleteCategory(@RequestHeader("Authorization") String authHeader, @RequestParam Long id) {
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
            return productCategoryService.deleteCategory(id);
        } catch (Exception e) {
            return ResponseResult.errorResult(AppHttpCodeEnum.TOKEN_EXPIRED);
        }

    }
    //更新分类
    @PatchMapping("/updateCategoryInfo")
    public ResponseResult updateCategoryInfo(@RequestHeader("Authorization") String authHeader, @RequestBody UpdateCategoryRequest updateCategoryRequest) {
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
            return productCategoryService.updateCategory(updateCategoryRequest);
        } catch (Exception e) {
            return ResponseResult.errorResult(AppHttpCodeEnum.TOKEN_EXPIRED);
        }
    }
}