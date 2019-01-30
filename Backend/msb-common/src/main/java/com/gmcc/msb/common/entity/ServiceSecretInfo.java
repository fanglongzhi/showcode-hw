package com.gmcc.msb.common.entity;

/**
 * @author zhi fanglong
 */
public class ServiceSecretInfo {
    private String serviceName;
    private String serviceId;
    private String serviceSecret;

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
}
