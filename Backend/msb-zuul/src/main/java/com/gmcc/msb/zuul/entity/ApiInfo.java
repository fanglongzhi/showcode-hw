package com.gmcc.msb.zuul.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Yuan Chunhai
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "t_service_api")
public class ApiInfo {

    public static final int STATUS_ONLINE = 2;

    private String serviceId;
    private String apiName;

    @Id
    private Integer id;
    private String path;
    private String method;
    private Integer status;

    /** 是否匿名访问，默认不能匿名访问 */
    private boolean isAnnoymousAccess = false;

    public ApiInfo(){
    }

    public ApiInfo(Integer id, String serviceId){
        this.id = id;
        this.serviceId = serviceId;
    }

    public ApiInfo(Integer id, String path, String method) {
        this.id = id;
        this.path = path;
        this.method = method;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public boolean isAnnoymousAccess() {
        return isAnnoymousAccess;
    }

    public void setAnnoymousAccess(boolean annoymousAccess) {
        isAnnoymousAccess = annoymousAccess;
    }

    @Override
    public String toString() {
        return "ApiInfo{" +
                       "serviceId='" + serviceId + '\'' +
                       ", apiName='" + apiName + '\'' +
                       ", id=" + id +
                       ", path='" + path + '\'' +
                       ", method='" + method + '\'' +
                       ", status=" + status +
                       ", isAnnoymousAccess=" + isAnnoymousAccess +
                       '}';
    }
}
