package com.gmcc.msb.api.vo.response;

import com.gmcc.msb.api.entity.API;
import com.gmcc.msb.api.entity.ApiGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel("api分组响应")
public class ApiGroupResponse extends ApiGroup {

    private List<API> apis;

    @ApiModelProperty(name = "组包含的api", reference = "API")
    public List<API> getApis() {
        return apis;
    }

    public void setApis(List<API> apis) {
        this.apis = apis;
    }
}
