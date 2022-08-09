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
@ApiModel("练习题目关联的用户表")
public class PracticeUser {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("主键")
    private Integer id;
    @ApiModelProperty("用户唯一ID")
    private String practiceUserId;
    @ApiModelProperty("用户总共做过的题目数量")
    private Integer practiceUserPracticeNum;
    @ApiModelProperty("用户错误的题目数量")
    private Integer practiceUserPracticeWrongNum;
    @ApiModelProperty("用户做过的错误的题号  使用#作为分割")
    private String practiceUserWrongPractice;
    @ApiModelProperty("用户头像")
    private String practiceUserImg;
    @ApiModelProperty("用户名")
    private String practiceUserUsername;
    @ApiModelProperty("邮箱")
    private String practiceUserEmail;
    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date createTime;
    @TableLogic
    @ApiModelProperty("mybatis-plus需要的删除字段")
    private Integer deleted;
}
