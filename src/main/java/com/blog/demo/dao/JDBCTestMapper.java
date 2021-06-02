package com.blog.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.demo.bean.JDBCTest;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface JDBCTestMapper extends BaseMapper<JDBCTest> {

}
