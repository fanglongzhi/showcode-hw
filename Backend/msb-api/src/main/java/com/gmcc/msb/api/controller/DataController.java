package com.gmcc.msb.api.controller;

import com.gmcc.msb.api.service.ApiService;
import com.gmcc.msb.api.service.AppService;
import com.gmcc.msb.api.service.ServService;
import com.gmcc.msb.api.vo.response.UsageData;
import com.gmcc.msb.common.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统数据
 *
 * @author Yuan Chunhai
 * @Date 11/28/2018-5:46 PM
 */
@RestController
public class DataController {

    @Autowired
    private ApiService apiService;

    @Autowired
    private AppService appService;

    @Autowired
    private ServService servService;

    @GetMapping("/index_data")
    public Response<UsageData> getUsageData() {
        UsageData usageData = new UsageData();
        usageData.setApiCount(apiService.getAllOnlineApiCount());
        usageData.setAppCount(appService.getAllAvaAppCount());
        usageData.setServiceCount((int) servService.allServiceCount());
        return Response.ok(usageData);
    }
}
