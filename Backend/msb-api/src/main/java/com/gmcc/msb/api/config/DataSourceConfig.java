package com.gmcc.msb.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import javax.sql.DataSource;

/**
 * @author Zhi Fanglong
 */
@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = {"com.gmcc.msb.api.repository"})
public class DataSourceConfig {
    @Autowired
    private RedisConfig redisConfig;

    @Bean
    @RefreshScope
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @RefreshScope
    @ConfigurationProperties(prefix = "spring.redis")
    public RedisConnectionFactory dynamicConfiguration() {
        JedisConnectionFactory jedisConnectionFactory = new  JedisConnectionFactory();
        jedisConnectionFactory.setPort(redisConfig.getRedisPort());
        jedisConnectionFactory.setHostName(redisConfig.getRedisHost());
        jedisConnectionFactory.setPassword(redisConfig.getPassword());
        jedisConnectionFactory.afterPropertiesSet();
        return jedisConnectionFactory;
    }

}
