package com.gmcc.msb.common.vo;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.gmcc.msb.common.entity.ErrorCode;

/**
 * @param <T>
 * @author Yuan Chunhai
 */
public class Response<T> {

    public static final String SUCCESS = "0";

    private String code;

    private String message;

    private String traceId;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private String description;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private T content;


    /**
     * 默认code为0 成功
     */
    public Response() {
        this(null, SUCCESS, "成功");
    }

    /**
     * 默认code为0 成功
     */
    public Response(T content) {
        this(content, SUCCESS, "成功");
    }

    public Response(T content, String code, String message) {
        this.code = code;
        this.message = message;
        this.content = content;
    }

    public Response(T content, String code, String message, String description) {
        this.code = code;
        this.message = message;
        this.content = content;
        this.description = description;
    }

    public static Response ok() {
        return new Response();
    }

    public static <T> Response ok(T content) {
        return new Response<>(content);
    }

    public static Response notOk(ErrorCode errorCode) {
        return new Response(null, errorCode.getCode(), errorCode.getMessage()
                , errorCode.getDescription());
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Response{" +
                       "code='" + code + '\'' +
                       ", message='" + message + '\'' +
                       ", description='" + description + '\'' +
                       ", content=" + content +
                       ", traceId=" + traceId +
                       '}';
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
}
