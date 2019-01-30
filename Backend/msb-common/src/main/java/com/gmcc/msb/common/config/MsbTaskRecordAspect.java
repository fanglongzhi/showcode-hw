package com.gmcc.msb.common.config;

import com.gmcc.msb.common.service.client.MsbSystemClient;
import com.gmcc.msb.common.vo.Constant;
import com.gmcc.msb.common.vo.request.TaskLogRequest;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Yuan Chunhai
 * @Date 12/9/2018-2:26 PM
 */
@Aspect
@Component
public class MsbTaskRecordAspect {

    private static final Logger log = LoggerFactory.getLogger(MsbTaskRecordAspect.class);

    @Value("${spring.application.name}")
    private String applicationName;

    @Autowired
    @Lazy
    MsbSystemClient msbSystemClient;


    @Pointcut("execution(public * com.gmcc.msb.*.task.*.*(..))")
    public void recordTask() {
    }


    @Around("recordTask()")
    public Object recordTaskLog(ProceedingJoinPoint proceedingJoinPoint) {
        String method = proceedingJoinPoint.getSignature().getDeclaringTypeName() + "." + proceedingJoinPoint.getSignature().getName();

        int runStatus = TaskLogRequest.SUCCESS;
        String errorMessage = null;
        long begin = System.currentTimeMillis();
        Object retVal = null;
        try {
            retVal = proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            errorMessage = ExceptionUtils.getFullStackTrace(throwable);
            runStatus = TaskLogRequest.FAIL;
            log.error("定时任务执行失败，{}", ExceptionUtils.getMessage(throwable));
        } finally {
            long end = System.currentTimeMillis();

            if (retVal == null ||
                        (retVal != null && retVal instanceof String
                                 && !retVal.toString().equals(Constant.TASK_IGNORE))) {

                TaskLogRequest taskLog = new TaskLogRequest();
                taskLog.setServiceId(applicationName);
                taskLog.setTaskKey(method);
                taskLog.setRunTime(new Date());
                taskLog.setRunStatus(runStatus);
                taskLog.setErrorMessage(errorMessage);
                taskLog.setElasped((int) (end - begin));
                try{
                    msbSystemClient.addTaskLog(taskLog);
                }
                catch (Exception e){
                    log.error("记录定时任务失败,{}" ,ExceptionUtils.getMessage(e));
                }
            }
        }

        return retVal;
    }

}
