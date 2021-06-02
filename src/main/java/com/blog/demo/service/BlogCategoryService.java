package com.blog.demo.service;

import com.blog.demo.bean.BlogCategory;
import com.blog.demo.util.PageResult;

import java.util.List;

public interface BlogCategoryService {
    List<BlogCategory> getAllCategories();
    int getTotalCategories();
    PageResult getBlogCategoryPage(Long pageNumber, Long limit);
    Boolean saveCategory(BlogCategory category);
    Boolean updateCategory(BlogCategory category);
    boolean deleteBath(Integer[] ids);
}
