package com.hty.markquestion.constant;

public enum ResponseMessage {
    SUCCESS(200,"成功"),
    ERROR(400,"出错"),
    USER_NOT_FOUND(1000,"用户为找到"),
    LOGIN_FAIL(1001,"用户名或密码错误"),
    ADVICE_TAG_EXISTS(1002,"推荐的标签已经存在");


    public final Integer code;
    public final String msg;
    ResponseMessage(Integer code,String msg){
        this.code = code;
        this.msg = msg;
    }
}
