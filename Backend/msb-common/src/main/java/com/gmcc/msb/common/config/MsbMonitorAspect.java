package com.gmcc.msb.common.config;

import com.gmcc.msb.common.property.MsbProperties;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.Executor;

/**
 * @author Yuan Chunhai
 * @Date 12/7/2018-1:55 PM
 */
@Aspect
@Component
@ConditionalOnClass(name = "org.springframework.data.redis.core.RedisTemplate")
@ConditionalOnProperty(value = "msb.enabledMonitor", havingValue = "true")
public class MsbMonitorAspect {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    MsbProperties msbProperties;

    @Resource(name = "msbExecutorService")
    Executor executor;


    @Pointcut("execution(public * com.gmcc.msb.*.service.*.*(..))")
    public void logService() {
    }

    @Pointcut("execution(public * com.gmcc.msb.*.repository.*.*(..))")
    public void logRepository() {
    }

    @Around("logService() || logRepository()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        long begin = System.currentTimeMillis();
        try {
            return pjp.proceed();
        } catch (Throwable throwable) {
            throw throwable;
        } finally {
            long end = System.currentTimeMillis();

            if (msbProperties.isEnabledMonitor()){
                String method =  pjp.getSignature().getDeclaringTypeName() + "." + pjp.getSignature().getName();
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        stringRedisTemplate.boundHashOps("msb.monitor").increment(method + "_count", 1);
                        stringRedisTemplate.boundHashOps("msb.monitor").increment(method + "_totalTime", (end - begin));
                    }
                });
            }
        }
    }

}
