package com.blog.demo.controller.admin;
import com.blog.demo.bean.AdminUser;
import com.blog.demo.dao.BlogTagMapper;
import com.blog.demo.service.AdminUserService;
import com.blog.demo.service.BlogCategoryService;
import com.blog.demo.service.BlogCommentService;
import com.blog.demo.service.BlogLinkeService;
import com.blog.demo.service.impl.BlogServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminUserService userService;

    @Autowired
    BlogCategoryService categoryService;

    @Autowired
    BlogTagMapper tagService;

    @Autowired
    BlogCommentService commentService;

    @Autowired
    BlogServiceImpl blogService;

    @Autowired
    BlogLinkeService linkeService;


    @GetMapping("/login")
    public String login(){
        return "admin/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String userName,
                        @RequestParam String password,
                        @RequestParam String verifyCode,
                        HttpSession session){
        if (verifyCode.isEmpty()){
            session.setAttribute("errorMsg", "verify code required");
            return "admin/login";
        }

        if (userName.isEmpty() || password.isEmpty()){
            session.setAttribute("errorMsg", "username or password is empty");
            return "admin/login";
        }

        String kaptchaCode = session.getAttribute("verifyCode") + "";

        if (kaptchaCode.isEmpty() || !verifyCode.equals(kaptchaCode)){
            session.setAttribute("errorMsg", "verifyCode error");
            return "admin/login";
        }

        AdminUser user = userService.login(userName, password);
        if (user != null){
            session.setAttribute("loginUser", user.getNickName());
            session.setAttribute("loginUserId", user.getAdminUserId());
            return "redirect:/admin/index";
        } else {
            return "admin/login";
        }
    }

    @GetMapping({"/index", "", "/", "index.html"})
    public String index(HttpServletRequest request){
        request.setAttribute("path","index");
        request.setAttribute("categoryCount", categoryService.getTotalCategories());
        request.setAttribute("blogCount", blogService.getTotalBlogs());
        request.setAttribute("linkCount", linkeService.getTotalLinks());
        request.setAttribute("tagCount", tagService.getTotalTags());
        request.setAttribute("commentCount", commentService.getTotalComments());

        return "admin/index";
    }

    @GetMapping("/profile")
    public String profile(HttpServletRequest request){
        if(request.getSession().getAttribute("loginUserId") == null){
            return "admin/login";
        }
        Integer userId= (int)request.getSession().getAttribute("loginUserId");
        AdminUser user = userService.getUserbyId(userId);
        if (user != null){
            request.setAttribute("path", "profile");
            request.setAttribute("loginUserName", user.getLoginUserName());
            request.setAttribute("nickName", user.getNickName());
            return "admin/profile";
        } else {
            return "admin/login";
        }
    }

    @PostMapping("/profile/password")
    @ResponseBody
    public String updatePassword(HttpServletRequest request, @RequestParam("originalPassword") String originalPassword,
                                 @RequestParam("newPassword") String newPassword){
        if (originalPassword == null || originalPassword.isEmpty() || newPassword == null || newPassword.isEmpty()){
            return "Update Failed";
        }
        Integer loginUserId = (int)request.getSession().getAttribute("loginUserId");
        if(userService.updatePassword(loginUserId,originalPassword,newPassword)){
            request.getSession().removeAttribute("loginUserName");
            request.getSession().removeAttribute("loginUserId");
            request.getSession().removeAttribute("nickName");
            return "success";
        }else {
            return "Update Failed";
        }
    }

    @PostMapping("/profile/name")
    @ResponseBody
    public String UpdateName(HttpServletRequest request, @RequestParam("loginUserName") String loginUserName,
                                 @RequestParam("nickName") String nickName){
        if (loginUserName == null || loginUserName.isEmpty() || nickName == null || nickName.isEmpty()){
            return "Update Failed";
        }
        Integer loginUserId = (int)request.getSession().getAttribute("loginUserId");
        if(userService.updateName(loginUserId,loginUserName,nickName)){
            return "success";
        }else {
            return "Update Failed";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        request.getSession().removeAttribute("loginUser");
        request.getSession().removeAttribute("loginUserId");
        request.getSession().removeAttribute("errorMsg");
        return "admin/login";
    }
}
