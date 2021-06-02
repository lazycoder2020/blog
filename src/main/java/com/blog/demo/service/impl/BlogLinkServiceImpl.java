package com.blog.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.demo.bean.BlogLink;
import com.blog.demo.dao.BlogLinkMapper;
import com.blog.demo.service.BlogLinkeService;
import com.blog.demo.util.PageResult;
import com.blog.demo.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BlogLinkServiceImpl implements BlogLinkeService {

    @Autowired
    BlogLinkMapper linkMapper;

    @Override
    public Boolean saveLink(BlogLink blogLink) {

        return linkMapper.insert(blogLink) > 0;
    }

    @Override
    public PageResult getLinkPage(Long pageNumber, Long limit) {
        Page<BlogLink> page = new Page<>(pageNumber,limit);
        QueryWrapper<BlogLink> wrapper = new QueryWrapper<>();
        wrapper.eq("is_deleted",0);
        Page<BlogLink> list = linkMapper.selectPage(page,wrapper);

        PageResult pageResult = new PageResult();
        pageResult.setList(list.getRecords());
        pageResult.setPageSize(list.getPages());
        pageResult.setCurrPage(pageNumber);
        pageResult.setPageSize(limit);

        return pageResult;
    }

    @Override
    public BlogLink selectById(Integer id) {
        BlogLink link = linkMapper.selectById(id);
        return link;
    }

    @Override
    public Boolean updateLink(BlogLink blogLink) {

        return linkMapper.updateById(blogLink) > 0;
    }

    @Override
    public Boolean deleteBatch(Integer[] ids) {

        return linkMapper.deleteBatch(ids) > 0;
    }

    @Override
    public Map<Byte, List<BlogLink>> getLinkForLinkPage() {
        List<BlogLink> links = linkMapper.selectList(null);
        if (links.size() < 1) {
            return null;
        }
        Map<Byte, List<BlogLink>> mapLiks = new HashMap<>();
        for (BlogLink link : links){
            if (mapLiks.containsKey(link.getLinkType())){
                mapLiks.get(link.getLinkType()).add(link);
            }else {
                List<BlogLink> list = new ArrayList<>();
                list.add(link);
                mapLiks.put(link.getLinkType(), list);
            }
        }
        return mapLiks;
    }

    @Override
    public Integer getTotalLinks() {
        return linkMapper.getTotalLinks();
    }

}
