package com.gmcc.msb.common.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;


/**
 * @author Yuan Chunhai
 */
@RefreshScope
@ConfigurationProperties(prefix = RedisKeysProperties.PREFIX)
@Component
public class RedisKeysProperties {

    public static final String PREFIX = "msb.rediskeys";

    /**
     * 请求api记录
     */
    private String apiRequestLog = "request_log_list";

    /** 服务下所有api，前缀 */
    private String serviceApisPrefix = "service_apis:";

    /** 总请求数 */
    private String totalRequest = "total_request_count:";

    /** api请求数 */
    private String apiRequestCount = "api_request_count:";

    private String ssoTokenCheck = "sso_token_check:";

    private String appToken = "app_token:";

    /** 保存所有app信息 */
    private String apps = "apps:";
    private String app = "app:";
    private String appOrderApi = "app_order_api:";

    /** api请求数统计结果 */
    private String apiRequestCountStatics = "api_request_count_statics:";

    /**
     * api请求数统计结果
     */
    private String apiRequestTotalCountStaticsPrefix = "api_request_total_count_statics_";
    private String apiRequestSuccessCountStaticsPrefix = "api_request_success_count_statics_";
    private String apiRequestFailStaticsPrefix = "api_request_fail_count_statics_";

    public String getServiceApisPrefix() {
        return serviceApisPrefix;
    }

    public void setServiceApisPrefix(String serviceApisPrefix) {
        this.serviceApisPrefix = serviceApisPrefix;
    }

    public String getTotalRequest() {
        return totalRequest;
    }

    public void setTotalRequest(String totalRequest) {
        this.totalRequest = totalRequest;
    }

    public String getApiRequestCount() {
        return apiRequestCount;
    }

    public void setApiRequestCount(String apiRequestCount) {
        this.apiRequestCount = apiRequestCount;
    }


    public String getSsoTokenCheck() {
        return ssoTokenCheck;
    }

    public void setSsoTokenCheck(String ssoTokenCheck) {
        this.ssoTokenCheck = ssoTokenCheck;
    }

    public String getApiRequestLog() {
        return apiRequestLog;
    }

    public void setApiRequestLog(String apiRequestLog) {
        this.apiRequestLog = apiRequestLog;
    }

    public String getAppToken() {
        return appToken;
    }

    public void setAppToken(String appToken) {
        this.appToken = appToken;
    }

    public String getApiRequestCountStatics() {
        return apiRequestCountStatics;
    }

    public void setApiRequestCountStatics(String apiRequestCountStatics) {
        this.apiRequestCountStatics = apiRequestCountStatics;
    }

    public String getApiRequestTotalCountStaticsPrefix() {
        return apiRequestTotalCountStaticsPrefix;
    }

    public void setApiRequestTotalCountStaticsPrefix(String apiRequestTotalCountStaticsPrefix) {
        this.apiRequestTotalCountStaticsPrefix = apiRequestTotalCountStaticsPrefix;
    }

    public String getApiRequestSuccessCountStaticsPrefix() {
        return apiRequestSuccessCountStaticsPrefix;
    }

    public void setApiRequestSuccessCountStaticsPrefix(String apiRequestSuccessCountStaticsPrefix) {
        this.apiRequestSuccessCountStaticsPrefix = apiRequestSuccessCountStaticsPrefix;
    }

    public String getApiRequestFailStaticsPrefix() {
        return apiRequestFailStaticsPrefix;
    }

    public void setApiRequestFailStaticsPrefix(String apiRequestFailStaticsPrefix) {
        this.apiRequestFailStaticsPrefix = apiRequestFailStaticsPrefix;
    }

    public String getApps() {
        return apps;
    }

    public void setApps(String apps) {
        this.apps = apps;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getAppOrderApi() {
        return appOrderApi;
    }

    public void setAppOrderApi(String appOrderApi) {
        this.appOrderApi = appOrderApi;
    }
}
