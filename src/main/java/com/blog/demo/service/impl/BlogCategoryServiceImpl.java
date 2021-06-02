package com.blog.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.demo.bean.BlogCategory;
import com.blog.demo.dao.BlogCategoryMapper;
import com.blog.demo.service.BlogCategoryService;
import com.blog.demo.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogCategoryServiceImpl implements BlogCategoryService {

    @Autowired
    BlogCategoryMapper blogCategoryMapper;

    @Override
    public List<BlogCategory> getAllCategories() {
        return blogCategoryMapper.selectList(null);
    }


    @Override
    public int getTotalCategories() {

        int total = blogCategoryMapper.getTotalCategories();
        return total;
    }

    @Override
    public PageResult getBlogCategoryPage(Long pageNumber, Long limit) {

        Page<BlogCategory> page = new Page<>(pageNumber, limit);
        QueryWrapper<BlogCategory> wrapper = new QueryWrapper();
        wrapper.eq("is_deleted", 0);
        Page<BlogCategory> list = blogCategoryMapper.selectPage(page, wrapper);
        PageResult pageResult = new PageResult();
        pageResult.setList(list.getRecords());
        pageResult.setTotalPage(list.getPages());
        pageResult.setCurrPage(pageNumber);
        pageResult.setPageSize(limit);

        return pageResult;
    }

    @Override
    public Boolean saveCategory(BlogCategory category) {

        return blogCategoryMapper.insert(category) > 0;
    }

    @Override
    public Boolean updateCategory(BlogCategory category) {
        BlogCategory categoryForUpdate = blogCategoryMapper.selectById(category.getCategoryId());
        if (categoryForUpdate != null){
            categoryForUpdate.setCategoryIcon(category.getCategoryIcon());
            categoryForUpdate.setCategoryName(category.getCategoryName());
            return blogCategoryMapper.updateById(categoryForUpdate) > 0;
        }else {
            return false;
        }
    }

    @Override
    public boolean deleteBath(Integer[] ids) {

        return blogCategoryMapper.deleteBatch(ids) > 0;
    }

}
