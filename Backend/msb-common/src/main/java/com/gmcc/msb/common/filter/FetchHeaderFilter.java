package com.gmcc.msb.common.filter;

import com.gmcc.msb.common.property.UserContextHolder;
import com.gmcc.msb.common.service.client.MsbSystemClient;
import com.gmcc.msb.common.vo.Response;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @program: msb-common
 * @description: 处理报文头信息
 * @author: zhifanglong
 * @create: 2018-10-17 14:32
 */
public class FetchHeaderFilter implements HandlerInterceptor {
    public static final String USER_ID = "user_id";
    public static final String MAIN_ORG = "mainOrg";
    public static final String DATA_ORG = "dataOrg";
    public static final String MSB_CONFIG="msb-config";
    private MsbSystemClient msbSystemClient;
    private String applicationName;
    private static final Logger log = LoggerFactory.getLogger(FetchHeaderFilter.class);

    public FetchHeaderFilter(MsbSystemClient client,String applicationName) {
        this.msbSystemClient = client;
        this.applicationName = applicationName;
    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String userId = httpServletRequest.getHeader(USER_ID);

        if (StringUtils.isNotBlank(userId)&&!MSB_CONFIG.equals(applicationName)) {
            UserContextHolder.getContext().setUserId(userId);
            Response<Map<String, List<Long>>> result = msbSystemClient.findAllOrgId(userId);
            Map<String, List<Long>> orgInfos = result.getContent();
            Long mainOrg = orgInfos.get(MAIN_ORG) != null ? orgInfos.get(MAIN_ORG).get(0) : null;
            UserContextHolder.getContext().setOrgId(mainOrg);
            UserContextHolder.getContext().setDataOrgList(orgInfos.get(DATA_ORG));
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
