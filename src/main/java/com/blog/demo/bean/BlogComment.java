package com.blog.demo.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_blog_comment")
public class BlogComment {

    @TableId(type = IdType.AUTO)
    private Long commentId;
    private Long blogId;
    private String commentator;
    private String email;
    private String websiteUrl;
    private String commentBody;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date commentCreateTime;
    private String commentatorIp;
    private String replyBody;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date replyCreateTime;
    private Byte commentStatus;
    private Byte isDeleted;
}
