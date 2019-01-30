package com.gmcc.msb.common.config;

import com.gmcc.msb.common.filter.FetchHeaderFilter;
import com.gmcc.msb.common.filter.SignCheckFilter;
import com.gmcc.msb.common.property.MsbProperties;
import com.gmcc.msb.common.service.ErrorCodeCacheService;
import com.gmcc.msb.common.service.client.MsbSystemClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author Yuan Chunhai
 * @Date 9/28/2018-6:06 PM
 */
@Configuration
@ConditionalOnProperty("msb.common.enabled")
public class WebConfiguation extends WebMvcConfigurerAdapter {


    @Value("${spring.application.name}")
    private String applicationName;

    @Autowired
    private MsbProperties msbProperties;

    @Autowired
    @Lazy
    private ErrorCodeCacheService errorCodeCacheService;

    @Autowired
    @Lazy
    private MsbSystemClient msbSystemClient;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SignCheckFilter(msbProperties, errorCodeCacheService,applicationName));
        registry.addInterceptor(new FetchHeaderFilter(msbSystemClient,applicationName));
    }
}
