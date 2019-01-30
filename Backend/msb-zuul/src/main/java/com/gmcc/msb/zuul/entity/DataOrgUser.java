package com.gmcc.msb.zuul.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.util.Date;

/**
 * @program: msb-zuul
 * @description: 用户数据组
 * @author: zhifanglong
 * @create: 2018-11-09 11:17
 */
@Entity
@Table(name="t_data_org_user")
@IdClass(DataOrgUserKey.class)
public class DataOrgUser {
    @Id
    private Integer orgId;
    @Id
    private String userId;

    private Date createTime;

    private String mainFlag;//0表示可见组关系，1表示主组关系

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getMainFlag() {
        return mainFlag;
    }

    public void setMainFlag(String mainFlag) {
        this.mainFlag = mainFlag;
    }
}

