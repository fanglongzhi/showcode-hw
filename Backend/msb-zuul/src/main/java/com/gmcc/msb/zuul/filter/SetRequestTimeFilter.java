package com.gmcc.msb.zuul.filter;

import com.gmcc.msb.zuul.Constant;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * @author Yuan Chunhai
 */
@Component
public class SetRequestTimeFilter extends ZuulFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(SetRequestTimeFilter.class);

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return -1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        requestContext.set(Constant.REQUEST_TIME, System.currentTimeMillis());
        LOGGER.debug("CONTEXT-PATH:::" + RequestContext.getCurrentContext().getRequest().getContextPath());
        LOGGER.debug("URI:::" + RequestContext.getCurrentContext().getRequest().getRequestURI());
        return null;
    }
}
