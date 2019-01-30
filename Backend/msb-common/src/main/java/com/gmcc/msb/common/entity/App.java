package com.gmcc.msb.common.entity;

import javax.persistence.Transient;
import java.util.HashMap;
import java.util.Map;

public class App  {

    public static final int STATUS_NEW = 0;
    public static final int STATUS_AVAILABLE = 1;
    public static final int STATUS_AUDITING = 2;
    public static final int STATUS_AUDIT_FAIL = 3;


    private static final Map<Integer, String> STATUS_NAMES = new HashMap<>();

    static {
        STATUS_NAMES.put(0, "新增");
        STATUS_NAMES.put(1, "生效");
        STATUS_NAMES.put(2, "审核中");
        STATUS_NAMES.put(3, "审核不通过");
    }


    private String appName;

    /**
     * 0:新增，1：生效，2：审核中，3：审核不通过
     */
    private Integer status;

    @Transient
    private String statusName;

    private String appId;

    private String appSecret;

    private String description;

    private String linkMan;

    private String linkTel;

    private String linkEmail;

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

    public String getStatusName() {
        return STATUS_NAMES.get(this.status);
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public static String getStatusName(int status) {
        return STATUS_NAMES.get(status);
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
