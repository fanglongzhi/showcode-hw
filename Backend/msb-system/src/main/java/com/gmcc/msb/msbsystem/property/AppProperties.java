package com.gmcc.msb.msbsystem.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import static com.gmcc.msb.msbsystem.property.AppProperties.PREFIX;

@RefreshScope
@ConfigurationProperties(prefix = PREFIX)
@Component
public class AppProperties {

    public static final String PREFIX = "msb.system";


    private String eomsServiceUrl;
    private int reqEomsConnectionTime = 3000;
    private int reqEomsReadTime = 20000;
    private String reqEomsFields = "orgid,orgcode,orgname,parentorgid,orgaddr";


    public String getEomsServiceUrl() {
        return eomsServiceUrl;
    }

    public void setEomsServiceUrl(String eomsServiceUrl) {
        this.eomsServiceUrl = eomsServiceUrl;
    }

    public int getReqEomsConnectionTime() {
        return reqEomsConnectionTime;
    }

    public void setReqEomsConnectionTime(int reqEomsConnectionTime) {
        this.reqEomsConnectionTime = reqEomsConnectionTime;
    }

    public int getReqEomsReadTime() {
        return reqEomsReadTime;
    }

    public void setReqEomsReadTime(int reqEomsReadTime) {
        this.reqEomsReadTime = reqEomsReadTime;
    }

    public String getReqEomsFields() {
        return reqEomsFields;
    }

    public void setReqEomsFields(String reqEomsFields) {
        this.reqEomsFields = reqEomsFields;
    }
}
