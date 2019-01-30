package com.gmcc.msb.msbsystem.entity.module;

import com.gmcc.msb.msbsystem.common.MyModelParent;
import com.gmcc.msb.msbsystem.entity.API;
import com.gmcc.msb.msbsystem.vo.req.ModuleVo;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

@Entity
@Table(name="t_module")
@ApiModel
public class Module extends MyModelParent {
    @ApiModelProperty("父节点")
    private Long parentId;
    @ApiModelProperty("模块名称")
    private String moduleName;
    @ApiModelProperty("模块KEY")
    @Column(name="m_key")
    private String key;
    @ApiModelProperty("模块关联的API的主键列表，主键用逗号隔开")
    private String apis;
    @ApiModelProperty("模块状态，0生效，1失效")
    private String status;
    @Transient
    @ApiModelProperty("API列表")
    private List<API> apiList;


    public Module(){}
    public Module(ModuleVo param){
        this.parentId = param.getParentId();
        this.moduleName=param.getModuleName();
        this.key=param.getKey();
        this.apis="";
        for(Integer api:param.getApis()){
            this.apis+=api;
            this.apis+=",";
        }
        if(!"".equals(this.apis)) {
            this.apis = this.apis.substring(0, this.apis.length() - 1);
        }

    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getApis() {
        return apis;
    }

    public void setApis(String apis) {
        this.apis = apis;
    }

    public List<API> getApiList() {
        if(apiList==null){
            apiList= Lists.newArrayList();
        }
        return apiList;
    }

    public void setApiList(List<API> apiList) {
        this.apiList = apiList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
