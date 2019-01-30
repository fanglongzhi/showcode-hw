package com.gmcc.msb.msbservice.task;

import com.gmcc.msb.msbservice.entity.ServiceLog;
import com.gmcc.msb.msbservice.service.ServiceLogService;
import com.gmcc.msb.msbservice.vo.ServiceLogResp;
import net.javacrumbs.shedlock.core.SchedulerLock;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
/**
 * @author yuan chunhai
 */
@Component
public class Tasks {

    private static final Logger LOGGER = LoggerFactory.getLogger(Tasks.class);
    public static final String LEFT_BRACKET = "(";


    private Date lastSyncTime;


    @Autowired
    ServiceLogService serviceLogService;

    /**
     * 同步服务注册、取消日志到数据库
     * 每隔90秒运行一次
     */
    @Scheduled(initialDelay = 60000, fixedDelay = 90000)
    @SchedulerLock(name = "syncServiceLogTask", lockAtMostFor = 90000, lockAtLeastFor = 90000)
    public void syncServiceLogTask() {

        LOGGER.info("同步服务日志");

        ServiceLogResp logs = null;

        try {
            logs = serviceLogService.getEurekaRegisterLog();
        } catch (Exception e) {
            LOGGER.error("同步服务日志失败,{}", e.getMessage());
        }

        if (logs != null) {
            saveLog(logs.getLastNCanceled(), 1);
            saveLog(logs.getLastNRegistered(), 0);

            lastSyncTime = new Date();
        }

    }


    /**
     * 每天1点、3点执行
     */
    @Scheduled(cron = "0 0 1,3 * * ?")
    public void resetLastSyncTime() {
        LOGGER.info("重置同步时间");
        lastSyncTime = null;
    }

    private void saveLog(List<ServiceLogResp.Data> datas, int type) {
        int count = 0;
        if (datas != null && !datas.isEmpty()) {
            LOGGER.debug("有 {} 条数据", datas.size());
            for (ServiceLogResp.Data data : datas) {
                ServiceLog save = save(data, type);
                if (save != null) {
                    count++;
                }
            }
        }
        if (count > 0) {
            if (type == 0) {
                LOGGER.info("同步服务日志，注册{}条", count);
            } else if (type == 1) {
                LOGGER.info("同步服务日志，取消{}条", count);
            }
        }

    }

    private ServiceLog save(ServiceLogResp.Data data, int type) {
        if (data == null || StringUtils.isEmpty(data.getId())) {
            LOGGER.error("错误数据{}", data);
            return null;
        }
        long date = data.getDate();
        String id = data.getId();
        String serviceId = "";
        if (id.contains(LEFT_BRACKET)) {
            serviceId = id.substring(0, id.indexOf('('));
        }
        if (this.lastSyncTime == null ||
                    DateUtils.addMinutes(this.lastSyncTime, -10).before(new Date(date))
                ) {
            return serviceLogService.add(new ServiceLog(date, type, serviceId, id));
        }
        return null;
    }
}
