package com.gmcc.msb.msbbreak.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @program: msb-break
 * @description: API VO
 * @author: zhifanglong
 * @create: 2018-11-07 14:57
 */
@ApiModel
public class ApiVo {
    private Integer apiId;

    private String serviceId;

    private String apiName;

    /**
     * api路径
     */
    private String path;

    private String method;

    private Integer strategyId;

    private String strategyName;

    private Boolean enableBreaker;

    private Integer requestVolume;

    private Integer sleep;

    private Integer failRate;

    private Integer timeout;

    public ApiVo(){}


    public ApiVo(Integer apiId, String serviceId, String apiName, String path,
                 String method, Integer strategyId, String strategyName,
                 Boolean enableBreaker, Integer requestVolume, Integer sleep,
                 Integer failRate, Integer timeout) {
        this.apiId = apiId;
        this.serviceId = serviceId;
        this.apiName = apiName;
        this.path = path;
        this.method = method;
        this.strategyId = strategyId;
        this.strategyName = strategyName;
        this.enableBreaker = enableBreaker;
        this.requestVolume = requestVolume;
        this.sleep = sleep;
        this.failRate = failRate;
        this.timeout = timeout;
    }

    public ApiVo(Integer apiId, String serviceId, String apiName, String path, String method) {
        this.apiId = apiId;
        this.serviceId = serviceId;
        this.apiName = apiName;
        this.path = path;
        this.method = method;
    }

    @Override
    public int hashCode() {
        return apiId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ApiVo) {
            ApiVo api = (ApiVo) obj;
            return (api.apiId.equals(this.apiId));
        }
        return super.equals(obj);
    }

    public Integer getApiId() {
        return apiId;
    }

    public void setApiId(Integer apiId) {
        this.apiId = apiId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Integer getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(Integer strategyId) {
        this.strategyId = strategyId;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    public Boolean getEnableBreaker() {
        return enableBreaker;
    }

    public void setEnableBreaker(Boolean enableBreaker) {
        this.enableBreaker = enableBreaker;
    }

    public Integer getRequestVolume() {
        return requestVolume;
    }

    public void setRequestVolume(Integer requestVolume) {
        this.requestVolume = requestVolume;
    }

    public Integer getSleep() {
        return sleep;
    }

    public void setSleep(Integer sleep) {
        this.sleep = sleep;
    }

    public Integer getFailRate() {
        return failRate;
    }

    public void setFailRate(Integer failRate) {
        this.failRate = failRate;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }


}
