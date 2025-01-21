package com.auroral.controller;

import com.auroral.dto.AddSupplierDTO;
import com.auroral.dto.StockListDTO;
import com.auroral.dto.SupplierListDTO;
import com.auroral.dto.UpdateSupplierDTO;
import com.auroral.entity.ResponseResult;
import com.auroral.enums.AppHttpCodeEnum;
import com.auroral.service.SupplierService;
import com.auroral.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/supplier")
public class SupplierController {
    @Autowired
    private SupplierService supplierService;
    //获取供应商列表
    @PostMapping("/getSupplierList")
    public ResponseResult getSupplierList(@RequestHeader("Authorization") String authHeader, @RequestBody SupplierListDTO supplierListDTO){
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
            return supplierService.getSupplierList(supplierListDTO);
        } catch (Exception e) {
            return ResponseResult.errorResult(AppHttpCodeEnum.TOKEN_EXPIRED);
        }
    }
    //新增供应商
    @PutMapping("/addSupplier")
    public ResponseResult addSupplier(@RequestHeader("Authorization") String authHeader, @RequestBody AddSupplierDTO addSupplierDTO){
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
            return supplierService.addSupplier(addSupplierDTO);
        } catch (Exception e) {
            return ResponseResult.errorResult(AppHttpCodeEnum.TOKEN_EXPIRED);
        }
    }
    //更新供应商
    @PatchMapping("/updateSupplier")
    public ResponseResult updateSupplier(@RequestHeader("Authorization") String authHeader, @RequestBody UpdateSupplierDTO updateSupplierDTO){
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
            return supplierService.updateSupplier(updateSupplierDTO);
        } catch (Exception e) {
            return ResponseResult.errorResult(AppHttpCodeEnum.TOKEN_EXPIRED);
        }
    }
    //删除供应商
    @DeleteMapping("/deleteSupplier")
    public ResponseResult deleteSupplier(@RequestHeader("Authorization") String authHeader,@RequestParam("id") Long id){
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
            return supplierService.deleteSupplier(id);
        } catch (Exception e) {
            return ResponseResult.errorResult(AppHttpCodeEnum.TOKEN_EXPIRED);
        }
    }
}
