package com.auroral.service.impl;

import com.auroral.dto.UserChangePwdDTO;
import com.auroral.dto.UserLoginDTO;
import com.auroral.dto.UserUpdateDTO;
import com.auroral.entity.ResponseResult;
import com.auroral.entity.User;
import com.auroral.utils.RedisCache;
import com.auroral.vo.UserLoginVo;
import com.auroral.vo.UserInfoVo;
import com.auroral.enums.AppHttpCodeEnum;
import com.auroral.mapper.UserMapper;
import com.auroral.service.UserService;
import com.auroral.utils.BeanCopyUtils;
import com.auroral.utils.JwtUtil;
import com.auroral.utils.Md5Util;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;


/**
 * 用户表(Users)表服务实现类
 *
 * @author makejava
 * @since 2025-01-19 13:58:20
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisCache redisCache;

    @Override
    @Transactional
    public ResponseResult register(String username, String nickname, String password){
        //判断是否存在这个用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        User u = userMapper.selectOne(queryWrapper);
        //用户存在
        if (!Objects.isNull(u)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.USERNAME_EXIST);
        }
        //用户不存在，进行注册
        User user = new User();
        user.setUsername(username);
        user.setNickname(nickname);
        //加密密码
        String md5String = Md5Util.getMD5String(password);
        user.setPasswordHash(md5String);
        userMapper.insert(user);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult login(UserLoginDTO request) {

        String username = request.getUsername();
        String password = request.getPassword();
        //根据用户名查询用户信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        queryWrapper.eq(User::getStatus, 1);//只查询激活的用户
        User user = userMapper.selectOne(queryWrapper);

        //用户不存在或者密码错误就返回错误信息
        if (Objects.isNull(user)||!Md5Util.getMD5String(password).equals(user.getPasswordHash())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR);
        }

        //密码正确就生成token
        String userId = user.getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        //返回用户信息和token
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        //把用户信息存入redis
        redisCache.setCacheObject("login:"+userId,userInfoVo);
        return ResponseResult.okResult(new UserLoginVo(jwt,userInfoVo));
    }

    @Override
    @Transactional
    public ResponseResult updateInfo(UserUpdateDTO request) {
        //已经通过了controller层的登录验证，所以这里不需要再验证token了
        User user = BeanCopyUtils.copyBean(request, User.class);
        //更新用户信息
        userMapper.updateById(user);
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult changePassword(UserChangePwdDTO request) {
        //已经通过了controller层的登录验证，所以这里不需要再验证token了
        //先验证旧密码是否正确,根据id查询用户信息
        Long id = request.getId();
        User user = userMapper.selectById(id);
        if (Objects.isNull(user) ||!Md5Util.getMD5String(request.getOldPassword()).equals(user.getPasswordHash())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.OLD_PASSWORD_ERROR);
        }
        //加密密码
        String md5String = Md5Util.getMD5String(request.getNewPassword());
        //使用update查找id等于request.getId()的用户，并更新密码
        user.setPasswordHash(md5String);
        userMapper.updateById(user);
        return ResponseResult.okResult();
    }
}
