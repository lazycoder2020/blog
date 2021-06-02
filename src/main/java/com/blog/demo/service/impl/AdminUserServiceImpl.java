package com.blog.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blog.demo.bean.AdminUser;
import com.blog.demo.dao.AdminUserMapper;
import com.blog.demo.service.AdminUserService;
import com.blog.demo.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired
    AdminUserMapper adminUserMapper;

    @Override
    public AdminUser login(String userName, String password) {
        QueryWrapper<AdminUser> wrapper = new QueryWrapper<>();
        wrapper.eq("login_user_name",userName).eq("login_password",MD5Utils.MD5Encode(password));
        AdminUser user = adminUserMapper.selectOne(wrapper);
        return user;
    }

    @Override
    public AdminUser getUserbyId(Integer id) {
        AdminUser user = adminUserMapper.selectById(id);
        return user;
    }

    @Override
    public Boolean updatePassword(Integer loginUserId, String originalPassword, String newPassword) {
        AdminUser user = adminUserMapper.selectById(loginUserId);
        if (user == null || !MD5Utils.MD5Encode(originalPassword).equals(user.getLoginPassword())){
            return false;
        }
        user.setLoginPassword(MD5Utils.MD5Encode(newPassword));
        return adminUserMapper.updateById(user) > 0;
    }

    @Override
    public Boolean updateName(Integer loginUserId, String loginUserName, String nickName) {
        AdminUser user = adminUserMapper.selectById(loginUserId);
        if (user != null ){
            user.setLoginUserName(loginUserName);
            user.setNickName(nickName);
            return adminUserMapper.updateById(user) > 0;
        }
        return false;
    }
}
