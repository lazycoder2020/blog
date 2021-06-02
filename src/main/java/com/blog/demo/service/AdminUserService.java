package com.blog.demo.service;

import com.blog.demo.bean.AdminUser;

public interface AdminUserService {
    AdminUser login(String userName, String password);
    AdminUser getUserbyId(Integer id);
    Boolean updatePassword(Integer loginUserId, String originalPassword, String newPassword);
    Boolean updateName(Integer loginUserId, String loginUserName, String nickName);

}
