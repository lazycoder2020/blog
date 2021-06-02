package com.blog.demo.service;

import com.blog.demo.bean.Blog;
import com.blog.demo.controller.vo.BlogDetailVO;
import com.blog.demo.controller.vo.SimpleBlogListVO;
import com.blog.demo.util.PageResult;

import java.util.List;


public interface BlogService {
    String saveBlog(Blog blog);
    BlogDetailVO getBlogDetail(Long blogId);
    List<SimpleBlogListVO> getBlogListForIndexPage(String type);
    int getTotalBlogs();
    Blog getBlogById(long blogId);
    int deleteBatch(Integer[] ids);
    PageResult findBloglist(Integer pageNumber, Integer pageLimit);
    BlogDetailVO getBlogDetailByUrl(String subUrl);
    PageResult getBlogPageBySearch(String keyword, int pageNum);
    PageResult getBlogPageByCategory(String categoryName, int pageNum);
    PageResult getBlogPageByTag(String tagName, long pageNum);

}
