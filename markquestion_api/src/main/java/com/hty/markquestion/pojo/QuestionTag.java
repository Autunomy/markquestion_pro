package com.hty.markquestion.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("题解标签")
public class QuestionTag {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("主键")
    private Integer id;
    @ApiModelProperty("标签名称")
    private String tagName;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty("创建日期")
    private Date createTime;
    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty("修改日期")
    private Date updateTime;
}
