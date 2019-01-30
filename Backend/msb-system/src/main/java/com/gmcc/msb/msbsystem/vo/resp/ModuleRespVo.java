package com.gmcc.msb.msbsystem.vo.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gmcc.msb.msbsystem.entity.module.Module;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;

import java.util.List;

/**
 * @program: msb-system
 * @description: 模块视图
 * @author: zhifanglong
 * @create: 2018-10-11 11:41
 */
@ApiModel
public class
ModuleRespVo {
    private Long id;
    private String label;
    private Module data;
    private boolean leaf = true;
    private boolean expanded = true;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private List<ModuleRespVo> children;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Module getData() {
        return data;
    }

    public void setData(Module data) {
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

    public List<ModuleRespVo> getChildren() {
        if(children==null){
            children=Lists.newArrayList();
        }
        return children;
    }

    public void setChildren(List<ModuleRespVo> children) {
        this.children = children;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
