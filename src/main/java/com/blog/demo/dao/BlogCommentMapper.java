package com.blog.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.demo.bean.BlogComment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface BlogCommentMapper extends BaseMapper<BlogComment> {

    @Select("select count(*) from tb_blog_comment where is_deleted = 0")
    int getTotalComments();
    int checkDone(Integer[] ids);
    int deleteBatch(Integer[] ids);
}
