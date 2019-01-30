package com.gmcc.msb.msbsystem.vo.req.role;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel
public class ModifyUserRoleParam {
    @ApiModelProperty("角色主键")
    private Long roleId;
    @ApiModelProperty("需要添加的用户列表，由用户信息的userId组成")
    private List<Long> addUserIdList;
    @ApiModelProperty("需要移除的用户列表，由用户信息的userId组成")
    private List<Long> removeUserIdList;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public List<Long> getAddUserIdList() {
        return addUserIdList;
    }

    public void setAddUserIdList(List<Long> addUserIdList) {
        this.addUserIdList = addUserIdList;
    }

    public List<Long> getRemoveUserIdList() {
        return removeUserIdList;
    }

    public void setRemoveUserIdList(List<Long> removeUserIdList) {
        this.removeUserIdList = removeUserIdList;
    }
}
