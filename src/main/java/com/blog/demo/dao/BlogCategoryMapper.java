package com.blog.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.demo.bean.BlogCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface BlogCategoryMapper extends BaseMapper<BlogCategory> {
    @Select("select count(*) from tb_blog_category where is_deleted = 0")
    int getTotalCategories();

    int deleteBatch(Integer[] ids);
}
