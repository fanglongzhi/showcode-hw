package com.gmcc.msb.common.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmcc.msb.common.entity.ErrorCode;
import com.gmcc.msb.common.property.MsbProperties;
import com.gmcc.msb.common.service.ErrorCodeCacheService;
import com.gmcc.msb.common.util.SignUtils;
import com.gmcc.msb.common.vo.Constant;
import com.gmcc.msb.common.vo.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Yuan Chunhai
 * @Date 9/28/2018-6:07 PM
 */
public class SignCheckFilter implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(SignCheckFilter.class);

    private final MsbProperties msbProperties;

    private final ErrorCodeCacheService errorCodeCacheService;
    private final String applicationName;

    public SignCheckFilter(MsbProperties msbProperties, ErrorCodeCacheService errorCodeCacheService, String applicationName) {
        this.msbProperties = msbProperties;
        this.errorCodeCacheService = errorCodeCacheService;
        this.applicationName = applicationName;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        // 如果配置了要检查的Controller，只检查配置中有的，没有的就跳过
        boolean check = true;
        if (!msbProperties.getSignCheckController().isEmpty()) {
            List<String> controllers = msbProperties.getSignCheckController();

            String controllerName = ((HandlerMethod) handler).getBeanType().getName();

            if (controllers.contains(controllerName)) {
                check = true;
            }
            else {
                check = false;
            }
        }

        if (msbProperties.isEnabledSignFilter()
                    && !msbProperties.getSkipSignCheckUri().contains(request.getRequestURI())
                    && check) {

            String isMsbInnerCall = request.getHeader(Constant.MSB_INNER_CALL);
            String sign = request.getHeader(Constant.SIGN);
            String verifySign = null;
            if (StringUtils.isEmpty(isMsbInnerCall)) {
                verifySign = SignUtils.signRequest(request, msbProperties.getSignKey());
            } else {
                verifySign = SignUtils.signRequest(request, msbProperties.getCommonSignKey());
            }

            log.debug("sign {} {} ", sign, verifySign);

            if (StringUtils.isEmpty(sign) || !sign.equals(verifySign)) {
                ErrorCode errorCode =
                        errorCodeCacheService.getErrorCode(msbProperties.getServiceCode() + "-11001");
                ObjectMapper objectMapper = new ObjectMapper();
                String responseStr = objectMapper.writeValueAsString(Response.notOk(errorCode));
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write(responseStr);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //ignore
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //ignore
    }
}
