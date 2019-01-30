package com.gmcc.msb.common.util;

import feign.RequestTemplate;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author Yuan Chunhai
 * @Date 9/28/2018-5:02 PM
 */
public class SignUtils {

    private static final Logger log = LoggerFactory.getLogger(SignUtils.class);

    private SignUtils() {
    }


    public static String signFeignRequest(RequestTemplate requestTemplate, String key) {
        String url = requestTemplate.url();
        StringBuilder stringBuilder = new StringBuilder();
        Map<String, Collection<String>> map = requestTemplate.queries();

        if (!map.isEmpty()) {
            List<String> keyList = new ArrayList<String>(map.keySet());
            Collections.sort(keyList);
            for (String name : keyList) {
                stringBuilder.append(name).append("=")
                        .append(((List) map.get(name)).get(0))
                        .append("&");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }

        return signRequest(url, stringBuilder.toString(), key);
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

