package com.blog.demo.controller.admin;

import com.blog.demo.service.BlogCommentService;
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
public class CommentController {

    @Autowired
    BlogCommentService commentService;

    @GetMapping("/comments/list")
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params){
//        for (String key: params.keySet()){
//            System.out.println(key + " : " + params.get(key));
//        }
//        Long pageNumber = Long.valueOf((String) params.get("page")) ;
        Long pageNumber = Long.parseLong(params.get("page").toString());
        PageResult pageResult = commentService.getCommentsPage(pageNumber);
        return ResultGenerator.genSuccessResult(pageResult);
    }

    @GetMapping("/comments")
    public String list(HttpServletRequest request){
        request.setAttribute("path","comments");
        return "admin/comment";
    }

    @PostMapping("/comments/checkDone")
    @ResponseBody
    public Result checkDone(@RequestBody Integer[] ids){
//        System.out.println("ids: " + Arrays.toString(ids));
        if (ids.length < 1){
            return ResultGenerator.genFailResult("Invalid Request");
        }
        if (commentService.checkDone(ids)){
            return ResultGenerator.genSuccessResult("");
        }else {
            return ResultGenerator.genFailResult("Review Failed");
        }
    }

    @PostMapping("/comments/delete")
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids){
        System.out.println("ids: " + Arrays.toString(ids));
        if (ids.length < 1){
            return ResultGenerator.genFailResult("Invalid Request");
        }
        if (commentService.deleteBatch(ids)){
            return ResultGenerator.genSuccessResult("");
        }else {
            return ResultGenerator.genFailResult("Delete Failed");
        }
    }

    @PostMapping("/comments/reply")
    @ResponseBody
    public Result reply(@RequestParam Long commentId,
                        @RequestParam String replyBody){

        if(commentId == null || commentId < 1 || replyBody.isEmpty()){
            return ResultGenerator.genFailResult("Invalid Request");
        }

        if (commentService.reply(commentId,replyBody)){
            return ResultGenerator.genSuccessResult();
        }else {
            return ResultGenerator.genFailResult("Reply Failed");
        }
    }
}
