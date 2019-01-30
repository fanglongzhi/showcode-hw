package com.gmcc.msb.config.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmcc.msb.common.property.MsbProperties;
import com.gmcc.msb.common.util.RefreshUtil;
import com.gmcc.msb.common.vo.SignKey;
import com.gmcc.msb.config.config.MyProperties;
import com.gmcc.msb.config.entity.Config;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author Yuan Chunhai
 * @Date 11/7/2018-10:19 AM
 */
@Service
public class SignKeyRefreshService {

    private static final Logger log = LoggerFactory.getLogger(SignKeyRefreshService.class);

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private MsbProperties msbProperties;

    @Autowired
    private MyProperties myProperties;

    @Autowired
    private ConfigService configService;

    @Value("${spring.profiles.active:local}")
    private String profile;


    public void refresh(String serviceId){

        log.info("查询{}的签名key,profile:{}", serviceId, profile);
        List<Config> list = configService.getServiceSignKey(serviceId, profile);
        if (list != null && !list.isEmpty()){
            Config config = list.get(0);

            if (config.getUpdateTime() != null &&
                        DateUtils.addHours(config.getUpdateTime(), +1).before(new Date())){
                log.warn("最近更新时间在1小时前，不更新{}签名，profile{}", serviceId, profile);
                return;
            }

            RefreshUtil refreshUtil = new RefreshUtil(discoveryClient, "msb-zuul", msbProperties);
            log.debug("{}签名key {}", config.getApplication(), config.getPropertyValue());
            ObjectMapper objectMapper = new ObjectMapper();
            SignKey signKey = new SignKey();
            signKey.setServiceId(serviceId);
            signKey.setKey(config.getPropertyValue());
            String jsonBodyStr = null;
            try {
                jsonBodyStr = objectMapper.writeValueAsString(signKey);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            refreshUtil.refresh(myProperties.getSignKeyRefreshPath(), jsonBodyStr, "刷新签名key",
                    "0003-10013", "0003-10014", "0003-10015", "0003-10016");
        }
        else {
            log.info("查询{}的签名key,profile:{}为空");
        }
    }

}
