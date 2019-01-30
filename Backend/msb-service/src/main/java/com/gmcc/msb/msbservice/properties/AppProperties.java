package com.gmcc.msb.msbservice.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import static com.gmcc.msb.msbservice.properties.AppProperties.PREFIX;

/**
 * @author Yuan Chunhai
 */
@Component
@ConfigurationProperties(prefix = PREFIX)
public class AppProperties {

    public static final String PREFIX = "msb.service";

    private Integer requestEurekaConnectionTime = 6000;

    private Integer requestEurekaReadTime = 6000;

    /** 请求eureka 服务注册、取消接口地址 */
    private String eurekaRegisterLogUrl = "http://localhost:8761/register_log";
    private long requestRefreshErrorCodeConnectionTime;
    private long requestRefreshErrorCodeReadTime;
    private String refreshErrorCodePath = "/refreshErrorCodes";

    public Integer getRequestEurekaConnectionTime() {
        return requestEurekaConnectionTime;
    }

    public void setRequestEurekaConnectionTime(Integer requestEurekaConnectionTime) {
        this.requestEurekaConnectionTime = requestEurekaConnectionTime;
    }

    public Integer getRequestEurekaReadTime() {
        return requestEurekaReadTime;
    }

    public void setRequestEurekaReadTime(Integer requestEurekaReadTime) {
        this.requestEurekaReadTime = requestEurekaReadTime;
    }

    public String getEurekaRegisterLogUrl() {
        return eurekaRegisterLogUrl;
    }

    public void setEurekaRegisterLogUrl(String eurekaRegisterLogUrl) {
        this.eurekaRegisterLogUrl = eurekaRegisterLogUrl;
    }

    public long getRequestRefreshErrorCodeConnectionTime() {
        return requestRefreshErrorCodeConnectionTime;
    }

    public void setRequestRefreshErrorCodeConnectionTime(long requestRefreshErrorCodeConnectionTime) {
        this.requestRefreshErrorCodeConnectionTime = requestRefreshErrorCodeConnectionTime;
    }

    public long getRequestRefreshErrorCodeReadTime() {
        return requestRefreshErrorCodeReadTime;
    }

    public void setRequestRefreshErrorCodeReadTime(long requestRefreshErrorCodeReadTime) {
        this.requestRefreshErrorCodeReadTime = requestRefreshErrorCodeReadTime;
    }

    public String getRefreshErrorCodePath() {
        return refreshErrorCodePath;
    }

    public void setRefreshErrorCodePath(String refreshErrorCodePath) {
        this.refreshErrorCodePath = refreshErrorCodePath;
    }
}
