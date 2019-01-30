package com.gmcc.msb.zuul.filter;

import com.gmcc.msb.zuul.Constant;
import com.gmcc.msb.zuul.properties.MsbZuulProperties;
import com.gmcc.msb.zuul.service.FilterHelperService;
import com.gmcc.msb.zuul.util.SignUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * @author Yuan Chunhai
 * @Date 9/28/2018-11:13 AM
 */
@Component
public class SignPreFilter extends ZuulFilter {

    private static final Logger log = LoggerFactory.getLogger(SignPreFilter.class);

    @Autowired
    private FilterHelperService filterHelperService;

    @Autowired
    private MsbZuulProperties msbZuulProperties;

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return Constant.SIGN_FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return !msbZuulProperties.getSkipSignPreFilter();
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        String serviceId = ctx.get(FilterConstants.SERVICE_ID_KEY).toString();
        String key = filterHelperService.getServiceSignKey(serviceId);
        log.debug("sign key : {} ", key);
        String sign = SignUtils.signRequest(ctx.getRequest(), key, ctx.get(FilterConstants.REQUEST_URI_KEY).toString());
        ctx.addZuulRequestHeader("sign", sign);
        return null;
    }
}
