package com.gmcc.msb.zuul.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPoolConfig;

import javax.sql.DataSource;


/**
 * @author Yuan Chunhai
 */
@Configuration
public class DataSourceConfig {
    private static final Logger log = LoggerFactory.getLogger(DataSourceConfig.class);

    @Bean
    @RefreshScope
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @RefreshScope
    @ConfigurationProperties(prefix = "spring.redis")
    public RedisConnectionFactory dynamicConfiguration(RedisConfig redisConfig, RedisProperties redisProperties) {
        log.debug("==========create redis factory");
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setPort(redisConfig.getRedisPort());
        jedisConnectionFactory.setHostName(redisConfig.getRedisHost());
        jedisConnectionFactory.setPassword(redisConfig.getPassword());
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        if (redisProperties != null && redisProperties.getPool() != null) {
            poolConfig.setMaxTotal(redisProperties.getPool().getMaxActive());
            poolConfig.setMaxIdle(redisProperties.getPool().getMaxIdle());
            poolConfig.setMinIdle(redisProperties.getPool().getMinIdle());
        }
        jedisConnectionFactory.setPoolConfig(poolConfig);
        jedisConnectionFactory.afterPropertiesSet();
        return jedisConnectionFactory;
    }
}
