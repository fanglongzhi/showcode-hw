package com.gmcc.msb.msbsystem.vo.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gmcc.msb.msbsystem.entity.org.DataOrg;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;

import java.util.List;

/**
 * @program: msb-system
 * @description: 数据组响应数据结构
 * @author: zhifanglong
 * @create: 2018-10-30 11:23
 */
@ApiModel
public class DataOrgRespVo {
    private Integer id;
    private String label;
    private DataOrg data;
    private boolean leaf = true;
    private boolean expanded = true;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private List<DataOrgRespVo> children;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public DataOrg getData() {
        return data;
    }

    public void setData(DataOrg data) {
        this.data = data;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public List<DataOrgRespVo> getChildren() {
        if(children==null){
            this.children= Lists.newArrayList();
        }
        return children;
    }

    public void setChildren(List<DataOrgRespVo> children) {
        this.children = children;
    }
}
