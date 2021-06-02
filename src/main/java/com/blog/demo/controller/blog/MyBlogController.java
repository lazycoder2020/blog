package com.blog.demo.controller.blog;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.demo.bean.Blog;
import com.blog.demo.bean.BlogComment;
import com.blog.demo.bean.BlogLink;
import com.blog.demo.controller.vo.BlogDetailVO;
import com.blog.demo.controller.vo.BlogListVO;
import com.blog.demo.dao.BlogMapper;
import com.blog.demo.service.*;
import com.blog.demo.util.PageResult;
import com.blog.demo.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;


@Controller
public class MyBlogController {
    private Integer pageLimit = 6;

    @Autowired
    private BlogService blogService;

    @Autowired
    private BlogTagService tagService;

    @Autowired
    private BlogCommentService commentService;

    @Autowired
    private BlogConfigService configService;

    @Autowired
    private BlogLinkeService linkeService;

    @GetMapping({"/","/index", "/index.html"})
    public String index(HttpServletRequest request){
        return this.page(request,1);
    }

    @GetMapping("/page/{pageNum}")
    public String page(HttpServletRequest request, @PathVariable("pageNum") int pageNum){
        PageResult result = blogService.findBloglist(pageNum, pageLimit);
        if (result == null){
            return "error/error_404";
        }
        request.setAttribute("blogPageResult",result);
        request.setAttribute("newBlogs", blogService.getBlogListForIndexPage("create_time"));
        request.setAttribute("hotBlogs", blogService.getBlogListForIndexPage("blog_views"));
        request.setAttribute("hotTags", tagService.getBlogTagCountForIndex());
        request.setAttribute("pageName", "HomePage");
        request.setAttribute("configurations",configService.getAllConfigs());
        return "blog/amaze/index";
    }

    @GetMapping("/blog/{blogId}")
    public String detail(HttpServletRequest request,
                         @PathVariable("blogId") Long blogId,
                         @RequestParam(value = "commentPage", required = false, defaultValue = "1") Integer commentPage){

        BlogDetailVO detailVO = blogService.getBlogDetail(blogId);
        if (detailVO != null){
            request.setAttribute("commentPageResult", commentService.getCommentPageByBlogIdAndPageNum(blogId, commentPage));
            request.setAttribute("blogDetailVO", detailVO);
        }
        request.setAttribute("pageName","Article");
        request.setAttribute("configurations",configService.getAllConfigs());
        return "blog/amaze/detail";
    }

    @PostMapping("/blog/comment")
    @ResponseBody
    public Result comment(HttpServletRequest request, HttpSession session,
                          @RequestParam Long blogId, @RequestParam String verifyCode,
                          @RequestParam String commentator, @RequestParam String email,
                          @RequestParam String websiteUrl, @RequestParam String commentBody){

        Result result = new Result();
        if (verifyCode.isEmpty()) {
            result.setMessage("Verify code Required");
        }

        String kaptchaCode = session.getAttribute("verifyCode") + "";
        if (kaptchaCode.isEmpty()){
            result.setMessage("Invalid Request");
        }

        if (!verifyCode.equals(kaptchaCode)){
            result.setMessage("Verify code Error");
        }

        if (request.getHeader("Referer").isEmpty()){
            result.setMessage("Invalid Request");
        }

        if (blogId == null || blogId < 0){
            result.setMessage("Invalid Request");
        }

        if (commentator.isEmpty()){
            result.setMessage("Please Leave Your Name");
        }

        if (email.isEmpty()){
            result.setMessage("Please Leave Your email");
        }

        if (commentBody.isEmpty()){
            result.setMessage("Please Leave Your comment");
        }

        if (commentBody.trim().length() > 200){

        }

        if (result.getMessage() != null){
            result.setResultCode(400);
            return result;
        }

        BlogComment blogComment = new BlogComment();
        blogComment.setBlogId(blogId);
        blogComment.setCommentator(commentator);
        blogComment.setEmail(email);
        blogComment.setWebsiteUrl(websiteUrl);
        blogComment.setCommentBody(commentBody.trim());
        if(commentService.saveComment(blogComment) > 0){
            result.setResultCode(200);
        }else {
            result.setMessage("Submit Failed");
            result.setResultCode(400);
        }
        return result;
    }

    @GetMapping("/search/{keyword}")
    public String search(HttpServletRequest request, @PathVariable("keyword") String keyword){
        return search(request, keyword, 1);
    }

    @GetMapping("/search/{keyword}/{page}")
    public String search(HttpServletRequest request, @PathVariable("keyword") String keyword, @PathVariable("page") Integer page){
        PageResult result = blogService.getBlogPageBySearch(keyword,page);
        request.setAttribute("blogPageResult",result);
        request.setAttribute("pageName","SEARCH");
        request.setAttribute("pageUrl","search");
        request.setAttribute("keyword",keyword);
        request.setAttribute("newBlogs", blogService.getBlogListForIndexPage("create_time"));
        request.setAttribute("hotBlogs", blogService.getBlogListForIndexPage("blog_views"));
        request.setAttribute("hotTags", tagService.getBlogTagCountForIndex());
        request.setAttribute("configurations",configService.getAllConfigs());
        return "blog/amaze/list";
    }

    @GetMapping("/tag/{tagName}")
    public String tag(HttpServletRequest request, @PathVariable("tagName") String tagName){
        return tag(request, tagName, 1);
    }

    @GetMapping("/tag/{tagName}/{page}")
    public String tag(HttpServletRequest request, @PathVariable("tagName") String tagName, @PathVariable("page") long pageNum){
        PageResult result = blogService.getBlogPageByTag(tagName,pageNum);
        request.setAttribute("blogPageResult", result);
        request.setAttribute("pageName","Tag");
        request.setAttribute("pageUrl","tag");
        request.setAttribute("keyword", tagName);
        request.setAttribute("newBlogs", blogService.getBlogListForIndexPage("create_time"));
        request.setAttribute("hotBlogs", blogService.getBlogListForIndexPage("blog_views"));
        request.setAttribute("hotTags", tagService.getBlogTagCountForIndex());
        request.setAttribute("configurations",configService.getAllConfigs());
        return "blog/amaze/list";
    }

    @GetMapping("/category/{categoryName}")
    public String category(HttpServletRequest request, @PathVariable("categoryName") String categoryName){
        return category(request, categoryName, 1);
    }

    @GetMapping("/category/{categoryName}/{page}")
    public String category(HttpServletRequest request, @PathVariable("categoryName") String categoryName, @PathVariable("page") Integer pageNum){
        PageResult result = blogService.getBlogPageByCategory(categoryName,pageNum);
        request.setAttribute("blogPageResult", result);
        request.setAttribute("pageName","Category");
        request.setAttribute("pageUrl","category");
        request.setAttribute("keyword", categoryName);
        request.setAttribute("newBlogs", blogService.getBlogListForIndexPage("create_time"));
        request.setAttribute("hotBlogs", blogService.getBlogListForIndexPage("blog_views"));
        request.setAttribute("hotTags", tagService.getBlogTagCountForIndex());
        request.setAttribute("configurations",configService.getAllConfigs());
        return "blog/amaze/list";
    }

    @GetMapping("/link")
    public String link(HttpServletRequest request){
        Map<Byte, List<BlogLink>> linkMap = linkeService.getLinkForLinkPage();
        if (linkMap != null) {
            //Type of outside links 0-friend link 1-recommend 2-personal
            if (linkMap.containsKey((byte) 0)) {
                request.setAttribute("favoriteLinks", linkMap.get((byte) 0));
            }
            if (linkMap.containsKey((byte) 1)) {
                request.setAttribute("recommendLinks", linkMap.get((byte) 1));
            }
            if (linkMap.containsKey((byte) 2)) {
                request.setAttribute("personalLinks", linkMap.get((byte) 2));
            }
        }
        request.setAttribute("pageName", "Link");
        request.setAttribute("configurations", configService.getAllConfigs());
        return "blog/amaze/link";
    }

    @GetMapping("/{subUrl}")
    public String detail(HttpServletRequest request, @PathVariable String subUrl ){
        BlogDetailVO detailVO = blogService.getBlogDetailByUrl(subUrl);
        if (detailVO != null){
            request.setAttribute("blogDetailVO", detailVO);
            request.setAttribute("pageName", subUrl);
            request.setAttribute("configurations",configService.getAllConfigs());
            return "blog/amaze/detail";
        }else {
            return "error/error_400";
        }
    }
}
