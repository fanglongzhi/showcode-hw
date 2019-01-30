package com.gmcc.msb.msbsystem.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Yuan Chunhai
 */
@Entity
@Table(name = "t_service_api")
@ApiModel
public class API {
    @Id
    @ApiModelProperty("api id")
    private Integer id;

    @ApiModelProperty("服务ID")
    private String serviceId;

    @ApiModelProperty("api名称")
    private String apiName;

    @ApiModelProperty("api路径")
    private String path;

    @ApiModelProperty("请求方法")
    private String method;

    /**
     * 0:新增，1:申请上线，2:已上线，3：上线审核不通过，4:申请下线，5：已下线，6：下线审核不通过
     */
    @ApiModelProperty("状态，0:新增，1:申请上线，2:已上线，3：上线审核不通过，4:申请下线，5：已下线，6：下线审核不通过")
    private Integer status;

    /** 是否匿名访问，默认不能匿名访问 */
    private boolean isAnnoymousAccess = false;

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

    public boolean isAnnoymousAccess() {
        return isAnnoymousAccess;
    }

    public void setAnnoymousAccess(boolean annoymousAccess) {
        isAnnoymousAccess = annoymousAccess;
    }
}
