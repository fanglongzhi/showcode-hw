package com.gmcc.msb.msbsystem.vo.req;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @program: msb-system
 * @description: 模块VO
 * @author: zhifanglong
 * @create: 2018-10-11 09:24
 */
@ApiModel
public class ModuleVo {
    @ApiModelProperty(value = "主键",hidden = true)
    private Long id;
    @ApiModelProperty("父节点")
    @NotNull
    private Long parentId;
    @ApiModelProperty("模块名称,长度不超过50")
    @NotEmpty
    @Length(min=1,max=50)
    private String moduleName;
    @ApiModelProperty("模块KEY,长度补超过100")
    @NotEmpty
    @Length(min=1,max=100)
    private String key;
    @ApiModelProperty("模块关联的API的主键列表")
    private List<Integer> apis;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<Integer> getApis() {
        if(this.apis==null){
            this.apis= Lists.newArrayList();
        }
        return apis;
    }

    public void setApis(List<Integer> apis) {
        this.apis = apis;
    }

    public String buildApiList(){
        String apiStr="";
        for(Integer api:getApis()){
            apiStr+=api;
            apiStr+=",";
        }
        if(!"".equals(apiStr)) {
            apiStr = apiStr.substring(0, apiStr.length() - 1);
        }
        return apiStr;
    }
}
