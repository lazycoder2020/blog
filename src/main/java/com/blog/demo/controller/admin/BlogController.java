package com.blog.demo.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.demo.bean.Blog;
import com.blog.demo.config.Constants;
import com.blog.demo.controller.vo.BlogListVO;
import com.blog.demo.dao.BlogMapper;
import com.blog.demo.service.BlogCategoryService;
import com.blog.demo.service.BlogService;
import com.blog.demo.util.MyBlogUtils;
import com.blog.demo.util.PageResult;
import com.blog.demo.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.acl.LastOwnerException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Random;

@Controller
@RequestMapping("/admin")
public class BlogController {

    @Autowired
    BlogService blogService;

    @Autowired
    BlogCategoryService blogCategoryService;

    @Autowired
    BlogMapper blogMapper;

    @GetMapping("/blogs")
    public String list(HttpServletRequest request){
        request.setAttribute("path", "blogs");
        return "admin/blog";
    }

    @PostMapping("/blogs/md/uploadfile")
    public void uploadFileByEditormd(HttpServletRequest request,
                                     HttpServletResponse response,
                                     @RequestParam(name = "editormd-image-file", required = true)
                                             MultipartFile file) throws IOException, URISyntaxException {
        System.out.println("---------upload method is called-------------" + file.getOriginalFilename() );
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //生成文件名称通用方法
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Random r = new Random();
        StringBuilder tempName = new StringBuilder();
        tempName.append(sdf.format(new Date())).append(r.nextInt(100)).append(suffixName);
        String newFileName = tempName.toString();
        //创建文件
        File destFile = new File(Constants.FILE_UPLOAD_DIC + newFileName);
        String fileUrl = MyBlogUtils.getHost(new URI(request.getRequestURL() + "")) + "/upload/" + newFileName;
        File fileDirectory = new File(Constants.FILE_UPLOAD_DIC);
        try {
            if (!fileDirectory.exists()) {
                if (!fileDirectory.mkdir()) {
                    throw new IOException("文件夹创建失败,路径为：" + fileDirectory);
                }
            }
            file.transferTo(destFile);
            request.setCharacterEncoding("utf-8");
            response.setHeader("Content-Type", "text/html");
            response.getWriter().write("{\"success\": 1, \"message\":\"success\",\"url\":\"" + fileUrl + "\"}");
        } catch (UnsupportedEncodingException e) {
            response.getWriter().write("{\"success\":0}");
        } catch (IOException e) {
            response.getWriter().write("{\"success\":0}");
        }
    }

    @GetMapping("/blogs/list")
    @ResponseBody
    public Result<PageResult> list(@RequestParam Map<String, Object> params){
        System.out.println("blog lis is called");
        for (String key: params.keySet()){
            System.out.println(key + " : " + params.get(key));
        }

        Long pageNumber = Long.valueOf((String) params.get("page")) ;
        Long limit = Long.valueOf((String) params.get("limit")) ;
        Page<Blog> page = new Page<>(pageNumber, limit);
        QueryWrapper<Blog> wrapper = new QueryWrapper();
        wrapper.eq("is_deleted",0);
        Page<Blog> list = blogMapper.selectPage(page,wrapper);
//        System.out.println("list: " + list.getRecords());
        PageResult pageResult = new PageResult();
        pageResult.setPageSize(limit);
        pageResult.setTotalPage(list.getPages());
        pageResult.setList(list.getRecords());

        Result<PageResult> result = new Result<>(200,"success",pageResult);

        return result;
    }

    @GetMapping("/blogs/edit")
    public String edit(HttpServletRequest request){
        request.setAttribute("path", "edit");
        request.setAttribute("categories", blogCategoryService.getAllCategories());
        return "admin/edit";
    }

    @GetMapping("/blogs/edit/{blogId}")
    public String edit(HttpServletRequest request, @PathVariable long blogId){
        System.out.println("id is: " + blogId);
        request.setAttribute("path","edit");
        Blog blog = blogService.getBlogById(blogId);
        if (blog == null){
            return "error/error_400";
        }
        request.setAttribute("blog", blog);
        request.setAttribute("categories", blogCategoryService.getAllCategories());
        return "admin/edit";
    }


    @PostMapping("/blogs/save")
    @ResponseBody
    public Result save(@RequestParam Map<String, Object> params, Blog blog){
        for (String key: params.keySet()){
            System.out.println("key: " + key + " : " + params.get(key));
        }

        Result result = new Result();
        if ("success".equals(blogService.saveBlog(blog))){
            result.setResultCode(200);
        }else {
            result.setResultCode(400);
            result.setMessage("Submit Failed");
        }
        return  result;
    }

    @PostMapping("/blogs/delete")
    @ResponseBody
    public Result delete(@RequestParam Integer[] ids) {
        Result result = new Result();
        result.setResultCode(400);
        result.setMessage("Invalid Selection");
        if (ids.length < 1) {
            return result;
        }
        if (blogService.deleteBatch(ids) > 0){
            result.setResultCode(200);
            result.setMessage("Deleted");
        }
        return  result;
    }
}
