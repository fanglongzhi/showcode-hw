package com.gmcc.msb.api.entity;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 应用app订阅api
 */
@Entity
@Table(name = "t_app_order_api")
public class AppOrderApi {

    /**
     * 0:订阅申请中
     */
    public static final int APPLY = 0;
    /**
     * 1:订阅申请不通过
     */
    public static final int APPLY_FAIL = 1;
    /**
     * 2:已订阅
     */
    public static final int ORDERED = 2;
    /**
     * 3:已退订
     */
    public static final int ORDER_CANCELED = 3;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull(message = "0001-00021")
    private Integer appId;

    @NotNull(message = "0001-00016")
    @ApiModelProperty("API ID")
    private Integer apiId;

    @ApiModelProperty("开始时间")
    private Date startDate;

    @ApiModelProperty("结束时间")
    private Date endDate;

    /**
     * 0:订阅申请中、1:订阅申请不通过、2:已订阅、3:已退订
     */
    @ApiModelProperty("状态，0:订阅申请中、1:订阅申请不通过、2:已订阅、3:已退订")
    private Integer status;

    @ApiModelProperty("创建时间")
    private Date createDate;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("更新时间")
    private Date updateDate;

    @ApiModelProperty("更新人")
    private String updateBy;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
}
