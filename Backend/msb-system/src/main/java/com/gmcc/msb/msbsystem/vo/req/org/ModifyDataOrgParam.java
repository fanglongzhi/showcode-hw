package com.gmcc.msb.msbsystem.vo.req.org;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @program: msb-system
 * @description: 修改数据组名称参数
 * @author: zhifanglong
 * @create: 2018-10-30 09:52
 */
@ApiModel
public class ModifyDataOrgParam {
    @ApiModelProperty("组名称")
    @NotEmpty(message = "0007-00002")
    private String orgName;

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}
