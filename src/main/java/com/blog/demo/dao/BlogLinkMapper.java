package com.blog.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.demo.bean.BlogLink;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface BlogLinkMapper extends BaseMapper<BlogLink> {
    int deleteBatch(Integer[] ids);

    @Select("select count(*) from tb_link where is_deleted = 0")
    int getTotalLinks();
}
