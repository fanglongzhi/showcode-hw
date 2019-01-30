package com.gmcc.msb.zuul.config;

import com.netflix.client.config.IClientConfig;
import com.netflix.config.DynamicIntProperty;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.zuul.constants.ZuulConstants;
import org.springframework.cloud.netflix.ribbon.apache.RibbonApacheHttpRequest;
import org.springframework.cloud.netflix.ribbon.apache.RibbonApacheHttpResponse;
import org.springframework.cloud.netflix.ribbon.apache.RibbonLoadBalancingHttpClient;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommandContext;
import org.springframework.cloud.netflix.zuul.filters.route.ZuulFallbackProvider;
import org.springframework.cloud.netflix.zuul.filters.route.support.AbstractRibbonCommand;

/**
 * @program: msb-zuul
 * @description: 实现了独立设置线程池的Comand
 * @author: zhifanglong
 * @create: 2018-11-21 12:30
 */
public class MyRibbonCommand extends MyAbstractRibbonCommand<RibbonLoadBalancingHttpClient, RibbonApacheHttpRequest, RibbonApacheHttpResponse> {


    public MyRibbonCommand(final String serviceId,final String commandKey,
                           final RibbonLoadBalancingHttpClient client,
                           final RibbonCommandContext context,
                           final ZuulProperties zuulProperties,
                           final ZuulFallbackProvider zuulFallbackProvider,
                           final IClientConfig config) {
        super(serviceId,commandKey, client, context, zuulProperties, zuulFallbackProvider, config);
    }

    @Override
    protected RibbonApacheHttpRequest createRequest() throws Exception {
        return new RibbonApacheHttpRequest(this.context);
    }

}

