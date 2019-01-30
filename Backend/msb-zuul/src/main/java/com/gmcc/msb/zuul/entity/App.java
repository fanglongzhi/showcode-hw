package com.gmcc.msb.zuul.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Yuan Chunhai
 */
@Entity()
@Table(name = "t_app")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class App {

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

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String appName;

    /**
     * 0:新增，1：生效，2：审核中，3：审核不通过
     */
    private Integer status;

    private String appId;

    private String appSecret;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

}
