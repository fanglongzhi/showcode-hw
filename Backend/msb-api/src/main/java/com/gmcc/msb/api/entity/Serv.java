package com.gmcc.msb.api.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

/**
 * @author Yuan Chunhai
 */
@Entity
@Table(name = "t_service")
@ApiModel
public class Serv {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ApiModelProperty("服务ID")
    private String serviceId;

    @ApiModelProperty("服务名")
    private String serviceName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
