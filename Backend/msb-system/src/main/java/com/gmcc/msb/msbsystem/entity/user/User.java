package com.gmcc.msb.msbsystem.entity.user;

import com.gmcc.msb.msbsystem.common.UserStatus;
import com.gmcc.msb.msbsystem.vo.req.user.CreateUserParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="t_user")
@ApiModel
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ApiModelProperty("用户编号")
    private Long operatorId;//用户编号
    @ApiModelProperty("用户ID")
    private String userId;//用户ID
    @ApiModelProperty("登陆ID")
    private String loginId;//登陆ID
    @ApiModelProperty("身份证号码")
    private String personCardNo;//身份证号码
    @ApiModelProperty("电子邮件")
    private String email;//电子邮件
    @ApiModelProperty("手机号码")
    private String mobile;//手机号码
    @ApiModelProperty("组织机构ID")
    private Long orgId;//组织机构ID
    @ApiModelProperty("组织机构名称")
    private String orgName;//组织机构名称
    @ApiModelProperty("用户姓名")
    private String name;//用户姓名
    @ApiModelProperty("用户状态 VALID 启用 INVALID 停用")
    private UserStatus status;
    @ApiModelProperty("账户锁定状态 true 锁定 false 未锁定")
    private Boolean isLock;
    @ApiModelProperty("数据组ID")
    @Transient
    private Integer dataOrgId;
    @ApiModelProperty("数据组名称")
    @Transient
    private String dataOrgName;

    private Date createTime;

    private Date updateTime;


    public User(){}
    public User(CreateUserParam param){
        this.operatorId=param.getOperatorId();
        this.userId=param.getUserId();
        this.loginId=param.getLoginId();
        this.personCardNo=param.getPersonCardNo();
        this.email=param.getEmail();
        this.mobile=param.getMobile();
        this.orgId=param.getOrgId();
        this.name=param.getName();
    }

    public User(Long id, Long operatorId, String userId, String loginId, String personCardNo, String email, String mobile, Long orgId, String orgName, String name, UserStatus status, Boolean isLock, Integer dataOrgId, String dataOrgName, Date createTime, Date updateTime) {
        this.id = id;
        this.operatorId = operatorId;
        this.userId = userId;
        this.loginId = loginId;
        this.personCardNo = personCardNo;
        this.email = email;
        this.mobile = mobile;
        this.orgId = orgId;
        this.orgName = orgName;
        this.name = name;
        this.status = status;
        this.isLock = isLock;
        this.dataOrgId = dataOrgId;
        this.dataOrgName = dataOrgName;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public Boolean getIsLock() {
        return isLock;
    }

    public void setIsLock(Boolean lock) {
        isLock = lock;
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

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getDataOrgId() {
        return dataOrgId;
    }

    public void setDataOrgId(Integer dataOrgId) {
        this.dataOrgId = dataOrgId;
    }

    public String getDataOrgName() {
        return dataOrgName;
    }

    public void setDataOrgName(String dataOrgName) {
        this.dataOrgName = dataOrgName;
    }
}
