package com.gmcc.msb.zuul.filter;

import com.gmcc.msb.zuul.Constant;
import com.gmcc.msb.zuul.properties.MsbZuulProperties;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.zuul.util.ZuulRuntimeException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.gmcc.msb.zuul.Constant.CHECK_API_ACCESS_FILTER_ORDER;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

@Component
@RefreshScope
/**
 * @author zhiwen.hu
 */
public class RateLimitFilter  extends ZuulFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(RateLimitFilter.class);
    public static final int FILTER_ORDER = CHECK_API_ACCESS_FILTER_ORDER+1;
    public static final String RATELIMIT_H_KEY = "rt_h_time:";
    public static final String RATELIMIT_M_KEY = "rt_m_time:";
    public static final String RATELIMIT_S_KEY = "rt_s_time:";
    public static final String STRATEGY_APP = "strategy_app:";
    public static final String STRATEGY_API = "strategy_api:";
    public static final String SEC = "S";
    public static final String MIN = "M";
    public static final String HOUR = "H";
    public static final int ONE = 1;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private MsbZuulProperties msbZuulProperties;

    @Value("${rate.limit.app.h.default:-1}")
    private int rate_limit_app_h_default;

    @Value("${rate.limit.app.m.default:-1}")
    private int rate_limit_app_m_default;

    @Value("${rate.limit.app.s.default:-1}")
    private int rate_limit_app_s_default;

    @Value("${rate.limit.api.h.default:-1}")
    private int rate_limit_api_h_default;

    @Value("${rate.limit.api.m.default:-1}")
    private int rate_limit_api_m_default;

    @Value("${rate.limit.api.s.default:-1}")
    private int rate_limit_api_s_default;

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return !msbZuulProperties.isSkipRateLimitFilter();
    }

    @Override
    public Object run() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        String apiId = requestContext.get(Constant.API_ID).toString();
        String appId = requestContext.get(Constant.APP_ID).toString();
        checkLimit(STRATEGY_APP,appId);
        checkLimit(STRATEGY_API,apiId);
        return null;
    }

    private void checkLimit(String strategy,String id) {
        String key = strategy +id;
        Map<Object, Object> entries;
        if(!stringRedisTemplate.hasKey(key)){
            entries = setDefaultStrategy(strategy);
        }else{
            entries = stringRedisTemplate.opsForHash().entries(key);
        }

        if(entries.containsKey(SEC) && entries.get(SEC)!=null){
            Integer limitValue = Integer.valueOf(entries.get(SEC).toString());
            checkRateLimit(id, strategy, limitValue, RATELIMIT_S_KEY, TimeUnit.SECONDS);
        }
        if(entries.containsKey(MIN) && entries.get(MIN)!=null) {
            Integer limitValue = Integer.valueOf(entries.get(MIN).toString());
            checkRateLimit(id, strategy, limitValue, RATELIMIT_M_KEY, TimeUnit.MINUTES);
        }
        if(entries.containsKey(HOUR) && entries.get(HOUR)!=null) {
            Integer limitValue = Integer.valueOf(entries.get(HOUR).toString());
            checkRateLimit(id, strategy,limitValue , RATELIMIT_H_KEY, TimeUnit.HOURS);
        }
        return;
    }

    private Map<Object, Object> setDefaultStrategy(String strategy) {
        Map<Object, Object> entries = new HashMap<>(10);
        if(STRATEGY_APP.equalsIgnoreCase(strategy)){
            if(rate_limit_app_h_default>0){
                entries.put(HOUR,rate_limit_app_h_default);
            }
            if(rate_limit_app_m_default>0){
                entries.put(MIN,rate_limit_app_m_default);
            }
            if(rate_limit_app_s_default>0){
                entries.put(SEC,rate_limit_app_s_default);
            }
        }else{
            if(rate_limit_api_h_default>0){
                entries.put(HOUR,rate_limit_api_h_default);
            }
            if(rate_limit_api_m_default>0){
                entries.put(MIN,rate_limit_api_m_default);
            }
            if(rate_limit_api_s_default>0){
                entries.put(SEC,rate_limit_api_s_default);
            }
        }
        return entries;
    }

    private void checkRateLimit(String id, String rateType, int limitValue, String key, TimeUnit timeUnit) {
        String timeKey = key.concat(rateType).concat(id);
        Long increment = stringRedisTemplate.opsForValue().increment(timeKey, ONE);
        if(increment == ONE){
            stringRedisTemplate.expire(timeKey,ONE,timeUnit);
        }

        if(increment>limitValue){
            LOGGER.info("流控限制：{},{},{},{}", id, rateType, limitValue, key);
            String str = rateType.indexOf("app") < 0 ?"API:":"APP:";
            ZuulException zuulException =
                    new ZuulException("0008-10011", HttpStatus.TOO_MANY_REQUESTS.value(), null);
            throw new ZuulRuntimeException(zuulException);
        }
    }

}
