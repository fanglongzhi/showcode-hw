package com.gmcc.msb.api.task;

import com.gmcc.msb.api.service.SyncRedisService;
import com.gmcc.msb.common.vo.Constant;
import net.javacrumbs.shedlock.core.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Yuan Chunhai
 */
@Component
public class Tasks {

    private static final Logger LOGGER = LoggerFactory.getLogger(Tasks.class);

    @Autowired
    private SyncRedisService syncRedisService;

    private boolean firstSyncApiInfoDone = false;

    private boolean firstSyncAppInfoDone = false;
    private boolean firstSyncAppOrderApisInfoDone = false;

    /**
     * 同步api信息到redis
     * <p>
     * //每天2点、4点执行
     */
    @Scheduled(cron = "0 0 2,4 * * ?")
    @SchedulerLock(name = "syncApiInfos", lockAtMostFor = 90000, lockAtLeastFor = 90000)
    public void syncApiInfos() {
        LOGGER.info("同步api,apps,appOrderApis信息到redis");
        syncRedisService.syncApiInfo();
        syncRedisService.syncApps();
        syncRedisService.syncAppOrderApis();
    }

    @Scheduled(initialDelay = 5000, fixedDelay = 30000)
    @SchedulerLock(name = "firstSyncInfos", lockAtMostFor = 10000, lockAtLeastFor = 5000)
    public String firstSyncApiInfos() {
        if (!firstSyncApiInfoDone) {
            LOGGER.info("启动后，同步api信息到redis");
            syncRedisService.syncApiInfo();
            firstSyncApiInfoDone = true;
            return Constant.TASK_RUN;
        }
        else {
            return Constant.TASK_IGNORE;
        }
    }


    @Scheduled(initialDelay = 5000, fixedDelay = 30000)
    @SchedulerLock(name = "firstSyncAppInfos", lockAtMostFor = 10000, lockAtLeastFor = 5000)
    public String firstSyncAppInfos() {
        if (!firstSyncAppInfoDone) {
            LOGGER.info("启动后，同步app信息到redis");
            syncRedisService.syncApps();
            firstSyncAppInfoDone = true;
            return Constant.TASK_RUN;
        }
        else {
            return Constant.TASK_IGNORE;
        }
    }


    @Scheduled(initialDelay = 5000, fixedDelay = 30000)
    @SchedulerLock(name = "firstSyncAppOrderApis", lockAtMostFor = 10000, lockAtLeastFor = 5000)
    public String firstSyncAppOrderApis() {
        if (!firstSyncAppOrderApisInfoDone) {
            LOGGER.info("启动后，同步app订阅信息到redis");
            syncRedisService.syncAppOrderApis();
            firstSyncAppOrderApisInfoDone = true;
            return Constant.TASK_RUN;
        }
        else {
            return Constant.TASK_IGNORE;
        }
    }

}
