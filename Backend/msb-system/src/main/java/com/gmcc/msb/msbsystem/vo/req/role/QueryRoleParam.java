package com.gmcc.msb.msbsystem.vo.req.role;

import java.util.List;

/**
 * @program: msb-system
 * @description: 角色数据过滤参数
 * @author: zhifanglong
 * @create: 2018-10-25 11:46
 */
public class QueryRoleParam {
    private String  roleName;
    private Long id;
    private List<Long> orgIds;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Long> getOrgIds() {
        return orgIds;
    }

    public void setOrgIds(List<Long> orgIds) {
        this.orgIds = orgIds;
    }
}
