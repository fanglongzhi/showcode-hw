package com.gmcc.msb.zuul.service;

import com.gmcc.msb.common.vo.Constant;
import com.gmcc.msb.zuul.entity.AppOrderApi;
import com.gmcc.msb.zuul.properties.MsbZuulProperties;
import com.gmcc.msb.zuul.repository.AppOrderApiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * app订阅api服务
 *
 * @author Yuan Chunhai
 */
@Service
public class AppOrderApiService {

    @Autowired
    private AppOrderApiRepository repository;

    @Autowired
    private RedisService redisService;

    Map<String, List<AppOrderApi>> cache = new ConcurrentHashMap<>(500);
    @Autowired
    private MsbZuulProperties properties;

    public boolean isAppOrderApi(Integer appId, Integer apiId) {
        if (Constant.WAY_REDIS.equals(properties.getGetAppWay())) {
            return this.isAppOrderApiFromRedis(appId, apiId);
        } else if (Constant.WAY_CACHE.equals(properties.getGetAppWay())) {
            return this.isAppOrderApiFromCache(appId, apiId);
        } else {
            List<AppOrderApi> list = repository.findAllAvaiableOrder(appId, apiId);
            return list != null && !list.isEmpty();
        }
    }

    public boolean isAppOrderApiFromRedis(int appId, int apiId) {

        List<AppOrderApi> orders = redisService.getAppOrderApis(appId, apiId);
        return checkOrderTime(orders);
    }

    private boolean checkOrderTime(List<AppOrderApi> orders) {
        if (orders == null) {
            return false;
        }
        for (AppOrderApi appOrderApi : orders) {
            if (appOrderApi.getStatus() != null && AppOrderApi.ORDERED == appOrderApi.getStatus()) {
                Date date = new Date();
                if (appOrderApi.getStartDate() != null && appOrderApi.getStartDate().before(date)
                            && appOrderApi.getEndDate() != null && appOrderApi.getEndDate().after(date)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isAppOrderApiFromCache(int appId, int apiId) {
        List<AppOrderApi> orders = cache.get(appId + "_" + apiId);
        return checkOrderTime(orders);
    }

    public synchronized void refreshAllCache() {
        List<AppOrderApi> orders = this.repository.findAllOrders();
        cache.clear();
        if (orders != null) {
            for (AppOrderApi appOrderApi : orders) {
                String key = appOrderApi.getAppId() + "_" + appOrderApi.getApiId();
                List<AppOrderApi> list;
                if (cache.containsKey(key)) {
                    list = cache.get(key);
                } else {
                    list = new ArrayList<>();
                    cache.put(key, list);
                }
                list.add(appOrderApi);
            }
        }
    }

}
