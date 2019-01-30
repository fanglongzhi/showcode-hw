package com.gmcc.msb.zuul.service;

import com.gmcc.msb.common.service.client.MsbConfigClient;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Yuan Chunhai
 * @Date 11/6/2018-6:06 PM
 */
@Service
public class SignKeyCache {


    private static final Logger log = LoggerFactory.getLogger(SignKeyCache.class);

    private ConcurrentHashMap<String, String> signKeyCache = new ConcurrentHashMap<>();

    @Autowired
    private MsbConfigClient msbConfigClient;

    @Value("${spring.profiles.active:local}")
    private String profile;

    public String getSignKey(String serviceId) {
        String key = signKeyCache.get(serviceId);
        log.debug("签名信息： {}", key);
        if (key != null && !key.isEmpty()) {
            return key;
        }
        log.debug("签名信息： {}", serviceId);
        return serviceId;
    }

    public void setSignKey(String serviceId, String signKey) {
        log.debug("serviceId : {} , signKey : {}", serviceId, signKey);
        signKeyCache.put(serviceId, signKey);
    }

    public boolean refresh() {
        try {
            Map<String, String> map = msbConfigClient.getServiceSignKeyConfigs(profile).getContent();
            signKeyCache = new ConcurrentHashMap<>(map);
            log.debug("签名key:{}", signKeyCache);
            return true;
        } catch (Exception e) {
            log.error("获取sign信息失败", ExceptionUtils.getMessage(e));
        }
        return false;
    }

}
