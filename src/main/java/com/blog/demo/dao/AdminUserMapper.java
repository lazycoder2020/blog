package com.blog.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.demo.bean.AdminUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminUserMapper extends BaseMapper<AdminUser> {
}
