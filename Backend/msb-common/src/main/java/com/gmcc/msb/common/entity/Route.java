package com.gmcc.msb.common.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author Yuan Chunhai
 */
public class Route {

    Integer id;

    String serviceId;

    String serviceName;

    String path;

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    @Override
    public String toString() {
        return "Route{" +
                       "id=" + id +
                       ", serviceId='" + serviceId + '\'' +
                       ", serviceName='" + serviceName + '\'' +
                       ", path='" + path + '\'' +
                       '}';
    }
}
