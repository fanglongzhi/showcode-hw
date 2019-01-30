package com.gmcc.msb.config.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = MyProperties.PREFIX)
@Configuration
public class MyProperties {
    public static final String PREFIX = "msb.config";

    /**
     * 微服务上更新路由的路径
     */
    private String refreshPath = "refresh";

    /**
     * 请求更新的连接时间
     */
    private Integer requestRefreshConnectionTime = 5000;

    /**
     * 请求更新的读时间
     */
    private Integer requestRefreshReadTime = 10000;


    private String signKeyPropertyName = "msb.signKey";
    private String signKeyRefreshPath = "/refreshSignKeyCache";


    public String getRefreshPath() {
        return refreshPath;
    }

    public void setRefreshPath(String refreshPath) {
        this.refreshPath = refreshPath;
    }

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

    public String getSignKeyPropertyName() {
        return signKeyPropertyName;
    }

    public void setSignKeyPropertyName(String signKeyPropertyName) {
        this.signKeyPropertyName = signKeyPropertyName;
    }

    public String getSignKeyRefreshPath() {
        return signKeyRefreshPath;
    }

    public void setSignKeyRefreshPath(String signKeyRefreshPath) {
        this.signKeyRefreshPath = signKeyRefreshPath;
    }
}
