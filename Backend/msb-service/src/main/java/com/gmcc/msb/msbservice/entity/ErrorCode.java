package com.gmcc.msb.msbservice.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author Yuan Chunhai
 * @Date 10/12/2018-2:23 PM
 */
@Entity
@Table(name = "t_error_code")
@ApiModel("错误码")
public class ErrorCode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty("服务ID")
    @NotNull(message = "0006-00010")
    private String serviceId;

    @ApiModelProperty("错误编码")
    @Length(max = 11)
    @NotEmpty(message = "0006-00011")
    private String code;

    @ApiModelProperty("错误消息")
    @Length(max = 100)
    @NotEmpty(message = "0006-00012")
    private String message;

    @ApiModelProperty("错误描述")
    @Length(max = 1000)
    private String description;

    @ApiModelProperty("创建时间")
    private Date createDate;

    @ApiModelProperty("更新时间")
    private Date updateDate;

    public ErrorCode() {

    }

    public ErrorCode(String code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
