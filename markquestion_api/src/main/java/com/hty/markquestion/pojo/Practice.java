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
@ApiModel("练习题目表")
public class Practice {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("主键")
    private Integer id;
    @ApiModelProperty("题目")
    private String practiceName;
    @ApiModelProperty("选项信息，以###作为分割")
    private String practiceChanges;
    @ApiModelProperty("答案")
    private String practiceAnswer;
    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date createTime;
    @ApiModelProperty("修改日期")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;
    @ApiModelProperty("做对的人数")
    private Integer practiceRight;
    @ApiModelProperty("做错的人数")
    private Integer practiceFalse;
    @TableLogic
    @ApiModelProperty("mybatis-plus需要的删除字段")
    private Integer deleted;
    @ApiModelProperty("练习标签")
    private String practiceTag;
    @ApiModelProperty("练习来源")
    private String practiceFrom;
    @ApiModelProperty("练习解析")
    private String practiceExplain;
    @ApiModelProperty("判断是单选还是多选 单选是radio 多选是select")
    private String practiceIsRadioOrSelect;

}
