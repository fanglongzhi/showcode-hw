package com.gmcc.msb.msbservice.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * @author zhi fanglong
 */
@ApiModel
public class ServiceStatusResp {
    @ApiModelProperty("服务ID")
    private String serviceId;
    @ApiModelProperty("实例IP")
    private String ip;
    @ApiModelProperty("实例端口")
    private Integer port;
    @ApiModelProperty("实例状态 UP 运行中")
    private String status;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
