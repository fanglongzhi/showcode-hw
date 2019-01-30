package com.gmcc.msb.api.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;
import java.util.List;

@ApiModel(value = "API分组请求")
public class ApiGroupRequest {

    @NotEmpty(message = "组名不能为空")
    @Size(max = 100, message = "组名最大长度为100")
    private String name;

    @ApiModelProperty(value = "组中API id列表", required = false)
    private List<Integer> apis;

    @ApiModelProperty(value = "组名称", required = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getApis() {
        return apis;
    }

    public void setApis(List<Integer> apis) {
        this.apis = apis;
    }
}
