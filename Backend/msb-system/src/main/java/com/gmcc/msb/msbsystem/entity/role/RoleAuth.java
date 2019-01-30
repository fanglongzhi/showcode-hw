package com.gmcc.msb.msbsystem.entity.role;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="t_role_auth")
@IdClass(RoleAuthKey.class)
public class RoleAuth{
    @Id
    private Long roleId;
    @Id
    private Long authId;
    private Date createTime;
    private String createBy;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getAuthId() {
        return authId;
    }

    public void setAuthId(Long authId) {
        this.authId = authId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
}
