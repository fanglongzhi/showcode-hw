package com.msb.join.demo.utils;

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

    private static final Logger log = LoggerFactory.getLogger(SignUtils.class);

    private SignUtils() {
    }



    public static String signRequest(String url, String paramStr, String key) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(url).append(",")
                .append(paramStr).append(",")
                .append(key);
        log.debug("sign : {} ", stringBuilder.toString());
        return DigestUtils.sha1Hex(stringBuilder.toString());
    }

    public static String signRequest(HttpServletRequest request, String key) {

        String url = request.getRequestURI();

        StringBuilder paramsStringBuilder = new StringBuilder();
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
                paramsStringBuilder.append(name).append("=")
                        .append(request.getParameter(name))
                        .append("&");
            }
            paramsStringBuilder.deleteCharAt(paramsStringBuilder.length() - 1);
        }

        return signRequest(url, paramsStringBuilder.toString(), key);
    }
}

