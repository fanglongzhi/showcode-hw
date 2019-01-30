package com.gmcc.msb.msbservice.entity;

import com.gmcc.msb.common.entity.ModelParent;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Date;
/**
 * @author zhi fanglong
 */
@Entity
@Table(name="t_service")
@ApiModel
public class ServiceItem extends ModelParent{
    @ApiModelProperty("服务名称")
    private String serviceName;
    @ApiModelProperty("服务ID")
    private String serviceId;
    @ApiModelProperty(value = "服务密钥",hidden = true)
    private String serviceSecret;
    @ApiModelProperty("服务code")
    private String serviceCode;
    @ApiModelProperty("最后一次刷新时间")
    private Date refreshDate;

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

    public String getServiceSecret() {
        return serviceSecret;
    }

    public void setServiceSecret(String serviceSecret) {
        this.serviceSecret = serviceSecret;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public Date getRefreshDate() {
        return refreshDate;
    }

    public void setRefreshDate(Date refreshDate) {
        this.refreshDate = refreshDate;
    }
}
