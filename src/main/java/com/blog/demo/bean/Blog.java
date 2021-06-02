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
@TableName("tb_blog")
public class Blog {
    @TableId(type = IdType.AUTO)
    private Long blogId;
    private String blogTitle;
    private String blogSubUrl;
    private String blogCoverImage;
    private Integer blogCategoryId;
    private String blogCategoryName;
    private String blogTags;
    private Byte blogStatus;
    private Long blogViews;
    private Byte enableComment;
    private Byte isDeleted;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    private Date updateTime;
    private String blogContent;

}
