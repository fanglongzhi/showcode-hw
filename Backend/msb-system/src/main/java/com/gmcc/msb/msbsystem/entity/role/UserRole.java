package com.gmcc.msb.msbsystem.entity.role;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="t_user_role")
@IdClass(UserRoleKey.class)
public class UserRole {
    @Id
    private  Long roleId;
    @Id
    private Long userId;
    private Date createTime;
    private String createBy;


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
}
