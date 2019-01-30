package com.gmcc.msb.common.property;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @program: msb-common
 * @description: 登录用户信息
 * @author: zhifanglong
 * @create: 2018-10-17 14:43
 */
public class UserContext {
    private String userId;
    private Long orgId;
    private List<Long> dataOrgList;

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Long> getDataOrgList() {
        if(dataOrgList==null){
            dataOrgList= Lists.newArrayList();
        }
        return dataOrgList;
    }

    public void setDataOrgList(List<Long> dataOrgList) {
        this.dataOrgList = dataOrgList;
    }
}
