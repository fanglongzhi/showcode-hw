package com.gmcc.msb.zuul.controller;

import com.gmcc.msb.common.vo.Response;
import com.gmcc.msb.common.vo.SignKey;
import com.gmcc.msb.zuul.service.ApiService;
import com.gmcc.msb.zuul.service.AppOrderApiService;
import com.gmcc.msb.zuul.service.AppService;
import com.gmcc.msb.zuul.service.SignKeyCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Yuan Chunhai
 * @Date 11/6/2018-6:29 PM
 */
@RestController
public class MsbZuulController {

    @Autowired
    private SignKeyCache signKeyCache;

    @Autowired
    private AppOrderApiService appOrderApiService;

    @Autowired
    private AppService appService;

    @Autowired
    private ApiService apiService;

    @PostMapping("/refreshSignKeyCache")
    public Response refreshSignKeyCache(@RequestBody SignKey signKey) {
        signKeyCache.setSignKey(signKey.getServiceId(), signKey.getKey());
        return Response.ok();
    }

    @PostMapping("/refreshAllSignKeyCache")
    public Response refreshAllSignKeyCache() {
        signKeyCache.refresh();
        return Response.ok();
    }


    @PostMapping("/refreshAllAppOrdersCache")
    public Response refreshAllAppOrdersCache() {
        appOrderApiService.refreshAllCache();
        return Response.ok();
    }

    @PostMapping("/refreshAllAppsCache")
    public Response refreshAllAppCache() {
        appService.refreshAllCache();
        return Response.ok();
    }

    @PostMapping("/refreshAllApisCache")
    public Response refreshAllApisCache() {
        apiService.refreshAllCache();
        return Response.ok();
    }

    @PostMapping("/refreshAllCache")
    public Response refreshAllCache() {
        appService.refreshAllCache();
        apiService.refreshAllCache();
        appOrderApiService.refreshAllCache();
        return Response.ok();
    }
}
