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
@ApiModel("友链表")
public class FriendLink {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("主键")
    private Integer id;
    @ApiModelProperty("友链名称")
    private String linkName;
    @ApiModelProperty("友链地址")
    private String link;
    @ApiModelProperty("友链标签")
    private String tag;
    @ApiModelProperty("友链描述")
    private String description;
    @ApiModelProperty("简介")
    private String introduce;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty("创建时间")
    private Date createTime;

    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty("更新时间")
    private Date updateTime;

    @TableLogic
    @ApiModelProperty("mybatis-plus需要的删除字段")
    private Integer deleted;
}
