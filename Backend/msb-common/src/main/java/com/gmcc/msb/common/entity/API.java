package com.gmcc.msb.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Transient;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Yuan Chunhai
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class API {

    public static final int STATUS_NEW = 0;
    public static final int STATUS_APPLY_ONLINE = 1;
    public static final int STATUS_ONLINE = 2;
    public static final int STATUS_APPLY_ONLINE_FAIL = 3;
    public static final int STATUS_APPLY_OFFLINE = 4;
    public static final int STATUS_OFFLINE = 5;
    public static final int STATUS_APPLY_OFFLINE_FAIL = 6;


    private static final Map<Integer, String> STATUS_NAMES = new HashMap<>();

    static {
        STATUS_NAMES.put(STATUS_NEW, "新增");
        STATUS_NAMES.put(STATUS_APPLY_ONLINE, "申请上线中");
        STATUS_NAMES.put(STATUS_ONLINE, "已上线");
        STATUS_NAMES.put(STATUS_APPLY_ONLINE_FAIL, "上线审核不通过");
        STATUS_NAMES.put(STATUS_APPLY_OFFLINE, "申请下线中");
        STATUS_NAMES.put(STATUS_OFFLINE, "已下线");
        STATUS_NAMES.put(STATUS_APPLY_OFFLINE_FAIL, "下线审核不通过");
    }

    private Integer id;

    private String serviceId;

    @Transient
    private String serviceName;

    private String apiName;

    private String path;

    private String method;

    /**
     * 0:新增，1:申请上线，2:已上线，3：上线审核不通过，4:申请下线，5：已下线，6：下线审核不通过
     */
    private Integer status;

    /** 是否匿名访问，默认不能匿名访问 */
    private boolean isAnnoymousAccess = false;

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public static String getStatusName(Integer status){
        return STATUS_NAMES.get(status);
    }

    public boolean isAnnoymousAccess() {
        return isAnnoymousAccess;
    }

    public void setAnnoymousAccess(boolean annoymousAccess) {
        isAnnoymousAccess = annoymousAccess;
    }
}
