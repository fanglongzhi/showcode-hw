package com.gmcc.msb.msbsystem.config;

import com.gmcc.msb.common.config.UserInfoAware;
import com.gmcc.msb.common.entity.Operator;
import com.gmcc.msb.msbsystem.repository.org.DataOrgUserRepository;
import com.gmcc.msb.msbsystem.service.DataOrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.gmcc.msb.msbsystem.repository")
public class MyConfig {
    @Autowired
    private DataOrgService dataOrgService;
    @Autowired
    private DataOrgUserRepository dataOrgUserRepository;
    @Bean
    public AuditorAware<Operator> auditorProvider() {
        return new UserInfoAware();
    }
}
