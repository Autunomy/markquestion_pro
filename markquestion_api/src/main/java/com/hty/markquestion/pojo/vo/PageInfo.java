package com.hty.markquestion.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

//分页信息
@Data
@AllArgsConstructor
public class PageInfo {
    private Integer currentPage;//当前页码
    private Integer pageSize;//页面大小
    private Integer total;//数据总量
}
