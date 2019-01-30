package com.gmcc.msb.msbbreak.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

@ApiModel
public class BindBreakerApiParam {
    @ApiModelProperty("熔断策略ID")
    @NotNull
    private Integer strategyId;

    public Integer getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(Integer strategyId) {
        this.strategyId = strategyId;
    }

}
