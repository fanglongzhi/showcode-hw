package com.gmcc.msb.zuul.filter;

import com.gmcc.msb.zuul.Constant;
import com.gmcc.msb.zuul.entity.ApiInfo;
import com.gmcc.msb.zuul.properties.MsbZuulProperties;
import com.gmcc.msb.zuul.repository.DataOrgUserRepository;
import com.gmcc.msb.zuul.repository.ModuleRepository;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.util.ZuulRuntimeException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static com.gmcc.msb.zuul.Constant.CHECK_API_ACCESS_FILTER_ORDER;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * @author Yuan Chunhai
 * @Date 9/26/2018-6:29 PM
 */
@Component
public class CheckApiAccessPreFilter extends ZuulFilter {


    @Autowired
    private MsbZuulProperties properties;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private DataOrgUserRepository dataOrgUserRepository;

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return CHECK_API_ACCESS_FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return !properties.isSkipCheckApiAccessPreFilter();
    }

    @Override
    public Object run() {

        RequestContext requestContext = RequestContext.getCurrentContext();

        ApiInfo targetApi = (ApiInfo) requestContext.get(Constant.API_INFO);

        String userId = (String) requestContext.get(Constant.USER_ID);
        if (!targetApi.isAnnoymousAccess() && StringUtils.isEmpty(userId)) {
            throw new ZuulRuntimeException(new ZuulException("0008-10007", 400, null));
        }

        //如果不能匿名访问且调用来自MSB管理平台，检查用户对api的访问权限,检查用户是否拥有数据主组
        String appId = (String) requestContext.get(Constant.APP_APP_ID);
        if (!targetApi.isAnnoymousAccess() && properties.getMsbFrontClientIds().contains(appId)) {
            String apiId = targetApi.getId().toString();
            String regexp = "(^" + apiId + ",)|(," + apiId + ",)|(," + apiId + "$)|(^" + apiId + "$)";
            if (moduleRepository.countApi(regexp, userId) == 0) {
                throw new ZuulRuntimeException(new ZuulException("0008-10008", 401, null));
            }

            if (dataOrgUserRepository.countByUserIdAndMainFlag(userId, "1") == 0) {
                throw new ZuulRuntimeException(new ZuulException("0008-10014", 401, null));
            }
        }

        return null;
    }
}
