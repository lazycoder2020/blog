package com.blog.demo.util;

import com.blog.demo.controller.vo.BlogListVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult implements Serializable {
    private List<?> list;
    private long pageSize;
    private long totalPage;
    private long currPage;
}
