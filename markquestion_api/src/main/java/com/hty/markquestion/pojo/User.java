package com.hty.markquestion.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class User {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("主键")
    private Integer id;
    private String username;
    private String password;
    private String email;
    private Date createTime;
    private String headImg;
}
