package com.blog.demo.controller.admin;

import com.blog.demo.bean.BlogLink;
import com.blog.demo.service.BlogLinkeService;
import com.blog.demo.util.Result;
import com.blog.demo.util.ResultGenerator;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.swing.plaf.PanelUI;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class LinkController {

    @Autowired
    BlogLinkeService linkeService;

    @GetMapping("/links")
    public String blogLink(HttpServletRequest request){
        request.setAttribute("path", "links");
        return "admin/link";
    }

    @GetMapping("/links/list")
    @ResponseBody
    public Result list(@RequestParam Map<String,Object> params ){
        for(String key : params.keySet()){
            System.out.println(key + " : " + params.get(key));
        }

        Long pageNumber = Long.parseLong(params.get("page").toString());
        Long limit = Long.parseLong(params.get("limit").toString());

        return ResultGenerator.genSuccessResult(linkeService.getLinkPage(pageNumber,limit));
    }

    @GetMapping("/links/info/{id}")
    @ResponseBody
    public Result info(@PathVariable Integer id){

        return ResultGenerator.genSuccessResult(linkeService.selectById(id));
    }

    @PostMapping("/links/save")
    @ResponseBody
    public Result save(BlogLink blogLink){
        System.out.println("link: " + blogLink);
        if (blogLink == null || blogLink.getLinkName().isEmpty()
                             || blogLink.getLinkUrl().isEmpty()
                             || blogLink.getLinkDescription().isEmpty()){
            return ResultGenerator.genFailResult("Invalid Request");
        }
        if (linkeService.saveLink(blogLink)){
            return ResultGenerator.genSuccessResult();
        }else {
            return ResultGenerator.genFailResult("Save Failed");
        }
    }

    @PostMapping("/links/update")
    @ResponseBody
    public Result update(BlogLink blogLink){
        System.out.println("link: " + blogLink);
        if (blogLink == null || blogLink.getLinkName().isEmpty()
                || blogLink.getLinkUrl().isEmpty()
                || blogLink.getLinkDescription().isEmpty()){
            return ResultGenerator.genFailResult("Invalid Request");
        }
        if (linkeService.updateLink(blogLink)){
            return ResultGenerator.genSuccessResult();
        }else {
            return ResultGenerator.genFailResult("Save Failed");
        }
    }

    @PostMapping("/links/delete")
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids){
        System.out.println("ids: " + ids);
        if (ids.length < 1){
            return ResultGenerator.genFailResult("Invalid Request");
        }
        if(linkeService.deleteBatch(ids)){
            return ResultGenerator.genSuccessResult();
        }else {
            return ResultGenerator.genFailResult("Save Failed");
        }
    }

}
