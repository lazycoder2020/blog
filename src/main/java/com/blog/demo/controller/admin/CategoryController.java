package com.blog.demo.controller.admin;

import com.blog.demo.bean.BlogCategory;
import com.blog.demo.service.BlogCategoryService;
import com.blog.demo.util.PageResult;
import com.blog.demo.util.Result;
import com.blog.demo.util.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class CategoryController {

    @Autowired
    BlogCategoryService categoryService;

    @GetMapping("/categories")
    public String categoryPage(HttpServletRequest request){
        request.setAttribute("path","categories");
        return "admin/category";
    }

    @GetMapping("/categories/list")
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params){
//        for(String key : params.keySet()){
//            System.out.println(key + " : " + params.get(key));
//        }
        Long pageNumber = Long.parseLong(params.get("page").toString());
        Long limit = Long.parseLong(params.get("limit").toString());
        PageResult pageResult = categoryService.getBlogCategoryPage(pageNumber, limit);

        return ResultGenerator.genSuccessResult(pageResult);
    }

    @PostMapping("/categories/save")
    @ResponseBody
    public Result save(BlogCategory category){
        System.out.println("category " + category);
        if (categoryService.saveCategory(category)){
            return ResultGenerator.genSuccessResult();
        }else {
            return ResultGenerator.genFailResult("Save Failed");
        }
    }

    @PostMapping("/categories/update")
    @ResponseBody
    public Result update(BlogCategory category){
        System.out.println("category " + category);
        if (categoryService.updateCategory(category)){
            return ResultGenerator.genSuccessResult();
        }else {
            return ResultGenerator.genFailResult("Update Failed");
        }
    }

    @PostMapping("/categories/delete")
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids){
        System.out.println("ids " + Arrays.toString(ids));

        if (ids.length < 1){
            return ResultGenerator.genFailResult("Invalid Request");
        }
        if(categoryService.deleteBath(ids)){
            return ResultGenerator.genSuccessResult();
        }else {
            return ResultGenerator.genFailResult("Delete Failed");
        }

    }




}
