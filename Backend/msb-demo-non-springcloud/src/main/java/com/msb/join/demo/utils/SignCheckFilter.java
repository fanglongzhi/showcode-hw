package com.msb.join.demo.utils;

import com.msb.join.demo.DemoProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Yuan Chunhai
 * @Date 9/28/2018-6:07 PM
 */
public class SignCheckFilter implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(SignCheckFilter.class);

    private final DemoProperties msbProperties;

    public SignCheckFilter(DemoProperties msbProperties) {
        this.msbProperties = msbProperties;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String sign = request.getHeader(Constant.SIGN);
        String verifySign = null;

        verifySign = SignUtils.signRequest(request, msbProperties.getMsbSignKey());

        log.info("sign {} {} ", sign, verifySign);
        log.info("URL:::{}",request.getRequestURI());
        if(!msbProperties.getSkipSignCheckUri().contains(request.getRequestURI())) {
            if (StringUtils.isEmpty(sign) || !sign.equals(verifySign)) {
                String code = msbProperties.getServiceCode() + "-11001";
                String responseStr = "{\"code\":\"" + code + "\",\"message\":\"签名不正确\"}";
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write(responseStr);
                return false;
            } else {
                return true;
            }
        }else{
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
