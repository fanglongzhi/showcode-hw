package com.gmcc.msb.api.entity;

import com.gmcc.msb.common.entity.ModelParent;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

@Entity
@Table(name = "t_api_group")
@ApiModel
public class ApiGroup extends ModelParent {
    @ApiModelProperty("组名称")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
