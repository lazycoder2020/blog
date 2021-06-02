package com.blog.demo.service;

import com.blog.demo.bean.BlogLink;
import com.blog.demo.util.PageResult;
import com.blog.demo.util.Result;

import java.util.List;
import java.util.Map;

public interface BlogLinkeService {
    Boolean saveLink(BlogLink blogLink);
    PageResult getLinkPage(Long pageNumber, Long limit);
    BlogLink selectById(Integer id);
    Boolean updateLink(BlogLink blogLink);
    Boolean deleteBatch(Integer[] ids);
    Map<Byte, List<BlogLink>> getLinkForLinkPage();
    Integer getTotalLinks();
}
