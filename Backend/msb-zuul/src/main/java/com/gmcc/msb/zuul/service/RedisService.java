package com.gmcc.msb.zuul.service;

import com.gmcc.msb.common.property.RedisKeysProperties;
import com.gmcc.msb.zuul.entity.ApiInfo;
import com.gmcc.msb.zuul.entity.App;
import com.gmcc.msb.zuul.entity.AppOrderApi;
import com.gmcc.msb.zuul.properties.MsbZuulProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Yuan Chunhai
 */
@Service
public class RedisService {
    public static final String MONITORING_DATA = "gateway_monitoring_data";

    @Autowired
    MsbZuulProperties properties;

    @Autowired
    @Lazy
    RedisKeysProperties redisKeysProperties;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Resource(name = "stringRedisTemplate")
    ValueOperations<String, String> valueOperations;

    @Resource(name = "stringRedisTemplate")
    HashOperations<String, String, String> hashOperations;

    @Resource(name = "stringRedisTemplate")
    ListOperations<String, String> listOperations;

    @Resource(name = "apiInfoRedisTemplate")
    HashOperations<String, String, ApiInfo> apiInfoHashOperations;

    @Resource(name = "appInfoRedisTemplate")
    HashOperations<String, String, App> appHashOperations;

    @Resource(name = "appOrderApiRedisTemplate")
    HashOperations<String, String, AppOrderApi> appOrderApiHashOperations;

    public void logRequest(String requestLogStr) {
        listOperations.rightPush(redisKeysProperties.getApiRequestLog(), requestLogStr);
    }

    /**
     * 记录日志
     *
     * @param filterName
     * @param filterElapseTime
     */
    public void logFilterRequestTime(String filterName, int filterElapseTime) {
        hashOperations.increment("zuul_filter_log", filterName + "_count", 1);
        hashOperations.increment("zuul_filter_log", filterName + "_totalTime", filterElapseTime);
    }

    /**
     * 记录网关监控数据
     *
     * @param str
     */
    public void logGW(String str) {
        stringRedisTemplate.opsForList().rightPush(MONITORING_DATA, str);
    }

    /**
     * 获取服务下所有API
     */
    public List<ApiInfo> getServiceApis(String serviceId) {
        return apiInfoHashOperations.values(redisKeysProperties.getServiceApisPrefix() + serviceId);
    }

    /**
     * 检查token是否合法
     */
    public boolean userTokenValid(String appId, String username, String token) {
        String key = getUserKey(appId, username, redisKeysProperties.getSsoTokenCheck());
        String value = valueOperations.get(key);
        if (value != null && value.equals(token)) {
            setKeyExpire(key, properties.getUserTokenExpireSeconds());
            return true;
        } else {
            return false;
        }
    }

    private String getUserKey(String appId, String username, String ssoTokenCheck) {
        return appId + ":" + ssoTokenCheck + username;
    }

    public void setKeyExpire(String key, long timeout) {
        valueOperations.getOperations().expire(key, timeout, TimeUnit.SECONDS);
    }

    public boolean appTokenValid(String appId, String token) {

        String value = valueOperations.get(redisKeysProperties.getAppToken() + appId);
        if (value != null && value.equals(token)) {
            setKeyExpire(redisKeysProperties.getAppToken() + appId, properties.getAppTokenExpireSeconds());
            return true;
        } else {
            return false;
        }
    }


    /**
     * 根据appid获取应用信息
     *
     * @param appId
     * @return
     */
    public App getAppByAppId(String appId) {
        return appHashOperations.get(redisKeysProperties.getApps(), appId);
    }


    /**
     * 根据appid获取应用信息
     *
     * @param appId
     * @return
     */
    public List<AppOrderApi> getAppOrderApis(int appId, int apiId) {
        return appOrderApiHashOperations.values(redisKeysProperties.getAppOrderApi() + appId + "_" + apiId);
    }


}
