package com.blog.demo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.demo.bean.BlogTag;
import com.blog.demo.bean.BlogTagCount;
import com.blog.demo.util.PageResult;

import java.util.List;

public interface BlogTagService extends IService<BlogTag> {
    Boolean saveTag(String tagName);
    BlogTag selectByTagName(String tagName);
    List<BlogTagCount> getBlogTagCountForIndex();
    int getTotalTags();
    PageResult getTagListByPage(Long pageNumber, Long limit);
    Boolean saveTage(String tagName);
    Boolean deleteBatch(Integer[] ids);
}
