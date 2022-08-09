package com.hty.markquestion.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@ApiModel("网站基本信息表")
public class WebBasicMessage {
    @ApiModelProperty("主键")
    @TableId(type = IdType.AUTO)
    private Integer id;
    @ApiModelProperty("访问人数")
    private Integer watchNum;
    @ApiModelProperty("网站创建时间")
    private Date createTime;
}
