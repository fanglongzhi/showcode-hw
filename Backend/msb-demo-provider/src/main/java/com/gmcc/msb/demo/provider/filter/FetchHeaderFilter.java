package com.gmcc.msb.demo.provider.filter;

import com.gmcc.msb.demo.provider.util.UserContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program:
 * @description: 处理报文头信息
 * @author: zhifanglong
 * @create: 2018-10-17 14:32
 */
public class FetchHeaderFilter implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(FetchHeaderFilter.class);
    public static final String USER_ID = "user_id";

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String userId = httpServletRequest.getHeader(USER_ID);

        UserContextHolder.getContext().setUserId(userId);
        log.info("HEADER"+httpServletRequest.getHeaderNames());
        log.info("USER_ID_KEY:::"+USER_ID);
        log.info("USER_ID:::"+userId);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
