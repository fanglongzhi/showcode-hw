package com.gmcc.msb.zuul.service;

import com.gmcc.msb.common.vo.Constant;
import com.gmcc.msb.zuul.entity.ApiInfo;
import com.gmcc.msb.zuul.properties.MsbZuulProperties;
import com.gmcc.msb.zuul.repository.ApiReposotiry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Yuan Chunhai
 * @Date 12/18/2018-3:01 PM
 */
@Service
public class ApiService {

    Map<String, List<ApiInfo>> cache = new ConcurrentHashMap<>(500);
    @Autowired
    private RedisService redisService;
    @Autowired
    private MsbZuulProperties msbZuulProperties;
    @Autowired
    private ApiReposotiry apiReposotiry;

    public List<ApiInfo> getServiceApis(String serviceId) {
        if (Constant.WAY_CACHE.equals(msbZuulProperties.getGetApisWay())) {
            return cache.get(serviceId);
        } else if (Constant.WAY_DB.equals(msbZuulProperties.getGetApisWay())) {
            List<ApiInfo> apiInfos = this.getServiceApisFromDB(serviceId);
            sortByPath(apiInfos);
            return apiInfos;
        } else {
            List<ApiInfo> apiInfos = redisService.getServiceApis(serviceId);
            sortByPath(apiInfos);
            return apiInfos;
        }
    }


    private List<ApiInfo> getServiceApisFromDB(String serviceId) {
        return this.apiReposotiry.findAllByServiceIdEqualsAndStatusEquals(serviceId, ApiInfo.STATUS_ONLINE);
    }

    public synchronized void refreshAllCache() {
        List<ApiInfo> all = this.apiReposotiry.findAllByStatusEquals(ApiInfo.STATUS_ONLINE);
        cache.clear();
        if (all != null) {
            for (ApiInfo apiInfo : all) {
                String key = apiInfo.getServiceId();
                List<ApiInfo> list;
                if (cache.containsKey(key)) {
                    list = cache.get(key);
                } else {
                    list = new ArrayList<>();
                    cache.put(key, list);
                }
                list.add(apiInfo);
            }
        }

        Collection<List<ApiInfo>> values = cache.values();
        for (List<ApiInfo> apiInfos : values) {
            sortByPath(apiInfos);
        }

    }

    private void sortByPath(List<ApiInfo> apiInfos) {
        if (apiInfos != null && !apiInfos.isEmpty()) {
            Collections.sort(apiInfos, new Comparator<ApiInfo>() {
                @Override
                public int compare(ApiInfo o1, ApiInfo o2) {
                    return o1.getPath().compareTo(o2.getPath());
                }
            });
        }
    }


}
