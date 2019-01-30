package com.gmcc.msb.zuul.service;

import com.gmcc.msb.zuul.properties.MsbZuulProperties;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Service;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.REQUEST_URI_KEY;

/**
 * @author Yuan Chunhai
 * @Date 9/26/2018-2:10 PM
 */
@Service
public class FilterHelperService {

    public static final String SEP = ":";
    @Autowired
    private MsbZuulProperties msbZuulProperties;

    @Autowired
    private SignKeyCache signKeyCache;

    public boolean skipTokenCheck() {

        RequestContext requestContext = RequestContext.getCurrentContext();
        String uri = (String) requestContext.get(REQUEST_URI_KEY);
        String serviceId = (String) requestContext.get(FilterConstants.SERVICE_ID_KEY);

        return msbZuulProperties.getSkipCheckTokenUri().contains(serviceId + SEP + uri);

    }


    public String getServiceSignKey(String serviceId) {
        return signKeyCache.getSignKey(serviceId);
    }


}
