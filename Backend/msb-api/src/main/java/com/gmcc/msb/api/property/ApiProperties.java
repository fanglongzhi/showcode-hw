package com.gmcc.msb.api.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = ApiProperties.PREFIX)
@Component
public class ApiProperties {

    public static final String PREFIX = "msb.api";

    /**
     * 最大返回结果数
     */
    private int maxPageSize = 10000;

    private ApiProperties() {

    }


    public int getMaxPageSize() {
        return maxPageSize;
    }

    public void setMaxPageSize(int maxPageSize) {
        this.maxPageSize = maxPageSize;
    }
}
