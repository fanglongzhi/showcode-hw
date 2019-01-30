package com.gmcc.msb.demo.consumer;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

/**
 * @program: msb-demo-consumer
 * @description: 结果类
 * @author: zhifanglong
 * @create: 2018-11-28 14:58
 */
public class Response {

    private String code;

    private String message;

    private String description;

    private Map content;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map getContent() {
        return content;
    }

    public void setContent(Map content) {
        this.content = content;
    }
}
