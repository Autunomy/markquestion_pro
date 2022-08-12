package com.hty.markquestion.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

//回复评论的实体类
@Data
public class CommentReply {
    @ApiModelProperty("主键")
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String content;
    private Integer userId;
    private Integer questionId;
    private Integer isReply;
    private Integer commentId;
    private Date commentDate;

    private String username;
}
