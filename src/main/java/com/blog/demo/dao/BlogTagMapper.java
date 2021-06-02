package com.blog.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.demo.bean.BlogTag;
import com.blog.demo.bean.BlogTagCount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BlogTagMapper extends BaseMapper<BlogTag> {
    @Select("SELECT a.tag_count , tb_blog_tag.tag_name FROM" +
            "(SELECT COUNT(tb_blog_tag_relation.tag_id) AS tag_count, tb_blog_tag_relation.tag_id FROM tb_blog_tag_relation, tb_blog " +
            "WHERE tb_blog_tag_relation.blog_id = tb_blog.blog_id AND tb_blog.is_deleted = 0 " +
            "GROUP BY tb_blog_tag_relation.tag_id order BY COUNT(tb_blog_tag_relation.tag_id) DESC) AS a, tb_blog_tag " +
            "WHERE a.tag_id = tb_blog_tag.tag_id ")
    List<BlogTagCount> getBlogTagCountForIndex();

    @Select("SELECT COUNT(*) FROM tb_blog_tag WHERE is_deleted = 0")
    int getTotalTags();
    int deleteBatch(Integer[] ids);
}
