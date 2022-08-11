package com.hty.markquestion.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("比赛日程表")
public class Contest {

    @ApiModelProperty("主键")
    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("题目来源(比赛平台)")
    private String platform;
    @ApiModelProperty("比赛地址")
    private String link;
    @ApiModelProperty("比赛时间")
    private Date contestTime;
    @ApiModelProperty("表示比赛是否过期 过期为1 未过期为0")
    private Integer isExpired;
    @ApiModelProperty("比赛名称")
    private String contestName;

    @TableLogic//逻辑删除
    @ApiModelProperty("mybatis-plus需要的删除字段")
    private Integer deleted;

    @ApiModelProperty("是否需要邮件提示 0表示不需要 1表示需要")
    private Integer isMailTip;
}
