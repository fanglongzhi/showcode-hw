package com.gmcc.msb.api.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ApiModel
public class ApiRequest {

    @NotNull(message = "0001-00010")
    @NotEmpty(message = "0001-00010")
    @ApiModelProperty(value = "服务ID", required = true)
    private String serviceId;

    @NotEmpty(message = "0001-00011")
    @Size(max = 100, message = "api名称最大长度100")
    @ApiModelProperty(value = "api名称", required = true)
    private String apiName;

    @NotEmpty(message = "0001-00012")
    @Size(max = 100, message = "api路径最大长度100")
    @ApiModelProperty(value = "api路径", required = true)
    private String path;

    @NotEmpty(message = "0001-00013")
    @Size(max = 10, message = "0001-00014")
    @ApiModelProperty(value = "请求方法", required = true)
    private String method;

    /**
     * 是否匿名访问，默认不能匿名访问
     */
    private boolean isAnnoymousAccess = false;


    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public boolean isAnnoymousAccess() {
        return isAnnoymousAccess;
    }

    public void setAnnoymousAccess(boolean annoymousAccess) {
        isAnnoymousAccess = annoymousAccess;
    }
}
