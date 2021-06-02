package com.blog.demo;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.demo.bean.*;
import com.blog.demo.controller.vo.BlogListVO;
import com.blog.demo.controller.vo.SimpleBlogListVO;
import com.blog.demo.dao.*;
import com.blog.demo.service.TestInterface;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.DigestUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BlogApplication.class)
class BlogApplicationTests {


    @Autowired
    UserMapper userMapper;
    @Autowired
    JDBCTestMapper jdbcTestMapper;

    @Autowired
    BlogMapper blogMapper;

    @Autowired
    AdminUserMapper adminUserMapper;

    @Autowired
    BlogTagMapper tagMapper;

    @Autowired
    BlogCategoryMapper categoryMapper;

    @Autowired
    TestInterface testInterface;

    @Autowired
    BlogTagMapper blogTagMapper;

    @Autowired
    BlogConfigMapper configMapper;

    @Test
    public void configTest(){

        QueryWrapper<BlogConfig> wrapper = new QueryWrapper<>();
        wrapper.eq("config_name","websiteName");
        BlogConfig config = configMapper.selectOne(wrapper);
        System.out.println(config);
        config.setConfigValue("john's home");
        configMapper.updateById(config);
    }


    @Test
    public void tagTest(){
        List<BlogTagCount> list = tagMapper.getBlogTagCountForIndex();
        System.out.println("list: " + list);
    }


    @Test
    public void testPage(){
        Page<Blog> page = new Page<>(1,4);
        Page<BlogListVO> list = blogMapper.findBloglist(page);
        System.out.println("list: " + list.getRecords());
        System.out.println("total: " + list.getTotal());
        System.out.println("pages: " + list.getPages());

    }

    @Test
    public void categoryMapperTest(){
        BlogCategory category = categoryMapper.selectById(20);
        System.out.println(category);
    }

    @Test
    public void tagMapperTest(){
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("tag_name","soccer");
//        List<BlogTag> re = tagMapper.selectByMap(map);
//        System.out.println("re: " + re.get(0));

//        QueryWrapper<BlogTag> queryWrapper = new QueryWrapper();
//        queryWrapper.eq("tag_name","soccer").eq("is_deleted",0);
//        List<BlogTag> re = tagMapper.selectList(queryWrapper);
//        System.out.println("re: " + re.size());

        BlogTag tag = new BlogTag();
        tag.setTagName("baseball");
        int re = tagMapper.insert(tag);
        System.out.println(re);
        System.out.println(tag.getTagId());



    }



    @Test
    void contextLoads() {
    }

    @Test
    public void testUser(){

        List<User> user = userMapper.selectList(null);
        System.out.println("user: " + user );
        log.println("user: " + user);
    }

    @Test
    public void jdbcTestInsert(){
        JDBCTest test = new JDBCTest();
        test.setTest("test three");
        int result = jdbcTestMapper.insert(test);
        System.out.println("result is: " + test.getId());

    }

    @Test
    public void jdbcTestSelect(){
        JDBCTest test = jdbcTestMapper.selectById(2);
        System.out.println("result is: " + test);
    }

    @Test
    public void blogTest(){
        QueryWrapper<Blog> wrapper = new QueryWrapper<>();
        wrapper.select("blog_id","blog_title").orderByDesc("create_time").last("limit 3");
        List<Blog> blog = blogMapper.selectList(wrapper);
        System.out.println("blog: " + blog);
    }

    @Test
    public void userTest(){
        AdminUser user = adminUserMapper.selectById(1);
        System.out.println("user: " + user);
    }

    @Test
    public void infaceTest(){
        testInterface.myName();

    }

    @Test
    public void testJava(){
//        Integer[] arr = {1, 2, 3};
//        List<Integer> list = Arrays.asList(arr);
//        String str = list.toString();
//        System.out.println("str: " + str.substring(1, str.length() - 1));
//        HashMap<String,Integer> map = new HashMap<>();
//        map.put("one",1);
//        map.put("one2",2);
//        map.put("one3",3);
//        System.out.println(map.values());
        Integer[] arr = {5, 6, 7};
        QueryWrapper<Blog> wrapper = new QueryWrapper<>();
        wrapper.select("blog_id").in("blog_id",arr);
        List<Blog> list = blogMapper.selectList(wrapper);
        System.out.println("list: " + list);
    }

    @Test
    public void md5Test(){
        String str = "blog123";
        String md5 = "";
        try {
            md5 = DigestUtils.md5DigestAsHex(str.getBytes("utf-8"));

        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("md5: " + md5);
    }

}
