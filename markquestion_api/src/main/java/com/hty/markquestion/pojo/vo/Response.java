package com.hty.markquestion.pojo.vo;

import com.hty.markquestion.constant.ResponseMessage;
import lombok.Data;

@Data
public class Response {
    private Integer code;
    private String msg;
    private Object data;
    private PageInfo pageInfo;

    public Response(ResponseMessage responseMessage){
        this.code = responseMessage.code;
        this.msg = responseMessage.msg;
    }

    public Response(ResponseMessage responseMessage,Object data){
        this.code = responseMessage.code;
        this.msg = responseMessage.msg;
        this.data = data;
    }
}
