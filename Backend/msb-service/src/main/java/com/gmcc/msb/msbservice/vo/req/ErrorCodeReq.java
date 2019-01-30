package com.gmcc.msb.msbservice.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * @author Yuan Chunhai
 * @Date 10/12/2018-6:08 PM
 */
@ApiModel
public class ErrorCodeReq {

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
}
