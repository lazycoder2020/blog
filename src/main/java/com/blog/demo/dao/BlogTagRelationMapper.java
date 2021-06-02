package com.blog.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.demo.bean.BlogTagRelation;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BlogTagRelationMapper extends BaseMapper<BlogTagRelation> {
}
