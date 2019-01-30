package com.gmcc.msb.msbservice.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;
/**
 * @author zhi fanglong
 */
@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = {"com.gmcc.msb.msbservice.repository"})
public class DataSourceConfig {
    @Bean
    @RefreshScope
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }
}
