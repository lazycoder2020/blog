package com.blog.demo.service;

import com.blog.demo.bean.BlogComment;
import com.blog.demo.util.PageResult;

public interface BlogCommentService {
    int saveComment(BlogComment blogComment);
    int getTotalComments();
    PageResult getCommentsPage(long pageNumber);
    Boolean checkDone(Integer[] ids);
    Boolean deleteBatch(Integer[] ids);
    Boolean reply(long commentId, String replyBody);
    PageResult getCommentPageByBlogIdAndPageNum(Long blogId, int pageNum);
}
