package com.blog.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.demo.bean.BlogTagRelation;
import com.blog.demo.dao.BlogTagRelationMapper;
import com.blog.demo.service.BlogTagRelationService;
import org.springframework.stereotype.Service;

@Service
public class BlogTagRelationServiceImpl extends ServiceImpl<BlogTagRelationMapper, BlogTagRelation> implements BlogTagRelationService {

}
