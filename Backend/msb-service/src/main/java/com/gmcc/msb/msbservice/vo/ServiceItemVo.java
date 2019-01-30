package com.gmcc.msb.msbservice.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
/**
 * @author zhi fanglong
 */
@ApiModel
public class ServiceItemVo {
    @NotEmpty
    @Length(min=1,max=50)
    @ApiModelProperty("服务名称")
    private String serviceName;

    @NotEmpty
    @Length(min=1,max=50)
    @ApiModelProperty("服务ID")
    private String serviceId;

    @ApiModelProperty("服务编码")
    private String serviceCode;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }
}
