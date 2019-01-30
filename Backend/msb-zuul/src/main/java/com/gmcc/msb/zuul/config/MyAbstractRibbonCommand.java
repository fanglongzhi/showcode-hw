package com.gmcc.msb.zuul.config;

import com.netflix.client.AbstractLoadBalancerAwareClient;
import com.netflix.client.ClientRequest;
import com.netflix.client.config.IClientConfig;
import com.netflix.client.http.HttpResponse;
import com.netflix.config.DynamicIntProperty;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.zuul.constants.ZuulConstants;
import com.netflix.zuul.context.RequestContext;
import org.springframework.cloud.netflix.ribbon.RibbonHttpResponse;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommand;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommandContext;
import org.springframework.cloud.netflix.zuul.filters.route.ZuulFallbackProvider;
import org.springframework.http.client.ClientHttpResponse;

/**
 * @program: msb-zuul
 * @description: 实现独立线程池
 * @author: zhifanglong
 * @create: 2018-11-21 14:08
 */
public abstract class MyAbstractRibbonCommand <LBC extends AbstractLoadBalancerAwareClient<RQ, RS>, RQ extends ClientRequest, RS extends HttpResponse>
        extends HystrixCommand<ClientHttpResponse> implements RibbonCommand {

    protected final LBC client;
    protected RibbonCommandContext context;
    protected ZuulFallbackProvider zuulFallbackProvider;
    protected IClientConfig config;
    public MyAbstractRibbonCommand(String serviceId,String commandKey, LBC client,
                                   RibbonCommandContext context, ZuulProperties zuulProperties,
                                   ZuulFallbackProvider fallbackProvider, IClientConfig config) {
        super(getSetter(serviceId,commandKey, zuulProperties));
        this.client = client;
        this.context = context;
        this.zuulFallbackProvider = fallbackProvider;
        this.config = config;
    }

    protected static Setter getSetter(final String serviceId,final String commandKey,
                                      ZuulProperties zuulProperties) {

        // @formatter:off
        final HystrixCommandProperties.Setter setter = HystrixCommandProperties.Setter()
                .withExecutionIsolationStrategy(zuulProperties.getRibbonIsolationStrategy());
        if (zuulProperties.getRibbonIsolationStrategy() == HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE){
            final String name = ZuulConstants.ZUUL_EUREKA + commandKey + ".semaphore.maxSemaphores";
            // we want to default to semaphore-isolation since this wraps
            // 2 others commands that are already thread isolated
            final DynamicIntProperty value = DynamicPropertyFactory.getInstance()
                    .getIntProperty(name, zuulProperties.getSemaphore().getMaxSemaphores());
            setter.withExecutionIsolationSemaphoreMaxConcurrentRequests(value.get());
        }

        return Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(serviceId))
                .andCommandKey(HystrixCommandKey.Factory.asKey(commandKey))
                .andCommandPropertiesDefaults(setter);
        // @formatter:on
    }

    @Override
    protected ClientHttpResponse run() throws Exception {
        final RequestContext context = RequestContext.getCurrentContext();

        RQ request = createRequest();
        RS response = this.client.executeWithLoadBalancer(request, config);

        context.set("ribbonResponse", response);

        // Explicitly close the HttpResponse if the Hystrix command timed out to
        // release the underlying HTTP connection held by the response.
        //
        if (this.isResponseTimedOut()) {
            if (response != null) {
                response.close();
            }
        }

        return new RibbonHttpResponse(response);
    }

    @Override
    protected ClientHttpResponse getFallback() {
        if(zuulFallbackProvider != null) {
            return zuulFallbackProvider.fallbackResponse();
        }
        return super.getFallback();
    }

    public LBC getClient() {
        return client;
    }

    public RibbonCommandContext getContext() {
        return context;
    }

    protected abstract RQ createRequest() throws Exception;
}

