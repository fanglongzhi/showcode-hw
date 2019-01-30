package com.gmcc.msb.config.config;

import com.gmcc.msb.config.service.ConfigService;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.bootstrap.encrypt.KeyProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfig {
    @Autowired
    private AuthProperties authConfig;
    @Autowired
    private KeyProperties key;
    @Autowired
    private ConfigService configService;

    @Bean
    public FilterRegistrationBean companyUrlFilterRegister() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        //注入过滤器
        registration.setFilter(new AuthFilter(authConfig, configService));
        //拦截规则
        registration.addUrlPatterns("/*");
        //过滤器名称
        registration.setName("authFilter");
        //过滤器顺序
        registration.setOrder(FilterRegistrationBean.HIGHEST_PRECEDENCE);
        return registration;
    }

   /* @Bean
    @Primary
    public TextEncryptor textEncryptor() {
        Object encryptor = Encryptors.text(key.getKey(), "1234aefd1111");
        return (TextEncryptor)encryptor;
    }*/


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
