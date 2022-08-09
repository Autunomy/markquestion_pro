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
@ApiModel("博主信息表")
public class AuthorMessage {
    @ApiModelProperty("主键")
    @TableId(type = IdType.AUTO)//主键自增
    private Integer id;

    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("性别 1表示男 0表示女")
    private Integer gender;//1表示男 0表示女
    @ApiModelProperty("兴趣爱好")
    private String hobby;
    @ApiModelProperty("星座")
    private String constellation;
    @ApiModelProperty("博主描述")
    private String description;
    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("qq号")
    private String qq;
    @ApiModelProperty("微信号")
    private String wechat;
    @ApiModelProperty("头像的路径")
    private String headImg;
    @ApiModelProperty("手机号")
    private String phone;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)//修改的时候更新
    @ApiModelProperty("更新时间")
    private Date update_time;


}
