package com.gmcc.msb.zuul.service;

import com.gmcc.msb.common.exception.MsbException;
import com.gmcc.msb.common.vo.Constant;
import com.gmcc.msb.zuul.entity.App;
import com.gmcc.msb.zuul.properties.MsbZuulProperties;
import com.gmcc.msb.zuul.repository.AppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Yuan Chunhai
 */
@Service
public class AppService {


    @Autowired
    private AppRepository appRepository;

    @Autowired
    private RedisService redisService;

    Map<String, App> cache = new ConcurrentHashMap<>(10);
    @Autowired
    private MsbZuulProperties properties;

    public App findByAppId(String clientId) {
        return this.appRepository.findOneByAppIdEquals(clientId);
    }

    public App findByAppIdFromRedis(String clientId) {
        return redisService.getAppByAppId(clientId);
    }

    public App findByAppIdFromCache(String clientId) {
        return cache.get(clientId);
    }

    public App isAppAvaiable(String appId) {

        App app;
        if (Constant.WAY_REDIS.equals(properties.getGetAppWay())) {
            app = this.findByAppIdFromRedis(appId);
        } else if (Constant.WAY_CACHE.equals(properties.getGetAppWay())) {
            app = this.findByAppIdFromCache(appId);
        } else {
            app = this.findByAppId(appId);
        }

        if (app == null) {
            throw new MsbException("0008-10012");
        }

        if (app.getStatus() == null || app.getStatus() != App.STATUS_AVAILABLE) {
            throw new MsbException("0008-10013");
        }

        return app;
    }

    public List<App> findAllAvaiable() {
        return this.appRepository.findAllByStatusEquals(App.STATUS_AVAILABLE);
    }


    public synchronized void refreshAllCache() {
        List<App> apps = this.findAllAvaiable();
        cache.clear();
        if (apps != null) {
            for (App app : apps) {
                cache.put(app.getAppId(), app);
            }
        }
    }
}
