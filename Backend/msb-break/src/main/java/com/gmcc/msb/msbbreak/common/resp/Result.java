package com.gmcc.msb.msbbreak.common.resp;

public class Result<T> {
    private String code;
    private String message;
    private T content;

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


