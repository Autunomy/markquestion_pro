package com.hty.markquestion.pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ApiModel("推荐标签表")
public class AdviceTag {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("主键")
    private Integer id;
    @ApiModelProperty("推荐标签名")
    private String adviceTagName;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty("创建时间")
    private Date createTime;
    @TableLogic
    @ApiModelProperty("mybatis-plus需要的删除字段")
    private Integer deleted;
    @ApiModelProperty("这个标签对应的推荐的数量")
    private Integer adviceTagNum;
}
