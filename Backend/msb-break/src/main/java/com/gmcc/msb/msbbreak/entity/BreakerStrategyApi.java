package com.gmcc.msb.msbbreak.entity;

import com.gmcc.msb.common.entity.ModelParent;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="t_breaker_strategy_api")
public class BreakerStrategyApi extends ModelParent {
    private Integer strategyId;
    private Integer apiId;

    public Integer getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(Integer strategyId) {
        this.strategyId = strategyId;
    }

    public Integer getApiId() {
        return apiId;
    }

    public void setApiId(Integer apiId) {
        this.apiId = apiId;
    }
}
