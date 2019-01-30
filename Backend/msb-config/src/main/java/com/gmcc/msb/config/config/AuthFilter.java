package com.gmcc.msb.config.config;

import com.gmcc.msb.config.entity.ServiceItem;
import com.gmcc.msb.config.service.ConfigService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.Enumeration;
import java.util.List;

public class AuthFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(AuthFilter.class);
    private AuthProperties authConfig;
    private ConfigService configService;
    private PathMatcher matcher = new AntPathMatcher();

    public AuthFilter(AuthProperties authConfig,ConfigService configService) {
        this.authConfig = authConfig;
        this.configService = configService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String uri = request.getRequestURI();

        log.debug("=============URI:" + uri);

        Enumeration<String> headerNames = request.getHeaderNames();
        log.debug("==============header names:" + headerNames);
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            log.debug("===NAME:" + name);
            log.debug("===VALUE:" + request.getHeader(name));
        }

        String auth = request.getHeader("authorization");

        List<String> urlPattern = authConfig.getPattern();

        boolean authResult=false;
        boolean isMatch=false;
        for(String pattern:urlPattern) {
            log.debug("url pattern:" + pattern);
            if(matcher.match(pattern, request.getRequestURI())){
                isMatch=true;
                log.debug("不需要认证");
                break;
            }
        }

        String userName = "";
        String passWord = "";
            if (!isMatch) {
                String serviceId = "";
                if(uri.endsWith("json")||uri.endsWith("properties")){
                    serviceId=uri.substring(1,uri.lastIndexOf("-"));
                }else{

                    serviceId=uri.substring(1);
                    serviceId=serviceId.substring(0,serviceId.indexOf("/"));
                }

                log.debug("===========serviceId:"+serviceId);

                if (auth != null) {
                    try {
                        String auStr = new String(
                                Base64.getDecoder().decode(auth.substring(6)));
                        log.debug("value is:" + auStr);

                        userName = auStr.substring(0, auStr.indexOf(":"));
                        passWord = auStr.substring(auStr.indexOf(":") + 1);

                        log.debug("userName is:" + userName);

                        if(StringUtils.isNotEmpty(passWord)) {
                            ServiceItem service = null;
                            try {
                                service = configService.findServiceByServiceId(serviceId);
                            } catch (Exception e) {
                                log.error("查询服务信息出错:", e);
                            }

                            if (service != null
                                    && StringUtils.isNotEmpty(service.getServiceSecret())
                                    && service.getServiceSecret().equals(passWord)) {
                                authResult = true;

                            }
                        }
                    }catch(Exception e){
                       log.error("凭证解析错误:",e);
                    }
                }
            }else{
              authResult=true;
            }

        if(authResult) {
            filterChain.doFilter(servletRequest, servletResponse);
        }else{
             log.error("URI:::"+uri);
            log.error("授权失败，无效凭证;USERNAME="+userName+":::PWD="+passWord);
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.setStatus(401);
            return;
        }
    }

    @Override
    public void destroy() {

    }
}
