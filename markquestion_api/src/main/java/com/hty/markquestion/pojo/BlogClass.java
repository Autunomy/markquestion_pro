package com.hty.markquestion.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ApiModel("博客分类表")
public class BlogClass {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String className;
    private Integer classNum;
    private Date createDate;
}
