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

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ApiModel("访客日志表")
public class AccessLog {
    @ApiModelProperty("主键")
    @TableId(type = IdType.AUTO)//主键自增
    private Integer id;

    @ApiModelProperty("ip地址")
    private String ip;
    @ApiModelProperty("ip地址对应的地理位置")
    private String address;
    @ApiModelProperty("访问时间")
    private Date watchTime;
}
