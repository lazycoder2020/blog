package com.blog.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.demo.bean.Blog;
import com.blog.demo.bean.BlogCategory;
import com.blog.demo.bean.BlogTag;
import com.blog.demo.bean.BlogTagRelation;
import com.blog.demo.controller.vo.BlogDetailVO;
import com.blog.demo.controller.vo.BlogListVO;
import com.blog.demo.controller.vo.SimpleBlogListVO;
import com.blog.demo.dao.BlogCategoryMapper;
import com.blog.demo.dao.BlogMapper;
import com.blog.demo.dao.BlogTagMapper;
import com.blog.demo.service.BlogService;
import com.blog.demo.service.BlogTagRelationService;
import com.blog.demo.service.BlogTagService;
import com.blog.demo.util.MarkDownUtil;
import com.blog.demo.util.PageResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    BlogMapper blogMapper;

    @Autowired
    BlogCategoryMapper categoryMapper;

    @Autowired
    BlogTagService tagService;

    @Autowired
    BlogTagMapper tagMapper;

    @Autowired
    BlogTagRelationService relationService;

    @Override
    @Transactional
    public String saveBlog(Blog blog) {
        System.out.println("blog is " + blog);

        BlogCategory category = categoryMapper.selectById(blog.getBlogCategoryId());
        if (category == null) {
            blog.setBlogCategoryId(0);
            blog.setBlogCategoryName("Default Category");
        } else {
            blog.setBlogCategoryName(category.getCategoryName());
            category.setCategoryRank(category.getCategoryRank() + 1);
        }

        String[] tags = blog.getBlogTags().split(",");
        if (tags.length > 6) {
            return "Max tags limit: 6";
        }

        int res;
        if (blog.getBlogId() !=null && blogMapper.selectById(blog.getBlogId()) !=null){
            res = blogMapper.updateById(blog);
        }else {
            res = blogMapper.insert(blog);
        }

        if (res > 0) {

            ArrayList<BlogTag> tagList = new ArrayList<>();
            ArrayList<BlogTag> tagListForInsert = new ArrayList<>();
            for (String tagsName : tags) {
                BlogTag result = tagService.selectByTagName(tagsName);
                if (result == null) {
                    BlogTag tempTag = new BlogTag();
                    tempTag.setTagName(tagsName);
                    tagListForInsert.add(tempTag);
                } else {
                    tagList.add(result);
                }
            }

            if (tagListForInsert.size() > 0) {
                tagService.saveBatch(tagListForInsert);
            }
            tagList.addAll(tagListForInsert);

            categoryMapper.updateById(category);

            List<BlogTagRelation> relations = new ArrayList<>();

            for (BlogTag bTag : tagList) {
                BlogTagRelation relation = new BlogTagRelation();
                relation.setBlogId(blog.getBlogId());
                relation.setTagId(bTag.getTagId());
                relations.add(relation);
            }

            if (relationService.saveBatch(relations)) {
                return "success";
            }
        }
        return "fail";
    }

    @Override
    public BlogDetailVO getBlogDetail(Long blogId) {
        Blog blog = blogMapper.selectById(blogId);
        if (blog != null && blog.getIsDeleted() != 1){
            blog.setBlogViews(blog.getBlogViews() + 1);
            blogMapper.updateById(blog);
            BlogDetailVO detailVO = new BlogDetailVO();
            BeanUtils.copyProperties(blog, detailVO);
            detailVO.setBlogTags(blog.getBlogTags().split(","));
            detailVO.setBlogContent(MarkDownUtil.mdToHtml(blog.getBlogContent()));
            return detailVO;
        }

        return null;
    }

    @Override
    public List<SimpleBlogListVO> getBlogListForIndexPage(String type) {
        QueryWrapper<Blog> wrapper = new QueryWrapper<>();
        wrapper.select("blog_id","blog_title").orderByDesc(type).last("limit 3");
        List<Blog> blogs = blogMapper.selectList(wrapper);
        List<SimpleBlogListVO> listVOS = new ArrayList<>();

        if (!blogs.isEmpty()){
            for(Blog blog: blogs){
                SimpleBlogListVO simpleBlogListVO = new SimpleBlogListVO();
                BeanUtils.copyProperties(blog, simpleBlogListVO);
                listVOS.add(simpleBlogListVO);
            }
        }
        return listVOS;
    }

    @Override
    public int getTotalBlogs() {
        int total = blogMapper.getTotalBlogs();
        return total;
    }

    @Override
    public Blog getBlogById(long blogId) {
        Blog blog = blogMapper.selectById(blogId);
        return blog;
    }

    @Override
    public int deleteBatch(Integer[] ids) {

        System.out.println("ids " + Arrays.toString(ids));
        int result = blogMapper.deleteBatch(ids);
        return result;
    }

    @Override
    public PageResult findBloglist(Integer pageNumber, Integer pageLimit) {
        Page<Blog> page = new Page<>(pageNumber, pageLimit);
        Page<BlogListVO> list = blogMapper.findBloglist(page);
        PageResult result = new PageResult();
        result.setList(list.getRecords());
        result.setCurrPage(pageNumber);
        result.setPageSize(pageLimit);
        result.setTotalPage(page.getPages());
        return result;
    }

    @Override
    public BlogDetailVO getBlogDetailByUrl(String subUrl) {
        QueryWrapper<Blog> wrapper = new QueryWrapper<>();
        wrapper.eq("blog_sub_url", subUrl);
        Blog blog = blogMapper.selectOne(wrapper);

        BlogDetailVO detailVO = new BlogDetailVO();
        BeanUtils.copyProperties(blog, detailVO);
        detailVO.setBlogTags(blog.getBlogTags().split(","));
        detailVO.setBlogContent(MarkDownUtil.mdToHtml(blog.getBlogContent()));
        return detailVO;
    }

    @Override
    public PageResult getBlogPageBySearch(String keyword, int pageNum) {
        Page<Blog> page = new Page<>(pageNum, 10);
        QueryWrapper<Blog> wrapper = new QueryWrapper<>();
        wrapper.eq("is_deleted",0).like("blog_title",keyword).or().like("blog_category_name",keyword);
        Page<Blog> list = blogMapper.selectPage(page, wrapper);
        List<BlogListVO> listVOS = new ArrayList<>();
        if (list.getSize() > 0){
            for (Blog blog : list.getRecords()){
                BlogListVO vo = new BlogListVO();
                BeanUtils.copyProperties(blog, vo);
                listVOS.add(vo);
            }
        }
        PageResult result = new PageResult();
        result.setList(listVOS);
        result.setTotalPage(list.getPages());
        result.setCurrPage(pageNum);
        result.setPageSize(10);
        return result;
    }

    @Override
    public PageResult getBlogPageByCategory(String categoryName, int pageNum) {
        Page<Blog> page = new Page<>(pageNum, 10);
        QueryWrapper<Blog> wrapper = new QueryWrapper<>();
        wrapper.eq("is_deleted",0).eq("blog_category_name",categoryName);
        Page<Blog> list = blogMapper.selectPage(page, wrapper);
        List<BlogListVO> listVOS = new ArrayList<>();
        if (list.getSize() > 0){
            for (Blog blog : list.getRecords()){
                BlogListVO vo = new BlogListVO();
                BeanUtils.copyProperties(blog, vo);
                listVOS.add(vo);
            }
        }
        PageResult result = new PageResult();
        result.setList(listVOS);
        result.setTotalPage(list.getPages());
        result.setCurrPage(pageNum);
        result.setPageSize(10);
        return result;

    }

    @Override
    public PageResult getBlogPageByTag(String tagName, long pageNumber) {
        Page<Blog> page = new Page<>(pageNumber, 10);

        BlogTag tag =  tagService.selectByTagName(tagName);
        if (tag == null){
            return null;
        }

        Page<BlogListVO> list = blogMapper.selectByTagId(page, tag.getTagId());

        PageResult result = new PageResult();
        result.setList(list.getRecords());
        result.setCurrPage(pageNumber);
        result.setPageSize(10);
        result.setTotalPage(page.getPages());
        return result;
    }


}
