package com.gmcc.msb.msbsystem.vo.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gmcc.msb.msbsystem.entity.org.Org;

import java.util.List;

public class OrgVo {

    private String label;
    private Org data;
    private boolean leaf = false;
    private boolean expanded;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private List<OrgVo> children;

    public OrgVo(){
    }

    public OrgVo(String label){
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Org getData() {
        return data;
    }

    public void setData(Org data) {
        this.data = data;
    }

    public List<OrgVo> getChildren() {
        return children;
    }

    public void setChildren(List<OrgVo> children) {
        this.children = children;
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
}
