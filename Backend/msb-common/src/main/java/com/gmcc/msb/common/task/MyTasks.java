package com.gmcc.msb.common.task;

import com.gmcc.msb.common.service.ErrorCodeCacheService;
import com.gmcc.msb.common.service.impl.ErrorCodeCacheServiceImpl;
import com.gmcc.msb.common.vo.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;

/**
 * @author Yuan Chunhai
 * @Date 11/9/2018-5:40 PM
 */
@Service
public class MyTasks {

    private static final Logger log = LoggerFactory.getLogger(ErrorCodeCacheServiceImpl.class);

    @Resource(name = "msbExecutorService")
    ExecutorService executorService;

    @Autowired
    ErrorCodeCacheService errorCodeCacheService;

    private boolean getErrorCodeSuccess = false;

    @Scheduled(fixedDelay = 30000, initialDelay = 10000)
    public String initErrorCode() {
        if (!getErrorCodeSuccess) {
            errorCodeCacheService.refreshCache();
            log.info("获取错误码成功");
            getErrorCodeSuccess = true;
            return Constant.TASK_RUN;
        } else {
            return Constant.TASK_IGNORE;
        }
    }

}
