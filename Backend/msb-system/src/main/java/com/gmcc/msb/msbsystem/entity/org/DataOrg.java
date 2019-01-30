package com.gmcc.msb.msbsystem.entity.org;

import com.gmcc.msb.common.entity.ModelParent;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @program: msb-system
 * @description: 数据组
 * @author: zhifanglong
 * @create: 2018-10-24 15:28
 */
@Entity
@Table(name="t_data_org")
public class DataOrg extends ModelParent{

    private String orgName;

    private Integer parent;

    private String parentList;//父节点列表，例如：1,2,3,4

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public String getParentList() {
        return parentList;
    }

    public void setParentList(String parentList) {
        this.parentList = parentList;
    }
}
