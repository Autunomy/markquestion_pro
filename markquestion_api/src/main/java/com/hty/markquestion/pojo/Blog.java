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
@ApiModel("博客表")
public class Blog {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String title;
    private Integer watch;
    private Date createTime;
    private Integer commentNum;
    private Integer classId;
    private String content;
    private String descp;
}
