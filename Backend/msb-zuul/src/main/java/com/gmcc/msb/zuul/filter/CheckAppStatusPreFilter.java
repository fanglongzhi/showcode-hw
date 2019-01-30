package com.gmcc.msb.zuul.filter;

import com.gmcc.msb.zuul.Constant;
import com.gmcc.msb.zuul.entity.App;
import com.gmcc.msb.zuul.properties.MsbZuulProperties;
import com.gmcc.msb.zuul.service.AppService;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.util.ZuulRuntimeException;
import org.springframework.stereotype.Component;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * @author Yuan Chunhai
 * @Date 9/26/2018-2:08 PM
 */
@Component
public class CheckAppStatusPreFilter extends ZuulFilter {

    Logger logger = LoggerFactory.getLogger(CheckApiStatusFilter.class);


    @Autowired
    private MsbZuulProperties msbZuulProperties;

    @Autowired
    private AppService appService;

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return Constant.CHECK_APP_STATUS_FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return !msbZuulProperties.isSkipCheckAppStatusPreFilter();
    }

    @Override
    public Object run() {
        logger.debug("检查api状态");
        RequestContext requestContext = RequestContext.getCurrentContext();

        String appId = requestContext.getRequest().getHeader(msbZuulProperties.getHeaderAppIdKey());

        if (StringUtils.isEmpty(appId)) {
            appId = requestContext.getRequest().getParameter(msbZuulProperties.getHeaderAppIdKey());
            if (StringUtils.isEmpty(appId)) {
                throw new ZuulRuntimeException(new ZuulException("0008-00002", 400, null));
            }
        }

        try{
            App app = appService.isAppAvaiable(appId);
            requestContext.set(Constant.APP_ID, app.getId());
            requestContext.set(Constant.APP_APP_ID,app.getAppId());
            requestContext.set(Constant.APP_INFO, app);
        }
        catch (Exception e){
            throw new ZuulRuntimeException(e);
        }


        return null;
    }
}
