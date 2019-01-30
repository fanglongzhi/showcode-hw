package com.gmcc.msb.msbsystem.entity.role;

import javax.persistence.Column;
import java.io.Serializable;

public class RoleAuthKey implements Serializable{
    @Column(name = "role_id")
    private Long roleId;
    @Column(name = "auth_id")
    private Long authId;

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

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((roleId == null) ? 0 : roleId.hashCode());
        result = PRIME * result + ((authId == null) ? 0 : authId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RoleAuthKey other = (RoleAuthKey) obj;
        if (roleId == null) {
            if (other.roleId != null)
                return false;
        } else if (!roleId.equals(other.roleId))
            return false;
        if (authId == null) {
            if (other.authId != null)
                return false;
        } else if (!authId.equals(other.authId))
            return false;
        return true;
    }
}
