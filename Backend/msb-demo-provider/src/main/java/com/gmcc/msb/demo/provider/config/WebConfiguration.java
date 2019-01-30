package com.gmcc.msb.demo.provider.config;

import com.gmcc.msb.demo.provider.filter.FetchHeaderFilter;
import com.gmcc.msb.demo.provider.filter.SignCheckFilter;
import com.gmcc.msb.demo.provider.property.MsbProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author sirui.lin
 */
@Configuration
@ConditionalOnProperty("msb.common.enabled")
public class WebConfiguration extends WebMvcConfigurerAdapter {

    @Value("${spring.application.name}")
    private String applicationName;

    @Autowired
    private MsbProperties msbProperties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SignCheckFilter(msbProperties, applicationName));
        registry.addInterceptor(new FetchHeaderFilter());
    }
}
