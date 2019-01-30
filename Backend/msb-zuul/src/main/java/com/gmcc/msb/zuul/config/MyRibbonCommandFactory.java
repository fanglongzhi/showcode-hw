package com.gmcc.msb.zuul.config;

import com.gmcc.msb.zuul.Constant;
import com.gmcc.msb.zuul.entity.ApiInfo;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.netflix.ribbon.apache.RibbonLoadBalancingHttpClient;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommand;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommandContext;
import org.springframework.cloud.netflix.zuul.filters.route.ZuulFallbackProvider;
import org.springframework.cloud.netflix.zuul.filters.route.support.AbstractRibbonCommandFactory;

import java.util.Collections;
import java.util.Set;

/**
 * 通过修改commandkey，使zuul实现针对不同的（如方法名）的方式进行熔断，不仅仅针对服务名
 *
 * @author Yuan Chunhai
 */
public class MyRibbonCommandFactory extends AbstractRibbonCommandFactory {
    private static final Logger log = LoggerFactory.getLogger(MyRibbonCommandFactory.class);
    private final SpringClientFactory clientFactory;

    private final ZuulProperties zuulProperties;


    public MyRibbonCommandFactory(SpringClientFactory clientFactory, ZuulProperties zuulProperties) {
        super(Collections.emptySet());
        this.clientFactory = clientFactory;
        this.zuulProperties = zuulProperties;
    }

    public MyRibbonCommandFactory(SpringClientFactory clientFactory, ZuulProperties zuulProperties, Set<ZuulFallbackProvider> fallbackProviders) {
        super(fallbackProviders);
        this.clientFactory = clientFactory;
        this.zuulProperties = zuulProperties;
    }


    @Override
    public RibbonCommand create(RibbonCommandContext context) {

        ZuulFallbackProvider zuulFallbackProvider = getFallbackProvider(context.getServiceId());
        final String serviceId = context.getServiceId();

        final RibbonLoadBalancingHttpClient client = this.clientFactory.getClient(
                serviceId, RibbonLoadBalancingHttpClient.class);
        client.setLoadBalancer(this.clientFactory.getLoadBalancer(serviceId));
        RequestContext rc = RequestContext.getCurrentContext();
        ApiInfo apiInfo = (ApiInfo) rc.get(Constant.API_INFO);
        final String commandKey = serviceId + "#" + apiInfo.getId();
        /*return new HttpClientRibbonCommand(commandKey, client, context, zuulProperties, zuulFallbackProvider,
                clientFactory.getClientConfig(serviceId));*/
        log.debug("COMMAND KEY:::" + commandKey);
        return new MyRibbonCommand(serviceId, commandKey, client, context, zuulProperties, zuulFallbackProvider,
                clientFactory.getClientConfig(serviceId));
    }

}


