package com.msb.join.demo;

import com.msb.join.demo.utils.FetchHeaderFilter;
import com.msb.join.demo.utils.SignCheckFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfiguation extends WebMvcConfigurerAdapter {

    @Autowired
    private DemoProperties msbProperties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SignCheckFilter(msbProperties));
        registry.addInterceptor(new FetchHeaderFilter());
    }
}
