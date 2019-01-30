package com.gmcc.msb.demo.provider.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmcc.msb.demo.provider.vo.ErrorCode;
import com.gmcc.msb.demo.provider.property.MsbProperties;
import com.gmcc.msb.demo.provider.util.SignUtils;
import com.gmcc.msb.demo.provider.vo.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @author sirui.lin
 */
public class SignCheckFilter extends HandlerInterceptorAdapter {

    private static final Logger log = LoggerFactory.getLogger(SignCheckFilter.class);

    private final MsbProperties msbProperties;
    private final String applicationName;

    private final static String HEADER_SIGN = "sign";

    private List<String> skipSignCheckUri = Arrays.asList("/error");

    public SignCheckFilter(MsbProperties msbProperties, String applicationName) {
        this.msbProperties = msbProperties;
        this.applicationName = applicationName;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String sign = request.getHeader(HEADER_SIGN);
        String verifySign = SignUtils.signRequest(request, msbProperties.getSignKey());

        log.debug("sign {} {} ", sign, verifySign);

        if (!skipSignCheckUri.contains(request.getRequestURI())&&(
                StringUtils.isEmpty(sign) || !sign.equals(verifySign)
        )) {
            //TODO 调用MSB错误代码API
            ErrorCode errorCode = new ErrorCode(msbProperties.getServiceCode() + "-11001", "签名错误", "");
            ObjectMapper objectMapper = new ObjectMapper();
            String responseStr = objectMapper.writeValueAsString(Response.notOk(errorCode));
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(responseStr);
            return false;
        } else {
            return true;
        }
    }
}
