package com.blog.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.demo.bean.Blog;
import com.blog.demo.controller.vo.BlogListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
public interface BlogMapper extends BaseMapper<Blog> {


    @Select("Select tb_blog.blog_id, tb_blog.blog_title, tb_blog.blog_sub_url,"+
            "tb_blog.blog_cover_image,tb_blog.blog_category_id,tb_blog.blog_category_name, " +
            "tb_blog.create_time, tb_blog_category.category_icon as blog_category_icon from tb_blog, tb_blog_category " +
            "WHERE tb_blog.blog_category_id = tb_blog_category.category_id  and tb_blog.is_deleted=0 order by tb_blog.create_time desc")
    Page<BlogListVO> findBloglist(Page<Blog> page);

    @Select("select count(*) from tb_blog where is_deleted = 0")
    int getTotalBlogs();

    int deleteBatch(Integer[] ids);

    Page<BlogListVO> selectByTagId(Page<Blog> page, long tagId);
}
