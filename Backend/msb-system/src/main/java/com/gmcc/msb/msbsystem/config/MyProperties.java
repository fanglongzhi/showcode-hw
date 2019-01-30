package com.gmcc.msb.msbsystem.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@ConfigurationProperties(prefix = MyProperties.PREFIX)
@Configuration
@RefreshScope
public class MyProperties {
    public static final String PREFIX = "msb.system";

    /**
     * sso token验证路径
     */
    private String ssoCheckTokenPath = "http://188.0.55.247:80/sso/oauth/check_token";

    /**
     * 请求更新的连接时间
     */
    private Integer requestRefreshConnectionTime = 10000;

    /**
     * 请求更新的读时间
     */
    private Integer requestRefreshReadTime = 30000;


    private Map<String, String> taskNames;


    public Integer getRequestRefreshConnectionTime() {
        return requestRefreshConnectionTime;
    }

    public void setRequestRefreshConnectionTime(Integer requestRefreshConnectionTime) {
        this.requestRefreshConnectionTime = requestRefreshConnectionTime;
    }

    public Integer getRequestRefreshReadTime() {
        return requestRefreshReadTime;
    }

    public void setRequestRefreshReadTime(Integer requestRefreshReadTime) {
        this.requestRefreshReadTime = requestRefreshReadTime;
    }

    public static String getPREFIX() {
        return PREFIX;
    }

    public String getSsoCheckTokenPath() {
        return ssoCheckTokenPath;
    }

    public void setSsoCheckTokenPath(String ssoCheckTokenPath) {
        this.ssoCheckTokenPath = ssoCheckTokenPath;
    }

    public Map<String, String> getTaskNames() {
        return taskNames;
    }

    public void setTaskNames(Map<String, String> taskNames) {
        this.taskNames = taskNames;
    }
}
