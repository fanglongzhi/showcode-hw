package com.gmcc.msb.msbsystem.vo.req.role;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @program: msb-system
 * @description: 创建ROLE VO
 * @author: zhifanglong
 * @create: 2018-10-24 14:21
 */
@ApiModel
public class CreateRoleParam {
    @NotEmpty
    @Length(max=50)
    @ApiModelProperty("角色名称，长度50")
    private String  roleName;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
