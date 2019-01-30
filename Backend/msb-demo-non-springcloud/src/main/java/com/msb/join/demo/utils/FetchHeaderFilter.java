package com.msb.join.demo.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: msb-common
 * @description: 处理报文头信息
 * @author: zhifanglong
 * @create: 2018-10-17 14:32
 */
public class FetchHeaderFilter implements HandlerInterceptor {
    public static final String USER_ID = "user_id";
    private static final Logger log = LoggerFactory.getLogger(FetchHeaderFilter.class);

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String userId = httpServletRequest.getHeader(USER_ID);
        UserContextHolder.getContext().setUserId(userId);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
