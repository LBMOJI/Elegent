package com.example.demo.Base;

public enum ResultEnum {
    SUCCESS(10000,"成功"),
    ERROR(-1,"失败");

    private Integer code;
    private String msg;

    ResultEnum(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }
    public Integer getCode(){
        return code;
    }
    public String getMsg(){
        return msg;
    }
}
