package com.gmcc.msb.demo.provider.property;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import static com.gmcc.msb.demo.provider.property.MsbProperties.PREFIX;

/**
 * @author sirui.lin
 */
@Component
@ConfigurationProperties(prefix = PREFIX)
@RefreshScope
public class MsbProperties {

    public static final String PREFIX = "msb";
    private String signKey = "demo-service-provider-sign-key";
    private String serviceCode = "0090";

    public String getSignKey() {
        return signKey;
    }

    public void setSignKey(String signKey) {
        this.signKey = signKey;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }
}
