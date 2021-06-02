package com.blog.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.demo.bean.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
