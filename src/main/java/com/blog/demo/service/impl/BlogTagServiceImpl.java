package com.blog.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.demo.bean.BlogTag;
import com.blog.demo.bean.BlogTagCount;
import com.blog.demo.bean.BlogTagRelation;
import com.blog.demo.dao.BlogTagMapper;
import com.blog.demo.dao.BlogTagRelationMapper;
import com.blog.demo.service.BlogTagService;
import com.blog.demo.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class BlogTagServiceImpl extends ServiceImpl<BlogTagMapper, BlogTag> implements BlogTagService {

    @Autowired
    BlogTagMapper tagMapper;

    @Autowired
    BlogTagRelationMapper relationMapper;

    @Override
    public Boolean saveTag(String tagName) {

        QueryWrapper<BlogTag> wrapper = new QueryWrapper<>();
        wrapper.eq("tag_name",tagName).eq("is_deleted",0);
        List<BlogTag> re = tagMapper.selectList(wrapper);
        if (re.size() == 0){
            BlogTag tag = new BlogTag();
            tag.setTagName(tagName);
            return tagMapper.insert(tag) > 0;
        }

        return false;
    }

    @Override
    public BlogTag selectByTagName(String tagName) {
        QueryWrapper<BlogTag> wrapper = new QueryWrapper();
        wrapper.eq("tag_name",tagName);
        List<BlogTag> re = tagMapper.selectList(wrapper);
        if (re.size() > 0){
            return re.get(0);
        }else return null;
    }

    @Override
    public List<BlogTagCount> getBlogTagCountForIndex() {
        List<BlogTagCount> list = tagMapper.getBlogTagCountForIndex();
        return list;
    }

    @Override
    public int getTotalTags() {
        int total = tagMapper.getTotalTags();
        return total;
    }

    @Override
    public PageResult getTagListByPage(Long pageNumber, Long limit) {
        QueryWrapper<BlogTag> wrapper = new QueryWrapper<>();
        wrapper.eq("is_deleted", 0).orderByDesc("tag_id");
        Page<BlogTag> page = new Page<>(pageNumber, limit);
        Page<BlogTag> list = tagMapper.selectPage(page,wrapper);
        PageResult pageResult = new PageResult();
        pageResult.setList(list.getRecords());
        pageResult.setTotalPage(list.getPages());
        pageResult.setCurrPage(pageNumber);
        pageResult.setPageSize(limit);
        return pageResult;
    }

    @Override
    public Boolean saveTage(String tagName) {
        QueryWrapper<BlogTag> wrapper = new QueryWrapper<>();
        wrapper.eq("tag_name", tagName);
        BlogTag tag = tagMapper.selectOne(wrapper);
        if (tag == null){
            BlogTag newTag = new BlogTag();
            tag.setTagName(tagName);
            return tagMapper.insert(newTag) > 0;
        } else {
            return false;
        }
    }

    @Override
    public Boolean deleteBatch(Integer[] ids) {
        QueryWrapper<BlogTagRelation> wrapper = new QueryWrapper<>();
        wrapper.in("tag_id",ids);
        List<BlogTagRelation> list = relationMapper.selectList(wrapper);
        if (list.size() > 0){
            return false;
        }

        return tagMapper.deleteBatch(ids) > 0;
    }
}
