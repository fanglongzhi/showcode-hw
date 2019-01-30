package com.gmcc.msb.zuul.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmcc.msb.zuul.Constant;
import com.gmcc.msb.zuul.properties.MsbZuulProperties;
import com.gmcc.msb.zuul.service.FilterHelperService;
import com.gmcc.msb.zuul.service.RedisService;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.util.ZuulRuntimeException;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.InvalidSignatureException;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * @author Yuan Chunhai
 * @Date 9/26/2018-11:59 AM
 */
@Component
public class CheckTokenPreFilter extends ZuulFilter {

    private static final Logger logger = LoggerFactory.getLogger(CheckTokenPreFilter.class);

    @Autowired
    private MsbZuulProperties msbZuulProperties;

    @Autowired
    private RedisService redisService;

    @Autowired
    private FilterHelperService filterHelperService;

    @Resource(name = "ssoPublicKey")
    RSAPublicKey ssoPublicKey;

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return Constant.CHECK_TOKEN_FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return !msbZuulProperties.isSkipCheckTokenPreFilter();
    }

    @Override
    public Object run() {

        logger.debug("检查token");

        RequestContext requestContext = RequestContext.getCurrentContext();

        //从url参数获取
        String token = requestContext.getRequest().getParameter("token");

        if (StringUtils.isEmpty(token)) {
            //从 header 中获取
            String auth = requestContext.getRequest().getHeader("Authorization");
            if (!StringUtils.isEmpty(auth)) {
                token = auth.replace("Bearer ", "");
            }
        }
        if (StringUtils.isEmpty(token)) {
            if (!filterHelperService.skipTokenCheck()) {
                throw new ZuulRuntimeException(new ZuulException("0008-10003", 400, null));
            }
        } else {

            Jwt jwt;
            try {
                jwt = JwtHelper.decodeAndVerify(token,
                        new RsaVerifier(this.ssoPublicKey)
                );
            } catch (InvalidSignatureException e) {
                throw new ZuulRuntimeException(new ZuulException("0008-10004", 400, null));
            } catch (Exception e) {
                throw new ZuulRuntimeException(new ZuulException("0008-10005", 400, e.getMessage()));
            }

            String userId = null;
            String appId = null;

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                Map map = objectMapper.readValue(jwt.getClaims(), Map.class);
                userId = (String) map.get("user_name");
                appId = (String) map.get("app_id");
            } catch (Exception e) {
                logger.error("jwt json解析异常 {} {} ", e.getMessage(), jwt.getClaims());
                throw new ZuulRuntimeException(new ZuulException("0008-10006", 400, e.getMessage()));
            }

            logger.debug("token valid, {}, {}", userId, appId);

            if (!filterHelperService.skipTokenCheck()) {

                String appIdFromHeader = (String) requestContext.get(Constant.APP_APP_ID);

                //如果userid不为空，通过sso登录的方式进来，检查是否做过了sso check
                if (!StringUtils.isEmpty(userId)) {
                    if (!redisService.userTokenValid(appIdFromHeader, userId, token)) {
                        throw new ZuulRuntimeException(new ZuulException("0008-10001", 400, null));
                    }
                } else {
                    if (!redisService.appTokenValid(appId, token)) {
                        throw new ZuulRuntimeException(new ZuulException("0008-10002", 400, null));
                    }
                }
            }


            requestContext.set(Constant.USER_ID, userId);

            requestContext.addZuulRequestHeader(Constant.USER_ID, userId);

        }

        return null;
    }
}
