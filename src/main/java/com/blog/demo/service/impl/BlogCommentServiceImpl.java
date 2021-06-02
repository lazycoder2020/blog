package com.blog.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.demo.bean.BlogComment;
import com.blog.demo.dao.BlogCommentMapper;
import com.blog.demo.service.BlogCommentService;
import com.blog.demo.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BlogCommentServiceImpl implements BlogCommentService {

    @Autowired
    BlogCommentMapper commentMapper;

    @Override
    public int saveComment(BlogComment blogComment) {
        return commentMapper.insert(blogComment);
    }

    @Override
    public int getTotalComments() {

        int total = commentMapper.getTotalComments();
        return total;
    }

    @Override
    public PageResult getCommentsPage(long pageNumber) {
        Page<BlogComment> page = new Page<>(pageNumber,10);
        QueryWrapper<BlogComment> wrapper = new QueryWrapper<>();
        wrapper.eq("is_deleted",0);
        Page<BlogComment> list = commentMapper.selectPage(page, wrapper);
        PageResult pageResult = new PageResult(list.getRecords(), list.getSize(), list.getPages(), pageNumber);

        return pageResult;
    }

    @Override
    public Boolean checkDone(Integer[] ids) {
        if(commentMapper.checkDone(ids) > 0){
            return true;
        }else return false;
    }

    @Override
    public Boolean deleteBatch(Integer[] ids) {
        if(commentMapper.deleteBatch(ids) > 0){
            return true;
        }else return false;
    }

    @Override
    public Boolean reply(long commentId, String replyBody) {
        BlogComment comment = commentMapper.selectById(commentId);
        if(comment != null && comment.getCommentStatus() != 0){
            comment.setReplyBody(replyBody);
            comment.setReplyCreateTime(new Date());
            return commentMapper.updateById(comment) > 0;
        }

        return false;
    }

    @Override
    public PageResult getCommentPageByBlogIdAndPageNum(Long blogId, int pageNum) {
        Page<BlogComment> page = new Page<>(pageNum,8);
        QueryWrapper<BlogComment> wrapper = new QueryWrapper<>();
        wrapper.eq("is_deleted",0).eq("blog_id",blogId).eq("comment_status",1);
        Page<BlogComment> list = commentMapper.selectPage(page, wrapper);
        PageResult pageResult = new PageResult(list.getRecords(), list.getSize(), list.getPages(), pageNum);
        return pageResult;
    }
}
