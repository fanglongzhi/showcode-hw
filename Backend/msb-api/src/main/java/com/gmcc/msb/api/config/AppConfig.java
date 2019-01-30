package com.gmcc.msb.api.config;

import com.gmcc.msb.api.entity.AppOrderApi;
import com.gmcc.msb.api.vo.ApiInfo;
import com.gmcc.msb.common.config.UserInfoAware;
import com.gmcc.msb.common.entity.App;
import com.gmcc.msb.common.entity.Operator;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import net.javacrumbs.shedlock.spring.ScheduledLockConfiguration;
import net.javacrumbs.shedlock.spring.ScheduledLockConfigurationBuilder;
import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.sql.DataSource;
import java.time.Duration;

@Configuration
public class AppConfig {

    @Bean("apiInfoRedisTemplate")
    public RedisTemplate<String, ApiInfo> apiInfoRedisTemplate(RedisConnectionFactory factory) {

        RedisTemplate<String, ApiInfo> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(ApiInfo.class));

        //支持事务
        redisTemplate.setEnableTransactionSupport(true);
        return redisTemplate;
    }

    @Bean("appInfoRedisTemplate")
    public RedisTemplate<String, App> appInfoRedisTemplate(RedisConnectionFactory factory) {

        RedisTemplate<String, App> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(App.class));

        //支持事务
        redisTemplate.setEnableTransactionSupport(true);
        return redisTemplate;
    }

    @Bean("appOrderApiRedisTemplate")
    public RedisTemplate<String, AppOrderApi> appOrderApiRedisTemplate(RedisConnectionFactory factory) {

        RedisTemplate<String, AppOrderApi> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(AppOrderApi.class));


        //支持事务
        redisTemplate.setEnableTransactionSupport(true);
        return redisTemplate;
    }


    @Bean
    public LockProvider lockProvider(DataSource dataSource) {
        return new JdbcTemplateLockProvider(dataSource);
    }

    @Bean
    public ScheduledLockConfiguration scheduledLockConfiguration(LockProvider lockProvider) {
        return ScheduledLockConfigurationBuilder
                       .withLockProvider(lockProvider)
                       .withPoolSize(10)
                       .withDefaultLockAtMostFor(Duration.ofMinutes(10))
                       .build();
    }

    @Bean
    public AuditorAware<Operator> auditorProvider() {
        return new UserInfoAware();
    }


    @Bean
    public FlywayMigrationStrategy flywayMigrationStrategy() {
        return new FlywayMigrationStrategy() {
            @Override
            public void migrate(Flyway flyway) {
                flyway.repair();
                flyway.migrate();
            }
        };
    }
}
