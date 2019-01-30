package com.gmcc.msb.api.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author Zhi Fanglong
 */

@ApiModel
public class AppRequest {

    @NotEmpty
    @Length(min = 1, max = 100)
    @ApiModelProperty("应用名")
    private String appName;

    /**
     * 0:新增，1：生效，2：审核中，3：审核不通过
     */
    @ApiModelProperty("状态，0:新增，1：生效，2：审核中，3：审核不通过")
    private Integer status;
    @Length(max = 100)
    @ApiModelProperty("应用ID")
    private String appId;

    @Length(max = 200)
    @ApiModelProperty("应用秘钥")
    private String appSecret;

    @NotEmpty
    @Length(min = 1, max = 1000)
    @ApiModelProperty("应用描述")
    private String description;

    @NotEmpty
    @Length(min = 1, max = 50)
    @ApiModelProperty("联系人")
    private String linkMan;

    @NotEmpty
    @Length(min = 1, max = 20)
    @ApiModelProperty("联系人电话")
    private String linkTel;

    @NotEmpty
    @Length(min = 1, max = 100)
    @ApiModelProperty("联系人邮箱")
    private String linkEmail;

    @NotEmpty
    @Length(min = 1, max = 100)
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
