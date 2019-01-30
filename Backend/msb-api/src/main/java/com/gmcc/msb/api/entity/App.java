package com.gmcc.msb.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.gmcc.msb.api.vo.request.AppRequest;
import com.gmcc.msb.common.entity.ModelParent;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "t_app")
@ApiModel
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class App extends ModelParent{

    public static final int STATUS_NEW = 0;
    public static final int STATUS_AVAILABLE = 1;
    public static final int STATUS_AUDITING = 2;
    public static final int STATUS_AUDIT_FAIL = 3;


    private static final Map<Integer, String> STATUS_NAMES = new HashMap<>();

    static {
        STATUS_NAMES.put(0, "新增");
        STATUS_NAMES.put(1, "生效");
        STATUS_NAMES.put(2, "审核中");
        STATUS_NAMES.put(3, "审核不通过");
    }

    public App(){}
    public App(AppRequest appRequest){
        this.appId = appRequest.getAppId();
        this.appName = appRequest.getAppName();
        this.appSecret = appRequest.getAppSecret();
        this.company = appRequest.getCompany();
        this.linkEmail = appRequest.getLinkEmail();
        this.linkMan = appRequest.getLinkMan();
        this.linkTel = appRequest.getLinkTel();
        this.description = appRequest.getDescription();
    }

    @ApiModelProperty("应用名")
    private String appName;

    /**
     * 0:新增，1：生效，2：审核中，3：审核不通过
     */
    @ApiModelProperty("状态，0:新增，1：生效，2：审核中，3：审核不通过")
    private Integer status;

    @Transient
    @ApiModelProperty("状态名")
    private String statusName;

    @ApiModelProperty("应用ID")
    private String appId;

    @ApiModelProperty("应用秘钥")
    private String appSecret;

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

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusName() {
        return STATUS_NAMES.get(this.status);
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public static String getStatusName(int status) {
        return STATUS_NAMES.get(status);
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
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


}
