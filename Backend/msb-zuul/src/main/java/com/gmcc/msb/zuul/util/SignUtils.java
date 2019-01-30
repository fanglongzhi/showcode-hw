package com.gmcc.msb.zuul.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * @author Yuan Chunhai
 * @Date 9/28/2018-5:02 PM
 */
public class SignUtils {

    private SignUtils() {
    }

    private static final Logger log = LoggerFactory.getLogger(SignUtils.class);

    public static String signRequest(HttpServletRequest request, String key, String requestUri) {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(requestUri).append(",");

        // 1. 获取url中的参数
        //获取所有的请求参数param name
        //对param排序
        //拼接param=paramvalue&
        Enumeration<String> names = request.getParameterNames();
        List<String> nameList = new ArrayList<>();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            nameList.add(name);
        }

        if (!nameList.isEmpty()) {
            Collections.sort(nameList);
            for (String name : nameList) {
                stringBuilder.append(name).append("=")
                        .append(request.getParameter(name))
                        .append("&");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        stringBuilder.append(",");
        stringBuilder.append(key);

        String sign = DigestUtils.sha1Hex(stringBuilder.toString());
        log.debug("sign str {} {} ", sign, stringBuilder);

        return sign;
    }

    public static String signRequest(HttpServletRequest request, String key) {
        return signRequest(request, key, request.getRequestURI());
    }
}

