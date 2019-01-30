package com.gmcc.msb.zuul.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmcc.msb.common.vo.RequestLog;
import com.gmcc.msb.zuul.Constant;
import com.gmcc.msb.zuul.entity.ApiInfo;
import com.gmcc.msb.zuul.entity.App;
import com.gmcc.msb.zuul.properties.MsbZuulProperties;
import com.gmcc.msb.zuul.service.RedisService;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.cloud.sleuth.Span;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.concurrent.Executor;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.POST_TYPE;

/**
 * @author Yuan Chunhai
 */
@Component
public class ApiRequestLogPostFilter extends ZuulFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiRequestLogPostFilter.class);

    @Autowired
    MsbZuulProperties properties;

    @Autowired
    RedisService redisService;

    @Autowired
    MsbZuulProperties msbZuulProperties;


    @Resource(name = "msbExecutorService")
    Executor executor;

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String filterType() {
        return POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return 101;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    Random random = new Random();

    @Override
    public Object run() {
        RequestContext requestContext = RequestContext.getCurrentContext();

        long requestTime = (long) requestContext.get(Constant.REQUEST_TIME);
        Integer appId = (Integer) requestContext.get(Constant.APP_ID);
        if (appId == null) {
            LOGGER.warn("未获取到app id,不记录");
            return 1;
        }
        Integer apiId = (Integer) requestContext.get(Constant.API_ID);
        if (apiId == null) {
            LOGGER.warn("未获取到api id,不记录");
            return 2;
        }

        String serviceId = (String) requestContext.get(Constant.SERVICE_ID);

        String traceId = requestContext.getZuulRequestHeaders().get("X-B3-TraceId");
//        if (StringUtils.isEmpty(traceId)) {
//            traceId = createId();
//        }

        final RequestLog[] requestLog = {new RequestLog()};
        requestLog[0].setTraceId(traceId);
        requestLog[0].setAppId(appId);
        requestLog[0].setApiId(apiId);
        requestLog[0].setServiceId(serviceId);
        requestLog[0].setRequestTime(requestTime);

        requestLog[0].setRequestMethod(requestContext.getRequest().getMethod());
        requestLog[0].setRequestUri((String) requestContext.get(FilterConstants.REQUEST_URI_KEY));
        requestLog[0].setRequestParam(requestContext.getRequest().getQueryString());
        requestLog[0].setRequestBody(requestContext.getResponseBody());

        requestLog[0].setUserId((String) requestContext.get(Constant.USER_ID));

        int statusCode = requestContext.getResponseStatusCode();
        if (statusCode != HttpStatus.OK.value()) {
            LOGGER.debug("status code : {}", statusCode);
        }

        if (properties.getFailStatusCodes().contains(statusCode)) {
            requestLog[0].setRequestResult(RequestLog.FAIL);
        } else {
            requestLog[0].setRequestResult(RequestLog.SUCCESS);
        }


        try {
            ServletInputStream inputStream = requestContext.getRequest().getInputStream();
            String body = StreamUtils.copyToString(inputStream, Charset.defaultCharset());
            requestLog[0].setRequestBody(body);
        } catch (IOException e) {
            LOGGER.error("读取请求内容异常", e);
        }

        Long routingDate = null;
        if (requestContext.get(Constant.ROUTING_DATE) != null) {
            routingDate = (Long) requestContext.get(Constant.ROUTING_DATE);
            requestLog[0].setGatewayDelayTime((int) (routingDate - requestTime));
        }
        Long responseDate = null;
        if (requestContext.get(Constant.RESPOND_DATE) != null) {
            responseDate = (Long) requestContext.get(Constant.RESPOND_DATE);
            requestLog[0].setElapsedTime((int) (responseDate - requestTime));
        }
        if (routingDate != null && responseDate != null) {
            requestLog[0].setRequestServiceTime((int) (responseDate - routingDate));
        }

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String requestLogStr = objectMapper.writeValueAsString(requestLog[0]);
                    redisService.logRequest(requestLogStr);
                } catch (Exception e) {
                    LOGGER.error("写入redis异常", e);
                }


                App app;
                try {
                    app = (App) requestContext.get(Constant.APP_INFO);
                } catch (Exception e) {
                    app = new App();
                }

                ApiInfo apiInfo;
                try {
                    apiInfo = (ApiInfo) requestContext.get(Constant.API_INFO);
                } catch (Exception e) {
                    apiInfo = new ApiInfo();
                }

                LOGGER.info("返回: TRACEID:{}, SERVICE:{},APP:{},API:{},用户:{},结果:{},耗时:{},网关延时:{},服务调用用时:{} ",
                        traceId,
                        serviceId, app.getAppName(), apiInfo.getApiName(),
                        requestContext.get(Constant.USER_ID), requestLog[0].getRequestResult(),
                        requestLog[0].getElapsedTime(), requestLog[0].getGatewayDelayTime(),
                        requestLog[0].getRequestServiceTime());

                requestLog[0] = null;
            }
        });


        return null;
    }

    private String createId() {
        long id = random.nextLong();
        return Span.idToHex(id);
    }

}
