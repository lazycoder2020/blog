package com.blog.demo.bean;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_blog_tag_relation")
public class BlogTagRelation {

    @TableId(type = IdType.AUTO)
    private Long relationId;
    private Long blogId;
    private Integer tagId;
    private Date createTime;
}
