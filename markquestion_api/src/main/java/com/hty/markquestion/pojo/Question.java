package com.hty.markquestion.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("题解表")
public class Question {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("主键")
    private Integer id;
    @ApiModelProperty("题解名称")
    private String questionName;
    @ApiModelProperty("题解来源")
    private String questionFrom;
    @ApiModelProperty("作者")
    private String author;
    @ApiModelProperty("题解标签")
    private String tag;
    @ApiModelProperty("题解正文")
    private String solution;
    @ApiModelProperty("浏览量")
    private Integer watch;
    @ApiModelProperty("难度")
    private String level;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty("创建时间")
    private Date createTime;
    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty("修改时间")
    private Date updateTime;
    @TableLogic
    @ApiModelProperty("mybatis-plus需要的删除字段")
    private Integer deleted;
}
