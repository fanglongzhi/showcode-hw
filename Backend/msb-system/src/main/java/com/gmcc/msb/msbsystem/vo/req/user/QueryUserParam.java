package com.gmcc.msb.msbsystem.vo.req.user;

import com.gmcc.msb.msbsystem.common.PageInfo;
import com.gmcc.msb.msbsystem.common.UserStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

public class QueryUserParam {
    @ApiModelProperty("用户编号")
    private String operatorId;
    @ApiModelProperty("用户姓名")
    private String name;
    @ApiModelProperty("用户状态 VALID 启用 INVALID 停用")
    private UserStatus status;
    @ApiModelProperty("账户锁定状态 true 锁定 false 未锁定")
    private Boolean isLock;
    @ApiModelProperty(hidden = true)
    private PageInfo pageInfo;

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
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

    @ApiModel
    public static class ModifyUserParam {
        @ApiModelProperty("主键")
        private Long id;
        @ApiModelProperty("账户锁定状态 true 锁定 false 未锁定")
        private Boolean isLock;
        @ApiModelProperty("用户状态 VALID 启用 INVALID 停用")
        private UserStatus status;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Boolean getIsLock() {
            return isLock;
        }

        public void setIsLock(Boolean lock) {
            isLock = lock;
        }

        public UserStatus getStatus() {
            return status;
        }

        public void setStatus(UserStatus status) {
            this.status = status;
        }
    }
}
