package com.gmcc.msb.api.entity;

import com.gmcc.msb.common.entity.ModelParent;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 应用app订阅api
 */
@Entity
@Table(name = "t_app_order_api_audit")
@ApiModel
public class AppOrderApiAudit extends ModelParent {

    public static final String AUDIT_RESULT_SUCCESS = "通过";
    public static final String AUDIT_RESULT_FAIL = "不通过";

    @NotNull(message = "0001-00022")
    @ApiModelProperty("订阅id")
    private Integer orderId;

    /**APP 信息 start*/
    @NotNull(message = "0001-00021")
    @ApiModelProperty("应用id")
    private Integer appId;

    @ApiModelProperty("应用名")
    private String appName;

    @ApiModelProperty("应用描述")
    private String description;

    @ApiModelProperty("联系人")
    private String linkMan;
    @ApiModelProperty("联系人电话")
    private String linkTel;

    @ApiModelProperty("联系人邮箱")
    private String linkEmail;

    @ApiModelProperty("开发商")
    private String company;

    @ApiModelProperty("应用添加时间")
    private Date appCreateTime;
    /**APP 信息 end*/

    /**api 信息 start*/
    @NotNull(message = "0001-00016")
    @ApiModelProperty("API ID")
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
    @ApiModelProperty("是否能匿名访问")
    private boolean isAnnoymousAccess = false;

    /**api 信息 end*/

    @ApiModelProperty("开始时间")
    private Date startDate;

    @ApiModelProperty("结束时间")
    private Date endDate;

    @ApiModelProperty("申请类型，0 申请订阅")
    private Integer applyType = 0;

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

    @ApiModelProperty("审核结果")
    private String auditResult;

    @ApiModelProperty("原因")
    private String reason;

    @ApiModelProperty("更新时间")
    private Date updateDate;


    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public Integer getApiId() {
        return apiId;
    }

    public void setApiId(Integer apiId) {
        this.apiId = apiId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
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

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getApplyType() {
        return applyType;
    }

    public void setApplyType(Integer applyType) {
        this.applyType = applyType;
    }

    public String getApplyTypeName() {
        //暂时只有申请订阅
        return this.applyType == 0 ? "申请订阅" : "";
    }

    public void setApplyTypeName(String applyTypeName) {
        this.applyTypeName = applyTypeName;
    }

    public String getAuditResult() {
        return auditResult;
    }

    public void setAuditResult(String auditResult) {
        this.auditResult = auditResult;
    }

    public void setAuditResultSuccess(){
        this.auditResult = AUDIT_RESULT_SUCCESS;
    }

    public void setAuditResultFail(){
        this.auditResult = AUDIT_RESULT_FAIL;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }

    public String getLinkTel() {
        return linkTel;
    }

    public void setLinkTel(String linkTel) {
        this.linkTel = linkTel;
    }

    public String getLinkEmail() {
        return linkEmail;
    }

    public void setLinkEmail(String linkEmail) {
        this.linkEmail = linkEmail;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Date getAppCreateTime() {
        return appCreateTime;
    }

    public void setAppCreateTime(Date appCreateTime) {
        this.appCreateTime = appCreateTime;
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

    public boolean isAnnoymousAccess() {
        return isAnnoymousAccess;
    }

    public void setAnnoymousAccess(boolean annoymousAccess) {
        isAnnoymousAccess = annoymousAccess;
    }
}
