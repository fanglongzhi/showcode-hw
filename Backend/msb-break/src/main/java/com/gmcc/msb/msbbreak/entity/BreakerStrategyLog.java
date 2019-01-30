package com.gmcc.msb.msbbreak.entity;

import com.gmcc.msb.common.entity.ModelParent;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="t_breaker_strategy_log")
public class BreakerStrategyLog extends ModelParent {
    private Integer strategyId;
    @NotEmpty
    private String  strategyName;
    private Boolean enableBreaker;
    private Integer requestVolume;
    private Integer sleep;
    private Integer failRate;
    private Integer timeout;
    @NotNull
    private Boolean isDefault;

    private String apis;

    @ApiModelProperty("操作类型：1，新增；2，修改；3，删除；" +
            "4，启用熔断策略；5，关闭熔断策略；6，为一个API绑定熔断策略；" +
            "7，删除一个API绑定熔断策略；8，批量API绑定熔断策略；" +
            "9，批量删除API绑定熔断策略")
    private int operatorType;

    public BreakerStrategyLog() {
    }

    public BreakerStrategyLog(BreakerStrategy strategy) {
        this.strategyId = strategy.getId();
        this.strategyName = strategy.getStrategyName();
        this.enableBreaker = strategy.getEnableBreaker();
        this.requestVolume = strategy.getRequestVolume();
        this.sleep = strategy.getSleep();
        this.failRate = strategy.getFailRate();
        this.timeout = strategy.getTimeout();
        this.isDefault = strategy.getIsDefault();
    }

    public String getApis() {
        return apis;
    }

    public void setApis(String apis) {
        this.apis = apis;
    }

    public Integer getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(Integer strategyId) {
        this.strategyId = strategyId;
    }

    public int getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(int operatorType) {
        this.operatorType = operatorType;
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

    public void setIsDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }
}
