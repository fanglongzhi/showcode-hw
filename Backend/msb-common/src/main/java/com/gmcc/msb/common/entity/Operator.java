package com.gmcc.msb.common.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @program: msb-common
 * @description: 用户信息
 * @author: zhifanglong
 * @create: 2018-10-24 11:21
 */
@Embeddable
public class Operator {
    @Column(name="create_by")
    private String userId;
    @Column(name="org_id")
    private Long orgId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }
}
