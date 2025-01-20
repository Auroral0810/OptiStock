package com.auroral.service;

import com.auroral.dto.UserChangePwdDTO;
import com.auroral.dto.UserLoginDTO;
import com.auroral.dto.UserUpdateDTO;
import com.auroral.entity.ResponseResult;
import com.auroral.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * 用户表(Users)表服务接口
 *
 * @author makejava
 * @since 2025-01-19 13:58:19
 */
public interface UserService extends IService<User> {

    ResponseResult register(String username, String nickname, String password);

    ResponseResult login(UserLoginDTO request);

    ResponseResult updateInfo(UserUpdateDTO request);

    ResponseResult changePassword( UserChangePwdDTO request);
}

