package com.gmcc.msb.common.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 请求日志
 *
 * @author Yuan Chunhai
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class RequestLog {

    public static final int SUCCESS = 0;
    public static final int FAIL = 1;


    private String traceId;

    private int appId;
    private int apiId;
    private String serviceId;
    private String userId;

    /**
     * 请求时间
     */
    private Long requestTime;
    private String requestUri;
    private String requestMethod;
    private String requestParam;
    private String requestBody;


    /**
     * 请求结果 0成功，1失败
     */
    private int requestResult;

    /**
     * 整个请求耗时，毫秒
     */
    private int elapsedTime;

    /**
     * 网关延时时间
     */
    private int gatewayDelayTime;

    /**
     * 微服务请求耗时，毫秒
     */
    private int requestServiceTime;

    public int getGatewayDelayTime() {
        return gatewayDelayTime;
    }

    public void setGatewayDelayTime(int gatewayDelayTime) {
        this.gatewayDelayTime = gatewayDelayTime;
    }

    public int getRequestServiceTime() {
        return requestServiceTime;
    }

    public void setRequestServiceTime(int requestServiceTime) {
        this.requestServiceTime = requestServiceTime;
    }

    public int getApiId() {
        return apiId;
    }

    public void setApiId(int apiId) {
        this.apiId = apiId;
    }

    public int getRequestResult() {
        return requestResult;
    }

    public void setRequestResult(int requestResult) {
        this.requestResult = requestResult;
    }

    public int getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(int elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public Long getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Long requestTime) {
        this.requestTime = requestTime;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getRequestParam() {
        return requestParam;
    }

    public void setRequestParam(String requestParam) {
        this.requestParam = requestParam;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    @Override
    public String toString() {
        return "RequestLog{" +
                       "traceId='" + traceId + '\'' +
                       ", appId=" + appId +
                       ", apiId=" + apiId +
                       ", serviceId='" + serviceId + '\'' +
                       ", userId='" + userId + '\'' +
                       ", requestTime=" + requestTime +
                       ", requestUri='" + requestUri + '\'' +
                       ", requestMethod='" + requestMethod + '\'' +
                       ", requestParam='" + requestParam + '\'' +
                       ", requestBody='" + requestBody + '\'' +
                       ", requestResult=" + requestResult +
                       ", elapsedTime=" + elapsedTime +
                       '}';
    }
}
