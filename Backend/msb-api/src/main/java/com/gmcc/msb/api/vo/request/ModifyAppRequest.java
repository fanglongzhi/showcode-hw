package com.gmcc.msb.api.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author Zhi Fanglong
 */
@ApiModel
public class ModifyAppRequest {
    @NotEmpty
    @Length(min=1,max=100)
    @ApiModelProperty("应用名")
    private String appName;

    @NotEmpty
    @Length(min=1,max=1000)
    @ApiModelProperty("应用描述")
    private String description;

    @NotEmpty
    @Length(min=1,max=50)
    @ApiModelProperty("联系人")
    private String linkMan;

    @NotEmpty
    @Length(min=1,max=20)
    @ApiModelProperty("联系人电话")
    private String linkTel;

    @NotEmpty
    @Length(min=1,max=100)
    @ApiModelProperty("联系人邮箱")
    private String linkEmail;

    @NotEmpty
    @Length(min=1,max=100)
    @ApiModelProperty("开发商")
    private String company;

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
}
