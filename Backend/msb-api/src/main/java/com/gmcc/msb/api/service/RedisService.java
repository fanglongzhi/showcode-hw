package com.gmcc.msb.api.service;

import com.gmcc.msb.api.entity.App;
import com.gmcc.msb.api.entity.AppOrderApi;
import com.gmcc.msb.api.vo.ApiInfo;
import com.gmcc.msb.common.property.RedisKeysProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @author Yuan Chunhai
 */
@Service
public class RedisService {

    @Autowired
    private RedisKeysProperties redisKeysProperties;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Resource(name = "stringRedisTemplate")
    private ValueOperations<String, String> valueOperations;

    @Resource(name = "stringRedisTemplate")
    private HashOperations<String, String, String> hashOperations;

    @Resource(name = "stringRedisTemplate")
    private ListOperations<String, String> listOperations;

    @Resource(name = "apiInfoRedisTemplate")
    private HashOperations<String, String, ApiInfo> apiInfoHashOperations;

    @Resource(name = "appInfoRedisTemplate")
    private HashOperations<String, String, App> appInfoHashOperations;

    @Resource(name = "appOrderApiRedisTemplate")
    private HashOperations<String, String, AppOrderApi> appOrderApiHashOperations;

    /**
     * 添加API信息
     */
    public void addApiInfo(ApiInfo apiInfo) {
        apiInfoHashOperations.put(redisKeysProperties.getServiceApisPrefix() + apiInfo.getServiceId(),
                apiInfo.getId().toString(), apiInfo);
    }

    /**
     * 删除服务下api
     */
    public void deleteApiInfo(ApiInfo apiInfo) {
        apiInfoHashOperations.delete(redisKeysProperties.getServiceApisPrefix() + apiInfo.getServiceId(), apiInfo.getId().toString());
    }

    /**
     * 通过服务id删除下面所有服务
     * @param serviceId
     */
    public void deleteAllApiInfo(String serviceId) {
        apiInfoHashOperations.getOperations().delete(redisKeysProperties.getServiceApisPrefix() + serviceId);
    }

    /**
     * 获取服务下所有API
     */
    public List<ApiInfo> getServiceApis(String serviceId) {
        return apiInfoHashOperations.values(redisKeysProperties.getServiceApisPrefix() + serviceId);
    }


    public void saveApp(App app){
        appInfoHashOperations.put(redisKeysProperties.getApps(), app.getAppId(),  app);
    }

    public void delApp(App app){
        appInfoHashOperations.delete(redisKeysProperties.getApps(), app.getAppId());
    }

    public void delAllApps(){
        stringRedisTemplate.delete(redisKeysProperties.getApps());
    }

    public void addAppOrderApi(AppOrderApi appOrderApi){
        String key = getAppOrderApiKey(appOrderApi);
        appOrderApiHashOperations.put(key, appOrderApi.getId().toString(), appOrderApi);
    }

    private String getAppOrderApiKey(AppOrderApi appOrderApi) {
        return getAppOrderApiKey(appOrderApi.getAppId(), appOrderApi.getApiId());
    }

    private String getAppOrderApiKey(int appId, int apiId) {
        return redisKeysProperties.getAppOrderApi() + appId + "_" + apiId;
    }

    public void removeAllAppOrderApi(App app){
        Set<String> keys = appOrderApiHashOperations.getOperations().keys(
                redisKeysProperties.getAppOrderApi() + app.getId()+ "_*");
        for(String key : keys){
            appOrderApiHashOperations.getOperations().delete(key);
        }
    }


    public void deleteAppOrderApi(AppOrderApi one) {
        appOrderApiHashOperations.delete(getAppOrderApiKey(one), one.getId().toString());
    }
}
