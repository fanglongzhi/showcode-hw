package com.gmcc.msb.api.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * @author Yuan Chunhai
 */
@ApiModel
public class AuditRequest {

    /**
     * 0上线申请，1下线申请
     */
    @NotNull(message = "类型不能为空")
    @ApiModelProperty("类型,0上线申请，1下线申请")
    private Integer type;

    /**
     * 0, 通过， 1, 不通过
     */
    @NotNull(message = "审核结果不能为空")
    @ApiModelProperty("审核结果,0, 通过， 1, 不通过")
    private Integer result;


    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }
}
