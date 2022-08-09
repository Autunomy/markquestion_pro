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
@ApiModel("推荐信息表")
public class Advice {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("主键")
    private Integer id;
    @ApiModelProperty("推荐名称")
    private String adviceName;
    @ApiModelProperty("推荐作者")
    private String adviceAuthor;
    @ApiModelProperty("推荐标签")
    private String adviceTag;
    @ApiModelProperty("推荐观看量")
    private Integer adviceWatch;
    @ApiModelProperty("推荐内容")
    private String adviceContent;
    @ApiModelProperty("推荐描述")
    private String adviceDescribe;
    @ApiModelProperty("配图地址")
    private String adviceImg;
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
