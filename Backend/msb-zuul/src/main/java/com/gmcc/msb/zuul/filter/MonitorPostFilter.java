package com.gmcc.msb.zuul.filter;

import com.gmcc.msb.zuul.properties.MsbZuulProperties;
import com.gmcc.msb.zuul.service.RedisService;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.Executor;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.POST_TYPE;

/**
 * @author Yuan Chunhai
 * @Date 12/19/2018-11:56 AM
 */
@Component
public class MonitorPostFilter extends ZuulFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiRequestLogPostFilter.class);

    @Autowired
    MsbZuulProperties msbZuulProperties;

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
        return 102;
    }

    @Override
    public boolean shouldFilter() {
        return !msbZuulProperties.isSkipMonitorPostFilter();
    }

    @Override
    public Object run() {

        RequestContext requestContext = RequestContext.getCurrentContext();

        String executedFilters = requestContext.get("executedFilters").toString();
        LOGGER.debug("executedFilters: {}", executedFilters);

        executor.execute(new Runnable() {
            @Override
            public void run() {
                String[] arr = executedFilters.split(",");
                for (int i = 0; i < arr.length; i++) {
                    String str = arr[i].trim();

                    int firstSquareBracket = str.indexOf('[');
                    int secondSquareBracket = str.indexOf('[', firstSquareBracket + 1);
                    String filterName = str.substring(0, firstSquareBracket);
                    if (filterName.contains("$")) {
                        filterName = filterName.substring(0, filterName.indexOf('$'));
                    }
                    String filterStatus = str.substring(firstSquareBracket + 1, secondSquareBracket - 1);
                    String elapsedTimeStr = str.substring(secondSquareBracket + 1, str.lastIndexOf(']')).replace("ms", "");

                    redisService.logFilterRequestTime(filterName, Integer.parseInt(elapsedTimeStr));

                }
            }
        });


        return null;
    }
}
