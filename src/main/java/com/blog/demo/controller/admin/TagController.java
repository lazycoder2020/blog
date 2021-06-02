package com.blog.demo.controller.admin;

import com.blog.demo.service.BlogTagService;
import com.blog.demo.util.PageResult;
import com.blog.demo.util.Result;
import com.blog.demo.util.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class TagController {

    @Autowired
    BlogTagService tagService;

    @GetMapping("/tags")
    public String tags(HttpServletRequest request){
        request.setAttribute("path", "tags");
        return "admin/tag";
    }

    @GetMapping("/tags/list")
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params){

        if (params.get("page") == null || params.get("limit") == null){
            return ResultGenerator.genFailResult("Invalid Request");
        }
        Long pageNumber = Long.parseLong(params.get("page").toString());
        Long limit = Long.parseLong(params.get("limit").toString());

        return ResultGenerator.genSuccessResult(tagService.getTagListByPage(pageNumber, limit));
    }

    @PostMapping("/tags/save")
    @ResponseBody
    public Result save(String tagName){
        System.out.println("tagName: " + tagName);
        if (tagName.isEmpty()){
            return ResultGenerator.genFailResult("Invalid Request");
        }
        if(tagService.saveTag(tagName)){
            return ResultGenerator.genSuccessResult();
        }else {
            return ResultGenerator.genFailResult("Save Failed");
        }
    }

    @PostMapping("/tags/delete")
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids){
        System.out.println("ids: " + ids);
        if (ids.length < 1){
            return ResultGenerator.genFailResult("Invalid Request");
        }
        if(tagService.deleteBatch(ids)){
            return ResultGenerator.genSuccessResult();
        }else {
            return ResultGenerator.genFailResult("Save Failed");
        }
    }

}
