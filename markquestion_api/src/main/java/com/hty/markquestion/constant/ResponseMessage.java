package com.hty.markquestion.constant;

public enum ResponseMessage {
    SUCCESS(200,"成功"),
    ERROR(400,"出错");


    public final Integer code;
    public final String msg;
    ResponseMessage(Integer code,String msg){
        this.code = code;
        this.msg = msg;
    }
}
