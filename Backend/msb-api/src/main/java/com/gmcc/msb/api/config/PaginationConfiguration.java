package com.gmcc.msb.api.config;

import com.gmcc.msb.api.property.ApiProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.config.SpringDataWebConfiguration;

/**
 * @author Yuan Chunhai
 */
@Configuration
public class PaginationConfiguration extends SpringDataWebConfiguration {

    @Autowired
    private ApiProperties properties;

    @Bean
    @RefreshScope
    @Override
    public PageableHandlerMethodArgumentResolver pageableResolver() {
        PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver =
                new PageableHandlerMethodArgumentResolver(sortResolver());
        //修改jpa返回结果最大值，默认2000
        pageableHandlerMethodArgumentResolver.setMaxPageSize(properties.getMaxPageSize());

        return pageableHandlerMethodArgumentResolver;
    }

}