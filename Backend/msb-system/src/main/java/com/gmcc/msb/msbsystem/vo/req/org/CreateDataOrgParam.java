package com.gmcc.msb.msbsystem.vo.req.org;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * @program: msb-system
 * @description: 创建数据组参数
 * @author: zhifanglong
 * @create: 2018-10-29 18:01
 */
@ApiModel
public class CreateDataOrgParam {
    @ApiModelProperty("父组主键")
    @NotNull(message = "0007-00001")
    private Integer parent;
    @ApiModelProperty("组名称")
    @NotEmpty(message = "0007-00002")
    private String orgName;

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}
