package com.auroral.mapper;

import com.auroral.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


/**
 * 用户表(Users)表数据库访问层
 *
 * @author makejava
 * @since 2025-01-19 13:58:17
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}

