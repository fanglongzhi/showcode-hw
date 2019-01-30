package com.gmcc.msb.zuul.filter;

import com.gmcc.msb.zuul.Constant;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.stereotype.Component;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.ROUTE_TYPE;
/**
 * @author: zhiwen.hu
 */
@Component
public class RequestRouteFilter extends ZuulFilter {
    public static final int FILTER_ORDER = 1;
    @Override
    public String filterType() {
        return ROUTE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        requestContext.set(Constant.ROUTING_DATE,System.currentTimeMillis());
        return null;
    }
}
