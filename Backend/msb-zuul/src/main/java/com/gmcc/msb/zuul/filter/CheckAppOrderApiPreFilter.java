package com.gmcc.msb.zuul.filter;

import com.gmcc.msb.zuul.Constant;
import com.gmcc.msb.zuul.properties.MsbZuulProperties;
import com.gmcc.msb.zuul.service.AppOrderApiService;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.util.ZuulRuntimeException;
import org.springframework.stereotype.Component;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * @author Yuan Chunhai
 * @Date 9/26/2018-2:37 PM
 */
@Component
public class CheckAppOrderApiPreFilter extends ZuulFilter {


    @Autowired
    private AppOrderApiService appOrderApiService;

    @Autowired
    private MsbZuulProperties properties;

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return Constant.CHECK_APP_ORDER_API_FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return !properties.isSkipCheckAppOrderApiPreFilter();
    }

    @Override
    public Object run() {

        RequestContext ctx = RequestContext.getCurrentContext();
        Integer appId = (Integer) ctx.get(Constant.APP_ID);
        Integer apiId = (Integer) ctx.get(Constant.API_ID);

        if (!appOrderApiService.isAppOrderApi(appId, apiId)) {
            throw new ZuulRuntimeException(new ZuulException("0008-10010", 400, null));
        }

        return null;
    }
}
