package com.gmcc.msb.msbsystem.entity.role;

import com.gmcc.msb.msbsystem.common.MyModelParent;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="t_role")
@ApiModel
public class Role extends MyModelParent {
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
