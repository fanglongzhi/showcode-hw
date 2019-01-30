package com.gmcc.msb.zuul.entity;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * @program: msb-zuul
 * @description: 用户数据组关系主键
 * @author: zhifanglong
 * @create: 2018-10-24 15:37
 */
public class DataOrgUserKey implements Serializable {
    @Column(name = "org_id")
    private Integer orgId;
    @Column(name = "user_id")
    private String userId;

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
