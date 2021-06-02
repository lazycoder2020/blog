package com.blog.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.demo.bean.BlogConfig;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BlogConfigMapper extends BaseMapper<BlogConfig> {
}
