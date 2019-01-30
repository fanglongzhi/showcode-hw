package com.gmcc.msb.config.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@RefreshScope
@ConfigurationProperties("auth.url.exclude")
@Component
public class AuthProperties {

    private List<String> pattern;

    public List<String> getPattern() {
        if(pattern==null) {
            pattern = new ArrayList<>();
        }
        return pattern;
    }

    public void setPattern(List<String> pattern) {
        this.pattern = pattern;
    }
}
