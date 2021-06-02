package com.blog.demo.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_admin_user")
public class AdminUser {
    @TableId(type = IdType.AUTO)
    private Integer adminUserId;
    private String LoginUserName;
    private String loginPassword;
    private String nickName;
    private Byte locked;
}
