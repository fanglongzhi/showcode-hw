package com.gmcc.msb.msbsystem.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel
public class AuthorityVo {
    @ApiModelProperty("模块主键")
    private Long moduleId;
    @ApiModelProperty("模块名称")
    private String moduleName;
    @ApiModelProperty("模块KEY")
    private String key;
    @ApiModelProperty("模块依赖的API列表")
    private String apis;
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("创建人")
    private String createBy;

    public AuthorityVo(){}
    public AuthorityVo(Long moduleId, String moduleName, String key,
                       String apis, Date createTime, String createBy) {

        this.moduleId = moduleId;
        this.moduleName = moduleName;
        this.key = key;
        this.apis = apis;
        this.createTime = createTime;
        this.createBy = createBy;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

}
