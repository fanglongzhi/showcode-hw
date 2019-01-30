package com.gmcc.msb.msbsystem.vo.req.org;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @program: msb-system
 * @description: 创建数据组用户关系参数
 * @author: zhifanglong
 * @create: 2018-10-30 12:15
 */
public class CreateDataOrgUserParam {
    @ApiModelProperty("数据组")
    @NotNull(message = "0007-00003")
    private Integer orgId;
    @ApiModelProperty("用户信息列表，用户的userId列表")
    private List<String> userIdList;
    @ApiModelProperty("主组标志，1表示主组，0表示非主组")
    @NotEmpty(message = "0007-00004")
    private String mainFlag;

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public List<String> getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(List<String> userIdList) {
        this.userIdList = userIdList;
    }

    public String getMainFlag() {
        return mainFlag;
    }

    public void setMainFlag(String mainFlag) {
        this.mainFlag = mainFlag;
    }
}
