package com.gmcc.msb.zuul.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Yuan Chunhai
 */
@RefreshScope
@ConfigurationProperties(prefix = MsbZuulProperties.PREFIX)
@Component
public class MsbZuulProperties {

    public static final String PREFIX = "msb.zuul";

    private static final String ROUTE_SELECT_SQL = "select * from t_service_route";

    private static final String BREAKER_SELECT_SQL = "select * from t_service_breaker";

    private static final String API_SELECT_SQL = "select * from t_service_api";

    private static final String BREAKER_KEY = "property_key";

    private static final String BREAKER_VALUE = "property_value";

    /**
     * 需要进行API访问权限验证的应用ID列表
     */
    private List<String> msbFrontClientIds = new ArrayList<>();

    /**
     * 查询路由的sql
     */
    private String routeSelectSql = ROUTE_SELECT_SQL;

    /**
     * 查询api
     */
    private String apiSelectSql = API_SELECT_SQL;

    /**
     * 查询熔断配置
     */
    private String breakerSelectSql = BREAKER_SELECT_SQL;

    /**
     * 熔断配置字段KEY名称
     */
    private String breakerKey = BREAKER_KEY;

    /**
     * 熔断配置字段value名称
     */
    private String breakerValue = BREAKER_VALUE;

    /**
     * 认为请求失败的httpstatuscode返回码
     */
    private List<Integer> failStatusCodes =
            Arrays.asList(400, 403, 404, 405, 406, 407, 408, 500,
                    501, 502, 503, 504, 505, 506, 507, 509, 510
            );


    private List<String> skipCheckTokenUri = Arrays.asList("msb-auth:/oauth/token"
            , "msb-auth:/oauth/sso_login_check");

    /**
     * 用于sso token加密验证的key
     */
    @Deprecated
    private String ssoTokenSignKey = "123";

    /**
     * sso 使用rsa的方式加密
     */
    private String rasPublikKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCUiaxToKuP+IU4UZ4sg3J5QS0IadGYo/vo3FmWMfIlu6mw4XRX4dPm6Sl/u9rDm4uJYeXwq7OuMd1porRmER4b32RQ/NK3TFx3l+aRNXqoFHrAnJjbjkffvAdTQLBDAE2HXM+IVioxuH7rl/KFf1lbnBhJLyGNBTDOUlihngwwkQIDAQAB";

    /**
     * 在header中app id的key
     */
    private String headerAppIdKey = "app-id";


    private boolean skipCheckTokenPreFilter = false;
    private boolean skipCheckAppStatusPreFilter = false;
    private boolean skipCheckAppOrderApiPreFilter = false;
    private boolean skipCheckApiStatusFilter = false;
    private boolean skipCheckApiAccessPreFilter = false;
    private boolean skipSignPreFilter = false;
    private boolean skipRateLimitFilter = false;
    private boolean skipMonitorPostFilter = true;


    /**
     * 获取sign配置时，使用的propertyname
     */
    private String signKeyPropertyName = "msb.signKey";

    /** token过期时间，秒 */
    private long userTokenExpireSeconds = 1800;

    /** app鉴权token过期时间，秒 */
    private long appTokenExpireSeconds = 1800;

    /**
     * db,cache,redis
     */
    private String getAppWay = "db";
    private String getAppOrderApiWay = "db";
    private String getApisWay = "redis";


    public String getRouteSelectSql() {
        return routeSelectSql;
    }

    public void setRouteSelectSql(String routeSelectSql) {
        this.routeSelectSql = routeSelectSql;
    }

    public String getBreakerSelectSql() {
        return breakerSelectSql;
    }

    public void setBreakerSelectSql(String breakerSelectSql) {
        this.breakerSelectSql = breakerSelectSql;
    }

    public String getBreakerKey() {
        return breakerKey;
    }

    public void setBreakerKey(String breakerKey) {
        this.breakerKey = breakerKey;
    }

    public String getBreakerValue() {
        return breakerValue;
    }

    public void setBreakerValue(String breakerValue) {
        this.breakerValue = breakerValue;
    }

    public String getApiSelectSql() {
        return apiSelectSql;
    }

    public void setApiSelectSql(String apiSelectSql) {
        this.apiSelectSql = apiSelectSql;
    }

    public List<Integer> getFailStatusCodes() {
        return failStatusCodes;
    }

    public void setFailStatusCodes(List<Integer> failStatusCodes) {
        this.failStatusCodes = failStatusCodes;
    }

    public List<String> getSkipCheckTokenUri() {
        return skipCheckTokenUri;
    }

    public void setSkipCheckTokenUri(List<String> skipCheckTokenUri) {
        this.skipCheckTokenUri = skipCheckTokenUri;
    }

    public String getSsoTokenSignKey() {
        return ssoTokenSignKey;
    }

    public void setSsoTokenSignKey(String ssoTokenSignKey) {
        this.ssoTokenSignKey = ssoTokenSignKey;
    }

    public String getHeaderAppIdKey() {
        return headerAppIdKey;
    }

    public void setHeaderAppIdKey(String headerAppIdKey) {
        this.headerAppIdKey = headerAppIdKey;
    }

    public boolean isSkipCheckTokenPreFilter() {
        return skipCheckTokenPreFilter;
    }

    public void setSkipCheckTokenPreFilter(boolean skipCheckTokenPreFilter) {
        this.skipCheckTokenPreFilter = skipCheckTokenPreFilter;
    }

    public boolean isSkipCheckAppStatusPreFilter() {
        return skipCheckAppStatusPreFilter;
    }

    public void setSkipCheckAppStatusPreFilter(boolean skipCheckAppStatusPreFilter) {
        this.skipCheckAppStatusPreFilter = skipCheckAppStatusPreFilter;
    }

    public boolean isSkipCheckAppOrderApiPreFilter() {
        return skipCheckAppOrderApiPreFilter;
    }

    public void setSkipCheckAppOrderApiPreFilter(boolean skipCheckAppOrderApiPreFilter) {
        this.skipCheckAppOrderApiPreFilter = skipCheckAppOrderApiPreFilter;
    }

    public boolean isSkipCheckApiStatusFilter() {
        return skipCheckApiStatusFilter;
    }

    public void setSkipCheckApiStatusFilter(boolean skipCheckApiStatusFilter) {
        this.skipCheckApiStatusFilter = skipCheckApiStatusFilter;
    }

    public boolean isSkipCheckApiAccessPreFilter() {
        return skipCheckApiAccessPreFilter;
    }

    public void setSkipCheckApiAccessPreFilter(boolean skipCheckApiAccessPreFilter) {
        this.skipCheckApiAccessPreFilter = skipCheckApiAccessPreFilter;
    }

    public String getSignKeyPropertyName() {
        return signKeyPropertyName;
    }

    public void setSignKeyPropertyName(String signKeyPropertyName) {
        this.signKeyPropertyName = signKeyPropertyName;
    }

    public boolean getSkipSignPreFilter() {
        return skipSignPreFilter;
    }

    public void setSkipSignPreFilter(boolean skipSignPreFilter) {
        this.skipSignPreFilter = skipSignPreFilter;
    }

    public void setUserTokenExpireSeconds(long userTokenExpireSeconds) {
        this.userTokenExpireSeconds = userTokenExpireSeconds;
    }

    public long getUserTokenExpireSeconds() {
        return userTokenExpireSeconds;
    }

    public long getAppTokenExpireSeconds() {
        return appTokenExpireSeconds;
    }

    public void setAppTokenExpireSeconds(long appTokenExpireSeconds) {
        this.appTokenExpireSeconds = appTokenExpireSeconds;
    }

    public List<String> getMsbFrontClientIds() {
        return msbFrontClientIds;
    }

    public void setMsbFrontClientIds(List<String> msbFrontClientIds) {
        this.msbFrontClientIds = msbFrontClientIds;
    }

    public String getRasPublikKey() {
        return rasPublikKey;
    }

    public void setRasPublikKey(String rasPublikKey) {
        this.rasPublikKey = rasPublikKey;
    }

    public String getGetAppWay() {
        return getAppWay;
    }

    public void setGetAppWay(String getAppWay) {
        this.getAppWay = getAppWay;
    }

    public String getGetAppOrderApiWay() {
        return getAppOrderApiWay;
    }

    public void setGetAppOrderApiWay(String getAppOrderApiWay) {
        this.getAppOrderApiWay = getAppOrderApiWay;
    }

    public String getGetApisWay() {
        return getApisWay;
    }

    public void setGetApisWay(String getApisWay) {
        this.getApisWay = getApisWay;
    }

    public boolean isSkipRateLimitFilter() {
        return skipRateLimitFilter;
    }

    public void setSkipRateLimitFilter(boolean skipRateLimitFilter) {
        this.skipRateLimitFilter = skipRateLimitFilter;
    }

    public boolean isSkipMonitorPostFilter() {
        return skipMonitorPostFilter;
    }

    public void setSkipMonitorPostFilter(boolean skipMonitorPostFilter) {
        this.skipMonitorPostFilter = skipMonitorPostFilter;
    }
}
