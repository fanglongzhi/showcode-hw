package com.gmcc.msb.msbbreak.entity;

import com.gmcc.msb.common.entity.ModelParent;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="t_breaker_strategy")
public class BreakerStrategy extends ModelParent {
    @NotEmpty
    private String  strategyName;
    private Boolean enableBreaker;
    private Integer requestVolume;
    private Integer sleep;
    private Integer failRate;
    private Integer timeout;
    @NotNull
    private Boolean isDefault;

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
