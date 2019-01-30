package com.gmcc.msb.msbsystem.common.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel
public class Result<T> {
    @ApiModelProperty("状态码，0 成功")
    private String code;
    @ApiModelProperty("状态信息")
    private String message;
    @ApiModelProperty("数据对象")
    private T content;

    public Result(){}
    public static Result fail(String code,String message){
        Result result = new Result();
        result.setCode(code);
        result.setMessage(message);

        return result;
    }
    public static Result success(){
        Result result = new Result();
        result.setCode("0");
        result.setMessage("success");

        return result;
    }

    public static Result success(Object content){
        Result result = new Result();
        result.setCode("0");
        result.setMessage("success");
        result.setContent(content);

        return result;
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }
}


