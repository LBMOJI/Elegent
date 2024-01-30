package com.example.demo.Base;

import lombok.Data;

@Data
public class Result {
    private Integer code;
    private String msg;
    private Object data;

    public static Result success(Object object){
        Result result = new Result();
        result.setCode(ResultEnum.SUCCESS.getCode());
        result.setMsg(ResultEnum.SUCCESS.getMsg());
        result.setData(object);
        return result;
    }
    public static Result success(){
        return success(null);
    }

    public static Result ERROR(String msg){
        Result result = new Result();
        result.setMsg(msg);
        result.setCode(ResultEnum.ERROR.getCode());
        return result;
    }
}
