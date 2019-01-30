package com.gmcc.msb.msbsystem.vo.req.user;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class CreateUserParam {
    @NotNull
    @ApiModelProperty("用户编号")
    private Long operatorId;//用户编号
    @NotNull
    @ApiModelProperty("用户ID")
    private String userId;//用户ID
    @NotNull
    @ApiModelProperty("登陆ID")
    private String loginId;//登陆ID
    @NotEmpty
    @ApiModelProperty("身份证号码")
    private String personCardNo;//身份证号码
    @Email
    @ApiModelProperty("电子邮件")
    private String email;//电子邮件
    @NotEmpty
    @ApiModelProperty("手机号码")
    private String mobile;//手机号码
    @NotNull
    @ApiModelProperty("组织机构ID")
    private Long orgId;//组织机构ID
    @NotEmpty
    @ApiModelProperty("用户姓名")
    private String name;//用户姓名

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPersonCardNo() {
        return personCardNo;
    }

    public void setPersonCardNo(String personCardNo) {
        this.personCardNo = personCardNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
