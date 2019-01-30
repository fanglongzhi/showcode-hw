package com.gmcc.msb.zuul.tasks;

import com.gmcc.msb.common.vo.Constant;
import com.gmcc.msb.zuul.service.ApiService;
import com.gmcc.msb.zuul.service.AppOrderApiService;
import com.gmcc.msb.zuul.service.AppService;
import com.gmcc.msb.zuul.service.SignKeyCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author Yuan Chunhai
 * @Date 11/7/2018-11:58 AM
 */
@Service
public class MsbTask {

    private static final Logger log = LoggerFactory.getLogger(MsbTask.class);

    @Autowired
    SignKeyCache signKeyCache;

    @Autowired
    AppService appService;

    @Autowired
    ApiService apiService;

    @Autowired
    AppOrderApiService appOrderApiService;

    private boolean refreshSignKeyCacheSuccess = false;
    private boolean firstRefreshCacheSuccess = false;

    @Scheduled(fixedDelay = 3000, initialDelay = 1000)
    public String refreshSignKeyCache() {
        if (!refreshSignKeyCacheSuccess) {
            log.info("启动后获取签名配置");
            if (signKeyCache.refresh()) {
                refreshSignKeyCacheSuccess = true;
            } else {
                log.info("启动后获取签名配置失败");
            }
            return Constant.TASK_RUN;
        } else {
            return Constant.TASK_IGNORE;
        }
    }

    @Scheduled(fixedDelay = 6000, initialDelay = 1000)
    public String firstRefreshCache() {
        if (!firstRefreshCacheSuccess) {
            log.info("启动后刷新缓存");
            refreshCache();
            firstRefreshCacheSuccess = true;
            return Constant.TASK_RUN;
        } else {
            return Constant.TASK_IGNORE;
        }
    }


    @Scheduled(cron = "0 0 * * * ?")
    public void timingRefreshCache() {
        log.info("定时刷新缓存");
        refreshCache();
    }

    private void refreshCache() {

        appService.refreshAllCache();
        apiService.refreshAllCache();
        appOrderApiService.refreshAllCache();
    }


}
