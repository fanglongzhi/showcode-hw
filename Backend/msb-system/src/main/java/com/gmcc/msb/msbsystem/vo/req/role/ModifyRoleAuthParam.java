package com.gmcc.msb.msbsystem.vo.req.role;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class ModifyRoleAuthParam {
    @ApiModelProperty("角色主键")
    private Long roleId;
    @ApiModelProperty("权限主键")
    private List<Long> addAuthId;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public List<Long> getAddAuthId() {
        return addAuthId;
    }

    public void setAddAuthId(List<Long> addAuthId) {
        this.addAuthId = addAuthId;
    }
}
