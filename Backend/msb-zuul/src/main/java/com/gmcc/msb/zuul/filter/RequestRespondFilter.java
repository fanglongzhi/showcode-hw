package com.gmcc.msb.zuul.filter;

import com.gmcc.msb.zuul.Constant;
import com.gmcc.msb.zuul.service.RedisService;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.Executor;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.POST_TYPE;
/**
 * @author: zhiwen.hu
 */
@Component
public class RequestRespondFilter extends ZuulFilter {
    public static final int FILTER_ORDER = 100;


    @Autowired
    RedisService redisService;

    @Resource(name = "msbExecutorService")
    Executor executor;


    @Override
    public String filterType() {
        return POST_TYPE;
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
        long respondDate = System.currentTimeMillis();
        requestContext.set(Constant.RESPOND_DATE, respondDate);
        String apiId = String.valueOf( requestContext.get(Constant.API_ID));
        String appId = String.valueOf(requestContext.get(Constant.APP_ID));
        Long routingDate = (Long) requestContext.get(Constant.ROUTING_DATE);
        Long requestDate = (Long) requestContext.get(Constant.REQUEST_TIME);
        int statusCode = requestContext.getResponseStatusCode();
        StringBuilder builder = new StringBuilder();
        builder.append(apiId).append("-")
                .append(appId).append("-")
                .append(requestDate).append("-")
                .append(routingDate).append("-")
                .append(respondDate).append("-")
                .append(statusCode);

        executor.execute(new Runnable() {
            @Override
            public void run() {
                redisService.logGW(builder.toString());
            }
        });

        return null;
    }
}
