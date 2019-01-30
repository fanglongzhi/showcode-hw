package com.gmcc.msb.api.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gmcc.msb.common.entity.ModelParent;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Date;

import static com.gmcc.msb.api.entity.ApiAudit.AUDIT_RESULT_FAIL;
import static com.gmcc.msb.api.entity.ApiAudit.AUDIT_RESULT_SUCCESS;

@Entity
@Table(name = "t_app_audit")
@ApiModel
public class AppAudit extends ModelParent{
    @ApiModelProperty(value = "应用主键", hidden = true)
    @JsonIgnore
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

    @ApiModelProperty("审核结果")
    private String auditResult;

    @ApiModelProperty("原因")
    private String reason;

    @ApiModelProperty("申请时间")
    private Date applyDate;

    @ApiModelProperty("申请人")
    private String applyBy;

    @ApiModelProperty("审核时间")
    private Date auditDate;

    @ApiModelProperty("审核人")
    private String auditBy;

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


    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAuditResult() {
        return auditResult;
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

    public void setAuditResult(Integer result) {
        this.auditResult = (result == 0 ? AUDIT_RESULT_SUCCESS : AUDIT_RESULT_FAIL);
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

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public Date getAppCreateTime() {
        return appCreateTime;
    }

    public void setAppCreateTime(Date appCreateTime) {
        this.appCreateTime = appCreateTime;
    }
}
