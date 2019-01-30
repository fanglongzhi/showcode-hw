package com.gmcc.msb.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gmcc.msb.common.entity.ModelParent;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_service_api_audit")
@ApiModel
public class ApiAudit extends ModelParent {

    public static final String AUDIT_RESULT_SUCCESS = "通过";
    public static final String AUDIT_RESULT_FAIL = "不通过";

    public static final int APPLY_TYPE_ONLINE = 0;
    public static final int APPLY_TYPE_OFFLINE = 1;

    @ApiModelProperty("api id")
    private Integer apiId;

    @ApiModelProperty("服务ID")
    private String serviceId;

    @Transient
    @ApiModelProperty("服务名")
    private String serviceName;

    @ApiModelProperty("api名")
    private String apiName;

    @ApiModelProperty("路径")
    private String path;

    @ApiModelProperty("请求方式")
    private String method;

    /** 是否匿名访问，默认不能匿名访问 */
    @ApiModelProperty("是否匿名访问")
    private boolean isAnnoymousAccess = false;

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private Integer status;

    @Transient
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private String statusName;

    @ApiModelProperty("审核结果")
    private String auditResult;

    @ApiModelProperty("原因")
    private String reason;

    @ApiModelProperty("申请类型，0 申请上线，1申请下线")
    private Integer applyType;

    @Transient
    @ApiModelProperty("申请类型名称")
    private String applyTypeName;

    @ApiModelProperty("申请时间")
    private Date applyDate;

    @ApiModelProperty("申请人")
    private String applyBy;

    @ApiModelProperty("审核时间")
    private Date auditDate;

    @ApiModelProperty("审核人")
    private String auditBy;

    public Integer getApiId() {
        return apiId;
    }

    public void setApiId(Integer apiId) {
        this.apiId = apiId;
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

    public String getStatusName() {
        if (this.status == null) {
            return null;
        }
        return API.getStatusName(this.status);
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getAuditResult() {
        return auditResult;
    }

    public void setAuditResult(Integer status) {
        this.auditResult = (status == 0 ? AUDIT_RESULT_SUCCESS : AUDIT_RESULT_FAIL);
    }

    public void setAuditResult(String auditResult) {
        this.auditResult = auditResult;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public Integer getApplyType() {
        return applyType;
    }

    public void setApplyType(Integer applyType) {
        this.applyType = applyType;
    }

    public String getApplyTypeName() {
        if (this.applyType == APPLY_TYPE_ONLINE) {
            return "申请上线";
        } else if (this.applyType == APPLY_TYPE_OFFLINE) {
            return "申请下线";
        }

        return applyTypeName;
    }

    public void setApplyTypeName(String applyTypeName) {
        this.applyTypeName = applyTypeName;
    }

    public String getApplyBy() {
        return applyBy;
    }

    public void setApplyBy(String applyBy) {
        this.applyBy = applyBy;
    }

    public Date getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(Date auditDate) {
        this.auditDate = auditDate;
    }

    public String getAuditBy() {
        return auditBy;
    }

    public void setAuditBy(String auditBy) {
        this.auditBy = auditBy;
    }

    public boolean isAnnoymousAccess() {
        return isAnnoymousAccess;
    }

    public void setAnnoymousAccess(boolean annoymousAccess) {
        isAnnoymousAccess = annoymousAccess;
    }
}
