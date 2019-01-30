package com.gmcc.msb.msbservice.config;

import com.gmcc.msb.common.config.UserInfoAware;
import com.gmcc.msb.common.entity.Operator;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import net.javacrumbs.shedlock.spring.ScheduledLockConfiguration;
import net.javacrumbs.shedlock.spring.ScheduledLockConfigurationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import javax.sql.DataSource;
import java.time.Duration;
/**
 * @author zhi fanglong
 */
@Configuration
public class AppConfig {

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

}
