package com.gmcc.msb.zuul.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author Yuan Chunhai
 */
@Component
@RefreshScope
public class RedisConfig {
    @Value("${spring.redis.host:localhost}")
    private String redisHost;
    @Value("${spring.redis.port:6379}")
    private Integer redisPort;
    @Value("${spring.redis.password:}")
    private String password;

    public String getRedisHost() {
        return redisHost;
    }

    public void setRedisHost(String redisHost) {
        this.redisHost = redisHost;
    }

    public Integer getRedisPort() {
        return redisPort;
    }

    public void setRedisPort(Integer redisPort) {
        this.redisPort = redisPort;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
