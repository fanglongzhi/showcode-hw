package com.gmcc.msb.zuul.filter;

import com.gmcc.msb.zuul.Constant;
import com.gmcc.msb.zuul.entity.ApiInfo;
import com.gmcc.msb.zuul.service.ApiService;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.cloud.netflix.zuul.util.ZuulRuntimeException;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.util.UrlPathHelper;

import java.util.List;

import static com.gmcc.msb.zuul.entity.ApiInfo.STATUS_ONLINE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * 从请求地址获取api
 *
 * @author Yuan Chunhai
 */
@Component
public class CheckApiStatusFilter extends ZuulFilter {

    public static final String PATH_SEPARATOR = "/";
    Logger logger = LoggerFactory.getLogger(CheckApiStatusFilter.class);

    private UrlPathHelper urlPathHelper = new UrlPathHelper();

    private AntPathMatcher antPathMatcher = new AntPathMatcher();


    @Autowired
    private RouteLocator routeLocator;

    @Autowired
    private ApiService apiService;

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return Constant.CHECK_API_STATUS_FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        logger.debug("检查api状态");
        RequestContext requestContext = RequestContext.getCurrentContext();
        String serviceId = (String) requestContext.get(Constant.SERVICE_ID);

        String method = requestContext.getRequest().getMethod();

        String requestURI = this.urlPathHelper.getPathWithinApplication(requestContext.getRequest());
        Route route = this.routeLocator.getMatchingRoute(requestURI);

        if (route == null) {
            throw new ZuulRuntimeException(new ZuulException("0008-10015", 400, null));
        }

        String path = route.getPath();
        if (!path.endsWith(PATH_SEPARATOR)) {
            path = path + PATH_SEPARATOR;
        }

        List<ApiInfo> apis = apiService.getServiceApis(serviceId);



        ApiInfo targetApi = null;
        if (apis != null && !apis.isEmpty()) {
            for (ApiInfo apiInfo : apis) {
                if (!apiInfo.getPath().endsWith(PATH_SEPARATOR)) {
                    apiInfo.setPath(apiInfo.getPath() + PATH_SEPARATOR);
                }
                if (apiInfo.getStatus() != null &&
                            apiInfo.getStatus() == ApiInfo.STATUS_ONLINE
                            && method.equalsIgnoreCase(apiInfo.getMethod())
                            && path.equals(apiInfo.getPath())) {
                    targetApi = apiInfo;
                    break;
                }
            }

            //通过antmatcher查找
            if (targetApi == null) {
                for (ApiInfo apiInfo : apis) {
                    if (apiInfo.getStatus() != null &&
                                apiInfo.getStatus() == ApiInfo.STATUS_ONLINE
                                && method.equalsIgnoreCase(apiInfo.getMethod())
                                && apiInfo.getPath().contains("{")
                                && antPathMatcher.match(apiInfo.getPath(), path)) {
                        targetApi = apiInfo;
                        break;
                    }
                }
            }
        } else {
            logger.warn("该服务{}下没有任何api信息", serviceId);
            throw new ZuulRuntimeException(new ZuulException("0008-10009", 400, null));
        }

        if (targetApi == null || STATUS_ONLINE != targetApi.getStatus()) {
            throw new ZuulRuntimeException(new ZuulException("0008-10009", 400, null));
        }

        logger.debug("api 信息 {}", targetApi);
        requestContext.getResponse().addHeader("api-id", targetApi.getId().toString());

        requestContext.set(Constant.API_ID, targetApi.getId());
        requestContext.set(Constant.API_INFO, targetApi);


        return null;
    }
}
