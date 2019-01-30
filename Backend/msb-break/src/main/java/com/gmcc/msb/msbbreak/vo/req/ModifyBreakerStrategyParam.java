package com.gmcc.msb.msbbreak.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ApiModel
public class ModifyBreakerStrategyParam {
    @ApiModelProperty(hidden = true)
    private Integer id;
    @ApiModelProperty("策略名称，长度不超过50字符")
    @NotEmpty
    @Size(max = 50)
    private String  strategyName;
    @ApiModelProperty("是否启用熔断")
    private Boolean enableBreaker;
    @ApiModelProperty("一个统计窗口需要达到的请求数量")
    private Integer requestVolume;
    @ApiModelProperty("一次熔断的时长，毫秒")
    private Integer sleep;
    @ApiModelProperty("失败率")
    private Integer failRate;
    @ApiModelProperty("超时时长")
    private Integer timeout;
    @ApiModelProperty(value = "是否默认策略",hidden = true)
    private Boolean isDefault;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIDefault(Boolean aDefault) {
        isDefault = aDefault;
    }
}
