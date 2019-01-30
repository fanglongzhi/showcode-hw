package com.gmcc.msb.api.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

@Entity
@Table(name = "t_api_group_item")
@ApiModel
public class ApiGroupItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty("ID")
    private Integer id;

    @ApiModelProperty("组ID")
    private Integer groupId;

    @ApiModelProperty("api ID")
    private Integer apiId;

    public ApiGroupItem(){

    }

    public ApiGroupItem(Integer groupId, Integer apiId) {
        this.groupId = groupId;
        this.apiId = apiId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getApiId() {
        return apiId;
    }

    public void setApiId(Integer apiId) {
        this.apiId = apiId;
    }
}
