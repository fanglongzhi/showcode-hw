package com.gmcc.msb.common.config;

import com.gmcc.msb.common.property.MsbProperties;
import com.gmcc.msb.common.util.SignUtils;
import com.gmcc.msb.common.vo.Constant;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author Yuan Chunhai
 * @Date 10/15/2018-4:14 PM
 */
@Configuration
@EnableFeignClients(basePackages = "com.gmcc.msb.common.service.client")
@ComponentScan(basePackages = "com.gmcc.msb.common")
@ConditionalOnProperty("msb.common.enabled")
public class CommonConfiguration {




    @Bean
    public RequestInterceptor requestInterceptor(MsbProperties msbProperties) {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                template.header(Constant.MSB_INNER_CALL, "true");
                template.header(Constant.SIGN, SignUtils.signFeignRequest(template, msbProperties.getCommonSignKey()));
            }
        };
    }

    @Bean("msbExecutorService")
    public ExecutorService executorService(MsbProperties properties) {
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(properties.getExecutorServicePoolSize(),
                new BasicThreadFactory.Builder().namingPattern("msb-pool-%d").daemon(true).build());
        return executorService;
    }


    @Bean("taskScheduler")
    public ExecutorService taskScheduler(MsbProperties properties) {
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(properties.getTaskSchedulerPoolSize(),
                new BasicThreadFactory.Builder().namingPattern("schedule-pool-%d").daemon(true).build());
        return executorService;
    }

}
